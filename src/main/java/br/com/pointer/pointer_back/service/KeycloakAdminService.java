package br.com.pointer.pointer_back.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.pointer.pointer_back.dto.KeycloakResponseDTO;
import br.com.pointer.pointer_back.exception.KeycloakException;
import br.com.pointer.pointer_back.exception.UsuarioJaExisteException;
import br.com.pointer.pointer_back.util.UserUtil;
import br.com.pointer.pointer_back.util.ValidationUtil;
import jakarta.ws.rs.core.Response;

@Service
public class KeycloakAdminService {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakAdminService.class);
    private static final int STATUS_CREATED = 201;

    private Keycloak keycloak;
    private final String realm;

    public KeycloakAdminService(
            Keycloak keycloak,
            @Value("${keycloak.realm}") String realm) {
        this.keycloak = keycloak;
        this.realm = realm;
    }

    // Método para testes
    void setKeycloak(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    /**
     * Cria um usuário no Keycloak e retorna o ID.
     */
    public KeycloakResponseDTO createUserAndReturnId(String nome, String email, String senha) {
        ValidationUtil.validarNome(nome);
        ValidationUtil.validarEmail(email);
        ValidationUtil.validarSenha(senha);

        try {
            RealmResource realmResource = obterRealmResource();
            UsersResource usersResource = realmResource.users();

            verificarUsuarioExistente(usersResource, email);

            String[] nomeESobrenome = UserUtil.extrairNomeESobrenome(nome);
            UserRepresentation user = UserUtil.construirUserRepresentation(nomeESobrenome[0], nomeESobrenome[1], email);

            return criarUsuario(usersResource, user, senha);
        } catch (Exception e) {
            logger.error("Exceção ao criar usuário no Keycloak: {}", e.getMessage(), e);
            if (e instanceof KeycloakException) {
                throw e;
            }
            throw new KeycloakException("Erro ao criar usuário: " + e.getMessage(), 500, "KEYCLOAK_ERROR");
        }
    }

    /**
     * Define a senha do usuário no Keycloak.
     */
    public KeycloakResponseDTO setUserPassword(String userId, String password) {
        ValidationUtil.validarUserId(userId);
        ValidationUtil.validarSenha(password);

        try {
            RealmResource realmResource = obterRealmResource();
            CredentialRepresentation credential = criarCredential(password);

            realmResource.users().get(userId).resetPassword(credential);
            logger.info("Senha definida para usuário Keycloak ID: {}", userId);
            return KeycloakResponseDTO.success("Senha definida com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao definir senha do usuário: {}", e.getMessage(), e);
            throw new KeycloakException("Erro ao definir senha do usuário: " + e.getMessage(), 500,
                    "KEYCLOAK_PASSWORD_ERROR");
        }
    }

    /**
     * Atribui roles ao usuário no Keycloak.
     */
    public KeycloakResponseDTO assignRolesToUser(String userId, Set<String> roles) {
        ValidationUtil.validarUserId(userId);
        ValidationUtil.validarRoles(roles);

        try {
            RealmResource realmResource = obterRealmResource();
            List<RoleRepresentation> roleRepresentations = obterRoleRepresentations(realmResource, roles);

            realmResource.users().get(userId).roles().realmLevel().add(roleRepresentations);
            logger.info("Roles atribuídas ao usuário Keycloak ID: {}: {}", userId, roles);
            return KeycloakResponseDTO.success("Roles atribuídas com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao atribuir roles ao usuário: {}", e.getMessage(), e);
            throw new KeycloakException("Erro ao atribuir roles ao usuário: " + e.getMessage(), 500,
                    "KEYCLOAK_ROLE_ERROR");
        }
    }

    public KeycloakResponseDTO disableUser(String email) {
        ValidationUtil.validarEmail(email);
        try {
            RealmResource realmResource = obterRealmResource();
            UserRepresentation user = buscarUsuarioPorEmail(realmResource, email);

            user.setEnabled(false);
            atualizarUsuario(realmResource, user);
            fazerLogoutUsuario(realmResource, user.getId());

            logger.info("Usuário desativado no Keycloak: {}", email);
            return KeycloakResponseDTO.success("Usuário desativado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao desativar usuário: {}", e.getMessage(), e);
            throw new KeycloakException("Erro ao desativar usuário: " + e.getMessage(), 500, "KEYCLOAK_DISABLE_ERROR");
        }
    }

    public KeycloakResponseDTO enableUser(String email) {
        ValidationUtil.validarEmail(email);
        try {
            RealmResource realmResource = obterRealmResource();
            UserRepresentation user = buscarUsuarioPorEmail(realmResource, email);

            user.setEnabled(true);
            atualizarUsuario(realmResource, user);

            logger.info("Usuário ativado no Keycloak: {}", email);
            return KeycloakResponseDTO.success("Usuário ativado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao ativar usuário: {}", e.getMessage(), e);
            throw new KeycloakException("Erro ao ativar usuário: " + e.getMessage(), 500, "KEYCLOAK_ENABLE_ERROR");
        }
    }

    public KeycloakResponseDTO updateUser(String userId, UserRepresentation user) {
        ValidationUtil.validarUserId(userId);
        try {
            RealmResource realmResource = obterRealmResource();
            atualizarUsuario(realmResource, user);

            logger.info("Usuário atualizado no Keycloak: {}", userId);
            return KeycloakResponseDTO.success("Usuário atualizado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao atualizar usuário: {}", e.getMessage(), e);
            throw new KeycloakException("Erro ao atualizar usuário: " + e.getMessage(), 500, "KEYCLOAK_UPDATE_ERROR");
        }
    }

    public KeycloakResponseDTO removeRolesFromUser(String userId, Set<String> roles) {
        ValidationUtil.validarUserId(userId);
        ValidationUtil.validarRoles(roles);
        try {
            RealmResource realmResource = obterRealmResource();
            List<RoleRepresentation> roleRepresentations = obterRoleRepresentations(realmResource, roles);

            realmResource.users().get(userId).roles().realmLevel().remove(roleRepresentations);
            logger.info("Roles removidas do usuário Keycloak ID: {}: {}", userId, roles);
            return KeycloakResponseDTO.success("Roles removidas com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao remover roles do usuário: {}", e.getMessage(), e);
            throw new KeycloakException("Erro ao remover roles do usuário: " + e.getMessage(), 500,
                    "KEYCLOAK_ROLE_REMOVE_ERROR");
        }
    }

    public KeycloakResponseDTO updatePassword(String userId, String password) {
        ValidationUtil.validarUserId(userId);
        ValidationUtil.validarSenha(password);
        try {
            RealmResource realmResource = obterRealmResource();
            UserResource userResource = realmResource.users().get(userId);

            verificarUsuarioExiste(userResource);

            CredentialRepresentation credential = criarCredential(password);
            resetarSenhaUsuario(userResource, credential);

            logger.info("Senha atualizada para usuário Keycloak ID: {}", userId);
            return KeycloakResponseDTO.success("Senha atualizada com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao atualizar senha: {}", e.getMessage(), e);
            throw new KeycloakException("Erro ao atualizar senha: " + e.getMessage(), 500,
                    "KEYCLOAK_PASSWORD_UPDATE_ERROR");
        }
    }

    private RealmResource obterRealmResource() {
        return keycloak.realm(realm);
    }

    private void verificarUsuarioExistente(UsersResource usersResource, String email) {
        if (!usersResource.search(email).isEmpty()) {
            throw new UsuarioJaExisteException("Já existe um usuário com este email");
        }
    }

    private KeycloakResponseDTO criarUsuario(UsersResource usersResource, UserRepresentation user, String senha) {
        try (Response response = usersResource.create(user)) {
            if (response.getStatus() == STATUS_CREATED) {
                String userId = extrairUserId(response);
                setUserPassword(userId, senha);
                logger.info("Usuário criado no Keycloak com ID: {}", userId);
                return KeycloakResponseDTO.success("Usuário criado com sucesso", userId);
            } else {
                String error = response.readEntity(String.class);
                logger.error("Erro ao criar usuário no Keycloak. Status: {}. Erro: {}", response.getStatus(), error);
                throw new KeycloakException("Erro ao criar usuário no Keycloak", response.getStatus(),
                        "KEYCLOAK_CREATE_ERROR");
            }
        }
    }

    private String extrairUserId(Response response) {
        String location = response.getHeaderString("Location");
        return location.substring(location.lastIndexOf('/') + 1);
    }

    private CredentialRepresentation criarCredential(String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        return credential;
    }

    private List<RoleRepresentation> obterRoleRepresentations(RealmResource realmResource, Set<String> roles) {
        return roles.stream()
                .map(roleName -> obterRoleRepresentation(realmResource, roleName))
                .collect(Collectors.toList());
    }

    private RoleRepresentation obterRoleRepresentation(RealmResource realmResource, String roleName) {
        RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();
        if (role == null) {
            throw new KeycloakException("Role não encontrada: " + roleName, 404, "ROLE_NOT_FOUND");
        }
        return role;
    }

    private UserRepresentation buscarUsuarioPorEmail(RealmResource realmResource, String email) {
        return realmResource.users().search(email).stream()
                .findFirst()
                .orElseThrow(() -> new KeycloakException("Usuário não encontrado: " + email, 404, "USER_NOT_FOUND"));
    }

    private void atualizarUsuario(RealmResource realmResource, UserRepresentation user) {
        realmResource.users().get(user.getId()).update(user);
    }

    private void fazerLogoutUsuario(RealmResource realmResource, String userId) {
        realmResource.users().get(userId).logout();
    }

    private void verificarUsuarioExiste(UserResource userResource) {
        UserRepresentation user = userResource.toRepresentation();
        if (user == null) {
            throw new KeycloakException("Usuário não encontrado", 404, "USER_NOT_FOUND");
        }
    }

    private void resetarSenhaUsuario(UserResource userResource, CredentialRepresentation credential) {
        try {
            userResource.resetPassword(credential);
        } catch (jakarta.ws.rs.WebApplicationException wae) {
            String errorMessage = wae.getResponse().readEntity(String.class);
            logger.error("Erro ao resetar senha: Status {} - {}", wae.getResponse().getStatus(), errorMessage);
            throw new KeycloakException("Erro ao resetar senha", wae.getResponse().getStatus(),
                    "KEYCLOAK_PASSWORD_RESET_ERROR");
        }
    }
}
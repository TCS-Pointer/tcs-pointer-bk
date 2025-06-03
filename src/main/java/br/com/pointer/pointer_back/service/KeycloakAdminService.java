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
import br.com.pointer.pointer_back.exception.EmailInvalidoException;
import br.com.pointer.pointer_back.exception.KeycloakException;
import br.com.pointer.pointer_back.exception.SenhaInvalidaException;
import br.com.pointer.pointer_back.exception.UsuarioJaExisteException;
import jakarta.ws.rs.core.Response;

@Service
public class KeycloakAdminService {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakAdminService.class);
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
     * 
     * @param nome  Nome completo do usuário
     * @param email Email do usuário
     * @param senha Senha do usuário
     * @return ID do usuário criado no Keycloak
     * @throws KeycloakException se houver erro na criação do usuário
     */
    public KeycloakResponseDTO createUserAndReturnId(String nome, String email, String senha) {
        validarNome(nome);
        validarEmail(email);
        validarSenha(senha);

        try {
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();

            // Verifica se já existe usuário com este email
            if (!usersResource.search(email).isEmpty()) {
                throw new UsuarioJaExisteException("Já existe um usuário com este email");
            }

            String[] nomeESobrenome = extrairNomeESobrenome(nome);
            UserRepresentation user = construirUserRepresentation(nomeESobrenome[0], nomeESobrenome[1], email);

            try (Response response = usersResource.create(user)) {
                if (response.getStatus() == 201) {
                    String location = response.getHeaderString("Location");
                    String userId = location.substring(location.lastIndexOf('/') + 1);

                    setUserPassword(userId, senha);
                    logger.info("Usuário criado no Keycloak com ID: {}", userId);
                    return KeycloakResponseDTO.success("Usuário criado com sucesso", userId);
                } else {
                    String error = response.readEntity(String.class);
                    logger.error("Erro ao criar usuário no Keycloak. Status: {}. Erro: {}", response.getStatus(),
                            error);
                    throw new KeycloakException(
                            "Erro ao criar usuário no Keycloak",
                            response.getStatus(),
                            "KEYCLOAK_CREATE_ERROR");
                }
            }
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
     * 
     * @param userId   ID do usuário
     * @param password Senha a ser definida
     * @throws KeycloakException se houver erro ao definir a senha
     */
    public KeycloakResponseDTO setUserPassword(String userId, String password) {
        validarUserId(userId);
        validarSenha(password);

        try {
            RealmResource realmResource = keycloak.realm(realm);
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(password);
            credential.setTemporary(false);

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
     * 
     * @param userId ID do usuário
     * @param roles  Conjunto de roles
     * @throws KeycloakException se houver erro ao atribuir as roles
     */
    public KeycloakResponseDTO assignRolesToUser(String userId, Set<String> roles) {
        validarUserId(userId);
        validarRoles(roles);

        try {
            RealmResource realmResource = keycloak.realm(realm);
            List<RoleRepresentation> roleRepresentations = roles.stream()
                    .map(roleName -> {
                        RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();
                        if (role == null) {
                            throw new KeycloakException("Role não encontrada: " + roleName, 404, "ROLE_NOT_FOUND");
                        }
                        return role;
                    })
                    .collect(Collectors.toList());

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
        validarEmail(email);
        try {
            RealmResource realmResource = keycloak.realm(realm);
            UserRepresentation user = realmResource.users().search(email).stream()
                    .findFirst()
                    .orElseThrow(
                            () -> new KeycloakException("Usuário não encontrado: " + email, 404, "USER_NOT_FOUND"));

            user.setEnabled(false);
            realmResource.users().get(user.getId()).update(user);
            realmResource.users().get(user.getId()).logout();
            logger.info("Usuário desativado no Keycloak: {}", email);
            return KeycloakResponseDTO.success("Usuário desativado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao desativar usuário: {}", e.getMessage(), e);
            throw new KeycloakException("Erro ao desativar usuário: " + e.getMessage(), 500, "KEYCLOAK_DISABLE_ERROR");
        }
    }

    public KeycloakResponseDTO enableUser(String email) {
        validarEmail(email);
        try {
            RealmResource realmResource = keycloak.realm(realm);
            UserRepresentation user = realmResource.users().search(email).stream()
                    .findFirst()
                    .orElseThrow(
                            () -> new KeycloakException("Usuário não encontrado: " + email, 404, "USER_NOT_FOUND"));

            user.setEnabled(true);
            realmResource.users().get(user.getId()).update(user);
            logger.info("Usuário ativado no Keycloak: {}", email);
            return KeycloakResponseDTO.success("Usuário ativado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao ativar usuário: {}", e.getMessage(), e);
            throw new KeycloakException("Erro ao ativar usuário: " + e.getMessage(), 500, "KEYCLOAK_ENABLE_ERROR");
        }
    }

    public KeycloakResponseDTO updateUser(String userId, UserRepresentation user) {
        validarUserId(userId);
        try {
            RealmResource realmResource = keycloak.realm(realm);
            realmResource.users().get(userId).update(user);
            logger.info("Usuário atualizado no Keycloak: {}", userId);
            return KeycloakResponseDTO.success("Usuário atualizado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao atualizar usuário: {}", e.getMessage(), e);
            throw new KeycloakException("Erro ao atualizar usuário: " + e.getMessage(), 500, "KEYCLOAK_UPDATE_ERROR");
        }
    }

    public KeycloakResponseDTO removeRolesFromUser(String userId, Set<String> roles) {
        validarUserId(userId);
        validarRoles(roles);
        try {
            RealmResource realmResource = keycloak.realm(realm);
            List<RoleRepresentation> roleRepresentations = roles.stream()
                    .map(roleName -> {
                        RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();
                        if (role == null) {
                            throw new KeycloakException("Role não encontrada: " + roleName, 404, "ROLE_NOT_FOUND");
                        }
                        return role;
                    })
                    .collect(Collectors.toList());

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
        validarUserId(userId);
        validarSenha(password);
        try {
            RealmResource realmResource = keycloak.realm(realm);
            UserResource userResource = realmResource.users().get(userId);
            UserRepresentation user = userResource.toRepresentation();
            if (user == null) {
                throw new KeycloakException("Usuário não encontrado com o ID: " + userId, 404, "USER_NOT_FOUND");
            }
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(password);
            credential.setTemporary(false);
            try {
                userResource.resetPassword(credential);
                logger.info("Senha atualizada para usuário Keycloak ID: {}", userId);
                return KeycloakResponseDTO.success("Senha atualizada com sucesso");
            } catch (jakarta.ws.rs.WebApplicationException wae) {
                String errorMessage = wae.getResponse().readEntity(String.class);
                logger.error("Erro ao resetar senha: Status {} - {}", wae.getResponse().getStatus(), errorMessage);
                throw new KeycloakException(
                        "Erro ao resetar senha",
                        wae.getResponse().getStatus(),
                        "KEYCLOAK_PASSWORD_RESET_ERROR");
            }
        } catch (Exception e) {
            logger.error("Erro ao atualizar senha: {}", e.getMessage(), e);
            throw new KeycloakException("Erro ao atualizar senha: " + e.getMessage(), 500,
                    "KEYCLOAK_PASSWORD_UPDATE_ERROR");
        }
    }

    // Métodos privados utilitários para validação e construção
    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new KeycloakException("Nome não pode ser vazio", 400, "INVALID_NAME");
        }
    }

    private void validarEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new EmailInvalidoException("Email inválido");
        }
    }

    private void validarSenha(String senha) {
        if (senha == null || senha.length() < 8) {
            throw new SenhaInvalidaException("Senha deve ter pelo menos 8 caracteres");
        }
    }

    private void validarUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new KeycloakException("ID do usuário não pode ser vazio", 400, "INVALID_USER_ID");
        }
    }

    private void validarRoles(Set<String> roles) {
        if (roles == null || roles.isEmpty()) {
            throw new KeycloakException("Ao menos uma role deve ser especificada", 400, "INVALID_ROLES");
        }
    }

    private String[] extrairNomeESobrenome(String nomeCompleto) {
        String[] partes = nomeCompleto.trim().split(" ", 2);
        String nome = partes[0];
        String sobrenome = partes.length > 1 ? partes[1] : "";
        return new String[] { nome, sobrenome };
    }

    private UserRepresentation construirUserRepresentation(String nome, String sobrenome, String email) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(email);
        user.setEmail(email);
        user.setFirstName(nome);
        user.setLastName(sobrenome);
        user.setEnabled(true);
        user.setEmailVerified(false);
        return user;
    }
}
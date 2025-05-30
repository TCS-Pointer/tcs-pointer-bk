package br.com.pointer.pointer_back.service;

import br.com.pointer.pointer_back.exception.EmailInvalidoException;
import br.com.pointer.pointer_back.exception.KeycloakException;
import br.com.pointer.pointer_back.exception.SenhaInvalidaException;
import br.com.pointer.pointer_back.exception.UsuarioJaExisteException;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
     * @param nome Nome completo do usuário
     * @param email Email do usuário
     * @param senha Senha do usuário
     * @return ID do usuário criado no Keycloak
     * @throws KeycloakException se houver erro na criação do usuário
     */
    public String createUserAndReturnId(String nome, String email, String senha) {
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
                    return userId;
                } else {
                    String error = response.readEntity(String.class);
                    logger.error("Erro ao criar usuário no Keycloak. Status: {}. Erro: {}", response.getStatus(), error);
                    throw new KeycloakException(
                            "Erro ao criar usuário no Keycloak. Status: " + response.getStatus() + ". Erro: " + error);
                }
            }
        } catch (Exception e) {
            logger.error("Exceção ao criar usuário no Keycloak: {}", e.getMessage(), e);
            if (e instanceof KeycloakException || e instanceof UsuarioJaExisteException ||
                    e instanceof EmailInvalidoException || e instanceof SenhaInvalidaException) {
                throw e;
            }
            throw new KeycloakException("Erro ao criar usuário: " + e.getMessage(), e);
        }
    }

    /**
     * Define a senha do usuário no Keycloak.
     * @param userId ID do usuário
     * @param password Senha a ser definida
     * @throws KeycloakException se houver erro ao definir a senha
     */
    public void setUserPassword(String userId, String password) {
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
        } catch (Exception e) {
            logger.error("Erro ao definir senha do usuário: {}", e.getMessage(), e);
            if (e instanceof SenhaInvalidaException) {
                throw e;
            }
            throw new KeycloakException("Erro ao definir senha do usuário: " + e.getMessage(), e);
        }
    }

    /**
     * Atribui roles ao usuário no Keycloak.
     * @param userId ID do usuário
     * @param roles Conjunto de roles
     * @throws KeycloakException se houver erro ao atribuir as roles
     */
    public void assignRolesToUser(String userId, Set<String> roles) {
        validarUserId(userId);
        validarRoles(roles);

        try {
            RealmResource realmResource = keycloak.realm(realm);
            List<RoleRepresentation> roleRepresentations = roles.stream()
                    .map(roleName -> {
                        RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();
                        if (role == null) {
                            throw new IllegalArgumentException("Role não encontrada: " + roleName);
                        }
                        return role;
                    })
                    .collect(Collectors.toList());

            realmResource.users().get(userId).roles().realmLevel().add(roleRepresentations);
            logger.info("Roles atribuídas ao usuário Keycloak ID: {}: {}", userId, roles);
        } catch (Exception e) {
            logger.error("Erro ao atribuir roles ao usuário: {}", e.getMessage(), e);
            if (e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new KeycloakException("Erro ao atribuir roles ao usuário: " + e.getMessage(), e);
        }
    }

    public void disableUser(String email) {
        validarEmail(email);
        try {
            RealmResource realmResource = keycloak.realm(realm);
            UserRepresentation user = realmResource.users().search(email).stream()
                    .findFirst()
                    .orElseThrow(() -> new KeycloakException("Usuário não encontrado: " + email));

            user.setEnabled(false);
            realmResource.users().get(user.getId()).update(user);
            realmResource.users().get(user.getId()).logout();
            logger.info("Usuário desativado no Keycloak: {}", email);
        } catch (Exception e) {
            logger.error("Erro ao desativar usuário: {}", e.getMessage(), e);
            throw new KeycloakException("Erro ao desativar usuário: " + e.getMessage(), e);
        }
    }

    public void enableUser(String email) {
        validarEmail(email);
        try {
            RealmResource realmResource = keycloak.realm(realm);
            UserRepresentation user = realmResource.users().search(email).stream()
                    .findFirst()
                    .orElseThrow(() -> new KeycloakException("Usuário não encontrado: " + email));

            user.setEnabled(true);
            realmResource.users().get(user.getId()).update(user);
            logger.info("Usuário ativado no Keycloak: {}", email);
        } catch (Exception e) {
            logger.error("Erro ao ativar usuário: {}", e.getMessage(), e);
            throw new KeycloakException("Erro ao ativar usuário: " + e.getMessage(), e);
        }
    }

    public void updateUser(String userId, UserRepresentation user) {
        validarUserId(userId);
        try {
            RealmResource realmResource = keycloak.realm(realm);
            realmResource.users().get(userId).update(user);
            logger.info("Usuário atualizado no Keycloak: {}", userId);
        } catch (Exception e) {
            logger.error("Erro ao atualizar usuário: {}", e.getMessage(), e);
            throw new KeycloakException("Erro ao atualizar usuário: " + e.getMessage(), e);
        }
    }

    public void removeRolesFromUser(String userId, Set<String> roles) {
        validarUserId(userId);
        validarRoles(roles);
        try {
            RealmResource realmResource = keycloak.realm(realm);
            List<RoleRepresentation> roleRepresentations = roles.stream()
                    .map(roleName -> {
                        RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();
                        if (role == null) {
                            throw new IllegalArgumentException("Role não encontrada: " + roleName);
                        }
                        return role;
                    })
                    .collect(Collectors.toList());

            realmResource.users().get(userId).roles().realmLevel().remove(roleRepresentations);
            logger.info("Roles removidas do usuário Keycloak ID: {}: {}", userId, roles);
        } catch (Exception e) {
            logger.error("Erro ao remover roles do usuário: {}", e.getMessage(), e);
            if (e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new KeycloakException("Erro ao remover roles do usuário: " + e.getMessage(), e);
        }
    }

    public void updatePassword(String userId, String password) {
        validarUserId(userId);
        validarSenha(password);
        try {
            RealmResource realmResource = keycloak.realm(realm);
            UserResource userResource = realmResource.users().get(userId);
            UserRepresentation user = userResource.toRepresentation();
            if (user == null) {
                throw new KeycloakException("Usuário não encontrado com o ID: " + userId);
            }
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(password);
            credential.setTemporary(false);
            try {
                userResource.resetPassword(credential);
                logger.info("Senha atualizada para usuário Keycloak ID: {}", userId);
            } catch (jakarta.ws.rs.WebApplicationException wae) {
                String errorMessage = wae.getResponse().readEntity(String.class);
                logger.error("Erro ao resetar senha: Status {} - {}", wae.getResponse().getStatus(), errorMessage);
                throw new KeycloakException("Erro ao resetar senha: Status " + wae.getResponse().getStatus() + " - " + errorMessage);
            }
        } catch (Exception e) {
            logger.error("Erro ao atualizar senha: {}", e.getMessage(), e);
            if (e instanceof KeycloakException) {
                throw e;
            }
            throw new KeycloakException("Erro ao atualizar senha: " + e.getMessage(), e);
        }
    }

    // Métodos privados utilitários para validação e construção
    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
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
            throw new IllegalArgumentException("ID do usuário não pode ser vazio");
        }
    }

    private void validarRoles(Set<String> roles) {
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Ao menos uma role deve ser especificada");
        }
    }

    private String[] extrairNomeESobrenome(String nomeCompleto) {
        String[] partes = nomeCompleto.trim().split(" ", 2);
        String nome = partes[0];
        String sobrenome = partes.length > 1 ? partes[1] : "";
        return new String[]{nome, sobrenome};
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
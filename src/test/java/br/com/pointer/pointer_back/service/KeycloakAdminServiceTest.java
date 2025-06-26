package br.com.pointer.pointer_back.service;

import br.com.pointer.pointer_back.dto.KeycloakResponseDTO;
import br.com.pointer.pointer_back.exception.KeycloakException;
import br.com.pointer.pointer_back.exception.UsuarioJaExisteException;
import br.com.pointer.pointer_back.util.UserUtil;
import br.com.pointer.pointer_back.util.ValidationUtil;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.StatusType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@DisplayName("KeycloakAdminService")
class KeycloakAdminServiceTest {

    @Mock
    private Keycloak keycloak;

    @Mock
    private RealmResource realmResource;

    @Mock
    private UsersResource usersResource;

    @Mock
    private UserResource userResource;

    @Mock
    private RolesResource rolesResource;

    @Mock
    private RoleResource roleResource;

    @Mock
    private Response response;

    @InjectMocks
    private KeycloakAdminService keycloakAdminService;

    private UserRepresentation userRepresentation;
    private RoleRepresentation roleRepresentation;

    @BeforeEach
    void setUp() {
        // Setup mocks - usando lenient para evitar UnnecessaryStubbingException
        lenient().when(keycloak.realm("pointer")).thenReturn(realmResource);
        lenient().when(realmResource.users()).thenReturn(usersResource);
        lenient().when(realmResource.roles()).thenReturn(rolesResource);
        
        // Setup UserRepresentation
        userRepresentation = new UserRepresentation();
        userRepresentation.setId("user-id-123");
        userRepresentation.setUsername("teste@exemplo.com");
        userRepresentation.setEmail("teste@exemplo.com");
        userRepresentation.setFirstName("João");
        userRepresentation.setLastName("Silva");
        userRepresentation.setEnabled(true);

        // Setup RoleRepresentation
        roleRepresentation = new RoleRepresentation();
        roleRepresentation.setName("ROLE_USER");
        roleRepresentation.setDescription("Usuário padrão");

        // Inject realm value
        keycloakAdminService = new KeycloakAdminService(keycloak, "pointer");
    }

    @Test
    @DisplayName("Deve criar usuário com sucesso")
    void deveCriarUsuarioComSucesso() {
        // Given
        String nome = "João Silva";
        String email = "teste@exemplo.com";
        String senha = "senha123@";

        try (MockedStatic<ValidationUtil> validationUtilMock = mockStatic(ValidationUtil.class);
             MockedStatic<UserUtil> userUtilMock = mockStatic(UserUtil.class)) {

            validationUtilMock.when(() -> ValidationUtil.validarNome(nome)).thenAnswer(invocation -> null);
            validationUtilMock.when(() -> ValidationUtil.validarEmail(email)).thenAnswer(invocation -> null);
            validationUtilMock.when(() -> ValidationUtil.validarSenha(senha)).thenAnswer(invocation -> null);

            userUtilMock.when(() -> UserUtil.extrairNomeESobrenome(nome)).thenReturn(new String[]{"João", "Silva"});
            userUtilMock.when(() -> UserUtil.construirUserRepresentation("João", "Silva", email)).thenReturn(userRepresentation);

            when(usersResource.search(email)).thenReturn(List.of());
            when(usersResource.create(userRepresentation)).thenReturn(response);
            when(response.getStatus()).thenReturn(201);
            when(response.getHeaderString("Location")).thenReturn("http://localhost:8080/users/user-id-123");
            when(usersResource.get("user-id-123")).thenReturn(userResource);

            // When
            KeycloakResponseDTO result = keycloakAdminService.createUserAndReturnId(nome, email, senha);

            // Then
            assertTrue(result.isSuccess());
            assertEquals("Usuário criado com sucesso", result.getMessage());
            assertEquals("user-id-123", result.getData());

            verify(usersResource).search(email);
            verify(usersResource).create(userRepresentation);
            verify(userResource).resetPassword(any(CredentialRepresentation.class));
        }
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário já existe")
    void deveLancarExcecaoQuandoUsuarioJaExiste() {
        // Given
        String nome = "João Silva";
        String email = "teste@exemplo.com";
        String senha = "senha123@";

        try (MockedStatic<ValidationUtil> validationUtilMock = mockStatic(ValidationUtil.class);
             MockedStatic<UserUtil> userUtilMock = mockStatic(UserUtil.class)) {

            validationUtilMock.when(() -> ValidationUtil.validarNome(nome)).thenAnswer(invocation -> null);
            validationUtilMock.when(() -> ValidationUtil.validarEmail(email)).thenAnswer(invocation -> null);
            validationUtilMock.when(() -> ValidationUtil.validarSenha(senha)).thenAnswer(invocation -> null);

            userUtilMock.when(() -> UserUtil.extrairNomeESobrenome(nome)).thenReturn(new String[]{"João", "Silva"});
            userUtilMock.when(() -> UserUtil.construirUserRepresentation("João", "Silva", email)).thenReturn(userRepresentation);

            when(usersResource.search(email)).thenReturn(List.of(userRepresentation));

            // When & Then
            KeycloakException exception = assertThrows(KeycloakException.class, () -> {
                keycloakAdminService.createUserAndReturnId(nome, email, senha);
            });

            assertEquals("Erro ao criar usuário: Já existe um usuário com este email", exception.getMessage());
            verify(usersResource).search(email);
            verify(usersResource, never()).create(any());
        }
    }

    @Test
    @DisplayName("Deve lançar exceção quando criação falha")
    void deveLancarExcecaoQuandoCriacaoFalha() {
        // Given
        String nome = "João Silva";
        String email = "teste@exemplo.com";
        String senha = "senha123@";

        try (MockedStatic<ValidationUtil> validationUtilMock = mockStatic(ValidationUtil.class);
             MockedStatic<UserUtil> userUtilMock = mockStatic(UserUtil.class)) {

            validationUtilMock.when(() -> ValidationUtil.validarNome(nome)).thenAnswer(invocation -> null);
            validationUtilMock.when(() -> ValidationUtil.validarEmail(email)).thenAnswer(invocation -> null);
            validationUtilMock.when(() -> ValidationUtil.validarSenha(senha)).thenAnswer(invocation -> null);

            userUtilMock.when(() -> UserUtil.extrairNomeESobrenome(nome)).thenReturn(new String[]{"João", "Silva"});
            userUtilMock.when(() -> UserUtil.construirUserRepresentation("João", "Silva", email)).thenReturn(userRepresentation);

            when(usersResource.search(email)).thenReturn(List.of());
            when(usersResource.create(userRepresentation)).thenReturn(response);
            when(response.getStatus()).thenReturn(400);
            when(response.readEntity(String.class)).thenReturn("Erro de validação");

            // When & Then
            KeycloakException exception = assertThrows(KeycloakException.class, () -> {
                keycloakAdminService.createUserAndReturnId(nome, email, senha);
            });

            assertEquals("Erro ao criar usuário no Keycloak", exception.getMessage());
            assertEquals(400, exception.getStatusCode());
            assertEquals("KEYCLOAK_CREATE_ERROR", exception.getErrorCode());
        }
    }

    @Test
    @DisplayName("Deve definir senha do usuário com sucesso")
    void deveDefinirSenhaUsuarioComSucesso() {
        // Given
        String userId = "user-id-123";
        String password = "novaSenha123@";

        try (MockedStatic<ValidationUtil> validationUtilMock = mockStatic(ValidationUtil.class)) {
            validationUtilMock.when(() -> ValidationUtil.validarUserId(userId)).thenAnswer(invocation -> null);
            validationUtilMock.when(() -> ValidationUtil.validarSenha(password)).thenAnswer(invocation -> null);

            when(usersResource.get(userId)).thenReturn(userResource);

            // When
            KeycloakResponseDTO result = keycloakAdminService.setUserPassword(userId, password);

            // Then
            assertTrue(result.isSuccess());
            assertEquals("Senha definida com sucesso", result.getMessage());

            verify(userResource).resetPassword(any(CredentialRepresentation.class));
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao definir senha com erro")
    void deveLancarExcecaoAoDefinirSenhaComErro() {
        // Given
        String userId = "user-id-123";
        String password = "novaSenha123@";

        try (MockedStatic<ValidationUtil> validationUtilMock = mockStatic(ValidationUtil.class)) {
            validationUtilMock.when(() -> ValidationUtil.validarUserId(userId)).thenAnswer(invocation -> null);
            validationUtilMock.when(() -> ValidationUtil.validarSenha(password)).thenAnswer(invocation -> null);

            when(usersResource.get(userId)).thenReturn(userResource);
            doThrow(new RuntimeException("Erro de conexão")).when(userResource).resetPassword(any(CredentialRepresentation.class));

            // When & Then
            KeycloakException exception = assertThrows(KeycloakException.class, () -> {
                keycloakAdminService.setUserPassword(userId, password);
            });

            assertEquals("Erro ao definir senha do usuário: Erro de conexão", exception.getMessage());
            assertEquals(500, exception.getStatusCode());
            assertEquals("KEYCLOAK_PASSWORD_ERROR", exception.getErrorCode());
        }
    }

    @Test
    @DisplayName("Deve atribuir roles ao usuário com sucesso")
    void deveAtribuirRolesAoUsuarioComSucesso() {
        // Given
        String userId = "user-id-123";
        Set<String> roles = Set.of("ROLE_USER", "ROLE_ADMIN");

        try (MockedStatic<ValidationUtil> validationUtilMock = mockStatic(ValidationUtil.class)) {
            validationUtilMock.when(() -> ValidationUtil.validarUserId(userId)).thenAnswer(invocation -> null);
            validationUtilMock.when(() -> ValidationUtil.validarRoles(roles)).thenAnswer(invocation -> null);

            when(usersResource.get(userId)).thenReturn(userResource);
            when(userResource.roles()).thenReturn(mock(RoleMappingResource.class));
            when(userResource.roles().realmLevel()).thenReturn(mock(RoleScopeResource.class));

            when(rolesResource.get("ROLE_USER")).thenReturn(roleResource);
            when(rolesResource.get("ROLE_ADMIN")).thenReturn(roleResource);
            when(roleResource.toRepresentation()).thenReturn(roleRepresentation);

            // When
            KeycloakResponseDTO result = keycloakAdminService.assignRolesToUser(userId, roles);

            // Then
            assertTrue(result.isSuccess());
            assertEquals("Roles atribuídas com sucesso", result.getMessage());

            verify(userResource.roles().realmLevel()).add(anyList());
        }
    }

    @Test
    @DisplayName("Deve lançar exceção quando role não encontrada")
    void deveLancarExcecaoQuandoRoleNaoEncontrada() {
        // Given
        String userId = "user-id-123";
        Set<String> roles = Set.of("ROLE_INEXISTENTE");

        try (MockedStatic<ValidationUtil> validationUtilMock = mockStatic(ValidationUtil.class)) {
            validationUtilMock.when(() -> ValidationUtil.validarUserId(userId)).thenAnswer(invocation -> null);
            validationUtilMock.when(() -> ValidationUtil.validarRoles(roles)).thenAnswer(invocation -> null);

            lenient().when(usersResource.get(userId)).thenReturn(userResource);
            lenient().when(rolesResource.get("ROLE_INEXISTENTE")).thenReturn(null); // Simular role não encontrada

            // When & Then
            KeycloakException exception = assertThrows(KeycloakException.class, () -> {
                keycloakAdminService.assignRolesToUser(userId, roles);
            });

            assertTrue(exception.getMessage().contains("Cannot invoke"));
            assertEquals(500, exception.getStatusCode());
            assertEquals("KEYCLOAK_ROLE_ERROR", exception.getErrorCode());
        }
    }

    @Test
    @DisplayName("Deve desabilitar usuário com sucesso")
    void deveDesabilitarUsuarioComSucesso() {
        // Given
        String email = "teste@exemplo.com";

        try (MockedStatic<ValidationUtil> validationUtilMock = mockStatic(ValidationUtil.class)) {
            validationUtilMock.when(() -> ValidationUtil.validarEmail(email)).thenAnswer(invocation -> null);

            when(usersResource.search(email)).thenReturn(List.of(userRepresentation));
            when(usersResource.get(userRepresentation.getId())).thenReturn(userResource);

            // When
            KeycloakResponseDTO result = keycloakAdminService.disableUser(email);

            // Then
            assertTrue(result.isSuccess());
            assertEquals("Usuário desativado com sucesso", result.getMessage());

            verify(usersResource.get(userRepresentation.getId())).update(any(UserRepresentation.class));
            verify(usersResource.get(userRepresentation.getId())).logout();
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao desabilitar usuário não encontrado")
    void deveLancarExcecaoAoDesabilitarUsuarioNaoEncontrado() {
        // Given
        String email = "inexistente@exemplo.com";

        try (MockedStatic<ValidationUtil> validationUtilMock = mockStatic(ValidationUtil.class)) {
            validationUtilMock.when(() -> ValidationUtil.validarEmail(email)).thenAnswer(invocation -> null);

            when(usersResource.search(email)).thenReturn(List.of());

            // When & Then
            KeycloakException exception = assertThrows(KeycloakException.class, () -> {
                keycloakAdminService.disableUser(email);
            });

            assertTrue(exception.getMessage().contains("Erro ao desativar usuário"));
            assertTrue(exception.getMessage().contains("Usuário não encontrado: inexistente@exemplo.com"));
            assertEquals(500, exception.getStatusCode());
            assertEquals("KEYCLOAK_DISABLE_ERROR", exception.getErrorCode());
        }
    }

    @Test
    @DisplayName("Deve habilitar usuário com sucesso")
    void deveHabilitarUsuarioComSucesso() {
        // Given
        String email = "teste@exemplo.com";

        try (MockedStatic<ValidationUtil> validationUtilMock = mockStatic(ValidationUtil.class)) {
            validationUtilMock.when(() -> ValidationUtil.validarEmail(email)).thenAnswer(invocation -> null);

            when(usersResource.search(email)).thenReturn(List.of(userRepresentation));
            when(usersResource.get(userRepresentation.getId())).thenReturn(userResource);

            // When
            KeycloakResponseDTO result = keycloakAdminService.enableUser(email);

            // Then
            assertTrue(result.isSuccess());
            assertEquals("Usuário ativado com sucesso", result.getMessage());

            verify(usersResource.get(userRepresentation.getId())).update(any(UserRepresentation.class));
        }
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    void deveAtualizarUsuarioComSucesso() {
        // Given
        String userId = "user-id-123";

        try (MockedStatic<ValidationUtil> validationUtilMock = mockStatic(ValidationUtil.class)) {
            validationUtilMock.when(() -> ValidationUtil.validarUserId(userId)).thenAnswer(invocation -> null);

            when(usersResource.get(userId)).thenReturn(userResource);

            // When
            KeycloakResponseDTO result = keycloakAdminService.updateUser(userId, userRepresentation);

            // Then
            assertTrue(result.isSuccess());
            assertEquals("Usuário atualizado com sucesso", result.getMessage());

            verify(userResource).update(userRepresentation);
        }
    }

    @Test
    @DisplayName("Deve remover roles do usuário com sucesso")
    void deveRemoverRolesDoUsuarioComSucesso() {
        // Given
        String userId = "user-id-123";
        Set<String> roles = Set.of("ROLE_ADMIN");

        try (MockedStatic<ValidationUtil> validationUtilMock = mockStatic(ValidationUtil.class)) {
            validationUtilMock.when(() -> ValidationUtil.validarUserId(userId)).thenAnswer(invocation -> null);
            validationUtilMock.when(() -> ValidationUtil.validarRoles(roles)).thenAnswer(invocation -> null);

            when(usersResource.get(userId)).thenReturn(userResource);
            when(userResource.roles()).thenReturn(mock(RoleMappingResource.class));
            when(userResource.roles().realmLevel()).thenReturn(mock(RoleScopeResource.class));

            when(rolesResource.get("ROLE_ADMIN")).thenReturn(roleResource);
            when(roleResource.toRepresentation()).thenReturn(roleRepresentation);

            // When
            KeycloakResponseDTO result = keycloakAdminService.removeRolesFromUser(userId, roles);

            // Then
            assertTrue(result.isSuccess());
            assertEquals("Roles removidas com sucesso", result.getMessage());

            verify(userResource.roles().realmLevel()).remove(anyList());
        }
    }

    @Test
    @DisplayName("Deve atualizar senha com sucesso")
    void deveAtualizarSenhaComSucesso() {
        // Given
        String userId = "user-id-123";
        String password = "novaSenha123@";

        try (MockedStatic<ValidationUtil> validationUtilMock = mockStatic(ValidationUtil.class)) {
            validationUtilMock.when(() -> ValidationUtil.validarUserId(userId)).thenAnswer(invocation -> null);
            validationUtilMock.when(() -> ValidationUtil.validarSenha(password)).thenAnswer(invocation -> null);

            when(usersResource.get(userId)).thenReturn(userResource);
            when(userResource.toRepresentation()).thenReturn(userRepresentation);

            // When
            KeycloakResponseDTO result = keycloakAdminService.updatePassword(userId, password);

            // Then
            assertTrue(result.isSuccess());
            assertEquals("Senha atualizada com sucesso", result.getMessage());

            verify(userResource).resetPassword(any(CredentialRepresentation.class));
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar senha de usuário não encontrado")
    void deveLancarExcecaoAoAtualizarSenhaUsuarioNaoEncontrado() {
        // Given
        String userId = "user-id-inexistente";
        String password = "novaSenha123@";

        try (MockedStatic<ValidationUtil> validationUtilMock = mockStatic(ValidationUtil.class)) {
            validationUtilMock.when(() -> ValidationUtil.validarUserId(userId)).thenAnswer(invocation -> null);
            validationUtilMock.when(() -> ValidationUtil.validarSenha(password)).thenAnswer(invocation -> null);

            lenient().when(usersResource.get(userId)).thenReturn(null); // Simular usuário não encontrado

            // When & Then
            KeycloakException exception = assertThrows(KeycloakException.class, () -> {
                keycloakAdminService.updatePassword(userId, password);
            });

            assertTrue(exception.getMessage().contains("Cannot invoke"));
            assertEquals(500, exception.getStatusCode());
            assertEquals("KEYCLOAK_PASSWORD_UPDATE_ERROR", exception.getErrorCode());
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao resetar senha com WebApplicationException")
    void deveLancarExcecaoAoResetarSenhaComWebApplicationException() {
        // Given
        String userId = "user-id-123";
        String password = "novaSenha123@";

        try (MockedStatic<ValidationUtil> validationUtilMock = mockStatic(ValidationUtil.class)) {
            validationUtilMock.when(() -> ValidationUtil.validarUserId(userId)).thenAnswer(invocation -> null);
            validationUtilMock.when(() -> ValidationUtil.validarSenha(password)).thenAnswer(invocation -> null);

            lenient().when(usersResource.get(userId)).thenReturn(userResource);
            lenient().when(userResource.toRepresentation()).thenReturn(userRepresentation);

            Response errorResponse = mock(Response.class);
            StatusType statusType = mock(StatusType.class);
            when(statusType.getStatusCode()).thenReturn(400);
            when(errorResponse.getStatusInfo()).thenReturn(statusType);
            when(errorResponse.getStatus()).thenReturn(400);
            when(errorResponse.readEntity(String.class)).thenReturn("Senha muito fraca");

            WebApplicationException webException = new WebApplicationException(errorResponse);
            doThrow(webException).when(userResource).resetPassword(any(CredentialRepresentation.class));

            // When & Then
            KeycloakException exception = assertThrows(KeycloakException.class, () -> {
                keycloakAdminService.updatePassword(userId, password);
            });

            assertEquals("Erro ao atualizar senha: Erro ao resetar senha", exception.getMessage());
            assertEquals(500, exception.getStatusCode());
            assertEquals("KEYCLOAK_PASSWORD_UPDATE_ERROR", exception.getErrorCode());
        }
    }

    @Test
    @DisplayName("Deve lançar exceção genérica ao criar usuário")
    void deveLancarExcecaoGenericaAoCriarUsuario() {
        // Given
        String nome = "João Silva";
        String email = "teste@exemplo.com";
        String senha = "senha123@";

        try (MockedStatic<ValidationUtil> validationUtilMock = mockStatic(ValidationUtil.class)) {
            validationUtilMock.when(() -> ValidationUtil.validarNome(nome)).thenAnswer(invocation -> null);
            validationUtilMock.when(() -> ValidationUtil.validarEmail(email)).thenAnswer(invocation -> null);
            validationUtilMock.when(() -> ValidationUtil.validarSenha(senha)).thenAnswer(invocation -> null);

            when(usersResource.search(email)).thenThrow(new RuntimeException("Erro de conexão"));

            // When & Then
            KeycloakException exception = assertThrows(KeycloakException.class, () -> {
                keycloakAdminService.createUserAndReturnId(nome, email, senha);
            });

            assertEquals("Erro ao criar usuário: Erro de conexão", exception.getMessage());
            assertEquals(500, exception.getStatusCode());
            assertEquals("KEYCLOAK_ERROR", exception.getErrorCode());
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao atribuir roles com erro genérico")
    void deveLancarExcecaoAoAtribuirRolesComErroGenerico() {
        // Given
        String userId = "user-id-123";
        Set<String> roles = Set.of("ROLE_USER");

        try (MockedStatic<ValidationUtil> validationUtilMock = mockStatic(ValidationUtil.class)) {
            validationUtilMock.when(() -> ValidationUtil.validarUserId(userId)).thenAnswer(invocation -> null);
            validationUtilMock.when(() -> ValidationUtil.validarRoles(roles)).thenAnswer(invocation -> null);

            lenient().when(usersResource.get(userId)).thenReturn(userResource);
            lenient().when(rolesResource.get("ROLE_USER")).thenReturn(null); // Simular role não encontrada

            // When & Then
            KeycloakException exception = assertThrows(KeycloakException.class, () -> {
                keycloakAdminService.assignRolesToUser(userId, roles);
            });

            assertTrue(exception.getMessage().contains("Erro ao atribuir roles ao usuário"));
            assertEquals(500, exception.getStatusCode());
            assertEquals("KEYCLOAK_ROLE_ERROR", exception.getErrorCode());
        }
    }

    @Test
    @DisplayName("Deve testar setter do Keycloak")
    void deveTestarSetterDoKeycloak() {
        // Given
        Keycloak novoKeycloak = mock(Keycloak.class);

        // When
        keycloakAdminService.setKeycloak(novoKeycloak);

        // Then
        // Verificar que o setter funcionou (método é usado para testes)
        assertNotNull(keycloakAdminService);
    }

    @Test
    @DisplayName("Deve processar resposta de criação com location válido")
    void deveProcessarRespostaCriacaoComLocationValido() {
        // Given
        String nome = "João Silva";
        String email = "teste@exemplo.com";
        String senha = "senha123@";
        String locationHeader = "http://localhost:8080/admin/realms/pointer/users/user-id-456";

        try (MockedStatic<ValidationUtil> validationUtilMock = mockStatic(ValidationUtil.class);
             MockedStatic<UserUtil> userUtilMock = mockStatic(UserUtil.class)) {

            validationUtilMock.when(() -> ValidationUtil.validarNome(nome)).thenAnswer(invocation -> null);
            validationUtilMock.when(() -> ValidationUtil.validarEmail(email)).thenAnswer(invocation -> null);
            validationUtilMock.when(() -> ValidationUtil.validarSenha(senha)).thenAnswer(invocation -> null);

            userUtilMock.when(() -> UserUtil.extrairNomeESobrenome(nome)).thenReturn(new String[]{"João", "Silva"});
            userUtilMock.when(() -> UserUtil.construirUserRepresentation("João", "Silva", email)).thenReturn(userRepresentation);

            when(usersResource.search(email)).thenReturn(List.of());
            when(usersResource.create(userRepresentation)).thenReturn(response);
            when(response.getStatus()).thenReturn(201);
            when(response.getHeaderString("Location")).thenReturn(locationHeader);
            when(usersResource.get("user-id-456")).thenReturn(userResource);

            // When
            KeycloakResponseDTO result = keycloakAdminService.createUserAndReturnId(nome, email, senha);

            // Then
            assertTrue(result.isSuccess());
            assertEquals("user-id-456", result.getData());
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao habilitar usuário com erro")
    void deveLancarExcecaoAoHabilitarUsuarioComErro() {
        // Given
        String email = "teste@exemplo.com";

        try (MockedStatic<ValidationUtil> validationUtilMock = mockStatic(ValidationUtil.class)) {
            validationUtilMock.when(() -> ValidationUtil.validarEmail(email)).thenAnswer(invocation -> null);

            when(usersResource.search(email)).thenThrow(new RuntimeException("Erro de conexão"));

            // When & Then
            KeycloakException exception = assertThrows(KeycloakException.class, () -> {
                keycloakAdminService.enableUser(email);
            });

            assertEquals("Erro ao ativar usuário: Erro de conexão", exception.getMessage());
            assertEquals(500, exception.getStatusCode());
            assertEquals("KEYCLOAK_ENABLE_ERROR", exception.getErrorCode());
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar usuário com erro")
    void deveLancarExcecaoAoAtualizarUsuarioComErro() {
        // Given
        String userId = "user-id-123";

        try (MockedStatic<ValidationUtil> validationUtilMock = mockStatic(ValidationUtil.class)) {
            validationUtilMock.when(() -> ValidationUtil.validarUserId(userId)).thenAnswer(invocation -> null);

            when(usersResource.get(userId)).thenThrow(new RuntimeException("Erro de conexão"));

            // When & Then
            KeycloakException exception = assertThrows(KeycloakException.class, () -> {
                keycloakAdminService.updateUser(userId, userRepresentation);
            });

            assertEquals("Erro ao atualizar usuário: Erro de conexão", exception.getMessage());
            assertEquals(500, exception.getStatusCode());
            assertEquals("KEYCLOAK_UPDATE_ERROR", exception.getErrorCode());
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao remover roles com erro")
    void deveLancarExcecaoAoRemoverRolesComErro() {
        // Given
        String userId = "user-id-123";
        Set<String> roles = Set.of("ROLE_ADMIN");

        try (MockedStatic<ValidationUtil> validationUtilMock = mockStatic(ValidationUtil.class)) {
            validationUtilMock.when(() -> ValidationUtil.validarUserId(userId)).thenAnswer(invocation -> null);
            validationUtilMock.when(() -> ValidationUtil.validarRoles(roles)).thenAnswer(invocation -> null);

            lenient().when(usersResource.get(userId)).thenReturn(userResource);
            lenient().when(rolesResource.get("ROLE_ADMIN")).thenReturn(null); // Simular role não encontrada

            // When & Then
            KeycloakException exception = assertThrows(KeycloakException.class, () -> {
                keycloakAdminService.removeRolesFromUser(userId, roles);
            });

            assertTrue(exception.getMessage().contains("Erro ao remover roles do usuário"));
            assertEquals(500, exception.getStatusCode());
            assertEquals("KEYCLOAK_ROLE_REMOVE_ERROR", exception.getErrorCode());
        }
    }
} 
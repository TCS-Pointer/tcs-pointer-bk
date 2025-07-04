package br.com.pointer.pointer_back.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.exception.KeycloakException;
import br.com.pointer.pointer_back.model.Usuario;
import br.com.pointer.pointer_back.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class TokenControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private TokenController tokenController;

    private TokenController.LoginRequest loginRequest;
    private TokenResponse tokenResponse;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(tokenController, "clientId", "pointer");
        ReflectionTestUtils.setField(tokenController, "clientSecret", "secret");
        ReflectionTestUtils.setField(tokenController, "authServerUrl", "http://localhost:8080");
        ReflectionTestUtils.setField(tokenController, "realm", "pointer");

        loginRequest = new TokenController.LoginRequest("user@test.com", "password");
        
        tokenResponse = new TokenResponse();
        tokenResponse.setAccess_token("access-token");
        tokenResponse.setRefresh_token("refresh-token");
        tokenResponse.setExpires_in(300);
        tokenResponse.setToken_type("Bearer");
        
        usuario = new Usuario();
        usuario.setEmail("user@test.com");
        usuario.setTwoFactorEnabled(false);
        usuario.setSecretKey(null);
    }

    @Test
    @DisplayName("Deve gerar token com sucesso quando credenciais são válidas e 2FA está desabilitado")
    void deveGerarTokenComSucesso() {
        // Arrange
        when(usuarioRepository.findByEmail("user@test.com")).thenReturn(Optional.of(usuario));

        try (MockedConstruction<RestTemplate> mocked = mockConstruction(RestTemplate.class,
                (mock, context) -> {
                    when(mock.postForEntity(anyString(), any(), eq(TokenResponse.class)))
                        .thenReturn(new ResponseEntity<>(tokenResponse, HttpStatus.OK));
                })) {

            // Act
            ApiResponse<TokenResponse> response = tokenController.token(loginRequest);

            // Assert
            assertNotNull(response);
            assertEquals("Token gerado com sucesso", response.getMessage());
            assertNotNull(response.getContent());
            assertEquals("access-token", response.getContent().getAccess_token());
            assertFalse(response.getContent().getTwo_factor_enabled());
            assertFalse(response.getContent().getTwo_factor_configured());
        }
    }

    @Test
    @DisplayName("Deve gerar token com sucesso quando credenciais são válidas e 2FA está habilitado")
    void deveGerarTokenComSucessoCom2FAHabilitado() {
        // Arrange
        usuario.setTwoFactorEnabled(true);
        usuario.setSecretKey("secret-key");

        when(usuarioRepository.findByEmail("user@test.com")).thenReturn(Optional.of(usuario));

        try (MockedConstruction<RestTemplate> mocked = mockConstruction(RestTemplate.class,
                (mock, context) -> {
                    when(mock.postForEntity(anyString(), any(), eq(TokenResponse.class)))
                        .thenReturn(new ResponseEntity<>(tokenResponse, HttpStatus.OK));
                })) {

            // Act
            ApiResponse<TokenResponse> response = tokenController.token(loginRequest);

            // Assert
            assertNotNull(response);
            assertEquals("Token gerado com sucesso", response.getMessage());
            assertNotNull(response.getContent());
            assertEquals("access-token", response.getContent().getAccess_token());
            assertTrue(response.getContent().getTwo_factor_enabled());
            assertTrue(response.getContent().getTwo_factor_configured());
        }
    }

    @Test
    @DisplayName("Deve lançar exceção quando credenciais são inválidas")
    void deveLancarExcecaoQuandoCredenciaisInvalidas() {
        // Arrange
        try (MockedConstruction<RestTemplate> mocked = mockConstruction(RestTemplate.class,
                (mock, context) -> {
                    when(mock.postForEntity(anyString(), any(), eq(TokenResponse.class)))
                        .thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));
                })) {

            // Act & Assert
            KeycloakException exception = assertThrows(KeycloakException.class, () -> {
                tokenController.token(loginRequest);
            });

            assertEquals("Credenciais inválidas", exception.getMessage());
            assertEquals(401, exception.getStatusCode());
            assertEquals("INVALID_CREDENTIALS", exception.getErrorCode());
        }
    }

    @Test
    @DisplayName("Deve lançar exceção quando conta está desativada")
    void deveLancarExcecaoQuandoContaDesativada() {
        // Arrange
        try (MockedConstruction<RestTemplate> mocked = mockConstruction(RestTemplate.class,
                (mock, context) -> {
                    HttpClientErrorException exception = new HttpClientErrorException(
                        HttpStatus.FORBIDDEN,
                        "Account disabled",
                        "Account disabled".getBytes(),
                        null
                    );
                    when(mock.postForEntity(anyString(), any(), eq(TokenResponse.class)))
                        .thenThrow(exception);
                })) {

            // Act & Assert
            KeycloakException exception = assertThrows(KeycloakException.class, () -> {
                tokenController.token(loginRequest);
            });

            assertEquals("Conta desativada. Entre em contato com o administrador do sistema.", exception.getMessage());
            assertEquals(403, exception.getStatusCode());
            assertEquals("ACCOUNT_DISABLED", exception.getErrorCode());
        }
    }

    @Test
    @DisplayName("Deve definir 2FA como false quando usuário não é encontrado")
    void deveDefinir2FAComoFalseQuandoUsuarioNaoEncontrado() {
        // Arrange
        when(usuarioRepository.findByEmail("user@test.com")).thenReturn(Optional.empty());

        try (MockedConstruction<RestTemplate> mocked = mockConstruction(RestTemplate.class,
                (mock, context) -> {
                    when(mock.postForEntity(anyString(), any(), eq(TokenResponse.class)))
                        .thenReturn(new ResponseEntity<>(tokenResponse, HttpStatus.OK));
                })) {

            // Act
            ApiResponse<TokenResponse> response = tokenController.token(loginRequest);

            // Assert
            assertNotNull(response);
            assertEquals("Token gerado com sucesso", response.getMessage());
            assertNotNull(response.getContent());
            assertFalse(response.getContent().getTwo_factor_enabled());
            assertFalse(response.getContent().getTwo_factor_configured());
        }
    }

    @Test
    @DisplayName("Deve lançar exceção quando ocorre erro genérico")
    void deveLancarExcecaoQuandoOcorreErroGenerico() {
        // Arrange
        try (MockedConstruction<RestTemplate> mocked = mockConstruction(RestTemplate.class,
                (mock, context) -> {
                    when(mock.postForEntity(anyString(), any(), eq(TokenResponse.class)))
                        .thenThrow(new RuntimeException("Erro inesperado"));
                })) {

            // Act & Assert
            KeycloakException exception = assertThrows(KeycloakException.class, () -> {
                tokenController.token(loginRequest);
            });

            assertEquals("Erro inesperado ao gerar token: Erro inesperado", exception.getMessage());
            assertEquals(500, exception.getStatusCode());
            assertEquals("TOKEN_GENERATION_ERROR", exception.getErrorCode());
        }
    }
} 
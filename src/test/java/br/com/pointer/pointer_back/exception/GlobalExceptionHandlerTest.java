package br.com.pointer.pointer_back.exception;

import br.com.pointer.pointer_back.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        // Setup inicial se necessário
    }

    @Test
    void handleAuthorizationDeniedException_DeveRetornar403() {
        // Arrange
        AuthorizationDeniedException ex = new AuthorizationDeniedException("Acesso negado");

        // Act
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleAuthorizationDeniedException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Acesso negado. Você não tem permissão para acessar este recurso.", response.getBody().getMessage());
        assertEquals(403, response.getBody().getStatus());
    }

    @Test
    void handleAccessDeniedException_DeveRetornar403() {
        // Arrange
        AccessDeniedException ex = new AccessDeniedException("Acesso negado");

        // Act
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleAccessDeniedException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Acesso negado. Você não tem permissão para acessar este recurso.", response.getBody().getMessage());
        assertEquals(403, response.getBody().getStatus());
    }

    @Test
    void handleUsuarioJaExisteException_DeveRetornar409() {
        // Arrange
        UsuarioJaExisteException ex = new UsuarioJaExisteException("Usuário já existe");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleUsuarioJaExisteException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("Usuário já existe", body.get("message"));
        assertEquals(409, body.get("status"));
        assertEquals("Conflito", body.get("error"));
        assertNotNull(body.get("timestamp"));
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
    }

    @Test
    void handleEmailInvalidoException_DeveRetornar400() {
        // Arrange
        EmailInvalidoException ex = new EmailInvalidoException("Email inválido");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleEmailInvalidoException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("Email inválido", body.get("message"));
        assertEquals(400, body.get("status"));
        assertEquals("Email Inválido", body.get("error"));
        assertNotNull(body.get("timestamp"));
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
    }

    @Test
    void handleSenhaInvalidaException_DeveRetornar400() {
        // Arrange
        SenhaInvalidaException ex = new SenhaInvalidaException("Senha inválida");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleSenhaInvalidaException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("Senha inválida", body.get("message"));
        assertEquals(400, body.get("status"));
        assertEquals("Senha Inválida", body.get("error"));
        assertNotNull(body.get("timestamp"));
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
    }

    @Test
    void handleKeycloakException_DeveRetornar500() {
        // Arrange
        KeycloakException ex = new KeycloakException("Erro no Keycloak", 500, "KEYCLOAK_ERROR");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleKeycloakException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("Erro no Keycloak", body.get("message"));
        assertEquals(500, body.get("status"));
        assertEquals("Erro no Keycloak", body.get("error"));
        assertNotNull(body.get("timestamp"));
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
    }

    @Test
    void handleHttpMessageNotReadableException_ComTipoFeedback_DeveRetornar400ComMensagemEspecifica() {
        // Arrange
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Erro ao deserializar TipoFeedback", (Throwable) null);

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleHttpMessageNotReadableException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("Tipo de feedback inválido. Valores aceitos: POSITIVO, CONSTRUTIVO", body.get("message"));
        assertEquals(400, body.get("status"));
        assertEquals("Dados inválidos", body.get("error"));
        assertNotNull(body.get("timestamp"));
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
    }

    @Test
    void handleHttpMessageNotReadableException_SemTipoFeedback_DeveRetornar400ComMensagemGenerica() {
        // Arrange
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Erro ao deserializar dados", (Throwable) null);

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleHttpMessageNotReadableException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("Erro ao processar dados da requisição", body.get("message"));
        assertEquals(400, body.get("status"));
        assertEquals("Dados inválidos", body.get("error"));
        assertNotNull(body.get("timestamp"));
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
    }

    @Test
    void handleHttpMessageNotReadableException_ComMensagemNula_DeveRetornar400ComMensagemGenerica() {
        // Arrange
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException((String) null, (Throwable) null);

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleHttpMessageNotReadableException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("Erro ao processar dados da requisição", body.get("message"));
        assertEquals(400, body.get("status"));
        assertEquals("Dados inválidos", body.get("error"));
        assertNotNull(body.get("timestamp"));
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
    }

    @Test
    void handleGlobalException_DeveRetornar500() {
        // Arrange
        Exception ex = new RuntimeException("Erro interno");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleGlobalException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("Erro interno do servidor", body.get("message"));
        assertEquals(500, body.get("status"));
        assertEquals("Erro Interno", body.get("error"));
        assertNotNull(body.get("timestamp"));
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
    }

    @Test
    void handleGlobalException_ComExcecaoNula_DeveRetornar500() {
        // Arrange
        Exception ex = null;

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleGlobalException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("Erro interno do servidor", body.get("message"));
        assertEquals(500, body.get("status"));
        assertEquals("Erro Interno", body.get("error"));
        assertNotNull(body.get("timestamp"));
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
    }

    @Test
    void handleUsuarioJaExisteException_ComMensagemNula_DeveRetornar409() {
        // Arrange
        UsuarioJaExisteException ex = new UsuarioJaExisteException(null);

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleUsuarioJaExisteException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertNull(body.get("message"));
        assertEquals(409, body.get("status"));
        assertEquals("Conflito", body.get("error"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handleEmailInvalidoException_ComMensagemVazia_DeveRetornar400() {
        // Arrange
        EmailInvalidoException ex = new EmailInvalidoException("");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleEmailInvalidoException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("", body.get("message"));
        assertEquals(400, body.get("status"));
        assertEquals("Email Inválido", body.get("error"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handleSenhaInvalidaException_ComMensagemLonga_DeveRetornar400() {
        // Arrange
        String mensagemLonga = "Esta é uma mensagem muito longa para testar o comportamento do handler com textos extensos que podem conter muitos caracteres e informações detalhadas sobre o erro de senha";
        SenhaInvalidaException ex = new SenhaInvalidaException(mensagemLonga);

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleSenhaInvalidaException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals(mensagemLonga, body.get("message"));
        assertEquals(400, body.get("status"));
        assertEquals("Senha Inválida", body.get("error"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handleKeycloakException_ComMensagemComCaracteresEspeciais_DeveRetornar500() {
        // Arrange
        String mensagemEspecial = "Erro no Keycloak com caracteres especiais: áéíóú çãõ ñ";
        KeycloakException ex = new KeycloakException(mensagemEspecial, 500, "KEYCLOAK_ERROR");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleKeycloakException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals(mensagemEspecial, body.get("message"));
        assertEquals(500, body.get("status"));
        assertEquals("Erro no Keycloak", body.get("error"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handleHttpMessageNotReadableException_ComMensagemComTipoFeedback_DeveRetornar400ComMensagemEspecifica() {
        // Arrange
        String mensagemComTipoFeedback = "Erro ao deserializar TipoFeedback: valor inválido 'INVALIDO'";
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException(mensagemComTipoFeedback, (Throwable) null);

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleHttpMessageNotReadableException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("Tipo de feedback inválido. Valores aceitos: POSITIVO, CONSTRUTIVO", body.get("message"));
        assertEquals(400, body.get("status"));
        assertEquals("Dados inválidos", body.get("error"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handleGlobalException_ComExcecaoCustomizada_DeveRetornar500() {
        // Arrange
        Exception ex = new IllegalArgumentException("Argumento inválido");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleGlobalException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("Erro interno do servidor", body.get("message"));
        assertEquals(500, body.get("status"));
        assertEquals("Erro Interno", body.get("error"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handleAuthorizationDeniedException_ComMensagemNula_DeveRetornar403() {
        // Arrange
        AuthorizationDeniedException ex = new AuthorizationDeniedException(null);

        // Act
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleAuthorizationDeniedException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Acesso negado. Você não tem permissão para acessar este recurso.", response.getBody().getMessage());
        assertEquals(403, response.getBody().getStatus());
    }

    @Test
    void handleAccessDeniedException_ComMensagemVazia_DeveRetornar403() {
        // Arrange
        AccessDeniedException ex = new AccessDeniedException("");

        // Act
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleAccessDeniedException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Acesso negado. Você não tem permissão para acessar este recurso.", response.getBody().getMessage());
        assertEquals(403, response.getBody().getStatus());
    }
} 
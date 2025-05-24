package br.com.pointer.pointer_back.exception;

import br.com.pointer.pointer_back.dto.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleUsuarioJaExisteException_DeveRetornarResponseEntityComStatus409() {
        // Arrange
        UsuarioJaExisteException ex = new UsuarioJaExisteException("Usuário já existe");

        // Act
        ResponseEntity<ApiResponse<Void>> response = handler.handleUsuarioJaExisteException(ex);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(409, response.getBody().getStatus());
        assertEquals("Usuário já existe", response.getBody().getMessage());
    }

    @Test
    void handleEmailInvalidoException_DeveRetornarResponseEntityComStatus400() {
        // Arrange
        EmailInvalidoException ex = new EmailInvalidoException("Email inválido");

        // Act
        ResponseEntity<ApiResponse<Void>> response = handler.handleEmailInvalidoException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Email inválido", response.getBody().getMessage());
    }

    @Test
    void handleSenhaInvalidaException_DeveRetornarResponseEntityComStatus400() {
        // Arrange
        SenhaInvalidaException ex = new SenhaInvalidaException("Senha inválida");

        // Act
        ResponseEntity<ApiResponse<Void>> response = handler.handleSenhaInvalidaException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Senha inválida", response.getBody().getMessage());
    }

    @Test
    void handleKeycloakException_DeveRetornarResponseEntityComStatus500() {
        // Arrange
        KeycloakException ex = new KeycloakException("Erro no Keycloak");

        // Act
        ResponseEntity<ApiResponse<Void>> response = handler.handleKeycloakException(ex);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("Erro no Keycloak", response.getBody().getMessage());
    }

    @Test
    void handleException_DeveRetornarResponseEntityComStatus500() {
        // Arrange
        Exception ex = new RuntimeException("Erro interno");

        // Act
        ResponseEntity<ApiResponse<Void>> response = handler.handleException(ex);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Erro interno do servidor: Erro interno", response.getBody().getMessage());
    }
}
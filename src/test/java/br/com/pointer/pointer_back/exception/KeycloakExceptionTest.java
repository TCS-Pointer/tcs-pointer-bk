package br.com.pointer.pointer_back.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class KeycloakExceptionTest {

    @Test
    void testConstrutorComMessage_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Erro no Keycloak";

        // Act
        KeycloakException exception = new KeycloakException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals(500, exception.getStatusCode());
        assertEquals("KEYCLOAK_ERROR", exception.getErrorCode());
        assertEquals(message, exception.getErrorMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComMessageEStatusCode_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Erro no Keycloak";
        int statusCode = 404;

        // Act
        KeycloakException exception = new KeycloakException(message, statusCode);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals(statusCode, exception.getStatusCode());
        assertEquals("KEYCLOAK_ERROR", exception.getErrorCode());
        assertEquals(message, exception.getErrorMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComMessageStatusCodeEErrorCode_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Erro no Keycloak";
        int statusCode = 400;
        String errorCode = "INVALID_REQUEST";

        // Act
        KeycloakException exception = new KeycloakException(message, statusCode, errorCode);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals(statusCode, exception.getStatusCode());
        assertEquals(errorCode, exception.getErrorCode());
        assertEquals(message, exception.getErrorMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComMessageECause_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Erro no Keycloak";
        Throwable cause = new RuntimeException("Causa original");

        // Act
        KeycloakException exception = new KeycloakException(message, cause);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals(500, exception.getStatusCode());
        assertEquals("KEYCLOAK_ERROR", exception.getErrorCode());
        assertEquals(message, exception.getErrorMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testConstrutorComMessageNula_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = null;

        // Act
        KeycloakException exception = new KeycloakException(message);

        // Assert
        assertNull(exception.getMessage());
        assertEquals(500, exception.getStatusCode());
        assertEquals("KEYCLOAK_ERROR", exception.getErrorCode());
        assertNull(exception.getErrorMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComMessageVazia_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "";

        // Act
        KeycloakException exception = new KeycloakException(message);

        // Assert
        assertEquals("", exception.getMessage());
        assertEquals(500, exception.getStatusCode());
        assertEquals("KEYCLOAK_ERROR", exception.getErrorCode());
        assertEquals("", exception.getErrorMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComStatusCodeZero_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Erro no Keycloak";
        int statusCode = 0;

        // Act
        KeycloakException exception = new KeycloakException(message, statusCode);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals(statusCode, exception.getStatusCode());
        assertEquals("KEYCLOAK_ERROR", exception.getErrorCode());
        assertEquals(message, exception.getErrorMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComStatusCodeNegativo_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Erro no Keycloak";
        int statusCode = -1;

        // Act
        KeycloakException exception = new KeycloakException(message, statusCode);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals(statusCode, exception.getStatusCode());
        assertEquals("KEYCLOAK_ERROR", exception.getErrorCode());
        assertEquals(message, exception.getErrorMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComErrorCodeNulo_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Erro no Keycloak";
        int statusCode = 500;
        String errorCode = null;

        // Act
        KeycloakException exception = new KeycloakException(message, statusCode, errorCode);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals(statusCode, exception.getStatusCode());
        assertNull(exception.getErrorCode());
        assertEquals(message, exception.getErrorMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComErrorCodeVazio_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Erro no Keycloak";
        int statusCode = 500;
        String errorCode = "";

        // Act
        KeycloakException exception = new KeycloakException(message, statusCode, errorCode);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals(statusCode, exception.getStatusCode());
        assertEquals("", exception.getErrorCode());
        assertEquals(message, exception.getErrorMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComCauseNula_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Erro no Keycloak";
        Throwable cause = null;

        // Act
        KeycloakException exception = new KeycloakException(message, cause);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals(500, exception.getStatusCode());
        assertEquals("KEYCLOAK_ERROR", exception.getErrorCode());
        assertEquals(message, exception.getErrorMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComMessageLonga_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Esta é uma mensagem de erro muito longa que contém muitos detalhes sobre o que aconteceu no Keycloak e por que a operação falhou";

        // Act
        KeycloakException exception = new KeycloakException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals(500, exception.getStatusCode());
        assertEquals("KEYCLOAK_ERROR", exception.getErrorCode());
        assertEquals(message, exception.getErrorMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComMessageComCaracteresEspeciais_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Erro no Keycloak: @#$%^&*()_+-=[]{}|;':\",./<>?";

        // Act
        KeycloakException exception = new KeycloakException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals(500, exception.getStatusCode());
        assertEquals("KEYCLOAK_ERROR", exception.getErrorCode());
        assertEquals(message, exception.getErrorMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComMessageComAcentos_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Erro no Keycloak: usuário não encontrado";

        // Act
        KeycloakException exception = new KeycloakException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals(500, exception.getStatusCode());
        assertEquals("KEYCLOAK_ERROR", exception.getErrorCode());
        assertEquals(message, exception.getErrorMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComDiferentesStatusCodes_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Erro no Keycloak";
        int[] statusCodes = {200, 201, 400, 401, 403, 404, 500, 502, 503};

        for (int statusCode : statusCodes) {
            // Act
            KeycloakException exception = new KeycloakException(message, statusCode);

            // Assert
            assertEquals(message, exception.getMessage());
            assertEquals(statusCode, exception.getStatusCode());
            assertEquals("KEYCLOAK_ERROR", exception.getErrorCode());
            assertEquals(message, exception.getErrorMessage());
            assertNull(exception.getCause());
        }
    }

    @Test
    void testConstrutorComDiferentesErrorCodes_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Erro no Keycloak";
        int statusCode = 500;
        String[] errorCodes = {
            "KEYCLOAK_ERROR",
            "INVALID_REQUEST",
            "UNAUTHORIZED",
            "FORBIDDEN",
            "NOT_FOUND",
            "INTERNAL_SERVER_ERROR",
            "SERVICE_UNAVAILABLE"
        };

        for (String errorCode : errorCodes) {
            // Act
            KeycloakException exception = new KeycloakException(message, statusCode, errorCode);

            // Assert
            assertEquals(message, exception.getMessage());
            assertEquals(statusCode, exception.getStatusCode());
            assertEquals(errorCode, exception.getErrorCode());
            assertEquals(message, exception.getErrorMessage());
            assertNull(exception.getCause());
        }
    }

    @Test
    void testConstrutorComDiferentesTiposDeCause_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Erro no Keycloak";
        Throwable[] causes = {
            new RuntimeException("Runtime exception"),
            new IllegalArgumentException("Illegal argument"),
            new NullPointerException("Null pointer"),
            new IllegalStateException("Illegal state")
        };

        for (Throwable cause : causes) {
            // Act
            KeycloakException exception = new KeycloakException(message, cause);

            // Assert
            assertEquals(message, exception.getMessage());
            assertEquals(500, exception.getStatusCode());
            assertEquals("KEYCLOAK_ERROR", exception.getErrorCode());
            assertEquals(message, exception.getErrorMessage());
            assertEquals(cause, exception.getCause());
        }
    }

    @Test
    void testGetters_DeveRetornarValoresCorretos() {
        // Arrange
        String message = "Erro no Keycloak";
        int statusCode = 404;
        String errorCode = "NOT_FOUND";
        Throwable cause = new RuntimeException("Causa");

        // Act
        KeycloakException exception = new KeycloakException(message, statusCode, errorCode);

        // Assert
        assertEquals(statusCode, exception.getStatusCode());
        assertEquals(errorCode, exception.getErrorCode());
        assertEquals(message, exception.getErrorMessage());
    }

    @Test
    void testHerancaDeRuntimeException_DeveFuncionarCorretamente() {
        // Arrange
        String message = "Erro no Keycloak";

        // Act
        KeycloakException exception = new KeycloakException(message);

        // Assert
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
        assertTrue(exception instanceof Throwable);
    }

    @Test
    void testToString_DeveConterInformacoesDaExcecao() {
        // Arrange
        String message = "Erro no Keycloak";
        int statusCode = 500;
        String errorCode = "KEYCLOAK_ERROR";

        // Act
        KeycloakException exception = new KeycloakException(message, statusCode, errorCode);
        String toString = exception.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("KeycloakException"));
        assertTrue(toString.contains(message));
    }

    @Test
    void testEqualsEHashCode_DeveFuncionarCorretamente() {
        // Arrange
        String message = "Erro no Keycloak";
        int statusCode = 500;
        String errorCode = "KEYCLOAK_ERROR";

        KeycloakException exception1 = new KeycloakException(message, statusCode, errorCode);
        KeycloakException exception2 = new KeycloakException(message, statusCode, errorCode);
        KeycloakException exception3 = new KeycloakException("Outro erro", statusCode, errorCode);

        // Act & Assert
        assertNotEquals(exception1, exception2); // Exceções não são iguais por padrão
        assertNotEquals(exception1, exception3);
        assertNotEquals(exception1.hashCode(), exception2.hashCode());
    }
} 
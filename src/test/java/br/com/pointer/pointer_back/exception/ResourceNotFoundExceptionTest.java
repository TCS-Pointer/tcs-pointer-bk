package br.com.pointer.pointer_back.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ResourceNotFoundExceptionTest {

    @Test
    void testConstrutorComMessage_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Recurso não encontrado";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComMessageNula_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = null;

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Assert
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComMessageVazia_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Assert
        assertEquals("", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComMessageLonga_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "O recurso solicitado não foi encontrado no sistema. Verifique se o ID fornecido está correto e tente novamente.";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComMessageComCaracteresEspeciais_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Recurso não encontrado: @#$%^&*()_+-=[]{}|;':\",./<>?";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComMessageComAcentos_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Recurso não encontrado: usuário com ID 123 não existe";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testHerancaDeRuntimeException_DeveFuncionarCorretamente() {
        // Arrange
        String message = "Recurso não encontrado";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Assert
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
        assertTrue(exception instanceof Throwable);
    }

    @Test
    void testToString_DeveConterInformacoesDaExcecao() {
        // Arrange
        String message = "Recurso não encontrado";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message);
        String toString = exception.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("ResourceNotFoundException"));
        assertTrue(toString.contains(message));
    }

    @Test
    void testEqualsEHashCode_DeveFuncionarCorretamente() {
        // Arrange
        String message = "Recurso não encontrado";

        ResourceNotFoundException exception1 = new ResourceNotFoundException(message);
        ResourceNotFoundException exception2 = new ResourceNotFoundException(message);
        ResourceNotFoundException exception3 = new ResourceNotFoundException("Outro recurso não encontrado");

        // Act & Assert
        assertNotEquals(exception1, exception2); // Exceções não são iguais por padrão
        assertNotEquals(exception1, exception3);
        assertNotEquals(exception1.hashCode(), exception2.hashCode());
    }

    @Test
    void testConstrutorComDiferentesTiposDeMensagem_DeveCriarExcecaoCorretamente() {
        // Arrange
        String[] messages = {
            "Usuário não encontrado",
            "Comunicado não encontrado",
            "PDI não encontrado",
            "Feedback não encontrado",
            "Marco PDI não encontrado"
        };

        for (String message : messages) {
            // Act
            ResourceNotFoundException exception = new ResourceNotFoundException(message);

            // Assert
            assertEquals(message, exception.getMessage());
            assertNull(exception.getCause());
        }
    }

    @Test
    void testConstrutorComMensagemComNumeros_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Recurso com ID 12345 não encontrado";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComMensagemComEspacosExtras_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "  Recurso não encontrado  ";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComMensagemComQuebrasDeLinha_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Recurso não encontrado\nVerifique os parâmetros fornecidos";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComMensagemComTabulacao_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Recurso não encontrado\tID: 123";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComMensagemComUnicode_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Recurso não encontrado: 🚫❌⚠️";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComMensagemComHTML_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Recurso não encontrado: <div>ID: 123</div>";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComMensagemComSQL_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Recurso não encontrado: SELECT * FROM users WHERE id = 123";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComMensagemComJSON_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Recurso não encontrado: {\"id\": 123, \"name\": \"teste\"}";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComMensagemComURL_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Recurso não encontrado: https://api.example.com/users/123";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComMensagemComEmail_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Recurso não encontrado: usuario@exemplo.com";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComMensagemComData_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Recurso não encontrado: 2024-01-15T10:30:00Z";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstrutorComMensagemComUUID_DeveCriarExcecaoCorretamente() {
        // Arrange
        String message = "Recurso não encontrado: 550e8400-e29b-41d4-a716-446655440000";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }
} 
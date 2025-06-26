package br.com.pointer.pointer_back.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TokenExpiradoException")
class TokenExpiradoExceptionTest {

    @Test
    @DisplayName("Deve criar exceção com mensagem")
    void deveCriarExcecaoComMensagem() {
        // Given
        String mensagem = "Token expirado";
        
        // When
        TokenExpiradoException exception = new TokenExpiradoException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem vazia")
    void deveCriarExcecaoComMensagemVazia() {
        // Given
        String mensagem = "";
        
        // When
        TokenExpiradoException exception = new TokenExpiradoException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem nula")
    void deveCriarExcecaoComMensagemNula() {
        // Given
        String mensagem = null;
        
        // When
        TokenExpiradoException exception = new TokenExpiradoException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("Deve verificar se é instância de RuntimeException")
    void deveSerInstanciaDeRuntimeException() {
        // Given
        String mensagem = "Token expirado";
        
        // When
        TokenExpiradoException exception = new TokenExpiradoException(mensagem);
        
        // Then
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Deve verificar se é instância de Exception")
    void deveSerInstanciaDeException() {
        // Given
        String mensagem = "Token expirado";
        
        // When
        TokenExpiradoException exception = new TokenExpiradoException(mensagem);
        
        // Then
        assertTrue(exception instanceof Exception);
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem específica de tempo")
    void deveCriarExcecaoComMensagemDeTempo() {
        // Given
        String mensagem = "Token expirado há mais de 24 horas";
        
        // When
        TokenExpiradoException exception = new TokenExpiradoException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem específica de primeiro acesso")
    void deveCriarExcecaoComMensagemDePrimeiroAcesso() {
        // Given
        String mensagem = "Token de primeiro acesso expirado";
        
        // When
        TokenExpiradoException exception = new TokenExpiradoException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem longa")
    void deveCriarExcecaoComMensagemLonga() {
        // Given
        String mensagem = "O token fornecido expirou. Por favor, solicite um novo token para continuar com o processo.";
        
        // When
        TokenExpiradoException exception = new TokenExpiradoException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem específica de JWT")
    void deveCriarExcecaoComMensagemDeJWT() {
        // Given
        String mensagem = "JWT token expirado";
        
        // When
        TokenExpiradoException exception = new TokenExpiradoException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
    }
} 
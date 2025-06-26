package br.com.pointer.pointer_back.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TokenInvalidoException")
class TokenInvalidoExceptionTest {

    @Test
    @DisplayName("Deve criar exceção com mensagem")
    void deveCriarExcecaoComMensagem() {
        // Given
        String mensagem = "Token inválido";
        
        // When
        TokenInvalidoException exception = new TokenInvalidoException(mensagem);
        
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
        TokenInvalidoException exception = new TokenInvalidoException(mensagem);
        
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
        TokenInvalidoException exception = new TokenInvalidoException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("Deve verificar se é instância de RuntimeException")
    void deveSerInstanciaDeRuntimeException() {
        // Given
        String mensagem = "Token inválido";
        
        // When
        TokenInvalidoException exception = new TokenInvalidoException(mensagem);
        
        // Then
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Deve verificar se é instância de Exception")
    void deveSerInstanciaDeException() {
        // Given
        String mensagem = "Token inválido";
        
        // When
        TokenInvalidoException exception = new TokenInvalidoException(mensagem);
        
        // Then
        assertTrue(exception instanceof Exception);
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem específica de formato")
    void deveCriarExcecaoComMensagemDeFormato() {
        // Given
        String mensagem = "Token em formato inválido";
        
        // When
        TokenInvalidoException exception = new TokenInvalidoException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem específica de assinatura")
    void deveCriarExcecaoComMensagemDeAssinatura() {
        // Given
        String mensagem = "Token com assinatura inválida";
        
        // When
        TokenInvalidoException exception = new TokenInvalidoException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem longa")
    void deveCriarExcecaoComMensagemLonga() {
        // Given
        String mensagem = "O token fornecido não é válido. Verifique se o token está correto e não foi manipulado.";
        
        // When
        TokenInvalidoException exception = new TokenInvalidoException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem específica de primeiro acesso")
    void deveCriarExcecaoComMensagemDePrimeiroAcesso() {
        // Given
        String mensagem = "Token de primeiro acesso inválido";
        
        // When
        TokenInvalidoException exception = new TokenInvalidoException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
    }
} 
package br.com.pointer.pointer_back.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EmailInvalidoException")
class EmailInvalidoExceptionTest {

    @Test
    @DisplayName("Deve criar exceção com mensagem")
    void deveCriarExcecaoComMensagem() {
        // Given
        String mensagem = "Email inválido";
        
        // When
        EmailInvalidoException exception = new EmailInvalidoException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem e causa")
    void deveCriarExcecaoComMensagemECausa() {
        // Given
        String mensagem = "Email inválido";
        Throwable causa = new RuntimeException("Causa original");
        
        // When
        EmailInvalidoException exception = new EmailInvalidoException(mensagem, causa);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
        assertEquals(causa, exception.getCause());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem vazia")
    void deveCriarExcecaoComMensagemVazia() {
        // Given
        String mensagem = "";
        
        // When
        EmailInvalidoException exception = new EmailInvalidoException(mensagem);
        
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
        EmailInvalidoException exception = new EmailInvalidoException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem e causa nula")
    void deveCriarExcecaoComMensagemECausaNula() {
        // Given
        String mensagem = "Email inválido";
        Throwable causa = null;
        
        // When
        EmailInvalidoException exception = new EmailInvalidoException(mensagem, causa);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Deve verificar se é instância de RuntimeException")
    void deveSerInstanciaDeRuntimeException() {
        // Given
        String mensagem = "Email inválido";
        
        // When
        EmailInvalidoException exception = new EmailInvalidoException(mensagem);
        
        // Then
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Deve verificar se é instância de Exception")
    void deveSerInstanciaDeException() {
        // Given
        String mensagem = "Email inválido";
        
        // When
        EmailInvalidoException exception = new EmailInvalidoException(mensagem);
        
        // Then
        assertTrue(exception instanceof Exception);
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem específica de formato")
    void deveCriarExcecaoComMensagemDeFormato() {
        // Given
        String mensagem = "Formato de email inválido";
        
        // When
        EmailInvalidoException exception = new EmailInvalidoException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem longa")
    void deveCriarExcecaoComMensagemLonga() {
        // Given
        String mensagem = "O email fornecido não está em um formato válido. Por favor, verifique se contém @ e um domínio válido.";
        
        // When
        EmailInvalidoException exception = new EmailInvalidoException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
    }
} 
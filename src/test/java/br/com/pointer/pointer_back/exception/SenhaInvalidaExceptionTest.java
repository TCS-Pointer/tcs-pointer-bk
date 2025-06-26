package br.com.pointer.pointer_back.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SenhaInvalidaException")
class SenhaInvalidaExceptionTest {

    @Test
    @DisplayName("Deve criar exceção com mensagem")
    void deveCriarExcecaoComMensagem() {
        // Given
        String mensagem = "Senha inválida";
        
        // When
        SenhaInvalidaException exception = new SenhaInvalidaException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem e causa")
    void deveCriarExcecaoComMensagemECausa() {
        // Given
        String mensagem = "Senha inválida";
        Throwable causa = new RuntimeException("Causa original");
        
        // When
        SenhaInvalidaException exception = new SenhaInvalidaException(mensagem, causa);
        
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
        SenhaInvalidaException exception = new SenhaInvalidaException(mensagem);
        
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
        SenhaInvalidaException exception = new SenhaInvalidaException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem e causa nula")
    void deveCriarExcecaoComMensagemECausaNula() {
        // Given
        String mensagem = "Senha inválida";
        Throwable causa = null;
        
        // When
        SenhaInvalidaException exception = new SenhaInvalidaException(mensagem, causa);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Deve verificar se é instância de RuntimeException")
    void deveSerInstanciaDeRuntimeException() {
        // Given
        String mensagem = "Senha inválida";
        
        // When
        SenhaInvalidaException exception = new SenhaInvalidaException(mensagem);
        
        // Then
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Deve verificar se é instância de Exception")
    void deveSerInstanciaDeException() {
        // Given
        String mensagem = "Senha inválida";
        
        // When
        SenhaInvalidaException exception = new SenhaInvalidaException(mensagem);
        
        // Then
        assertTrue(exception instanceof Exception);
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem específica de complexidade")
    void deveCriarExcecaoComMensagemDeComplexidade() {
        // Given
        String mensagem = "Senha deve conter pelo menos 8 caracteres";
        
        // When
        SenhaInvalidaException exception = new SenhaInvalidaException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem específica de caracteres especiais")
    void deveCriarExcecaoComMensagemDeCaracteresEspeciais() {
        // Given
        String mensagem = "Senha deve conter pelo menos um caractere especial";
        
        // When
        SenhaInvalidaException exception = new SenhaInvalidaException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem longa")
    void deveCriarExcecaoComMensagemLonga() {
        // Given
        String mensagem = "A senha fornecida não atende aos critérios de segurança. Deve conter pelo menos 8 caracteres, incluindo letras maiúsculas, minúsculas, números e caracteres especiais.";
        
        // When
        SenhaInvalidaException exception = new SenhaInvalidaException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
    }
} 
package br.com.pointer.pointer_back.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UsuarioJaExisteException")
class UsuarioJaExisteExceptionTest {

    @Test
    @DisplayName("Deve criar exceção com mensagem")
    void deveCriarExcecaoComMensagem() {
        // Given
        String mensagem = "Usuário já existe";
        
        // When
        UsuarioJaExisteException exception = new UsuarioJaExisteException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem e causa")
    void deveCriarExcecaoComMensagemECausa() {
        // Given
        String mensagem = "Usuário já existe";
        Throwable causa = new RuntimeException("Causa original");
        
        // When
        UsuarioJaExisteException exception = new UsuarioJaExisteException(mensagem, causa);
        
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
        UsuarioJaExisteException exception = new UsuarioJaExisteException(mensagem);
        
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
        UsuarioJaExisteException exception = new UsuarioJaExisteException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem e causa nula")
    void deveCriarExcecaoComMensagemECausaNula() {
        // Given
        String mensagem = "Usuário já existe";
        Throwable causa = null;
        
        // When
        UsuarioJaExisteException exception = new UsuarioJaExisteException(mensagem, causa);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Deve verificar se é instância de RuntimeException")
    void deveSerInstanciaDeRuntimeException() {
        // Given
        String mensagem = "Usuário já existe";
        
        // When
        UsuarioJaExisteException exception = new UsuarioJaExisteException(mensagem);
        
        // Then
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Deve verificar se é instância de Exception")
    void deveSerInstanciaDeException() {
        // Given
        String mensagem = "Usuário já existe";
        
        // When
        UsuarioJaExisteException exception = new UsuarioJaExisteException(mensagem);
        
        // Then
        assertTrue(exception instanceof Exception);
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem específica de email")
    void deveCriarExcecaoComMensagemDeEmail() {
        // Given
        String mensagem = "Já existe um usuário com este email";
        
        // When
        UsuarioJaExisteException exception = new UsuarioJaExisteException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem longa")
    void deveCriarExcecaoComMensagemLonga() {
        // Given
        String mensagem = "Usuário já existe no sistema com o email fornecido. Por favor, utilize outro email ou faça login com a conta existente.";
        
        // When
        UsuarioJaExisteException exception = new UsuarioJaExisteException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
    }
} 
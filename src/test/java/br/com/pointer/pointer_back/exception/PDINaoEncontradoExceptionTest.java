package br.com.pointer.pointer_back.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PDINaoEncontradoException")
class PDINaoEncontradoExceptionTest {

    @Test
    @DisplayName("Deve criar exceção com ID")
    void deveCriarExcecaoComId() {
        // Given
        Long id = 123L;
        
        // When
        PDINaoEncontradoException exception = new PDINaoEncontradoException(id);
        
        // Then
        assertNotNull(exception);
        assertEquals("PDI não encontrado com ID: 123", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem personalizada")
    void deveCriarExcecaoComMensagem() {
        // Given
        String mensagem = "PDI não encontrado";
        
        // When
        PDINaoEncontradoException exception = new PDINaoEncontradoException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem e causa")
    void deveCriarExcecaoComMensagemECausa() {
        // Given
        String mensagem = "PDI não encontrado";
        Throwable causa = new RuntimeException("Causa original");
        
        // When
        PDINaoEncontradoException exception = new PDINaoEncontradoException(mensagem, causa);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
        assertEquals(causa, exception.getCause());
    }

    @Test
    @DisplayName("Deve criar exceção com ID zero")
    void deveCriarExcecaoComIdZero() {
        // Given
        Long id = 0L;
        
        // When
        PDINaoEncontradoException exception = new PDINaoEncontradoException(id);
        
        // Then
        assertNotNull(exception);
        assertEquals("PDI não encontrado com ID: 0", exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar exceção com ID negativo")
    void deveCriarExcecaoComIdNegativo() {
        // Given
        Long id = -1L;
        
        // When
        PDINaoEncontradoException exception = new PDINaoEncontradoException(id);
        
        // Then
        assertNotNull(exception);
        assertEquals("PDI não encontrado com ID: -1", exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem vazia")
    void deveCriarExcecaoComMensagemVazia() {
        // Given
        String mensagem = "";
        
        // When
        PDINaoEncontradoException exception = new PDINaoEncontradoException(mensagem);
        
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
        PDINaoEncontradoException exception = new PDINaoEncontradoException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem e causa nula")
    void deveCriarExcecaoComMensagemECausaNula() {
        // Given
        String mensagem = "PDI não encontrado";
        Throwable causa = null;
        
        // When
        PDINaoEncontradoException exception = new PDINaoEncontradoException(mensagem, causa);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Deve verificar se é instância de RuntimeException")
    void deveSerInstanciaDeRuntimeException() {
        // Given
        Long id = 123L;
        
        // When
        PDINaoEncontradoException exception = new PDINaoEncontradoException(id);
        
        // Then
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Deve verificar se é instância de Exception")
    void deveSerInstanciaDeException() {
        // Given
        Long id = 123L;
        
        // When
        PDINaoEncontradoException exception = new PDINaoEncontradoException(id);
        
        // Then
        assertTrue(exception instanceof Exception);
    }
} 
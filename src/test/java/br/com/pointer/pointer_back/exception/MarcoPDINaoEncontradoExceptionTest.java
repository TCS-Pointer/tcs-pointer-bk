package br.com.pointer.pointer_back.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MarcoPDINaoEncontradoException")
class MarcoPDINaoEncontradoExceptionTest {

    @Test
    @DisplayName("Deve criar exceção com ID")
    void deveCriarExcecaoComId() {
        // Given
        Long id = 123L;
        
        // When
        MarcoPDINaoEncontradoException exception = new MarcoPDINaoEncontradoException(id);
        
        // Then
        assertNotNull(exception);
        assertEquals("Marco PDI não encontrado com ID: 123", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem personalizada")
    void deveCriarExcecaoComMensagem() {
        // Given
        String mensagem = "Marco PDI não encontrado";
        
        // When
        MarcoPDINaoEncontradoException exception = new MarcoPDINaoEncontradoException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem e causa")
    void deveCriarExcecaoComMensagemECausa() {
        // Given
        String mensagem = "Marco PDI não encontrado";
        Throwable causa = new RuntimeException("Causa original");
        
        // When
        MarcoPDINaoEncontradoException exception = new MarcoPDINaoEncontradoException(mensagem, causa);
        
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
        MarcoPDINaoEncontradoException exception = new MarcoPDINaoEncontradoException(id);
        
        // Then
        assertNotNull(exception);
        assertEquals("Marco PDI não encontrado com ID: 0", exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar exceção com ID negativo")
    void deveCriarExcecaoComIdNegativo() {
        // Given
        Long id = -1L;
        
        // When
        MarcoPDINaoEncontradoException exception = new MarcoPDINaoEncontradoException(id);
        
        // Then
        assertNotNull(exception);
        assertEquals("Marco PDI não encontrado com ID: -1", exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem vazia")
    void deveCriarExcecaoComMensagemVazia() {
        // Given
        String mensagem = "";
        
        // When
        MarcoPDINaoEncontradoException exception = new MarcoPDINaoEncontradoException(mensagem);
        
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
        MarcoPDINaoEncontradoException exception = new MarcoPDINaoEncontradoException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem e causa nula")
    void deveCriarExcecaoComMensagemECausaNula() {
        // Given
        String mensagem = "Marco PDI não encontrado";
        Throwable causa = null;
        
        // When
        MarcoPDINaoEncontradoException exception = new MarcoPDINaoEncontradoException(mensagem, causa);
        
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
        MarcoPDINaoEncontradoException exception = new MarcoPDINaoEncontradoException(id);
        
        // Then
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Deve verificar se é instância de Exception")
    void deveSerInstanciaDeException() {
        // Given
        Long id = 123L;
        
        // When
        MarcoPDINaoEncontradoException exception = new MarcoPDINaoEncontradoException(id);
        
        // Then
        assertTrue(exception instanceof Exception);
    }
} 
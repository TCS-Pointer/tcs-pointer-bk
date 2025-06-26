package br.com.pointer.pointer_back.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SetorCargoInvalidoException")
class SetorCargoInvalidoExceptionTest {

    @Test
    @DisplayName("Deve criar exceção com mensagem")
    void deveCriarExcecaoComMensagem() {
        // Given
        String mensagem = "Setor ou cargo inválido";
        
        // When
        SetorCargoInvalidoException exception = new SetorCargoInvalidoException(mensagem);
        
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
        SetorCargoInvalidoException exception = new SetorCargoInvalidoException(mensagem);
        
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
        SetorCargoInvalidoException exception = new SetorCargoInvalidoException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("Deve verificar se é instância de RuntimeException")
    void deveSerInstanciaDeRuntimeException() {
        // Given
        String mensagem = "Setor ou cargo inválido";
        
        // When
        SetorCargoInvalidoException exception = new SetorCargoInvalidoException(mensagem);
        
        // Then
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Deve verificar se é instância de Exception")
    void deveSerInstanciaDeException() {
        // Given
        String mensagem = "Setor ou cargo inválido";
        
        // When
        SetorCargoInvalidoException exception = new SetorCargoInvalidoException(mensagem);
        
        // Then
        assertTrue(exception instanceof Exception);
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem específica de setor")
    void deveCriarExcecaoComMensagemDeSetor() {
        // Given
        String mensagem = "Setor inválido: TI";
        
        // When
        SetorCargoInvalidoException exception = new SetorCargoInvalidoException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem específica de cargo")
    void deveCriarExcecaoComMensagemDeCargo() {
        // Given
        String mensagem = "Cargo inválido: Desenvolvedor para o setor: RH";
        
        // When
        SetorCargoInvalidoException exception = new SetorCargoInvalidoException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem longa")
    void deveCriarExcecaoComMensagemLonga() {
        // Given
        String mensagem = "O setor e cargo fornecidos não são válidos. Por favor, verifique se o setor existe e se o cargo é compatível com o setor selecionado.";
        
        // When
        SetorCargoInvalidoException exception = new SetorCargoInvalidoException(mensagem);
        
        // Then
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
    }
} 
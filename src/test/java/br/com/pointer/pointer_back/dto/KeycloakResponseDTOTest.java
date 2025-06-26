package br.com.pointer.pointer_back.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

@DisplayName("KeycloakResponseDTO")
class KeycloakResponseDTOTest {

    @Test
    @DisplayName("Deve criar resposta de sucesso sem dados")
    void deveCriarRespostaDeSucessoSemDados() {
        // Given
        String message = "Operação realizada com sucesso";
        
        // When
        KeycloakResponseDTO response = KeycloakResponseDTO.success(message);
        
        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertNull(response.getData());
        assertNull(response.getErrorCode());
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve criar resposta de sucesso com dados")
    void deveCriarRespostaDeSucessoComDados() {
        // Given
        String message = "Usuário criado com sucesso";
        String userId = "12345";
        
        // When
        KeycloakResponseDTO response = KeycloakResponseDTO.success(message, userId);
        
        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertEquals(userId, response.getData());
        assertNull(response.getErrorCode());
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve criar resposta de erro")
    void deveCriarRespostaDeErro() {
        // Given
        String message = "Erro ao criar usuário";
        String errorCode = "USER_CREATION_ERROR";
        int statusCode = 400;
        
        // When
        KeycloakResponseDTO response = KeycloakResponseDTO.error(message, errorCode, statusCode);
        
        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertNull(response.getData());
        assertEquals(errorCode, response.getErrorCode());
        assertEquals(statusCode, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve criar resposta de erro com mensagem de erro")
    void deveCriarRespostaDeErroComMensagemDeErro() {
        // Given
        String message = "Erro ao criar usuário";
        String errorCode = "USER_CREATION_ERROR";
        String errorMessage = "Detalhes do erro";
        int statusCode = 400;
        
        // When
        KeycloakResponseDTO response = KeycloakResponseDTO.error(message, errorCode, errorMessage, statusCode);
        
        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertNull(response.getData());
        assertEquals(errorCode, response.getErrorCode());
        assertEquals(errorMessage, response.getErrorMessage());
        assertEquals(statusCode, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve criar resposta de sucesso com mensagem vazia")
    void deveCriarRespostaDeSucessoComMensagemVazia() {
        // Given
        String message = "";
        
        // When
        KeycloakResponseDTO response = KeycloakResponseDTO.success(message);
        
        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertNull(response.getData());
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve criar resposta de sucesso com mensagem nula")
    void deveCriarRespostaDeSucessoComMensagemNula() {
        // Given
        String message = null;
        
        // When
        KeycloakResponseDTO response = KeycloakResponseDTO.success(message);
        
        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNull(response.getMessage());
        assertNull(response.getData());
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve criar resposta de erro com mensagem vazia")
    void deveCriarRespostaDeErroComMensagemVazia() {
        // Given
        String message = "";
        String errorCode = "ERROR";
        int statusCode = 500;
        
        // When
        KeycloakResponseDTO response = KeycloakResponseDTO.error(message, errorCode, statusCode);
        
        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertEquals(errorCode, response.getErrorCode());
        assertEquals(statusCode, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve criar resposta de erro com código de erro nulo")
    void deveCriarRespostaDeErroComCodigoNulo() {
        // Given
        String message = "Erro ocorreu";
        String errorCode = null;
        int statusCode = 500;
        
        // When
        KeycloakResponseDTO response = KeycloakResponseDTO.error(message, errorCode, statusCode);
        
        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertNull(response.getErrorCode());
        assertEquals(statusCode, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve criar resposta de sucesso com dados nulos")
    void deveCriarRespostaDeSucessoComDadosNulos() {
        // Given
        String message = "Operação realizada";
        Object data = null;
        
        // When
        KeycloakResponseDTO response = KeycloakResponseDTO.success(message, data);
        
        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertNull(response.getData());
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve criar resposta de sucesso com dados complexos")
    void deveCriarRespostaDeSucessoComDadosComplexos() {
        // Given
        String message = "Dados recuperados";
        Object data = new Object() {
            public String name = "Test";
            public int value = 42;
        };
        
        // When
        KeycloakResponseDTO response = KeycloakResponseDTO.success(message, data);
        
        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertEquals(data, response.getData());
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve verificar equals e hashCode")
    void deveVerificarEqualsEHashCode() {
        // Given
        KeycloakResponseDTO response1 = KeycloakResponseDTO.success("Test", "data");
        KeycloakResponseDTO response2 = KeycloakResponseDTO.success("Test", "data");
        KeycloakResponseDTO response3 = KeycloakResponseDTO.error("Error", "CODE", 400);
        
        // When & Then
        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }

    @Test
    @DisplayName("Deve verificar toString")
    void deveVerificarToString() {
        // Given
        KeycloakResponseDTO response = KeycloakResponseDTO.success("Test", "data");
        
        // When
        String toString = response.toString();
        
        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("success=true"));
        assertTrue(toString.contains("message=Test"));
        assertTrue(toString.contains("data=data"));
    }
} 
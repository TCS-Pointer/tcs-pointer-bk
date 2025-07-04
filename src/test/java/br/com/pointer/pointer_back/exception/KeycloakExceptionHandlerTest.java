package br.com.pointer.pointer_back.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.StatusType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakExceptionHandlerTest {

    @Mock
    private Response mockResponse;
    
    @Mock
    private StatusType mockStatusType;

    @Mock
    private NoResourceFoundException mockNoResourceFoundException;

    @InjectMocks
    private KeycloakExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        // Setup inicial se necessário
    }

    @Test
    void handleNoResourceFoundException_DeveRetornar404() {
        // Arrange
        when(mockNoResourceFoundException.getResourcePath()).thenReturn("/api/endpoint-inexistente");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleNoResourceFoundException(mockNoResourceFoundException);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("Endpoint não encontrado", body.get("message"));
        assertEquals("ENDPOINT_NOT_FOUND", body.get("error"));
        assertEquals("O endpoint '/api/endpoint-inexistente' não existe. Verifique a URL e o método HTTP.", body.get("details"));
        assertEquals(404, body.get("status"));
        assertNotNull(body.get("timestamp"));
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
    }

    @Test
    void handleKeycloakException_DeveRetornarStatusCodeEspecifico() {
        // Arrange
        KeycloakException ex = new KeycloakException("Erro de autenticação", 401, "AUTH_ERROR");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleKeycloakException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("Erro de autenticação", body.get("message"));
        assertEquals("AUTH_ERROR", body.get("error"));
        assertEquals("Erro de autenticação", body.get("details"));
        assertEquals(401, body.get("status"));
        assertNotNull(body.get("timestamp"));
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
    }

    @Test
    void handleKeycloakException_ComStatusCode500_DeveRetornar500() {
        // Arrange
        KeycloakException ex = new KeycloakException("Erro interno do Keycloak", 500, "INTERNAL_ERROR");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleKeycloakException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("Erro interno do Keycloak", body.get("message"));
        assertEquals("INTERNAL_ERROR", body.get("error"));
        assertEquals("Erro interno do Keycloak", body.get("details"));
        assertEquals(500, body.get("status"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handleWebApplicationException_DeveRetornarStatusCodeDoResponse() throws Exception {
        // Arrange
        when(mockStatusType.getStatusCode()).thenReturn(400);
        when(mockResponse.getStatusInfo()).thenReturn(mockStatusType);
        when(mockResponse.getStatus()).thenReturn(400);
        when(mockResponse.readEntity(String.class)).thenReturn("Bad Request Error");
        
        WebApplicationException ex = new WebApplicationException(mockResponse);

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleWebApplicationException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("Erro na operação do Keycloak", body.get("message"));
        assertEquals("KEYCLOAK_WEB_ERROR", body.get("error"));
        assertEquals("Bad Request Error", body.get("details"));
        assertEquals(400, body.get("status"));
        assertNotNull(body.get("timestamp"));
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
        
        verify(mockResponse, atLeastOnce()).getStatus();
        verify(mockResponse).readEntity(String.class);
    }

    @Test
    void handleWebApplicationException_ComStatusCode500_DeveRetornar500() throws Exception {
        // Arrange
        when(mockStatusType.getStatusCode()).thenReturn(500);
        when(mockResponse.getStatusInfo()).thenReturn(mockStatusType);
        when(mockResponse.getStatus()).thenReturn(500);
        when(mockResponse.readEntity(String.class)).thenReturn("Internal Server Error");
        
        WebApplicationException ex = new WebApplicationException(mockResponse);

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleWebApplicationException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("Erro na operação do Keycloak", body.get("message"));
        assertEquals("KEYCLOAK_WEB_ERROR", body.get("error"));
        assertEquals("Internal Server Error", body.get("details"));
        assertEquals(500, body.get("status"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handleGenericException_DeveRetornar500() {
        // Arrange
        Exception ex = new RuntimeException("Erro genérico");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleGenericException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("Erro interno do servidor", body.get("message"));
        assertEquals("INTERNAL_SERVER_ERROR", body.get("error"));
        assertEquals("Erro genérico", body.get("details"));
        assertEquals(500, body.get("status"));
        assertNotNull(body.get("timestamp"));
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
    }

    @Test
    void handleGenericException_ComExcecaoNula_DeveRetornar500() {
        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleGenericException(null);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("Erro interno do servidor", body.get("message"));
        assertEquals("INTERNAL_SERVER_ERROR", body.get("error"));
        assertEquals("Exceção nula", body.get("details"));
        assertEquals(500, body.get("status"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handleNoResourceFoundException_ComResourcePathNulo_DeveRetornar404() {
        // Arrange
        when(mockNoResourceFoundException.getResourcePath()).thenReturn(null);

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleNoResourceFoundException(mockNoResourceFoundException);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("Endpoint não encontrado", body.get("message"));
        assertEquals("ENDPOINT_NOT_FOUND", body.get("error"));
        assertEquals("O endpoint 'null' não existe. Verifique a URL e o método HTTP.", body.get("details"));
        assertEquals(404, body.get("status"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handleNoResourceFoundException_ComResourcePathVazio_DeveRetornar404() {
        // Arrange
        when(mockNoResourceFoundException.getResourcePath()).thenReturn("");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleNoResourceFoundException(mockNoResourceFoundException);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("Endpoint não encontrado", body.get("message"));
        assertEquals("ENDPOINT_NOT_FOUND", body.get("error"));
        assertEquals("O endpoint '' não existe. Verifique a URL e o método HTTP.", body.get("details"));
        assertEquals(404, body.get("status"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handleKeycloakException_ComMensagemNula_DeveRetornarStatusCodeEspecifico() {
        // Arrange
        KeycloakException ex = new KeycloakException(null, 403, "FORBIDDEN");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleKeycloakException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertNull(body.get("message"));
        assertEquals("FORBIDDEN", body.get("error"));
        assertNull(body.get("details"));
        assertEquals(403, body.get("status"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handleKeycloakException_ComErrorCodeNulo_DeveRetornarStatusCodeEspecifico() {
        // Arrange
        KeycloakException ex = new KeycloakException("Erro sem código", 404, null);

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleKeycloakException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("Erro sem código", body.get("message"));
        assertNull(body.get("error"));
        assertEquals("Erro sem código", body.get("details"));
        assertEquals(404, body.get("status"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handleWebApplicationException_ComResponseNulo_DeveRetornar500() throws Exception {
        // Arrange - Não podemos criar WebApplicationException com Response nulo
        // pois isso causa NullPointerException no construtor
        // Vamos testar com um Response mockado que retorna nulo para readEntity
        
        when(mockStatusType.getStatusCode()).thenReturn(500);
        when(mockResponse.getStatusInfo()).thenReturn(mockStatusType);
        when(mockResponse.getStatus()).thenReturn(500);
        when(mockResponse.readEntity(String.class)).thenReturn(null);
        
        WebApplicationException ex = new WebApplicationException(mockResponse);

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleWebApplicationException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("Erro na operação do Keycloak", body.get("message"));
        assertEquals("KEYCLOAK_WEB_ERROR", body.get("error"));
        assertNull(body.get("details"));
        assertEquals(500, body.get("status"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handleWebApplicationException_ComReadEntityException_DeveRetornarStatusCode() throws Exception {
        // Arrange
        when(mockStatusType.getStatusCode()).thenReturn(400);
        when(mockResponse.getStatusInfo()).thenReturn(mockStatusType);
        when(mockResponse.getStatus()).thenReturn(400);
        when(mockResponse.readEntity(String.class)).thenThrow(new RuntimeException("Erro ao ler entidade"));
        
        WebApplicationException ex = new WebApplicationException(mockResponse);

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleWebApplicationException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("Erro na operação do Keycloak", body.get("message"));
        assertEquals("KEYCLOAK_WEB_ERROR", body.get("error"));
        assertEquals("Erro ao processar resposta do Keycloak", body.get("details"));
        assertEquals(400, body.get("status"));
        assertNotNull(body.get("timestamp"));
        
        verify(mockResponse, atLeastOnce()).getStatus();
        verify(mockResponse).readEntity(String.class);
    }

    @Test
    void handleGenericException_ComExcecaoCustomizada_DeveRetornar500() {
        // Arrange
        Exception ex = new IllegalArgumentException("Argumento inválido");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleGenericException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("Erro interno do servidor", body.get("message"));
        assertEquals("INTERNAL_SERVER_ERROR", body.get("error"));
        assertEquals("Argumento inválido", body.get("details"));
        assertEquals(500, body.get("status"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handleGenericException_ComExcecaoComMensagemLonga_DeveRetornar500() {
        // Arrange
        String mensagemLonga = "Esta é uma mensagem muito longa para testar o comportamento do handler com textos extensos que podem conter muitos caracteres e informações detalhadas sobre o erro";
        Exception ex = new RuntimeException(mensagemLonga);

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleGenericException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("Erro interno do servidor", body.get("message"));
        assertEquals("INTERNAL_SERVER_ERROR", body.get("error"));
        assertEquals(mensagemLonga, body.get("details"));
        assertEquals(500, body.get("status"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handleGenericException_ComExcecaoComCaracteresEspeciais_DeveRetornar500() {
        // Arrange
        String mensagemEspecial = "Erro com caracteres especiais: áéíóú çãõ ñ";
        Exception ex = new RuntimeException(mensagemEspecial);

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleGenericException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("Erro interno do servidor", body.get("message"));
        assertEquals("INTERNAL_SERVER_ERROR", body.get("error"));
        assertEquals(mensagemEspecial, body.get("details"));
        assertEquals(500, body.get("status"));
        assertNotNull(body.get("timestamp"));
    }
} 
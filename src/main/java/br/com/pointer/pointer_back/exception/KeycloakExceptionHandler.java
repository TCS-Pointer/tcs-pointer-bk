package br.com.pointer.pointer_back.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ControllerAdvice
public class KeycloakExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(KeycloakExceptionHandler.class);

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleNoResourceFoundException(NoResourceFoundException ex) {
        logger.error("Endpoint não encontrado: {}", ex.getMessage(), ex);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Endpoint não encontrado");
        response.put("error", "ENDPOINT_NOT_FOUND");
        response.put("details",
                "O endpoint '" + ex.getResourcePath() + "' não existe. Verifique a URL e o método HTTP.");
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.NOT_FOUND.value());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(KeycloakException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleKeycloakException(KeycloakException ex) {
        logger.error("Erro no Keycloak: {}", ex.getMessage(), ex);

        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getErrorMessage());
        response.put("error", ex.getErrorCode());
        response.put("details", ex.getMessage());
        response.put("timestamp", LocalDateTime.now());
        response.put("status", ex.getStatusCode());

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(WebApplicationException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleWebApplicationException(WebApplicationException ex) {
        logger.error("Erro na aplicação web: {}", ex.getMessage(), ex);
        Response response = ex.getResponse();
        String errorMessage = response.readEntity(String.class);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Erro na operação do Keycloak");
        responseBody.put("error", "KEYCLOAK_WEB_ERROR");
        responseBody.put("details", errorMessage);
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("status", response.getStatus());

        return ResponseEntity
                .status(response.getStatus())
                .body(responseBody);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        logger.error("Erro não tratado: {}", ex.getMessage(), ex);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Erro interno do servidor");
        response.put("error", "INTERNAL_SERVER_ERROR");
        response.put("details", ex.getMessage());
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
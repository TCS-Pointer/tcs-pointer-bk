package br.com.pointer.pointer_back.exception;

import br.com.pointer.pointer_back.dto.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UsuarioJaExisteException.class)
    public ResponseEntity<ApiResponse<Void>> handleUsuarioJaExisteException(UsuarioJaExisteException ex) {
        logger.error("Usuário já existe: ", ex);
        ApiResponse<Void> response = new ApiResponse<Void>().conflict(ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(EmailInvalidoException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailInvalidoException(EmailInvalidoException ex) {
        logger.error("Email inválido: ", ex);
        ApiResponse<Void> response = new ApiResponse<Void>().badRequest(ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(SenhaInvalidaException.class)
    public ResponseEntity<ApiResponse<Void>> handleSenhaInvalidaException(SenhaInvalidaException ex) {
        logger.error("Senha inválida: ", ex);
        ApiResponse<Void> response = new ApiResponse<Void>().badRequest(ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(KeycloakException.class)
    public ResponseEntity<ApiResponse<Void>> handleKeycloakException(KeycloakException ex) {
        logger.error("Erro no Keycloak: ", ex);
        ApiResponse<Void> response = new ApiResponse<Void>().badRequest(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        logger.error("Erro não tratado: ", e);
        ApiResponse<Void> response = new ApiResponse<Void>().badRequest(
                "Erro interno do servidor: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.error("Erro de validação: ", e);
        ApiResponse<Void> response = new ApiResponse<Void>().badRequest(e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException e) {
        logger.error("Erro de validação: ", e);
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        ApiResponse<Void> response = new ApiResponse<Void>().badRequest("Erro de validação", errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(ConstraintViolationException e) {
        logger.error("Erro de validação: ", e);
        List<String> errors = e.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());
        ApiResponse<Void> response = new ApiResponse<Void>().badRequest("Erro de validação", errors);
        return ResponseEntity.badRequest().body(response);
    }
}
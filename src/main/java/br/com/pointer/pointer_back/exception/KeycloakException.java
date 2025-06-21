package br.com.pointer.pointer_back.exception;

public class KeycloakException extends RuntimeException {
    private final int statusCode;
    private final String errorCode;
    private final String errorMessage;

    public KeycloakException(String message) {
        super(message);
        this.statusCode = 500;
        this.errorCode = "KEYCLOAK_ERROR";
        this.errorMessage = message;
    }

    public KeycloakException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = "KEYCLOAK_ERROR";
        this.errorMessage = message;
    }

    public KeycloakException(String message, int statusCode, String errorCode) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.errorMessage = message;
    }

    public KeycloakException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 500;
        this.errorCode = "KEYCLOAK_ERROR";
        this.errorMessage = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
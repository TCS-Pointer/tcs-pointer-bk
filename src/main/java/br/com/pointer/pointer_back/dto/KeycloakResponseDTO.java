package br.com.pointer.pointer_back.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakResponseDTO {
    private boolean success;
    private String message;
    private String errorCode;
    private String errorMessage;
    private int statusCode;
    private Object data;

    public static KeycloakResponseDTO success(String message) {
        return KeycloakResponseDTO.builder()
                .success(true)
                .message(message)
                .statusCode(200)
                .build();
    }

    public static KeycloakResponseDTO success(String message, Object data) {
        return KeycloakResponseDTO.builder()
                .success(true)
                .message(message)
                .statusCode(200)
                .data(data)
                .build();
    }

    public static KeycloakResponseDTO error(String message, String errorCode, int statusCode) {
        return KeycloakResponseDTO.builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .statusCode(statusCode)
                .build();
    }

    public static KeycloakResponseDTO error(String message, String errorCode, String errorMessage, int statusCode) {
        return KeycloakResponseDTO.builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .statusCode(statusCode)
                .build();
    }
}
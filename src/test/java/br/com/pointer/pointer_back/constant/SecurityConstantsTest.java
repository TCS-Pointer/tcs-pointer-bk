package br.com.pointer.pointer_back.constant;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConstantsTest {

    @Test
    void testPublicUrls_DeveConterUrlsCorretas() {
        // Act
        List<String> publicUrls = SecurityConstants.PUBLIC_URLS;

        // Assert
        assertNotNull(publicUrls);
        assertFalse(publicUrls.isEmpty());
        assertTrue(publicUrls.contains("/token/**"));
        assertTrue(publicUrls.contains("/api/usuarios/verificar-codigo"));
        assertTrue(publicUrls.contains("/api/usuarios/redefinir-senha"));
        assertTrue(publicUrls.contains("/api/usuarios/esqueceu-senha"));
        assertTrue(publicUrls.contains("/api/usuarios/primeiro-acesso"));
        assertTrue(publicUrls.contains("/api/usuarios/primeiro-acesso/reenviar"));
        assertTrue(publicUrls.contains("/api/2fa/verify"));
        assertEquals(7, publicUrls.size());
    }

    @Test
    void testExcludedPaths_DeveConterPathsCorretos() {
        // Act
        List<String> excludedPaths = SecurityConstants.EXCLUDED_PATHS;

        // Assert
        assertNotNull(excludedPaths);
        assertFalse(excludedPaths.isEmpty());
        assertTrue(excludedPaths.contains("/token"));
        assertTrue(excludedPaths.contains("/api/usuarios/atualizar-senha"));
        assertTrue(excludedPaths.contains("/api/usuarios/primeiro-acesso"));
        assertTrue(excludedPaths.contains("/api/usuarios/primeiro-acesso/reenviar"));
        assertTrue(excludedPaths.contains("/api/usuarios/redefinir-senha"));
        assertTrue(excludedPaths.contains("/api/usuarios/esqueceu-senha"));
        assertTrue(excludedPaths.contains("/api/usuarios/verificar-codigo"));
        assertTrue(excludedPaths.contains("/api/2fa/verify"));
        assertTrue(excludedPaths.contains("/error"));
        assertEquals(9, excludedPaths.size());
    }

    @Test
    void testAllowedOrigins_DeveConterOriginsCorretos() {
        // Act
        List<String> allowedOrigins = SecurityConstants.ALLOWED_ORIGINS;

        // Assert
        assertNotNull(allowedOrigins);
        assertFalse(allowedOrigins.isEmpty());
        assertTrue(allowedOrigins.contains("http://localhost:3000"));
        assertTrue(allowedOrigins.contains("http://localhost:8080"));
        assertTrue(allowedOrigins.contains("http://127.0.0.1:3000"));
        assertTrue(allowedOrigins.contains("http://127.0.0.1:8080"));
        assertEquals(4, allowedOrigins.size());
    }

    @Test
    void testAllowedMethods_DeveConterMetodosCorretos() {
        // Act
        List<String> allowedMethods = SecurityConstants.ALLOWED_METHODS;

        // Assert
        assertNotNull(allowedMethods);
        assertFalse(allowedMethods.isEmpty());
        assertTrue(allowedMethods.contains("GET"));
        assertTrue(allowedMethods.contains("POST"));
        assertTrue(allowedMethods.contains("PUT"));
        assertTrue(allowedMethods.contains("DELETE"));
        assertTrue(allowedMethods.contains("OPTIONS"));
        assertTrue(allowedMethods.contains("HEAD"));
        assertTrue(allowedMethods.contains("TRACE"));
        assertTrue(allowedMethods.contains("CONNECT"));
        assertTrue(allowedMethods.contains("PATCH"));
        assertEquals(9, allowedMethods.size());
    }

    @Test
    void testAllowedHeaders_DeveConterHeadersCorretos() {
        // Act
        List<String> allowedHeaders = SecurityConstants.ALLOWED_HEADERS;

        // Assert
        assertNotNull(allowedHeaders);
        assertFalse(allowedHeaders.isEmpty());
        assertTrue(allowedHeaders.contains("Authorization"));
        assertTrue(allowedHeaders.contains("Content-Type"));
        assertTrue(allowedHeaders.contains("Accept"));
        assertEquals(3, allowedHeaders.size());
    }

    @Test
    void testCorsMaxAge_DeveTerValorCorreto() {
        // Act
        long corsMaxAge = SecurityConstants.CORS_MAX_AGE;

        // Assert
        assertEquals(3600L, corsMaxAge);
        assertTrue(corsMaxAge > 0);
    }

    @Test
    void testAuthorizationHeader_DeveTerValorCorreto() {
        // Act
        String authorizationHeader = SecurityConstants.AUTHORIZATION_HEADER;

        // Assert
        assertEquals("Authorization", authorizationHeader);
        assertNotNull(authorizationHeader);
        assertFalse(authorizationHeader.isEmpty());
    }

    @Test
    void testBearerPrefix_DeveTerValorCorreto() {
        // Act
        String bearerPrefix = SecurityConstants.BEARER_PREFIX;

        // Assert
        assertEquals("Bearer ", bearerPrefix);
        assertNotNull(bearerPrefix);
        assertFalse(bearerPrefix.isEmpty());
        assertTrue(bearerPrefix.endsWith(" "));
    }

    @Test
    void testExpClaim_DeveTerValorCorreto() {
        // Act
        String expClaim = SecurityConstants.EXP_CLAIM;

        // Assert
        assertEquals("exp", expClaim);
        assertNotNull(expClaim);
        assertFalse(expClaim.isEmpty());
    }

    @Test
    void testExpiresInClaim_DeveTerValorCorreto() {
        // Act
        String expiresInClaim = SecurityConstants.EXPIRES_IN_CLAIM;

        // Assert
        assertEquals("expires_in", expiresInClaim);
        assertNotNull(expiresInClaim);
        assertFalse(expiresInClaim.isEmpty());
    }

    @Test
    void testPreferredUsernameClaim_DeveTerValorCorreto() {
        // Act
        String preferredUsernameClaim = SecurityConstants.PREFERRED_USERNAME_CLAIM;

        // Assert
        assertEquals("preferred_username", preferredUsernameClaim);
        assertNotNull(preferredUsernameClaim);
        assertFalse(preferredUsernameClaim.isEmpty());
    }

    @Test
    void testTokenMissing_DeveTerValorCorreto() {
        // Act
        String tokenMissing = SecurityConstants.TOKEN_MISSING;

        // Assert
        assertEquals("TOKEN_MISSING", tokenMissing);
        assertNotNull(tokenMissing);
        assertFalse(tokenMissing.isEmpty());
    }

    @Test
    void testInvalidToken_DeveTerValorCorreto() {
        // Act
        String invalidToken = SecurityConstants.INVALID_TOKEN;

        // Assert
        assertEquals("INVALID_TOKEN", invalidToken);
        assertNotNull(invalidToken);
        assertFalse(invalidToken.isEmpty());
    }

    @Test
    void testTokenExpired_DeveTerValorCorreto() {
        // Act
        String tokenExpired = SecurityConstants.TOKEN_EXPIRED;

        // Assert
        assertEquals("TOKEN_EXPIRED", tokenExpired);
        assertNotNull(tokenExpired);
        assertFalse(tokenExpired.isEmpty());
    }

    @Test
    void testTokenSessionExpired_DeveTerValorCorreto() {
        // Act
        String tokenSessionExpired = SecurityConstants.TOKEN_SESSION_EXPIRED;

        // Assert
        assertEquals("TOKEN_SESSION_EXPIRED", tokenSessionExpired);
        assertNotNull(tokenSessionExpired);
        assertFalse(tokenSessionExpired.isEmpty());
    }

    @Test
    void testTokenValidationError_DeveTerValorCorreto() {
        // Act
        String tokenValidationError = SecurityConstants.TOKEN_VALIDATION_ERROR;

        // Assert
        assertEquals("TOKEN_VALIDATION_ERROR", tokenValidationError);
        assertNotNull(tokenValidationError);
        assertFalse(tokenValidationError.isEmpty());
    }

    @Test
    void testUserNotFound_DeveTerValorCorreto() {
        // Act
        String userNotFound = SecurityConstants.USER_NOT_FOUND;

        // Assert
        assertEquals("USER_NOT_FOUND", userNotFound);
        assertNotNull(userNotFound);
        assertFalse(userNotFound.isEmpty());
    }

    @Test
    void testAccountDisabled_DeveTerValorCorreto() {
        // Act
        String accountDisabled = SecurityConstants.ACCOUNT_DISABLED;

        // Assert
        assertEquals("ACCOUNT_DISABLED", accountDisabled);
        assertNotNull(accountDisabled);
        assertFalse(accountDisabled.isEmpty());
    }

    @Test
    void testTokenNotProvided_DeveTerValorCorreto() {
        // Act
        String tokenNotProvided = SecurityConstants.TOKEN_NOT_PROVIDED;

        // Assert
        assertEquals("Token não fornecido", tokenNotProvided);
        assertNotNull(tokenNotProvided);
        assertFalse(tokenNotProvided.isEmpty());
    }

    @Test
    void testInvalidTokenFormat_DeveTerValorCorreto() {
        // Act
        String invalidTokenFormat = SecurityConstants.INVALID_TOKEN_FORMAT;

        // Assert
        assertEquals("Token inválido: formato incorreto", invalidTokenFormat);
        assertNotNull(invalidTokenFormat);
        assertFalse(invalidTokenFormat.isEmpty());
    }

    @Test
    void testExpirationNotFound_DeveTerValorCorreto() {
        // Act
        String expirationNotFound = SecurityConstants.EXPIRATION_NOT_FOUND;

        // Assert
        assertEquals("Token inválido: expiração não encontrada", expirationNotFound);
        assertNotNull(expirationNotFound);
        assertFalse(expirationNotFound.isEmpty());
    }

    @Test
    void testInvalidExpirationFormat_DeveTerValorCorreto() {
        // Act
        String invalidExpirationFormat = SecurityConstants.INVALID_EXPIRATION_FORMAT;

        // Assert
        assertEquals("Token inválido: formato de expiração incorreto", invalidExpirationFormat);
        assertNotNull(invalidExpirationFormat);
        assertFalse(invalidExpirationFormat.isEmpty());
    }

    @Test
    void testTokenExpiredMsg_DeveTerValorCorreto() {
        // Act
        String tokenExpiredMsg = SecurityConstants.TOKEN_EXPIRED_MSG;

        // Assert
        assertEquals("Token expirado", tokenExpiredMsg);
        assertNotNull(tokenExpiredMsg);
        assertFalse(tokenExpiredMsg.isEmpty());
    }

    @Test
    void testTokenSessionExpiredMsg_DeveTerValorCorreto() {
        // Act
        String tokenSessionExpiredMsg = SecurityConstants.TOKEN_SESSION_EXPIRED_MSG;

        // Assert
        assertEquals("Token expirado por tempo de sessão", tokenSessionExpiredMsg);
        assertNotNull(tokenSessionExpiredMsg);
        assertFalse(tokenSessionExpiredMsg.isEmpty());
    }

    @Test
    void testUsernameNotFound_DeveTerValorCorreto() {
        // Act
        String usernameNotFound = SecurityConstants.USERNAME_NOT_FOUND;

        // Assert
        assertEquals("Token inválido: username não encontrado", usernameNotFound);
        assertNotNull(usernameNotFound);
        assertFalse(usernameNotFound.isEmpty());
    }

    @Test
    void testUserNotFoundMsg_DeveTerValorCorreto() {
        // Act
        String userNotFoundMsg = SecurityConstants.USER_NOT_FOUND_MSG;

        // Assert
        assertEquals("Usuário não encontrado no Keycloak", userNotFoundMsg);
        assertNotNull(userNotFoundMsg);
        assertFalse(userNotFoundMsg.isEmpty());
    }

    @Test
    void testAccountDisabledMsg_DeveTerValorCorreto() {
        // Act
        String accountDisabledMsg = SecurityConstants.ACCOUNT_DISABLED_MSG;

        // Assert
        assertEquals("Conta desativada. Entre em contato com o administrador do sistema.", accountDisabledMsg);
        assertNotNull(accountDisabledMsg);
        assertFalse(accountDisabledMsg.isEmpty());
    }

    @Test
    void testPublicUrls_DeveSerImutavel() {
        // Act
        List<String> publicUrls = SecurityConstants.PUBLIC_URLS;

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> {
            publicUrls.add("/nova-url");
        });
    }

    @Test
    void testExcludedPaths_DeveSerImutavel() {
        // Act
        List<String> excludedPaths = SecurityConstants.EXCLUDED_PATHS;

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> {
            excludedPaths.add("/novo-path");
        });
    }

    @Test
    void testAllowedOrigins_DeveSerImutavel() {
        // Act
        List<String> allowedOrigins = SecurityConstants.ALLOWED_ORIGINS;

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> {
            allowedOrigins.add("http://novo-origin.com");
        });
    }

    @Test
    void testAllowedMethods_DeveSerImutavel() {
        // Act
        List<String> allowedMethods = SecurityConstants.ALLOWED_METHODS;

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> {
            allowedMethods.add("CUSTOM");
        });
    }

    @Test
    void testAllowedHeaders_DeveSerImutavel() {
        // Act
        List<String> allowedHeaders = SecurityConstants.ALLOWED_HEADERS;

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> {
            allowedHeaders.add("Custom-Header");
        });
    }

    @Test
    void testBearerPrefix_DeveConterEspaco() {
        // Act
        String bearerPrefix = SecurityConstants.BEARER_PREFIX;

        // Assert
        assertTrue(bearerPrefix.contains(" "));
        assertTrue(bearerPrefix.startsWith("Bearer"));
        assertEquals("Bearer ", bearerPrefix);
    }

    @Test
    void testCorsMaxAge_DeveSerPositivo() {
        // Act
        long corsMaxAge = SecurityConstants.CORS_MAX_AGE;

        // Assert
        assertTrue(corsMaxAge > 0);
        assertTrue(corsMaxAge <= 86400); // Máximo 24 horas
    }

    @Test
    void testClaims_DeveSerValoresUnicos() {
        // Act
        String expClaim = SecurityConstants.EXP_CLAIM;
        String expiresInClaim = SecurityConstants.EXPIRES_IN_CLAIM;
        String preferredUsernameClaim = SecurityConstants.PREFERRED_USERNAME_CLAIM;

        // Assert
        assertNotEquals(expClaim, expiresInClaim);
        assertNotEquals(expClaim, preferredUsernameClaim);
        assertNotEquals(expiresInClaim, preferredUsernameClaim);
    }

    @Test
    void testErrorCodes_DeveSerValoresUnicos() {
        // Act
        String tokenMissing = SecurityConstants.TOKEN_MISSING;
        String invalidToken = SecurityConstants.INVALID_TOKEN;
        String tokenExpired = SecurityConstants.TOKEN_EXPIRED;
        String tokenSessionExpired = SecurityConstants.TOKEN_SESSION_EXPIRED;
        String tokenValidationError = SecurityConstants.TOKEN_VALIDATION_ERROR;
        String userNotFound = SecurityConstants.USER_NOT_FOUND;
        String accountDisabled = SecurityConstants.ACCOUNT_DISABLED;

        // Assert
        assertNotEquals(tokenMissing, invalidToken);
        assertNotEquals(tokenMissing, tokenExpired);
        assertNotEquals(tokenMissing, tokenSessionExpired);
        assertNotEquals(tokenMissing, tokenValidationError);
        assertNotEquals(tokenMissing, userNotFound);
        assertNotEquals(tokenMissing, accountDisabled);
    }
} 
package br.com.pointer.pointer_back.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pointer.pointer_back.constant.SecurityConstants;
import br.com.pointer.pointer_back.exception.KeycloakException;

public final class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int JWT_PARTS_COUNT = 3;
    private static final int JWT_PAYLOAD_INDEX = 1;

    private JwtUtil() {
        // Construtor privado para evitar instanciação
    }

    /**
     * Extrai o token do header Authorization
     */
    public static String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(SecurityConstants.BEARER_PREFIX)) {
            return authorizationHeader.substring(SecurityConstants.BEARER_PREFIX.length());
        }
        return null;
    }

    /**
     * Decodifica o payload do JWT
     */
    public static Map<String, Object> decodeJwtPayload(String token) throws KeycloakException {
        try {
            String[] parts = token.split("\\.");
            validateJwtFormat(parts);

            String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[JWT_PAYLOAD_INDEX]));
            return objectMapper.readValue(payload, Map.class);
        } catch (Exception e) {
            logger.error("Erro ao decodificar payload do JWT: {}", e.getMessage(), e);
            throw new KeycloakException(
                    SecurityConstants.INVALID_TOKEN_FORMAT,
                    HttpStatus.UNAUTHORIZED.value(),
                    SecurityConstants.INVALID_TOKEN
            );
        }
    }

    /**
     * Valida o formato do JWT
     */
    public static void validateJwtFormat(String[] parts) throws KeycloakException {
        if (parts.length != JWT_PARTS_COUNT) {
            throw new KeycloakException(
                    SecurityConstants.INVALID_TOKEN_FORMAT,
                    HttpStatus.UNAUTHORIZED.value(),
                    SecurityConstants.INVALID_TOKEN
            );
        }
    }

    /**
     * Extrai o valor de expiração do token
     */
    public static long extractExpirationTime(Map<String, Object> claims) throws KeycloakException {
        Object expObj = claims.get(SecurityConstants.EXP_CLAIM);
        if (expObj == null) {
            throw new KeycloakException(
                    SecurityConstants.EXPIRATION_NOT_FOUND,
                    HttpStatus.UNAUTHORIZED.value(),
                    SecurityConstants.INVALID_TOKEN
            );
        }

        if (expObj instanceof Integer) {
            return ((Integer) expObj).longValue();
        } else if (expObj instanceof Long) {
            return (Long) expObj;
        } else {
            throw new KeycloakException(
                    SecurityConstants.INVALID_EXPIRATION_FORMAT,
                    HttpStatus.UNAUTHORIZED.value(),
                    SecurityConstants.INVALID_TOKEN
            );
        }
    }

    /**
     * Extrai o tempo de expiração da sessão
     */
    public static int extractSessionExpiration(Map<String, Object> claims, int defaultExpiresIn) {
        Object expiresInObj = claims.get(SecurityConstants.EXPIRES_IN_CLAIM);

        if (expiresInObj instanceof Integer) {
            return (Integer) expiresInObj;
        } else if (expiresInObj instanceof Long) {
            return ((Long) expiresInObj).intValue();
        } else {
            return defaultExpiresIn;
        }
    }

    /**
     * Extrai o username do token
     */
    public static String extractUsername(Map<String, Object> claims) throws KeycloakException {
        String username = (String) claims.get(SecurityConstants.PREFERRED_USERNAME_CLAIM);
        if (username == null) {
            throw new KeycloakException(
                    SecurityConstants.USERNAME_NOT_FOUND,
                    HttpStatus.UNAUTHORIZED.value(),
                    SecurityConstants.INVALID_TOKEN
            );
        }
        return username;
    }

    /**
     * Verifica se o token expirou
     */
    public static boolean isTokenExpired(long expirationTime) {
        return System.currentTimeMillis() / 1000 >= expirationTime;
    }

    /**
     * Verifica se a sessão expirou
     */
    public static boolean isSessionExpired(long expirationTime, int sessionExpiration) {
        long currentTime = System.currentTimeMillis() / 1000;
        long tokenCreationTime = expirationTime - sessionExpiration;
        long timeElapsed = currentTime - tokenCreationTime;

        return timeElapsed >= sessionExpiration;
    }
} 
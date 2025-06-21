package br.com.pointer.pointer_back.auth;

import java.util.List;
import java.util.Map;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import br.com.pointer.pointer_back.constant.SecurityConstants;
import br.com.pointer.pointer_back.exception.KeycloakException;
import br.com.pointer.pointer_back.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TokenValidationInterceptor implements HandlerInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(TokenValidationInterceptor.class);

    private final Keycloak keycloak;
    private final String realm;
    private final int defaultExpiresIn;

    public TokenValidationInterceptor(Keycloak keycloak,
                                    @Value("${keycloak.realm:pointer}") String realm,
                                    @Value("${keycloak.token.expires-in:36000}") int defaultExpiresIn) {
        this.keycloak = keycloak;
        this.realm = realm;
        this.defaultExpiresIn = defaultExpiresIn;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        
        if (isPublicEndpoint(request.getRequestURI())) {
            return true;
        }

        String token = extractToken(request);
        validateTokenPresence(token);
        
        try {
            validateTokenAndUser(token);
            return true;
        } catch (KeycloakException e) {
            logger.error("Erro de validação do Keycloak: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao validar token: {}", e.getMessage(), e);
            throw createTokenValidationError(e.getMessage());
        }
    }

    private boolean isPublicEndpoint(String requestUri) {
        return SecurityConstants.EXCLUDED_PATHS.stream()
                .anyMatch(requestUri::contains);
    }

    private String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);
        return JwtUtil.extractTokenFromHeader(authorizationHeader);
    }

    private void validateTokenPresence(String token) {
        if (token == null) {
            throw new KeycloakException(
                SecurityConstants.TOKEN_NOT_PROVIDED,
                HttpStatus.UNAUTHORIZED.value(),
                SecurityConstants.TOKEN_MISSING
            );
        }
    }

    private void validateTokenAndUser(String token) {
        TokenValidationResult validationResult = validateToken(token);
        if (!validationResult.isValid()) {
            throw new KeycloakException(
                validationResult.getErrorMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                validationResult.getErrorCode()
            );
        }

        validateUserStatus(token);
    }

    private TokenValidationResult validateToken(String token) {
        try {
            Map<String, Object> claims = JwtUtil.decodeJwtPayload(token);
            
            long expirationTime = JwtUtil.extractExpirationTime(claims);
            if (JwtUtil.isTokenExpired(expirationTime)) {
                return new TokenValidationResult(false, SecurityConstants.TOKEN_EXPIRED_MSG, SecurityConstants.TOKEN_EXPIRED);
            }

            int sessionExpiration = JwtUtil.extractSessionExpiration(claims, defaultExpiresIn);
            if (JwtUtil.isSessionExpired(expirationTime, sessionExpiration)) {
                return new TokenValidationResult(false, SecurityConstants.TOKEN_SESSION_EXPIRED_MSG, SecurityConstants.TOKEN_SESSION_EXPIRED);
            }

            return new TokenValidationResult(true, null, null);
        } catch (KeycloakException e) {
            return new TokenValidationResult(false, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            logger.error("Erro ao validar token: {}", e.getMessage(), e);
            return new TokenValidationResult(false,
                String.format("Erro ao validar token: %s", e.getMessage()),
                SecurityConstants.TOKEN_VALIDATION_ERROR);
        }
    }

    private void validateUserStatus(String token) {
        try {
            Map<String, Object> claims = JwtUtil.decodeJwtPayload(token);
            String username = JwtUtil.extractUsername(claims);
            
            UsersResource usersResource = keycloak.realm(realm).users();
            List<UserRepresentation> users = usersResource.search(username);

            if (users.isEmpty()) {
                throw new KeycloakException(
                    SecurityConstants.USER_NOT_FOUND_MSG,
                    HttpStatus.UNAUTHORIZED.value(),
                    SecurityConstants.USER_NOT_FOUND
                );
            }

            UserRepresentation user = users.get(0);
            if (!user.isEnabled()) {
                throw new KeycloakException(
                    SecurityConstants.ACCOUNT_DISABLED_MSG,
                    HttpStatus.FORBIDDEN.value(),
                    SecurityConstants.ACCOUNT_DISABLED
                );
            }
        } catch (KeycloakException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao validar status do usuário: {}", e.getMessage(), e);
            throw createTokenValidationError(e.getMessage());
        }
    }

    private KeycloakException createTokenValidationError(String message) {
        return new KeycloakException(
            String.format("Erro ao validar token: %s", message),
            HttpStatus.UNAUTHORIZED.value(),
            SecurityConstants.TOKEN_VALIDATION_ERROR
        );
    }

    private static class TokenValidationResult {
        private final boolean isValid;
        private final String errorMessage;
        private final String errorCode;

        public TokenValidationResult(boolean isValid, String errorMessage, String errorCode) {
            this.isValid = isValid;
            this.errorMessage = errorMessage;
            this.errorCode = errorCode;
        }

        public boolean isValid() {
            return isValid;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public String getErrorCode() {
            return errorCode;
        }
    }
}
package br.com.pointer.pointer_back.auth;

import java.util.List;
import java.util.Map;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
<<<<<<< HEAD
=======
import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> 3c46f92a3eab74bba1b2fc31a3bd29ad2f03f3ce
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

<<<<<<< HEAD
import br.com.pointer.pointer_back.constant.SecurityConstants;
import br.com.pointer.pointer_back.exception.KeycloakException;
import br.com.pointer.pointer_back.util.JwtUtil;
=======
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pointer.pointer_back.exception.KeycloakException;
>>>>>>> 3c46f92a3eab74bba1b2fc31a3bd29ad2f03f3ce
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TokenValidationInterceptor implements HandlerInterceptor {
<<<<<<< HEAD
    
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
=======
    private static final Logger logger = LoggerFactory.getLogger(TokenValidationInterceptor.class);

    @Autowired
    private Keycloak keycloak;

    @Value("${keycloak.realm:pointer}")
    private String realm;

    @Value("${keycloak.token.expires-in:36000}")
    private int defaultExpiresIn;
>>>>>>> 3c46f92a3eab74bba1b2fc31a3bd29ad2f03f3ce

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
<<<<<<< HEAD
        
        if (isPublicEndpoint(request.getRequestURI())) {
=======
        // Ignorar validação para endpoints de autenticação
        if (request.getRequestURI().contains("/token") ||
                request.getRequestURI().contains("/usuarios/criar") ||
                request.getRequestURI().contains("/usuarios/atualizar-senha")) {
>>>>>>> 3c46f92a3eab74bba1b2fc31a3bd29ad2f03f3ce
            return true;
        }

        String token = extractToken(request);
<<<<<<< HEAD
        validateTokenPresence(token);
        
        try {
            validateTokenAndUser(token);
=======
        if (token == null) {
            throw new KeycloakException("Token não fornecido", HttpStatus.UNAUTHORIZED.value(), "TOKEN_MISSING");
        }

        try {
            // Verificar expiração do token
            TokenValidationResult validationResult = validateToken(token);
            if (!validationResult.isValid()) {
                throw new KeycloakException(validationResult.getErrorMessage(),
                        HttpStatus.UNAUTHORIZED.value(),
                        validationResult.getErrorCode());
            }

            // Verificar status do usuário
            String username = getUsernameFromToken(token);
            UsersResource usersResource = keycloak.realm(realm).users();
            List<UserRepresentation> users = usersResource.search(username);

            if (users.isEmpty()) {
                throw new KeycloakException("Usuário não encontrado no Keycloak",
                        HttpStatus.UNAUTHORIZED.value(),
                        "USER_NOT_FOUND");
            }

            UserRepresentation user = users.get(0);
            if (!user.isEnabled()) {
                throw new KeycloakException("Conta desativada. Entre em contato com o administrador do sistema.",
                        HttpStatus.FORBIDDEN.value(), "ACCOUNT_DISABLED");
            }

>>>>>>> 3c46f92a3eab74bba1b2fc31a3bd29ad2f03f3ce
            return true;
        } catch (KeycloakException e) {
            logger.error("Erro de validação do Keycloak: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao validar token: {}", e.getMessage(), e);
<<<<<<< HEAD
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
=======
            String errorMessage = String.format("Erro ao validar token: %s", e.getMessage());
            throw new KeycloakException(errorMessage, HttpStatus.UNAUTHORIZED.value(), "TOKEN_VALIDATION_ERROR");
        }
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
>>>>>>> 3c46f92a3eab74bba1b2fc31a3bd29ad2f03f3ce
    }

    private TokenValidationResult validateToken(String token) {
        try {
<<<<<<< HEAD
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

=======
            // Decodificar o token JWT
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return new TokenValidationResult(false, "Token inválido: formato incorreto", "INVALID_TOKEN");
            }

            // Decodificar o payload (parte do meio)
            String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> claims = mapper.readValue(payload, Map.class);

            // Verificar a expiração
            Object expObj = claims.get("exp");
            if (expObj == null) {
                return new TokenValidationResult(false, "Token inválido: expiração não encontrada", "INVALID_TOKEN");
            }

            long exp;
            if (expObj instanceof Integer) {
                exp = ((Integer) expObj).longValue();
            } else if (expObj instanceof Long) {
                exp = (Long) expObj;
            } else {
                return new TokenValidationResult(false, "Token inválido: formato de expiração incorreto",
                        "INVALID_TOKEN");
            }

            // Verificar se o token expirou
            if (System.currentTimeMillis() / 1000 >= exp) {
                return new TokenValidationResult(false, "Token expirado", "TOKEN_EXPIRED");
            }

            // Verificar o expires_in
            Object expiresInObj = claims.get("expires_in");
            int expiresIn;
            if (expiresInObj instanceof Integer) {
                expiresIn = (Integer) expiresInObj;
            } else if (expiresInObj instanceof Long) {
                expiresIn = ((Long) expiresInObj).intValue();
            } else {
                expiresIn = defaultExpiresIn;
            }

            // Calcular o tempo restante do token
            long currentTime = System.currentTimeMillis() / 1000;
            long tokenCreationTime = exp - expiresIn;
            long timeElapsed = currentTime - tokenCreationTime;

            // Se o tempo decorrido for maior que o expires_in, o token está expirado
            if (timeElapsed >= expiresIn) {
                return new TokenValidationResult(false, "Token expirado por tempo de sessão", "TOKEN_SESSION_EXPIRED");
            }

            return new TokenValidationResult(true, null, null);
        } catch (Exception e) {
            logger.error("Erro ao validar token: {}", e.getMessage(), e);
            return new TokenValidationResult(false,
                    String.format("Erro ao validar token: %s", e.getMessage()),
                    "TOKEN_VALIDATION_ERROR");
        }
    }

    private String getUsernameFromToken(String token) {
        try {
            // Decodificar o token JWT
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new KeycloakException("Token inválido: formato incorreto",
                        HttpStatus.UNAUTHORIZED.value(), "INVALID_TOKEN");
            }

            // Decodificar o payload (parte do meio)
            String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> claims = mapper.readValue(payload, Map.class);

            // Extrair o username
            String username = (String) claims.get("preferred_username");
            if (username == null) {
                throw new KeycloakException("Token inválido: username não encontrado",
                        HttpStatus.UNAUTHORIZED.value(), "INVALID_TOKEN");
            }

            return username;
        } catch (Exception e) {
            logger.error("Erro ao extrair username do token: {}", e.getMessage(), e);
            throw new KeycloakException(
                    String.format("Erro ao extrair username do token: %s", e.getMessage()),
                    HttpStatus.UNAUTHORIZED.value(),
                    "TOKEN_VALIDATION_ERROR");
        }
    }

>>>>>>> 3c46f92a3eab74bba1b2fc31a3bd29ad2f03f3ce
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
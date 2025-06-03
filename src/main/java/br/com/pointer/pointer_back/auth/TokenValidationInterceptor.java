package br.com.pointer.pointer_back.auth;

import java.util.List;
import java.util.Map;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pointer.pointer_back.exception.KeycloakException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TokenValidationInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(TokenValidationInterceptor.class);

    @Autowired
    private Keycloak keycloak;

    @Value("${keycloak.realm:pointer}")
    private String realm;

    @Value("${keycloak.token.expires-in:36000}")
    private int defaultExpiresIn;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // Ignorar validação para endpoints de autenticação
        if (request.getRequestURI().contains("/token") ||
                request.getRequestURI().contains("/usuarios/criar") ||
                request.getRequestURI().contains("/usuarios/atualizar-senha")) {
            return true;
        }

        String token = extractToken(request);
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

            return true;
        } catch (KeycloakException e) {
            logger.error("Erro de validação do Keycloak: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao validar token: {}", e.getMessage(), e);
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
    }

    private TokenValidationResult validateToken(String token) {
        try {
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
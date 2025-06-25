package br.com.pointer.pointer_back.constant;

import java.util.Arrays;
import java.util.List;

public final class SecurityConstants {
    
    // URLs públicas que não precisam de autenticação
    public static final List<String> PUBLIC_URLS = Arrays.asList(
        "/token/**",
        "/api/usuarios/verificar-codigo",
        "/api/usuarios/redefinir-senha", 
        "/api/usuarios/esqueceu-senha",
        "/api/usuarios/primeiro-acesso",
        "/api/usuarios/primeiro-acesso/reenviar",
        "/api/2fa/verify"
    );
    
    // URLs excluídas do interceptor
    public static final List<String> EXCLUDED_PATHS = Arrays.asList(
        "/token",
        "/api/usuarios/atualizar-senha",
        "/api/usuarios/primeiro-acesso",
        "/api/usuarios/primeiro-acesso/reenviar",
        "/api/usuarios/redefinir-senha",
        "/api/usuarios/esqueceu-senha",
        "/api/usuarios/verificar-codigo",
        "/api/2fa/verify",
        "/error"
    );
    
    // Configurações CORS
    public static final List<String> ALLOWED_ORIGINS = Arrays.asList(
        "http://localhost:3000",
        "http://localhost:8080", 
        "http://127.0.0.1:3000",
        "http://127.0.0.1:8080"
    );
    
    public static final List<String> ALLOWED_METHODS = Arrays.asList(
        "GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT", "PATCH"
    );
    
    public static final List<String> ALLOWED_HEADERS = Arrays.asList(
        "Authorization", "Content-Type", "Accept"
    );
    
    public static final long CORS_MAX_AGE = 3600L;
    
    // Headers
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    
    // Claims JWT
    public static final String EXP_CLAIM = "exp";
    public static final String EXPIRES_IN_CLAIM = "expires_in";
    public static final String PREFERRED_USERNAME_CLAIM = "preferred_username";
    
    // Códigos de erro
    public static final String TOKEN_MISSING = "TOKEN_MISSING";
    public static final String INVALID_TOKEN = "INVALID_TOKEN";
    public static final String TOKEN_EXPIRED = "TOKEN_EXPIRED";
    public static final String TOKEN_SESSION_EXPIRED = "TOKEN_SESSION_EXPIRED";
    public static final String TOKEN_VALIDATION_ERROR = "TOKEN_VALIDATION_ERROR";
    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String ACCOUNT_DISABLED = "ACCOUNT_DISABLED";
    
    // Mensagens de erro
    public static final String TOKEN_NOT_PROVIDED = "Token não fornecido";
    public static final String INVALID_TOKEN_FORMAT = "Token inválido: formato incorreto";
    public static final String EXPIRATION_NOT_FOUND = "Token inválido: expiração não encontrada";
    public static final String INVALID_EXPIRATION_FORMAT = "Token inválido: formato de expiração incorreto";
    public static final String TOKEN_EXPIRED_MSG = "Token expirado";
    public static final String TOKEN_SESSION_EXPIRED_MSG = "Token expirado por tempo de sessão";
    public static final String USERNAME_NOT_FOUND = "Token inválido: username não encontrado";
    public static final String USER_NOT_FOUND_MSG = "Usuário não encontrado no Keycloak";
    public static final String ACCOUNT_DISABLED_MSG = "Conta desativada. Entre em contato com o administrador do sistema.";
    
    private SecurityConstants() {
    }
} 
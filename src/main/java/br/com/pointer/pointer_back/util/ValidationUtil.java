package br.com.pointer.pointer_back.util;

import br.com.pointer.pointer_back.exception.KeycloakException;
import br.com.pointer.pointer_back.exception.EmailInvalidoException;
import br.com.pointer.pointer_back.exception.SenhaInvalidaException;

import java.util.Set;

public class ValidationUtil {
    
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final int SENHA_MINIMA = 8;
    
    private ValidationUtil() {
        // Construtor privado para evitar instanciação
    }
    
    public static void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new KeycloakException("Nome não pode ser vazio", 400, "INVALID_NAME");
        }

        String nomeLimpo = nome.trim();
        
        // Verificar se tem pelo menos 2 palavras (nome e sobrenome)
        String[] partes = nomeLimpo.split("\\s+");
        if (partes.length < 2) {
            throw new KeycloakException("Nome deve ser completo (nome e sobrenome)", 400, "INVALID_NAME");
        }

        // Verificar se cada parte tem pelo menos 2 caracteres
        for (String parte : partes) {
            if (parte.length() < 2) {
                throw new KeycloakException("Cada parte do nome deve ter pelo menos 2 caracteres", 400, "INVALID_NAME");
            }
        }

        // Verificar se o nome completo tem pelo menos 5 caracteres
        if (nomeLimpo.length() < 5) {
            throw new KeycloakException("Nome completo deve ter pelo menos 5 caracteres", 400, "INVALID_NAME");
        }

        // Verificar se não tem caracteres especiais (apenas letras, espaços e acentos)
        if (!nomeLimpo.matches("^[\\p{L}\\s]+$")) {
            throw new KeycloakException("Nome deve conter apenas letras e espaços", 400, "INVALID_NAME");
        }
    }
    
    public static void validarEmail(String email) {
        if (email == null || !email.matches(EMAIL_REGEX)) {
            throw new EmailInvalidoException("Email inválido");
        }
    }
    
    public static void validarSenha(String senha) {
        if (senha == null || senha.length() < SENHA_MINIMA) {
            throw new SenhaInvalidaException("Senha deve ter pelo menos " + SENHA_MINIMA + " caracteres");
        }
    }
    
    public static void validarUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new KeycloakException("ID do usuário não pode ser vazio", 400, "INVALID_USER_ID");
        }
    }
    
    public static void validarRoles(Set<String> roles) {
        if (roles == null || roles.isEmpty()) {
            throw new KeycloakException("Ao menos uma role deve ser especificada", 400, "INVALID_ROLES");
        }
    }
} 
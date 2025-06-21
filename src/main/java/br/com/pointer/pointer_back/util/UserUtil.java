package br.com.pointer.pointer_back.util;

import org.keycloak.representations.idm.UserRepresentation;

public class UserUtil {
    
    private UserUtil() {
    }
    
    public static String[] extrairNomeESobrenome(String nomeCompleto) {
        String[] partes = nomeCompleto.trim().split(" ", 2);
        String nome = partes[0];
        String sobrenome = partes.length > 1 ? partes[1] : "";
        return new String[] { nome, sobrenome };
    }
    
    public static UserRepresentation construirUserRepresentation(String nome, String sobrenome, String email) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(email);
        user.setEmail(email);
        user.setFirstName(nome);
        user.setLastName(sobrenome);
        user.setEnabled(true);
        user.setEmailVerified(false);
        return user;
    }
} 
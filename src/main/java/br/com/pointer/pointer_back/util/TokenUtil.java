package br.com.pointer.pointer_back.util;

import java.util.Random;

public class TokenUtil {
    
    private static final String CARACTERES_VALIDOS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int TAMANHO_TOKEN = 32;
    
    private TokenUtil() {
    }
    
    public static String gerarTokenAleatorio() {
        StringBuilder token = new StringBuilder();
        Random random = new Random();
        
        for (int i = 0; i < TAMANHO_TOKEN; i++) {
            int index = random.nextInt(CARACTERES_VALIDOS.length());
            token.append(CARACTERES_VALIDOS.charAt(index));
        }
        
        return token.toString();
    }
} 
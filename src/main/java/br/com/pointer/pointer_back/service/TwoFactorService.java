package br.com.pointer.pointer_back.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class TwoFactorService {

    private final GoogleAuthenticator gAuth;
    private final SecureRandom secureRandom;

    public TwoFactorService() {
        this.gAuth = new GoogleAuthenticator();
        this.secureRandom = new SecureRandom();
    }

    // Construtor para testes
    public TwoFactorService(GoogleAuthenticator gAuth) {
        this.gAuth = gAuth;
        this.secureRandom = new SecureRandom();
    }

    /**
     * Gera uma nova chave secreta para 2FA
     */
    public String generateSecretKey() {
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key.getKey();
    }

    /**
     * Gera o QR Code URL para o Google Authenticator
     */
    public String generateQRCodeUrl(String secretKey, String email, String issuer) {
        return GoogleAuthenticatorQRGenerator.getOtpAuthURL(issuer, email, 
                new GoogleAuthenticatorKey.Builder(secretKey).build());
    }

    /**
     * Valida o código TOTP fornecido pelo usuário
     */
    public boolean validateCode(String secretKey, int code) {
        return gAuth.authorize(secretKey, code);
    }
} 
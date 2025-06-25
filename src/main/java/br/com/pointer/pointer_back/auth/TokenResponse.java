package br.com.pointer.pointer_back.auth;

import lombok.Data;

@Data
public class TokenResponse {
    private String access_token;
    private int expires_in;
    private int refresh_expires_in;
    private String refresh_token;
    private String token_type;
    private String id_token;
    private int not_before_policy;
    private String session_state;
    private String scope;
    
    // Informações de 2FA
    private Boolean two_factor_enabled;
    private Boolean two_factor_configured;
} 
package br.com.pointer.pointer_back.dto;

import lombok.Data;

@Data
public class TwoFactorStatusDTO {
    private Boolean twoFactorEnabled;
    private Boolean twoFactorConfigured; // se tem secretKey mas não está ativo
} 
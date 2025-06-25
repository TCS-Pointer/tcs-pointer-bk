package br.com.pointer.pointer_back.dto;

import lombok.Data;

@Data
public class TwoFactorSetupDTO {
    private String qrCodeUrl;
    private String secretKey;
} 
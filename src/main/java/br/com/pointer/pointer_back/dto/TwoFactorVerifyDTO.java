package br.com.pointer.pointer_back.dto;

import lombok.Data;

@Data
public class TwoFactorVerifyDTO {
    private String email;
    private Integer code;
} 
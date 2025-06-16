package br.com.pointer.pointer_back.dto;

import lombok.Data;

@Data
public class UsuarioSimpleDTO {
    
    private Long id;
    private String nome;
    private String email;
    private String setor;
    private String cargo;
} 
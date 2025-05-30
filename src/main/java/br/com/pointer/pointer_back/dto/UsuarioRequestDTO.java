package br.com.pointer.pointer_back.dto;

import lombok.Data;

@Data
public class UsuarioRequestDTO {
    private String nome;
    private String setor;
    private String cargo;
    private String tipoUsuario;
    private String email;
    private String senha;
} 
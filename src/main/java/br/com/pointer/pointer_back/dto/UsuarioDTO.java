package br.com.pointer.pointer_back.dto;

import br.com.pointer.pointer_back.model.StatusUsuario;
import lombok.Data;

@Data
public class UsuarioDTO {
    private String nome;
    private String email;
    private String senha;
    private String setor;
    private String cargo;
    private String tipoUsuario;
    private String keycloakId;
    private StatusUsuario status;
}
package br.com.pointer.pointer_back.dto;

import lombok.Data;
import java.time.LocalDateTime;

import br.com.pointer.pointer_back.model.StatusUsuario;

@Data
public class UsuarioResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String setor;
    private String cargo;
    private String tipoUsuario;
    private LocalDateTime dataCriacao;
    private StatusUsuario Status;
    private String keycloakId;
}

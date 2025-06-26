package br.com.pointer.pointer_back.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.pointer.pointer_back.model.StatusUsuario;
import lombok.Data;

@Data
public class UsuarioResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String setor;
    private String cargo;
    private String tipoUsuario;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataCriacao;
    private StatusUsuario status;
    private String keycloakId;
    private Boolean twoFactorEnabled;
}

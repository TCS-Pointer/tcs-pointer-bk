package br.com.pointer.pointer_back.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UsuarioResponseDTO {
    private Long id;
    private String nome;
    private String setor;
    private String cargo;
    private String tipoUsuario;
    private LocalDateTime dataCriacao;
    private String keycloakId;
}

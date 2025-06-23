package br.com.pointer.pointer_back.dto;

<<<<<<< HEAD
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.pointer.pointer_back.model.StatusUsuario;
=======
>>>>>>> 3c46f92a3eab74bba1b2fc31a3bd29ad2f03f3ce
import lombok.Data;

import br.com.pointer.pointer_back.model.StatusUsuario;

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
    private StatusUsuario Status;
    private String keycloakId;
}

package br.com.pointer.pointer_back.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UsuarioComunicadoDTO {
    private Long id;
    private LocalDateTime dtLeitura;
    private Long usuarioId;
}

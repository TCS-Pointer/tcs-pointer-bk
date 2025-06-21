package br.com.pointer.pointer_back.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.pointer.pointer_back.enums.StatusPDI;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class pdiDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private Long idDestinatario;
    private UsuarioResponseDTO destinatario;
    private StatusPDI status;
    private LocalDate dtInicio;
    private LocalDate dtFim;
    private Long idUsuario;
    private UsuarioResponseDTO usuario;
    private List<MarcoPDIDTO> marcos;
}
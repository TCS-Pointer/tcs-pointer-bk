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
<<<<<<< HEAD
    private Long idDestinatario;
    private UsuarioResponseDTO destinatario;
=======
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Long idUsuario;
    private Long idDestinatario;
>>>>>>> 3c46f92a3eab74bba1b2fc31a3bd29ad2f03f3ce
    private StatusPDI status;
    private LocalDate dtInicio;
    private LocalDate dtFim;
    private Long idUsuario;
    private UsuarioResponseDTO usuario;
    private List<MarcoPDIDTO> marcos;
}
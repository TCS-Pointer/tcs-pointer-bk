package br.com.pointer.pointer_back.dto;

import br.com.pointer.pointer_back.enums.StatusPDI;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class pdiDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Long idUsuario;
    private Long destinatario;
    private StatusPDI status;
    private List<MarcoPDIDTO> marcos;
}
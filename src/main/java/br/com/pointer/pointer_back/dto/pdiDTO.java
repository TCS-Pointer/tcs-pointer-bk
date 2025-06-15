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
    private Long idDestinatario;
    private UsuarioResponseDTO destinatario;
    private StatusPDI status;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Long idUsuario;
    private UsuarioResponseDTO usuario;
    private List<MarcoPDIDTO> marcos;
}
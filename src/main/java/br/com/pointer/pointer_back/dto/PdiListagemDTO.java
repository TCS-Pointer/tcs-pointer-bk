package br.com.pointer.pointer_back.dto;

import java.time.LocalDate;
import br.com.pointer.pointer_back.enums.StatusPDI;
import lombok.Data;

@Data
public class PdiListagemDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private StatusPDI status;
    private LocalDate dtInicio;
    private LocalDate dtFim;
    private Long idDestinatario;
    private String nomeDestinatario;
    private String cargoDestinatario;
    private String setorDestinatario;
    private String emailDestinatario;
    private Long totalMarcos;
    private Long marcosConcluidos;

    public PdiListagemDTO(Long id, String titulo, String descricao, StatusPDI status, LocalDate dtInicio,
            LocalDate dtFim,
            Long idDestinatario, String nomeDestinatario, String cargoDestinatario, String setorDestinatario,
            String emailDestinatario,
            Long totalMarcos, Long marcosConcluidos) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.dtInicio = dtInicio;
        this.dtFim = dtFim;
        this.idDestinatario = idDestinatario;
        this.nomeDestinatario = nomeDestinatario;
        this.cargoDestinatario = cargoDestinatario;
        this.setorDestinatario = setorDestinatario;
        this.emailDestinatario = emailDestinatario;
        this.totalMarcos = totalMarcos;
        this.marcosConcluidos = marcosConcluidos;
    }
}
package br.com.pointer.pointer_back.dto;

import br.com.pointer.pointer_back.enums.TipoFeedback;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FeedbackResponseDTO {
    
    private Long idFeedback;
    private String acoesRecomendadas;
    private Boolean anonimo;
    private String assunto;
    private Integer avComunicacao;
    private Integer avProdutividade;
    private Integer resolucaoDeProblemas;
    private Integer trabalhoEmEquipe;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dtEnvio;
    private String pontosFortes;
    private String pontosMelhoria;
    private TipoFeedback tipoFeedback;
    private DestinatarioDTO destinatarioDTO;
    private RemetenteDTO remetenteDTO;
} 
package br.com.pointer.pointer_back.dto;

import br.com.pointer.pointer_back.enums.TipoFeedback;
import lombok.Data;

@Data
public class FeedbackDTO {
    
    private String acoesRecomendadas;
    private Boolean anonimo = false;
    private String assunto;
    private Integer avComunicacao;
    private Integer avProdutividade;
    private Integer resolucaoDeProblemas;
    private Integer trabalhoEmEquipe;
    private String pontosFortes;
    private String pontosMelhoria;
    private TipoFeedback tipoFeedback;
    private Long idUsuarioDestinatario;
    private Long idUsuarioRemetente;
} 
package br.com.pointer.pointer_back.dto;

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
    private String destinatario;
    private String pontosFortes;
    private String pontosMelhoria;
    private String tipoFeedback;
    private Long idUsuarioDestinatario;
    private Long idUsuarioRemetente;
} 
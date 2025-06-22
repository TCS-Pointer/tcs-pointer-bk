package br.com.pointer.pointer_back.dto;

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
    private LocalDateTime dtEnvio;
    private String destinatario;
    private String pontosFortes;
    private String pontosMelhoria;
    private String tipoFeedback;
    private Long idUsuarioDestinatario;
    private Long idUsuarioRemetente;
} 
package br.com.pointer.pointer_back.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "feedback")
public class Feedback {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_feedback")
    private Long idFeedback;

    @Column(name = "acoes_recomendadas", length = 200)
    private String acoesRecomendadas;

    @Column(name = "anonimo", nullable = false)
    private Boolean anonimo = false;

    @Column(name = "assunto", length = 50, nullable = false)
    private String assunto;

    @Column(name = "av_comunicacao", nullable = false)
    private Integer avComunicacao;

    @Column(name = "av_produtividade", nullable = false)
    private Integer avProdutividade;

    @Column(name = "resolucao_de_problemas", nullable = false) 
    private Integer resolucaoDeProblemas;

    @Column(name = "trabalho_em_equipe", nullable = false)
    private Integer trabalhoEmEquipe;

    @Column(name = "dt_envio", nullable = false)
    private LocalDateTime dtEnvio;

    @Column(name = "destinatario", length = 45, nullable = false)
    private String destinatario;

    @Column(name = "pontos_fortes", length = 45)
    private String pontosFortes;

    @Column(name = "pontos_melhoria", length = 45)
    private String pontosMelhoria;

    @Column(name = "tipo_feedback", length = 45, nullable = false)
    private String tipoFeedback;

    @Column(name = "id_usuario_destinatario", nullable = false)
    private Long idUsuarioDestinatario;

    @Column(name = "id_usuario_remetente", nullable = false)
    private Long idUsuarioRemetente;

    @PrePersist
    protected void onCreate() {
        dtEnvio = LocalDateTime.now();
    }
} 
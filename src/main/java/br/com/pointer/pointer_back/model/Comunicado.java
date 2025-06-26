package br.com.pointer.pointer_back.model;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "comunicado")
public class Comunicado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, length = 400)
    private String descricao;

    @ElementCollection
    @CollectionTable(name = "comunicado_setores", joinColumns = @JoinColumn(name = "comunicado_id"))
    @Column(name = "setor")
    private Set<String> setores;

    @Column(name = "apenas_gestores", nullable = false)
    private boolean apenasGestores;

    @Column(name = "dt_publicacao", nullable = false)
    private LocalDateTime dataPublicacao;

    @Column(name = "ativo", nullable = false)
    private boolean ativo;

    @PrePersist
    protected void onCreate() {
        dataPublicacao = LocalDateTime.now();
    }
}
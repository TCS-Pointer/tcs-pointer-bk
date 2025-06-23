package br.com.pointer.pointer_back.model;

import br.com.pointer.pointer_back.enums.StatusPDI;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "pdi")
public class PDI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

<<<<<<< HEAD
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_destinatario", nullable = false)
    private Usuario destinatario;
=======
    @Column(name = "idDestinatario")
    private Long idDestinatario;
>>>>>>> 3c46f92a3eab74bba1b2fc31a3bd29ad2f03f3ce

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20)")
    private StatusPDI status;

    @Column(name = "dt_inicio", nullable = false)
    private LocalDate dtInicio;

    @Column(name = "dt_fim", nullable = false)
    private LocalDate dtFim;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", insertable = false, updatable = false)
    private Usuario usuario;

    @Column(name = "dt_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "pdi", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MarcoPDI> marcos;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        if (status == null) {
            status = StatusPDI.EM_ANDAMENTO;
        }
    }
}
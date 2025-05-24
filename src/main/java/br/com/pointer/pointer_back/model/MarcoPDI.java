package br.com.pointer.pointer_back.model;

import br.com.pointer.pointer_back.enums.StatusMarcoPDI;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "marco_pdi")
public class MarcoPDI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20)")
    private StatusMarcoPDI status;

    @Column(name = "dt_final", nullable = false)
    private LocalDate dtFinal;

    @ManyToOne
    @JoinColumn(name = "pdi_id", nullable = false)
    private PDI pdi;

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = StatusMarcoPDI.AGUARDANDO;
        }
    }
}
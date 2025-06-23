package br.com.pointer.pointer_back.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comunicado_leitura")
public class ComunicadoLeitura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comunicado_id", nullable = false)
    private Comunicado comunicado;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "data_leitura", nullable = false)
    private LocalDateTime dataLeitura;
} 
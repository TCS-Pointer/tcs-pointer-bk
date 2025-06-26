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

    @Column(name = "comunicado_id", nullable = false)
    private Long comunicadoId;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "dt_leitura", nullable = false)
    private LocalDateTime dtLeitura;
}
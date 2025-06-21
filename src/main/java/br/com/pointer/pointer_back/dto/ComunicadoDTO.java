package br.com.pointer.pointer_back.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComunicadoDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private Set<String> setores;
    private boolean apenasGestores;
    
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataPublicacao;
}


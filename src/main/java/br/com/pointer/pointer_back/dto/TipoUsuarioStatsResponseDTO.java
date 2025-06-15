package br.com.pointer.pointer_back.dto;

import lombok.Data;
import java.util.List;

@Data
public class TipoUsuarioStatsResponseDTO {
    private List<TipoUsuarioStatsDTO> stats;
    private Long sum;

    public TipoUsuarioStatsResponseDTO(List<TipoUsuarioStatsDTO> stats, Long sum) {
        this.stats = stats;
        this.sum = sum;
    }
} 
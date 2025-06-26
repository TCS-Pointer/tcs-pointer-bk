package br.com.pointer.pointer_back.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class TipoUsuarioStatsResponseDTO {
    private List<TipoUsuarioStatsDTO> stats;
    private Long sum;

    public TipoUsuarioStatsResponseDTO(List<TipoUsuarioStatsDTO> stats, Long sum) {
        this.stats = stats;
        this.sum = sum;
    }
} 
package br.com.pointer.pointer_back.dto;

import java.util.List;

import lombok.Data;

@Data
public class FeedbackStatsResponseDTO {
    private List<FeedbackStatsDTO> stats;
    private Long totalGeral;
}

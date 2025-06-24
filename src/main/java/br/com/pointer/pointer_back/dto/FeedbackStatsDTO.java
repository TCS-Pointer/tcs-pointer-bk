package br.com.pointer.pointer_back.dto;

import br.com.pointer.pointer_back.enums.TipoFeedback;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackStatsDTO {
    private TipoFeedback tipoFeedback;
    private Long total;
}

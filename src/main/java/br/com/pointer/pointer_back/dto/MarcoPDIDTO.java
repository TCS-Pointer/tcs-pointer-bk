package br.com.pointer.pointer_back.dto;

import br.com.pointer.pointer_back.enums.StatusMarcoPDI;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class MarcoPDIDTO {
    private Long id;

    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    private StatusMarcoPDI status;

    @NotNull(message = "A data final é obrigatória")
    private LocalDate dtFinal;

    @NotNull(message = "O ID do PDI é obrigatório")
    private Long pdiId;
}
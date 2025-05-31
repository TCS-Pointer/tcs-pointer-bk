package br.com.pointer.pointer_back.dto;

import br.com.pointer.pointer_back.enums.StatusMarcoPDI;
import lombok.Data;

@Data
public class AtualizarStatusPDIDTO {
    private Long idMarco;
    private StatusMarcoPDI statusMarco;
}
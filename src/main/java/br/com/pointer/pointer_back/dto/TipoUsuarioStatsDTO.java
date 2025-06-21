package br.com.pointer.pointer_back.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoUsuarioStatsDTO {
    private String tipoUsuario;
    private Long total;
}

package br.com.pointer.pointer_back.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private String setor;
    private String cargo;
    private LocalDateTime dataPublicacao;

    public String getDataPublicacaoFormatada() {
        if (dataPublicacao == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dataPublicacao.format(formatter);
    }
}


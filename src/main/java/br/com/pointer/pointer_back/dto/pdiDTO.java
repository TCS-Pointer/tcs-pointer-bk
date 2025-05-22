package br.com.pointer.pointer_back.dto;

import br.com.pointer.pointer_back.enums.StatusPDI;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class pdiDTO {

    @NotBlank(message = "Título é obrigatório")
    @Size(min = 2, max = 100, message = "Título deve ter entre 2 e 100 caracteres")
    private String titulo;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(min = 2, max = 500, message = "Descrição deve ter entre 2 e 500 caracteres")
    private String descricao;

    @NotNull(message = "Data de Início é obrigatória")
    private LocalDate dataInicio;

    @NotNull(message = "Data Fim é obrigatória")
    private LocalDate dataFim;

    @NotNull(message = "ID do Usuário é obrigatório")
    private Long idUsuario;

    private StatusPDI status;
} 
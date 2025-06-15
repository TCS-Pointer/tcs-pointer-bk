package br.com.pointer.pointer_back.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlterarStatusDTO {
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String emailChangeStatus;

    @NotBlank(message = "Username é obrigatório")
    @Email(message = "Username inválido")
    private String emailSend;
}

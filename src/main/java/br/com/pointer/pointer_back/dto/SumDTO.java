package br.com.pointer.pointer_back.dto;

import lombok.Data;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SumDTO {
    private BigDecimal sum;
}

package br.com.pointer.pointer_back.dto;

import java.util.List;
import lombok.Data;

@Data
public class SetorCargoDTO {
    private List<Setor> setores;

    @Data
    public static class Setor {
        private String setor;
        private List<String> cargos;
    }
}
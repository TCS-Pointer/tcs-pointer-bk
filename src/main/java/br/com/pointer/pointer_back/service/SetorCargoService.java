package br.com.pointer.pointer_back.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.SetorCargoDTO;
import br.com.pointer.pointer_back.exception.SetorCargoInvalidoException;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SetorCargoService {

    private static final Logger logger = LoggerFactory.getLogger(SetorCargoService.class);

    @Getter
    private SetorCargoDTO dados;

    @PostConstruct
    public void init() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = new ClassPathResource("dados.json").getInputStream();
            dados = mapper.readValue(is, SetorCargoDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar dados.json", e);
        }
    }

    public ApiResponse<SetorCargoDTO> listarSetoresECargos() {
        try {
            return ApiResponse.success(dados, "Setores e cargos listados com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao listar setores e cargos: ", e);
            return ApiResponse.badRequest("Erro ao listar setores e cargos: " + e.getMessage());
        }
    }

    public void validarSetorECargo(String setor, String cargo) {
        if (!isSetorValido(setor)) {
            throw new SetorCargoInvalidoException("Setor inválido: " + setor);
        }
        if (!isCargoValido(setor, cargo)) {
            throw new SetorCargoInvalidoException("Cargo inválido: " + cargo + " para o setor: " + setor);
        }
    }

    public boolean isSetorValido(String setor) {
        if (dados == null || dados.getSetores() == null || setor == null) {
            return false;
        }
        return dados.getSetores().stream()
                .anyMatch(s -> s.getSetor().equals(setor));
    }

    public boolean isCargoValido(String setor, String cargo) {
        if (dados == null || dados.getSetores() == null || setor == null || cargo == null) {
            return false;
        }
        return dados.getSetores().stream()
                .filter(s -> s.getSetor().equals(setor))
                .findFirst()
                .map(s -> s.getCargos().contains(cargo))
                .orElse(false);
    }

    public List<String> getSetores() {
        if (dados == null || dados.getSetores() == null) {
            return List.of();
        }
        return dados.getSetores().stream()
                .map(SetorCargoDTO.Setor::getSetor)
                .collect(Collectors.toList());
    }

    public List<String> getCargosPorSetor(String setor) {
        if (dados == null || dados.getSetores() == null || setor == null) {
            return List.of();
        }
        return dados.getSetores().stream()
                .filter(s -> s.getSetor().equals(setor))
                .findFirst()
                .map(SetorCargoDTO.Setor::getCargos)
                .orElse(List.of());
    }
}
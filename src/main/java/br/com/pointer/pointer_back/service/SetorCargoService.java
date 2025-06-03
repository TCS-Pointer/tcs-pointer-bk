package br.com.pointer.pointer_back.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pointer.pointer_back.dto.SetorCargoDTO;
import br.com.pointer.pointer_back.exception.SetorCargoInvalidoException;
import jakarta.annotation.PostConstruct;
import lombok.Getter;

@Service
public class SetorCargoService {

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

    public void validarSetorECargo(String setor, String cargo) {
        if (!isSetorValido(setor)) {
            throw new SetorCargoInvalidoException("Setor inválido: " + setor);
        }
        if (!isCargoValido(setor, cargo)) {
            throw new SetorCargoInvalidoException("Cargo inválido: " + cargo + " para o setor: " + setor);
        }
    }

    public boolean isSetorValido(String setor) {
        return dados.getSetores().stream()
                .anyMatch(s -> s.getSetor().equals(setor));
    }

    public boolean isCargoValido(String setor, String cargo) {
        return dados.getSetores().stream()
                .filter(s -> s.getSetor().equals(setor))
                .findFirst()
                .map(s -> s.getCargos().contains(cargo))
                .orElse(false);
    }

    public List<String> getSetores() {
        return dados.getSetores().stream()
                .map(SetorCargoDTO.Setor::getSetor)
                .collect(Collectors.toList());
    }

    public List<String> getCargosPorSetor(String setor) {
        return dados.getSetores().stream()
                .filter(s -> s.getSetor().equals(setor))
                .findFirst()
                .map(SetorCargoDTO.Setor::getCargos)
                .orElse(List.of());
    }
}
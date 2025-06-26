package br.com.pointer.pointer_back.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.ConfirmarLeituraDTO;
import br.com.pointer.pointer_back.dto.UsuarioComunicadoDTO;
import br.com.pointer.pointer_back.service.ComunicadoLeituraService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/comunicados/")
@RequiredArgsConstructor
public class ComunicadoLeituraController {
    private final ComunicadoLeituraService leituraService;

    @PostMapping("/confirmar-leitura")
    @PreAuthorize("hasRole('colaborador') or hasRole('gestor') or hasRole('admin')")
    public ApiResponse<Void> confirmarLeitura(@RequestBody ConfirmarLeituraDTO confirmarLeituraDTO) {
        return leituraService.confirmarLeitura(confirmarLeituraDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('gestor')")
    public ApiResponse<List<UsuarioComunicadoDTO>> listarLeitores(@PathVariable Long id) {
        return leituraService.listarLeitores(id);
    }
}
package br.com.pointer.pointer_back.controller;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.UsuarioDTO;
import br.com.pointer.pointer_back.model.Usuario;
import br.com.pointer.pointer_back.service.ComunicadoLeituraService;
import br.com.pointer.pointer_back.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comunicados/leitura")
@RequiredArgsConstructor
public class ComunicadoLeituraController {
    private final ComunicadoLeituraService leituraService;
    private final UsuarioService usuarioService;

    @PostMapping("/{id}/confirmar")
    @PreAuthorize("hasRole('colaborador') or hasRole('gestor') or hasRole('admin')")
    public ApiResponse<Void> confirmarLeitura(@PathVariable Long id, @RequestParam String keycloakId) {
        leituraService.confirmarLeitura(id, keycloakId);
        return new ApiResponse<Void>().ok(null, "Leitura confirmada com sucesso");
    }

    @GetMapping("/{id}/leitores")
    @PreAuthorize("hasRole('admin') or hasRole('gestor')")
    public ApiResponse<List<UsuarioDTO>> listarLeitores(@PathVariable Long id) {
        List<Usuario> usuarios = leituraService.listarLeitores(id);
        List<UsuarioDTO> dtos = usuarios.stream().map(usuarioService::toDTO).collect(Collectors.toList());
        return new ApiResponse<List<UsuarioDTO>>().ok(dtos, "Leitores listados com sucesso");
    }
} 
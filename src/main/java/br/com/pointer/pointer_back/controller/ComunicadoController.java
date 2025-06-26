package br.com.pointer.pointer_back.controller;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.ComunicadoDTO;
import br.com.pointer.pointer_back.dto.ComunicadoResponseDTO;
import br.com.pointer.pointer_back.service.ComunicadoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/comunicados")
@RequiredArgsConstructor
public class ComunicadoController {

    private static final String ALL_ROLES = "hasRole('colaborador') or hasRole('admin') or hasRole('gestor')";
    private static final String ROLES_ADMIN = "hasRole('admin')";

    private final ComunicadoService comunicadoService;

    @PostMapping
    @PreAuthorize(ROLES_ADMIN)
    public ApiResponse<ComunicadoDTO> criar(@RequestBody ComunicadoDTO comunicadoDTO) {
        return comunicadoService.criar(comunicadoDTO);
    }

    @GetMapping
    @PreAuthorize(ALL_ROLES)
    public ApiResponse<Page<ComunicadoResponseDTO>> listarTodos(@RequestParam String keycloakId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String titulo) {
        return comunicadoService.listarTodos(keycloakId, page, size, titulo);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(ROLES_ADMIN)
    public ApiResponse<Void> deletar(@PathVariable Long id) {
        return comunicadoService.deletar(id);
    }
}

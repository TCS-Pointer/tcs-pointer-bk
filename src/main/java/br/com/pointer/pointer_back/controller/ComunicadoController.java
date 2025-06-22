package br.com.pointer.pointer_back.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.ComunicadoDTO;
import br.com.pointer.pointer_back.service.ComunicadoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/comunicados")
@RequiredArgsConstructor
public class ComunicadoController {
    
    private static final String ROLES_COLABORADOR_ADMIN_GESTOR = "hasRole('colaborador') or hasRole('admin') or hasRole('gestor')";
    private static final String ROLES_ADMIN = "hasRole('admin')";
    private static final String ROLES_GESTOR_ADMIN = "hasRole('gestor') or hasRole('admin')";
    
    private final ComunicadoService comunicadoService;

    @GetMapping
    @PreAuthorize(ROLES_COLABORADOR_ADMIN_GESTOR)
    public ApiResponse<List<ComunicadoDTO>> listarTodos(@RequestParam String keycloakId) {
        return new ApiResponse<List<ComunicadoDTO>>().ok(
            comunicadoService.listarTodos(keycloakId), 
            "Comunicados listados com sucesso"
        );
    }

    @GetMapping("/setor/{setor}")
    @PreAuthorize(ROLES_COLABORADOR_ADMIN_GESTOR)
    public ApiResponse<List<ComunicadoDTO>> listarPorSetor(@PathVariable String setor) {
        return new ApiResponse<List<ComunicadoDTO>>().ok(
            comunicadoService.listarPorSetor(setor), 
            "Comunicados do setor listados com sucesso"
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize(ROLES_COLABORADOR_ADMIN_GESTOR)
    public ApiResponse<ComunicadoDTO> buscarPorId(@PathVariable Long id, @RequestParam String keycloakId) {
        return new ApiResponse<ComunicadoDTO>().ok(
            comunicadoService.buscarPorId(id, keycloakId), 
            "Comunicado encontrado com sucesso"
        );
    }

    @PostMapping
    @PreAuthorize(ROLES_ADMIN)
    public ApiResponse<ComunicadoDTO> criar(@RequestBody ComunicadoDTO comunicadoDTO) {
        return new ApiResponse<ComunicadoDTO>().ok(
            comunicadoService.criar(comunicadoDTO), 
            "Comunicado criado com sucesso"
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize(ROLES_ADMIN)
    public ApiResponse<ComunicadoDTO> atualizar(@PathVariable Long id, @RequestBody ComunicadoDTO comunicadoDTO) {
        return new ApiResponse<ComunicadoDTO>().ok(
            comunicadoService.atualizar(id, comunicadoDTO), 
            "Comunicado atualizado com sucesso"
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(ROLES_ADMIN)
    public ApiResponse<Void> deletar(@PathVariable Long id) {
        comunicadoService.deletar(id);
        return new ApiResponse<Void>().ok(null, "Comunicado deletado com sucesso");
    }

    @GetMapping("/gestores")
    @PreAuthorize(ROLES_GESTOR_ADMIN)
    public ApiResponse<List<ComunicadoDTO>> listarApenasGestores(@RequestParam String keycloakId) {
        return new ApiResponse<List<ComunicadoDTO>>().ok(
            comunicadoService.listarApenasGestores(keycloakId), 
            "Comunicados de gestores listados com sucesso"
        );
    }
}

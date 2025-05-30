package br.com.pointer.pointer_back.controller;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.UsuarioDTO;
import br.com.pointer.pointer_back.dto.UsuarioResponseDTO;
import br.com.pointer.pointer_back.dto.UsuarioRequestDTO;
import br.com.pointer.pointer_back.service.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/completo")
    @PreAuthorize("hasRole('admin')")
    public ApiResponse<UsuarioService.CriarUsuarioResponse> criarUsuarioCompleto(@RequestBody UsuarioRequestDTO request) {
        return new ApiResponse<UsuarioService.CriarUsuarioResponse>()
                .ok(usuarioService.criarUsuarioCompleto(request).getBody(), "Usuário criado com sucesso");
    }

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public ApiResponse<Page<UsuarioResponseDTO>> listarUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String setor,
            @RequestParam(required = false) String perfil) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<UsuarioResponseDTO> usuarios = usuarioService.listarUsuarios(pageRequest, setor, perfil);
        return new ApiResponse<Page<UsuarioResponseDTO>>()
                .ok(usuarios, "Usuários listados com sucesso");
    }

    @PutMapping("/atualizar-usuario/{id}")
    @PreAuthorize("hasRole('admin')")
    public ApiResponse<UsuarioResponseDTO> atualizarUsuario(
            @PathVariable String id,
            @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioResponseDTO usuarioAtualizado = usuarioService.atualizarUsuario(usuarioDTO, id);
        return new ApiResponse<UsuarioResponseDTO>()
                .ok(usuarioAtualizado, "Usuário atualizado com sucesso");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('colaborador') or hasRole('admin') or hasRole('gestor')")
    public ApiResponse<UsuarioResponseDTO> buscarUsuario(@PathVariable Long id) {
        UsuarioResponseDTO usuario = usuarioService.buscarUsuarioPorId(id);
        return new ApiResponse<UsuarioResponseDTO>()
                .ok(usuario, "Usuário encontrado com sucesso");
    }

    @GetMapping("/keycloak/{keycloakId}")
    @PreAuthorize("hasRole('colaborador') or hasRole('admin') or hasRole('gestor')")
    public ApiResponse<UsuarioResponseDTO> buscarUsuarioPorKeycloakId(@PathVariable String keycloakId) {
        UsuarioResponseDTO usuario = usuarioService.buscarUsuarioPorKeycloakId(keycloakId);
        return new ApiResponse<UsuarioResponseDTO>()
                .ok(usuario, "Usuário encontrado com sucesso");
    }
}
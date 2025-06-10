package br.com.pointer.pointer_back.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.EmailDTO;
import br.com.pointer.pointer_back.dto.UpdatePasswordDTO;
import br.com.pointer.pointer_back.dto.UsuarioDTO;
import br.com.pointer.pointer_back.dto.UsuarioResponseDTO;
import br.com.pointer.pointer_back.service.UsuarioService;
import br.com.pointer.pointer_back.util.ApiResponseUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final ApiResponseUtil apiResponseUtil;

    @PostMapping
    public ApiResponse<UsuarioResponseDTO> criarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.criarUsuario(usuarioDTO);
    }

    @GetMapping
    public ApiResponse<Page<UsuarioResponseDTO>> listarUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String setor,
            @RequestParam(required = false) String perfil,
            @RequestParam(required = false) String status) {
        return usuarioService.listarUsuarios(
                PageRequest.of(page, size), setor, perfil, status);
    }

    @PostMapping("/alterar-status")
    public ApiResponse<Void> alterarStatus(@RequestBody EmailDTO emailDTO) {
        return usuarioService.alternarStatusUsuarioPorEmail(emailDTO);
    }

    @PutMapping("/{id}")
    public ApiResponse<UsuarioResponseDTO> atualizarUsuario(
            @PathVariable String id,
            @RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.atualizarUsuarioComSincronizacaoKeycloak(usuarioDTO, id);
    }

    @PostMapping("/atualizar-senha")
    public ApiResponse<Void> atualizarSenha(@RequestBody UpdatePasswordDTO updatePasswordDTO) {
        return usuarioService.atualizarSenhaUsuario(updatePasswordDTO);
    }

    @PostMapping("/esqueceu-senha")
    public ApiResponse<Void> esqueceuSenha(@RequestBody EmailDTO emailDTO) {
        if (usuarioService.existsByEmail(emailDTO.getEmail())) {
            return usuarioService.resetarSenhaComEmailEKeycloak(emailDTO.getEmail());
        }
        return apiResponseUtil.error("Email não encontrado", 404);
    }

    @PostMapping("/verificar-codigo")
    public ApiResponse<Boolean> verificarCodigo(@RequestBody EmailDTO emailDTO) {
        boolean isValid = usuarioService.existsByEmail(emailDTO.getEmail());
        return apiResponseUtil.map(isValid, Boolean.class,
                isValid ? "Código válido" : "Código inválido");
    }

    @PostMapping("/redefinir-senha")
    public ApiResponse<Void> redefinirSenha(@RequestBody UpdatePasswordDTO updatePasswordDTO) {
        return usuarioService.atualizarSenhaUsuario(updatePasswordDTO);
    }

    @GetMapping("/verificar-email/{email}")
    public ApiResponse<Boolean> verificarEmail(@PathVariable String email) {
        boolean exists = usuarioService.existsByEmail(email);
        return apiResponseUtil.map(!exists, Boolean.class,
                exists ? "Email já cadastrado" : "Email disponível");
    }

    @GetMapping("/{keycloakId}")
    public ApiResponse<UsuarioResponseDTO> buscarUsuario(@PathVariable String keycloakId) {
        return usuarioService.buscarUsuario(keycloakId);
    }
}
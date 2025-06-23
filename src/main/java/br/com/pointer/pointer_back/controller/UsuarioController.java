package br.com.pointer.pointer_back.controller;

<<<<<<< HEAD
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
=======
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
>>>>>>> 3c46f92a3eab74bba1b2fc31a3bd29ad2f03f3ce
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.pointer.pointer_back.ApiResponse;
<<<<<<< HEAD
import br.com.pointer.pointer_back.dto.AlterarStatusDTO;
import br.com.pointer.pointer_back.dto.EmailCode;
import br.com.pointer.pointer_back.dto.EmailDTO;
import br.com.pointer.pointer_back.dto.PrimeiroAcessoDTO;
import br.com.pointer.pointer_back.dto.TipoUsuarioStatsResponseDTO;
import br.com.pointer.pointer_back.dto.UpdatePasswordDTO;
import br.com.pointer.pointer_back.dto.UsuarioDTO;
import br.com.pointer.pointer_back.dto.UsuarioResponseDTO;
import br.com.pointer.pointer_back.dto.UsuarioUpdateDTO;
import br.com.pointer.pointer_back.service.UsuarioService;
=======
import br.com.pointer.pointer_back.dto.EmailDTO;
import br.com.pointer.pointer_back.dto.UpdatePasswordDTO;
import br.com.pointer.pointer_back.dto.UsuarioDTO;
import br.com.pointer.pointer_back.dto.UsuarioResponseDTO;
import br.com.pointer.pointer_back.service.UsuarioService;
import br.com.pointer.pointer_back.util.ApiResponseUtil;
>>>>>>> 3c46f92a3eab74bba1b2fc31a3bd29ad2f03f3ce
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
<<<<<<< HEAD

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public ApiResponse<UsuarioResponseDTO> criarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.criarUsuario(usuarioDTO);
    }

    @PostMapping("/primeiro-acesso")
    public ApiResponse<Void> definirSenhaPrimeiroAcesso(@RequestBody PrimeiroAcessoDTO primeiroAcessoDTO) {
        return usuarioService.definirSenhaPrimeiroAcesso(primeiroAcessoDTO);
    }

    @PostMapping("/primeiro-acesso/reenviar")
    public ApiResponse<Void> reenviarEmailPrimeiroAcesso(@RequestBody EmailDTO emailDTO) {
        return usuarioService.reenviarEmailPrimeiroAcesso(emailDTO.getEmail());
    }

    @GetMapping
    @PreAuthorize("hasRole('admin')")
=======
    private final ApiResponseUtil apiResponseUtil;

    @PostMapping
    public ApiResponse<UsuarioResponseDTO> criarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.criarUsuario(usuarioDTO);
    }

    @GetMapping
>>>>>>> 3c46f92a3eab74bba1b2fc31a3bd29ad2f03f3ce
    public ApiResponse<Page<UsuarioResponseDTO>> listarUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String tipoUsuario,
            @RequestParam(required = false) String setor,
            @RequestParam(required = false) String status) {
        return usuarioService.listarUsuarios(
<<<<<<< HEAD
                PageRequest.of(page, size), tipoUsuario, setor, status);
    }

    @PutMapping("/alterar-status")
    @PreAuthorize("hasRole('admin')")
    public ApiResponse<Void> alterarStatus(@RequestBody AlterarStatusDTO alterarStatusDTO) {
        return usuarioService.alternarStatusUsuarioPorEmail(alterarStatusDTO);
    }

    @PutMapping("/{keycloakId}")
    @PreAuthorize("hasRole('admin')")
    public ApiResponse<UsuarioResponseDTO> atualizarUsuario(
            @PathVariable String keycloakId,
            @RequestBody UsuarioUpdateDTO usuarioUpdateDTO) {
        return usuarioService.atualizarUsuario(usuarioUpdateDTO, keycloakId);
=======
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
>>>>>>> 3c46f92a3eab74bba1b2fc31a3bd29ad2f03f3ce
    }

    @PostMapping("/atualizar-senha")
    public ApiResponse<Void> atualizarSenha(@RequestBody UpdatePasswordDTO updatePasswordDTO) {
        return usuarioService.atualizarSenhaUsuario(updatePasswordDTO);
    }

    @PostMapping("/esqueceu-senha")
    public ApiResponse<Void> esqueceuSenha(@RequestBody EmailDTO emailDTO) {
<<<<<<< HEAD
        return usuarioService.enviarCodigoVerificacao(emailDTO.getEmail());
    }

    @PostMapping("/verificar-codigo")
    public ApiResponse<Void> verificarCodigo(@RequestBody EmailCode emailCode) {
        return usuarioService.verificarCodigo(emailCode.getEmail(), emailCode.getCodigo());
=======
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
>>>>>>> 3c46f92a3eab74bba1b2fc31a3bd29ad2f03f3ce
    }

    @PostMapping("/redefinir-senha")
    public ApiResponse<Void> redefinirSenha(@RequestBody UpdatePasswordDTO updatePasswordDTO) {
        return usuarioService.atualizarSenhaUsuario(updatePasswordDTO);
    }

    @GetMapping("/verificar-email/{email}")
<<<<<<< HEAD
    public ApiResponse<Void> verificarEmail(@PathVariable String email) {
        return usuarioService.verificarEmailDisponibilidade(email);
=======
    public ApiResponse<Boolean> verificarEmail(@PathVariable String email) {
        boolean exists = usuarioService.existsByEmail(email);
        return apiResponseUtil.map(!exists, Boolean.class,
                exists ? "Email já cadastrado" : "Email disponível");
>>>>>>> 3c46f92a3eab74bba1b2fc31a3bd29ad2f03f3ce
    }

    @GetMapping("/{keycloakId}")
    public ApiResponse<UsuarioResponseDTO> buscarUsuario(@PathVariable String keycloakId) {
        return usuarioService.buscarUsuario(keycloakId);
<<<<<<< HEAD
    }

    @GetMapping("/estatisticas/tipos")
    @PreAuthorize("hasRole('admin')")
    public ApiResponse<TipoUsuarioStatsResponseDTO> buscarEstatisticasTipoUsuario() {
        return usuarioService.buscarEstatisticasTipoUsuario();
    }

    @GetMapping("/setores")
    @PreAuthorize("hasRole('admin')")
    public ApiResponse<List<String>> buscarSetoresDistintos() {
        return usuarioService.buscarSetoresDistintos();
    }

    @GetMapping("/setor/{keycloakId}")
    public ApiResponse<List<UsuarioResponseDTO>> buscarUsuariosPorSetor(@PathVariable String keycloakId) {
        return usuarioService.buscarUsuariosPorSetor(keycloakId);
=======
>>>>>>> 3c46f92a3eab74bba1b2fc31a3bd29ad2f03f3ce
    }
}
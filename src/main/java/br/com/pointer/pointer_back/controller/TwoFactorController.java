package br.com.pointer.pointer_back.controller;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.TwoFactorSetupDTO;
import br.com.pointer.pointer_back.dto.TwoFactorVerifyDTO;
import br.com.pointer.pointer_back.dto.TwoFactorStatusDTO;
import br.com.pointer.pointer_back.service.TwoFactorService;
import br.com.pointer.pointer_back.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/2fa")
@RequiredArgsConstructor
public class TwoFactorController {

    private final TwoFactorService twoFactorService;
    private final UsuarioService usuarioService;

    /**
     * Configura 2FA para um usuário
     */
    @PostMapping("/setup/{keycloakId}")
    @PreAuthorize("hasRole('admin') or hasRole('gestor') or hasRole('colaborador')")
    public ApiResponse<TwoFactorSetupDTO> setupTwoFactor(@PathVariable String keycloakId) {
        return usuarioService.setupTwoFactor(keycloakId);
    }

    /**
     * Verifica o status do 2FA para um usuário
     */
    @GetMapping("/status/{keycloakId}")
    @PreAuthorize("hasRole('admin') or hasRole('gestor') or hasRole('colaborador')")
    public ApiResponse<TwoFactorStatusDTO> getTwoFactorStatus(@PathVariable String keycloakId) {
        return usuarioService.getTwoFactorStatus(keycloakId);
    }

    /**
     * Verifica código 2FA durante login
     */
    @PostMapping("/verify")
    public ApiResponse<Void> verifyTwoFactor(@RequestBody TwoFactorVerifyDTO verifyDTO) {
        return usuarioService.verifyTwoFactor(verifyDTO);
    }

    /**
     * Ativa 2FA após verificação inicial
     */
    @PostMapping("/activate/{keycloakId}")
    @PreAuthorize("hasRole('admin') or hasRole('gestor') or hasRole('colaborador')")
    public ApiResponse<Void> activateTwoFactor(@PathVariable String keycloakId, @RequestBody TwoFactorVerifyDTO verifyDTO) {
        return usuarioService.activateTwoFactor(keycloakId, verifyDTO);
    }

    /**
     * Desabilita 2FA para um usuário
     */
    @DeleteMapping("/disable/{keycloakId}")
    @PreAuthorize("hasRole('admin') or hasRole('gestor') or hasRole('colaborador')")
    public ApiResponse<Void> disableTwoFactor(@PathVariable String keycloakId) {
        return usuarioService.disableTwoFactor(keycloakId);
    }
} 
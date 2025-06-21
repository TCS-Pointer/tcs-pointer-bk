package br.com.pointer.pointer_back.controller;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.MarcoPDIDTO;
import br.com.pointer.pointer_back.service.MarcoPDIService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/marco-pdi")
public class MarcoPDIController {

    @Autowired
    private MarcoPDIService marcoPDIService;

    @PostMapping
    @PreAuthorize("hasRole('admin') or hasRole('gestor')")
    public ApiResponse<MarcoPDIDTO> criar(@Valid @RequestBody MarcoPDIDTO marcoPDIDTO) {
        return marcoPDIService.criar(marcoPDIDTO);
    }

    @GetMapping("/pdi/{pdiId}")
    @PreAuthorize("hasRole('usuario') or hasRole('admin') or hasRole('gestor')")
    public ApiResponse<List<MarcoPDIDTO>> listarPorPDI(@PathVariable Long pdiId) {
        return marcoPDIService.listarPorPDI(pdiId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('usuario') or hasRole('admin') or hasRole('gestor')")
    public ApiResponse<MarcoPDIDTO> buscarPorId(@PathVariable Long id) {
        return marcoPDIService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('gestor')")
    public ApiResponse<MarcoPDIDTO> atualizar(@PathVariable Long id, @RequestBody MarcoPDIDTO marcoPDIDTO) {
        return marcoPDIService.atualizar(id, marcoPDIDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('gestor')")
    public ApiResponse<Void> deletar(@PathVariable Long id) {
        return marcoPDIService.deletar(id);
    }
}
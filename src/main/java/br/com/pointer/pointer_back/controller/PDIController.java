package br.com.pointer.pointer_back.controller;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.pdiDTO;
import br.com.pointer.pointer_back.dto.AtualizarStatusPDIDTO;
import br.com.pointer.pointer_back.service.PDIService;
import br.com.pointer.pointer_back.dto.PdiListagemDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pdi")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PDIController {

    @Autowired
    private PDIService pdiService;

    @GetMapping("/destinatario/{idDestinatario}")
    @PreAuthorize("hasRole('usuario')")
    public ApiResponse<List<pdiDTO>> buscarPorDestinatario(@PathVariable Long idDestinatario) {
        return pdiService.buscarPorDestinatario(idDestinatario);
    }

    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("hasRole('admin') or hasRole('gestor')")
    public ApiResponse<List<pdiDTO>> buscarPorUsuario(@PathVariable Long idUsuario) {
        return pdiService.buscarPorUsuario(idUsuario);
    }

    @GetMapping("/com-destinatario")
    @PreAuthorize("hasRole('admin')")
    public ApiResponse<List<pdiDTO>> listarTodosComDestinatario() {
        return pdiService.listarTodosComDestinatario();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('gestor')")
    public ApiResponse<pdiDTO> buscarPorId(@PathVariable Long id) {
        return pdiService.buscarPorId(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('admin') or hasRole('gestor')")
    public ApiResponse<pdiDTO> criar(@RequestBody pdiDTO pdiDTO) {
        return pdiService.criar(pdiDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('gestor')")
    public ApiResponse<pdiDTO> atualizar(@PathVariable Long id, @RequestBody pdiDTO pdiDTO) {
        return pdiService.atualizar(id, pdiDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('gestor')")
    public ApiResponse<Void> deletar(@PathVariable Long id) {
        return pdiService.deletar(id);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('usuario') or hasRole('admin') or hasRole('gestor')")
    public ApiResponse<pdiDTO> atualizarStatus(@PathVariable Long id, @RequestBody AtualizarStatusPDIDTO dto) {
        return pdiService.atualizarStatus(id, dto);
    }

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public ApiResponse<List<pdiDTO>> listarTodos() {
        return pdiService.listarTodos();
    }

    @GetMapping("/listagem-simples")
    @PreAuthorize("hasRole('admin')")
    public List<PdiListagemDTO> listarSimples() {
        return pdiService.listarParaListagem();
    }
}

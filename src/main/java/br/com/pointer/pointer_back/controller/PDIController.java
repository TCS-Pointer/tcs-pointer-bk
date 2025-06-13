package br.com.pointer.pointer_back.controller;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.pdiDTO;
import br.com.pointer.pointer_back.dto.AtualizarStatusPDIDTO;
import br.com.pointer.pointer_back.service.PDIService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pdi")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PDIController {

    @Autowired
    private PDIService pdiService;

    // feito: Verificar se todos os marcos estão CONCLUIDOS, PDI será CONCLUIDO
    // TODO: Get PDI por ID Usuario (criador) (request param)
    // TODO: Get PDI por ID Destinatário (request param)
    // feito: Trocar destinatario para idDestinatario
    // feito: Validação data inicial < data final PDI e Marcos
    // feito: Validar se todos os marcos estão CONCLUIDOS, PDI será CONCLUIDO
    // feito: Validar duração mínima de 1 mês

    // feito: Rota get todas os PDI (somente admin)
    // feito: Rota get PDI por ID usuario (rota do gestor e admin)
    // feito: Rota get PDI por ID destinatário (rota do usuario)
    @GetMapping("/destinatario/{idDestinatario}")
    @PreAuthorize("hasRole('usuario')")
    public ResponseEntity<ApiResponse<List<pdiDTO>>> buscarPorDestinatario(@PathVariable Long idDestinatario) {
        List<pdiDTO> pdis = pdiService.buscarPorDestinatario(idDestinatario);
        return ResponseEntity.ok(new ApiResponse<List<pdiDTO>>().ok(pdis, "PDIs do destinatário listados com sucesso"));
    }

    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("hasRole('admin') or hasRole('gestor')")
    public ResponseEntity<ApiResponse<List<pdiDTO>>> buscarPorUsuario(@PathVariable Long idUsuario) {
        List<pdiDTO> pdis = pdiService.buscarPorUsuario(idUsuario);
        return ResponseEntity.ok(new ApiResponse<List<pdiDTO>>().ok(pdis, "PDIs do usuário listados com sucesso"));
    }

    @GetMapping("/com-destinatario")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<ApiResponse<List<pdiDTO>>> listarTodosComDestinatario() {
        List<pdiDTO> pdis = pdiService.listarTodosComDestinatario();
        return ResponseEntity
                .ok(new ApiResponse<List<pdiDTO>>().ok(pdis, "PDIs com destinatário listados com sucesso"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('gestor')")
    public ResponseEntity<ApiResponse<pdiDTO>> buscarPorId(@PathVariable Long id) {
        pdiDTO pdi = pdiService.buscarPorId(id);
        return ResponseEntity.ok(new ApiResponse<pdiDTO>().ok(pdi, "PDI encontrado com sucesso"));
    }

    @PostMapping
    @PreAuthorize("hasRole('admin') or hasRole('gestor')")
    public ResponseEntity<ApiResponse<pdiDTO>> criar(@RequestBody pdiDTO pdiDTO) {
        pdiDTO pdiCriado = pdiService.criar(pdiDTO);
        return ResponseEntity.status(201).body(new ApiResponse<pdiDTO>().ok(pdiCriado, "PDI criado com sucesso"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('gestor')")
    public ResponseEntity<ApiResponse<pdiDTO>> atualizar(@PathVariable Long id, @RequestBody pdiDTO pdiDTO) {
        pdiDTO pdiAtualizado = pdiService.atualizar(id, pdiDTO);
        return ResponseEntity.ok(new ApiResponse<pdiDTO>().ok(pdiAtualizado, "PDI atualizado com sucesso"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('gestor')")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        pdiService.deletar(id);
        return ResponseEntity.ok(new ApiResponse<Void>().ok(null, "PDI deletado com sucesso"));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('usuario') or hasRole('admin')")
    public ResponseEntity<ApiResponse<pdiDTO>> atualizarStatus(@PathVariable Long id,
            @RequestBody AtualizarStatusPDIDTO dto) {
        try {
            pdiDTO pdiAtualizado = pdiService.atualizarStatus(id, dto);
            return ResponseEntity
                    .ok(new ApiResponse<pdiDTO>().ok(pdiAtualizado, "Status do PDI atualizado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<pdiDTO>().badRequest("Erro ao atualizar status do PDI: " + e.getMessage()));
        }
    }
}

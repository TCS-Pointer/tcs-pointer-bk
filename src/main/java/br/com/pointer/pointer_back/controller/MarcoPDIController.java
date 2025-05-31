package br.com.pointer.pointer_back.controller;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.MarcoPDIDTO;
import br.com.pointer.pointer_back.service.MarcoPDIService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<MarcoPDIDTO>> criar(@Valid @RequestBody MarcoPDIDTO marcoPDIDTO) {
        try {
            MarcoPDIDTO marcoCriado = marcoPDIService.criar(marcoPDIDTO);
            return ResponseEntity.status(201)
                    .body(new ApiResponse<MarcoPDIDTO>().ok(marcoCriado, "Marco PDI criado com sucesso"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<MarcoPDIDTO>().badRequest(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<MarcoPDIDTO>().badRequest("Erro ao criar Marco PDI: " + e.getMessage()));
        }
    }

    @GetMapping("/pdi/{pdiId}")
    @PreAuthorize("hasRole('usuario') or hasRole('admin') or hasRole('gestor')")
    public ResponseEntity<ApiResponse<List<MarcoPDIDTO>>> listarPorPDI(@PathVariable Long pdiId) {
        try {
            List<MarcoPDIDTO> marcos = marcoPDIService.listarPorPDI(pdiId);
            return ResponseEntity
                    .ok(new ApiResponse<List<MarcoPDIDTO>>().ok(marcos, "Marcos PDI listados com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<List<MarcoPDIDTO>>().badRequest("Erro ao listar Marcos PDI: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('usuario') or hasRole('admin') or hasRole('gestor')")
    public ResponseEntity<ApiResponse<MarcoPDIDTO>> buscarPorId(@PathVariable Long id) {
        try {
            MarcoPDIDTO marco = marcoPDIService.buscarPorId(id);
            return ResponseEntity.ok(new ApiResponse<MarcoPDIDTO>().ok(marco, "Marco PDI encontrado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<MarcoPDIDTO>().badRequest("Erro ao buscar Marco PDI: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('gestor')")
    public ResponseEntity<ApiResponse<MarcoPDIDTO>> atualizar(@PathVariable Long id,
            @RequestBody MarcoPDIDTO marcoPDIDTO) {
        try {
            MarcoPDIDTO marcoAtualizado = marcoPDIService.atualizar(id, marcoPDIDTO);
            return ResponseEntity
                    .ok(new ApiResponse<MarcoPDIDTO>().ok(marcoAtualizado, "Marco PDI atualizado com sucesso"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<MarcoPDIDTO>().badRequest(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<MarcoPDIDTO>().badRequest("Erro ao atualizar Marco PDI: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('gestor')")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        try {
            marcoPDIService.deletar(id);
            return ResponseEntity.ok(new ApiResponse<Void>().ok(null, "Marco PDI deletado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<Void>().badRequest("Erro ao deletar Marco PDI: " + e.getMessage()));
        }
    }
}
package br.com.pointer.pointer_back.controller;

import br.com.pointer.pointer_back.dto.MarcoPDIDTO;
import br.com.pointer.pointer_back.service.MarcoPDIService;
import br.com.pointer.pointer_back.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/marco-pdi")
public class MarcoPDIController {
    private static final Logger logger = LoggerFactory.getLogger(MarcoPDIController.class);

    @Autowired
    private MarcoPDIService marcoPDIService;

    @PostMapping
    @PreAuthorize("hasRole('admin') or hasRole('gestor')")
    public ResponseEntity<ApiResponse<MarcoPDIDTO>> criar(@Valid @RequestBody MarcoPDIDTO marcoPDIDTO) {
        try {
            logger.info("Iniciando criação de Marco PDI com dados: {}", marcoPDIDTO);
            MarcoPDIDTO marcoCriado = marcoPDIService.criar(marcoPDIDTO);
            ApiResponse<MarcoPDIDTO> response = new ApiResponse<MarcoPDIDTO>().ok(
                    marcoCriado,
                    "Marco PDI criado com sucesso");
            return ResponseEntity.status(201).body(response);
        } catch (IllegalArgumentException e) {
            logger.error("Erro de validação ao criar Marco PDI: ", e);
            ApiResponse<MarcoPDIDTO> response = new ApiResponse<MarcoPDIDTO>().badRequest(
                    e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            logger.error("Erro ao criar Marco PDI: ", e);
            ApiResponse<MarcoPDIDTO> response = new ApiResponse<MarcoPDIDTO>().badRequest(
                    "Erro ao criar Marco PDI: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/pdi/{pdiId}")
    @PreAuthorize("hasRole('usuario') or hasRole('admin') or hasRole('gestor')")
    public ResponseEntity<ApiResponse<List<MarcoPDIDTO>>> listarPorPDI(@PathVariable Long pdiId) {
        try {
            logger.info("Iniciando listagem de Marcos PDI para o PDI: {}", pdiId);
            List<MarcoPDIDTO> marcos = marcoPDIService.listarPorPDI(pdiId);
            ApiResponse<List<MarcoPDIDTO>> response = new ApiResponse<List<MarcoPDIDTO>>().ok(
                    marcos,
                    "Marcos PDI listados com sucesso");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erro ao listar Marcos PDI: ", e);
            ApiResponse<List<MarcoPDIDTO>> response = new ApiResponse<List<MarcoPDIDTO>>().badRequest(
                    "Erro ao listar Marcos PDI: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('usuario') or hasRole('admin') or hasRole('gestor')")
    public ResponseEntity<ApiResponse<MarcoPDIDTO>> buscarPorId(@PathVariable Long id) {
        try {
            logger.info("Buscando Marco PDI com ID: {}", id);
            MarcoPDIDTO marco = marcoPDIService.buscarPorId(id);
            ApiResponse<MarcoPDIDTO> response = new ApiResponse<MarcoPDIDTO>().ok(
                    marco,
                    "Marco PDI encontrado com sucesso");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erro ao buscar Marco PDI: ", e);
            ApiResponse<MarcoPDIDTO> response = new ApiResponse<MarcoPDIDTO>().badRequest(
                    "Erro ao buscar Marco PDI: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('gestor')")
    public ResponseEntity<ApiResponse<MarcoPDIDTO>> atualizar(@PathVariable Long id,
            @RequestBody MarcoPDIDTO marcoPDIDTO) {
        try {
            logger.info("Atualizando Marco PDI com ID: {}", id);
            MarcoPDIDTO marcoAtualizado = marcoPDIService.atualizar(id, marcoPDIDTO);
            ApiResponse<MarcoPDIDTO> response = new ApiResponse<MarcoPDIDTO>().ok(
                    marcoAtualizado,
                    "Marco PDI atualizado com sucesso");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("Erro de validação ao atualizar Marco PDI: ", e);
            ApiResponse<MarcoPDIDTO> response = new ApiResponse<MarcoPDIDTO>().badRequest(
                    e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            logger.error("Erro ao atualizar Marco PDI: ", e);
            ApiResponse<MarcoPDIDTO> response = new ApiResponse<MarcoPDIDTO>().badRequest(
                    "Erro ao atualizar Marco PDI: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('gestor')")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        try {
            logger.info("Deletando Marco PDI com ID: {}", id);
            marcoPDIService.deletar(id);
            ApiResponse<Void> response = new ApiResponse<Void>().ok(
                    null,
                    "Marco PDI deletado com sucesso");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erro ao deletar Marco PDI: ", e);
            ApiResponse<Void> response = new ApiResponse<Void>().badRequest(
                    "Erro ao deletar Marco PDI: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
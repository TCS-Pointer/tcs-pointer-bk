package br.com.pointer.pointer_back.controller;

import br.com.pointer.pointer_back.dto.pdiDTO;
import br.com.pointer.pointer_back.service.PDIService;
import br.com.pointer.pointer_back.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/pdi")
public class PDIController {
    private static final Logger logger = LoggerFactory.getLogger(PDIController.class);

    @Autowired
    private PDIService pdiService;

    @PostMapping
    @PreAuthorize("hasRole('admin') or hasRole('gestor')")
    public ResponseEntity<ApiResponse<pdiDTO>> criar(@RequestBody pdiDTO pdiDTO) {
        try {
            logger.info("Iniciando criação de PDI com dados: {}", pdiDTO);
            pdiDTO pdiCriado = pdiService.criar(pdiDTO);
            ApiResponse<pdiDTO> response = new ApiResponse<pdiDTO>().ok(
                    pdiCriado,
                    "PDI criado com sucesso");
            return ResponseEntity.status(201).body(response);
        } catch (IllegalArgumentException e) {
            logger.error("Erro de validação ao criar PDI: ", e);
            ApiResponse<pdiDTO> response = new ApiResponse<pdiDTO>().badRequest(
                    e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            logger.error("Erro ao criar PDI: ", e);
            ApiResponse<pdiDTO> response = new ApiResponse<pdiDTO>().badRequest(
                    "Erro ao criar PDI: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('usuario') or hasRole('admin') or hasRole('gestor')")
    public ResponseEntity<ApiResponse<List<pdiDTO>>> listarTodos() {
        try {
            logger.info("Iniciando listagem de PDIs");
            List<pdiDTO> pdis = pdiService.listarTodos();
            ApiResponse<List<pdiDTO>> response = new ApiResponse<List<pdiDTO>>().ok(
                    pdis,
                    "PDIs listados com sucesso");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erro ao listar PDIs: ", e);
            ApiResponse<List<pdiDTO>> response = new ApiResponse<List<pdiDTO>>().badRequest(
                    "Erro ao listar PDIs: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('usuario') or hasRole('admin') or hasRole('gestor')")
    public ResponseEntity<ApiResponse<pdiDTO>> buscarPorId(@PathVariable Long id) {
        try {
            logger.info("Buscando PDI com ID: {}", id);
            pdiDTO pdi = pdiService.buscarPorId(id);
            ApiResponse<pdiDTO> response = new ApiResponse<pdiDTO>().ok(
                    pdi,
                    "PDI encontrado com sucesso");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erro ao buscar PDI: ", e);
            ApiResponse<pdiDTO> response = new ApiResponse<pdiDTO>().badRequest(
                    "Erro ao buscar PDI: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('gestor')")
    public ResponseEntity<ApiResponse<pdiDTO>> atualizar(@PathVariable Long id, @RequestBody pdiDTO pdiDTO) {
        try {
            logger.info("Atualizando PDI com ID: {}", id);
            pdiDTO pdiAtualizado = pdiService.atualizar(id, pdiDTO);
            ApiResponse<pdiDTO> response = new ApiResponse<pdiDTO>().ok(
                    pdiAtualizado,
                    "PDI atualizado com sucesso");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("Erro de validação ao atualizar PDI: ", e);
            ApiResponse<pdiDTO> response = new ApiResponse<pdiDTO>().badRequest(
                    e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            logger.error("Erro ao atualizar PDI: ", e);
            ApiResponse<pdiDTO> response = new ApiResponse<pdiDTO>().badRequest(
                    "Erro ao atualizar PDI: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('gestor')")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        try {
            logger.info("Deletando PDI com ID: {}", id);
            pdiService.deletar(id);
            ApiResponse<Void> response = new ApiResponse<Void>().ok(
                    null,
                    "PDI deletado com sucesso");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erro ao deletar PDI: ", e);
            ApiResponse<Void> response = new ApiResponse<Void>().badRequest(
                    "Erro ao deletar PDI: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}

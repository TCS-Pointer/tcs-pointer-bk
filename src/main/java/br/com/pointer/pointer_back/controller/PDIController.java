package br.com.pointer.pointer_back.controller;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.pdiDTO;
import br.com.pointer.pointer_back.service.PDIService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pdi")
public class PDIController {

    @Autowired
    private PDIService pdiService;

    @PostMapping
    @PreAuthorize("hasRole('admin') or hasRole('gestor')")
    public ResponseEntity<ApiResponse<pdiDTO>> criar(@RequestBody pdiDTO pdiDTO) {
        pdiDTO pdiCriado = pdiService.criar(pdiDTO);
        return ResponseEntity.status(201).body(new ApiResponse<pdiDTO>().ok(pdiCriado, "PDI criado com sucesso"));
    }

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<ApiResponse<List<pdiDTO>>> listarTodos() {
        List<pdiDTO> pdis = pdiService.listarTodos();
        return ResponseEntity.ok(new ApiResponse<List<pdiDTO>>().ok(pdis, "PDIs listados com sucesso"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('usuario') or hasRole('admin') or hasRole('gestor')")
    public ResponseEntity<ApiResponse<pdiDTO>> buscarPorId(@PathVariable Long id) {
        pdiDTO pdi = pdiService.buscarPorId(id);
        return ResponseEntity.ok(new ApiResponse<pdiDTO>().ok(pdi, "PDI encontrado com sucesso"));
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
}

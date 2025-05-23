package br.com.pointer.pointer_back.controller;

import br.com.pointer.pointer_back.dto.pdiDTO;
import br.com.pointer.pointer_back.service.PDIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pdis")
public class PDIController {

    @Autowired
    private PDIService pdiService;

    // @GetMapping("/listar-pdis")
    // @PreAuthorize("hasRole('usuario') or hasRole('admin') or hasRole('gestor')")
    // public ResponseEntity<List<PDIResponseDTO>> listarTodos() {
    //     return ResponseEntity.ok(pdiService.getAllPDIs());
    // }

    @GetMapping("/buscar-pdi/{id}")
    @PreAuthorize("hasRole('usuario') or hasRole('admin') or hasRole('gestor')")
    public ResponseEntity<pdiDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pdiService.buscarPorId(id));
    }

    @PostMapping("/criar-pdi")
    @PreAuthorize("hasRole('admin') or hasRole('gestor')")
    public ResponseEntity<pdiDTO> criar(@RequestBody pdiDTO pdiDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pdiService.criar(pdiDTO));
    }

    // @PutMapping("/atualizar-pdi/{id}")
    // @PreAuthorize("hasRole('admin') or hasRole('gestor')")
    // public ResponseEntity<PDIResponseDTO> atualizar(@PathVariable Long id, @RequestBody pdiDTO pdiDTO) {
    //     return ResponseEntity.ok(pdiService.updatePDI(id, pdiDTO));
    // }

    // @DeleteMapping("/deletar-pdi/{id}")
    // @PreAuthorize("hasRole('admin') or hasRole('gestor')")
    // public ResponseEntity<Void> deletar(@PathVariable Long id) {
    //     pdiService.deletePDI(id);
    //     return ResponseEntity.noContent().build();
    // }
}

package br.com.pointer.pointer_back.controller;

import br.com.pointer.pointer_back.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService relatorioService;

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/usuarios")
    public ResponseEntity<byte[]> downloadRelatorioUsuarios() {
        byte[] csvContent = relatorioService.gerarRelatorioUsuarios();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "relatorio_usuarios.csv");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(csvContent);
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/pdi")
    public ResponseEntity<byte[]> downloadRelatorioPDI() {
        byte[] csvContent = relatorioService.gerarRelatorioPDI();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "relatorio_pdi.csv");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(csvContent);
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/comunicados")
    public ResponseEntity<byte[]> downloadRelatorioComunicados() {
        byte[] csvContent = relatorioService.gerarRelatorioComunicados();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "relatorio_comunicados.csv");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(csvContent);
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/feedback")
    public ResponseEntity<byte[]> downloadRelatorioFeedback() {
        byte[] csvContent = relatorioService.gerarRelatorioFeedback();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "relatorio_feedback.csv");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(csvContent);
    }
} 
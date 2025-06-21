package br.com.pointer.pointer_back.controller;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.SetorCargoDTO;
import br.com.pointer.pointer_back.service.SetorCargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/setor-cargo")
public class SetorCargoController {

    @Autowired
    private SetorCargoService setorCargoService;

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public ApiResponse<SetorCargoDTO> listarSetoresECargos() {
        return setorCargoService.listarSetoresECargos();
    }
}
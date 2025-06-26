package br.com.pointer.pointer_back.controller;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.SetorCargoDTO;
import br.com.pointer.pointer_back.service.SetorCargoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SetorCargoControllerTest {

    @Mock
    private SetorCargoService setorCargoService;

    @InjectMocks
    private SetorCargoController setorCargoController;

    private MockMvc mockMvc;
    private SetorCargoDTO setorCargoDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(setorCargoController).build();

        // Setup SetorCargoDTO
        setorCargoDTO = new SetorCargoDTO();
        SetorCargoDTO.Setor setor = new SetorCargoDTO.Setor();
        setor.setSetor("TI");
        setor.setCargos(Arrays.asList("Desenvolvedor", "Analista"));
        setorCargoDTO.setSetores(Arrays.asList(setor));
    }

    @Test
    @WithMockUser(roles = "admin")
    void listarSetoresECargos_ComPermissaoAdmin_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<SetorCargoDTO> response = ApiResponse.success(setorCargoDTO, "Setores e cargos listados com sucesso");
        when(setorCargoService.listarSetoresECargos()).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/setor-cargo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Setores e cargos listados com sucesso"))
                .andExpect(jsonPath("$.content.setores[0].setor").value("TI"))
                .andExpect(jsonPath("$.content.setores[0].cargos[0]").value("Desenvolvedor"));

        verify(setorCargoService).listarSetoresECargos();
    }

    @Test
    @WithMockUser(roles = "admin")
    void listarSetoresECargos_ListaVazia_DeveRetornar200() throws Exception {
        // Arrange
        SetorCargoDTO setorCargoVazio = new SetorCargoDTO();
        SetorCargoDTO.Setor setorVazio = new SetorCargoDTO.Setor();
        setorVazio.setSetor("");
        setorVazio.setCargos(Arrays.asList(""));
        setorCargoVazio.setSetores(Arrays.asList(setorVazio));
        
        ApiResponse<SetorCargoDTO> response = ApiResponse.success(setorCargoVazio, "Setores e cargos listados com sucesso");
        when(setorCargoService.listarSetoresECargos()).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/setor-cargo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.content.setores[0].setor").value(""))
                .andExpect(jsonPath("$.content.setores[0].cargos[0]").value(""));

        verify(setorCargoService).listarSetoresECargos();
    }

    @Test
    @WithMockUser(roles = "admin")
    void listarSetoresECargos_ErroNoService_DeveRetornar400() throws Exception {
        // Arrange
        ApiResponse<SetorCargoDTO> response = ApiResponse.badRequest("Erro ao listar setores e cargos");
        when(setorCargoService.listarSetoresECargos()).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/setor-cargo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Erro ao listar setores e cargos"));

        verify(setorCargoService).listarSetoresECargos();
    }
} 
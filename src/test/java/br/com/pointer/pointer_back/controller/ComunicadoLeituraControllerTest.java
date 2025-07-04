package br.com.pointer.pointer_back.controller;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.ConfirmarLeituraDTO;
import br.com.pointer.pointer_back.dto.UsuarioComunicadoDTO;
import br.com.pointer.pointer_back.service.ComunicadoLeituraService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ComunicadoLeituraControllerTest {

    @Mock
    private ComunicadoLeituraService leituraService;

    @InjectMocks
    private ComunicadoLeituraController leituraController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private ConfirmarLeituraDTO confirmarLeituraDTO;
    private UsuarioComunicadoDTO usuarioComunicadoDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(leituraController).build();
        objectMapper = new ObjectMapper();

        // Setup ConfirmarLeituraDTO
        confirmarLeituraDTO = new ConfirmarLeituraDTO();
        confirmarLeituraDTO.setComunicadoId(1L);
        confirmarLeituraDTO.setKeycloakId("user-123");

        // Setup UsuarioComunicadoDTO
        usuarioComunicadoDTO = new UsuarioComunicadoDTO();
        usuarioComunicadoDTO.setId(1L);
        usuarioComunicadoDTO.setDtLeitura(LocalDateTime.now());
        usuarioComunicadoDTO.setUsuarioId(1L);
    }

    @Test
    @WithMockUser(roles = "colaborador")
    void confirmarLeitura_ComPermissaoColaborador_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<Void> response = ApiResponse.success(null, "Leitura confirmada com sucesso");
        when(leituraService.confirmarLeitura(any(ConfirmarLeituraDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/comunicados/confirmar-leitura")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(confirmarLeituraDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Leitura confirmada com sucesso"));

        verify(leituraService).confirmarLeitura(any(ConfirmarLeituraDTO.class));
    }

    @Test
    @WithMockUser(roles = "admin")
    void confirmarLeitura_ComPermissaoAdmin_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<Void> response = ApiResponse.success(null, "Leitura confirmada com sucesso");
        when(leituraService.confirmarLeitura(any(ConfirmarLeituraDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/comunicados/confirmar-leitura")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(confirmarLeituraDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        verify(leituraService).confirmarLeitura(any(ConfirmarLeituraDTO.class));
    }

    @Test
    void confirmarLeitura_SemPermissao_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<Void> response = ApiResponse.success(null, "Leitura confirmada com sucesso");
        when(leituraService.confirmarLeitura(any(ConfirmarLeituraDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/comunicados/confirmar-leitura")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(confirmarLeituraDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        verify(leituraService).confirmarLeitura(any(ConfirmarLeituraDTO.class));
    }

    @Test
    @WithMockUser(roles = "colaborador")
    void confirmarLeitura_DadosInvalidos_DeveRetornar400() throws Exception {
        // Arrange
        confirmarLeituraDTO.setComunicadoId(null);
        ApiResponse<Void> response = ApiResponse.badRequest("ID do comunicado é obrigatório");
        when(leituraService.confirmarLeitura(any(ConfirmarLeituraDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/comunicados/confirmar-leitura")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(confirmarLeituraDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("ID do comunicado é obrigatório"));

        verify(leituraService).confirmarLeitura(any(ConfirmarLeituraDTO.class));
    }

    @Test
    @WithMockUser(roles = "admin")
    void listarLeitores_ComPermissaoAdmin_DeveRetornar200() throws Exception {
        // Arrange
        List<UsuarioComunicadoDTO> leitores = Arrays.asList(usuarioComunicadoDTO);
        ApiResponse<List<UsuarioComunicadoDTO>> response = ApiResponse.success(leitores, "Leitores listados com sucesso");
        when(leituraService.listarLeitores(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/comunicados/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Leitores listados com sucesso"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].usuarioId").value(1))
                .andExpect(jsonPath("$.content[0].dtLeitura").exists());

        verify(leituraService).listarLeitores(1L);
    }

    @Test
    @WithMockUser(roles = "gestor")
    void listarLeitores_ComPermissaoGestor_DeveRetornar200() throws Exception {
        // Arrange
        List<UsuarioComunicadoDTO> leitores = Arrays.asList(usuarioComunicadoDTO);
        ApiResponse<List<UsuarioComunicadoDTO>> response = ApiResponse.success(leitores, "Leitores listados com sucesso");
        when(leituraService.listarLeitores(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/comunicados/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        verify(leituraService).listarLeitores(1L);
    }

    @Test
    @WithMockUser(roles = "colaborador")
    void listarLeitores_SemPermissao_DeveRetornar200() throws Exception {
        // Arrange
        List<UsuarioComunicadoDTO> leitores = Arrays.asList(usuarioComunicadoDTO);
        ApiResponse<List<UsuarioComunicadoDTO>> response = ApiResponse.success(leitores, "Leitores listados com sucesso");
        when(leituraService.listarLeitores(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/comunicados/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        verify(leituraService).listarLeitores(1L);
    }

    @Test
    @WithMockUser(roles = "admin")
    void listarLeitores_ComunicadoNaoEncontrado_DeveRetornar400() throws Exception {
        // Arrange
        ApiResponse<List<UsuarioComunicadoDTO>> response = ApiResponse.badRequest("Comunicado não encontrado");
        when(leituraService.listarLeitores(999L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/comunicados/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Comunicado não encontrado"));

        verify(leituraService).listarLeitores(999L);
    }

    @Test
    @WithMockUser(roles = "admin")
    void listarLeitores_ListaVazia_DeveRetornar200() throws Exception {
        // Arrange
        List<UsuarioComunicadoDTO> leitores = Arrays.asList();
        ApiResponse<List<UsuarioComunicadoDTO>> response = ApiResponse.success(leitores, "Leitores listados com sucesso");
        when(leituraService.listarLeitores(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/comunicados/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.content").isEmpty());

        verify(leituraService).listarLeitores(1L);
    }

    @Test
    @WithMockUser(roles = "admin")
    void listarLeitores_ComIdInvalido_DeveRetornar400() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/comunicados/abc"))
                .andExpect(status().isBadRequest());

        verify(leituraService, never()).listarLeitores(anyLong());
    }

    @Test
    @WithMockUser(roles = "colaborador")
    void confirmarLeitura_ComunicadoJaLido_DeveRetornar400() throws Exception {
        // Arrange
        ApiResponse<Void> response = ApiResponse.badRequest("Comunicado já foi lido por este usuário");
        when(leituraService.confirmarLeitura(any(ConfirmarLeituraDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/comunicados/confirmar-leitura")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(confirmarLeituraDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Comunicado já foi lido por este usuário"));

        verify(leituraService).confirmarLeitura(any(ConfirmarLeituraDTO.class));
    }

    @Test
    @WithMockUser(roles = "colaborador")
    void confirmarLeitura_ComunicadoNaoEncontrado_DeveRetornar400() throws Exception {
        // Arrange
        ApiResponse<Void> response = ApiResponse.badRequest("Comunicado não encontrado");
        when(leituraService.confirmarLeitura(any(ConfirmarLeituraDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/comunicados/confirmar-leitura")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(confirmarLeituraDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Comunicado não encontrado"));

        verify(leituraService).confirmarLeitura(any(ConfirmarLeituraDTO.class));
    }
} 
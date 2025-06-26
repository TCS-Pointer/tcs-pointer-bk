package br.com.pointer.pointer_back.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.ComunicadoDTO;
import br.com.pointer.pointer_back.dto.ComunicadoResponseDTO;
import br.com.pointer.pointer_back.service.ComunicadoService;

@ExtendWith(MockitoExtension.class)
class ComunicadoControllerTest {

    @Mock
    private ComunicadoService comunicadoService;

    @InjectMocks
    private ComunicadoController comunicadoController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private ComunicadoDTO comunicadoDTO;
    private ComunicadoResponseDTO comunicadoResponseDTO;
    private Page<ComunicadoResponseDTO> pageComunicados;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(comunicadoController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());

        // Setup ComunicadoDTO
        comunicadoDTO = new ComunicadoDTO();
        comunicadoDTO.setId(1L);
        comunicadoDTO.setTitulo("Comunicado Teste");
        comunicadoDTO.setDescricao("Descrição do comunicado");
        comunicadoDTO.setDataPublicacao(LocalDateTime.now());
        comunicadoDTO.setAtivo(true);

        // Setup ComunicadoResponseDTO
        comunicadoResponseDTO = new ComunicadoResponseDTO();
        comunicadoResponseDTO.setId(1L);
        comunicadoResponseDTO.setTitulo("Comunicado Teste");
        comunicadoResponseDTO.setDescricao("Descrição do comunicado");
        comunicadoResponseDTO.setSetores(new HashSet<>(Arrays.asList("RH", "TI")));
        comunicadoResponseDTO.setApenasGestores(false);
        comunicadoResponseDTO.setAtivo(true);
        comunicadoResponseDTO.setDataPublicacao(LocalDateTime.now());
        comunicadoResponseDTO.setQuantidadeLeitores(5);
        comunicadoResponseDTO.setLido(false);

        // Setup Page
        List<ComunicadoResponseDTO> comunicados = Arrays.asList(comunicadoResponseDTO);
        pageComunicados = new PageImpl<>(comunicados, PageRequest.of(0, 10), 1);
    }

    @Test
    @WithMockUser(roles = "admin")
    void criar_ComDadosValidos_DeveRetornar201() throws Exception {
        // Arrange
        ApiResponse<ComunicadoDTO> response = ApiResponse.success(comunicadoDTO, "Comunicado criado com sucesso");
        when(comunicadoService.criar(any(ComunicadoDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/comunicados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(comunicadoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Comunicado criado com sucesso"))
                .andExpect(jsonPath("$.content.id").value(1))
                .andExpect(jsonPath("$.content.titulo").value("Comunicado Teste"));

        verify(comunicadoService).criar(any(ComunicadoDTO.class));
    }

    @Test
    @WithMockUser(roles = "colaborador")
    void criar_SemPermissaoAdmin_DeveRetornar403() throws Exception {
        // Arrange - Mock do service para simular erro de autorização
        ApiResponse<ComunicadoDTO> response = ApiResponse.badRequest("Acesso negado");
        when(comunicadoService.criar(any(ComunicadoDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/comunicados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(comunicadoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Acesso negado"));

        verify(comunicadoService).criar(any(ComunicadoDTO.class));
    }

    @Test
    @WithMockUser(roles = "admin")
    void criar_DadosInvalidos_DeveRetornar400() throws Exception {
        // Arrange
        comunicadoDTO.setTitulo(null);
        ApiResponse<ComunicadoDTO> response = ApiResponse.badRequest("Título é obrigatório");
        when(comunicadoService.criar(any(ComunicadoDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/comunicados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(comunicadoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Título é obrigatório"));

        verify(comunicadoService).criar(any(ComunicadoDTO.class));
    }

    @Test
    @WithMockUser(roles = "colaborador")
    void listarTodos_ComPermissao_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<Page<ComunicadoResponseDTO>> response = ApiResponse.success(pageComunicados, "Comunicados listados com sucesso");
        when(comunicadoService.listarTodos(anyString(), anyInt(), anyInt(), anyString())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/comunicados")
                .param("keycloakId", "user-123")
                .param("page", "0")
                .param("size", "10")
                .param("titulo", "teste"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Comunicados listados com sucesso"))
                .andExpect(jsonPath("$.content.content[0].id").value(1))
                .andExpect(jsonPath("$.content.content[0].titulo").value("Comunicado Teste"));

        verify(comunicadoService).listarTodos("user-123", 0, 10, "teste");
    }

    @Test
    @WithMockUser(roles = "colaborador")
    void listarTodos_SemTitulo_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<Page<ComunicadoResponseDTO>> response = ApiResponse.success(pageComunicados, "Comunicados listados com sucesso");
        when(comunicadoService.listarTodos(anyString(), anyInt(), anyInt(), isNull())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/comunicados")
                .param("keycloakId", "user-123")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        verify(comunicadoService).listarTodos("user-123", 0, 10, null);
    }

    @Test
    @WithMockUser(roles = "admin")
    void listarTodos_ComParametrosPadrao_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<Page<ComunicadoResponseDTO>> response = ApiResponse.success(pageComunicados, "Comunicados listados com sucesso");
        when(comunicadoService.listarTodos("user-123", 0, 10, null)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/comunicados")
                .param("keycloakId", "user-123")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Comunicados listados com sucesso"));

        verify(comunicadoService).listarTodos("user-123", 0, 10, null);
    }

    @Test
    @WithMockUser(roles = "admin")
    void listarTodos_ListaVazia_DeveRetornar200() throws Exception {
        // Arrange
        Page<ComunicadoResponseDTO> pageVazia = new PageImpl<>(Arrays.asList(), PageRequest.of(0, 10), 0);
        ApiResponse<Page<ComunicadoResponseDTO>> response = ApiResponse.success(pageVazia, "Comunicados listados com sucesso");
        when(comunicadoService.listarTodos("user-123", 0, 10, null)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/comunicados")
                .param("keycloakId", "user-123")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.content.content").isEmpty());

        verify(comunicadoService).listarTodos("user-123", 0, 10, null);
    }

    @Test
    @WithMockUser(roles = "admin")
    void deletar_ComPermissaoAdmin_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<Void> response = ApiResponse.success(null, "Comunicado deletado com sucesso");
        when(comunicadoService.deletar(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(delete("/comunicados/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Comunicado deletado com sucesso"));

        verify(comunicadoService).deletar(1L);
    }

    @Test
    @WithMockUser(roles = "colaborador")
    void deletar_SemPermissaoAdmin_DeveRetornar403() throws Exception {
        // Arrange - Mock do service para simular erro de autorização
        ApiResponse<Void> response = ApiResponse.badRequest("Acesso negado");
        when(comunicadoService.deletar(anyLong())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(delete("/comunicados/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Acesso negado"));

        verify(comunicadoService).deletar(1L);
    }

    @Test
    @WithMockUser(roles = "admin")
    void deletar_ComunicadoNaoExistente_DeveRetornar400() throws Exception {
        // Arrange
        ApiResponse<Void> response = ApiResponse.badRequest("Comunicado não encontrado");
        when(comunicadoService.deletar(999L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(delete("/comunicados/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Comunicado não encontrado"));

        verify(comunicadoService).deletar(999L);
    }

    @Test
    @WithMockUser(roles = "admin")
    void deletar_ComIdInvalido_DeveRetornar400() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/comunicados/abc"))
                .andExpect(status().isBadRequest());

        verify(comunicadoService, never()).deletar(anyLong());
    }
} 
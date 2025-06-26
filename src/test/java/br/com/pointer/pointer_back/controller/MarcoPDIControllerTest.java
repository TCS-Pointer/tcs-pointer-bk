package br.com.pointer.pointer_back.controller;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.MarcoPDIDTO;
import br.com.pointer.pointer_back.enums.StatusMarcoPDI;
import br.com.pointer.pointer_back.service.MarcoPDIService;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MarcoPDIControllerTest {

    @Mock
    private MarcoPDIService marcoPDIService;

    @InjectMocks
    private MarcoPDIController marcoPDIController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private MarcoPDIDTO marcoPDIDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(marcoPDIController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JSR310Module());

        // Setup MarcoPDIDTO
        marcoPDIDTO = new MarcoPDIDTO();
        marcoPDIDTO.setId(1L);
        marcoPDIDTO.setTitulo("Marco Teste");
        marcoPDIDTO.setDescricao("Descrição do marco");
        marcoPDIDTO.setDtFinal(LocalDate.now().plusDays(30));
        marcoPDIDTO.setStatus(StatusMarcoPDI.PENDENTE);
        marcoPDIDTO.setPdiId(1L);
    }

    @Test
    @WithMockUser(roles = "admin")
    void criar_ComPermissaoAdmin_DeveRetornar201() throws Exception {
        // Arrange
        ApiResponse<MarcoPDIDTO> response = ApiResponse.success(marcoPDIDTO, "Marco PDI criado com sucesso");
        when(marcoPDIService.criar(any(MarcoPDIDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/marco-pdi")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(marcoPDIDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Marco PDI criado com sucesso"))
                .andExpect(jsonPath("$.content.id").value(1))
                .andExpect(jsonPath("$.content.titulo").value("Marco Teste"))
                .andExpect(jsonPath("$.content.descricao").value("Descrição do marco"))
                .andExpect(jsonPath("$.content.status").value("PENDENTE"))
                .andExpect(jsonPath("$.content.pdiId").value(1));

        verify(marcoPDIService).criar(any(MarcoPDIDTO.class));
    }

    @Test
    @WithMockUser(roles = "gestor")
    void criar_ComPermissaoGestor_DeveRetornar201() throws Exception {
        // Arrange
        ApiResponse<MarcoPDIDTO> response = ApiResponse.success(marcoPDIDTO, "Marco PDI criado com sucesso");
        when(marcoPDIService.criar(any(MarcoPDIDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/marco-pdi")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(marcoPDIDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        verify(marcoPDIService).criar(any(MarcoPDIDTO.class));
    }

    @Test
    @WithMockUser(roles = "colaborador")
    void criar_SemPermissao_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<MarcoPDIDTO> response = ApiResponse.success(marcoPDIDTO, "Marco PDI criado com sucesso");
        when(marcoPDIService.criar(any(MarcoPDIDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/marco-pdi")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(marcoPDIDTO)))
                .andExpect(status().isOk());

        verify(marcoPDIService).criar(any(MarcoPDIDTO.class));
    }

    @Test
    @WithMockUser(roles = "admin")
    void criar_DadosInvalidos_DeveRetornar400() throws Exception {
        // Arrange
        marcoPDIDTO.setTitulo(null);
        ApiResponse<MarcoPDIDTO> response = ApiResponse.badRequest("Título é obrigatório");
        when(marcoPDIService.criar(any(MarcoPDIDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/marco-pdi")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(marcoPDIDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Título é obrigatório"));

        verify(marcoPDIService).criar(any(MarcoPDIDTO.class));
    }

    @Test
    @WithMockUser(roles = "admin")
    void listarPorPDI_ComPermissaoAdmin_DeveRetornar200() throws Exception {
        // Arrange
        List<MarcoPDIDTO> marcos = Arrays.asList(marcoPDIDTO);
        ApiResponse<List<MarcoPDIDTO>> response = ApiResponse.success(marcos, "Marcos listados com sucesso");
        when(marcoPDIService.listarPorPDI(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/marco-pdi/pdi/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Marcos listados com sucesso"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].titulo").value("Marco Teste"));

        verify(marcoPDIService).listarPorPDI(1L);
    }

    @Test
    @WithMockUser(roles = "colaborador")
    void listarPorPDI_ComPermissaoColaborador_DeveRetornar200() throws Exception {
        // Arrange
        List<MarcoPDIDTO> marcos = Arrays.asList(marcoPDIDTO);
        ApiResponse<List<MarcoPDIDTO>> response = ApiResponse.success(marcos, "Marcos listados com sucesso");
        when(marcoPDIService.listarPorPDI(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/marco-pdi/pdi/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        verify(marcoPDIService).listarPorPDI(1L);
    }

    @Test
    @WithMockUser(roles = "colaborador")
    void listarPorPDI_SemPermissao_DeveRetornar200() throws Exception {
        // Arrange
        List<MarcoPDIDTO> marcos = Arrays.asList(marcoPDIDTO);
        ApiResponse<List<MarcoPDIDTO>> response = ApiResponse.success(marcos, "Marcos listados com sucesso");
        when(marcoPDIService.listarPorPDI(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/marco-pdi/pdi/1"))
                .andExpect(status().isOk());

        verify(marcoPDIService).listarPorPDI(1L);
    }

    @Test
    @WithMockUser(roles = "admin")
    void listarPorPDI_PDINaoEncontrado_DeveRetornar400() throws Exception {
        // Arrange
        ApiResponse<List<MarcoPDIDTO>> response = ApiResponse.badRequest("PDI não encontrado");
        when(marcoPDIService.listarPorPDI(999L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/marco-pdi/pdi/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("PDI não encontrado"));

        verify(marcoPDIService).listarPorPDI(999L);
    }

    @Test
    @WithMockUser(roles = "admin")
    void listarPorPDI_ListaVazia_DeveRetornar200() throws Exception {
        // Arrange
        List<MarcoPDIDTO> marcos = Arrays.asList();
        ApiResponse<List<MarcoPDIDTO>> response = ApiResponse.success(marcos, "Marcos listados com sucesso");
        when(marcoPDIService.listarPorPDI(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/marco-pdi/pdi/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.content").isEmpty());

        verify(marcoPDIService).listarPorPDI(1L);
    }

    @Test
    @WithMockUser(roles = "admin")
    void buscarPorId_ComPermissaoAdmin_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<MarcoPDIDTO> response = ApiResponse.success(marcoPDIDTO, "Marco encontrado com sucesso");
        when(marcoPDIService.buscarPorId(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/marco-pdi/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Marco encontrado com sucesso"))
                .andExpect(jsonPath("$.content.id").value(1))
                .andExpect(jsonPath("$.content.titulo").value("Marco Teste"));

        verify(marcoPDIService).buscarPorId(1L);
    }

    @Test
    @WithMockUser(roles = "colaborador")
    void buscarPorId_ComPermissaoColaborador_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<MarcoPDIDTO> response = ApiResponse.success(marcoPDIDTO, "Marco encontrado com sucesso");
        when(marcoPDIService.buscarPorId(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/marco-pdi/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        verify(marcoPDIService).buscarPorId(1L);
    }

    @Test
    @WithMockUser(roles = "colaborador")
    void buscarPorId_SemPermissao_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<MarcoPDIDTO> response = ApiResponse.success(marcoPDIDTO, "Marco encontrado com sucesso");
        when(marcoPDIService.buscarPorId(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/marco-pdi/1"))
                .andExpect(status().isOk());

        verify(marcoPDIService).buscarPorId(1L);
    }

    @Test
    @WithMockUser(roles = "admin")
    void buscarPorId_MarcoNaoEncontrado_DeveRetornar400() throws Exception {
        // Arrange
        ApiResponse<MarcoPDIDTO> response = ApiResponse.badRequest("Marco não encontrado");
        when(marcoPDIService.buscarPorId(999L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/marco-pdi/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Marco não encontrado"));

        verify(marcoPDIService).buscarPorId(999L);
    }

    @Test
    @WithMockUser(roles = "admin")
    void atualizar_ComPermissaoAdmin_DeveRetornar200() throws Exception {
        // Arrange
        marcoPDIDTO.setTitulo("Marco Atualizado");
        ApiResponse<MarcoPDIDTO> response = ApiResponse.success(marcoPDIDTO, "Marco atualizado com sucesso");
        when(marcoPDIService.atualizar(eq(1L), any(MarcoPDIDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(put("/marco-pdi/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(marcoPDIDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Marco atualizado com sucesso"))
                .andExpect(jsonPath("$.content.titulo").value("Marco Atualizado"));

        verify(marcoPDIService).atualizar(eq(1L), any(MarcoPDIDTO.class));
    }

    @Test
    @WithMockUser(roles = "gestor")
    void atualizar_ComPermissaoGestor_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<MarcoPDIDTO> response = ApiResponse.success(marcoPDIDTO, "Marco atualizado com sucesso");
        when(marcoPDIService.atualizar(eq(1L), any(MarcoPDIDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(put("/marco-pdi/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(marcoPDIDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        verify(marcoPDIService).atualizar(eq(1L), any(MarcoPDIDTO.class));
    }

    @Test
    @WithMockUser(roles = "colaborador")
    void atualizar_SemPermissao_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<MarcoPDIDTO> response = ApiResponse.success(marcoPDIDTO, "Marco atualizado com sucesso");
        when(marcoPDIService.atualizar(eq(1L), any(MarcoPDIDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(put("/marco-pdi/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(marcoPDIDTO)))
                .andExpect(status().isOk());

        verify(marcoPDIService).atualizar(eq(1L), any(MarcoPDIDTO.class));
    }

    @Test
    @WithMockUser(roles = "admin")
    void atualizar_MarcoNaoEncontrado_DeveRetornar400() throws Exception {
        // Arrange
        ApiResponse<MarcoPDIDTO> response = ApiResponse.badRequest("Marco não encontrado");
        when(marcoPDIService.atualizar(eq(999L), any(MarcoPDIDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(put("/marco-pdi/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(marcoPDIDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Marco não encontrado"));

        verify(marcoPDIService).atualizar(eq(999L), any(MarcoPDIDTO.class));
    }

    @Test
    @WithMockUser(roles = "admin")
    void deletar_ComPermissaoAdmin_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<Void> response = ApiResponse.success(null, "Marco deletado com sucesso");
        when(marcoPDIService.deletar(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(delete("/marco-pdi/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Marco deletado com sucesso"));

        verify(marcoPDIService).deletar(1L);
    }

    @Test
    @WithMockUser(roles = "gestor")
    void deletar_ComPermissaoGestor_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<Void> response = ApiResponse.success(null, "Marco deletado com sucesso");
        when(marcoPDIService.deletar(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(delete("/marco-pdi/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        verify(marcoPDIService).deletar(1L);
    }

    @Test
    @WithMockUser(roles = "colaborador")
    void deletar_SemPermissao_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<Void> response = ApiResponse.success(null, "Marco deletado com sucesso");
        when(marcoPDIService.deletar(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(delete("/marco-pdi/1"))
                .andExpect(status().isOk());

        verify(marcoPDIService).deletar(1L);
    }

    @Test
    @WithMockUser(roles = "admin")
    void deletar_MarcoNaoEncontrado_DeveRetornar400() throws Exception {
        // Arrange
        ApiResponse<Void> response = ApiResponse.badRequest("Marco não encontrado");
        when(marcoPDIService.deletar(999L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(delete("/marco-pdi/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Marco não encontrado"));

        verify(marcoPDIService).deletar(999L);
    }

    @Test
    @WithMockUser(roles = "admin")
    void buscarPorId_ComIdInvalido_DeveRetornar400() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/marco-pdi/abc"))
                .andExpect(status().isBadRequest());

        verify(marcoPDIService, never()).buscarPorId(anyLong());
    }

    @Test
    @WithMockUser(roles = "admin")
    void listarPorPDI_ComIdInvalido_DeveRetornar400() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/marco-pdi/pdi/abc"))
                .andExpect(status().isBadRequest());

        verify(marcoPDIService, never()).listarPorPDI(anyLong());
    }

    @Test
    @WithMockUser(roles = "admin")
    void atualizar_ComIdInvalido_DeveRetornar400() throws Exception {
        // Act & Assert
        mockMvc.perform(put("/marco-pdi/abc")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(marcoPDIDTO)))
                .andExpect(status().isBadRequest());

        verify(marcoPDIService, never()).atualizar(anyLong(), any(MarcoPDIDTO.class));
    }

    @Test
    @WithMockUser(roles = "admin")
    void deletar_ComIdInvalido_DeveRetornar400() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/marco-pdi/abc"))
                .andExpect(status().isBadRequest());

        verify(marcoPDIService, never()).deletar(anyLong());
    }
} 
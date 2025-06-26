package br.com.pointer.pointer_back.controller;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.pdiDTO;
import br.com.pointer.pointer_back.dto.AtualizarStatusPDIDTO;
import br.com.pointer.pointer_back.dto.PdiListagemDTO;
import br.com.pointer.pointer_back.service.PDIService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PDIControllerTest {

    @Mock
    private PDIService pdiService;

    @InjectMocks
    private PDIController pdiController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private pdiDTO pdiDTO;
    private AtualizarStatusPDIDTO statusDTO;
    private PdiListagemDTO listagemDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(pdiController).build();
        objectMapper = new ObjectMapper();

        // Setup PDI DTO
        pdiDTO = new pdiDTO();
        pdiDTO.setId(1L);
        pdiDTO.setTitulo("PDI Teste");
        pdiDTO.setDescricao("Descrição do PDI");

        // Setup Status DTO
        statusDTO = new AtualizarStatusPDIDTO();
        statusDTO.setIdMarco(1L);
        statusDTO.setStatusMarco(br.com.pointer.pointer_back.enums.StatusMarcoPDI.CONCLUIDO);

        // Setup Listagem DTO
        listagemDTO = new PdiListagemDTO(1L, "PDI Teste", "Descrição", 
            br.com.pointer.pointer_back.enums.StatusPDI.EM_ANDAMENTO, 
            java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(30),
            1L, "João Silva", "Desenvolvedor", "TI", "joao@teste.com", 5L, 2L);
    }

    @Test
    void buscarPorDestinatario_DeveRetornarLista() throws Exception {
        // Arrange
        List<pdiDTO> pdis = Arrays.asList(pdiDTO);
        ApiResponse<List<pdiDTO>> response = ApiResponse.success(pdis, "PDIs encontrados");
        when(pdiService.buscarPorDestinatario(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/pdi/destinatario/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("PDIs encontrados"))
                .andExpect(jsonPath("$.content").isArray());

        verify(pdiService).buscarPorDestinatario(1L);
    }

    @Test
    void buscarPorUsuario_DeveRetornarLista() throws Exception {
        // Arrange
        List<pdiDTO> pdis = Arrays.asList(pdiDTO);
        ApiResponse<List<pdiDTO>> response = ApiResponse.success(pdis, "PDIs encontrados");
        when(pdiService.buscarPorUsuario(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/pdi/usuario/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("PDIs encontrados"))
                .andExpect(jsonPath("$.content").isArray());

        verify(pdiService).buscarPorUsuario(1L);
    }

    @Test
    void listarTodosComDestinatario_DeveRetornarLista() throws Exception {
        // Arrange
        List<pdiDTO> pdis = Arrays.asList(pdiDTO);
        ApiResponse<List<pdiDTO>> response = ApiResponse.success(pdis, "PDIs listados");
        when(pdiService.listarTodosComDestinatario()).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/pdi/com-destinatario")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("PDIs listados"))
                .andExpect(jsonPath("$.content").isArray());

        verify(pdiService).listarTodosComDestinatario();
    }

    @Test
    void buscarPorId_DeveRetornarPDI() throws Exception {
        // Arrange
        ApiResponse<pdiDTO> response = ApiResponse.success(pdiDTO, "PDI encontrado");
        when(pdiService.buscarPorId(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/pdi/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("PDI encontrado"))
                .andExpect(jsonPath("$.content.id").value(1));

        verify(pdiService).buscarPorId(1L);
    }

    @Test
    void criar_DeveCriarPDI() throws Exception {
        // Arrange
        ApiResponse<pdiDTO> response = ApiResponse.success(pdiDTO, "PDI criado com sucesso");
        when(pdiService.criar(any(pdiDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/pdi")
                .content(objectMapper.writeValueAsString(pdiDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("PDI criado com sucesso"))
                .andExpect(jsonPath("$.content.id").value(1));

        verify(pdiService).criar(any(pdiDTO.class));
    }

    @Test
    void atualizar_DeveAtualizarPDI() throws Exception {
        // Arrange
        ApiResponse<pdiDTO> response = ApiResponse.success(pdiDTO, "PDI atualizado com sucesso");
        when(pdiService.atualizar(eq(1L), any(pdiDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(put("/pdi/1")
                .content(objectMapper.writeValueAsString(pdiDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("PDI atualizado com sucesso"))
                .andExpect(jsonPath("$.content.id").value(1));

        verify(pdiService).atualizar(eq(1L), any(pdiDTO.class));
    }

    @Test
    void deletar_DeveDeletarPDI() throws Exception {
        // Arrange
        ApiResponse<Void> response = ApiResponse.success(null, "PDI deletado com sucesso");
        when(pdiService.deletar(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(delete("/pdi/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("PDI deletado com sucesso"));

        verify(pdiService).deletar(1L);
    }

    @Test
    void atualizarStatus_DeveAtualizarStatus() throws Exception {
        // Arrange
        ApiResponse<pdiDTO> response = ApiResponse.success(pdiDTO, "Status atualizado com sucesso");
        when(pdiService.atualizarStatus(eq(1L), any(AtualizarStatusPDIDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(patch("/pdi/1/status")
                .content(objectMapper.writeValueAsString(statusDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Status atualizado com sucesso"))
                .andExpect(jsonPath("$.content.id").value(1));

        verify(pdiService).atualizarStatus(eq(1L), any(AtualizarStatusPDIDTO.class));
    }

    @Test
    void listarTodos_DeveRetornarLista() throws Exception {
        // Arrange
        List<pdiDTO> pdis = Arrays.asList(pdiDTO);
        ApiResponse<List<pdiDTO>> response = ApiResponse.success(pdis, "PDIs listados");
        when(pdiService.listarTodos()).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/pdi")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("PDIs listados"))
                .andExpect(jsonPath("$.content").isArray());

        verify(pdiService).listarTodos();
    }

    @Test
    void listarSimples_DeveRetornarLista() throws Exception {
        // Arrange
        List<PdiListagemDTO> listagem = Arrays.asList(listagemDTO);
        when(pdiService.listarParaListagem()).thenReturn(listagem);

        // Act & Assert
        mockMvc.perform(get("/pdi/listagem-simples")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].titulo").value("PDI Teste"));

        verify(pdiService).listarParaListagem();
    }

    @Test
    void criar_PDIInvalido_DeveRetornarErro() throws Exception {
        // Arrange
        pdiDTO pdiInvalido = new pdiDTO();
        // Sem título - inválido
        
        ApiResponse<pdiDTO> response = ApiResponse.badRequest("Erro ao criar PDI");
        when(pdiService.criar(any(pdiDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/pdi")
                .content(objectMapper.writeValueAsString(pdiInvalido))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Erro ao criar PDI"));

        verify(pdiService).criar(any(pdiDTO.class));
    }

    @Test
    void buscarPorId_PDINaoEncontrado_DeveRetornarErro() throws Exception {
        // Arrange
        ApiResponse<pdiDTO> response = ApiResponse.badRequest("PDI não encontrado");
        when(pdiService.buscarPorId(999L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/pdi/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("PDI não encontrado"));

        verify(pdiService).buscarPorId(999L);
    }

    @Test
    void atualizarStatus_StatusInvalido_DeveRetornarErro() throws Exception {
        // Arrange
        AtualizarStatusPDIDTO statusInvalido = new AtualizarStatusPDIDTO();
        statusInvalido.setIdMarco(1L);
        statusInvalido.setStatusMarco(null); // Status nulo é inválido
        
        ApiResponse<pdiDTO> response = ApiResponse.badRequest("Status inválido");
        when(pdiService.atualizarStatus(eq(1L), any(AtualizarStatusPDIDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(patch("/pdi/1/status")
                .content(objectMapper.writeValueAsString(statusInvalido))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Status inválido"));

        verify(pdiService).atualizarStatus(eq(1L), any(AtualizarStatusPDIDTO.class));
    }
} 
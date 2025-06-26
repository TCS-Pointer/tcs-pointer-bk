package br.com.pointer.pointer_back.controller;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.FeedbackDTO;
import br.com.pointer.pointer_back.dto.FeedbackResponseDTO;
import br.com.pointer.pointer_back.dto.FeedbackStatsResponseDTO;
import br.com.pointer.pointer_back.dto.FeedbackStatsDTO;
import br.com.pointer.pointer_back.dto.DestinatarioDTO;
import br.com.pointer.pointer_back.dto.RemetenteDTO;
import br.com.pointer.pointer_back.enums.TipoFeedback;
import br.com.pointer.pointer_back.service.FeedbackService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FeedbackControllerTest {

    @Mock
    private FeedbackService feedbackService;

    @InjectMocks
    private FeedbackController feedbackController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private FeedbackDTO feedbackDTO;
    private FeedbackResponseDTO feedbackResponseDTO;
    private FeedbackStatsResponseDTO feedbackStatsResponseDTO;
    private Page<FeedbackResponseDTO> pageFeedbacks;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(feedbackController).build();
        objectMapper = new ObjectMapper();

        // Setup FeedbackDTO
        feedbackDTO = new FeedbackDTO();
        feedbackDTO.setResolucaoDeProblemas(3);
        feedbackDTO.setTrabalhoEmEquipe(4);
        feedbackDTO.setTipoFeedback(TipoFeedback.CONSTRUTIVO);
        feedbackDTO.setIdUsuarioDestinatario(2L);
        feedbackDTO.setKeycloakIdRemetente("user-123");

        // Setup FeedbackResponseDTO
        feedbackResponseDTO = new FeedbackResponseDTO();
        feedbackResponseDTO.setIdFeedback(1L);
        feedbackResponseDTO.setAssunto("Feedback Teste");
        feedbackResponseDTO.setAcoesRecomendadas("Melhorar comunicação");
        feedbackResponseDTO.setAnonimo(false);
        feedbackResponseDTO.setAvComunicacao(4);
        feedbackResponseDTO.setAvProdutividade(5);
        feedbackResponseDTO.setResolucaoDeProblemas(3);
        feedbackResponseDTO.setTrabalhoEmEquipe(4);
        feedbackResponseDTO.setDtEnvio(LocalDateTime.now());
        
        // Setup DestinatarioDTO e RemetenteDTO
        DestinatarioDTO destinatarioDTO = new DestinatarioDTO();
        destinatarioDTO.setUsuarioId(2L);
        destinatarioDTO.setNome("Maria Santos");
        destinatarioDTO.setCargo("Desenvolvedora");
        
        RemetenteDTO remetenteDTO = new RemetenteDTO();
        remetenteDTO.setUsuarioId(1L);
        remetenteDTO.setNome("João Silva");
        remetenteDTO.setCargo("Desenvolvedor");
        
        feedbackResponseDTO.setDestinatarioDTO(destinatarioDTO);
        feedbackResponseDTO.setRemetenteDTO(remetenteDTO);

        // Setup FeedbackStatsResponseDTO
        feedbackStatsResponseDTO = new FeedbackStatsResponseDTO();
        feedbackStatsResponseDTO.setTotalGeral(10L);
        feedbackStatsResponseDTO.setStats(Arrays.asList(
            new FeedbackStatsDTO(TipoFeedback.POSITIVO, 5L),
            new FeedbackStatsDTO(TipoFeedback.CONSTRUTIVO, 3L)
        ));

        // Setup Page
        List<FeedbackResponseDTO> feedbacks = Arrays.asList(feedbackResponseDTO);
        pageFeedbacks = new PageImpl<>(feedbacks, PageRequest.of(0, 10), 1);
    }

    @Test
    void listarFeedbacksRecebidos_ComDadosValidos_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<Page<FeedbackResponseDTO>> response = ApiResponse.success(pageFeedbacks, "Feedbacks recebidos listados com sucesso");
        when(feedbackService.listarFeedbacksRecebidos(anyString(), anyString(), anyInt())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/feedback/recebidos")
                .param("keycloakId", "user-123")
                .param("keyword", "teste")
                .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Feedbacks recebidos listados com sucesso"))
                .andExpect(jsonPath("$.content.content[0].idFeedback").value(1))
                .andExpect(jsonPath("$.content.content[0].assunto").value("Feedback Teste"));

        verify(feedbackService).listarFeedbacksRecebidos("user-123", "teste", 0);
    }

    @Test
    void listarFeedbacksRecebidos_SemKeyword_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<Page<FeedbackResponseDTO>> response = ApiResponse.success(pageFeedbacks, "Feedbacks recebidos listados com sucesso");
        when(feedbackService.listarFeedbacksRecebidos(anyString(), isNull(), anyInt())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/feedback/recebidos")
                .param("keycloakId", "user-123")
                .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        verify(feedbackService).listarFeedbacksRecebidos("user-123", null, 0);
    }

    @Test
    void listarFeedbacksRecebidos_ComParametrosPadrao_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<Page<FeedbackResponseDTO>> response = ApiResponse.success(pageFeedbacks, "Feedbacks recebidos listados com sucesso");
        when(feedbackService.listarFeedbacksRecebidos(anyString(), anyString(), anyInt())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/feedback/recebidos")
                .param("keycloakId", "user-123")
                .param("keyword", "")
                .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Feedbacks recebidos listados com sucesso"));

        verify(feedbackService).listarFeedbacksRecebidos(anyString(), anyString(), anyInt());
    }

    @Test
    void listarFeedbacksEnviados_ComDadosValidos_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<Page<FeedbackResponseDTO>> response = ApiResponse.success(pageFeedbacks, "Feedbacks enviados listados com sucesso");
        when(feedbackService.listarFeedbacksEnviados(anyString(), anyString(), anyInt())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/feedback/enviados")
                .param("keycloakId", "user-123")
                .param("keyword", "teste")
                .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Feedbacks enviados listados com sucesso"))
                .andExpect(jsonPath("$.content.content[0].idFeedback").value(1));

        verify(feedbackService).listarFeedbacksEnviados("user-123", "teste", 0);
    }

    @Test
    @WithMockUser(roles = "admin")
    void listarTodosFeedbacks_ComPermissaoAdmin_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<Page<FeedbackResponseDTO>> response = ApiResponse.success(pageFeedbacks, "Todos os feedbacks listados com sucesso");
        when(feedbackService.listarTodosFeedbacks(anyString(), anyInt())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/feedback/all")
                .param("keyword", "teste")
                .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Todos os feedbacks listados com sucesso"));

        verify(feedbackService).listarTodosFeedbacks("teste", 0);
    }

    @Test
    @WithMockUser(roles = "colaborador")
    void listarTodosFeedbacks_SemPermissaoAdmin_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<Page<FeedbackResponseDTO>> response = ApiResponse.success(pageFeedbacks, "Todos os feedbacks listados com sucesso");
        when(feedbackService.listarTodosFeedbacks(anyString(), anyInt())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/feedback/all")
                .param("keyword", "teste")
                .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        verify(feedbackService).listarTodosFeedbacks("teste", 0);
    }

    @Test
    void criarFeedback_ComDadosValidos_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<Void> response = ApiResponse.success(null, "Feedback criado com sucesso");
        when(feedbackService.criarFeedback(any(FeedbackDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/feedback/novo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(feedbackDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Feedback criado com sucesso"));

        verify(feedbackService).criarFeedback(any(FeedbackDTO.class));
    }

    @Test
    void criarFeedback_DadosInvalidos_DeveRetornar400() throws Exception {
        // Arrange
        feedbackDTO.setAssunto(null);
        ApiResponse<Void> response = ApiResponse.badRequest("Título é obrigatório");
        when(feedbackService.criarFeedback(any(FeedbackDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/feedback/novo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(feedbackDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Título é obrigatório"));

        verify(feedbackService).criarFeedback(any(FeedbackDTO.class));
    }

    @Test
    @WithMockUser(roles = "admin")
    void getFeedbackStats_ComPermissaoAdmin_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<FeedbackStatsResponseDTO> response = ApiResponse.success(feedbackStatsResponseDTO, "Estatísticas obtidas com sucesso");
        when(feedbackService.getFeedbackStats()).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/feedback/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Estatísticas obtidas com sucesso"))
                .andExpect(jsonPath("$.content.totalGeral").value(10))
                .andExpect(jsonPath("$.content.stats[0].tipoFeedback").value("POSITIVO"))
                .andExpect(jsonPath("$.content.stats[0].total").value(5))
                .andExpect(jsonPath("$.content.stats[1].tipoFeedback").value("CONSTRUTIVO"))
                .andExpect(jsonPath("$.content.stats[1].total").value(3));

        verify(feedbackService).getFeedbackStats();
    }

    @Test
    @WithMockUser(roles = "colaborador")
    void getFeedbackStats_SemPermissaoAdmin_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<FeedbackStatsResponseDTO> response = ApiResponse.success(feedbackStatsResponseDTO, "Estatísticas obtidas com sucesso");
        when(feedbackService.getFeedbackStats()).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/feedback/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        verify(feedbackService).getFeedbackStats();
    }

    @Test
    void listarFeedbacksRecebidos_ListaVazia_DeveRetornar200() throws Exception {
        // Arrange
        Page<FeedbackResponseDTO> pageVazia = new PageImpl<>(Arrays.asList(), PageRequest.of(0, 10), 0);
        ApiResponse<Page<FeedbackResponseDTO>> response = ApiResponse.success(pageVazia, "Feedbacks recebidos listados com sucesso");
        when(feedbackService.listarFeedbacksRecebidos(anyString(), anyString(), anyInt())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/feedback/recebidos")
                .param("keycloakId", "user-123")
                .param("keyword", "teste")
                .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.content.content").isEmpty());

        verify(feedbackService).listarFeedbacksRecebidos("user-123", "teste", 0);
    }

    @Test
    void listarFeedbacksEnviados_ListaVazia_DeveRetornar200() throws Exception {
        // Arrange
        Page<FeedbackResponseDTO> pageVazia = new PageImpl<>(Arrays.asList(), PageRequest.of(0, 10), 0);
        ApiResponse<Page<FeedbackResponseDTO>> response = ApiResponse.success(pageVazia, "Feedbacks enviados listados com sucesso");
        when(feedbackService.listarFeedbacksEnviados(anyString(), anyString(), anyInt())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/feedback/enviados")
                .param("keycloakId", "user-123")
                .param("keyword", "teste")
                .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.content.content").isEmpty());

        verify(feedbackService).listarFeedbacksEnviados("user-123", "teste", 0);
    }
} 
package br.com.pointer.pointer_back.service;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.FeedbackDTO;
import br.com.pointer.pointer_back.dto.FeedbackResponseDTO;
import br.com.pointer.pointer_back.dto.FeedbackStatsDTO;
import br.com.pointer.pointer_back.dto.FeedbackStatsResponseDTO;
import br.com.pointer.pointer_back.enums.TipoFeedback;
import br.com.pointer.pointer_back.exception.UsuarioNaoEncontradoException;
import br.com.pointer.pointer_back.model.Feedback;
import br.com.pointer.pointer_back.model.Usuario;
import br.com.pointer.pointer_back.repository.FeedbackRepository;
import br.com.pointer.pointer_back.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private FeedbackService feedbackService;

    private Usuario remetente;
    private Usuario destinatario;
    private Feedback feedback;
    private FeedbackDTO feedbackDTO;
    private FeedbackResponseDTO feedbackResponseDTO;

    @BeforeEach
    void setUp() {
        // Setup Usuario Remetente
        remetente = new Usuario();
        remetente.setId(1L);
        remetente.setNome("João Silva");
        remetente.setEmail("joao@teste.com");
        remetente.setKeycloakId("joao-keycloak-id");

        // Setup Usuario Destinatário
        destinatario = new Usuario();
        destinatario.setId(2L);
        destinatario.setNome("Maria Santos");
        destinatario.setEmail("maria@teste.com");

        // Setup Feedback
        feedback = new Feedback();
        feedback.setIdFeedback(1L);
        feedback.setIdUsuarioRemetente(1L);
        feedback.setIdUsuarioDestinatario(2L);
        feedback.setTipoFeedback(TipoFeedback.POSITIVO);
        feedback.setAssunto("Excelente trabalho!");
        feedback.setAvComunicacao(5);
        feedback.setAvProdutividade(4);
        feedback.setResolucaoDeProblemas(5);
        feedback.setTrabalhoEmEquipe(4);
        feedback.setAnonimo(false);
        feedback.setDtEnvio(LocalDateTime.now());

        // Setup DTOs
        feedbackDTO = new FeedbackDTO();
        feedbackDTO.setKeycloakIdRemetente("joao-keycloak-id");
        feedbackDTO.setIdUsuarioDestinatario(2L);
        feedbackDTO.setTipoFeedback(TipoFeedback.POSITIVO);
        feedbackDTO.setAssunto("Excelente trabalho!");
        feedbackDTO.setAvComunicacao(5);
        feedbackDTO.setAvProdutividade(4);
        feedbackDTO.setResolucaoDeProblemas(5);
        feedbackDTO.setTrabalhoEmEquipe(4);
        feedbackDTO.setAnonimo(false);

        feedbackResponseDTO = new FeedbackResponseDTO();
        feedbackResponseDTO.setIdFeedback(1L);
        feedbackResponseDTO.setTipoFeedback(TipoFeedback.POSITIVO);
        feedbackResponseDTO.setAssunto("Excelente trabalho!");
    }

    @Test
    void criarFeedback_DadosValidos_DeveCriarComSucesso() {
        // Arrange
        when(usuarioRepository.findByKeycloakId("joao-keycloak-id")).thenReturn(Optional.of(remetente));
        when(usuarioRepository.existsById(2L)).thenReturn(true);
        when(modelMapper.map(feedbackDTO, Feedback.class)).thenReturn(feedback);
        when(feedbackRepository.save(feedback)).thenReturn(feedback);

        // Act
        ApiResponse<Void> response = feedbackService.criarFeedback(feedbackDTO);

        // Assert
        assertNotNull(response);
        assertTrue(response.isOk());
        assertEquals("Feedback criado com sucesso", response.getMessage());
        
        verify(usuarioRepository).findByKeycloakId("joao-keycloak-id");
        verify(usuarioRepository).existsById(2L);
        verify(modelMapper).map(feedbackDTO, Feedback.class);
        verify(feedbackRepository).save(feedback);
    }

    @Test
    void criarFeedback_NotasInvalidas_DeveRetornarErro() {
        // Arrange
        feedbackDTO.setAvComunicacao(6); // Nota inválida

        // Act
        ApiResponse<Void> response = feedbackService.criarFeedback(feedbackDTO);

        // Assert
        assertNotNull(response);
        assertFalse(response.isOk());
        assertTrue(response.getMessage().contains("As notas devem estar entre 1 e 5"));
        
        verify(usuarioRepository, never()).findByKeycloakId(anyString());
        verify(feedbackRepository, never()).save(any());
    }

    @Test
    void criarFeedback_RemetenteNaoEncontrado_DeveRetornarErro() {
        // Arrange
        when(usuarioRepository.findByKeycloakId("joao-keycloak-id")).thenReturn(Optional.empty());

        // Act
        ApiResponse<Void> response = feedbackService.criarFeedback(feedbackDTO);

        // Assert
        assertNotNull(response);
        assertFalse(response.isOk());
        assertTrue(response.getMessage().contains("Usuário remetente não encontrado"));
        
        verify(usuarioRepository).findByKeycloakId("joao-keycloak-id");
        verify(feedbackRepository, never()).save(any());
    }

    @Test
    void criarFeedback_DestinatarioNaoEncontrado_DeveRetornarErro() {
        // Arrange
        when(usuarioRepository.findByKeycloakId("joao-keycloak-id")).thenReturn(Optional.of(remetente));
        when(usuarioRepository.existsById(2L)).thenReturn(false);

        // Act
        ApiResponse<Void> response = feedbackService.criarFeedback(feedbackDTO);

        // Assert
        assertNotNull(response);
        assertFalse(response.isOk());
        assertTrue(response.getMessage().contains("Usuário destinatário não encontrado"));
        
        verify(usuarioRepository).findByKeycloakId("joao-keycloak-id");
        verify(usuarioRepository).existsById(2L);
        verify(feedbackRepository, never()).save(any());
    }

    @Test
    void getFeedbackStats_DeveRetornarEstatisticas() {
        // Arrange
        List<FeedbackStatsDTO> stats = Arrays.asList(
            new FeedbackStatsDTO(TipoFeedback.POSITIVO, 10L),
            new FeedbackStatsDTO(TipoFeedback.CONSTRUTIVO, 5L)
        );
        
        when(feedbackRepository.findFeedbackStats()).thenReturn(stats);

        // Act
        ApiResponse<FeedbackStatsResponseDTO> response = feedbackService.getFeedbackStats();

        // Assert
        assertNotNull(response);
        assertTrue(response.isOk());
        assertEquals("Estatísticas de feedback listadas com sucesso", response.getMessage());
        
        FeedbackStatsResponseDTO content = response.getContent();
        assertNotNull(content);
        assertEquals(2, content.getStats().size());
        assertEquals(15L, content.getTotalGeral());
        
        verify(feedbackRepository).findFeedbackStats();
    }

    @Test
    void criarFeedback_NotaZero_DeveRetornarErro() {
        // Arrange
        feedbackDTO.setAvComunicacao(0);

        // Act
        ApiResponse<Void> response = feedbackService.criarFeedback(feedbackDTO);

        // Assert
        assertNotNull(response);
        assertFalse(response.isOk());
        assertTrue(response.getMessage().contains("As notas devem estar entre 1 e 5"));
    }

    @Test
    void criarFeedback_NotaNula_DeveRetornarErro() {
        // Arrange
        feedbackDTO.setAvComunicacao(null);

        // Act
        ApiResponse<Void> response = feedbackService.criarFeedback(feedbackDTO);

        // Assert
        assertNotNull(response);
        assertFalse(response.isOk());
        assertTrue(response.getMessage().contains("As notas devem estar entre 1 e 5"));
    }

    @Test
    void listarTodosFeedbacks_SemKeyword_DeveRetornarTodos() {
        // Arrange
        int page = 0;
        Page<Feedback> feedbackPage = new PageImpl<>(Arrays.asList(feedback));
        
        when(feedbackRepository.findAll(any(Pageable.class))).thenReturn(feedbackPage);
        when(modelMapper.map(feedback, FeedbackResponseDTO.class)).thenReturn(feedbackResponseDTO);
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(destinatario));

        // Act
        ApiResponse<Page<FeedbackResponseDTO>> response = feedbackService.listarTodosFeedbacks(null, page);

        // Assert
        assertNotNull(response);
        assertTrue(response.isOk());
        
        verify(feedbackRepository).findAll(any(Pageable.class));
    }
}       
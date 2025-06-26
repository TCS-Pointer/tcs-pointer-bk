package br.com.pointer.pointer_back.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import br.com.pointer.pointer_back.dto.FeedbackStatsDTO;
import br.com.pointer.pointer_back.enums.TipoFeedback;
import br.com.pointer.pointer_back.model.Feedback;
import br.com.pointer.pointer_back.model.Usuario;

@DataJpaTest
@ActiveProfiles("test")
class FeedbackRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Feedback feedback1;
    private Feedback feedback2;
    private Feedback feedback3;
    private Usuario remetente;
    private Usuario destinatario;

    @BeforeEach
    void setUp() {
        // Limpar dados anteriores
        entityManager.clear();

        // Criar usuários de teste
        remetente = new Usuario();
        remetente.setNome("João Silva");
        remetente.setEmail("joao@teste.com");
        remetente.setKeycloakId("keycloak-joao");
        remetente.setTipoUsuario("GESTOR");
        remetente.setSetor("TI");
        remetente.setCargo("Gerente");
        remetente.setStatus(br.com.pointer.pointer_back.model.StatusUsuario.ATIVO);
        remetente.setDataCriacao(LocalDateTime.now());
        entityManager.persistAndFlush(remetente);

        destinatario = new Usuario();
        destinatario.setNome("Maria Santos");
        destinatario.setEmail("maria@teste.com");
        destinatario.setKeycloakId("keycloak-maria");
        destinatario.setTipoUsuario("COLABORADOR");
        destinatario.setSetor("TI");
        destinatario.setCargo("Desenvolvedor");
        destinatario.setStatus(br.com.pointer.pointer_back.model.StatusUsuario.ATIVO);
        destinatario.setDataCriacao(LocalDateTime.now());
        entityManager.persistAndFlush(destinatario);

        // Criar feedbacks de teste
        feedback1 = new Feedback();
        feedback1.setAssunto("Feedback Positivo");
        feedback1.setPontosFortes("Excelente trabalho no projeto");
        feedback1.setPontosMelhoria("Pode melhorar a documentação");
        feedback1.setAcoesRecomendadas("Documentar melhor o código");
        feedback1.setTipoFeedback(TipoFeedback.POSITIVO);
        feedback1.setIdUsuarioRemetente(remetente.getId());
        feedback1.setIdUsuarioDestinatario(destinatario.getId());
        feedback1.setAvComunicacao(5);
        feedback1.setAvProdutividade(4);
        feedback1.setResolucaoDeProblemas(5);
        feedback1.setTrabalhoEmEquipe(4);
        feedback1.setAnonimo(false);

        feedback2 = new Feedback();
        feedback2.setAssunto("Feedback Construtivo");
        feedback2.setPontosFortes("Boa comunicação");
        feedback2.setPontosMelhoria("Precisa melhorar pontualidade");
        feedback2.setAcoesRecomendadas("Chegar no horário");
        feedback2.setTipoFeedback(TipoFeedback.CONSTRUTIVO);
        feedback2.setIdUsuarioRemetente(remetente.getId());
        feedback2.setIdUsuarioDestinatario(destinatario.getId());
        feedback2.setAvComunicacao(4);
        feedback2.setAvProdutividade(3);
        feedback2.setResolucaoDeProblemas(4);
        feedback2.setTrabalhoEmEquipe(3);
        feedback2.setAnonimo(false);

        feedback3 = new Feedback();
        feedback3.setAssunto("Feedback Geral");
        feedback3.setPontosFortes("Trabalho em equipe");
        feedback3.setPontosMelhoria("Organização");
        feedback3.setAcoesRecomendadas("Melhorar organização");
        feedback3.setTipoFeedback(TipoFeedback.POSITIVO);
        feedback3.setIdUsuarioRemetente(destinatario.getId());
        feedback3.setIdUsuarioDestinatario(remetente.getId());
        feedback3.setAvComunicacao(5);
        feedback3.setAvProdutividade(4);
        feedback3.setResolucaoDeProblemas(4);
        feedback3.setTrabalhoEmEquipe(5);
        feedback3.setAnonimo(false);

        // Persistir feedbacks
        entityManager.persistAndFlush(feedback1);
        entityManager.persistAndFlush(feedback2);
        entityManager.persistAndFlush(feedback3);
    }

    @Test
    void findByIdUsuarioDestinatario_ComUsuarioExistente_DeveRetornarFeedbacksRecebidos() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Feedback> result = feedbackRepository.findByIdUsuarioDestinatario(destinatario.getId(), pageRequest);

        // Assert
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream().allMatch(f -> f.getIdUsuarioDestinatario().equals(destinatario.getId())));
    }

    @Test
    void findByIdUsuarioDestinatario_ComUsuarioInexistente_DeveRetornarVazio() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Feedback> result = feedbackRepository.findByIdUsuarioDestinatario(999L, pageRequest);

        // Assert
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void findByIdUsuarioRemetente_ComUsuarioExistente_DeveRetornarFeedbacksEnviados() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Feedback> result = feedbackRepository.findByIdUsuarioRemetente(remetente.getId(), pageRequest);

        // Assert
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream().allMatch(f -> f.getIdUsuarioRemetente().equals(remetente.getId())));
    }

    @Test
    void findByIdUsuarioRemetente_ComUsuarioInexistente_DeveRetornarVazio() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Feedback> result = feedbackRepository.findByIdUsuarioRemetente(999L, pageRequest);

        // Assert
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void findRecebidosByKeyword_ComKeywordExistente_DeveRetornarFeedbacks() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Feedback> result = feedbackRepository.findRecebidosByKeyword(destinatario.getId(), "trabalho", pageRequest);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().get(0).getPontosFortes().toLowerCase().contains("trabalho"));
    }

    @Test
    void findRecebidosByKeyword_ComKeywordInexistente_DeveRetornarVazio() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Feedback> result = feedbackRepository.findRecebidosByKeyword(destinatario.getId(), "inexistente", pageRequest);

        // Assert
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void findRecebidosByKeyword_ComKeywordNula_DeveRetornarTodosFeedbacksRecebidos() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Feedback> result = feedbackRepository.findRecebidosByKeyword(destinatario.getId(), null, pageRequest);

        // Assert
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream().allMatch(f -> f.getIdUsuarioDestinatario().equals(destinatario.getId())));
    }

    @Test
    void findRecebidosByKeyword_ComKeywordVazia_DeveRetornarTodosFeedbacksRecebidos() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Feedback> result = feedbackRepository.findRecebidosByKeyword(destinatario.getId(), "", pageRequest);

        // Assert
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream().allMatch(f -> f.getIdUsuarioDestinatario().equals(destinatario.getId())));
    }

    @Test
    void findEnviadosByKeyword_ComKeywordExistente_DeveRetornarFeedbacks() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Feedback> result = feedbackRepository.findEnviadosByKeyword(remetente.getId(), "excelente", pageRequest);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().get(0).getPontosFortes().toLowerCase().contains("excelente"));
    }

    @Test
    void findEnviadosByKeyword_ComKeywordInexistente_DeveRetornarVazio() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Feedback> result = feedbackRepository.findEnviadosByKeyword(remetente.getId(), "inexistente", pageRequest);

        // Assert
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void findEnviadosByKeyword_ComKeywordNula_DeveRetornarTodosFeedbacksEnviados() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Feedback> result = feedbackRepository.findEnviadosByKeyword(remetente.getId(), null, pageRequest);

        // Assert
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream().allMatch(f -> f.getIdUsuarioRemetente().equals(remetente.getId())));
    }

    @Test
    void findEnviadosByKeyword_ComKeywordVazia_DeveRetornarTodosFeedbacksEnviados() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Feedback> result = feedbackRepository.findEnviadosByKeyword(remetente.getId(), "", pageRequest);

        // Assert
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream().allMatch(f -> f.getIdUsuarioRemetente().equals(remetente.getId())));
    }

    @Test
    void findAllByKeyword_ComKeywordExistente_DeveRetornarFeedbacks() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Feedback> result = feedbackRepository.findAllByKeyword("trabalho", pageRequest);

        // Assert
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream()
            .anyMatch(f -> f.getPontosFortes().toLowerCase().contains("trabalho")));
    }

    @Test
    void findAllByKeyword_ComKeywordInexistente_DeveRetornarVazio() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Feedback> result = feedbackRepository.findAllByKeyword("inexistente", pageRequest);

        // Assert
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void findAllByKeyword_ComKeywordNula_DeveRetornarTodosFeedbacks() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Feedback> result = feedbackRepository.findAllByKeyword(null, pageRequest);

        // Assert
        assertEquals(3, result.getTotalElements());
    }

    @Test
    void findAllByKeyword_ComKeywordVazia_DeveRetornarTodosFeedbacks() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Feedback> result = feedbackRepository.findAllByKeyword("", pageRequest);

        // Assert
        assertEquals(3, result.getTotalElements());
    }

    @Test
    void findFeedbackStats_DeveRetornarEstatisticas() {
        // Act
        List<FeedbackStatsDTO> result = feedbackRepository.findFeedbackStats();

        // Assert
        assertEquals(2, result.size());
        
        // Verificar se todos os tipos estão presentes
        assertTrue(result.stream().anyMatch(stats -> TipoFeedback.POSITIVO.equals(stats.getTipoFeedback())));
        assertTrue(result.stream().anyMatch(stats -> TipoFeedback.CONSTRUTIVO.equals(stats.getTipoFeedback())));
        
        // Verificar contagem correta
        FeedbackStatsDTO positivoStats = result.stream()
                .filter(stats -> TipoFeedback.POSITIVO.equals(stats.getTipoFeedback()))
                .findFirst()
                .orElse(null);
        assertNotNull(positivoStats);
        assertEquals(2L, positivoStats.getTotal());
    }

    @Test
    void save_ComFeedbackNovo_DeveSalvarFeedback() {
        // Given
        Feedback feedback = new Feedback();
        feedback.setAssunto("Teste de Feedback");
        feedback.setAnonimo(false);
        feedback.setAvComunicacao(5);
        feedback.setAvProdutividade(4);
        feedback.setResolucaoDeProblemas(3);
        feedback.setTrabalhoEmEquipe(4);
        feedback.setTipoFeedback(TipoFeedback.POSITIVO);
        feedback.setIdUsuarioDestinatario(1L);
        feedback.setIdUsuarioRemetente(2L);
        feedback.setPontosFortes("Pontos fortes");
        feedback.setPontosMelhoria("Pontos de melhoria");
        feedback.setAcoesRecomendadas("Ações recomendadas");

        // When
        Feedback savedFeedback = feedbackRepository.save(feedback);

        // Then
        assertNotNull(savedFeedback.getIdFeedback());
        assertEquals("Teste de Feedback", savedFeedback.getAssunto());
        assertFalse(savedFeedback.getAnonimo());
        assertEquals(5, savedFeedback.getAvComunicacao());
        assertEquals(4, savedFeedback.getAvProdutividade());
        assertEquals(3, savedFeedback.getResolucaoDeProblemas());
        assertEquals(4, savedFeedback.getTrabalhoEmEquipe());
        assertEquals(TipoFeedback.POSITIVO, savedFeedback.getTipoFeedback());
        assertEquals(1L, savedFeedback.getIdUsuarioDestinatario());
        assertEquals(2L, savedFeedback.getIdUsuarioRemetente());
        assertEquals("Pontos fortes", savedFeedback.getPontosFortes());
        assertEquals("Pontos de melhoria", savedFeedback.getPontosMelhoria());
        assertEquals("Ações recomendadas", savedFeedback.getAcoesRecomendadas());
        assertNotNull(savedFeedback.getDtEnvio());
    }

    @Test
    void save_ComFeedbackExistente_DeveAtualizarFeedback() {
        // Arrange
        feedback1.setAssunto("Feedback Atualizado");

        // Act
        Feedback updated = feedbackRepository.save(feedback1);

        // Assert
        assertEquals("Feedback Atualizado", updated.getAssunto());
        assertEquals(feedback1.getIdFeedback(), updated.getIdFeedback());
        
        Optional<Feedback> found = feedbackRepository.findById(feedback1.getIdFeedback());
        assertTrue(found.isPresent());
        assertEquals("Feedback Atualizado", found.get().getAssunto());
        
        // Test delete
        Long feedbackId = feedback1.getIdFeedback();
        feedbackRepository.deleteById(feedbackId);
        
        Optional<Feedback> result = feedbackRepository.findById(feedback1.getIdFeedback());
        assertFalse(result.isPresent());
        
        // Test exists
        boolean exists = feedbackRepository.existsById(feedback1.getIdFeedback());
        assertFalse(exists);
    }

    @Test
    void delete_ComFeedbackExistente_DeveRemoverFeedback() {
        // Arrange
        Long feedbackId = feedback1.getIdFeedback();

        // Act
        feedbackRepository.delete(feedback1);

        // Assert
        Optional<Feedback> found = feedbackRepository.findById(feedbackId);
        assertFalse(found.isPresent());
    }

    @Test
    void findById_ComIdExistente_DeveRetornarFeedback() {
        // Act
        Optional<Feedback> result = feedbackRepository.findById(feedback1.getIdFeedback());

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Feedback Positivo", result.get().getAssunto());
    }

    @Test
    void findById_ComIdInexistente_DeveRetornarVazio() {
        // Act
        Optional<Feedback> result = feedbackRepository.findById(999L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findAll_DeveRetornarTodosFeedbacks() {
        // Act
        List<Feedback> result = feedbackRepository.findAll();

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(f -> f.getAssunto().equals("Feedback Positivo")));
        assertTrue(result.stream().anyMatch(f -> f.getAssunto().equals("Feedback Construtivo")));
        assertTrue(result.stream().anyMatch(f -> f.getAssunto().equals("Feedback Geral")));
    }

    @Test
    void count_DeveRetornarNumeroCorretoDeFeedbacks() {
        // Act
        long count = feedbackRepository.count();

        // Assert
        assertEquals(3, count);
    }

    @Test
    void existsById_ComIdExistente_DeveRetornarTrue() {
        // Act
        boolean exists = feedbackRepository.existsById(feedback1.getIdFeedback());

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsById_ComIdInexistente_DeveRetornarFalse() {
        // Act
        boolean exists = feedbackRepository.existsById(999L);

        // Assert
        assertFalse(exists);
    }

    @Test
    void saveAll_ComListaDeFeedbacks_DeveSalvarTodos() {
        // Given
        List<Feedback> feedbacks = Arrays.asList(
            createFeedback("Feedback 1", TipoFeedback.POSITIVO),
            createFeedback("Feedback 2", TipoFeedback.CONSTRUTIVO),
            createFeedback("Feedback 3", TipoFeedback.POSITIVO)
        );

        // When
        List<Feedback> savedFeedbacks = feedbackRepository.saveAll(feedbacks);

        // Then
        assertEquals(3, savedFeedbacks.size());
        savedFeedbacks.forEach(feedback -> {
            assertNotNull(feedback.getIdFeedback());
            assertNotNull(feedback.getDtEnvio());
        });
    }

    private Feedback createFeedback(String assunto, TipoFeedback tipo) {
        Feedback feedback = new Feedback();
        feedback.setAssunto(assunto);
        feedback.setAnonimo(false);
        feedback.setAvComunicacao(5);
        feedback.setAvProdutividade(4);
        feedback.setResolucaoDeProblemas(3);
        feedback.setTrabalhoEmEquipe(4);
        feedback.setTipoFeedback(tipo);
        feedback.setIdUsuarioDestinatario(1L);
        feedback.setIdUsuarioRemetente(2L);
        return feedback;
    }

    @Test
    void deleteAll_DeveRemoverTodosFeedbacks() {
        // Act
        feedbackRepository.deleteAll();

        // Assert
        assertEquals(0, feedbackRepository.count());
        assertTrue(feedbackRepository.findAll().isEmpty());
    }

    @Test
    void deleteAllById_ComIdsExistentes_DeveRemoverFeedbacks() {
        // Arrange
        List<Long> ids = Arrays.asList(feedback1.getIdFeedback(), feedback2.getIdFeedback());

        // Act
        feedbackRepository.deleteAllById(ids);

        // Assert
        assertEquals(1, feedbackRepository.count());
        assertFalse(feedbackRepository.existsById(feedback1.getIdFeedback()));
        assertFalse(feedbackRepository.existsById(feedback2.getIdFeedback()));
        assertTrue(feedbackRepository.existsById(feedback3.getIdFeedback()));
    }

    @Test
    void findRecebidosByKeyword_ComPaginacao_DeveRetornarPaginaCorreta() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 1);

        // Act
        Page<Feedback> result = feedbackRepository.findRecebidosByKeyword(destinatario.getId(), null, pageRequest);

        // Assert
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(2, result.getTotalPages());
    }

    @Test
    void findEnviadosByKeyword_ComPaginacao_DeveRetornarPaginaCorreta() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 1);

        // Act
        Page<Feedback> result = feedbackRepository.findEnviadosByKeyword(remetente.getId(), null, pageRequest);

        // Assert
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(2, result.getTotalPages());
    }

    @Test
    void findAllByKeyword_ComPaginacao_DeveRetornarPaginaCorreta() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 2);

        // Act
        Page<Feedback> result = feedbackRepository.findAllByKeyword(null, pageRequest);

        // Assert
        assertEquals(3, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalPages());
    }
} 
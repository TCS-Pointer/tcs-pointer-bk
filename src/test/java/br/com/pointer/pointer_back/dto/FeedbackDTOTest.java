package br.com.pointer.pointer_back.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.pointer.pointer_back.enums.TipoFeedback;

class FeedbackDTOTest {

    private FeedbackDTO feedback;

    @BeforeEach
    void setUp() {
        feedback = new FeedbackDTO();
    }

    @Test
    @DisplayName("Deve criar FeedbackDTO com todos os campos")
    void deveCriarFeedbackDTOComTodosOsCampos() {
        // Arrange
        feedback.setAcoesRecomendadas("Ações recomendadas");
        feedback.setAnonimo(true);
        feedback.setAssunto("Assunto do feedback");
        feedback.setAvComunicacao(5);
        feedback.setAvProdutividade(4);
        feedback.setResolucaoDeProblemas(3);
        feedback.setTrabalhoEmEquipe(5);
        feedback.setPontosFortes("Pontos fortes");
        feedback.setPontosMelhoria("Pontos de melhoria");
        feedback.setTipoFeedback(TipoFeedback.POSITIVO);
        feedback.setIdUsuarioDestinatario(1L);
        feedback.setKeycloakIdRemetente("keycloak-id");

        // Assert
        assertEquals("Ações recomendadas", feedback.getAcoesRecomendadas());
        assertTrue(feedback.getAnonimo());
        assertEquals("Assunto do feedback", feedback.getAssunto());
        assertEquals(5, feedback.getAvComunicacao());
        assertEquals(4, feedback.getAvProdutividade());
        assertEquals(3, feedback.getResolucaoDeProblemas());
        assertEquals(5, feedback.getTrabalhoEmEquipe());
        assertEquals("Pontos fortes", feedback.getPontosFortes());
        assertEquals("Pontos de melhoria", feedback.getPontosMelhoria());
        assertEquals(TipoFeedback.POSITIVO, feedback.getTipoFeedback());
        assertEquals(1L, feedback.getIdUsuarioDestinatario());
        assertEquals("keycloak-id", feedback.getKeycloakIdRemetente());
    }

    @Test
    @DisplayName("Deve criar FeedbackDTO com valores padrão")
    void deveCriarFeedbackDTOComValoresPadrao() {
        // Assert
        assertNull(feedback.getAcoesRecomendadas());
        assertFalse(feedback.getAnonimo());
        assertNull(feedback.getAssunto());
        assertNull(feedback.getAvComunicacao());
        assertNull(feedback.getAvProdutividade());
        assertNull(feedback.getResolucaoDeProblemas());
        assertNull(feedback.getTrabalhoEmEquipe());
        assertNull(feedback.getPontosFortes());
        assertNull(feedback.getPontosMelhoria());
        assertNull(feedback.getTipoFeedback());
        assertNull(feedback.getIdUsuarioDestinatario());
        assertNull(feedback.getKeycloakIdRemetente());
    }

    @Test
    @DisplayName("Deve atualizar campos do FeedbackDTO")
    void deveAtualizarCamposDoFeedbackDTO() {
        // Arrange
        feedback.setAssunto("Assunto original");
        feedback.setPontosFortes("Pontos originais");

        // Act
        feedback.setAssunto("Assunto atualizado");
        feedback.setPontosFortes("Pontos atualizados");

        // Assert
        assertEquals("Assunto atualizado", feedback.getAssunto());
        assertEquals("Pontos atualizados", feedback.getPontosFortes());
    }

    @Test
    @DisplayName("Deve definir diferentes tipos de feedback")
    void deveDefinirDiferentesTiposDeFeedback() {
        // Act & Assert
        feedback.setTipoFeedback(TipoFeedback.POSITIVO);
        assertEquals(TipoFeedback.POSITIVO, feedback.getTipoFeedback());

        feedback.setTipoFeedback(TipoFeedback.CONSTRUTIVO);
        assertEquals(TipoFeedback.CONSTRUTIVO, feedback.getTipoFeedback());
    }

    @Test
    @DisplayName("Deve definir avaliações")
    void deveDefinirAvaliacoes() {
        // Act
        feedback.setAvComunicacao(5);
        feedback.setAvProdutividade(4);
        feedback.setResolucaoDeProblemas(3);
        feedback.setTrabalhoEmEquipe(2);

        // Assert
        assertEquals(5, feedback.getAvComunicacao());
        assertEquals(4, feedback.getAvProdutividade());
        assertEquals(3, feedback.getResolucaoDeProblemas());
        assertEquals(2, feedback.getTrabalhoEmEquipe());
    }

    @Test
    @DisplayName("Deve definir pontos fortes e de melhoria")
    void deveDefinirPontosFortesEMelhoria() {
        // Act
        feedback.setPontosFortes("Pontos fortes do usuário");
        feedback.setPontosMelhoria("Pontos de melhoria");

        // Assert
        assertEquals("Pontos fortes do usuário", feedback.getPontosFortes());
        assertEquals("Pontos de melhoria", feedback.getPontosMelhoria());
    }

    @Test
    @DisplayName("Deve definir ações recomendadas")
    void deveDefinirAcoesRecomendadas() {
        // Act
        feedback.setAcoesRecomendadas("Ações recomendadas para melhoria");

        // Assert
        assertEquals("Ações recomendadas para melhoria", feedback.getAcoesRecomendadas());
    }

    @Test
    @DisplayName("Deve definir anônimo como null")
    void deveDefinirAnonimoComoNull() {
        // Act
        feedback.setAnonimo(null);

        // Assert
        assertNull(feedback.getAnonimo());
    }

    @Test
    @DisplayName("Deve definir avaliações como null")
    void deveDefinirAvaliacoesComoNull() {
        // Act
        feedback.setAvComunicacao(null);
        feedback.setAvProdutividade(null);
        feedback.setResolucaoDeProblemas(null);
        feedback.setTrabalhoEmEquipe(null);

        // Assert
        assertNull(feedback.getAvComunicacao());
        assertNull(feedback.getAvProdutividade());
        assertNull(feedback.getResolucaoDeProblemas());
        assertNull(feedback.getTrabalhoEmEquipe());
    }

    @Test
    @DisplayName("Deve testar equals e hashCode com objetos iguais")
    void deveTestarEqualsEHashCodeComObjetosIguais() {
        // Arrange
        FeedbackDTO feedback1 = new FeedbackDTO();
        feedback1.setAssunto("Teste");
        feedback1.setTipoFeedback(TipoFeedback.POSITIVO);

        FeedbackDTO feedback2 = new FeedbackDTO();
        feedback2.setAssunto("Teste");
        feedback2.setTipoFeedback(TipoFeedback.POSITIVO);

        FeedbackDTO feedback3 = new FeedbackDTO();
        feedback3.setAssunto("Teste");
        feedback3.setTipoFeedback(TipoFeedback.POSITIVO);

        // Assert
        assertEquals(feedback1, feedback2);
        assertEquals(feedback2, feedback3);
        assertEquals(feedback1, feedback3);
        assertEquals(feedback1.hashCode(), feedback2.hashCode());
        assertEquals(feedback2.hashCode(), feedback3.hashCode());
    }

    @Test
    @DisplayName("Deve testar equals e hashCode com objetos diferentes")
    void deveTestarEqualsEHashCodeComObjetosDiferentes() {
        // Arrange
        FeedbackDTO feedback1 = new FeedbackDTO();
        feedback1.setAssunto("Teste 1");
        feedback1.setTipoFeedback(TipoFeedback.POSITIVO);

        FeedbackDTO feedback2 = new FeedbackDTO();
        feedback2.setAssunto("Teste 2");
        feedback2.setTipoFeedback(TipoFeedback.POSITIVO);

        FeedbackDTO feedback3 = new FeedbackDTO();
        feedback3.setAssunto("Teste 1");
        feedback3.setTipoFeedback(TipoFeedback.CONSTRUTIVO);

        // Assert
        assertNotEquals(feedback1, feedback2);
        assertNotEquals(feedback1, feedback3);
        assertNotEquals(feedback2, feedback3);
        assertNotEquals(feedback1.hashCode(), feedback2.hashCode());
        assertNotEquals(feedback1.hashCode(), feedback3.hashCode());
        assertNotEquals(feedback2.hashCode(), feedback3.hashCode());
    }

    @Test
    @DisplayName("Deve testar toString")
    void deveTestarToString() {
        // Arrange
        feedback.setAssunto("Teste");
        feedback.setPontosFortes("Pontos fortes");

        // Act
        String result = feedback.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("Teste"));
        assertTrue(result.contains("Pontos fortes"));
    }
} 
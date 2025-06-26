package br.com.pointer.pointer_back.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.pointer.pointer_back.enums.TipoFeedback;

class FeedbackTest {

    private Feedback feedback;

    @BeforeEach
    void setUp() {
        feedback = new Feedback();
    }

    @Test
    void testSetAndGetIdFeedback() {
        // Arrange
        Long idFeedback = 1L;

        // Act
        feedback.setIdFeedback(idFeedback);

        // Assert
        assertEquals(idFeedback, feedback.getIdFeedback());
    }

    @Test
    void testSetAndGetAcoesRecomendadas() {
        // Arrange
        String acoesRecomendadas = "Melhorar comunicação e liderança";

        // Act
        feedback.setAcoesRecomendadas(acoesRecomendadas);

        // Assert
        assertEquals(acoesRecomendadas, feedback.getAcoesRecomendadas());
    }

    @Test
    void testSetAndGetAnonimo() {
        // Arrange
        Boolean anonimo = true;

        // Act
        feedback.setAnonimo(anonimo);

        // Assert
        assertEquals(anonimo, feedback.getAnonimo());
    }

    @Test
    void testSetAndGetAssunto() {
        // Arrange
        String assunto = "Avaliação de Desempenho";

        // Act
        feedback.setAssunto(assunto);

        // Assert
        assertEquals(assunto, feedback.getAssunto());
    }

    @Test
    void testSetAndGetAvComunicacao() {
        // Arrange
        Integer avComunicacao = 8;

        // Act
        feedback.setAvComunicacao(avComunicacao);

        // Assert
        assertEquals(avComunicacao, feedback.getAvComunicacao());
    }

    @Test
    void testSetAndGetAvProdutividade() {
        // Arrange
        Integer avProdutividade = 9;

        // Act
        feedback.setAvProdutividade(avProdutividade);

        // Assert
        assertEquals(avProdutividade, feedback.getAvProdutividade());
    }

    @Test
    void testSetAndGetResolucaoDeProblemas() {
        // Arrange
        Integer resolucaoDeProblemas = 7;

        // Act
        feedback.setResolucaoDeProblemas(resolucaoDeProblemas);

        // Assert
        assertEquals(resolucaoDeProblemas, feedback.getResolucaoDeProblemas());
    }

    @Test
    void testSetAndGetTrabalhoEmEquipe() {
        // Arrange
        Integer trabalhoEmEquipe = 8;

        // Act
        feedback.setTrabalhoEmEquipe(trabalhoEmEquipe);

        // Assert
        assertEquals(trabalhoEmEquipe, feedback.getTrabalhoEmEquipe());
    }

    @Test
    void testSetAndGetDtEnvio() {
        // Arrange
        LocalDateTime dtEnvio = LocalDateTime.now();

        // Act
        feedback.setDtEnvio(dtEnvio);

        // Assert
        assertEquals(dtEnvio, feedback.getDtEnvio());
    }

    @Test
    void testSetAndGetPontosFortes() {
        // Arrange
        String pontosFortes = "Liderança e comunicação";

        // Act
        feedback.setPontosFortes(pontosFortes);

        // Assert
        assertEquals(pontosFortes, feedback.getPontosFortes());
    }

    @Test
    void testSetAndGetPontosMelhoria() {
        // Arrange
        String pontosMelhoria = "Gestão de tempo";

        // Act
        feedback.setPontosMelhoria(pontosMelhoria);

        // Assert
        assertEquals(pontosMelhoria, feedback.getPontosMelhoria());
    }

    @Test
    void testSetAndGetTipoFeedback() {
        // Arrange
        TipoFeedback tipoFeedback = TipoFeedback.POSITIVO;

        // Act
        feedback.setTipoFeedback(tipoFeedback);

        // Assert
        assertEquals(tipoFeedback, feedback.getTipoFeedback());
    }

    @Test
    void testSetAndGetIdUsuarioDestinatario() {
        // Arrange
        Long idUsuarioDestinatario = 1L;

        // Act
        feedback.setIdUsuarioDestinatario(idUsuarioDestinatario);

        // Assert
        assertEquals(idUsuarioDestinatario, feedback.getIdUsuarioDestinatario());
    }

    @Test
    void testSetAndGetIdUsuarioRemetente() {
        // Arrange
        Long idUsuarioRemetente = 2L;

        // Act
        feedback.setIdUsuarioRemetente(idUsuarioRemetente);

        // Assert
        assertEquals(idUsuarioRemetente, feedback.getIdUsuarioRemetente());
    }

    @Test
    void testDefaultValues() {
        // Assert
        assertFalse(feedback.getAnonimo());
        assertNull(feedback.getDtEnvio());
    }

    @Test
    void testEquals_ComMesmoId_DeveRetornarTrue() {
        // Arrange
        Feedback feedback1 = new Feedback();
        feedback1.setIdFeedback(1L);
        feedback1.setAssunto("Feedback 1");

        Feedback feedback2 = new Feedback();
        feedback2.setIdFeedback(1L);
        feedback2.setAssunto("Feedback 1");

        // Act & Assert
        assertEquals(feedback1, feedback2);
    }

    @Test
    void testEquals_ComIdsDiferentes_DeveRetornarFalse() {
        // Arrange
        Feedback feedback1 = new Feedback();
        feedback1.setIdFeedback(1L);

        Feedback feedback2 = new Feedback();
        feedback2.setIdFeedback(2L);

        // Act & Assert
        assertNotEquals(feedback1, feedback2);
    }

    @Test
    void testEquals_ComNull_DeveRetornarFalse() {
        // Arrange
        Feedback feedback1 = new Feedback();
        feedback1.setIdFeedback(1L);

        // Act & Assert
        assertNotEquals(null, feedback1);
    }

    @Test
    void testEquals_ComMesmoObjeto_DeveRetornarTrue() {
        // Arrange
        Feedback feedback1 = new Feedback();
        feedback1.setIdFeedback(1L);

        // Act & Assert
        assertEquals(feedback1, feedback1);
    }

    @Test
    void testEquals_ComTipoDiferente_DeveRetornarFalse() {
        // Arrange
        Feedback feedback1 = new Feedback();
        feedback1.setIdFeedback(1L);

        String string = "teste";

        // Act & Assert
        assertNotEquals(feedback1, string);
    }

    @Test
    void testHashCode_ComMesmoId_DeveRetornarMesmoHashCode() {
        // Arrange
        Feedback feedback1 = new Feedback();
        feedback1.setIdFeedback(1L);

        Feedback feedback2 = new Feedback();
        feedback2.setIdFeedback(1L);

        // Act & Assert
        assertEquals(feedback1.hashCode(), feedback2.hashCode());
    }

    @Test
    void testHashCode_ComIdsDiferentes_DeveRetornarHashCodesDiferentes() {
        // Arrange
        Feedback feedback1 = new Feedback();
        feedback1.setIdFeedback(1L);

        Feedback feedback2 = new Feedback();
        feedback2.setIdFeedback(2L);

        // Act & Assert
        assertNotEquals(feedback1.hashCode(), feedback2.hashCode());
    }

    @Test
    void testToString_DeveConterInformacoesPrincipais() {
        // Arrange
        feedback.setIdFeedback(1L);
        feedback.setAssunto("Feedback Teste");
        feedback.setTipoFeedback(TipoFeedback.POSITIVO);
        feedback.setAvComunicacao(8);

        // Act
        String toString = feedback.toString();

        // Assert
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("Feedback Teste"));
        assertTrue(toString.contains("POSITIVO"));
        assertTrue(toString.contains("8"));
    }

    @Test
    void testToString_ComValoresNulos_DeveFuncionar() {
        // Act
        String toString = feedback.toString();

        // Assert
        assertNotNull(toString);
        assertFalse(toString.isEmpty());
    }

    @Test
    void testTipoFeedbackEnum_DeveAceitarTodosOsValores() {
        // Arrange & Act & Assert
        feedback.setTipoFeedback(TipoFeedback.POSITIVO);
        assertEquals(TipoFeedback.POSITIVO, feedback.getTipoFeedback());

        feedback.setTipoFeedback(TipoFeedback.CONSTRUTIVO);
        assertEquals(TipoFeedback.CONSTRUTIVO, feedback.getTipoFeedback());

        feedback.setTipoFeedback(TipoFeedback.CONSTRUTIVO);
        assertEquals(TipoFeedback.CONSTRUTIVO, feedback.getTipoFeedback());
    }

    @Test
    void testAvaliacoes_DeveAceitarValoresDe1A10() {
        // Arrange & Act & Assert
        feedback.setAvComunicacao(1);
        assertEquals(1, feedback.getAvComunicacao());

        feedback.setAvComunicacao(5);
        assertEquals(5, feedback.getAvComunicacao());

        feedback.setAvComunicacao(10);
        assertEquals(10, feedback.getAvComunicacao());
    }

    @Test
    void testAvaliacoes_DeveAceitarValoresNulos() {
        // Arrange & Act & Assert
        feedback.setAvComunicacao(null);
        assertNull(feedback.getAvComunicacao());

        feedback.setAvProdutividade(null);
        assertNull(feedback.getAvProdutividade());

        feedback.setResolucaoDeProblemas(null);
        assertNull(feedback.getResolucaoDeProblemas());

        feedback.setTrabalhoEmEquipe(null);
        assertNull(feedback.getTrabalhoEmEquipe());
    }

    @Test
    void testDataEnvio_DeveSerAutomaticaNoPrePersist() {
        // Arrange
        Feedback feedbackComPrePersist = new Feedback();

        // Act
        feedbackComPrePersist.onCreate();

        // Assert
        assertNotNull(feedbackComPrePersist.getDtEnvio());
        assertTrue(feedbackComPrePersist.getDtEnvio().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(feedbackComPrePersist.getDtEnvio().isAfter(LocalDateTime.now().minusSeconds(1)));
    }

    @Test
    void testDataEnvio_DeveManterDataQuandoJaDefinida() {
        // Arrange
        LocalDateTime dataEspecifica = LocalDateTime.of(2023, 1, 1, 12, 0, 0);
        feedback.setDtEnvio(dataEspecifica);

        // Act
        feedback.onCreate();

        // Assert
        int currentYear = LocalDateTime.now().getYear();
        assertEquals(currentYear, feedback.getDtEnvio().getYear());
    }

    @Test
    void testAnonimo_DeveAceitarValoresBooleanos() {
        // Arrange & Act & Assert
        feedback.setAnonimo(true);
        assertTrue(feedback.getAnonimo());

        feedback.setAnonimo(false);
        assertFalse(feedback.getAnonimo());

        feedback.setAnonimo(null);
        assertNull(feedback.getAnonimo());
    }

    @Test
    void testAssunto_DeveAceitarDiferentesAssuntos() {
        // Arrange & Act & Assert
        feedback.setAssunto("Avaliação de Desempenho");
        assertEquals("Avaliação de Desempenho", feedback.getAssunto());

        feedback.setAssunto("Feedback de Projeto");
        assertEquals("Feedback de Projeto", feedback.getAssunto());

        feedback.setAssunto("Revisão de Metas");
        assertEquals("Revisão de Metas", feedback.getAssunto());

        feedback.setAssunto("Acompanhamento");
        assertEquals("Acompanhamento", feedback.getAssunto());
    }

    @Test
    void testAcoesRecomendadas_DeveAceitarTextoLongo() {
        // Arrange
        String acoesLongas = "Melhorar a comunicação com a equipe, implementar reuniões semanais, " +
                "estabelecer metas claras e acompanhar o progresso regularmente.";

        // Act
        feedback.setAcoesRecomendadas(acoesLongas);

        // Assert
        assertEquals(acoesLongas, feedback.getAcoesRecomendadas());
    }

    @Test
    void testAcoesRecomendadas_DeveAceitarTextoVazio() {
        // Arrange
        String acoesVazias = "";

        // Act
        feedback.setAcoesRecomendadas(acoesVazias);

        // Assert
        assertEquals(acoesVazias, feedback.getAcoesRecomendadas());
    }

    @Test
    void testPontosFortes_DeveAceitarTextoLimitado() {
        // Arrange
        String pontosFortes = "Liderança, comunicação e trabalho em equipe";

        // Act
        feedback.setPontosFortes(pontosFortes);

        // Assert
        assertEquals(pontosFortes, feedback.getPontosFortes());
    }

    @Test
    void testPontosMelhoria_DeveAceitarTextoLimitado() {
        // Arrange
        String pontosMelhoria = "Gestão de tempo e priorização";

        // Act
        feedback.setPontosMelhoria(pontosMelhoria);

        // Assert
        assertEquals(pontosMelhoria, feedback.getPontosMelhoria());
    }

    @Test
    void testEquals_ComIdNulo_DeveRetornarFalse() {
        // Arrange
        Feedback feedback1 = new Feedback();
        feedback1.setIdFeedback(null);

        Feedback feedback2 = new Feedback();
        feedback2.setIdFeedback(1L);

        // Act & Assert
        assertNotEquals(feedback1, feedback2);
    }

    @Test
    void testEquals_ComAmbosIdsNulos_DeveRetornarTrue() {
        // Arrange
        Feedback feedback1 = new Feedback();
        feedback1.setIdFeedback(null);

        Feedback feedback2 = new Feedback();
        feedback2.setIdFeedback(null);

        // Act & Assert
        assertEquals(feedback1, feedback2);
    }

    @Test
    void testHashCode_ComIdNulo_DeveRetornarHashCodeConsistente() {
        // Arrange
        Feedback feedback1 = new Feedback();
        feedback1.setIdFeedback(null);

        Feedback feedback2 = new Feedback();
        feedback2.setIdFeedback(null);

        // Act & Assert
        assertEquals(feedback1.hashCode(), feedback2.hashCode());
    }

    @Test
    void testToString_ComTodosOsCamposPreenchidos_DeveConterTodasInformacoes() {
        // Arrange
        LocalDateTime dtEnvio = LocalDateTime.of(2023, 12, 1, 10, 0, 0);

        feedback.setIdFeedback(1L);
        feedback.setAcoesRecomendadas("Melhorar comunicação");
        feedback.setAnonimo(false);
        feedback.setAssunto("Avaliação Completa");
        feedback.setAvComunicacao(8);
        feedback.setAvProdutividade(9);
        feedback.setResolucaoDeProblemas(7);
        feedback.setTrabalhoEmEquipe(8);
        feedback.setDtEnvio(dtEnvio);
        feedback.setPontosFortes("Liderança");
        feedback.setPontosMelhoria("Gestão de tempo");
        feedback.setTipoFeedback(TipoFeedback.POSITIVO);
        feedback.setIdUsuarioDestinatario(1L);
        feedback.setIdUsuarioRemetente(2L);

        // Act
        String toString = feedback.toString();

        // Assert
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("Melhorar comunicação"));
        assertTrue(toString.contains("false"));
        assertTrue(toString.contains("Avaliação Completa"));
        assertTrue(toString.contains("8"));
        assertTrue(toString.contains("9"));
        assertTrue(toString.contains("7"));
        assertTrue(toString.contains("Liderança"));
        assertTrue(toString.contains("Gestão de tempo"));
        assertTrue(toString.contains("POSITIVO"));
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("2"));
    }

    @Test
    void testAssunto_DeveAceitarAssuntosComCaracteresEspeciais() {
        // Arrange
        String assuntoComEspeciais = "Avaliação com caracteres: áéíóú çãõ";

        // Act
        feedback.setAssunto(assuntoComEspeciais);

        // Assert
        assertEquals(assuntoComEspeciais, feedback.getAssunto());
    }

    @Test
    void testAcoesRecomendadas_DeveAceitarTextoComQuebrasDeLinha() {
        // Arrange
        String acoesComQuebras = "Primeira ação\nSegunda ação\nTerceira ação";

        // Act
        feedback.setAcoesRecomendadas(acoesComQuebras);

        // Assert
        assertEquals(acoesComQuebras, feedback.getAcoesRecomendadas());
    }

    @Test
    void testAvaliacoes_DeveAceitarValoresExtremos() {
        // Arrange & Act & Assert
        feedback.setAvComunicacao(0);
        assertEquals(0, feedback.getAvComunicacao());

        feedback.setAvComunicacao(11);
        assertEquals(11, feedback.getAvComunicacao());

        feedback.setAvComunicacao(-1);
        assertEquals(-1, feedback.getAvComunicacao());
    }

    @Test
    void testIdUsuarioRemetente_DeveAceitarValorNulo() {
        // Act
        feedback.setIdUsuarioRemetente(null);

        // Assert
        assertNull(feedback.getIdUsuarioRemetente());
    }

    @Test
    void testIdUsuarioDestinatario_DeveSerObrigatorio() {
        // Arrange
        Long idDestinatario = 1L;

        // Act
        feedback.setIdUsuarioDestinatario(idDestinatario);

        // Assert
        assertEquals(idDestinatario, feedback.getIdUsuarioDestinatario());
    }

    @Test
    void testFeedbackAnonimo_DeveOcultarRemetente() {
        // Arrange
        feedback.setAnonimo(true);
        feedback.setIdUsuarioRemetente(1L);

        // Act & Assert
        assertTrue(feedback.getAnonimo());
        assertEquals(1L, feedback.getIdUsuarioRemetente());
    }

    @Test
    void testFeedbackNaoAnonimo_DeveMostrarRemetente() {
        // Arrange
        feedback.setAnonimo(false);
        feedback.setIdUsuarioRemetente(1L);

        // Act & Assert
        assertFalse(feedback.getAnonimo());
        assertEquals(1L, feedback.getIdUsuarioRemetente());
    }
} 
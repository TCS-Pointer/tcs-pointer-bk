package br.com.pointer.pointer_back.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.pointer.pointer_back.enums.StatusMarcoPDI;

class MarcoPDITest {

    private MarcoPDI marcoPDI;
    private PDI pdi;

    @BeforeEach
    void setUp() {
        marcoPDI = new MarcoPDI();
        pdi = new PDI();
        pdi.setId(1L);
        pdi.setTitulo("PDI Teste");
    }

    @Test
    void testSetAndGetId() {
        // Arrange
        Long id = 1L;

        // Act
        marcoPDI.setId(id);

        // Assert
        assertEquals(id, marcoPDI.getId());
    }

    @Test
    void testSetAndGetTitulo() {
        // Arrange
        String titulo = "Marco de Desenvolvimento";

        // Act
        marcoPDI.setTitulo(titulo);

        // Assert
        assertEquals(titulo, marcoPDI.getTitulo());
    }

    @Test
    void testSetAndGetDescricao() {
        // Arrange
        String descricao = "Descrição detalhada do marco de desenvolvimento";

        // Act
        marcoPDI.setDescricao(descricao);

        // Assert
        assertEquals(descricao, marcoPDI.getDescricao());
    }

    @Test
    void testSetAndGetStatus() {
        // Arrange
        StatusMarcoPDI status = StatusMarcoPDI.PENDENTE;

        // Act
        marcoPDI.setStatus(status);

        // Assert
        assertEquals(status, marcoPDI.getStatus());
    }

    @Test
    void testSetAndGetDtFinal() {
        // Arrange
        LocalDate dtFinal = LocalDate.of(2024, 6, 30);

        // Act
        marcoPDI.setDtFinal(dtFinal);

        // Assert
        assertEquals(dtFinal, marcoPDI.getDtFinal());
    }

    @Test
    void testSetAndGetPdi() {
        // Act
        marcoPDI.setPdi(pdi);

        // Assert
        assertEquals(pdi, marcoPDI.getPdi());
    }

    @Test
    void testDefaultValues() {
        // Assert
        assertNull(marcoPDI.getStatus());
    }

    @Test
    void testEquals_ComMesmoId_DeveRetornarTrue() {
        // Arrange
        MarcoPDI marco1 = new MarcoPDI();
        marco1.setId(1L);
        marco1.setTitulo("Marco 1");
        marco1.setDescricao("Descrição do marco");
        marco1.setStatus(StatusMarcoPDI.PENDENTE);
        marco1.setDtFinal(LocalDate.of(2024, 12, 31));

        MarcoPDI marco2 = new MarcoPDI();
        marco2.setId(1L);
        marco2.setTitulo("Marco 1");
        marco2.setDescricao("Descrição do marco");
        marco2.setStatus(StatusMarcoPDI.PENDENTE);
        marco2.setDtFinal(LocalDate.of(2024, 12, 31));

        // Act & Assert
        assertEquals(marco1, marco2);
    }

    @Test
    void testEquals_ComIdsDiferentes_DeveRetornarFalse() {
        // Arrange
        MarcoPDI marco1 = new MarcoPDI();
        marco1.setId(1L);
        marco1.setTitulo("Marco Teste");
        marco1.setDescricao("Descrição do marco");
        marco1.setStatus(StatusMarcoPDI.PENDENTE);
        marco1.setDtFinal(LocalDate.of(2024, 12, 31));

        MarcoPDI marco2 = new MarcoPDI();
        marco2.setId(2L);
        marco2.setTitulo("Marco Teste");
        marco2.setDescricao("Descrição do marco");
        marco2.setStatus(StatusMarcoPDI.PENDENTE);
        marco2.setDtFinal(LocalDate.of(2024, 12, 31));

        // Act & Assert
        assertNotEquals(marco1, marco2);
    }

    @Test
    void testEquals_ComNull_DeveRetornarFalse() {
        // Arrange
        MarcoPDI marco1 = new MarcoPDI();
        marco1.setId(1L);

        // Act & Assert
        assertNotEquals(null, marco1);
    }

    @Test
    void testEquals_ComMesmoObjeto_DeveRetornarTrue() {
        // Arrange
        MarcoPDI marco1 = new MarcoPDI();
        marco1.setId(1L);

        // Act & Assert
        assertEquals(marco1, marco1);
    }

    @Test
    void testEquals_ComTipoDiferente_DeveRetornarFalse() {
        // Arrange
        MarcoPDI marco1 = new MarcoPDI();
        marco1.setId(1L);

        String string = "teste";

        // Act & Assert
        assertNotEquals(marco1, string);
    }

    @Test
    void testHashCode_ComMesmoId_DeveRetornarMesmoHashCode() {
        // Arrange
        MarcoPDI marco1 = new MarcoPDI();
        marco1.setId(1L);
        marco1.setTitulo("Marco Teste");
        marco1.setDescricao("Descrição do marco");
        marco1.setStatus(StatusMarcoPDI.PENDENTE);
        marco1.setDtFinal(LocalDate.of(2024, 12, 31));

        MarcoPDI marco2 = new MarcoPDI();
        marco2.setId(1L);
        marco2.setTitulo("Marco Teste");
        marco2.setDescricao("Descrição do marco");
        marco2.setStatus(StatusMarcoPDI.PENDENTE);
        marco2.setDtFinal(LocalDate.of(2024, 12, 31));

        // Act & Assert
        assertEquals(marco1.hashCode(), marco2.hashCode());
    }

    @Test
    void testHashCode_ComIdsDiferentes_DeveRetornarHashCodesDiferentes() {
        // Arrange
        MarcoPDI marco1 = new MarcoPDI();
        marco1.setId(1L);
        marco1.setTitulo("Marco Teste");
        marco1.setDescricao("Descrição do marco");
        marco1.setStatus(StatusMarcoPDI.PENDENTE);
        marco1.setDtFinal(LocalDate.of(2024, 12, 31));

        MarcoPDI marco2 = new MarcoPDI();
        marco2.setId(2L);
        marco2.setTitulo("Marco Teste");
        marco2.setDescricao("Descrição do marco");
        marco2.setStatus(StatusMarcoPDI.PENDENTE);
        marco2.setDtFinal(LocalDate.of(2024, 12, 31));

        // Act & Assert
        assertNotEquals(marco1.hashCode(), marco2.hashCode());
    }

    @Test
    void testToString_DeveConterInformacoesPrincipais() {
        // Arrange
        marcoPDI.setId(1L);
        marcoPDI.setTitulo("Marco Teste");
        marcoPDI.setDescricao("Descrição do marco");
        marcoPDI.setStatus(StatusMarcoPDI.PENDENTE);

        // Act
        String toString = marcoPDI.toString();

        // Assert
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("Marco Teste"));
        assertTrue(toString.contains("Descrição do marco"));
        assertTrue(toString.contains("PENDENTE"));
    }

    @Test
    void testToString_ComValoresNulos_DeveFuncionar() {
        // Act
        String toString = marcoPDI.toString();

        // Assert
        assertNotNull(toString);
        assertFalse(toString.isEmpty());
    }

    @Test
    void testStatusEnum_DeveAceitarTodosOsValores() {
        // Arrange & Act & Assert
        marcoPDI.setStatus(StatusMarcoPDI.PENDENTE);
        assertEquals(StatusMarcoPDI.PENDENTE, marcoPDI.getStatus());

        marcoPDI.setStatus(StatusMarcoPDI.CONCLUIDO);
        assertEquals(StatusMarcoPDI.CONCLUIDO, marcoPDI.getStatus());
    }

    @Test
    void testDataFinal_DeveAceitarDiferentesDatas() {
        // Arrange & Act & Assert
        LocalDate dataFinal1 = LocalDate.of(2024, 3, 31);
        marcoPDI.setDtFinal(dataFinal1);
        assertEquals(dataFinal1, marcoPDI.getDtFinal());

        LocalDate dataFinal2 = LocalDate.of(2024, 6, 30);
        marcoPDI.setDtFinal(dataFinal2);
        assertEquals(dataFinal2, marcoPDI.getDtFinal());

        LocalDate dataFinal3 = LocalDate.of(2024, 12, 31);
        marcoPDI.setDtFinal(dataFinal3);
        assertEquals(dataFinal3, marcoPDI.getDtFinal());
    }

    @Test
    void testStatus_DeveSerPendentePorPadraoNoPrePersist() {
        // Arrange
        MarcoPDI marcoNovo = new MarcoPDI();

        // Act
        marcoNovo.onCreate();

        // Assert
        assertEquals(StatusMarcoPDI.PENDENTE, marcoNovo.getStatus());
    }

    @Test
    void testStatus_DeveManterStatusQuandoJaDefinido() {
        // Arrange
        marcoPDI.setStatus(StatusMarcoPDI.CONCLUIDO);

        // Act
        marcoPDI.onCreate();

        // Assert
        assertEquals(StatusMarcoPDI.CONCLUIDO, marcoPDI.getStatus());
    }

    @Test
    void testTitulo_DeveAceitarDiferentesTitulos() {
        // Arrange & Act & Assert
        marcoPDI.setTitulo("Marco de Aprendizado");
        assertEquals("Marco de Aprendizado", marcoPDI.getTitulo());

        marcoPDI.setTitulo("Objetivo Específico");
        assertEquals("Objetivo Específico", marcoPDI.getTitulo());

        marcoPDI.setTitulo("Meta Mensurável");
        assertEquals("Meta Mensurável", marcoPDI.getTitulo());

        marcoPDI.setTitulo("Resultado Esperado");
        assertEquals("Resultado Esperado", marcoPDI.getTitulo());
    }

    @Test
    void testDescricao_DeveAceitarDescricoesLongas() {
        // Arrange
        String descricaoLonga = "Esta é uma descrição muito detalhada do marco que inclui " +
                "objetivos específicos, critérios de sucesso, recursos necessários, " +
                "prazos definidos e indicadores de progresso para acompanhar o desenvolvimento.";

        // Act
        marcoPDI.setDescricao(descricaoLonga);

        // Assert
        assertEquals(descricaoLonga, marcoPDI.getDescricao());
    }

    @Test
    void testDescricao_DeveAceitarDescricoesVazias() {
        // Arrange
        String descricaoVazia = "";

        // Act
        marcoPDI.setDescricao(descricaoVazia);

        // Assert
        assertEquals(descricaoVazia, marcoPDI.getDescricao());
    }

    @Test
    void testEquals_ComIdNulo_DeveRetornarFalse() {
        // Arrange
        MarcoPDI marco1 = new MarcoPDI();
        marco1.setId(null);

        MarcoPDI marco2 = new MarcoPDI();
        marco2.setId(1L);

        // Act & Assert
        assertNotEquals(marco1, marco2);
    }

    @Test
    void testEquals_ComAmbosIdsNulos_DeveRetornarTrue() {
        // Arrange
        MarcoPDI marco1 = new MarcoPDI();
        marco1.setId(null);

        MarcoPDI marco2 = new MarcoPDI();
        marco2.setId(null);

        // Act & Assert
        assertEquals(marco1, marco2);
    }

    @Test
    void testHashCode_ComIdNulo_DeveRetornarHashCodeConsistente() {
        // Arrange
        MarcoPDI marco1 = new MarcoPDI();
        marco1.setId(null);

        MarcoPDI marco2 = new MarcoPDI();
        marco2.setId(null);

        // Act & Assert
        assertEquals(marco1.hashCode(), marco2.hashCode());
    }

    @Test
    void testToString_ComTodosOsCamposPreenchidos_DeveConterTodasInformacoes() {
        // Arrange
        LocalDate dtFinal = LocalDate.of(2024, 6, 30);

        marcoPDI.setId(1L);
        marcoPDI.setTitulo("Marco Completo");
        marcoPDI.setDescricao("Descrição completa do marco");
        marcoPDI.setStatus(StatusMarcoPDI.CONCLUIDO);
        marcoPDI.setDtFinal(dtFinal);
        marcoPDI.setPdi(pdi);

        // Act
        String toString = marcoPDI.toString();

        // Assert
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("Marco Completo"));
        assertTrue(toString.contains("Descrição completa do marco"));
        assertTrue(toString.contains("CONCLUIDO"));
        assertTrue(toString.contains("2024-06-30"));
    }

    @Test
    void testTitulo_DeveAceitarTitulosComCaracteresEspeciais() {
        // Arrange
        String tituloComEspeciais = "Marco com caracteres especiais: áéíóú çãõ";

        // Act
        marcoPDI.setTitulo(tituloComEspeciais);

        // Assert
        assertEquals(tituloComEspeciais, marcoPDI.getTitulo());
    }

    @Test
    void testDescricao_DeveAceitarDescricoesComQuebrasDeLinha() {
        // Arrange
        String descricaoComQuebras = "Primeira linha\nSegunda linha\nTerceira linha";

        // Act
        marcoPDI.setDescricao(descricaoComQuebras);

        // Assert
        assertEquals(descricaoComQuebras, marcoPDI.getDescricao());
    }

    @Test
    void testDataFinal_DeveAceitarDatasPassadas() {
        // Arrange
        LocalDate dataPassada = LocalDate.of(2023, 12, 31);

        // Act
        marcoPDI.setDtFinal(dataPassada);

        // Assert
        assertEquals(dataPassada, marcoPDI.getDtFinal());
    }

    @Test
    void testDataFinal_DeveAceitarDatasFuturas() {
        // Arrange
        LocalDate dataFutura = LocalDate.of(2025, 12, 31);

        // Act
        marcoPDI.setDtFinal(dataFutura);

        // Assert
        assertEquals(dataFutura, marcoPDI.getDtFinal());
    }

    @Test
    void testDataFinal_DeveAceitarDataAtual() {
        // Arrange
        LocalDate dataAtual = LocalDate.now();

        // Act
        marcoPDI.setDtFinal(dataAtual);

        // Assert
        assertEquals(dataAtual, marcoPDI.getDtFinal());
    }

    @Test
    void testRelacionamentoComPDI_DeveSerBidirecional() {
        // Arrange
        marcoPDI.setPdi(pdi);

        // Act & Assert
        assertEquals(pdi, marcoPDI.getPdi());
        assertNotNull(marcoPDI.getPdi().getId());
        assertEquals(1L, marcoPDI.getPdi().getId());
        assertEquals("PDI Teste", marcoPDI.getPdi().getTitulo());
    }

    @Test
    void testStatus_DevePermitirTransicoes() {
        // Arrange & Act & Assert
        marcoPDI.setStatus(StatusMarcoPDI.PENDENTE);
        assertEquals(StatusMarcoPDI.PENDENTE, marcoPDI.getStatus());

        marcoPDI.setStatus(StatusMarcoPDI.CONCLUIDO);
        assertEquals(StatusMarcoPDI.CONCLUIDO, marcoPDI.getStatus());
    }

    @Test
    void testTitulo_DeveAceitarTitulosComNumeros() {
        // Arrange
        String tituloComNumeros = "Marco 1 - Primeira Fase";

        // Act
        marcoPDI.setTitulo(tituloComNumeros);

        // Assert
        assertEquals(tituloComNumeros, marcoPDI.getTitulo());
    }

    @Test
    void testDescricao_DeveAceitarDescricoesComFormatacao() {
        // Arrange
        String descricaoComFormatacao = "Descrição com formatação:\n" +
                "• Item 1\n" +
                "• Item 2\n" +
                "• Item 3";

        // Act
        marcoPDI.setDescricao(descricaoComFormatacao);

        // Assert
        assertEquals(descricaoComFormatacao, marcoPDI.getDescricao());
    }
} 
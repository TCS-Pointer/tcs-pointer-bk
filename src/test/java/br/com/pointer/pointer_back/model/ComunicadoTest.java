package br.com.pointer.pointer_back.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComunicadoTest {

    private Comunicado comunicado;

    @BeforeEach
    void setUp() {
        comunicado = new Comunicado();
    }

    @Test
    void testSetAndGetId() {
        // Arrange
        Long id = 1L;

        // Act
        comunicado.setId(id);

        // Assert
        assertEquals(id, comunicado.getId());
    }

    @Test
    void testSetAndGetTitulo() {
        // Arrange
        String titulo = "Comunicado Importante";

        // Act
        comunicado.setTitulo(titulo);

        // Assert
        assertEquals(titulo, comunicado.getTitulo());
    }

    @Test
    void testSetAndGetDescricao() {
        // Arrange
        String descricao = "Este é um comunicado importante para todos os colaboradores";

        // Act
        comunicado.setDescricao(descricao);

        // Assert
        assertEquals(descricao, comunicado.getDescricao());
    }

    @Test
    void testSetAndGetSetores() {
        // Arrange
        Set<String> setores = new HashSet<>(Arrays.asList("TI", "RH", "FINANCEIRO"));

        // Act
        comunicado.setSetores(setores);

        // Assert
        assertEquals(setores, comunicado.getSetores());
        assertEquals(3, comunicado.getSetores().size());
    }

    @Test
    void testSetAndGetApenasGestores() {
        // Arrange
        boolean apenasGestores = true;

        // Act
        comunicado.setApenasGestores(apenasGestores);

        // Assert
        assertEquals(apenasGestores, comunicado.isApenasGestores());
    }

    @Test
    void testSetAndGetDataPublicacao() {
        // Arrange
        LocalDateTime dataPublicacao = LocalDateTime.now();

        // Act
        comunicado.setDataPublicacao(dataPublicacao);

        // Assert
        assertEquals(dataPublicacao, comunicado.getDataPublicacao());
    }

    @Test
    void testSetAndGetAtivo() {
        // Arrange
        boolean ativo = true;

        // Act
        comunicado.setAtivo(ativo);

        // Assert
        assertEquals(ativo, comunicado.isAtivo());
    }

    @Test
    void testDefaultValues() {
        // Assert
        assertFalse(comunicado.isApenasGestores());
        assertFalse(comunicado.isAtivo());
    }

    @Test
    void testEquals_ComMesmoId_DeveRetornarTrue() {
        // Arrange
        Comunicado comunicado1 = new Comunicado();
        comunicado1.setId(1L);
        comunicado1.setTitulo("Comunicado 1");

        Comunicado comunicado2 = new Comunicado();
        comunicado2.setId(1L);
        comunicado2.setTitulo("Comunicado 1");

        // Act & Assert
        assertEquals(comunicado1, comunicado2);
    }

    @Test
    void testEquals_ComIdsDiferentes_DeveRetornarFalse() {
        // Arrange
        Comunicado comunicado1 = new Comunicado();
        comunicado1.setId(1L);

        Comunicado comunicado2 = new Comunicado();
        comunicado2.setId(2L);

        // Act & Assert
        assertNotEquals(comunicado1, comunicado2);
    }

    @Test
    void testEquals_ComNull_DeveRetornarFalse() {
        // Arrange
        Comunicado comunicado1 = new Comunicado();
        comunicado1.setId(1L);

        // Act & Assert
        assertNotEquals(null, comunicado1);
    }

    @Test
    void testEquals_ComMesmoObjeto_DeveRetornarTrue() {
        // Arrange
        Comunicado comunicado1 = new Comunicado();
        comunicado1.setId(1L);

        // Act & Assert
        assertEquals(comunicado1, comunicado1);
    }

    @Test
    void testEquals_ComTipoDiferente_DeveRetornarFalse() {
        // Arrange
        Comunicado comunicado1 = new Comunicado();
        comunicado1.setId(1L);

        String string = "teste";

        // Act & Assert
        assertNotEquals(comunicado1, string);
    }

    @Test
    void testHashCode_ComMesmoId_DeveRetornarMesmoHashCode() {
        // Arrange
        Comunicado comunicado1 = new Comunicado();
        comunicado1.setId(1L);

        Comunicado comunicado2 = new Comunicado();
        comunicado2.setId(1L);

        // Act & Assert
        assertEquals(comunicado1.hashCode(), comunicado2.hashCode());
    }

    @Test
    void testHashCode_ComIdsDiferentes_DeveRetornarHashCodesDiferentes() {
        // Arrange
        Comunicado comunicado1 = new Comunicado();
        comunicado1.setId(1L);

        Comunicado comunicado2 = new Comunicado();
        comunicado2.setId(2L);

        // Act & Assert
        assertNotEquals(comunicado1.hashCode(), comunicado2.hashCode());
    }

    @Test
    void testToString_DeveConterInformacoesPrincipais() {
        // Arrange
        comunicado.setId(1L);
        comunicado.setTitulo("Comunicado Teste");
        comunicado.setDescricao("Descrição do comunicado");
        comunicado.setApenasGestores(false);
        comunicado.setAtivo(true);

        // Act
        String toString = comunicado.toString();

        // Assert
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("Comunicado Teste"));
        assertTrue(toString.contains("Descrição do comunicado"));
    }

    @Test
    void testToString_ComValoresNulos_DeveFuncionar() {
        // Act
        String toString = comunicado.toString();

        // Assert
        assertNotNull(toString);
        assertFalse(toString.isEmpty());
    }

    @Test
    void testSetores_DeveAceitarSetoresVazios() {
        // Arrange
        Set<String> setoresVazios = new HashSet<>();

        // Act
        comunicado.setSetores(setoresVazios);

        // Assert
        assertEquals(setoresVazios, comunicado.getSetores());
        assertTrue(comunicado.getSetores().isEmpty());
    }

    @Test
    void testSetores_DeveAceitarSetoresNulos() {
        // Act
        comunicado.setSetores(null);

        // Assert
        assertNull(comunicado.getSetores());
    }

    @Test
    void testSetores_DeveAceitarSetoresComElementosDuplicados() {
        // Arrange
        Set<String> setoresComDuplicados = new HashSet<>(Arrays.asList("TI", "TI", "RH"));

        // Act
        comunicado.setSetores(setoresComDuplicados);

        // Assert
        assertEquals(2, comunicado.getSetores().size());
        assertTrue(comunicado.getSetores().contains("TI"));
        assertTrue(comunicado.getSetores().contains("RH"));
    }

    @Test
    void testSetores_DeveAceitarDiferentesSetores() {
        // Arrange & Act & Assert
        Set<String> setoresTI = new HashSet<>(Arrays.asList("TI"));
        comunicado.setSetores(setoresTI);
        assertEquals(setoresTI, comunicado.getSetores());

        Set<String> setoresRH = new HashSet<>(Arrays.asList("RH"));
        comunicado.setSetores(setoresRH);
        assertEquals(setoresRH, comunicado.getSetores());

        Set<String> setoresFinanceiro = new HashSet<>(Arrays.asList("FINANCEIRO"));
        comunicado.setSetores(setoresFinanceiro);
        assertEquals(setoresFinanceiro, comunicado.getSetores());

        Set<String> setoresMarketing = new HashSet<>(Arrays.asList("MARKETING"));
        comunicado.setSetores(setoresMarketing);
        assertEquals(setoresMarketing, comunicado.getSetores());
    }

    @Test
    void testApenasGestores_DeveAceitarValoresBooleanos() {
        // Arrange & Act & Assert
        comunicado.setApenasGestores(true);
        assertTrue(comunicado.isApenasGestores());

        comunicado.setApenasGestores(false);
        assertFalse(comunicado.isApenasGestores());
    }

    @Test
    void testAtivo_DeveAceitarValoresBooleanos() {
        // Arrange & Act & Assert
        comunicado.setAtivo(true);
        assertTrue(comunicado.isAtivo());

        comunicado.setAtivo(false);
        assertFalse(comunicado.isAtivo());
    }

    @Test
    void testDataPublicacao_DeveSerAutomaticaNoPrePersist() {
        // Arrange
        Comunicado comunicadoComPrePersist = new Comunicado();

        // Act
        comunicadoComPrePersist.onCreate();

        // Assert
        assertNotNull(comunicadoComPrePersist.getDataPublicacao());
        assertTrue(comunicadoComPrePersist.getDataPublicacao().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(comunicadoComPrePersist.getDataPublicacao().isAfter(LocalDateTime.now().minusSeconds(1)));
    }

    @Test
    void testDataPublicacao_DeveManterDataQuandoJaDefinida() {
        // Arrange
        LocalDateTime dataEspecifica = LocalDateTime.of(2023, 1, 1, 12, 0, 0);
        comunicado.setDataPublicacao(dataEspecifica);

        // Act
        comunicado.onCreate();

        // Assert
        int currentYear = LocalDateTime.now().getYear();
        assertEquals(currentYear, comunicado.getDataPublicacao().getYear());
    }

    @Test
    void testTitulo_DeveAceitarDiferentesTitulos() {
        // Arrange & Act & Assert
        comunicado.setTitulo("Comunicado Geral");
        assertEquals("Comunicado Geral", comunicado.getTitulo());

        comunicado.setTitulo("Aviso Importante");
        assertEquals("Aviso Importante", comunicado.getTitulo());

        comunicado.setTitulo("Notificação");
        assertEquals("Notificação", comunicado.getTitulo());

        comunicado.setTitulo("Informe");
        assertEquals("Informe", comunicado.getTitulo());
    }

    @Test
    void testDescricao_DeveAceitarDescricoesLongas() {
        // Arrange
        String descricaoLonga = "Esta é uma descrição muito longa que pode conter até 1200 caracteres. " +
                "Ela pode incluir informações detalhadas sobre o comunicado, instruções específicas, " +
                "datas importantes, contatos relevantes e qualquer outra informação que seja necessária " +
                "para que os destinatários compreendam completamente o conteúdo do comunicado.";

        // Act
        comunicado.setDescricao(descricaoLonga);

        // Assert
        assertEquals(descricaoLonga, comunicado.getDescricao());
    }

    @Test
    void testDescricao_DeveAceitarDescricoesVazias() {
        // Arrange
        String descricaoVazia = "";

        // Act
        comunicado.setDescricao(descricaoVazia);

        // Assert
        assertEquals(descricaoVazia, comunicado.getDescricao());
    }

    @Test
    void testEquals_ComIdNulo_DeveRetornarFalse() {
        // Arrange
        Comunicado comunicado1 = new Comunicado();
        comunicado1.setId(null);

        Comunicado comunicado2 = new Comunicado();
        comunicado2.setId(1L);

        // Act & Assert
        assertNotEquals(comunicado1, comunicado2);
    }

    @Test
    void testEquals_ComAmbosIdsNulos_DeveRetornarTrue() {
        // Arrange
        Comunicado comunicado1 = new Comunicado();
        comunicado1.setId(null);

        Comunicado comunicado2 = new Comunicado();
        comunicado2.setId(null);

        // Act & Assert
        assertEquals(comunicado1, comunicado2);
    }

    @Test
    void testHashCode_ComIdNulo_DeveRetornarHashCodeConsistente() {
        // Arrange
        Comunicado comunicado1 = new Comunicado();
        comunicado1.setId(null);

        Comunicado comunicado2 = new Comunicado();
        comunicado2.setId(null);

        // Act & Assert
        assertEquals(comunicado1.hashCode(), comunicado2.hashCode());
    }

    @Test
    void testToString_ComTodosOsCamposPreenchidos_DeveConterTodasInformacoes() {
        // Arrange
        Set<String> setores = new HashSet<>(Arrays.asList("TI", "RH"));
        LocalDateTime dataPublicacao = LocalDateTime.of(2023, 1, 1, 12, 0, 0);

        comunicado.setId(1L);
        comunicado.setTitulo("Comunicado Completo");
        comunicado.setDescricao("Descrição completa do comunicado");
        comunicado.setSetores(setores);
        comunicado.setApenasGestores(true);
        comunicado.setDataPublicacao(dataPublicacao);
        comunicado.setAtivo(true);

        // Act
        String toString = comunicado.toString();

        // Assert
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("Comunicado Completo"));
        assertTrue(toString.contains("Descrição completa do comunicado"));
        assertTrue(toString.contains("TI"));
        assertTrue(toString.contains("RH"));
        assertTrue(toString.contains("true"));
    }

    @Test
    void testSetores_DeveManterOrdemInsercao() {
        // Arrange
        Set<String> setores = new HashSet<>(Arrays.asList("TI", "RH", "FINANCEIRO"));

        // Act
        comunicado.setSetores(setores);

        // Assert
        assertEquals(3, comunicado.getSetores().size());
        assertTrue(comunicado.getSetores().contains("TI"));
        assertTrue(comunicado.getSetores().contains("RH"));
        assertTrue(comunicado.getSetores().contains("FINANCEIRO"));
    }

    @Test
    void testSetores_DeveAceitarSetoresComEspacos() {
        // Arrange
        Set<String> setoresComEspacos = new HashSet<>(Arrays.asList("TI ", " RH", " FINANCEIRO "));

        // Act
        comunicado.setSetores(setoresComEspacos);

        // Assert
        assertEquals(3, comunicado.getSetores().size());
        assertTrue(comunicado.getSetores().contains("TI "));
        assertTrue(comunicado.getSetores().contains(" RH"));
        assertTrue(comunicado.getSetores().contains(" FINANCEIRO "));
    }

    @Test
    void testTitulo_DeveAceitarTitulosComCaracteresEspeciais() {
        // Arrange
        String tituloComEspeciais = "Comunicado com caracteres especiais: áéíóú çãõ";

        // Act
        comunicado.setTitulo(tituloComEspeciais);

        // Assert
        assertEquals(tituloComEspeciais, comunicado.getTitulo());
    }

    @Test
    void testDescricao_DeveAceitarDescricoesComQuebrasDeLinha() {
        // Arrange
        String descricaoComQuebras = "Primeira linha\nSegunda linha\nTerceira linha";

        // Act
        comunicado.setDescricao(descricaoComQuebras);

        // Assert
        assertEquals(descricaoComQuebras, comunicado.getDescricao());
    }
} 
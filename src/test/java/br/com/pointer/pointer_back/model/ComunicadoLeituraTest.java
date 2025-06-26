package br.com.pointer.pointer_back.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComunicadoLeituraTest {

    private ComunicadoLeitura comunicadoLeitura;

    @BeforeEach
    void setUp() {
        comunicadoLeitura = new ComunicadoLeitura();
    }

    @Test
    void testSetAndGetId() {
        // Arrange
        Long id = 1L;

        // Act
        comunicadoLeitura.setId(id);

        // Assert
        assertEquals(id, comunicadoLeitura.getId());
    }

    @Test
    void testSetAndGetComunicadoId() {
        // Arrange
        Long comunicadoId = 1L;

        // Act
        comunicadoLeitura.setComunicadoId(comunicadoId);

        // Assert
        assertEquals(comunicadoId, comunicadoLeitura.getComunicadoId());
    }

    @Test
    void testSetAndGetUsuarioId() {
        // Arrange
        Long usuarioId = 1L;

        // Act
        comunicadoLeitura.setUsuarioId(usuarioId);

        // Assert
        assertEquals(usuarioId, comunicadoLeitura.getUsuarioId());
    }

    @Test
    void testSetAndGetDtLeitura() {
        // Arrange
        LocalDateTime dtLeitura = LocalDateTime.now();

        // Act
        comunicadoLeitura.setDtLeitura(dtLeitura);

        // Assert
        assertEquals(dtLeitura, comunicadoLeitura.getDtLeitura());
    }

    @Test
    void testDefaultValues() {
        // Assert
        assertNull(comunicadoLeitura.getId());
        assertNull(comunicadoLeitura.getComunicadoId());
        assertNull(comunicadoLeitura.getUsuarioId());
        assertNull(comunicadoLeitura.getDtLeitura());
    }

    @Test
    void testEquals_ComMesmoId_DeveRetornarTrue() {
        // Arrange
        ComunicadoLeitura leitura1 = new ComunicadoLeitura();
        leitura1.setId(1L);
        leitura1.setComunicadoId(1L);
        leitura1.setUsuarioId(1L);

        ComunicadoLeitura leitura2 = new ComunicadoLeitura();
        leitura2.setId(1L);
        leitura2.setComunicadoId(1L);
        leitura2.setUsuarioId(1L);

        // Act & Assert
        assertEquals(leitura1, leitura2);
    }

    @Test
    void testEquals_ComIdsDiferentes_DeveRetornarFalse() {
        // Arrange
        ComunicadoLeitura leitura1 = new ComunicadoLeitura();
        leitura1.setId(1L);

        ComunicadoLeitura leitura2 = new ComunicadoLeitura();
        leitura2.setId(2L);

        // Act & Assert
        assertNotEquals(leitura1, leitura2);
    }

    @Test
    void testEquals_ComNull_DeveRetornarFalse() {
        // Arrange
        ComunicadoLeitura leitura1 = new ComunicadoLeitura();
        leitura1.setId(1L);

        // Act & Assert
        assertNotEquals(null, leitura1);
    }

    @Test
    void testEquals_ComMesmoObjeto_DeveRetornarTrue() {
        // Arrange
        ComunicadoLeitura leitura1 = new ComunicadoLeitura();
        leitura1.setId(1L);

        // Act & Assert
        assertEquals(leitura1, leitura1);
    }

    @Test
    void testEquals_ComTipoDiferente_DeveRetornarFalse() {
        // Arrange
        ComunicadoLeitura leitura1 = new ComunicadoLeitura();
        leitura1.setId(1L);

        String string = "teste";

        // Act & Assert
        assertNotEquals(leitura1, string);
    }

    @Test
    void testHashCode_ComMesmoId_DeveRetornarMesmoHashCode() {
        // Arrange
        ComunicadoLeitura leitura1 = new ComunicadoLeitura();
        leitura1.setId(1L);

        ComunicadoLeitura leitura2 = new ComunicadoLeitura();
        leitura2.setId(1L);

        // Act & Assert
        assertEquals(leitura1.hashCode(), leitura2.hashCode());
    }

    @Test
    void testHashCode_ComIdsDiferentes_DeveRetornarHashCodesDiferentes() {
        // Arrange
        ComunicadoLeitura leitura1 = new ComunicadoLeitura();
        leitura1.setId(1L);

        ComunicadoLeitura leitura2 = new ComunicadoLeitura();
        leitura2.setId(2L);

        // Act & Assert
        assertNotEquals(leitura1.hashCode(), leitura2.hashCode());
    }

    @Test
    void testToString_DeveConterInformacoesPrincipais() {
        // Arrange
        comunicadoLeitura.setId(1L);
        comunicadoLeitura.setComunicadoId(1L);
        comunicadoLeitura.setUsuarioId(1L);
        comunicadoLeitura.setDtLeitura(LocalDateTime.of(2024, 1, 1, 12, 0, 0));

        // Act
        String toString = comunicadoLeitura.toString();

        // Assert
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("2024-01-01"));
    }

    @Test
    void testToString_ComValoresNulos_DeveFuncionar() {
        // Act
        String toString = comunicadoLeitura.toString();

        // Assert
        assertNotNull(toString);
        assertFalse(toString.isEmpty());
    }

    @Test
    void testComunicadoId_DeveAceitarDiferentesIds() {
        // Arrange & Act & Assert
        comunicadoLeitura.setComunicadoId(1L);
        assertEquals(1L, comunicadoLeitura.getComunicadoId());

        comunicadoLeitura.setComunicadoId(100L);
        assertEquals(100L, comunicadoLeitura.getComunicadoId());

        comunicadoLeitura.setComunicadoId(999999L);
        assertEquals(999999L, comunicadoLeitura.getComunicadoId());
    }

    @Test
    void testUsuarioId_DeveAceitarDiferentesIds() {
        // Arrange & Act & Assert
        comunicadoLeitura.setUsuarioId(1L);
        assertEquals(1L, comunicadoLeitura.getUsuarioId());

        comunicadoLeitura.setUsuarioId(50L);
        assertEquals(50L, comunicadoLeitura.getUsuarioId());

        comunicadoLeitura.setUsuarioId(1000L);
        assertEquals(1000L, comunicadoLeitura.getUsuarioId());
    }

    @Test
    void testDataLeitura_DeveAceitarDiferentesDatas() {
        // Arrange & Act & Assert
        LocalDateTime data1 = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        comunicadoLeitura.setDtLeitura(data1);
        assertEquals(data1, comunicadoLeitura.getDtLeitura());

        LocalDateTime data2 = LocalDateTime.of(2024, 6, 15, 15, 30, 45);
        comunicadoLeitura.setDtLeitura(data2);
        assertEquals(data2, comunicadoLeitura.getDtLeitura());

        LocalDateTime data3 = LocalDateTime.of(2024, 12, 31, 23, 59, 59);
        comunicadoLeitura.setDtLeitura(data3);
        assertEquals(data3, comunicadoLeitura.getDtLeitura());
    }

    @Test
    void testDataLeitura_DeveAceitarDataAtual() {
        // Arrange
        LocalDateTime dataAtual = LocalDateTime.now();

        // Act
        comunicadoLeitura.setDtLeitura(dataAtual);

        // Assert
        assertEquals(dataAtual, comunicadoLeitura.getDtLeitura());
    }

    @Test
    void testDataLeitura_DeveAceitarDataPassada() {
        // Arrange
        LocalDateTime dataPassada = LocalDateTime.of(2023, 12, 31, 23, 59, 59);

        // Act
        comunicadoLeitura.setDtLeitura(dataPassada);

        // Assert
        assertEquals(dataPassada, comunicadoLeitura.getDtLeitura());
    }

    @Test
    void testDataLeitura_DeveAceitarDataFutura() {
        // Arrange
        LocalDateTime dataFutura = LocalDateTime.of(2025, 12, 31, 23, 59, 59);

        // Act
        comunicadoLeitura.setDtLeitura(dataFutura);

        // Assert
        assertEquals(dataFutura, comunicadoLeitura.getDtLeitura());
    }

    @Test
    void testEquals_ComIdNulo_DeveRetornarFalse() {
        // Arrange
        ComunicadoLeitura leitura1 = new ComunicadoLeitura();
        leitura1.setId(null);

        ComunicadoLeitura leitura2 = new ComunicadoLeitura();
        leitura2.setId(1L);

        // Act & Assert
        assertNotEquals(leitura1, leitura2);
    }

    @Test
    void testEquals_ComAmbosIdsNulos_DeveRetornarTrue() {
        // Arrange
        ComunicadoLeitura leitura1 = new ComunicadoLeitura();
        leitura1.setId(null);

        ComunicadoLeitura leitura2 = new ComunicadoLeitura();
        leitura2.setId(null);

        // Act & Assert
        assertEquals(leitura1, leitura2);
    }

    @Test
    void testHashCode_ComIdNulo_DeveRetornarHashCodeConsistente() {
        // Arrange
        ComunicadoLeitura leitura1 = new ComunicadoLeitura();
        leitura1.setId(null);

        ComunicadoLeitura leitura2 = new ComunicadoLeitura();
        leitura2.setId(null);

        // Act & Assert
        assertEquals(leitura1.hashCode(), leitura2.hashCode());
    }

    @Test
    void testToString_ComTodosOsCamposPreenchidos_DeveConterTodasInformacoes() {
        // Arrange
        LocalDateTime dtLeitura = LocalDateTime.of(2024, 6, 15, 14, 30, 25);

        comunicadoLeitura.setId(1L);
        comunicadoLeitura.setComunicadoId(100L);
        comunicadoLeitura.setUsuarioId(50L);
        comunicadoLeitura.setDtLeitura(dtLeitura);

        // Act
        String toString = comunicadoLeitura.toString();

        // Assert
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("100"));
        assertTrue(toString.contains("50"));
        assertTrue(toString.contains("2024-06-15"));
    }

    @Test
    void testComunicadoId_DeveAceitarValorNulo() {
        // Act
        comunicadoLeitura.setComunicadoId(null);

        // Assert
        assertNull(comunicadoLeitura.getComunicadoId());
    }

    @Test
    void testUsuarioId_DeveAceitarValorNulo() {
        // Act
        comunicadoLeitura.setUsuarioId(null);

        // Assert
        assertNull(comunicadoLeitura.getUsuarioId());
    }

    @Test
    void testDataLeitura_DeveAceitarValorNulo() {
        // Act
        comunicadoLeitura.setDtLeitura(null);

        // Assert
        assertNull(comunicadoLeitura.getDtLeitura());
    }

    @Test
    void testRelacionamentoComunicadoUsuario_DeveSerConsistente() {
        // Arrange
        Long comunicadoId = 1L;
        Long usuarioId = 2L;

        // Act
        comunicadoLeitura.setComunicadoId(comunicadoId);
        comunicadoLeitura.setUsuarioId(usuarioId);

        // Assert
        assertEquals(comunicadoId, comunicadoLeitura.getComunicadoId());
        assertEquals(usuarioId, comunicadoLeitura.getUsuarioId());
    }

    @Test
    void testDataLeitura_DeveSerPrecisa() {
        // Arrange
        LocalDateTime dataEspecifica = LocalDateTime.of(2024, 3, 15, 10, 30, 45, 123456789);

        // Act
        comunicadoLeitura.setDtLeitura(dataEspecifica);

        // Assert
        assertEquals(dataEspecifica, comunicadoLeitura.getDtLeitura());
        assertEquals(2024, comunicadoLeitura.getDtLeitura().getYear());
        assertEquals(3, comunicadoLeitura.getDtLeitura().getMonthValue());
        assertEquals(15, comunicadoLeitura.getDtLeitura().getDayOfMonth());
        assertEquals(10, comunicadoLeitura.getDtLeitura().getHour());
        assertEquals(30, comunicadoLeitura.getDtLeitura().getMinute());
        assertEquals(45, comunicadoLeitura.getDtLeitura().getSecond());
        assertEquals(123456789, comunicadoLeitura.getDtLeitura().getNano());
    }

    @Test
    void testId_DeveAceitarValoresExtremos() {
        // Arrange & Act & Assert
        comunicadoLeitura.setId(0L);
        assertEquals(0L, comunicadoLeitura.getId());

        comunicadoLeitura.setId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, comunicadoLeitura.getId());

        comunicadoLeitura.setId(Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, comunicadoLeitura.getId());
    }

    @Test
    void testComunicadoId_DeveAceitarValoresExtremos() {
        // Arrange & Act & Assert
        comunicadoLeitura.setComunicadoId(0L);
        assertEquals(0L, comunicadoLeitura.getComunicadoId());

        comunicadoLeitura.setComunicadoId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, comunicadoLeitura.getComunicadoId());

        comunicadoLeitura.setComunicadoId(Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, comunicadoLeitura.getComunicadoId());
    }

    @Test
    void testUsuarioId_DeveAceitarValoresExtremos() {
        // Arrange & Act & Assert
        comunicadoLeitura.setUsuarioId(0L);
        assertEquals(0L, comunicadoLeitura.getUsuarioId());

        comunicadoLeitura.setUsuarioId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, comunicadoLeitura.getUsuarioId());

        comunicadoLeitura.setUsuarioId(Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, comunicadoLeitura.getUsuarioId());
    }

    @Test
    void testDataLeitura_DeveManterPrecisao() {
        // Arrange
        LocalDateTime dataComNanos = LocalDateTime.of(2024, 1, 1, 12, 0, 0, 999999999);

        // Act
        comunicadoLeitura.setDtLeitura(dataComNanos);

        // Assert
        assertEquals(dataComNanos, comunicadoLeitura.getDtLeitura());
        assertEquals(999999999, comunicadoLeitura.getDtLeitura().getNano());
    }

    @Test
    void testDataLeitura_DeveAceitarDataComZeros() {
        // Arrange
        LocalDateTime dataComZeros = LocalDateTime.of(2024, 1, 1, 0, 0, 0, 0);

        // Act
        comunicadoLeitura.setDtLeitura(dataComZeros);

        // Assert
        assertEquals(dataComZeros, comunicadoLeitura.getDtLeitura());
        assertEquals(0, comunicadoLeitura.getDtLeitura().getHour());
        assertEquals(0, comunicadoLeitura.getDtLeitura().getMinute());
        assertEquals(0, comunicadoLeitura.getDtLeitura().getSecond());
        assertEquals(0, comunicadoLeitura.getDtLeitura().getNano());
    }
} 
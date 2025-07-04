package br.com.pointer.pointer_back.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TipoFeedbackTest {

    @Test
    void values_DeveConterTodosOsTipos() {
        // Act
        TipoFeedback[] values = TipoFeedback.values();

        // Assert
        assertNotNull(values);
        assertEquals(2, values.length);
        assertTrue(contains(values, TipoFeedback.POSITIVO));
        assertTrue(contains(values, TipoFeedback.CONSTRUTIVO));
    }

    @Test
    void valueOf_ComStringValida_DeveRetornarEnum() {
        // Act & Assert
        assertEquals(TipoFeedback.POSITIVO, TipoFeedback.valueOf("POSITIVO"));
        assertEquals(TipoFeedback.CONSTRUTIVO, TipoFeedback.valueOf("CONSTRUTIVO"));
    }

    @Test
    void valueOf_ComStringInvalida_DeveLancarExcecao() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            TipoFeedback.valueOf("INVALIDO");
        });
    }

    @Test
    void valueOf_ComStringNula_DeveLancarExcecao() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            TipoFeedback.valueOf(null);
        });
    }

    @Test
    void toString_DeveRetornarNomeDoEnum() {
        // Act & Assert
        assertEquals("POSITIVO", TipoFeedback.POSITIVO.toString());
        assertEquals("CONSTRUTIVO", TipoFeedback.CONSTRUTIVO.toString());
    }

    @Test
    void name_DeveRetornarNomeDoEnum() {
        // Act & Assert
        assertEquals("POSITIVO", TipoFeedback.POSITIVO.name());
        assertEquals("CONSTRUTIVO", TipoFeedback.CONSTRUTIVO.name());
    }

    @Test
    void ordinal_DeveRetornarPosicaoCorreta() {
        // Act & Assert
        assertEquals(0, TipoFeedback.POSITIVO.ordinal());
        assertEquals(1, TipoFeedback.CONSTRUTIVO.ordinal());
    }

    @Test
    void compareTo_DeveCompararCorretamente() {
        // Act & Assert
        assertTrue(TipoFeedback.POSITIVO.compareTo(TipoFeedback.CONSTRUTIVO) < 0);
        assertTrue(TipoFeedback.CONSTRUTIVO.compareTo(TipoFeedback.POSITIVO) > 0);
        assertEquals(0, TipoFeedback.POSITIVO.compareTo(TipoFeedback.POSITIVO));
    }

    @Test
    void equals_DeveCompararCorretamente() {
        // Act & Assert
        assertTrue(TipoFeedback.POSITIVO.equals(TipoFeedback.POSITIVO));
        assertFalse(TipoFeedback.POSITIVO.equals(TipoFeedback.CONSTRUTIVO));
        assertFalse(TipoFeedback.POSITIVO.equals(null));
        assertFalse(TipoFeedback.POSITIVO.equals("POSITIVO"));
    }

    @Test
    void hashCode_DeveSerConsistente() {
        // Act & Assert
        assertEquals(TipoFeedback.POSITIVO.hashCode(), TipoFeedback.POSITIVO.hashCode());
        assertEquals(TipoFeedback.CONSTRUTIVO.hashCode(), TipoFeedback.CONSTRUTIVO.hashCode());
    }

    @Test
    void positivo_DeveTerValorCorreto() {
        // Assert
        assertEquals("POSITIVO", TipoFeedback.POSITIVO.name());
        assertEquals(0, TipoFeedback.POSITIVO.ordinal());
    }

    @Test
    void construtivo_DeveTerValorCorreto() {
        // Assert
        assertEquals("CONSTRUTIVO", TipoFeedback.CONSTRUTIVO.name());
        assertEquals(1, TipoFeedback.CONSTRUTIVO.ordinal());
    }

    @Test
    void switchStatement_DeveFuncionarCorretamente() {
        // Act & Assert
        assertEquals("Feedback positivo", getTipoDescription(TipoFeedback.POSITIVO));
        assertEquals("Feedback construtivo", getTipoDescription(TipoFeedback.CONSTRUTIVO));
    }

    @Test
    void stream_DeveFuncionarCorretamente() {
        // Act
        long count = java.util.Arrays.stream(TipoFeedback.values()).count();

        // Assert
        assertEquals(2, count);
    }

    @Test
    void filter_DeveFuncionarCorretamente() {
        // Act
        TipoFeedback[] tiposPositivos = java.util.Arrays.stream(TipoFeedback.values())
                .filter(tipo -> tipo == TipoFeedback.POSITIVO)
                .toArray(TipoFeedback[]::new);

        // Assert
        assertEquals(1, tiposPositivos.length);
        assertTrue(contains(tiposPositivos, TipoFeedback.POSITIVO));
        assertFalse(contains(tiposPositivos, TipoFeedback.CONSTRUTIVO));
    }

    @Test
    void map_DeveFuncionarCorretamente() {
        // Act
        String[] nomes = java.util.Arrays.stream(TipoFeedback.values())
                .map(TipoFeedback::name)
                .toArray(String[]::new);

        // Assert
        assertEquals(2, nomes.length);
        assertTrue(contains(nomes, "POSITIVO"));
        assertTrue(contains(nomes, "CONSTRUTIVO"));
    }

    @Test
    void collect_DeveFuncionarCorretamente() {
        // Act
        java.util.List<TipoFeedback> tipoList = java.util.Arrays.stream(TipoFeedback.values())
                .collect(java.util.stream.Collectors.toList());

        // Assert
        assertEquals(2, tipoList.size());
        assertTrue(tipoList.contains(TipoFeedback.POSITIVO));
        assertTrue(tipoList.contains(TipoFeedback.CONSTRUTIVO));
    }

    @Test
    void enumConstants_DeveSerUnicos() {
        // Act
        TipoFeedback[] values = TipoFeedback.values();

        // Assert
        long uniqueCount = java.util.Arrays.stream(values).distinct().count();
        assertEquals(values.length, uniqueCount);
    }

    @Test
    void enumNames_DeveSerUnicos() {
        // Act
        String[] names = java.util.Arrays.stream(TipoFeedback.values())
                .map(TipoFeedback::name)
                .toArray(String[]::new);

        // Assert
        long uniqueCount = java.util.Arrays.stream(names).distinct().count();
        assertEquals(names.length, uniqueCount);
    }

    @Test
    void enumOrdinals_DeveSerUnicos() {
        // Act
        int[] ordinals = java.util.Arrays.stream(TipoFeedback.values())
                .mapToInt(TipoFeedback::ordinal)
                .toArray();

        // Assert
        long uniqueCount = java.util.Arrays.stream(ordinals).distinct().count();
        assertEquals(ordinals.length, uniqueCount);
    }

    @Test
    void enumToString_DeveSerConsistente() {
        // Act & Assert
        assertEquals(TipoFeedback.POSITIVO.name(), TipoFeedback.POSITIVO.toString());
        assertEquals(TipoFeedback.CONSTRUTIVO.name(), TipoFeedback.CONSTRUTIVO.toString());
    }

    @Test
    void enumGetClass_DeveSerCorreto() {
        // Act & Assert
        assertEquals(TipoFeedback.class, TipoFeedback.POSITIVO.getClass());
        assertEquals(TipoFeedback.class, TipoFeedback.CONSTRUTIVO.getClass());
    }

    @Test
    void enumGetDeclaringClass_DeveSerCorreto() {
        // Act & Assert
        assertEquals(TipoFeedback.class, TipoFeedback.POSITIVO.getDeclaringClass());
        assertEquals(TipoFeedback.class, TipoFeedback.CONSTRUTIVO.getDeclaringClass());
    }

    @Test
    void isPositivo_DeveRetornarCorreto() {
        // Act & Assert
        assertTrue(isPositivo(TipoFeedback.POSITIVO));
        assertFalse(isPositivo(TipoFeedback.CONSTRUTIVO));
    }

    @Test
    void isConstrutivo_DeveRetornarCorreto() {
        // Act & Assert
        assertTrue(isConstrutivo(TipoFeedback.CONSTRUTIVO));
        assertFalse(isConstrutivo(TipoFeedback.POSITIVO));
    }

    @Test
    void getTipoCount_DeveRetornarCorreto() {
        // Act & Assert
        assertEquals(2, getTipoCount());
    }

    @Test
    void getTipoByOrdinal_DeveRetornarCorreto() {
        // Act & Assert
        assertEquals(TipoFeedback.POSITIVO, getTipoByOrdinal(0));
        assertEquals(TipoFeedback.CONSTRUTIVO, getTipoByOrdinal(1));
        assertNull(getTipoByOrdinal(2));
        assertNull(getTipoByOrdinal(-1));
    }

    @Test
    void getTipoByName_DeveRetornarCorreto() {
        // Act & Assert
        assertEquals(TipoFeedback.POSITIVO, getTipoByName("POSITIVO"));
        assertEquals(TipoFeedback.CONSTRUTIVO, getTipoByName("CONSTRUTIVO"));
        assertNull(getTipoByName("INVALIDO"));
        assertNull(getTipoByName(null));
    }

    @Test
    void getAllTipoNames_DeveRetornarCorreto() {
        // Act
        String[] names = getAllTipoNames();

        // Assert
        assertEquals(2, names.length);
        assertTrue(contains(names, "POSITIVO"));
        assertTrue(contains(names, "CONSTRUTIVO"));
    }

    @Test
    void getAllTipoOrdinals_DeveRetornarCorreto() {
        // Act
        int[] ordinals = getAllTipoOrdinals();

        // Assert
        assertEquals(2, ordinals.length);
        assertTrue(contains(ordinals, 0));
        assertTrue(contains(ordinals, 1));
    }

    @Test
    void getTipoDescription_DeveRetornarCorreto() {
        // Act & Assert
        assertEquals("Feedback positivo", getTipoDescription(TipoFeedback.POSITIVO));
        assertEquals("Feedback construtivo", getTipoDescription(TipoFeedback.CONSTRUTIVO));
    }

    @Test
    void getTipoColor_DeveRetornarCorreto() {
        // Act & Assert
        assertEquals("green", getTipoColor(TipoFeedback.POSITIVO));
        assertEquals("blue", getTipoColor(TipoFeedback.CONSTRUTIVO));
    }

    @Test
    void getTipoIcon_DeveRetornarCorreto() {
        // Act & Assert
        assertEquals("👍", getTipoIcon(TipoFeedback.POSITIVO));
        assertEquals("💡", getTipoIcon(TipoFeedback.CONSTRUTIVO));
    }

    @Test
    void isTipoValido_DeveRetornarCorreto() {
        // Act & Assert
        assertTrue(isTipoValido(TipoFeedback.POSITIVO));
        assertTrue(isTipoValido(TipoFeedback.CONSTRUTIVO));
        assertFalse(isTipoValido(null));
    }

    @Test
    void getTipoFromString_DeveRetornarCorreto() {
        // Act & Assert
        assertEquals(TipoFeedback.POSITIVO, getTipoFromString("POSITIVO"));
        assertEquals(TipoFeedback.CONSTRUTIVO, getTipoFromString("CONSTRUTIVO"));
        assertNull(getTipoFromString("INVALIDO"));
        assertNull(getTipoFromString(null));
    }

    @Test
    void getTipoFromOrdinal_DeveRetornarCorreto() {
        // Act & Assert
        assertEquals(TipoFeedback.POSITIVO, getTipoFromOrdinal(0));
        assertEquals(TipoFeedback.CONSTRUTIVO, getTipoFromOrdinal(1));
        assertNull(getTipoFromOrdinal(2));
        assertNull(getTipoFromOrdinal(-1));
    }

    @Test
    void getTipoIndex_DeveRetornarCorreto() {
        // Act & Assert
        assertEquals(0, getTipoIndex(TipoFeedback.POSITIVO));
        assertEquals(1, getTipoIndex(TipoFeedback.CONSTRUTIVO));
    }

    @Test
    void getTipoByIndex_DeveRetornarCorreto() {
        // Act & Assert
        assertEquals(TipoFeedback.POSITIVO, getTipoByIndex(0));
        assertEquals(TipoFeedback.CONSTRUTIVO, getTipoByIndex(1));
        assertNull(getTipoByIndex(2));
        assertNull(getTipoByIndex(-1));
    }

    // Métodos auxiliares
    private boolean contains(TipoFeedback[] array, TipoFeedback value) {
        for (TipoFeedback item : array) {
            if (item == value) {
                return true;
            }
        }
        return false;
    }

    private boolean contains(String[] array, String value) {
        for (String item : array) {
            if (item.equals(value)) {
                return true;
            }
        }
        return false;
    }

    private boolean contains(int[] array, int value) {
        for (int item : array) {
            if (item == value) {
                return true;
            }
        }
        return false;
    }

    private String getTipoDescription(TipoFeedback tipo) {
        switch (tipo) {
            case POSITIVO:
                return "Feedback positivo";
            case CONSTRUTIVO:
                return "Feedback construtivo";
            default:
                return "Desconhecido";
        }
    }

    private boolean isPositivo(TipoFeedback tipo) {
        return tipo == TipoFeedback.POSITIVO;
    }

    private boolean isConstrutivo(TipoFeedback tipo) {
        return tipo == TipoFeedback.CONSTRUTIVO;
    }

    private int getTipoCount() {
        return TipoFeedback.values().length;
    }

    private TipoFeedback getTipoByOrdinal(int ordinal) {
        TipoFeedback[] values = TipoFeedback.values();
        if (ordinal >= 0 && ordinal < values.length) {
            return values[ordinal];
        }
        return null;
    }

    private TipoFeedback getTipoByName(String name) {
        if (name == null) {
            return null;
        }
        try {
            return TipoFeedback.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private String[] getAllTipoNames() {
        return java.util.Arrays.stream(TipoFeedback.values())
                .map(TipoFeedback::name)
                .toArray(String[]::new);
    }

    private int[] getAllTipoOrdinals() {
        return java.util.Arrays.stream(TipoFeedback.values())
                .mapToInt(TipoFeedback::ordinal)
                .toArray();
    }

    private String getTipoColor(TipoFeedback tipo) {
        switch (tipo) {
            case POSITIVO:
                return "green";
            case CONSTRUTIVO:
                return "blue";
            default:
                return "gray";
        }
    }

    private String getTipoIcon(TipoFeedback tipo) {
        switch (tipo) {
            case POSITIVO:
                return "👍";
            case CONSTRUTIVO:
                return "💡";
            default:
                return "❓";
        }
    }

    private boolean isTipoValido(TipoFeedback tipo) {
        return tipo != null;
    }

    private TipoFeedback getTipoFromString(String name) {
        return getTipoByName(name);
    }

    private TipoFeedback getTipoFromOrdinal(int ordinal) {
        return getTipoByOrdinal(ordinal);
    }

    private int getTipoIndex(TipoFeedback tipo) {
        return tipo.ordinal();
    }

    private TipoFeedback getTipoByIndex(int index) {
        return getTipoByOrdinal(index);
    }
} 
package br.com.pointer.pointer_back.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusPDITest {

    @Test
    void values_DeveConterTodosOsStatus() {
        // Act
        StatusPDI[] values = StatusPDI.values();

        // Assert
        assertNotNull(values);
        assertEquals(3, values.length);
        assertTrue(contains(values, StatusPDI.EM_ANDAMENTO));
        assertTrue(contains(values, StatusPDI.CONCLUIDO));
        assertTrue(contains(values, StatusPDI.ATRASADO));
    }

    @Test
    void valueOf_ComStringValida_DeveRetornarEnum() {
        // Act & Assert
        assertEquals(StatusPDI.EM_ANDAMENTO, StatusPDI.valueOf("EM_ANDAMENTO"));
        assertEquals(StatusPDI.CONCLUIDO, StatusPDI.valueOf("CONCLUIDO"));
        assertEquals(StatusPDI.ATRASADO, StatusPDI.valueOf("ATRASADO"));
    }

    @Test
    void valueOf_ComStringInvalida_DeveLancarExcecao() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            StatusPDI.valueOf("INVALIDO");
        });
    }

    @Test
    void valueOf_ComStringNula_DeveLancarExcecao() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            StatusPDI.valueOf(null);
        });
    }

    @Test
    void toString_DeveRetornarNomeDoEnum() {
        // Act & Assert
        assertEquals("EM_ANDAMENTO", StatusPDI.EM_ANDAMENTO.toString());
        assertEquals("CONCLUIDO", StatusPDI.CONCLUIDO.toString());
        assertEquals("ATRASADO", StatusPDI.ATRASADO.toString());
    }

    @Test
    void name_DeveRetornarNomeDoEnum() {
        // Act & Assert
        assertEquals("EM_ANDAMENTO", StatusPDI.EM_ANDAMENTO.name());
        assertEquals("CONCLUIDO", StatusPDI.CONCLUIDO.name());
        assertEquals("ATRASADO", StatusPDI.ATRASADO.name());
    }

    @Test
    void ordinal_DeveRetornarPosicaoCorreta() {
        // Act & Assert
        assertEquals(0, StatusPDI.EM_ANDAMENTO.ordinal());
        assertEquals(1, StatusPDI.CONCLUIDO.ordinal());
        assertEquals(2, StatusPDI.ATRASADO.ordinal());
    }

    @Test
    void compareTo_DeveCompararCorretamente() {
        // Act & Assert
        assertTrue(StatusPDI.EM_ANDAMENTO.compareTo(StatusPDI.CONCLUIDO) < 0);
        assertTrue(StatusPDI.CONCLUIDO.compareTo(StatusPDI.ATRASADO) < 0);
        assertTrue(StatusPDI.ATRASADO.compareTo(StatusPDI.EM_ANDAMENTO) > 0);
        assertEquals(0, StatusPDI.EM_ANDAMENTO.compareTo(StatusPDI.EM_ANDAMENTO));
    }

    @Test
    void equals_DeveCompararCorretamente() {
        // Act & Assert
        assertTrue(StatusPDI.EM_ANDAMENTO.equals(StatusPDI.EM_ANDAMENTO));
        assertFalse(StatusPDI.EM_ANDAMENTO.equals(StatusPDI.CONCLUIDO));
        assertFalse(StatusPDI.EM_ANDAMENTO.equals(null));
        assertFalse(StatusPDI.EM_ANDAMENTO.equals("EM_ANDAMENTO"));
    }

    @Test
    void hashCode_DeveSerConsistente() {
        // Act & Assert
        assertEquals(StatusPDI.EM_ANDAMENTO.hashCode(), StatusPDI.EM_ANDAMENTO.hashCode());
        assertEquals(StatusPDI.CONCLUIDO.hashCode(), StatusPDI.CONCLUIDO.hashCode());
        assertEquals(StatusPDI.ATRASADO.hashCode(), StatusPDI.ATRASADO.hashCode());
    }

    @Test
    void emAndamento_DeveTerValorCorreto() {
        // Assert
        assertEquals("EM_ANDAMENTO", StatusPDI.EM_ANDAMENTO.name());
        assertEquals(0, StatusPDI.EM_ANDAMENTO.ordinal());
    }

    @Test
    void concluido_DeveTerValorCorreto() {
        // Assert
        assertEquals("CONCLUIDO", StatusPDI.CONCLUIDO.name());
        assertEquals(1, StatusPDI.CONCLUIDO.ordinal());
    }

    @Test
    void atrasado_DeveTerValorCorreto() {
        // Assert
        assertEquals("ATRASADO", StatusPDI.ATRASADO.name());
        assertEquals(2, StatusPDI.ATRASADO.ordinal());
    }

    @Test
    void switchStatement_DeveFuncionarCorretamente() {
        // Act & Assert
        assertEquals("Em andamento", getStatusDescription(StatusPDI.EM_ANDAMENTO));
        assertEquals("Concluído", getStatusDescription(StatusPDI.CONCLUIDO));
        assertEquals("Atrasado", getStatusDescription(StatusPDI.ATRASADO));
    }

    @Test
    void stream_DeveFuncionarCorretamente() {
        // Act
        long count = java.util.Arrays.stream(StatusPDI.values()).count();

        // Assert
        assertEquals(3, count);
    }

    @Test
    void filter_DeveFuncionarCorretamente() {
        // Act
        StatusPDI[] statusAtivos = java.util.Arrays.stream(StatusPDI.values())
                .filter(status -> status != StatusPDI.CONCLUIDO)
                .toArray(StatusPDI[]::new);

        // Assert
        assertEquals(2, statusAtivos.length);
        assertTrue(contains(statusAtivos, StatusPDI.EM_ANDAMENTO));
        assertTrue(contains(statusAtivos, StatusPDI.ATRASADO));
        assertFalse(contains(statusAtivos, StatusPDI.CONCLUIDO));
    }

    @Test
    void map_DeveFuncionarCorretamente() {
        // Act
        String[] nomes = java.util.Arrays.stream(StatusPDI.values())
                .map(StatusPDI::name)
                .toArray(String[]::new);

        // Assert
        assertEquals(3, nomes.length);
        assertTrue(contains(nomes, "EM_ANDAMENTO"));
        assertTrue(contains(nomes, "CONCLUIDO"));
        assertTrue(contains(nomes, "ATRASADO"));
    }

    @Test
    void collect_DeveFuncionarCorretamente() {
        // Act
        java.util.List<StatusPDI> statusList = java.util.Arrays.stream(StatusPDI.values())
                .collect(java.util.stream.Collectors.toList());

        // Assert
        assertEquals(3, statusList.size());
        assertTrue(statusList.contains(StatusPDI.EM_ANDAMENTO));
        assertTrue(statusList.contains(StatusPDI.CONCLUIDO));
        assertTrue(statusList.contains(StatusPDI.ATRASADO));
    }

    @Test
    void enumConstants_DeveSerUnicos() {
        // Act
        StatusPDI[] values = StatusPDI.values();

        // Assert
        long uniqueCount = java.util.Arrays.stream(values).distinct().count();
        assertEquals(values.length, uniqueCount);
    }

    @Test
    void enumNames_DeveSerUnicos() {
        // Act
        String[] names = java.util.Arrays.stream(StatusPDI.values())
                .map(StatusPDI::name)
                .toArray(String[]::new);

        // Assert
        long uniqueCount = java.util.Arrays.stream(names).distinct().count();
        assertEquals(names.length, uniqueCount);
    }

    @Test
    void enumOrdinals_DeveSerUnicos() {
        // Act
        int[] ordinals = java.util.Arrays.stream(StatusPDI.values())
                .mapToInt(StatusPDI::ordinal)
                .toArray();

        // Assert
        long uniqueCount = java.util.Arrays.stream(ordinals).distinct().count();
        assertEquals(ordinals.length, uniqueCount);
    }

    @Test
    void enumToString_DeveSerConsistente() {
        // Act & Assert
        assertEquals(StatusPDI.EM_ANDAMENTO.name(), StatusPDI.EM_ANDAMENTO.toString());
        assertEquals(StatusPDI.CONCLUIDO.name(), StatusPDI.CONCLUIDO.toString());
        assertEquals(StatusPDI.ATRASADO.name(), StatusPDI.ATRASADO.toString());
    }

    @Test
    void enumGetClass_DeveSerCorreto() {
        // Act & Assert
        assertEquals(StatusPDI.class, StatusPDI.EM_ANDAMENTO.getClass());
        assertEquals(StatusPDI.class, StatusPDI.CONCLUIDO.getClass());
        assertEquals(StatusPDI.class, StatusPDI.ATRASADO.getClass());
    }

    @Test
    void enumGetDeclaringClass_DeveSerCorreto() {
        // Act & Assert
        assertEquals(StatusPDI.class, StatusPDI.EM_ANDAMENTO.getDeclaringClass());
        assertEquals(StatusPDI.class, StatusPDI.CONCLUIDO.getDeclaringClass());
        assertEquals(StatusPDI.class, StatusPDI.ATRASADO.getDeclaringClass());
    }

    // Métodos auxiliares
    private boolean contains(StatusPDI[] array, StatusPDI value) {
        for (StatusPDI item : array) {
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

    private String getStatusDescription(StatusPDI status) {
        switch (status) {
            case EM_ANDAMENTO:
                return "Em andamento";
            case CONCLUIDO:
                return "Concluído";
            case ATRASADO:
                return "Atrasado";
            default:
                return "Desconhecido";
        }
    }
} 
package br.com.pointer.pointer_back.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusMarcoPDITest {

    @Test
    void values_DeveConterTodosOsStatus() {
        // Act
        StatusMarcoPDI[] values = StatusMarcoPDI.values();

        // Assert
        assertNotNull(values);
        assertEquals(2, values.length);
        assertTrue(contains(values, StatusMarcoPDI.PENDENTE));
        assertTrue(contains(values, StatusMarcoPDI.CONCLUIDO));
    }

    @Test
    void valueOf_ComStringValida_DeveRetornarEnum() {
        // Act & Assert
        assertEquals(StatusMarcoPDI.PENDENTE, StatusMarcoPDI.valueOf("PENDENTE"));
        assertEquals(StatusMarcoPDI.CONCLUIDO, StatusMarcoPDI.valueOf("CONCLUIDO"));
    }

    @Test
    void valueOf_ComStringInvalida_DeveLancarExcecao() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            StatusMarcoPDI.valueOf("INVALIDO");
        });
    }

    @Test
    void valueOf_ComStringNula_DeveLancarExcecao() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            StatusMarcoPDI.valueOf(null);
        });
    }

    @Test
    void toString_DeveRetornarNomeDoEnum() {
        // Act & Assert
        assertEquals("PENDENTE", StatusMarcoPDI.PENDENTE.toString());
        assertEquals("CONCLUIDO", StatusMarcoPDI.CONCLUIDO.toString());
    }

    @Test
    void name_DeveRetornarNomeDoEnum() {
        // Act & Assert
        assertEquals("PENDENTE", StatusMarcoPDI.PENDENTE.name());
        assertEquals("CONCLUIDO", StatusMarcoPDI.CONCLUIDO.name());
    }

    @Test
    void ordinal_DeveRetornarPosicaoCorreta() {
        // Act & Assert
        assertEquals(0, StatusMarcoPDI.PENDENTE.ordinal());
        assertEquals(1, StatusMarcoPDI.CONCLUIDO.ordinal());
    }

    @Test
    void compareTo_DeveCompararCorretamente() {
        // Act & Assert
        assertTrue(StatusMarcoPDI.PENDENTE.compareTo(StatusMarcoPDI.CONCLUIDO) < 0);
        assertTrue(StatusMarcoPDI.CONCLUIDO.compareTo(StatusMarcoPDI.PENDENTE) > 0);
        assertEquals(0, StatusMarcoPDI.PENDENTE.compareTo(StatusMarcoPDI.PENDENTE));
    }

    @Test
    void equals_DeveCompararCorretamente() {
        // Act & Assert
        assertTrue(StatusMarcoPDI.PENDENTE.equals(StatusMarcoPDI.PENDENTE));
        assertFalse(StatusMarcoPDI.PENDENTE.equals(StatusMarcoPDI.CONCLUIDO));
        assertFalse(StatusMarcoPDI.PENDENTE.equals(null));
        assertFalse(StatusMarcoPDI.PENDENTE.equals("PENDENTE"));
    }

    @Test
    void hashCode_DeveSerConsistente() {
        // Act & Assert
        assertEquals(StatusMarcoPDI.PENDENTE.hashCode(), StatusMarcoPDI.PENDENTE.hashCode());
        assertEquals(StatusMarcoPDI.CONCLUIDO.hashCode(), StatusMarcoPDI.CONCLUIDO.hashCode());
    }

    @Test
    void pendente_DeveTerValorCorreto() {
        // Assert
        assertEquals("PENDENTE", StatusMarcoPDI.PENDENTE.name());
        assertEquals(0, StatusMarcoPDI.PENDENTE.ordinal());
    }

    @Test
    void concluido_DeveTerValorCorreto() {
        // Assert
        assertEquals("CONCLUIDO", StatusMarcoPDI.CONCLUIDO.name());
        assertEquals(1, StatusMarcoPDI.CONCLUIDO.ordinal());
    }

    @Test
    void switchStatement_DeveFuncionarCorretamente() {
        // Act & Assert
        assertEquals("Pendente", getStatusDescription(StatusMarcoPDI.PENDENTE));
        assertEquals("Concluído", getStatusDescription(StatusMarcoPDI.CONCLUIDO));
    }

    @Test
    void stream_DeveFuncionarCorretamente() {
        // Act
        long count = java.util.Arrays.stream(StatusMarcoPDI.values()).count();

        // Assert
        assertEquals(2, count);
    }

    @Test
    void filter_DeveFuncionarCorretamente() {
        // Act
        StatusMarcoPDI[] statusPendentes = java.util.Arrays.stream(StatusMarcoPDI.values())
                .filter(status -> status == StatusMarcoPDI.PENDENTE)
                .toArray(StatusMarcoPDI[]::new);

        // Assert
        assertEquals(1, statusPendentes.length);
        assertTrue(contains(statusPendentes, StatusMarcoPDI.PENDENTE));
        assertFalse(contains(statusPendentes, StatusMarcoPDI.CONCLUIDO));
    }

    @Test
    void map_DeveFuncionarCorretamente() {
        // Act
        String[] nomes = java.util.Arrays.stream(StatusMarcoPDI.values())
                .map(StatusMarcoPDI::name)
                .toArray(String[]::new);

        // Assert
        assertEquals(2, nomes.length);
        assertTrue(contains(nomes, "PENDENTE"));
        assertTrue(contains(nomes, "CONCLUIDO"));
    }

    @Test
    void collect_DeveFuncionarCorretamente() {
        // Act
        java.util.List<StatusMarcoPDI> statusList = java.util.Arrays.stream(StatusMarcoPDI.values())
                .collect(java.util.stream.Collectors.toList());

        // Assert
        assertEquals(2, statusList.size());
        assertTrue(statusList.contains(StatusMarcoPDI.PENDENTE));
        assertTrue(statusList.contains(StatusMarcoPDI.CONCLUIDO));
    }

    @Test
    void enumConstants_DeveSerUnicos() {
        // Act
        StatusMarcoPDI[] values = StatusMarcoPDI.values();

        // Assert
        long uniqueCount = java.util.Arrays.stream(values).distinct().count();
        assertEquals(values.length, uniqueCount);
    }

    @Test
    void enumNames_DeveSerUnicos() {
        // Act
        String[] names = java.util.Arrays.stream(StatusMarcoPDI.values())
                .map(StatusMarcoPDI::name)
                .toArray(String[]::new);

        // Assert
        long uniqueCount = java.util.Arrays.stream(names).distinct().count();
        assertEquals(names.length, uniqueCount);
    }

    @Test
    void enumOrdinals_DeveSerUnicos() {
        // Act
        int[] ordinals = java.util.Arrays.stream(StatusMarcoPDI.values())
                .mapToInt(StatusMarcoPDI::ordinal)
                .toArray();

        // Assert
        long uniqueCount = java.util.Arrays.stream(ordinals).distinct().count();
        assertEquals(ordinals.length, uniqueCount);
    }

    @Test
    void enumToString_DeveSerConsistente() {
        // Act & Assert
        assertEquals(StatusMarcoPDI.PENDENTE.name(), StatusMarcoPDI.PENDENTE.toString());
        assertEquals(StatusMarcoPDI.CONCLUIDO.name(), StatusMarcoPDI.CONCLUIDO.toString());
    }

    @Test
    void enumGetClass_DeveSerCorreto() {
        // Act & Assert
        assertEquals(StatusMarcoPDI.class, StatusMarcoPDI.PENDENTE.getClass());
        assertEquals(StatusMarcoPDI.class, StatusMarcoPDI.CONCLUIDO.getClass());
    }

    @Test
    void enumGetDeclaringClass_DeveSerCorreto() {
        // Act & Assert
        assertEquals(StatusMarcoPDI.class, StatusMarcoPDI.PENDENTE.getDeclaringClass());
        assertEquals(StatusMarcoPDI.class, StatusMarcoPDI.CONCLUIDO.getDeclaringClass());
    }

    @Test
    void isPendente_DeveRetornarCorreto() {
        // Act & Assert
        assertTrue(isPendente(StatusMarcoPDI.PENDENTE));
        assertFalse(isPendente(StatusMarcoPDI.CONCLUIDO));
    }

    @Test
    void isConcluido_DeveRetornarCorreto() {
        // Act & Assert
        assertTrue(isConcluido(StatusMarcoPDI.CONCLUIDO));
        assertFalse(isConcluido(StatusMarcoPDI.PENDENTE));
    }

    @Test
    void canTransition_DeveRetornarCorreto() {
        // Act & Assert
        assertTrue(canTransition(StatusMarcoPDI.PENDENTE, StatusMarcoPDI.CONCLUIDO));
        assertFalse(canTransition(StatusMarcoPDI.CONCLUIDO, StatusMarcoPDI.PENDENTE));
        assertFalse(canTransition(StatusMarcoPDI.PENDENTE, StatusMarcoPDI.PENDENTE));
    }

    @Test
    void getNextStatus_DeveRetornarCorreto() {
        // Act & Assert
        assertEquals(StatusMarcoPDI.CONCLUIDO, getNextStatus(StatusMarcoPDI.PENDENTE));
        assertNull(getNextStatus(StatusMarcoPDI.CONCLUIDO));
    }

    @Test
    void getPreviousStatus_DeveRetornarCorreto() {
        // Act & Assert
        assertEquals(StatusMarcoPDI.PENDENTE, getPreviousStatus(StatusMarcoPDI.CONCLUIDO));
        assertNull(getPreviousStatus(StatusMarcoPDI.PENDENTE));
    }

    @Test
    void getStatusCount_DeveRetornarCorreto() {
        // Act & Assert
        assertEquals(2, getStatusCount());
    }

    @Test
    void getStatusByOrdinal_DeveRetornarCorreto() {
        // Act & Assert
        assertEquals(StatusMarcoPDI.PENDENTE, getStatusByOrdinal(0));
        assertEquals(StatusMarcoPDI.CONCLUIDO, getStatusByOrdinal(1));
        assertNull(getStatusByOrdinal(2));
        assertNull(getStatusByOrdinal(-1));
    }

    @Test
    void getStatusByName_DeveRetornarCorreto() {
        // Act & Assert
        assertEquals(StatusMarcoPDI.PENDENTE, getStatusByName("PENDENTE"));
        assertEquals(StatusMarcoPDI.CONCLUIDO, getStatusByName("CONCLUIDO"));
        assertNull(getStatusByName("INVALIDO"));
        assertNull(getStatusByName(null));
    }

    @Test
    void getAllStatusNames_DeveRetornarCorreto() {
        // Act
        String[] names = getAllStatusNames();

        // Assert
        assertEquals(2, names.length);
        assertTrue(contains(names, "PENDENTE"));
        assertTrue(contains(names, "CONCLUIDO"));
    }

    @Test
    void getAllStatusOrdinals_DeveRetornarCorreto() {
        // Act
        int[] ordinals = getAllStatusOrdinals();

        // Assert
        assertEquals(2, ordinals.length);
        assertTrue(contains(ordinals, 0));
        assertTrue(contains(ordinals, 1));
    }

    // Métodos auxiliares
    private boolean contains(StatusMarcoPDI[] array, StatusMarcoPDI value) {
        for (StatusMarcoPDI item : array) {
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

    private String getStatusDescription(StatusMarcoPDI status) {
        switch (status) {
            case PENDENTE:
                return "Pendente";
            case CONCLUIDO:
                return "Concluído";
            default:
                return "Desconhecido";
        }
    }

    private boolean isPendente(StatusMarcoPDI status) {
        return status == StatusMarcoPDI.PENDENTE;
    }

    private boolean isConcluido(StatusMarcoPDI status) {
        return status == StatusMarcoPDI.CONCLUIDO;
    }

    private boolean canTransition(StatusMarcoPDI from, StatusMarcoPDI to) {
        if (from == StatusMarcoPDI.PENDENTE && to == StatusMarcoPDI.CONCLUIDO) {
            return true;
        }
        return false;
    }

    private StatusMarcoPDI getNextStatus(StatusMarcoPDI current) {
        switch (current) {
            case PENDENTE:
                return StatusMarcoPDI.CONCLUIDO;
            case CONCLUIDO:
                return null;
            default:
                return null;
        }
    }

    private StatusMarcoPDI getPreviousStatus(StatusMarcoPDI current) {
        switch (current) {
            case PENDENTE:
                return null;
            case CONCLUIDO:
                return StatusMarcoPDI.PENDENTE;
            default:
                return null;
        }
    }

    private int getStatusCount() {
        return StatusMarcoPDI.values().length;
    }

    private StatusMarcoPDI getStatusByOrdinal(int ordinal) {
        StatusMarcoPDI[] values = StatusMarcoPDI.values();
        if (ordinal >= 0 && ordinal < values.length) {
            return values[ordinal];
        }
        return null;
    }

    private StatusMarcoPDI getStatusByName(String name) {
        if (name == null) {
            return null;
        }
        try {
            return StatusMarcoPDI.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private String[] getAllStatusNames() {
        return java.util.Arrays.stream(StatusMarcoPDI.values())
                .map(StatusMarcoPDI::name)
                .toArray(String[]::new);
    }

    private int[] getAllStatusOrdinals() {
        return java.util.Arrays.stream(StatusMarcoPDI.values())
                .mapToInt(StatusMarcoPDI::ordinal)
                .toArray();
    }
} 
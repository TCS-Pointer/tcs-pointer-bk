package br.com.pointer.pointer_back.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusUsuarioTest {

    @Test
    void values_DeveConterTodosOsStatus() {
        // Act
        StatusUsuario[] values = StatusUsuario.values();

        // Assert
        assertNotNull(values);
        assertEquals(2, values.length);
        assertTrue(contains(values, StatusUsuario.ATIVO));
        assertTrue(contains(values, StatusUsuario.INATIVO));
    }

    @Test
    void valueOf_ComStringValida_DeveRetornarEnum() {
        // Act & Assert
        assertEquals(StatusUsuario.ATIVO, StatusUsuario.valueOf("ATIVO"));
        assertEquals(StatusUsuario.INATIVO, StatusUsuario.valueOf("INATIVO"));
    }

    @Test
    void valueOf_ComStringInvalida_DeveLancarExcecao() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            StatusUsuario.valueOf("INVALIDO");
        });
    }

    @Test
    void valueOf_ComStringNula_DeveLancarExcecao() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            StatusUsuario.valueOf(null);
        });
    }

    @Test
    void toString_DeveRetornarNomeDoEnum() {
        // Act & Assert
        assertEquals("ATIVO", StatusUsuario.ATIVO.toString());
        assertEquals("INATIVO", StatusUsuario.INATIVO.toString());
    }

    @Test
    void name_DeveRetornarNomeDoEnum() {
        // Act & Assert
        assertEquals("ATIVO", StatusUsuario.ATIVO.name());
        assertEquals("INATIVO", StatusUsuario.INATIVO.name());
    }

    @Test
    void ordinal_DeveRetornarPosicaoCorreta() {
        // Act & Assert
        assertEquals(0, StatusUsuario.ATIVO.ordinal());
        assertEquals(1, StatusUsuario.INATIVO.ordinal());
    }

    @Test
    void compareTo_DeveCompararCorretamente() {
        // Act & Assert
        assertTrue(StatusUsuario.ATIVO.compareTo(StatusUsuario.INATIVO) < 0);
        assertTrue(StatusUsuario.INATIVO.compareTo(StatusUsuario.ATIVO) > 0);
        assertEquals(0, StatusUsuario.ATIVO.compareTo(StatusUsuario.ATIVO));
    }

    @Test
    void equals_DeveCompararCorretamente() {
        // Act & Assert
        assertTrue(StatusUsuario.ATIVO.equals(StatusUsuario.ATIVO));
        assertFalse(StatusUsuario.ATIVO.equals(StatusUsuario.INATIVO));
        assertFalse(StatusUsuario.ATIVO.equals(null));
        assertFalse(StatusUsuario.ATIVO.equals("ATIVO"));
    }

    @Test
    void hashCode_DeveSerConsistente() {
        // Act & Assert
        assertEquals(StatusUsuario.ATIVO.hashCode(), StatusUsuario.ATIVO.hashCode());
        assertEquals(StatusUsuario.INATIVO.hashCode(), StatusUsuario.INATIVO.hashCode());
    }

    @Test
    void ativo_DeveTerValorCorreto() {
        // Assert
        assertEquals("ATIVO", StatusUsuario.ATIVO.name());
        assertEquals(0, StatusUsuario.ATIVO.ordinal());
    }

    @Test
    void inativo_DeveTerValorCorreto() {
        // Assert
        assertEquals("INATIVO", StatusUsuario.INATIVO.name());
        assertEquals(1, StatusUsuario.INATIVO.ordinal());
    }

    @Test
    void switchStatement_DeveFuncionarCorretamente() {
        // Act & Assert
        assertEquals("Usuário ativo", getStatusDescription(StatusUsuario.ATIVO));
        assertEquals("Usuário inativo", getStatusDescription(StatusUsuario.INATIVO));
    }

    @Test
    void stream_DeveFuncionarCorretamente() {
        // Act
        long count = java.util.Arrays.stream(StatusUsuario.values()).count();

        // Assert
        assertEquals(2, count);
    }

    @Test
    void filter_DeveFuncionarCorretamente() {
        // Act
        StatusUsuario[] statusAtivos = java.util.Arrays.stream(StatusUsuario.values())
                .filter(status -> status == StatusUsuario.ATIVO)
                .toArray(StatusUsuario[]::new);

        // Assert
        assertEquals(1, statusAtivos.length);
        assertTrue(contains(statusAtivos, StatusUsuario.ATIVO));
        assertFalse(contains(statusAtivos, StatusUsuario.INATIVO));
    }

    @Test
    void map_DeveFuncionarCorretamente() {
        // Act
        String[] nomes = java.util.Arrays.stream(StatusUsuario.values())
                .map(StatusUsuario::name)
                .toArray(String[]::new);

        // Assert
        assertEquals(2, nomes.length);
        assertTrue(contains(nomes, "ATIVO"));
        assertTrue(contains(nomes, "INATIVO"));
    }

    @Test
    void collect_DeveFuncionarCorretamente() {
        // Act
        java.util.List<StatusUsuario> statusList = java.util.Arrays.stream(StatusUsuario.values())
                .collect(java.util.stream.Collectors.toList());

        // Assert
        assertEquals(2, statusList.size());
        assertTrue(statusList.contains(StatusUsuario.ATIVO));
        assertTrue(statusList.contains(StatusUsuario.INATIVO));
    }

    @Test
    void enumConstants_DeveSerUnicos() {
        // Act
        StatusUsuario[] values = StatusUsuario.values();

        // Assert
        long uniqueCount = java.util.Arrays.stream(values).distinct().count();
        assertEquals(values.length, uniqueCount);
    }

    @Test
    void enumNames_DeveSerUnicos() {
        // Act
        String[] names = java.util.Arrays.stream(StatusUsuario.values())
                .map(StatusUsuario::name)
                .toArray(String[]::new);

        // Assert
        long uniqueCount = java.util.Arrays.stream(names).distinct().count();
        assertEquals(names.length, uniqueCount);
    }

    @Test
    void enumOrdinals_DeveSerUnicos() {
        // Act
        int[] ordinals = java.util.Arrays.stream(StatusUsuario.values())
                .mapToInt(StatusUsuario::ordinal)
                .toArray();

        // Assert
        long uniqueCount = java.util.Arrays.stream(ordinals).distinct().count();
        assertEquals(ordinals.length, uniqueCount);
    }

    @Test
    void enumToString_DeveSerConsistente() {
        // Act & Assert
        assertEquals(StatusUsuario.ATIVO.name(), StatusUsuario.ATIVO.toString());
        assertEquals(StatusUsuario.INATIVO.name(), StatusUsuario.INATIVO.toString());
    }

    @Test
    void enumGetClass_DeveSerCorreto() {
        // Act & Assert
        assertEquals(StatusUsuario.class, StatusUsuario.ATIVO.getClass());
        assertEquals(StatusUsuario.class, StatusUsuario.INATIVO.getClass());
    }

    @Test
    void enumGetDeclaringClass_DeveSerCorreto() {
        // Act & Assert
        assertEquals(StatusUsuario.class, StatusUsuario.ATIVO.getDeclaringClass());
        assertEquals(StatusUsuario.class, StatusUsuario.INATIVO.getDeclaringClass());
    }

    @Test
    void isAtivo_DeveRetornarCorreto() {
        // Act & Assert
        assertTrue(isAtivo(StatusUsuario.ATIVO));
        assertFalse(isAtivo(StatusUsuario.INATIVO));
    }

    @Test
    void isInativo_DeveRetornarCorreto() {
        // Act & Assert
        assertTrue(isInativo(StatusUsuario.INATIVO));
        assertFalse(isInativo(StatusUsuario.ATIVO));
    }

    @Test
    void canTransition_DeveRetornarCorreto() {
        // Act & Assert
        assertTrue(canTransition(StatusUsuario.ATIVO, StatusUsuario.INATIVO));
        assertTrue(canTransition(StatusUsuario.INATIVO, StatusUsuario.ATIVO));
        assertFalse(canTransition(StatusUsuario.ATIVO, StatusUsuario.ATIVO));
    }

    @Test
    void getStatusCount_DeveRetornarCorreto() {
        // Act & Assert
        assertEquals(2, getStatusCount());
    }

    @Test
    void getStatusByOrdinal_DeveRetornarCorreto() {
        // Act & Assert
        assertEquals(StatusUsuario.ATIVO, getStatusByOrdinal(0));
        assertEquals(StatusUsuario.INATIVO, getStatusByOrdinal(1));
        assertNull(getStatusByOrdinal(2));
        assertNull(getStatusByOrdinal(-1));
    }

    @Test
    void getStatusByName_DeveRetornarCorreto() {
        // Act & Assert
        assertEquals(StatusUsuario.ATIVO, getStatusByName("ATIVO"));
        assertEquals(StatusUsuario.INATIVO, getStatusByName("INATIVO"));
        assertNull(getStatusByName("INVALIDO"));
        assertNull(getStatusByName(null));
    }

    @Test
    void getAllStatusNames_DeveRetornarCorreto() {
        // Act
        String[] names = getAllStatusNames();

        // Assert
        assertEquals(2, names.length);
        assertTrue(contains(names, "ATIVO"));
        assertTrue(contains(names, "INATIVO"));
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

    @Test
    void getStatusDescription_DeveRetornarCorreto() {
        // Act & Assert
        assertEquals("Usuário ativo", getStatusDescription(StatusUsuario.ATIVO));
        assertEquals("Usuário inativo", getStatusDescription(StatusUsuario.INATIVO));
    }

    @Test
    void getStatusColor_DeveRetornarCorreto() {
        // Act & Assert
        assertEquals("green", getStatusColor(StatusUsuario.ATIVO));
        assertEquals("red", getStatusColor(StatusUsuario.INATIVO));
    }

    @Test
    void getStatusIcon_DeveRetornarCorreto() {
        // Act & Assert
        assertEquals("✅", getStatusIcon(StatusUsuario.ATIVO));
        assertEquals("❌", getStatusIcon(StatusUsuario.INATIVO));
    }

    @Test
    void isStatusValido_DeveRetornarCorreto() {
        // Act & Assert
        assertTrue(isStatusValido(StatusUsuario.ATIVO));
        assertTrue(isStatusValido(StatusUsuario.INATIVO));
        assertFalse(isStatusValido(null));
    }

    @Test
    void getStatusFromString_DeveRetornarCorreto() {
        // Act & Assert
        assertEquals(StatusUsuario.ATIVO, getStatusFromString("ATIVO"));
        assertEquals(StatusUsuario.INATIVO, getStatusFromString("INATIVO"));
        assertNull(getStatusFromString("INVALIDO"));
        assertNull(getStatusFromString(null));
    }

    @Test
    void getStatusFromOrdinal_DeveRetornarCorreto() {
        // Act & Assert
        assertEquals(StatusUsuario.ATIVO, getStatusFromOrdinal(0));
        assertEquals(StatusUsuario.INATIVO, getStatusFromOrdinal(1));
        assertNull(getStatusFromOrdinal(2));
        assertNull(getStatusFromOrdinal(-1));
    }

    @Test
    void getStatusIndex_DeveRetornarCorreto() {
        // Act & Assert
        assertEquals(0, getStatusIndex(StatusUsuario.ATIVO));
        assertEquals(1, getStatusIndex(StatusUsuario.INATIVO));
    }

    @Test
    void getStatusByIndex_DeveRetornarCorreto() {
        // Act & Assert
        assertEquals(StatusUsuario.ATIVO, getStatusByIndex(0));
        assertEquals(StatusUsuario.INATIVO, getStatusByIndex(1));
        assertNull(getStatusByIndex(2));
        assertNull(getStatusByIndex(-1));
    }

    @Test
    void isDefaultStatus_DeveRetornarCorreto() {
        // Act & Assert
        assertTrue(isDefaultStatus(StatusUsuario.ATIVO));
        assertFalse(isDefaultStatus(StatusUsuario.INATIVO));
    }

    @Test
    void getOppositeStatus_DeveRetornarCorreto() {
        // Act & Assert
        assertEquals(StatusUsuario.INATIVO, getOppositeStatus(StatusUsuario.ATIVO));
        assertEquals(StatusUsuario.ATIVO, getOppositeStatus(StatusUsuario.INATIVO));
    }

    // Métodos auxiliares
    private boolean contains(StatusUsuario[] array, StatusUsuario value) {
        for (StatusUsuario item : array) {
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

    private String getStatusDescription(StatusUsuario status) {
        switch (status) {
            case ATIVO:
                return "Usuário ativo";
            case INATIVO:
                return "Usuário inativo";
            default:
                return "Desconhecido";
        }
    }

    private boolean isAtivo(StatusUsuario status) {
        return status == StatusUsuario.ATIVO;
    }

    private boolean isInativo(StatusUsuario status) {
        return status == StatusUsuario.INATIVO;
    }

    private boolean canTransition(StatusUsuario from, StatusUsuario to) {
        return from != to;
    }

    private int getStatusCount() {
        return StatusUsuario.values().length;
    }

    private StatusUsuario getStatusByOrdinal(int ordinal) {
        StatusUsuario[] values = StatusUsuario.values();
        if (ordinal >= 0 && ordinal < values.length) {
            return values[ordinal];
        }
        return null;
    }

    private StatusUsuario getStatusByName(String name) {
        if (name == null) {
            return null;
        }
        try {
            return StatusUsuario.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private String[] getAllStatusNames() {
        return java.util.Arrays.stream(StatusUsuario.values())
                .map(StatusUsuario::name)
                .toArray(String[]::new);
    }

    private int[] getAllStatusOrdinals() {
        return java.util.Arrays.stream(StatusUsuario.values())
                .mapToInt(StatusUsuario::ordinal)
                .toArray();
    }

    private String getStatusColor(StatusUsuario status) {
        switch (status) {
            case ATIVO:
                return "green";
            case INATIVO:
                return "red";
            default:
                return "gray";
        }
    }

    private String getStatusIcon(StatusUsuario status) {
        switch (status) {
            case ATIVO:
                return "✅";
            case INATIVO:
                return "❌";
            default:
                return "❓";
        }
    }

    private boolean isStatusValido(StatusUsuario status) {
        return status != null;
    }

    private StatusUsuario getStatusFromString(String name) {
        return getStatusByName(name);
    }

    private StatusUsuario getStatusFromOrdinal(int ordinal) {
        return getStatusByOrdinal(ordinal);
    }

    private int getStatusIndex(StatusUsuario status) {
        return status.ordinal();
    }

    private StatusUsuario getStatusByIndex(int index) {
        return getStatusByOrdinal(index);
    }

    private boolean isDefaultStatus(StatusUsuario status) {
        return status == StatusUsuario.ATIVO;
    }

    private StatusUsuario getOppositeStatus(StatusUsuario status) {
        switch (status) {
            case ATIVO:
                return StatusUsuario.INATIVO;
            case INATIVO:
                return StatusUsuario.ATIVO;
            default:
                return null;
        }
    }
} 
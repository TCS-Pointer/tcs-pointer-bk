package br.com.pointer.pointer_back.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TwoFactorStatusDTOTest {

    private TwoFactorStatusDTO status;

    @BeforeEach
    void setUp() {
        status = new TwoFactorStatusDTO();
    }

    @Test
    @DisplayName("Deve criar TwoFactorStatusDTO com valores padrão")
    void deveCriarComValoresPadrao() {
        assertNotNull(status);
    }

    @Test
    @DisplayName("Deve testar equals e hashCode com objetos iguais")
    void deveTestarEqualsEHashCodeComObjetosIguais() {
        TwoFactorStatusDTO status1 = new TwoFactorStatusDTO();
        TwoFactorStatusDTO status2 = new TwoFactorStatusDTO();
        assertEquals(status1, status2);
        assertEquals(status1.hashCode(), status2.hashCode());
    }

    @Test
    @DisplayName("Deve testar toString")
    void deveTestarToString() {
        String result = status.toString();
        assertNotNull(result);
    }
} 
package br.com.pointer.pointer_back.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("TwoFactorVerifyDTO")
class TwoFactorVerifyDTOTest {

    private TwoFactorVerifyDTO verify;

    @BeforeEach
    void setUp() {
        verify = new TwoFactorVerifyDTO();
    }

    @Test
    @DisplayName("Deve criar TwoFactorVerifyDTO com valores padrão")
    void deveCriarComValoresPadrao() {
        assertNotNull(verify);
    }

    @Test
    @DisplayName("Deve testar equals e hashCode com objetos iguais")
    void deveTestarEqualsEHashCodeComObjetosIguais() {
        TwoFactorVerifyDTO verify1 = new TwoFactorVerifyDTO();
        TwoFactorVerifyDTO verify2 = new TwoFactorVerifyDTO();
        assertEquals(verify1, verify2);
        assertEquals(verify1.hashCode(), verify2.hashCode());
    }

    @Test
    @DisplayName("Deve testar toString")
    void deveTestarToString() {
        String result = verify.toString();
        assertNotNull(result);
    }
} 
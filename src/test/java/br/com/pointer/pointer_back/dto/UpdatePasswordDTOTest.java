package br.com.pointer.pointer_back.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UpdatePasswordDTOTest {

    private UpdatePasswordDTO updatePassword;

    @BeforeEach
    void setUp() {
        updatePassword = new UpdatePasswordDTO();
    }

    @Test
    @DisplayName("Deve criar UpdatePasswordDTO com valores padrão")
    void deveCriarComValoresPadrao() {
        assertNotNull(updatePassword);
    }

    @Test
    @DisplayName("Deve testar equals e hashCode com objetos iguais")
    void deveTestarEqualsEHashCodeComObjetosIguais() {
        UpdatePasswordDTO dto1 = new UpdatePasswordDTO();
        UpdatePasswordDTO dto2 = new UpdatePasswordDTO();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    @DisplayName("Deve testar toString")
    void deveTestarToString() {
        String result = updatePassword.toString();
        assertNotNull(result);
    }
} 
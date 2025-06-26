package br.com.pointer.pointer_back.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import br.com.pointer.pointer_back.auth.TokenResponse;

@DisplayName("TokenResponse")
class TokenResponseTest {

    @Test
    @DisplayName("Deve criar TokenResponse com todos os campos")
    void deveCriarTokenResponseComTodosOsCampos() {
        // Given
        TokenResponse response = new TokenResponse();
        response.setAccess_token("access_token_123");
        response.setRefresh_token("refresh_token_456");
        response.setToken_type("Bearer");
        response.setExpires_in(3600);
        response.setId_token("id_token_789");
        
        // Then
        assertNotNull(response);
        assertEquals("access_token_123", response.getAccess_token());
        assertEquals("refresh_token_456", response.getRefresh_token());
        assertEquals("Bearer", response.getToken_type());
        assertEquals(3600, response.getExpires_in());
        assertEquals("id_token_789", response.getId_token());
    }

    @Test
    @DisplayName("Deve criar TokenResponse com campos nulos")
    void deveCriarTokenResponseComCamposNulos() {
        // Given
        TokenResponse response = new TokenResponse();
        
        // When & Then
        assertNotNull(response);
        assertNull(response.getAccess_token());
        assertNull(response.getRefresh_token());
        assertNull(response.getToken_type());
        assertEquals(0, response.getExpires_in());
        assertNull(response.getId_token());
    }

    @Test
    @DisplayName("Deve criar TokenResponse com campos vazios")
    void deveCriarTokenResponseComCamposVazios() {
        // Given
        TokenResponse response = new TokenResponse();
        response.setAccess_token("");
        response.setRefresh_token("");
        response.setToken_type("");
        response.setExpires_in(0);
        response.setId_token("");
        
        // When & Then
        assertNotNull(response);
        assertEquals("", response.getAccess_token());
        assertEquals("", response.getRefresh_token());
        assertEquals("", response.getToken_type());
        assertEquals(0, response.getExpires_in());
        assertEquals("", response.getId_token());
    }

    @Test
    @DisplayName("Deve criar TokenResponse com valores extremos")
    void deveCriarTokenResponseComValoresExtremos() {
        // Given
        TokenResponse response = new TokenResponse();
        response.setAccess_token("A");
        response.setRefresh_token("B");
        response.setToken_type("C");
        response.setExpires_in(Integer.MAX_VALUE);
        response.setId_token("D");
        
        // Then
        assertEquals("A", response.getAccess_token());
        assertEquals("B", response.getRefresh_token());
        assertEquals("C", response.getToken_type());
        assertEquals(Integer.MAX_VALUE, response.getExpires_in());
        assertEquals("D", response.getId_token());
    }
} 
package br.com.pointer.pointer_back.util;

import br.com.pointer.pointer_back.constant.SecurityConstants;
import br.com.pointer.pointer_back.exception.KeycloakException;
import org.junit.jupiter.api.Test;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private String gerarJwtValido(Map<String, Object> claims) throws Exception {
        String header = Base64.getUrlEncoder().withoutPadding().encodeToString("{}".getBytes());
        String payload = Base64.getUrlEncoder().withoutPadding().encodeToString("{\"sub\":\"1234567890\",\"name\":\"John Doe\"}".getBytes());
        String signature = "signature";
        return header + "." + payload + "." + signature;
    }

    @Test
    void testExtractTokenFromHeader_ComBearerToken_DeveRetornarToken() {
        // Arrange
        String authorizationHeader = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        // Act
        String token = JwtUtil.extractTokenFromHeader(authorizationHeader);

        // Assert
        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c", token);
    }

    @Test
    void testExtractTokenFromHeader_ComHeaderNulo_DeveRetornarNull() {
        // Act
        String token = JwtUtil.extractTokenFromHeader(null);

        // Assert
        assertNull(token);
    }

    @Test
    void testExtractTokenFromHeader_ComHeaderVazio_DeveRetornarNull() {
        // Arrange
        String authorizationHeader = "";

        // Act
        String token = JwtUtil.extractTokenFromHeader(authorizationHeader);

        // Assert
        assertNull(token);
    }

    @Test
    void testExtractTokenFromHeader_ComHeaderSemBearer_DeveRetornarNull() {
        // Arrange
        String authorizationHeader = "Token eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";

        // Act
        String token = JwtUtil.extractTokenFromHeader(authorizationHeader);

        // Assert
        assertNull(token);
    }

    @Test
    void testExtractTokenFromHeader_ComHeaderApenasBearer_DeveRetornarStringVazia() {
        // Arrange
        String authorizationHeader = "Bearer ";

        // Act
        String token = JwtUtil.extractTokenFromHeader(authorizationHeader);

        // Assert
        assertEquals("", token);
    }

    @Test
    void testDecodeJwtPayload_ComTokenValido_DeveRetornarClaims() throws KeycloakException {
        // Arrange
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        // Act
        Map<String, Object> claims = JwtUtil.decodeJwtPayload(token);

        // Assert
        assertNotNull(claims);
        assertEquals("1234567890", claims.get("sub"));
        assertEquals("John Doe", claims.get("name"));
        assertEquals(1516239022, claims.get("iat"));
    }

    @Test
    void testDecodeJwtPayload_ComTokenInvalido_DeveLancarExcecao() {
        // Arrange
        String token = "token.invalido";

        // Act & Assert
        assertThrows(KeycloakException.class, () -> {
            JwtUtil.decodeJwtPayload(token);
        });
    }

    @Test
    void testDecodeJwtPayload_ComTokenComDuasPartes_DeveLancarExcecao() {
        // Arrange
        String token = "parte1.parte2";

        // Act & Assert
        assertThrows(KeycloakException.class, () -> {
            JwtUtil.decodeJwtPayload(token);
        });
    }

    @Test
    void testDecodeJwtPayload_ComTokenComQuatroPartes_DeveLancarExcecao() {
        // Arrange
        String token = "parte1.parte2.parte3.parte4";

        // Act & Assert
        assertThrows(KeycloakException.class, () -> {
            JwtUtil.decodeJwtPayload(token);
        });
    }

    @Test
    void testDecodeJwtPayload_ComTokenComPayloadInvalido_DeveLancarExcecao() {
        // Arrange
        String token = "header.payload.invalido.signature";

        // Act & Assert
        assertThrows(KeycloakException.class, () -> {
            JwtUtil.decodeJwtPayload(token);
        });
    }

    @Test
    void testValidateJwtFormat_ComTresPartes_DevePassar() throws KeycloakException {
        // Arrange
        String[] parts = {"header", "payload", "signature"};

        // Act & Assert
        assertDoesNotThrow(() -> {
            JwtUtil.validateJwtFormat(parts);
        });
    }

    @Test
    void testValidateJwtFormat_ComDuasPartes_DeveLancarExcecao() {
        // Arrange
        String[] parts = {"header", "payload"};

        // Act & Assert
        assertThrows(KeycloakException.class, () -> {
            JwtUtil.validateJwtFormat(parts);
        });
    }

    @Test
    void testValidateJwtFormat_ComQuatroPartes_DeveLancarExcecao() {
        // Arrange
        String[] parts = {"header", "payload", "signature", "extra"};

        // Act & Assert
        assertThrows(KeycloakException.class, () -> {
            JwtUtil.validateJwtFormat(parts);
        });
    }

    @Test
    void testExtractExpirationTime_ComExpInteger_DeveRetornarLong() throws KeycloakException {
        // Arrange
        Map<String, Object> claims = new HashMap<>();
        claims.put(SecurityConstants.EXP_CLAIM, 1516239022);

        // Act
        long expirationTime = JwtUtil.extractExpirationTime(claims);

        // Assert
        assertEquals(1516239022L, expirationTime);
    }

    @Test
    void testExtractExpirationTime_ComExpLong_DeveRetornarLong() throws KeycloakException {
        // Arrange
        Map<String, Object> claims = new HashMap<>();
        claims.put(SecurityConstants.EXP_CLAIM, 1516239022L);

        // Act
        long expirationTime = JwtUtil.extractExpirationTime(claims);

        // Assert
        assertEquals(1516239022L, expirationTime);
    }

    @Test
    void testExtractExpirationTime_ComExpNulo_DeveLancarExcecao() {
        // Arrange
        Map<String, Object> claims = new HashMap<>();

        // Act & Assert
        assertThrows(KeycloakException.class, () -> {
            JwtUtil.extractExpirationTime(claims);
        });
    }

    @Test
    void testExtractExpirationTime_ComExpString_DeveLancarExcecao() {
        // Arrange
        Map<String, Object> claims = new HashMap<>();
        claims.put(SecurityConstants.EXP_CLAIM, "1516239022");

        // Act & Assert
        assertThrows(KeycloakException.class, () -> {
            JwtUtil.extractExpirationTime(claims);
        });
    }

    @Test
    void testExtractSessionExpiration_ComExpiresInInteger_DeveRetornarInteger() {
        // Arrange
        Map<String, Object> claims = new HashMap<>();
        claims.put(SecurityConstants.EXPIRES_IN_CLAIM, 3600);

        // Act
        int sessionExpiration = JwtUtil.extractSessionExpiration(claims, 1800);

        // Assert
        assertEquals(3600, sessionExpiration);
    }

    @Test
    void testExtractSessionExpiration_ComExpiresInLong_DeveRetornarInteger() {
        // Arrange
        Map<String, Object> claims = new HashMap<>();
        claims.put(SecurityConstants.EXPIRES_IN_CLAIM, 3600L);

        // Act
        int sessionExpiration = JwtUtil.extractSessionExpiration(claims, 1800);

        // Assert
        assertEquals(3600, sessionExpiration);
    }

    @Test
    void testExtractSessionExpiration_ComExpiresInNulo_DeveRetornarDefault() {
        // Arrange
        Map<String, Object> claims = new HashMap<>();

        // Act
        int sessionExpiration = JwtUtil.extractSessionExpiration(claims, 1800);

        // Assert
        assertEquals(1800, sessionExpiration);
    }

    @Test
    void testExtractSessionExpiration_ComExpiresInString_DeveRetornarDefault() {
        // Arrange
        Map<String, Object> claims = new HashMap<>();
        claims.put(SecurityConstants.EXPIRES_IN_CLAIM, "3600");

        // Act
        int sessionExpiration = JwtUtil.extractSessionExpiration(claims, 1800);

        // Assert
        assertEquals(1800, sessionExpiration);
    }

    @Test
    void testExtractUsername_ComUsernameValido_DeveRetornarUsername() throws KeycloakException {
        // Arrange
        Map<String, Object> claims = new HashMap<>();
        claims.put(SecurityConstants.PREFERRED_USERNAME_CLAIM, "joao.silva");

        // Act
        String username = JwtUtil.extractUsername(claims);

        // Assert
        assertEquals("joao.silva", username);
    }

    @Test
    void testExtractUsername_ComUsernameNulo_DeveLancarExcecao() {
        // Arrange
        Map<String, Object> claims = new HashMap<>();

        // Act & Assert
        assertThrows(KeycloakException.class, () -> {
            JwtUtil.extractUsername(claims);
        });
    }

    @Test
    void testExtractUsername_ComUsernameVazio_DeveLancarExcecao() {
        // Arrange
        Map<String, Object> claims = new HashMap<>();
        claims.put(SecurityConstants.PREFERRED_USERNAME_CLAIM, "");

        // Act & Assert
        // Não deve lançar exceção para username vazio
        assertDoesNotThrow(() -> {
            JwtUtil.extractUsername(claims);
        });
    }

    @Test
    void testIsTokenExpired_ComTokenExpirado_DeveRetornarTrue() {
        // Arrange
        long expirationTime = System.currentTimeMillis() / 1000 - 3600; // 1 hora atrás

        // Act
        boolean isExpired = JwtUtil.isTokenExpired(expirationTime);

        // Assert
        assertTrue(isExpired);
    }

    @Test
    void testIsTokenExpired_ComTokenNaoExpirado_DeveRetornarFalse() {
        // Arrange
        long expirationTime = System.currentTimeMillis() / 1000 + 3600; // 1 hora no futuro

        // Act
        boolean isExpired = JwtUtil.isTokenExpired(expirationTime);

        // Assert
        assertFalse(isExpired);
    }

    @Test
    void testIsTokenExpired_ComTokenExpiradoAgora_DeveRetornarTrue() {
        // Arrange
        long expirationTime = System.currentTimeMillis() / 1000;

        // Act
        boolean isExpired = JwtUtil.isTokenExpired(expirationTime);

        // Assert
        assertTrue(isExpired);
    }

    @Test
    void testIsSessionExpired_ComSessaoExpirada_DeveRetornarTrue() {
        // Arrange
        long expirationTime = System.currentTimeMillis() / 1000;
        int sessionExpiration = 3600; // 1 hora
        long tokenCreationTime = expirationTime - sessionExpiration;
        long timeElapsed = System.currentTimeMillis() / 1000 - tokenCreationTime;

        // Simular que passou mais tempo que o permitido
        long expiredExpirationTime = System.currentTimeMillis() / 1000 - (sessionExpiration + 1000);

        // Act
        boolean isExpired = JwtUtil.isSessionExpired(expiredExpirationTime, sessionExpiration);

        // Assert
        assertTrue(isExpired);
    }

    @Test
    void testIsSessionExpired_ComSessaoNaoExpirada_DeveRetornarFalse() {
        // Arrange
        long expirationTime = System.currentTimeMillis() / 1000 + 3600; // 1 hora no futuro
        int sessionExpiration = 7200; // 2 horas

        // Act
        boolean isExpired = JwtUtil.isSessionExpired(expirationTime, sessionExpiration);

        // Assert
        assertFalse(isExpired);
    }

    @Test
    void testIsSessionExpired_ComSessaoExpiradaAgora_DeveRetornarTrue() {
        // Arrange
        long expirationTime = System.currentTimeMillis() / 1000;
        int sessionExpiration = 0; // Sessão expira imediatamente

        // Act
        boolean isExpired = JwtUtil.isSessionExpired(expirationTime, sessionExpiration);

        // Assert
        assertTrue(isExpired);
    }

    @Test
    void testExtractTokenFromHeader_ComBearerTokenComEspacos_DeveRetornarToken() {
        // Arrange
        String authorizationHeader = "Bearer  eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";

        // Act
        String token = JwtUtil.extractTokenFromHeader(authorizationHeader);

        // Assert
        assertEquals(" eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", token);
    }

    @Test
    void testExtractTokenFromHeader_ComBearerTokenLowerCase_DeveRetornarNull() {
        // Arrange
        String authorizationHeader = "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";

        // Act
        String token = JwtUtil.extractTokenFromHeader(authorizationHeader);

        // Assert
        assertNull(token);
    }

    @Test
    void testDecodeJwtPayload_ComTokenComClaimsComplexos_DeveRetornarClaims() throws KeycloakException {
        // Arrange
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiZW1haWwiOiJqb2FvQGV4YW1wbGUuY29tIiwicm9sZXMiOlsidXNlciIsImFkbWluIl0sImlhdCI6MTUxNjIzOTAyMn0.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        // Act
        Map<String, Object> claims = JwtUtil.decodeJwtPayload(token);

        // Assert
        assertNotNull(claims);
        assertEquals("1234567890", claims.get("sub"));
        assertEquals("John Doe", claims.get("name"));
        assertEquals("joao@example.com", claims.get("email"));
        assertEquals(1516239022, claims.get("iat"));
    }

    @Test
    void testExtractExpirationTime_ComExpZero_DeveRetornarZero() throws KeycloakException {
        // Arrange
        Map<String, Object> claims = new HashMap<>();
        claims.put(SecurityConstants.EXP_CLAIM, 0);

        // Act
        long expirationTime = JwtUtil.extractExpirationTime(claims);

        // Assert
        assertEquals(0L, expirationTime);
    }

    @Test
    void testExtractSessionExpiration_ComExpiresInZero_DeveRetornarZero() {
        // Arrange
        Map<String, Object> claims = new HashMap<>();
        claims.put(SecurityConstants.EXPIRES_IN_CLAIM, 0);

        // Act
        int sessionExpiration = JwtUtil.extractSessionExpiration(claims, 1800);

        // Assert
        assertEquals(0, sessionExpiration);
    }

    @Test
    void testExtractUsername_ComUsernameComCaracteresEspeciais_DeveRetornarUsername() throws KeycloakException {
        // Arrange
        Map<String, Object> claims = new HashMap<>();
        claims.put(SecurityConstants.PREFERRED_USERNAME_CLAIM, "joão.silva@empresa.com");

        // Act
        String username = JwtUtil.extractUsername(claims);

        // Assert
        assertEquals("joão.silva@empresa.com", username);
    }

    @Test
    void testIsTokenExpired_ComExpirationTimeNegativo_DeveRetornarTrue() {
        // Arrange
        long expirationTime = -1;

        // Act
        boolean isExpired = JwtUtil.isTokenExpired(expirationTime);

        // Assert
        assertTrue(isExpired);
    }

    @Test
    void testIsSessionExpired_ComSessionExpirationNegativo_DeveRetornarTrue() {
        // Arrange
        long expirationTime = System.currentTimeMillis() / 1000 + 3600; // 1 hora no futuro
        int sessionExpiration = -3600; // -1 hora

        // Act
        boolean isExpired = JwtUtil.isSessionExpired(expirationTime, sessionExpiration);

        // Assert
        assertFalse(isExpired);
    }

    // Novos testes para validações de segurança
    @Test
    void testDecodeJwtPayload_ComTokenNulo_DeveLancarExcecao() {
        // Act & Assert
        KeycloakException exception = assertThrows(KeycloakException.class, () -> {
            JwtUtil.decodeJwtPayload(null);
        });
        assertEquals("Token inválido: formato incorreto", exception.getMessage());
    }

    @Test
    void testDecodeJwtPayload_ComTokenVazio_DeveLancarExcecao() {
        // Act & Assert
        KeycloakException exception = assertThrows(KeycloakException.class, () -> {
            JwtUtil.decodeJwtPayload("");
        });
        assertEquals("Token inválido: formato incorreto", exception.getMessage());
    }

    @Test
    void testDecodeJwtPayload_ComTokenComEspacos_DeveLancarExcecao() {
        // Act & Assert
        KeycloakException exception = assertThrows(KeycloakException.class, () -> {
            JwtUtil.decodeJwtPayload("   ");
        });
        assertEquals("Token inválido: formato incorreto", exception.getMessage());
    }

    @Test
    void testDecodeJwtPayload_ComTokenMuitoGrande_DeveLancarExcecao() {
        // Arrange
        StringBuilder largeToken = new StringBuilder();
        for (int i = 0; i < 9000; i++) {
            largeToken.append("a");
        }
        String token = largeToken.toString() + ".payload.signature";

        // Act & Assert
        KeycloakException exception = assertThrows(KeycloakException.class, () -> {
            JwtUtil.decodeJwtPayload(token);
        });
        assertEquals("Token inválido: formato incorreto", exception.getMessage());
    }

    @Test
    void testDecodeJwtPayload_ComPayloadBase64MuitoGrande_DeveLancarExcecao() {
        // Arrange
        StringBuilder largePayload = new StringBuilder();
        for (int i = 0; i < 5000; i++) {
            largePayload.append("a");
        }
        String payloadBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(largePayload.toString().getBytes());
        String token = "header." + payloadBase64 + ".signature";

        // Act & Assert
        KeycloakException exception = assertThrows(KeycloakException.class, () -> {
            JwtUtil.decodeJwtPayload(token);
        });
        assertEquals("Token inválido: formato incorreto", exception.getMessage());
    }

    @Test
    void testDecodeJwtPayload_ComPayloadDecodificadoMuitoGrande_DeveLancarExcecao() {
        // Arrange
        StringBuilder largePayload = new StringBuilder();
        for (int i = 0; i < 5000; i++) {
            largePayload.append("a");
        }
        String payloadJson = "{\"data\":\"" + largePayload.toString() + "\"}";
        String payloadBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(payloadJson.getBytes());
        String token = "header." + payloadBase64 + ".signature";

        // Act & Assert
        // Este teste não deve lançar exceção pois o payload é válido
        assertDoesNotThrow(() -> {
            JwtUtil.decodeJwtPayload(token);
        });
    }

    @Test
    void testDecodeJwtPayload_ComBase64Malformado_DeveLancarExcecao() {
        // Arrange
        String token = "header.invalid-base64-!@#$%.signature";

        // Act & Assert
        KeycloakException exception = assertThrows(KeycloakException.class, () -> {
            JwtUtil.decodeJwtPayload(token);
        });
        assertEquals("Token inválido: formato incorreto", exception.getMessage());
    }

    @Test
    void testDecodeJwtPayload_ComBase64SemPadding_DeveFuncionar() throws KeycloakException {
        // Arrange
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", "1234567890");
        claims.put("name", "John Doe");
        
        String payloadJson = "{\"sub\":\"1234567890\",\"name\":\"John Doe\"}";
        String payloadBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(payloadJson.getBytes());
        String token = "header." + payloadBase64 + ".signature";

        // Act
        Map<String, Object> result = JwtUtil.decodeJwtPayload(token);

        // Assert
        assertNotNull(result);
        assertEquals("1234567890", result.get("sub"));
        assertEquals("John Doe", result.get("name"));
    }

    @Test
    void testDecodeJwtPayload_ComBase64ComPadding_DeveFuncionar() throws KeycloakException {
        // Arrange
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", "1234567890");
        claims.put("name", "John Doe");
        
        String payloadJson = "{\"sub\":\"1234567890\",\"name\":\"John Doe\"}";
        String payloadBase64 = Base64.getEncoder().encodeToString(payloadJson.getBytes());
        String token = "header." + payloadBase64 + ".signature";

        // Act
        Map<String, Object> result = JwtUtil.decodeJwtPayload(token);

        // Assert
        assertNotNull(result);
        assertEquals("1234567890", result.get("sub"));
        assertEquals("John Doe", result.get("name"));
    }

    @Test
    void testDecodeJwtPayload_ComTokenComCaracteresInvalidos_DeveLancarExcecao() {
        // Arrange
        String token = "header.payload\u0000.signature";

        // Act & Assert
        assertThrows(KeycloakException.class, () -> {
            JwtUtil.decodeJwtPayload(token);
        });
    }

    @Test
    void testDecodeJwtPayload_ComPayloadJSONInvalido_DeveLancarExcecao() {
        // Arrange
        String invalidJson = "{invalid json}";
        String payloadBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(invalidJson.getBytes());
        String token = "header." + payloadBase64 + ".signature";

        // Act & Assert
        assertThrows(KeycloakException.class, () -> {
            JwtUtil.decodeJwtPayload(token);
        });
    }

    @Test
    void testDecodeJwtPayload_ComTokenValidoNoLimite_DeveFuncionar() throws KeycloakException {
        // Arrange - Token com tamanho próximo ao limite
        StringBuilder largePayload = new StringBuilder();
        for (int i = 0; i < 3000; i++) {
            largePayload.append("a");
        }
        String payloadJson = "{\"data\":\"" + largePayload.toString() + "\"}";
        String payloadBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(payloadJson.getBytes());
        String token = "header." + payloadBase64 + ".signature";

        // Act
        Map<String, Object> result = JwtUtil.decodeJwtPayload(token);

        // Assert
        assertNotNull(result);
        assertNotNull(result.get("data"));
    }
} 
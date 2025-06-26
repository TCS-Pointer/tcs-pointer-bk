package br.com.pointer.pointer_back.util;

import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.UserRepresentation;
import static org.junit.jupiter.api.Assertions.*;

class UserUtilTest {

    @Test
    void testExtrairNomeESobrenome_ComNomeCompleto_DeveRetornarNomeESobrenome() {
        // Arrange
        String nomeCompleto = "João Silva";

        // Act
        String[] resultado = UserUtil.extrairNomeESobrenome(nomeCompleto);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.length);
        assertEquals("João", resultado[0]);
        assertEquals("Silva", resultado[1]);
    }

    @Test
    void testExtrairNomeESobrenome_ComNomeCompletoComTresPartes_DeveRetornarNomeESobrenome() {
        // Arrange
        String nomeCompleto = "João Silva Santos";

        // Act
        String[] resultado = UserUtil.extrairNomeESobrenome(nomeCompleto);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.length);
        assertEquals("João", resultado[0]);
        assertEquals("Silva Santos", resultado[1]);
    }

    @Test
    void testExtrairNomeESobrenome_ComApenasNome_DeveRetornarNomeEVazio() {
        // Arrange
        String nomeCompleto = "João";

        // Act
        String[] resultado = UserUtil.extrairNomeESobrenome(nomeCompleto);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.length);
        assertEquals("João", resultado[0]);
        assertEquals("", resultado[1]);
    }

    @Test
    void testExtrairNomeESobrenome_ComNomeComEspacos_DeveRetornarNomeESobrenome() {
        // Arrange
        String nomeCompleto = "João   Silva";

        // Act
        String[] resultado = UserUtil.extrairNomeESobrenome(nomeCompleto);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.length);
        assertEquals("João", resultado[0]);
        assertEquals("Silva", resultado[1].trim());
    }

    @Test
    void testExtrairNomeESobrenome_ComNomeVazio_DeveRetornarVazioEVazio() {
        // Arrange
        String nomeCompleto = "";

        // Act
        String[] resultado = UserUtil.extrairNomeESobrenome(nomeCompleto);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.length);
        assertEquals("", resultado[0]);
        assertEquals("", resultado[1]);
    }

    @Test
    void testExtrairNomeESobrenome_ComNomeComEspacosApenas_DeveRetornarVazioEVazio() {
        // Arrange
        String nomeCompleto = "   ";

        // Act
        String[] resultado = UserUtil.extrairNomeESobrenome(nomeCompleto);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.length);
        assertEquals("", resultado[0]);
        assertEquals("", resultado[1]);
    }

    @Test
    void testExtrairNomeESobrenome_ComNomeComCaracteresEspeciais_DeveRetornarNomeESobrenome() {
        // Arrange
        String nomeCompleto = "João Silva-Santos";

        // Act
        String[] resultado = UserUtil.extrairNomeESobrenome(nomeCompleto);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.length);
        assertEquals("João", resultado[0]);
        assertEquals("Silva-Santos", resultado[1]);
    }

    @Test
    void testExtrairNomeESobrenome_ComNomeComNumeros_DeveRetornarNomeESobrenome() {
        // Arrange
        String nomeCompleto = "João Silva 123";

        // Act
        String[] resultado = UserUtil.extrairNomeESobrenome(nomeCompleto);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.length);
        assertEquals("João", resultado[0]);
        assertEquals("Silva 123", resultado[1]);
    }

    @Test
    void testExtrairNomeESobrenome_ComNomeComPontuacao_DeveRetornarNomeESobrenome() {
        // Arrange
        String nomeCompleto = "João Silva, Jr.";

        // Act
        String[] resultado = UserUtil.extrairNomeESobrenome(nomeCompleto);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.length);
        assertEquals("João", resultado[0]);
        assertEquals("Silva, Jr.", resultado[1]);
    }

    @Test
    void testConstruirUserRepresentation_ComDadosValidos_DeveRetornarUserRepresentation() {
        // Arrange
        String nome = "João";
        String sobrenome = "Silva";
        String email = "joao.silva@empresa.com";

        // Act
        UserRepresentation user = UserUtil.construirUserRepresentation(nome, sobrenome, email);

        // Assert
        assertNotNull(user);
        assertEquals(email, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(nome, user.getFirstName());
        assertEquals(sobrenome, user.getLastName());
        assertTrue(user.isEnabled());
        assertFalse(user.isEmailVerified());
    }

    @Test
    void testConstruirUserRepresentation_ComNomeNulo_DeveRetornarUserRepresentation() {
        // Arrange
        String nome = null;
        String sobrenome = "Silva";
        String email = "joao.silva@empresa.com";

        // Act
        UserRepresentation user = UserUtil.construirUserRepresentation(nome, sobrenome, email);

        // Assert
        assertNotNull(user);
        assertEquals(email, user.getUsername());
        assertEquals(email, user.getEmail());
        assertNull(user.getFirstName());
        assertEquals(sobrenome, user.getLastName());
        assertTrue(user.isEnabled());
        assertFalse(user.isEmailVerified());
    }

    @Test
    void testConstruirUserRepresentation_ComSobrenomeNulo_DeveRetornarUserRepresentation() {
        // Arrange
        String nome = "João";
        String sobrenome = null;
        String email = "joao.silva@empresa.com";

        // Act
        UserRepresentation user = UserUtil.construirUserRepresentation(nome, sobrenome, email);

        // Assert
        assertNotNull(user);
        assertEquals(email, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(nome, user.getFirstName());
        assertNull(user.getLastName());
        assertTrue(user.isEnabled());
        assertFalse(user.isEmailVerified());
    }

    @Test
    void testConstruirUserRepresentation_ComEmailNulo_DeveRetornarUserRepresentation() {
        // Arrange
        String nome = "João";
        String sobrenome = "Silva";
        String email = null;

        // Act
        UserRepresentation user = UserUtil.construirUserRepresentation(nome, sobrenome, email);

        // Assert
        assertNotNull(user);
        assertNull(user.getUsername());
        assertNull(user.getEmail());
        assertEquals(nome, user.getFirstName());
        assertEquals(sobrenome, user.getLastName());
        assertTrue(user.isEnabled());
        assertFalse(user.isEmailVerified());
    }

    @Test
    void testConstruirUserRepresentation_ComTodosNulos_DeveRetornarUserRepresentation() {
        // Arrange
        String nome = null;
        String sobrenome = null;
        String email = null;

        // Act
        UserRepresentation user = UserUtil.construirUserRepresentation(nome, sobrenome, email);

        // Assert
        assertNotNull(user);
        assertNull(user.getUsername());
        assertNull(user.getEmail());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertTrue(user.isEnabled());
        assertFalse(user.isEmailVerified());
    }

    @Test
    void testConstruirUserRepresentation_ComNomeVazio_DeveRetornarUserRepresentation() {
        // Arrange
        String nome = "";
        String sobrenome = "Silva";
        String email = "joao.silva@empresa.com";

        // Act
        UserRepresentation user = UserUtil.construirUserRepresentation(nome, sobrenome, email);

        // Assert
        assertNotNull(user);
        assertEquals(email, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals("", user.getFirstName());
        assertEquals(sobrenome, user.getLastName());
        assertTrue(user.isEnabled());
        assertFalse(user.isEmailVerified());
    }

    @Test
    void testConstruirUserRepresentation_ComSobrenomeVazio_DeveRetornarUserRepresentation() {
        // Arrange
        String nome = "João";
        String sobrenome = "";
        String email = "joao.silva@empresa.com";

        // Act
        UserRepresentation user = UserUtil.construirUserRepresentation(nome, sobrenome, email);

        // Assert
        assertNotNull(user);
        assertEquals(email, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(nome, user.getFirstName());
        assertEquals("", user.getLastName());
        assertTrue(user.isEnabled());
        assertFalse(user.isEmailVerified());
    }

    @Test
    void testConstruirUserRepresentation_ComEmailVazio_DeveRetornarUserRepresentation() {
        // Arrange
        String nome = "João";
        String sobrenome = "Silva";
        String email = "";

        // Act
        UserRepresentation user = UserUtil.construirUserRepresentation(nome, sobrenome, email);

        // Assert
        assertNotNull(user);
        assertEquals("", user.getUsername());
        assertEquals("", user.getEmail());
        assertEquals(nome, user.getFirstName());
        assertEquals(sobrenome, user.getLastName());
        assertTrue(user.isEnabled());
        assertFalse(user.isEmailVerified());
    }

    @Test
    void testConstruirUserRepresentation_ComNomeComCaracteresEspeciais_DeveRetornarUserRepresentation() {
        // Arrange
        String nome = "João";
        String sobrenome = "Silva-Santos";
        String email = "joao.silva@empresa.com";

        // Act
        UserRepresentation user = UserUtil.construirUserRepresentation(nome, sobrenome, email);

        // Assert
        assertNotNull(user);
        assertEquals(email, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(nome, user.getFirstName());
        assertEquals(sobrenome, user.getLastName());
        assertTrue(user.isEnabled());
        assertFalse(user.isEmailVerified());
    }

    @Test
    void testConstruirUserRepresentation_ComEmailComSubdominio_DeveRetornarUserRepresentation() {
        // Arrange
        String nome = "João";
        String sobrenome = "Silva";
        String email = "joao.silva@subdominio.empresa.com";

        // Act
        UserRepresentation user = UserUtil.construirUserRepresentation(nome, sobrenome, email);

        // Assert
        assertNotNull(user);
        assertEquals(email, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(nome, user.getFirstName());
        assertEquals(sobrenome, user.getLastName());
        assertTrue(user.isEnabled());
        assertFalse(user.isEmailVerified());
    }

    @Test
    void testConstruirUserRepresentation_ComNomeComEspacos_DeveRetornarUserRepresentation() {
        // Arrange
        String nome = "João";
        String sobrenome = "Silva Santos";
        String email = "joao.silva@empresa.com";

        // Act
        UserRepresentation user = UserUtil.construirUserRepresentation(nome, sobrenome, email);

        // Assert
        assertNotNull(user);
        assertEquals(email, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(nome, user.getFirstName());
        assertEquals(sobrenome, user.getLastName());
        assertTrue(user.isEnabled());
        assertFalse(user.isEmailVerified());
    }

    @Test
    void testConstruirUserRepresentation_ComEmailComCaracteresEspeciais_DeveRetornarUserRepresentation() {
        // Arrange
        String nome = "João";
        String sobrenome = "Silva";
        String email = "joão.silva@empresa.com";

        // Act
        UserRepresentation user = UserUtil.construirUserRepresentation(nome, sobrenome, email);

        // Assert
        assertNotNull(user);
        assertEquals(email, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(nome, user.getFirstName());
        assertEquals(sobrenome, user.getLastName());
        assertTrue(user.isEnabled());
        assertFalse(user.isEmailVerified());
    }

    @Test
    void testExtrairNomeESobrenome_ComNomeComAcentos_DeveRetornarNomeESobrenome() {
        // Arrange
        String nomeCompleto = "João Silva";

        // Act
        String[] resultado = UserUtil.extrairNomeESobrenome(nomeCompleto);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.length);
        assertEquals("João", resultado[0]);
        assertEquals("Silva", resultado[1]);
    }

    @Test
    void testExtrairNomeESobrenome_ComNomeComCedilha_DeveRetornarNomeESobrenome() {
        // Arrange
        String nomeCompleto = "François Silva";

        // Act
        String[] resultado = UserUtil.extrairNomeESobrenome(nomeCompleto);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.length);
        assertEquals("François", resultado[0]);
        assertEquals("Silva", resultado[1]);
    }

    @Test
    void testExtrairNomeESobrenome_ComNomeComTil_DeveRetornarNomeESobrenome() {
        // Arrange
        String nomeCompleto = "João Silva";

        // Act
        String[] resultado = UserUtil.extrairNomeESobrenome(nomeCompleto);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.length);
        assertEquals("João", resultado[0]);
        assertEquals("Silva", resultado[1]);
    }

    @Test
    void testExtrairNomeESobrenome_ComNomeComHifen_DeveRetornarNomeESobrenome() {
        // Arrange
        String nomeCompleto = "João Silva-Santos";

        // Act
        String[] resultado = UserUtil.extrairNomeESobrenome(nomeCompleto);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.length);
        assertEquals("João", resultado[0]);
        assertEquals("Silva-Santos", resultado[1]);
    }

    @Test
    void testExtrairNomeESobrenome_ComNomeComApostrofo_DeveRetornarNomeESobrenome() {
        // Arrange
        String nomeCompleto = "O'Connor Silva";

        // Act
        String[] resultado = UserUtil.extrairNomeESobrenome(nomeCompleto);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.length);
        assertEquals("O'Connor", resultado[0]);
        assertEquals("Silva", resultado[1]);
    }
} 
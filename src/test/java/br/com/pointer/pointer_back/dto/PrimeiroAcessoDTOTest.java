package br.com.pointer.pointer_back.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PrimeiroAcessoDTOTest {

    private PrimeiroAcessoDTO primeiroAcesso;

    @BeforeEach
    void setUp() {
        primeiroAcesso = new PrimeiroAcessoDTO();
    }

    @Test
    @DisplayName("Deve criar PrimeiroAcessoDTO com todos os campos")
    void deveCriarPrimeiroAcessoDTOComTodosOsCampos() {
        // Arrange
        primeiroAcesso.setEmail("test@example.com");
        primeiroAcesso.setToken("token123");
        primeiroAcesso.setNovaSenha("novaSenha123");

        // Assert
        assertEquals("test@example.com", primeiroAcesso.getEmail());
        assertEquals("token123", primeiroAcesso.getToken());
        assertEquals("novaSenha123", primeiroAcesso.getNovaSenha());
    }

    @Test
    @DisplayName("Deve criar PrimeiroAcessoDTO com valores padrão")
    void deveCriarPrimeiroAcessoDTOComValoresPadrao() {
        // Assert
        assertNull(primeiroAcesso.getEmail());
        assertNull(primeiroAcesso.getToken());
        assertNull(primeiroAcesso.getNovaSenha());
    }

    @Test
    @DisplayName("Deve atualizar campos do PrimeiroAcessoDTO")
    void deveAtualizarCamposDoPrimeiroAcessoDTO() {
        // Arrange
        primeiroAcesso.setEmail("original@example.com");
        primeiroAcesso.setToken("original-token");
        primeiroAcesso.setNovaSenha("original-senha");

        // Act
        primeiroAcesso.setEmail("atualizado@example.com");
        primeiroAcesso.setToken("atualizado-token");
        primeiroAcesso.setNovaSenha("atualizada-senha");

        // Assert
        assertEquals("atualizado@example.com", primeiroAcesso.getEmail());
        assertEquals("atualizado-token", primeiroAcesso.getToken());
        assertEquals("atualizada-senha", primeiroAcesso.getNovaSenha());
    }

    @Test
    @DisplayName("Deve definir email")
    void deveDefinirEmail() {
        // Act
        primeiroAcesso.setEmail("test@example.com");

        // Assert
        assertEquals("test@example.com", primeiroAcesso.getEmail());
    }

    @Test
    @DisplayName("Deve definir token")
    void deveDefinirToken() {
        // Act
        primeiroAcesso.setToken("token123");

        // Assert
        assertEquals("token123", primeiroAcesso.getToken());
    }

    @Test
    @DisplayName("Deve definir nova senha")
    void deveDefinirNovaSenha() {
        // Act
        primeiroAcesso.setNovaSenha("novaSenha123");

        // Assert
        assertEquals("novaSenha123", primeiroAcesso.getNovaSenha());
    }

    @Test
    @DisplayName("Deve definir email como null")
    void deveDefinirEmailComoNull() {
        // Act
        primeiroAcesso.setEmail(null);

        // Assert
        assertNull(primeiroAcesso.getEmail());
    }

    @Test
    @DisplayName("Deve definir token como null")
    void deveDefinirTokenComoNull() {
        // Act
        primeiroAcesso.setToken(null);

        // Assert
        assertNull(primeiroAcesso.getToken());
    }

    @Test
    @DisplayName("Deve definir nova senha como null")
    void deveDefinirNovaSenhaComoNull() {
        // Act
        primeiroAcesso.setNovaSenha(null);

        // Assert
        assertNull(primeiroAcesso.getNovaSenha());
    }

    @Test
    @DisplayName("Deve definir email vazio")
    void deveDefinirEmailVazio() {
        // Act
        primeiroAcesso.setEmail("");

        // Assert
        assertEquals("", primeiroAcesso.getEmail());
    }

    @Test
    @DisplayName("Deve definir token vazio")
    void deveDefinirTokenVazio() {
        // Act
        primeiroAcesso.setToken("");

        // Assert
        assertEquals("", primeiroAcesso.getToken());
    }

    @Test
    @DisplayName("Deve definir nova senha vazia")
    void deveDefinirNovaSenhaVazia() {
        // Act
        primeiroAcesso.setNovaSenha("");

        // Assert
        assertEquals("", primeiroAcesso.getNovaSenha());
    }

    @Test
    @DisplayName("Deve definir email com caracteres especiais")
    void deveDefinirEmailComCaracteresEspeciais() {
        // Act
        primeiroAcesso.setEmail("test+tag@example.com");

        // Assert
        assertEquals("test+tag@example.com", primeiroAcesso.getEmail());
    }

    @Test
    @DisplayName("Deve definir token com caracteres especiais")
    void deveDefinirTokenComCaracteresEspeciais() {
        // Act
        primeiroAcesso.setToken("token-123_456.789");

        // Assert
        assertEquals("token-123_456.789", primeiroAcesso.getToken());
    }

    @Test
    @DisplayName("Deve definir nova senha com caracteres especiais")
    void deveDefinirNovaSenhaComCaracteresEspeciais() {
        // Act
        primeiroAcesso.setNovaSenha("Senha@123#456");

        // Assert
        assertEquals("Senha@123#456", primeiroAcesso.getNovaSenha());
    }

    @Test
    @DisplayName("Deve testar equals e hashCode com objetos iguais")
    void deveTestarEqualsEHashCodeComObjetosIguais() {
        // Arrange
        PrimeiroAcessoDTO primeiroAcesso1 = new PrimeiroAcessoDTO();
        primeiroAcesso1.setEmail("test@example.com");
        primeiroAcesso1.setToken("token123");
        primeiroAcesso1.setNovaSenha("novaSenha123");

        PrimeiroAcessoDTO primeiroAcesso2 = new PrimeiroAcessoDTO();
        primeiroAcesso2.setEmail("test@example.com");
        primeiroAcesso2.setToken("token123");
        primeiroAcesso2.setNovaSenha("novaSenha123");

        PrimeiroAcessoDTO primeiroAcesso3 = new PrimeiroAcessoDTO();
        primeiroAcesso3.setEmail("test@example.com");
        primeiroAcesso3.setToken("token123");
        primeiroAcesso3.setNovaSenha("novaSenha123");

        // Assert
        assertEquals(primeiroAcesso1, primeiroAcesso2);
        assertEquals(primeiroAcesso2, primeiroAcesso3);
        assertEquals(primeiroAcesso1, primeiroAcesso3);
        assertEquals(primeiroAcesso1.hashCode(), primeiroAcesso2.hashCode());
        assertEquals(primeiroAcesso2.hashCode(), primeiroAcesso3.hashCode());
    }

    @Test
    @DisplayName("Deve testar equals e hashCode com objetos diferentes")
    void deveTestarEqualsEHashCodeComObjetosDiferentes() {
        // Arrange
        PrimeiroAcessoDTO primeiroAcesso1 = new PrimeiroAcessoDTO();
        primeiroAcesso1.setEmail("test1@example.com");
        primeiroAcesso1.setToken("token123");
        primeiroAcesso1.setNovaSenha("novaSenha123");

        PrimeiroAcessoDTO primeiroAcesso2 = new PrimeiroAcessoDTO();
        primeiroAcesso2.setEmail("test2@example.com");
        primeiroAcesso2.setToken("token123");
        primeiroAcesso2.setNovaSenha("novaSenha123");

        PrimeiroAcessoDTO primeiroAcesso3 = new PrimeiroAcessoDTO();
        primeiroAcesso3.setEmail("test1@example.com");
        primeiroAcesso3.setToken("token456");
        primeiroAcesso3.setNovaSenha("novaSenha123");

        // Assert
        assertNotEquals(primeiroAcesso1, primeiroAcesso2);
        assertNotEquals(primeiroAcesso1, primeiroAcesso3);
        assertNotEquals(primeiroAcesso2, primeiroAcesso3);
        assertNotEquals(primeiroAcesso1.hashCode(), primeiroAcesso2.hashCode());
        assertNotEquals(primeiroAcesso1.hashCode(), primeiroAcesso3.hashCode());
        assertNotEquals(primeiroAcesso2.hashCode(), primeiroAcesso3.hashCode());
    }

    @Test
    @DisplayName("Deve testar toString")
    void deveTestarToString() {
        // Arrange
        primeiroAcesso.setEmail("test@example.com");
        primeiroAcesso.setToken("token123");

        // Act
        String result = primeiroAcesso.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("test@example.com"));
        assertTrue(result.contains("token123"));
    }
} 
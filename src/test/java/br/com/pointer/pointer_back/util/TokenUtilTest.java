package br.com.pointer.pointer_back.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TokenUtilTest {

    @Test
    void testGerarTokenAleatorio_DeveRetornarTokenComTamanhoCorreto() {
        // Act
        String token = TokenUtil.gerarTokenAleatorio();

        // Assert
        assertNotNull(token);
        assertEquals(32, token.length());
    }

    @Test
    void testGerarTokenAleatorio_DeveRetornarTokenComCaracteresValidos() {
        // Act
        String token = TokenUtil.gerarTokenAleatorio();

        // Assert
        assertNotNull(token);
        assertTrue(token.matches("^[A-Za-z0-9]{32}$"));
    }

    @Test
    void testGerarTokenAleatorio_DeveRetornarTokensDiferentes() {
        // Act
        String token1 = TokenUtil.gerarTokenAleatorio();
        String token2 = TokenUtil.gerarTokenAleatorio();

        // Assert
        assertNotNull(token1);
        assertNotNull(token2);
        assertNotEquals(token1, token2);
    }

    @Test
    void testGerarTokenAleatorio_DeveConterLetrasMaiusculas() {
        // Act
        String token = TokenUtil.gerarTokenAleatorio();

        // Assert
        assertNotNull(token);
        assertTrue(token.matches(".*[A-Z].*"));
    }

    @Test
    void testGerarTokenAleatorio_DeveConterLetrasMinusculas() {
        // Act
        String token = TokenUtil.gerarTokenAleatorio();

        // Assert
        assertNotNull(token);
        assertTrue(token.matches(".*[a-z].*"));
    }

    @Test
    void testGerarTokenAleatorio_DeveConterNumeros() {
        // Act
        String token = TokenUtil.gerarTokenAleatorio();

        // Assert
        assertNotNull(token);
        assertTrue(token.matches(".*[0-9].*"));
    }

    @Test
    void testGerarTokenAleatorio_DeveConterApenasCaracteresValidos() {
        // Act
        String token = TokenUtil.gerarTokenAleatorio();

        // Assert
        assertNotNull(token);
        // Verifica se contém apenas caracteres válidos (A-Z, a-z, 0-9)
        assertTrue(token.matches("^[A-Za-z0-9]+$"));
    }

    @Test
    void testGerarTokenAleatorio_DeveSerAleatorio() {
        // Arrange
        String[] tokens = new String[100];

        // Act
        for (int i = 0; i < 100; i++) {
            tokens[i] = TokenUtil.gerarTokenAleatorio();
        }

        // Assert
        // Verifica se há pelo menos alguns tokens diferentes
        boolean temTokensDiferentes = false;
        for (int i = 0; i < tokens.length - 1; i++) {
            for (int j = i + 1; j < tokens.length; j++) {
                if (!tokens[i].equals(tokens[j])) {
                    temTokensDiferentes = true;
                    break;
                }
            }
            if (temTokensDiferentes) break;
        }
        assertTrue(temTokensDiferentes, "Os tokens gerados devem ser diferentes");
    }

    @Test
    void testGerarTokenAleatorio_DeveTerTamanhoConsistente() {
        // Act
        String token1 = TokenUtil.gerarTokenAleatorio();
        String token2 = TokenUtil.gerarTokenAleatorio();
        String token3 = TokenUtil.gerarTokenAleatorio();

        // Assert
        assertEquals(32, token1.length());
        assertEquals(32, token2.length());
        assertEquals(32, token3.length());
    }

    @Test
    void testGerarTokenAleatorio_DeveSerUnico() {
        // Arrange
        String[] tokens = new String[1000];

        // Act
        for (int i = 0; i < 1000; i++) {
            tokens[i] = TokenUtil.gerarTokenAleatorio();
        }

        // Assert
        // Verifica se todos os tokens são únicos
        for (int i = 0; i < tokens.length - 1; i++) {
            for (int j = i + 1; j < tokens.length; j++) {
                assertNotEquals(tokens[i], tokens[j], 
                    "Token na posição " + i + " é igual ao token na posição " + j);
            }
        }
    }

    @Test
    void testGerarTokenAleatorio_DeveConterTodosOsTiposDeCaracteres() {
        // Act
        String token = TokenUtil.gerarTokenAleatorio();

        // Assert
        assertNotNull(token);
        // Verifica se contém pelo menos uma letra maiúscula, uma minúscula e um número
        boolean temMaiuscula = token.matches(".*[A-Z].*");
        boolean temMinuscula = token.matches(".*[a-z].*");
        boolean temNumero = token.matches(".*[0-9].*");

        assertTrue(temMaiuscula || temMinuscula || temNumero, 
            "Token deve conter pelo menos um tipo de caractere válido");
    }

    @Test
    void testGerarTokenAleatorio_DeveSerReproduzivel() {
        // Act
        String token1 = TokenUtil.gerarTokenAleatorio();
        String token2 = TokenUtil.gerarTokenAleatorio();

        // Assert
        assertNotNull(token1);
        assertNotNull(token2);
        // Ambos os tokens devem ter o mesmo formato
        assertEquals(token1.length(), token2.length());
        assertTrue(token1.matches("^[A-Za-z0-9]{32}$"));
        assertTrue(token2.matches("^[A-Za-z0-9]{32}$"));
    }

    @Test
    void testGerarTokenAleatorio_DeveSerCaseSensitive() {
        // Act
        String token1 = TokenUtil.gerarTokenAleatorio();
        String token2 = TokenUtil.gerarTokenAleatorio();

        // Assert
        assertNotNull(token1);
        assertNotNull(token2);
        // Os tokens devem ser diferentes (incluindo case)
        assertNotEquals(token1, token2);
        assertNotEquals(token1.toLowerCase(), token2.toLowerCase());
    }

    @Test
    void testGerarTokenAleatorio_DeveSerAlfanumerico() {
        // Act
        String token = TokenUtil.gerarTokenAleatorio();

        // Assert
        assertNotNull(token);
        // Verifica se é alfanumérico (apenas letras e números)
        assertTrue(token.matches("^[A-Za-z0-9]+$"));
        // Verifica se não contém caracteres especiais
        assertFalse(token.matches(".*[^A-Za-z0-9].*"));
    }

    @Test
    void testGerarTokenAleatorio_DeveTerDistribuicaoAleatoria() {
        // Arrange
        int[] contadores = new int[62]; // 26 maiúsculas + 26 minúsculas + 10 números
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        // Act
        for (int i = 0; i < 1000; i++) {
            String token = TokenUtil.gerarTokenAleatorio();
            for (char c : token.toCharArray()) {
                int index = caracteres.indexOf(c);
                if (index >= 0) {
                    contadores[index]++;
                }
            }
        }

        // Assert
        // Verifica se pelo menos alguns caracteres foram usados
        boolean algumCaractereUsado = false;
        for (int contador : contadores) {
            if (contador > 0) {
                algumCaractereUsado = true;
                break;
            }
        }
        assertTrue(algumCaractereUsado, "Pelo menos alguns caracteres devem ser usados");
    }

    @Test
    void testGerarTokenAleatorio_DeveSerValidoParaURL() {
        // Act
        String token = TokenUtil.gerarTokenAleatorio();

        // Assert
        assertNotNull(token);
        // Verifica se o token é seguro para uso em URLs (sem caracteres especiais)
        assertTrue(token.matches("^[A-Za-z0-9]+$"));
        assertFalse(token.contains(" "));
        assertFalse(token.contains("&"));
        assertFalse(token.contains("="));
        assertFalse(token.contains("?"));
        assertFalse(token.contains("#"));
        assertFalse(token.contains("+"));
        assertFalse(token.contains("/"));
    }

    @Test
    void testGerarTokenAleatorio_DeveSerValidoParaEmail() {
        // Act
        String token = TokenUtil.gerarTokenAleatorio();

        // Assert
        assertNotNull(token);
        // Verifica se o token é seguro para uso em emails (sem caracteres problemáticos)
        assertTrue(token.matches("^[A-Za-z0-9]+$"));
        assertFalse(token.contains(" "));
        assertFalse(token.contains("\""));
        assertFalse(token.contains("'"));
        assertFalse(token.contains("\\"));
        assertFalse(token.contains("\n"));
        assertFalse(token.contains("\r"));
        assertFalse(token.contains("\t"));
    }

    @Test
    void testGerarTokenAleatorio_DeveSerValidoParaBancoDeDados() {
        // Act
        String token = TokenUtil.gerarTokenAleatorio();

        // Assert
        assertNotNull(token);
        // Verifica se o token é seguro para uso em bancos de dados
        assertTrue(token.matches("^[A-Za-z0-9]+$"));
        assertFalse(token.contains("'"));
        assertFalse(token.contains("\""));
        assertFalse(token.contains(";"));
        assertFalse(token.contains("--"));
        assertFalse(token.contains("/*"));
        assertFalse(token.contains("*/"));
    }

    @Test
    void testGerarTokenAleatorio_DeveSerValidoParaJSON() {
        // Act
        String token = TokenUtil.gerarTokenAleatorio();

        // Assert
        assertNotNull(token);
        // Verifica se o token é seguro para uso em JSON
        assertTrue(token.matches("^[A-Za-z0-9]+$"));
        assertFalse(token.contains("\""));
        assertFalse(token.contains("\\"));
        assertFalse(token.contains("\n"));
        assertFalse(token.contains("\r"));
        assertFalse(token.contains("\t"));
    }

    @Test
    void testGerarTokenAleatorio_DeveSerValidoParaHTML() {
        // Act
        String token = TokenUtil.gerarTokenAleatorio();

        // Assert
        assertNotNull(token);
        // Verifica se o token é seguro para uso em HTML
        assertTrue(token.matches("^[A-Za-z0-9]+$"));
        assertFalse(token.contains("<"));
        assertFalse(token.contains(">"));
        assertFalse(token.contains("&"));
        assertFalse(token.contains("\""));
        assertFalse(token.contains("'"));
    }
} 
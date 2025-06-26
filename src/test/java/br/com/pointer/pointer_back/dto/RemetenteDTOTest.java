package br.com.pointer.pointer_back.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

@DisplayName("RemetenteDTO")
class RemetenteDTOTest {

    @Test
    @DisplayName("Deve criar Remetente com todos os campos")
    void deveCriarRemetenteComTodosOsCampos() {
        // Given
        Long usuarioId = 1L;
        String nome = "Maria Silva";
        String cargo = "Analista de RH";
        
        // When
        RemetenteDTO remetente = new RemetenteDTO();
        remetente.setUsuarioId(usuarioId);
        remetente.setNome(nome);
        remetente.setCargo(cargo);
        
        // Then
        assertNotNull(remetente);
        assertEquals(usuarioId, remetente.getUsuarioId());
        assertEquals(nome, remetente.getNome());
        assertEquals(cargo, remetente.getCargo());
    }

    @Test
    @DisplayName("Deve criar Remetente com campos nulos")
    void deveCriarRemetenteComCamposNulos() {
        // Given
        RemetenteDTO remetente = new RemetenteDTO();
        
        // When & Then
        assertNotNull(remetente);
        assertNull(remetente.getUsuarioId());
        assertNull(remetente.getNome());
        assertNull(remetente.getCargo());
    }

    @Test
    @DisplayName("Deve criar Remetente com campos vazios")
    void deveCriarRemetenteComCamposVazios() {
        // Given
        RemetenteDTO remetente = new RemetenteDTO();
        remetente.setUsuarioId(0L);
        remetente.setNome("");
        remetente.setCargo("");
        
        // When & Then
        assertNotNull(remetente);
        assertEquals(0L, remetente.getUsuarioId());
        assertEquals("", remetente.getNome());
        assertEquals("", remetente.getCargo());
    }

    @Test
    @DisplayName("Deve criar Remetente com usuarioId positivo")
    void deveCriarRemetenteComUsuarioIdPositivo() {
        // Given
        RemetenteDTO remetente = new RemetenteDTO();
        Long usuarioId = 123L;
        
        // When
        remetente.setUsuarioId(usuarioId);
        
        // Then
        assertEquals(usuarioId, remetente.getUsuarioId());
    }

    @Test
    @DisplayName("Deve criar Remetente com usuarioId zero")
    void deveCriarRemetenteComUsuarioIdZero() {
        // Given
        RemetenteDTO remetente = new RemetenteDTO();
        Long usuarioId = 0L;
        
        // When
        remetente.setUsuarioId(usuarioId);
        
        // Then
        assertEquals(usuarioId, remetente.getUsuarioId());
    }

    @Test
    @DisplayName("Deve criar Remetente com usuarioId negativo")
    void deveCriarRemetenteComUsuarioIdNegativo() {
        // Given
        RemetenteDTO remetente = new RemetenteDTO();
        Long usuarioId = -1L;
        
        // When
        remetente.setUsuarioId(usuarioId);
        
        // Then
        assertEquals(usuarioId, remetente.getUsuarioId());
    }

    @Test
    @DisplayName("Deve criar Remetente com nome simples")
    void deveCriarRemetenteComNomeSimples() {
        // Given
        RemetenteDTO remetente = new RemetenteDTO();
        String nomeSimples = "Maria";
        
        // When
        remetente.setNome(nomeSimples);
        
        // Then
        assertEquals(nomeSimples, remetente.getNome());
    }

    @Test
    @DisplayName("Deve criar Remetente com nome completo")
    void deveCriarRemetenteComNomeCompleto() {
        // Given
        RemetenteDTO remetente = new RemetenteDTO();
        String nomeCompleto = "Maria Silva dos Santos";
        
        // When
        remetente.setNome(nomeCompleto);
        
        // Then
        assertEquals(nomeCompleto, remetente.getNome());
    }

    @Test
    @DisplayName("Deve criar Remetente com nome longo")
    void deveCriarRemetenteComNomeLongo() {
        // Given
        RemetenteDTO remetente = new RemetenteDTO();
        String nomeLongo = "Este é um nome muito longo para testar o comportamento do sistema quando recebe strings com muitos caracteres e verificar se há algum limite ou problema de performance";
        
        // When
        remetente.setNome(nomeLongo);
        
        // Then
        assertEquals(nomeLongo, remetente.getNome());
    }

    @Test
    @DisplayName("Deve criar Remetente com cargo simples")
    void deveCriarRemetenteComCargoSimples() {
        // Given
        RemetenteDTO remetente = new RemetenteDTO();
        String cargoSimples = "Analista";
        
        // When
        remetente.setCargo(cargoSimples);
        
        // Then
        assertEquals(cargoSimples, remetente.getCargo());
    }

    @Test
    @DisplayName("Deve criar Remetente com cargo com espaços")
    void deveCriarRemetenteComCargoComEspacos() {
        // Given
        RemetenteDTO remetente = new RemetenteDTO();
        String cargoComEspacos = "Analista de Recursos Humanos Sênior";
        
        // When
        remetente.setCargo(cargoComEspacos);
        
        // Then
        assertEquals(cargoComEspacos, remetente.getCargo());
    }

    @Test
    @DisplayName("Deve criar Remetente com cargo longo")
    void deveCriarRemetenteComCargoLongo() {
        // Given
        RemetenteDTO remetente = new RemetenteDTO();
        String cargoLongo = "Este é um cargo muito longo para testar o comportamento do sistema quando recebe strings com muitos caracteres e verificar se há algum limite ou problema de performance";
        
        // When
        remetente.setCargo(cargoLongo);
        
        // Then
        assertEquals(cargoLongo, remetente.getCargo());
    }

    @Test
    @DisplayName("Deve verificar equals e hashCode")
    void deveVerificarEqualsEHashCode() {
        // Given
        RemetenteDTO remetente1 = new RemetenteDTO();
        remetente1.setUsuarioId(1L);
        remetente1.setNome("Maria Silva");
        remetente1.setCargo("Analista de RH");
        
        RemetenteDTO remetente2 = new RemetenteDTO();
        remetente2.setUsuarioId(1L);
        remetente2.setNome("Maria Silva");
        remetente2.setCargo("Analista de RH");
        
        RemetenteDTO remetente3 = new RemetenteDTO();
        remetente3.setUsuarioId(2L);
        remetente3.setNome("João Silva");
        remetente3.setCargo("Desenvolvedor");
        
        // When & Then
        assertEquals(remetente1, remetente2);
        assertNotEquals(remetente1, remetente3);
        assertEquals(remetente1.hashCode(), remetente2.hashCode());
        assertNotEquals(remetente1.hashCode(), remetente3.hashCode());
    }

    @Test
    @DisplayName("Deve verificar toString")
    void deveVerificarToString() {
        // Given
        RemetenteDTO remetente = new RemetenteDTO();
        remetente.setUsuarioId(1L);
        remetente.setNome("Maria Silva");
        remetente.setCargo("Analista de RH");
        
        // When
        String toString = remetente.toString();
        
        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("usuarioId=1"));
        assertTrue(toString.contains("nome=Maria Silva"));
        assertTrue(toString.contains("cargo=Analista de RH"));
    }

    @Test
    @DisplayName("Deve criar Remetente com caracteres especiais")
    void deveCriarRemetenteComCaracteresEspeciais() {
        // Given
        RemetenteDTO remetente = new RemetenteDTO();
        String nomeComEspeciais = "Maria Silva dos Santos";
        String cargoComEspeciais = "Analista de Recursos Humanos & Administração";
        
        // When
        remetente.setNome(nomeComEspeciais);
        remetente.setCargo(cargoComEspeciais);
        
        // Then
        assertEquals(nomeComEspeciais, remetente.getNome());
        assertEquals(cargoComEspeciais, remetente.getCargo());
    }

    @Test
    @DisplayName("Deve criar Remetente com valores extremos")
    void deveCriarRemetenteComValoresExtremos() {
        // Given
        RemetenteDTO remetente = new RemetenteDTO();
        Long usuarioIdMaximo = Long.MAX_VALUE;
        String nomeExtremo = "A".repeat(1000);
        String cargoExtremo = "B".repeat(1000);
        
        // When
        remetente.setUsuarioId(usuarioIdMaximo);
        remetente.setNome(nomeExtremo);
        remetente.setCargo(cargoExtremo);
        
        // Then
        assertEquals(usuarioIdMaximo, remetente.getUsuarioId());
        assertEquals(nomeExtremo, remetente.getNome());
        assertEquals(cargoExtremo, remetente.getCargo());
    }

    @Test
    @DisplayName("Deve criar Remetente com espaços em branco")
    void deveCriarRemetenteComEspacosEmBranco() {
        // Given
        RemetenteDTO remetente = new RemetenteDTO();
        String nomeComEspacos = "  Maria Silva  ";
        String cargoComEspacos = "  Analista de RH  ";
        
        // When
        remetente.setNome(nomeComEspacos);
        remetente.setCargo(cargoComEspacos);
        
        // Then
        assertEquals(nomeComEspacos, remetente.getNome());
        assertEquals(cargoComEspacos, remetente.getCargo());
    }

    @Test
    @DisplayName("Deve criar Remetente com cargo com quebras de linha")
    void deveCriarRemetenteComCargoComQuebrasDeLinha() {
        // Given
        RemetenteDTO remetente = new RemetenteDTO();
        String cargoComQuebras = "Analista de RH\nEspecialista em Recrutamento\nGestor de Pessoas";
        
        // When
        remetente.setCargo(cargoComQuebras);
        
        // Then
        assertEquals(cargoComQuebras, remetente.getCargo());
    }

    @Test
    @DisplayName("Deve criar Remetente com cargo com tabs")
    void deveCriarRemetenteComCargoComTabs() {
        // Given
        RemetenteDTO remetente = new RemetenteDTO();
        String cargoComTabs = "Analista\tde\tRH";
        
        // When
        remetente.setCargo(cargoComTabs);
        
        // Then
        assertEquals(cargoComTabs, remetente.getCargo());
    }
} 
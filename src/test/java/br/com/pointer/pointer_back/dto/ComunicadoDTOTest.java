package br.com.pointer.pointer_back.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

@DisplayName("ComunicadoDTO")
class ComunicadoDTOTest {

    @Test
    @DisplayName("Deve criar comunicado com todos os campos")
    void deveCriarComunicadoComTodosOsCampos() {
        // Given
        Long id = 1L;
        String titulo = "Comunicado Importante";
        String descricao = "Este é um comunicado de teste para verificar o funcionamento do sistema";
        Set<String> setores = Set.of("TI", "RH");
        boolean apenasGestores = false;
        boolean ativo = true;
        LocalDateTime dataPublicacao = LocalDateTime.now();
        
        // When
        ComunicadoDTO comunicado = new ComunicadoDTO();
        comunicado.setId(id);
        comunicado.setTitulo(titulo);
        comunicado.setDescricao(descricao);
        comunicado.setSetores(setores);
        comunicado.setApenasGestores(apenasGestores);
        comunicado.setAtivo(ativo);
        comunicado.setDataPublicacao(dataPublicacao);
        
        // Then
        assertNotNull(comunicado);
        assertEquals(id, comunicado.getId());
        assertEquals(titulo, comunicado.getTitulo());
        assertEquals(descricao, comunicado.getDescricao());
        assertEquals(setores, comunicado.getSetores());
        assertEquals(apenasGestores, comunicado.isApenasGestores());
        assertEquals(ativo, comunicado.isAtivo());
        assertEquals(dataPublicacao, comunicado.getDataPublicacao());
    }

    @Test
    @DisplayName("Deve criar comunicado com campos nulos")
    void deveCriarComunicadoComCamposNulos() {
        // Given
        ComunicadoDTO comunicado = new ComunicadoDTO();
        
        // When & Then
        assertNotNull(comunicado);
        assertNull(comunicado.getId());
        assertNull(comunicado.getTitulo());
        assertNull(comunicado.getDescricao());
        assertNull(comunicado.getSetores());
        assertFalse(comunicado.isApenasGestores());
        assertFalse(comunicado.isAtivo());
        assertNull(comunicado.getDataPublicacao());
    }

    @Test
    @DisplayName("Deve criar comunicado com campos vazios")
    void deveCriarComunicadoComCamposVazios() {
        // Given
        ComunicadoDTO comunicado = new ComunicadoDTO();
        comunicado.setTitulo("");
        comunicado.setDescricao("");
        comunicado.setSetores(Set.of());
        
        // When & Then
        assertNotNull(comunicado);
        assertEquals("", comunicado.getTitulo());
        assertEquals("", comunicado.getDescricao());
        assertTrue(comunicado.getSetores().isEmpty());
    }

    @Test
    @DisplayName("Deve criar comunicado com apenasGestores true")
    void deveCriarComunicadoComApenasGestoresTrue() {
        // Given
        ComunicadoDTO comunicado = new ComunicadoDTO();
        boolean apenasGestores = true;
        
        // When
        comunicado.setApenasGestores(apenasGestores);
        
        // Then
        assertTrue(comunicado.isApenasGestores());
    }

    @Test
    @DisplayName("Deve criar comunicado com apenasGestores false")
    void deveCriarComunicadoComApenasGestoresFalse() {
        // Given
        ComunicadoDTO comunicado = new ComunicadoDTO();
        boolean apenasGestores = false;
        
        // When
        comunicado.setApenasGestores(apenasGestores);
        
        // Then
        assertFalse(comunicado.isApenasGestores());
    }

    @Test
    @DisplayName("Deve criar comunicado com ativo true")
    void deveCriarComunicadoComAtivoTrue() {
        // Given
        ComunicadoDTO comunicado = new ComunicadoDTO();
        boolean ativo = true;
        
        // When
        comunicado.setAtivo(ativo);
        
        // Then
        assertTrue(comunicado.isAtivo());
    }

    @Test
    @DisplayName("Deve criar comunicado com ativo false")
    void deveCriarComunicadoComAtivoFalse() {
        // Given
        ComunicadoDTO comunicado = new ComunicadoDTO();
        boolean ativo = false;
        
        // When
        comunicado.setAtivo(ativo);
        
        // Then
        assertFalse(comunicado.isAtivo());
    }

    @Test
    @DisplayName("Deve criar comunicado com ID negativo")
    void deveCriarComunicadoComIdNegativo() {
        // Given
        ComunicadoDTO comunicado = new ComunicadoDTO();
        Long idNegativo = -1L;
        
        // When
        comunicado.setId(idNegativo);
        
        // Then
        assertEquals(idNegativo, comunicado.getId());
    }

    @Test
    @DisplayName("Deve criar comunicado com ID zero")
    void deveCriarComunicadoComIdZero() {
        // Given
        ComunicadoDTO comunicado = new ComunicadoDTO();
        Long idZero = 0L;
        
        // When
        comunicado.setId(idZero);
        
        // Then
        assertEquals(idZero, comunicado.getId());
    }

    @Test
    @DisplayName("Deve criar comunicado com título longo")
    void deveCriarComunicadoComTituloLongo() {
        // Given
        ComunicadoDTO comunicado = new ComunicadoDTO();
        String tituloLongo = "Este é um título muito longo para testar o comportamento do sistema quando recebe strings com muitos caracteres e verificar se há algum limite ou problema de performance";
        
        // When
        comunicado.setTitulo(tituloLongo);
        
        // Then
        assertEquals(tituloLongo, comunicado.getTitulo());
    }

    @Test
    @DisplayName("Deve criar comunicado com descrição longa")
    void deveCriarComunicadoComDescricaoLonga() {
        // Given
        ComunicadoDTO comunicado = new ComunicadoDTO();
        String descricaoLonga = "Esta é uma descrição muito longa para testar o comportamento do sistema quando recebe textos extensos. " +
                               "Pode conter múltiplos parágrafos e caracteres especiais como: @#$%^&*()_+-=[]{}|;':\",./<>? " +
                               "Também pode conter quebras de linha e espaços extras. " +
                               "O objetivo é verificar se o sistema consegue lidar adequadamente com textos de grande volume. " +
                               "Este texto pode ser ainda mais longo para testar limites extremos do sistema.";
        
        // When
        comunicado.setDescricao(descricaoLonga);
        
        // Then
        assertEquals(descricaoLonga, comunicado.getDescricao());
    }

    @Test
    @DisplayName("Deve criar comunicado com múltiplos setores")
    void deveCriarComunicadoComMultiplosSetores() {
        // Given
        ComunicadoDTO comunicado = new ComunicadoDTO();
        Set<String> setores = Set.of("TI", "RH", "Financeiro", "Marketing");
        
        // When
        comunicado.setSetores(setores);
        
        // Then
        assertEquals(setores, comunicado.getSetores());
        assertEquals(4, comunicado.getSetores().size());
    }

    @Test
    @DisplayName("Deve criar comunicado com setor especial")
    void deveCriarComunicadoComSetorEspecial() {
        // Given
        ComunicadoDTO comunicado = new ComunicadoDTO();
        Set<String> setores = Set.of("TI & Desenvolvimento");
        
        // When
        comunicado.setSetores(setores);
        
        // Then
        assertEquals(setores, comunicado.getSetores());
    }

    @Test
    @DisplayName("Deve criar comunicado com data específica")
    void deveCriarComunicadoComDataEspecifica() {
        // Given
        ComunicadoDTO comunicado = new ComunicadoDTO();
        LocalDateTime dataPublicacao = LocalDateTime.of(2023, 1, 1, 10, 0, 0);
        
        // When
        comunicado.setDataPublicacao(dataPublicacao);
        
        // Then
        assertEquals(dataPublicacao, comunicado.getDataPublicacao());
    }

    @Test
    @DisplayName("Deve criar comunicado com data nula")
    void deveCriarComunicadoComDataNula() {
        // Given
        ComunicadoDTO comunicado = new ComunicadoDTO();
        
        // When
        comunicado.setDataPublicacao(null);
        
        // Then
        assertNull(comunicado.getDataPublicacao());
    }

    @Test
    @DisplayName("Deve verificar equals e hashCode")
    void deveVerificarEqualsEHashCode() {
        // Given
        ComunicadoDTO comunicado1 = new ComunicadoDTO();
        comunicado1.setId(1L);
        comunicado1.setTitulo("Teste");
        comunicado1.setApenasGestores(false);
        
        ComunicadoDTO comunicado2 = new ComunicadoDTO();
        comunicado2.setId(1L);
        comunicado2.setTitulo("Teste");
        comunicado2.setApenasGestores(false);
        
        ComunicadoDTO comunicado3 = new ComunicadoDTO();
        comunicado3.setId(2L);
        comunicado3.setTitulo("Teste Diferente");
        comunicado3.setApenasGestores(true);
        
        // When & Then
        assertEquals(comunicado1, comunicado2);
        assertNotEquals(comunicado1, comunicado3);
        assertEquals(comunicado1.hashCode(), comunicado2.hashCode());
        assertNotEquals(comunicado1.hashCode(), comunicado3.hashCode());
    }

    @Test
    @DisplayName("Deve verificar toString")
    void deveVerificarToString() {
        // Given
        ComunicadoDTO comunicado = new ComunicadoDTO();
        comunicado.setId(1L);
        comunicado.setTitulo("Teste");
        comunicado.setApenasGestores(false);
        
        // When
        String toString = comunicado.toString();
        
        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("titulo=Teste"));
        assertTrue(toString.contains("apenasGestores=false"));
    }

    @Test
    @DisplayName("Deve criar comunicado com caracteres especiais")
    void deveCriarComunicadoComCaracteresEspeciais() {
        // Given
        ComunicadoDTO comunicado = new ComunicadoDTO();
        String tituloComEspeciais = "Título com acentos: áéíóú çãõ";
        String descricaoComEspeciais = "Descrição com símbolos: @#$%^&*()_+-=[]{}|;':\",./<>? e emojis: 😀🎉🚀";
        Set<String> setoresComEspeciais = Set.of("TI & Desenvolvimento");
        
        // When
        comunicado.setTitulo(tituloComEspeciais);
        comunicado.setDescricao(descricaoComEspeciais);
        comunicado.setSetores(setoresComEspeciais);
        
        // Then
        assertEquals(tituloComEspeciais, comunicado.getTitulo());
        assertEquals(descricaoComEspeciais, comunicado.getDescricao());
        assertEquals(setoresComEspeciais, comunicado.getSetores());
    }

    @Test
    @DisplayName("Deve criar comunicado com valores máximos")
    void deveCriarComunicadoComValoresMaximos() {
        // Given
        ComunicadoDTO comunicado = new ComunicadoDTO();
        Long idMaximo = Long.MAX_VALUE;
        
        // When
        comunicado.setId(idMaximo);
        
        // Then
        assertEquals(idMaximo, comunicado.getId());
    }
} 
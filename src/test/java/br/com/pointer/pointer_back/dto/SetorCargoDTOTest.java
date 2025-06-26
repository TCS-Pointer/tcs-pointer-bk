package br.com.pointer.pointer_back.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

@DisplayName("SetorCargoDTO")
class SetorCargoDTOTest {

    @Test
    @DisplayName("Deve criar SetorCargo com todos os campos")
    void deveCriarSetorCargoComTodosOsCampos() {
        // Given
        SetorCargoDTO setorCargo = new SetorCargoDTO();
        SetorCargoDTO.Setor setor = new SetorCargoDTO.Setor();
        setor.setSetor("TI");
        setor.setCargos(Arrays.asList("Desenvolvedor", "Analista"));
        setorCargo.setSetores(Arrays.asList(setor));
        
        // Then
        assertNotNull(setorCargo);
        assertEquals("TI", setorCargo.getSetores().get(0).getSetor());
        assertEquals(Arrays.asList("Desenvolvedor", "Analista"), setorCargo.getSetores().get(0).getCargos());
    }

    @Test
    @DisplayName("Deve criar SetorCargo com campos nulos")
    void deveCriarSetorCargoComCamposNulos() {
        // Given
        SetorCargoDTO setorCargo = new SetorCargoDTO();
        
        // When & Then
        assertNotNull(setorCargo);
        assertNull(setorCargo.getSetores());
    }

    @Test
    @DisplayName("Deve criar SetorCargo com campos vazios")
    void deveCriarSetorCargoComCamposVazios() {
        // Given
        SetorCargoDTO setorCargo = new SetorCargoDTO();
        SetorCargoDTO.Setor setor = new SetorCargoDTO.Setor();
        setor.setSetor("");
        setor.setCargos(Arrays.asList(""));
        setorCargo.setSetores(Arrays.asList(setor));
        
        // When & Then
        assertNotNull(setorCargo);
        assertEquals("", setorCargo.getSetores().get(0).getSetor());
        assertEquals(Arrays.asList(""), setorCargo.getSetores().get(0).getCargos());
    }

    @Test
    @DisplayName("Deve criar SetorCargo com setor simples")
    void deveCriarSetorCargoComSetorSimples() {
        // Given
        SetorCargoDTO setorCargo = new SetorCargoDTO();
        SetorCargoDTO.Setor setor = new SetorCargoDTO.Setor();
        String setorSimples = "TI";
        
        // When
        setor.setSetor(setorSimples);
        setorCargo.setSetores(Arrays.asList(setor));
        
        // Then
        assertEquals(setorSimples, setorCargo.getSetores().get(0).getSetor());
    }

    @Test
    @DisplayName("Deve criar SetorCargo com setor com espaços")
    void deveCriarSetorCargoComSetorComEspacos() {
        // Given
        SetorCargoDTO setorCargo = new SetorCargoDTO();
        SetorCargoDTO.Setor setor = new SetorCargoDTO.Setor();
        String setorComEspacos = "Recursos Humanos";
        
        // When
        setor.setSetor(setorComEspacos);
        setorCargo.setSetores(Arrays.asList(setor));
        
        // Then
        assertEquals(setorComEspacos, setorCargo.getSetores().get(0).getSetor());
    }

    @Test
    @DisplayName("Deve criar SetorCargo com setor longo")
    void deveCriarSetorCargoComSetorLongo() {
        // Given
        SetorCargoDTO setorCargo = new SetorCargoDTO();
        SetorCargoDTO.Setor setor = new SetorCargoDTO.Setor();
        String setorLongo = "Este é um setor muito longo para testar o comportamento do sistema quando recebe strings com muitos caracteres e verificar se há algum limite ou problema de performance";
        
        // When
        setor.setSetor(setorLongo);
        setorCargo.setSetores(Arrays.asList(setor));
        
        // Then
        assertEquals(setorLongo, setorCargo.getSetores().get(0).getSetor());
    }

    @Test
    @DisplayName("Deve criar SetorCargo com cargo simples")
    void deveCriarSetorCargoComCargoSimples() {
        // Given
        SetorCargoDTO setorCargo = new SetorCargoDTO();
        SetorCargoDTO.Setor setor = new SetorCargoDTO.Setor();
        String cargoSimples = "Analista";
        
        // When
        setor.setCargos(Arrays.asList(cargoSimples));
        setorCargo.setSetores(Arrays.asList(setor));
        
        // Then
        assertEquals(cargoSimples, setorCargo.getSetores().get(0).getCargos().get(0));
    }

    @Test
    @DisplayName("Deve criar SetorCargo com cargo com espaços")
    void deveCriarSetorCargoComCargoComEspacos() {
        // Given
        SetorCargoDTO setorCargo = new SetorCargoDTO();
        SetorCargoDTO.Setor setor = new SetorCargoDTO.Setor();
        String cargoComEspacos = "Analista de Sistemas Sênior";
        
        // When
        setor.setCargos(Arrays.asList(cargoComEspacos));
        setorCargo.setSetores(Arrays.asList(setor));
        
        // Then
        assertEquals(cargoComEspacos, setorCargo.getSetores().get(0).getCargos().get(0));
    }

    @Test
    @DisplayName("Deve criar SetorCargo com cargo longo")
    void deveCriarSetorCargoComCargoLongo() {
        // Given
        SetorCargoDTO setorCargo = new SetorCargoDTO();
        SetorCargoDTO.Setor setor = new SetorCargoDTO.Setor();
        String cargoLongo = "Este é um cargo muito longo para testar o comportamento do sistema quando recebe strings com muitos caracteres e verificar se há algum limite ou problema de performance";
        
        // When
        setor.setCargos(Arrays.asList(cargoLongo));
        setorCargo.setSetores(Arrays.asList(setor));
        
        // Then
        assertEquals(cargoLongo, setorCargo.getSetores().get(0).getCargos().get(0));
    }

    @Test
    @DisplayName("Deve criar SetorCargo com múltiplos cargos")
    void deveCriarSetorCargoComMultiplosCargos() {
        // Given
        SetorCargoDTO setorCargo = new SetorCargoDTO();
        SetorCargoDTO.Setor setor = new SetorCargoDTO.Setor();
        setor.setSetor("TI");
        setor.setCargos(Arrays.asList("Desenvolvedor", "Analista", "Gerente"));
        setorCargo.setSetores(Arrays.asList(setor));
        
        // Then
        assertEquals("TI", setorCargo.getSetores().get(0).getSetor());
        assertEquals(Arrays.asList("Desenvolvedor", "Analista", "Gerente"), setorCargo.getSetores().get(0).getCargos());
    }

    @Test
    @DisplayName("Deve criar SetorCargo com múltiplos setores")
    void deveCriarSetorCargoComMultiplosSetores() {
        // Given
        SetorCargoDTO setorCargo = new SetorCargoDTO();
        SetorCargoDTO.Setor setor1 = new SetorCargoDTO.Setor();
        setor1.setSetor("TI");
        setor1.setCargos(Arrays.asList("Desenvolvedor"));
        
        SetorCargoDTO.Setor setor2 = new SetorCargoDTO.Setor();
        setor2.setSetor("RH");
        setor2.setCargos(Arrays.asList("Analista"));
        
        setorCargo.setSetores(Arrays.asList(setor1, setor2));
        
        // Then
        assertEquals(2, setorCargo.getSetores().size());
        assertEquals("TI", setorCargo.getSetores().get(0).getSetor());
        assertEquals("RH", setorCargo.getSetores().get(1).getSetor());
    }

    @Test
    @DisplayName("Deve verificar equals e hashCode")
    void deveVerificarEqualsEHashCode() {
        // Given
        SetorCargoDTO setorCargo1 = new SetorCargoDTO();
        SetorCargoDTO.Setor setor1 = new SetorCargoDTO.Setor();
        setor1.setSetor("TI");
        setor1.setCargos(Arrays.asList("Desenvolvedor"));
        setorCargo1.setSetores(Arrays.asList(setor1));
        
        SetorCargoDTO setorCargo2 = new SetorCargoDTO();
        SetorCargoDTO.Setor setor2 = new SetorCargoDTO.Setor();
        setor2.setSetor("TI");
        setor2.setCargos(Arrays.asList("Desenvolvedor"));
        setorCargo2.setSetores(Arrays.asList(setor2));
        
        SetorCargoDTO setorCargo3 = new SetorCargoDTO();
        SetorCargoDTO.Setor setor3 = new SetorCargoDTO.Setor();
        setor3.setSetor("RH");
        setor3.setCargos(Arrays.asList("Analista"));
        setorCargo3.setSetores(Arrays.asList(setor3));
        
        // Then
        assertEquals(setorCargo1, setorCargo2);
        assertNotEquals(setorCargo1, setorCargo3);
        assertEquals(setorCargo1.hashCode(), setorCargo2.hashCode());
        assertNotEquals(setorCargo1.hashCode(), setorCargo3.hashCode());
    }

    @Test
    @DisplayName("Deve verificar toString")
    void deveVerificarToString() {
        // Given
        SetorCargoDTO setorCargo = new SetorCargoDTO();
        SetorCargoDTO.Setor setor = new SetorCargoDTO.Setor();
        setor.setSetor("TI");
        setor.setCargos(Arrays.asList("Desenvolvedor"));
        setorCargo.setSetores(Arrays.asList(setor));
        
        // When
        String toString = setorCargo.toString();
        
        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("TI"));
        assertTrue(toString.contains("Desenvolvedor"));
    }

    @Test
    @DisplayName("Deve criar SetorCargo com caracteres especiais")
    void deveCriarSetorCargoComCaracteresEspeciais() {
        // Given
        SetorCargoDTO setorCargo = new SetorCargoDTO();
        SetorCargoDTO.Setor setor = new SetorCargoDTO.Setor();
        setor.setSetor("TI & Desenvolvimento");
        setor.setCargos(Arrays.asList("Desenvolvedor@Sênior"));
        setorCargo.setSetores(Arrays.asList(setor));
        
        // Then
        assertEquals("TI & Desenvolvimento", setorCargo.getSetores().get(0).getSetor());
        assertEquals("Desenvolvedor@Sênior", setorCargo.getSetores().get(0).getCargos().get(0));
    }

    @Test
    @DisplayName("Deve criar SetorCargo com valores extremos")
    void deveCriarSetorCargoComValoresExtremos() {
        // Given
        SetorCargoDTO setorCargo = new SetorCargoDTO();
        SetorCargoDTO.Setor setor = new SetorCargoDTO.Setor();
        setor.setSetor("A");
        setor.setCargos(Arrays.asList("B"));
        setorCargo.setSetores(Arrays.asList(setor));
        
        // Then
        assertEquals("A", setorCargo.getSetores().get(0).getSetor());
        assertEquals("B", setorCargo.getSetores().get(0).getCargos().get(0));
    }

    @Test
    @DisplayName("Deve criar SetorCargo com espaços em branco")
    void deveCriarSetorCargoComEspacosEmBranco() {
        // Given
        SetorCargoDTO setorCargo = new SetorCargoDTO();
        SetorCargoDTO.Setor setor = new SetorCargoDTO.Setor();
        setor.setSetor("   TI   ");
        setor.setCargos(Arrays.asList("   Desenvolvedor   "));
        setorCargo.setSetores(Arrays.asList(setor));
        
        // Then
        assertEquals("   TI   ", setorCargo.getSetores().get(0).getSetor());
        assertEquals("   Desenvolvedor   ", setorCargo.getSetores().get(0).getCargos().get(0));
    }

    @Test
    @DisplayName("Deve criar SetorCargo com setor com quebras de linha")
    void deveCriarSetorCargoComSetorComQuebrasDeLinha() {
        // Given
        SetorCargoDTO setorCargo = new SetorCargoDTO();
        SetorCargoDTO.Setor setor = new SetorCargoDTO.Setor();
        String setorComQuebra = "TI\nDesenvolvimento";
        
        // When
        setor.setSetor(setorComQuebra);
        setorCargo.setSetores(Arrays.asList(setor));
        
        // Then
        assertEquals(setorComQuebra, setorCargo.getSetores().get(0).getSetor());
    }

    @Test
    @DisplayName("Deve criar SetorCargo com cargo com quebras de linha")
    void deveCriarSetorCargoComCargoComQuebrasDeLinha() {
        // Given
        SetorCargoDTO setorCargo = new SetorCargoDTO();
        SetorCargoDTO.Setor setor = new SetorCargoDTO.Setor();
        String cargoComQuebra = "Desenvolvedor\nSênior";
        
        // When
        setor.setCargos(Arrays.asList(cargoComQuebra));
        setorCargo.setSetores(Arrays.asList(setor));
        
        // Then
        assertEquals(cargoComQuebra, setorCargo.getSetores().get(0).getCargos().get(0));
    }

    @Test
    @DisplayName("Deve criar SetorCargo com setor com tabs")
    void deveCriarSetorCargoComSetorComTabs() {
        // Given
        SetorCargoDTO setorCargo = new SetorCargoDTO();
        SetorCargoDTO.Setor setor = new SetorCargoDTO.Setor();
        String setorComTab = "TI\tDesenvolvimento";
        
        // When
        setor.setSetor(setorComTab);
        setorCargo.setSetores(Arrays.asList(setor));
        
        // Then
        assertEquals(setorComTab, setorCargo.getSetores().get(0).getSetor());
    }

    @Test
    @DisplayName("Deve criar SetorCargo com cargo com tabs")
    void deveCriarSetorCargoComCargoComTabs() {
        // Given
        SetorCargoDTO setorCargo = new SetorCargoDTO();
        SetorCargoDTO.Setor setor = new SetorCargoDTO.Setor();
        String cargoComTab = "Desenvolvedor\tSênior";
        
        // When
        setor.setCargos(Arrays.asList(cargoComTab));
        setorCargo.setSetores(Arrays.asList(setor));
        
        // Then
        assertEquals(cargoComTab, setorCargo.getSetores().get(0).getCargos().get(0));
    }

    @Test
    @DisplayName("Deve criar SetorCargo com setor nulo")
    void deveCriarSetorCargoComSetorNulo() {
        // Given
        SetorCargoDTO setorCargo = new SetorCargoDTO();
        SetorCargoDTO.Setor setor = new SetorCargoDTO.Setor();
        
        // When
        setor.setSetor(null);
        setorCargo.setSetores(Arrays.asList(setor));
        
        // Then
        assertNull(setorCargo.getSetores().get(0).getSetor());
    }

    @Test
    @DisplayName("Deve criar SetorCargo com cargo nulo")
    void deveCriarSetorCargoComCargoNulo() {
        // Given
        SetorCargoDTO setorCargo = new SetorCargoDTO();
        SetorCargoDTO.Setor setor = new SetorCargoDTO.Setor();
        
        // When
        setor.setCargos(Arrays.asList((String) null));
        setorCargo.setSetores(Arrays.asList(setor));
        
        // Then
        assertNull(setorCargo.getSetores().get(0).getCargos().get(0));
    }

    @Test
    @DisplayName("Deve criar SetorCargo com lista de setores nula")
    void deveCriarSetorCargoComListaDeSetoresNula() {
        // Given
        SetorCargoDTO setorCargo = new SetorCargoDTO();
        
        // When
        setorCargo.setSetores(null);
        
        // Then
        assertNull(setorCargo.getSetores());
    }
} 
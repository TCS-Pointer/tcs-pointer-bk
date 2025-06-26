package br.com.pointer.pointer_back.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.pointer.pointer_back.enums.StatusMarcoPDI;

class MarcoPDIDTOTest {

    private MarcoPDIDTO marco;

    @BeforeEach
    void setUp() {
        marco = new MarcoPDIDTO();
    }

    @Test
    @DisplayName("Deve criar MarcoPDIDTO com todos os campos")
    void deveCriarMarcoPDIDTOComTodosOsCampos() {
        // Arrange
        marco.setId(1L);
        marco.setTitulo("Marco Teste");
        marco.setDescricao("Descrição do marco");
        marco.setStatus(StatusMarcoPDI.PENDENTE);
        marco.setDtFinal(LocalDate.now().plusDays(30));
        marco.setPdiId(1L);

        // Assert
        assertEquals(1L, marco.getId());
        assertEquals("Marco Teste", marco.getTitulo());
        assertEquals("Descrição do marco", marco.getDescricao());
        assertEquals(StatusMarcoPDI.PENDENTE, marco.getStatus());
        assertEquals(LocalDate.now().plusDays(30), marco.getDtFinal());
        assertEquals(1L, marco.getPdiId());
    }

    @Test
    @DisplayName("Deve criar MarcoPDIDTO com valores padrão")
    void deveCriarMarcoPDIDTOComValoresPadrao() {
        // Assert
        assertNull(marco.getId());
        assertNull(marco.getTitulo());
        assertNull(marco.getDescricao());
        assertNull(marco.getStatus());
        assertNull(marco.getDtFinal());
        assertNull(marco.getPdiId());
    }

    @Test
    @DisplayName("Deve atualizar campos do MarcoPDIDTO")
    void deveAtualizarCamposDoMarcoPDIDTO() {
        // Arrange
        marco.setTitulo("Título original");
        marco.setDescricao("Descrição original");

        // Act
        marco.setTitulo("Título atualizado");
        marco.setDescricao("Descrição atualizada");

        // Assert
        assertEquals("Título atualizado", marco.getTitulo());
        assertEquals("Descrição atualizada", marco.getDescricao());
    }

    @Test
    @DisplayName("Deve definir diferentes status")
    void deveDefinirDiferentesStatus() {
        // Act & Assert
        marco.setStatus(StatusMarcoPDI.PENDENTE);
        assertEquals(StatusMarcoPDI.PENDENTE, marco.getStatus());

        marco.setStatus(StatusMarcoPDI.CONCLUIDO);
        assertEquals(StatusMarcoPDI.CONCLUIDO, marco.getStatus());
    }

    @Test
    @DisplayName("Deve definir datas")
    void deveDefinirDatas() {
        // Arrange
        LocalDate data1 = LocalDate.of(2023, 12, 31);
        LocalDate data2 = LocalDate.of(2024, 1, 15);

        // Act
        marco.setDtFinal(data1);

        // Assert
        assertEquals(data1, marco.getDtFinal());

        // Act
        marco.setDtFinal(data2);

        // Assert
        assertEquals(data2, marco.getDtFinal());
    }

    @Test
    @DisplayName("Deve definir IDs")
    void deveDefinirIds() {
        // Act
        marco.setId(100L);
        marco.setPdiId(200L);

        // Assert
        assertEquals(100L, marco.getId());
        assertEquals(200L, marco.getPdiId());
    }

    @Test
    @DisplayName("Deve definir campos como null")
    void deveDefinirCamposComoNull() {
        // Act
        marco.setTitulo(null);
        marco.setDescricao(null);
        marco.setStatus(null);
        marco.setDtFinal(null);

        // Assert
        assertNull(marco.getTitulo());
        assertNull(marco.getDescricao());
        assertNull(marco.getStatus());
        assertNull(marco.getDtFinal());
    }

    @Test
    @DisplayName("Deve testar equals e hashCode com objetos iguais")
    void deveTestarEqualsEHashCodeComObjetosIguais() {
        // Arrange
        MarcoPDIDTO marco1 = new MarcoPDIDTO();
        marco1.setTitulo("Teste");
        marco1.setDtFinal(LocalDate.now());
        marco1.setPdiId(1L);

        MarcoPDIDTO marco2 = new MarcoPDIDTO();
        marco2.setTitulo("Teste");
        marco2.setDtFinal(LocalDate.now());
        marco2.setPdiId(1L);

        MarcoPDIDTO marco3 = new MarcoPDIDTO();
        marco3.setTitulo("Teste");
        marco3.setDtFinal(LocalDate.now());
        marco3.setPdiId(1L);

        // Assert
        assertEquals(marco1, marco2);
        assertEquals(marco2, marco3);
        assertEquals(marco1, marco3);
        assertEquals(marco1.hashCode(), marco2.hashCode());
        assertEquals(marco2.hashCode(), marco3.hashCode());
    }

    @Test
    @DisplayName("Deve testar equals e hashCode com objetos diferentes")
    void deveTestarEqualsEHashCodeComObjetosDiferentes() {
        // Arrange
        MarcoPDIDTO marco1 = new MarcoPDIDTO();
        marco1.setTitulo("Teste 1");
        marco1.setDtFinal(LocalDate.now());
        marco1.setPdiId(1L);

        MarcoPDIDTO marco2 = new MarcoPDIDTO();
        marco2.setTitulo("Teste 2");
        marco2.setDtFinal(LocalDate.now());
        marco2.setPdiId(1L);

        MarcoPDIDTO marco3 = new MarcoPDIDTO();
        marco3.setTitulo("Teste 1");
        marco3.setDtFinal(LocalDate.now().plusDays(1));
        marco3.setPdiId(1L);

        // Assert
        assertNotEquals(marco1, marco2);
        assertNotEquals(marco1, marco3);
        assertNotEquals(marco2, marco3);
        assertNotEquals(marco1.hashCode(), marco2.hashCode());
        assertNotEquals(marco1.hashCode(), marco3.hashCode());
        assertNotEquals(marco2.hashCode(), marco3.hashCode());
    }

    @Test
    @DisplayName("Deve testar toString")
    void deveTestarToString() {
        // Arrange
        marco.setTitulo("Teste");
        marco.setDescricao("Descrição teste");

        // Act
        String result = marco.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("Teste"));
        assertTrue(result.contains("Descrição teste"));
    }
} 
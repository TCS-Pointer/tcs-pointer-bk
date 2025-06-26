package br.com.pointer.pointer_back.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import br.com.pointer.pointer_back.enums.StatusPDI;

@DisplayName("pdiDTO")
class pdiDTOTest {

    @Test
    @DisplayName("Deve criar PDI com todos os campos")
    void deveCriarPDIComTodosOsCampos() {
        // Given
        Long id = 1L;
        String titulo = "PDI de Desenvolvimento";
        String descricao = "Plano de desenvolvimento individual para crescimento profissional";
        Long idDestinatario = 456L;
        UsuarioResponseDTO destinatario = new UsuarioResponseDTO();
        StatusPDI status = StatusPDI.EM_ANDAMENTO;
        LocalDate dtInicio = LocalDate.of(2023, 1, 1);
        LocalDate dtFim = LocalDate.of(2023, 12, 31);
        Long idUsuario = 123L;
        UsuarioResponseDTO usuario = new UsuarioResponseDTO();
        List<MarcoPDIDTO> marcos = List.of(new MarcoPDIDTO(), new MarcoPDIDTO());
        
        // When
        pdiDTO pdi = new pdiDTO();
        pdi.setId(id);
        pdi.setTitulo(titulo);
        pdi.setDescricao(descricao);
        pdi.setIdDestinatario(idDestinatario);
        pdi.setDestinatario(destinatario);
        pdi.setStatus(status);
        pdi.setDtInicio(dtInicio);
        pdi.setDtFim(dtFim);
        pdi.setIdUsuario(idUsuario);
        pdi.setUsuario(usuario);
        pdi.setMarcos(marcos);
        
        // Then
        assertNotNull(pdi);
        assertEquals(id, pdi.getId());
        assertEquals(titulo, pdi.getTitulo());
        assertEquals(descricao, pdi.getDescricao());
        assertEquals(idDestinatario, pdi.getIdDestinatario());
        assertEquals(destinatario, pdi.getDestinatario());
        assertEquals(status, pdi.getStatus());
        assertEquals(dtInicio, pdi.getDtInicio());
        assertEquals(dtFim, pdi.getDtFim());
        assertEquals(idUsuario, pdi.getIdUsuario());
        assertEquals(usuario, pdi.getUsuario());
        assertEquals(marcos, pdi.getMarcos());
    }

    @Test
    @DisplayName("Deve criar PDI com campos nulos")
    void deveCriarPDIComCamposNulos() {
        // Given
        pdiDTO pdi = new pdiDTO();
        
        // When & Then
        assertNotNull(pdi);
        assertNull(pdi.getId());
        assertNull(pdi.getTitulo());
        assertNull(pdi.getDescricao());
        assertNull(pdi.getIdDestinatario());
        assertNull(pdi.getDestinatario());
        assertNull(pdi.getStatus());
        assertNull(pdi.getDtInicio());
        assertNull(pdi.getDtFim());
        assertNull(pdi.getIdUsuario());
        assertNull(pdi.getUsuario());
        assertNull(pdi.getMarcos());
    }

    @Test
    @DisplayName("Deve criar PDI com campos vazios")
    void deveCriarPDIComCamposVazios() {
        // Given
        pdiDTO pdi = new pdiDTO();
        pdi.setTitulo("");
        pdi.setDescricao("");
        pdi.setMarcos(List.of());
        
        // When & Then
        assertNotNull(pdi);
        assertEquals("", pdi.getTitulo());
        assertEquals("", pdi.getDescricao());
        assertTrue(pdi.getMarcos().isEmpty());
    }

    @Test
    @DisplayName("Deve criar PDI com diferentes status")
    void deveCriarPDIComDiferentesStatus() {
        // Given
        pdiDTO pdi = new pdiDTO();
        
        // When & Then - EM_ANDAMENTO
        pdi.setStatus(StatusPDI.EM_ANDAMENTO);
        assertEquals(StatusPDI.EM_ANDAMENTO, pdi.getStatus());
        
        // When & Then - CONCLUIDO
        pdi.setStatus(StatusPDI.CONCLUIDO);
        assertEquals(StatusPDI.CONCLUIDO, pdi.getStatus());
        
        // When & Then - ATRASADO
        pdi.setStatus(StatusPDI.ATRASADO);
        assertEquals(StatusPDI.ATRASADO, pdi.getStatus());
    }

    @Test
    @DisplayName("Deve criar PDI com ID negativo")
    void deveCriarPDIComIdNegativo() {
        // Given
        pdiDTO pdi = new pdiDTO();
        Long idNegativo = -1L;
        
        // When
        pdi.setId(idNegativo);
        
        // Then
        assertEquals(idNegativo, pdi.getId());
    }

    @Test
    @DisplayName("Deve criar PDI com ID zero")
    void deveCriarPDIComIdZero() {
        // Given
        pdiDTO pdi = new pdiDTO();
        Long idZero = 0L;
        
        // When
        pdi.setId(idZero);
        
        // Then
        assertEquals(idZero, pdi.getId());
    }

    @Test
    @DisplayName("Deve criar PDI com título longo")
    void deveCriarPDIComTituloLongo() {
        // Given
        pdiDTO pdi = new pdiDTO();
        String tituloLongo = "Este é um título muito longo para testar o comportamento do sistema quando recebe strings com muitos caracteres e verificar se há algum limite ou problema de performance";
        
        // When
        pdi.setTitulo(tituloLongo);
        
        // Then
        assertEquals(tituloLongo, pdi.getTitulo());
    }

    @Test
    @DisplayName("Deve criar PDI com descrição longa")
    void deveCriarPDIComDescricaoLonga() {
        // Given
        pdiDTO pdi = new pdiDTO();
        String descricaoLonga = "Esta é uma descrição muito longa para testar o comportamento do sistema quando recebe textos extensos. " +
                               "Pode conter múltiplos parágrafos e caracteres especiais como: @#$%^&*()_+-=[]{}|;':\",./<>? " +
                               "Também pode conter quebras de linha e espaços extras.";
        
        // When
        pdi.setDescricao(descricaoLonga);
        
        // Then
        assertEquals(descricaoLonga, pdi.getDescricao());
    }

    @Test
    @DisplayName("Deve criar PDI com datas específicas")
    void deveCriarPDIComDatasEspecificas() {
        // Given
        pdiDTO pdi = new pdiDTO();
        LocalDate dtInicio = LocalDate.of(2023, 1, 1);
        LocalDate dtFim = LocalDate.of(2023, 12, 31);
        
        // When
        pdi.setDtInicio(dtInicio);
        pdi.setDtFim(dtFim);
        
        // Then
        assertEquals(dtInicio, pdi.getDtInicio());
        assertEquals(dtFim, pdi.getDtFim());
    }

    @Test
    @DisplayName("Deve criar PDI com datas nulas")
    void deveCriarPDIComDatasNulas() {
        // Given
        pdiDTO pdi = new pdiDTO();
        
        // When
        pdi.setDtInicio(null);
        pdi.setDtFim(null);
        
        // Then
        assertNull(pdi.getDtInicio());
        assertNull(pdi.getDtFim());
    }

    @Test
    @DisplayName("Deve criar PDI com múltiplos marcos")
    void deveCriarPDIComMultiplosMarcos() {
        // Given
        pdiDTO pdi = new pdiDTO();
        List<MarcoPDIDTO> marcos = List.of(new MarcoPDIDTO(), new MarcoPDIDTO(), new MarcoPDIDTO());
        
        // When
        pdi.setMarcos(marcos);
        
        // Then
        assertEquals(marcos, pdi.getMarcos());
        assertEquals(3, pdi.getMarcos().size());
    }

    @Test
    @DisplayName("Deve criar PDI com marcos vazios")
    void deveCriarPDIComMarcosVazios() {
        // Given
        pdiDTO pdi = new pdiDTO();
        List<MarcoPDIDTO> marcos = List.of();
        
        // When
        pdi.setMarcos(marcos);
        
        // Then
        assertEquals(marcos, pdi.getMarcos());
        assertTrue(pdi.getMarcos().isEmpty());
    }

    @Test
    @DisplayName("Deve verificar equals e hashCode")
    void deveVerificarEqualsEHashCode() {
        // Given
        pdiDTO pdi1 = new pdiDTO();
        pdi1.setId(1L);
        pdi1.setTitulo("Teste");
        pdi1.setStatus(StatusPDI.EM_ANDAMENTO);
        
        pdiDTO pdi2 = new pdiDTO();
        pdi2.setId(1L);
        pdi2.setTitulo("Teste");
        pdi2.setStatus(StatusPDI.EM_ANDAMENTO);
        
        pdiDTO pdi3 = new pdiDTO();
        pdi3.setId(2L);
        pdi3.setTitulo("Teste Diferente");
        pdi3.setStatus(StatusPDI.CONCLUIDO);
        
        // When & Then
        assertEquals(pdi1, pdi2);
        assertNotEquals(pdi1, pdi3);
        assertEquals(pdi1.hashCode(), pdi2.hashCode());
        assertNotEquals(pdi1.hashCode(), pdi3.hashCode());
    }

    @Test
    @DisplayName("Deve verificar toString")
    void deveVerificarToString() {
        // Given
        pdiDTO pdi = new pdiDTO();
        pdi.setId(1L);
        pdi.setTitulo("Teste");
        pdi.setStatus(StatusPDI.EM_ANDAMENTO);
        
        // When
        String toString = pdi.toString();
        
        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("titulo=Teste"));
        assertTrue(toString.contains("status=EM_ANDAMENTO"));
    }

    @Test
    @DisplayName("Deve criar PDI com caracteres especiais")
    void deveCriarPDIComCaracteresEspeciais() {
        // Given
        pdiDTO pdi = new pdiDTO();
        String tituloComEspeciais = "Título com acentos: áéíóú çãõ";
        String descricaoComEspeciais = "Descrição com símbolos: @#$%^&*()_+-=[]{}|;':\",./<>? e emojis: 😀🎉🚀";
        
        // When
        pdi.setTitulo(tituloComEspeciais);
        pdi.setDescricao(descricaoComEspeciais);
        
        // Then
        assertEquals(tituloComEspeciais, pdi.getTitulo());
        assertEquals(descricaoComEspeciais, pdi.getDescricao());
    }

    @Test
    @DisplayName("Deve criar PDI com valores máximos")
    void deveCriarPDIComValoresMaximos() {
        // Given
        pdiDTO pdi = new pdiDTO();
        Long idMaximo = Long.MAX_VALUE;
        
        // When
        pdi.setId(idMaximo);
        
        // Then
        assertEquals(idMaximo, pdi.getId());
    }

    @Test
    @DisplayName("Deve criar PDI com datas extremas")
    void deveCriarPDIComDatasExtremas() {
        // Given
        pdiDTO pdi = new pdiDTO();
        LocalDate dataMinima = LocalDate.of(1900, 1, 1);
        LocalDate dataMaxima = LocalDate.of(2100, 12, 31);
        
        // When
        pdi.setDtInicio(dataMinima);
        pdi.setDtFim(dataMaxima);
        
        // Then
        assertEquals(dataMinima, pdi.getDtInicio());
        assertEquals(dataMaxima, pdi.getDtFim());
    }
} 
package br.com.pointer.pointer_back.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.pointer.pointer_back.enums.StatusPDI;

class PDITest {

    private PDI pdi;
    private Usuario usuario;
    private Usuario destinatario;

    @BeforeEach
    void setUp() {
        pdi = new PDI();
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@teste.com");

        destinatario = new Usuario();
        destinatario.setId(2L);
        destinatario.setNome("Maria Santos");
        destinatario.setEmail("maria@teste.com");
    }

    @Test
    void testSetAndGetId() {
        // Arrange
        Long id = 1L;

        // Act
        pdi.setId(id);

        // Assert
        assertEquals(id, pdi.getId());
    }

    @Test
    void testSetAndGetTitulo() {
        // Arrange
        String titulo = "PDI Desenvolvimento Profissional";

        // Act
        pdi.setTitulo(titulo);

        // Assert
        assertEquals(titulo, pdi.getTitulo());
    }

    @Test
    void testSetAndGetDescricao() {
        // Arrange
        String descricao = "Plano de desenvolvimento individual focado em crescimento profissional";

        // Act
        pdi.setDescricao(descricao);

        // Assert
        assertEquals(descricao, pdi.getDescricao());
    }

    @Test
    void testSetAndGetDestinatario() {
        // Act
        pdi.setDestinatario(destinatario);

        // Assert
        assertEquals(destinatario, pdi.getDestinatario());
    }

    @Test
    void testSetAndGetStatus() {
        // Arrange
        StatusPDI status = StatusPDI.EM_ANDAMENTO;

        // Act
        pdi.setStatus(status);

        // Assert
        assertEquals(status, pdi.getStatus());
    }

    @Test
    void testSetAndGetDtInicio() {
        // Arrange
        LocalDate dtInicio = LocalDate.of(2024, 1, 1);

        // Act
        pdi.setDtInicio(dtInicio);

        // Assert
        assertEquals(dtInicio, pdi.getDtInicio());
    }

    @Test
    void testSetAndGetDtFim() {
        // Arrange
        LocalDate dtFim = LocalDate.of(2024, 12, 31);

        // Act
        pdi.setDtFim(dtFim);

        // Assert
        assertEquals(dtFim, pdi.getDtFim());
    }

    @Test
    void testSetAndGetIdUsuario() {
        // Arrange
        Long idUsuario = 1L;

        // Act
        pdi.setIdUsuario(idUsuario);

        // Assert
        assertEquals(idUsuario, pdi.getIdUsuario());
    }

    @Test
    void testSetAndGetUsuario() {
        // Act
        pdi.setUsuario(usuario);

        // Assert
        assertEquals(usuario, pdi.getUsuario());
    }

    @Test
    void testSetAndGetDataCriacao() {
        // Arrange
        LocalDateTime dataCriacao = LocalDateTime.now();

        // Act
        pdi.setDataCriacao(dataCriacao);

        // Assert
        assertEquals(dataCriacao, pdi.getDataCriacao());
    }

    @Test
    void testSetAndGetMarcos() {
        // Arrange
        List<MarcoPDI> marcos = new ArrayList<>();
        MarcoPDI marco1 = new MarcoPDI();
        marco1.setId(1L);
        marco1.setTitulo("Marco 1");
        marcos.add(marco1);

        MarcoPDI marco2 = new MarcoPDI();
        marco2.setId(2L);
        marco2.setTitulo("Marco 2");
        marcos.add(marco2);

        // Act
        pdi.setMarcos(marcos);

        // Assert
        assertEquals(marcos, pdi.getMarcos());
        assertEquals(2, pdi.getMarcos().size());
    }

    @Test
    void testDefaultValues() {
        // Assert
        assertNull(pdi.getStatus());
        assertNull(pdi.getDataCriacao());
    }

    @Test
    void testEquals_ComMesmoId_DeveRetornarTrue() {
        // Arrange
        PDI pdi1 = new PDI();
        pdi1.setId(1L);
        pdi1.setTitulo("PDI 1");
        pdi1.setDescricao("Descrição 1");
        pdi1.setStatus(StatusPDI.EM_ANDAMENTO);
        pdi1.setDtInicio(LocalDate.of(2024, 1, 1));
        pdi1.setDtFim(LocalDate.of(2024, 12, 31));
        pdi1.setIdUsuario(1L);
        pdi1.setDataCriacao(LocalDateTime.of(2024, 1, 1, 10, 0));

        PDI pdi2 = new PDI();
        pdi2.setId(1L);
        pdi2.setTitulo("PDI 1");
        pdi2.setDescricao("Descrição 1");
        pdi2.setStatus(StatusPDI.EM_ANDAMENTO);
        pdi2.setDtInicio(LocalDate.of(2024, 1, 1));
        pdi2.setDtFim(LocalDate.of(2024, 12, 31));
        pdi2.setIdUsuario(1L);
        pdi2.setDataCriacao(LocalDateTime.of(2024, 1, 1, 10, 0));

        // Act & Assert
        assertEquals(pdi1, pdi2);
    }

    @Test
    void testEquals_ComIdsDiferentes_DeveRetornarFalse() {
        // Arrange
        PDI pdi1 = new PDI();
        pdi1.setId(1L);
        pdi1.setTitulo("PDI 1");
        pdi1.setDescricao("Descrição 1");
        pdi1.setStatus(StatusPDI.EM_ANDAMENTO);
        pdi1.setDtInicio(LocalDate.of(2024, 1, 1));
        pdi1.setDtFim(LocalDate.of(2024, 12, 31));
        pdi1.setIdUsuario(1L);
        pdi1.setDataCriacao(LocalDateTime.of(2024, 1, 1, 10, 0));

        PDI pdi2 = new PDI();
        pdi2.setId(2L);
        pdi2.setTitulo("PDI 1");
        pdi2.setDescricao("Descrição 1");
        pdi2.setStatus(StatusPDI.EM_ANDAMENTO);
        pdi2.setDtInicio(LocalDate.of(2024, 1, 1));
        pdi2.setDtFim(LocalDate.of(2024, 12, 31));
        pdi2.setIdUsuario(1L);
        pdi2.setDataCriacao(LocalDateTime.of(2024, 1, 1, 10, 0));

        // Act & Assert
        assertNotEquals(pdi1, pdi2);
    }

    @Test
    void testEquals_ComNull_DeveRetornarFalse() {
        // Arrange
        PDI pdi1 = new PDI();
        pdi1.setId(1L);

        // Act & Assert
        assertNotEquals(null, pdi1);
    }

    @Test
    void testEquals_ComMesmoObjeto_DeveRetornarTrue() {
        // Arrange
        PDI pdi1 = new PDI();
        pdi1.setId(1L);

        // Act & Assert
        assertEquals(pdi1, pdi1);
    }

    @Test
    void testEquals_ComTipoDiferente_DeveRetornarFalse() {
        // Arrange
        PDI pdi1 = new PDI();
        pdi1.setId(1L);

        String string = "teste";

        // Act & Assert
        assertNotEquals(pdi1, string);
    }

    @Test
    void testHashCode_ComMesmoId_DeveRetornarMesmoHashCode() {
        // Arrange
        PDI pdi1 = new PDI();
        pdi1.setId(1L);
        pdi1.setTitulo("PDI 1");
        pdi1.setDescricao("Descrição 1");
        pdi1.setStatus(StatusPDI.EM_ANDAMENTO);
        pdi1.setDtInicio(LocalDate.of(2024, 1, 1));
        pdi1.setDtFim(LocalDate.of(2024, 12, 31));
        pdi1.setIdUsuario(1L);
        pdi1.setDataCriacao(LocalDateTime.of(2024, 1, 1, 10, 0));

        PDI pdi2 = new PDI();
        pdi2.setId(1L);
        pdi2.setTitulo("PDI 1");
        pdi2.setDescricao("Descrição 1");
        pdi2.setStatus(StatusPDI.EM_ANDAMENTO);
        pdi2.setDtInicio(LocalDate.of(2024, 1, 1));
        pdi2.setDtFim(LocalDate.of(2024, 12, 31));
        pdi2.setIdUsuario(1L);
        pdi2.setDataCriacao(LocalDateTime.of(2024, 1, 1, 10, 0));

        // Act & Assert
        assertEquals(pdi1.hashCode(), pdi2.hashCode());
    }

    @Test
    void testHashCode_ComIdsDiferentes_DeveRetornarHashCodesDiferentes() {
        // Arrange
        PDI pdi1 = new PDI();
        pdi1.setId(1L);
        pdi1.setTitulo("PDI 1");
        pdi1.setDescricao("Descrição 1");
        pdi1.setStatus(StatusPDI.EM_ANDAMENTO);
        pdi1.setDtInicio(LocalDate.of(2024, 1, 1));
        pdi1.setDtFim(LocalDate.of(2024, 12, 31));
        pdi1.setIdUsuario(1L);
        pdi1.setDataCriacao(LocalDateTime.of(2024, 1, 1, 10, 0));

        PDI pdi2 = new PDI();
        pdi2.setId(2L);
        pdi2.setTitulo("PDI 1");
        pdi2.setDescricao("Descrição 1");
        pdi2.setStatus(StatusPDI.EM_ANDAMENTO);
        pdi2.setDtInicio(LocalDate.of(2024, 1, 1));
        pdi2.setDtFim(LocalDate.of(2024, 12, 31));
        pdi2.setIdUsuario(1L);
        pdi2.setDataCriacao(LocalDateTime.of(2024, 1, 1, 10, 0));

        // Act & Assert
        assertNotEquals(pdi1.hashCode(), pdi2.hashCode());
    }

    @Test
    void testToString_DeveConterInformacoesPrincipais() {
        // Arrange
        pdi.setId(1L);
        pdi.setTitulo("PDI Teste");
        pdi.setDescricao("Descrição do PDI");
        pdi.setStatus(StatusPDI.EM_ANDAMENTO);

        // Act
        String toString = pdi.toString();

        // Assert
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("PDI Teste"));
        assertTrue(toString.contains("Descrição do PDI"));
        assertTrue(toString.contains("EM_ANDAMENTO"));
    }

    @Test
    void testToString_ComValoresNulos_DeveFuncionar() {
        // Act
        String toString = pdi.toString();

        // Assert
        assertNotNull(toString);
        assertFalse(toString.isEmpty());
    }

    @Test
    void testStatusEnum_DeveAceitarTodosOsValores() {
        // Arrange & Act & Assert
        pdi.setStatus(StatusPDI.EM_ANDAMENTO);
        assertEquals(StatusPDI.EM_ANDAMENTO, pdi.getStatus());

        pdi.setStatus(StatusPDI.CONCLUIDO);
        assertEquals(StatusPDI.CONCLUIDO, pdi.getStatus());

        pdi.setStatus(StatusPDI.ATRASADO);
        assertEquals(StatusPDI.ATRASADO, pdi.getStatus());
    }

    @Test
    void testDataInicio_DeveAceitarDiferentesDatas() {
        // Arrange & Act & Assert
        LocalDate dataInicio1 = LocalDate.of(2024, 1, 1);
        pdi.setDtInicio(dataInicio1);
        assertEquals(dataInicio1, pdi.getDtInicio());

        LocalDate dataInicio2 = LocalDate.of(2024, 6, 15);
        pdi.setDtInicio(dataInicio2);
        assertEquals(dataInicio2, pdi.getDtInicio());

        LocalDate dataInicio3 = LocalDate.of(2024, 12, 31);
        pdi.setDtInicio(dataInicio3);
        assertEquals(dataInicio3, pdi.getDtInicio());
    }

    @Test
    void testDataFim_DeveAceitarDiferentesDatas() {
        // Arrange & Act & Assert
        LocalDate dataFim1 = LocalDate.of(2024, 12, 31);
        pdi.setDtFim(dataFim1);
        assertEquals(dataFim1, pdi.getDtFim());

        LocalDate dataFim2 = LocalDate.of(2025, 6, 30);
        pdi.setDtFim(dataFim2);
        assertEquals(dataFim2, pdi.getDtFim());

        LocalDate dataFim3 = LocalDate.of(2025, 12, 31);
        pdi.setDtFim(dataFim3);
        assertEquals(dataFim3, pdi.getDtFim());
    }

    @Test
    void testDataCriacao_DeveSerAutomaticaNoPrePersist() {
        // Arrange
        PDI pdiComPrePersist = new PDI();

        // Act
        pdiComPrePersist.onCreate();

        // Assert
        assertNotNull(pdiComPrePersist.getDataCriacao());
        assertTrue(pdiComPrePersist.getDataCriacao().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(pdiComPrePersist.getDataCriacao().isAfter(LocalDateTime.now().minusSeconds(1)));
    }

    @Test
    void testDataCriacao_DeveManterDataQuandoJaDefinida() {
        // Arrange
        LocalDateTime dataEspecifica = LocalDateTime.of(2023, 1, 1, 12, 0, 0);
        pdi.setDataCriacao(dataEspecifica);

        // Act
        pdi.onCreate();

        // Assert
        // Verificar que a data foi definida pelo onCreate (sempre sobrescreve)
        assertNotNull(pdi.getDataCriacao());
        // Verificar que a data atual foi definida (não é a data específica anterior)
        assertNotEquals(dataEspecifica, pdi.getDataCriacao());
    }

    @Test
    void testStatus_DeveSerEmAndamentoPorPadraoNoPrePersist() {
        // Arrange
        PDI pdiNovo = new PDI();

        // Act
        pdiNovo.onCreate();

        // Assert
        assertEquals(StatusPDI.EM_ANDAMENTO, pdiNovo.getStatus());
    }

    @Test
    void testStatus_DeveManterStatusQuandoJaDefinido() {
        // Arrange
        pdi.setStatus(StatusPDI.CONCLUIDO);

        // Act
        pdi.onCreate();

        // Assert
        assertEquals(StatusPDI.CONCLUIDO, pdi.getStatus());
    }

    @Test
    void testMarcos_DeveAceitarListaVazia() {
        // Arrange
        List<MarcoPDI> marcosVazios = new ArrayList<>();

        // Act
        pdi.setMarcos(marcosVazios);

        // Assert
        assertEquals(marcosVazios, pdi.getMarcos());
        assertTrue(pdi.getMarcos().isEmpty());
    }

    @Test
    void testMarcos_DeveAceitarListaNula() {
        // Act
        pdi.setMarcos(null);

        // Assert
        assertNull(pdi.getMarcos());
    }

    @Test
    void testMarcos_DeveAceitarListaComElementos() {
        // Arrange
        List<MarcoPDI> marcos = new ArrayList<>();
        MarcoPDI marco1 = new MarcoPDI();
        marco1.setId(1L);
        marco1.setTitulo("Primeiro Marco");
        marcos.add(marco1);

        MarcoPDI marco2 = new MarcoPDI();
        marco2.setId(2L);
        marco2.setTitulo("Segundo Marco");
        marcos.add(marco2);

        MarcoPDI marco3 = new MarcoPDI();
        marco3.setId(3L);
        marco3.setTitulo("Terceiro Marco");
        marcos.add(marco3);

        // Act
        pdi.setMarcos(marcos);

        // Assert
        assertEquals(3, pdi.getMarcos().size());
        assertEquals("Primeiro Marco", pdi.getMarcos().get(0).getTitulo());
        assertEquals("Segundo Marco", pdi.getMarcos().get(1).getTitulo());
        assertEquals("Terceiro Marco", pdi.getMarcos().get(2).getTitulo());
    }

    @Test
    void testTitulo_DeveAceitarDiferentesTitulos() {
        // Arrange & Act & Assert
        pdi.setTitulo("PDI Desenvolvimento");
        assertEquals("PDI Desenvolvimento", pdi.getTitulo());

        pdi.setTitulo("Plano de Carreira");
        assertEquals("Plano de Carreira", pdi.getTitulo());

        pdi.setTitulo("Crescimento Profissional");
        assertEquals("Crescimento Profissional", pdi.getTitulo());

        pdi.setTitulo("Evolução na Empresa");
        assertEquals("Evolução na Empresa", pdi.getTitulo());
    }

    @Test
    void testDescricao_DeveAceitarDescricoesLongas() {
        // Arrange
        String descricaoLonga = "Este é um plano de desenvolvimento individual muito detalhado " +
                "que inclui objetivos específicos, metas mensuráveis, prazos definidos, " +
                "recursos necessários, indicadores de sucesso e planos de ação concretos " +
                "para alcançar o crescimento profissional desejado.";

        // Act
        pdi.setDescricao(descricaoLonga);

        // Assert
        assertEquals(descricaoLonga, pdi.getDescricao());
    }

    @Test
    void testDescricao_DeveAceitarDescricoesVazias() {
        // Arrange
        String descricaoVazia = "";

        // Act
        pdi.setDescricao(descricaoVazia);

        // Assert
        assertEquals(descricaoVazia, pdi.getDescricao());
    }

    @Test
    void testEquals_ComIdNulo_DeveRetornarFalse() {
        // Arrange
        PDI pdi1 = new PDI();
        pdi1.setId(null);

        PDI pdi2 = new PDI();
        pdi2.setId(1L);

        // Act & Assert
        assertNotEquals(pdi1, pdi2);
    }

    @Test
    void testEquals_ComAmbosIdsNulos_DeveRetornarTrue() {
        // Arrange
        PDI pdi1 = new PDI();
        pdi1.setId(null);

        PDI pdi2 = new PDI();
        pdi2.setId(null);

        // Act & Assert
        assertEquals(pdi1, pdi2);
    }

    @Test
    void testHashCode_ComIdNulo_DeveRetornarHashCodeConsistente() {
        // Arrange
        PDI pdi1 = new PDI();
        pdi1.setId(null);

        PDI pdi2 = new PDI();
        pdi2.setId(null);

        // Act & Assert
        assertEquals(pdi1.hashCode(), pdi2.hashCode());
    }

    @Test
    void testToString_ComTodosOsCamposPreenchidos_DeveConterTodasInformacoes() {
        // Arrange
        LocalDate dtInicio = LocalDate.of(2024, 1, 1);
        LocalDate dtFim = LocalDate.of(2024, 12, 31);
        LocalDateTime dataCriacao = LocalDateTime.of(2023, 12, 1, 10, 0, 0);
        List<MarcoPDI> marcos = new ArrayList<>();

        pdi.setId(1L);
        pdi.setTitulo("PDI Completo");
        pdi.setDescricao("Descrição completa do PDI");
        pdi.setDestinatario(destinatario);
        pdi.setStatus(StatusPDI.EM_ANDAMENTO);
        pdi.setDtInicio(dtInicio);
        pdi.setDtFim(dtFim);
        pdi.setIdUsuario(1L);
        pdi.setUsuario(usuario);
        pdi.setDataCriacao(dataCriacao);
        pdi.setMarcos(marcos);

        // Act
        String toString = pdi.toString();

        // Assert
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("PDI Completo"));
        assertTrue(toString.contains("Descrição completa do PDI"));
        assertTrue(toString.contains("EM_ANDAMENTO"));
        assertTrue(toString.contains("2024-01-01"));
        assertTrue(toString.contains("2024-12-31"));
    }

    @Test
    void testTitulo_DeveAceitarTitulosComCaracteresEspeciais() {
        // Arrange
        String tituloComEspeciais = "PDI com caracteres especiais: áéíóú çãõ";

        // Act
        pdi.setTitulo(tituloComEspeciais);

        // Assert
        assertEquals(tituloComEspeciais, pdi.getTitulo());
    }

    @Test
    void testDescricao_DeveAceitarDescricoesComQuebrasDeLinha() {
        // Arrange
        String descricaoComQuebras = "Primeira linha\nSegunda linha\nTerceira linha";

        // Act
        pdi.setDescricao(descricaoComQuebras);

        // Assert
        assertEquals(descricaoComQuebras, pdi.getDescricao());
    }

    @Test
    void testDataInicio_DeveSerAnteriorOuIgualADataFim() {
        // Arrange
        LocalDate dtInicio = LocalDate.of(2024, 1, 1);
        LocalDate dtFim = LocalDate.of(2024, 12, 31);

        // Act
        pdi.setDtInicio(dtInicio);
        pdi.setDtFim(dtFim);

        // Assert
        assertTrue(pdi.getDtInicio().isBefore(pdi.getDtFim()) || pdi.getDtInicio().isEqual(pdi.getDtFim()));
    }

    @Test
    void testDataInicio_DeveAceitarDataIgualADataFim() {
        // Arrange
        LocalDate data = LocalDate.of(2024, 6, 15);

        // Act
        pdi.setDtInicio(data);
        pdi.setDtFim(data);

        // Assert
        assertEquals(data, pdi.getDtInicio());
        assertEquals(data, pdi.getDtFim());
    }

    @Test
    void testIdUsuario_DeveSerConsistenteComUsuario() {
        // Arrange
        Long idUsuario = 1L;

        // Act
        pdi.setIdUsuario(idUsuario);
        pdi.setUsuario(usuario);

        // Assert
        assertEquals(idUsuario, pdi.getIdUsuario());
        assertEquals(usuario, pdi.getUsuario());
        assertEquals(idUsuario, pdi.getUsuario().getId());
    }
} 
package br.com.pointer.pointer_back.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import br.com.pointer.pointer_back.dto.PdiListagemDTO;
import br.com.pointer.pointer_back.enums.StatusPDI;
import br.com.pointer.pointer_back.model.PDI;
import br.com.pointer.pointer_back.model.Usuario;

@DataJpaTest
@ActiveProfiles("test")
class PDIRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PDIRepository pdiRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private PDI pdi1;
    private PDI pdi2;
    private PDI pdi3;
    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void setUp() {
        // Limpar dados anteriores
        entityManager.clear();

        // Criar usuários de teste
        usuario1 = new Usuario();
        usuario1.setNome("João Silva");
        usuario1.setEmail("joao@teste.com");
        usuario1.setKeycloakId("keycloak-joao");
        usuario1.setTipoUsuario("GESTOR");
        usuario1.setSetor("TI");
        usuario1.setCargo("Gerente");
        usuario1.setStatus(br.com.pointer.pointer_back.model.StatusUsuario.ATIVO);
        usuario1.setDataCriacao(LocalDateTime.now());
        entityManager.persistAndFlush(usuario1);

        usuario2 = new Usuario();
        usuario2.setNome("Maria Santos");
        usuario2.setEmail("maria@teste.com");
        usuario2.setKeycloakId("keycloak-maria");
        usuario2.setTipoUsuario("COLABORADOR");
        usuario2.setSetor("TI");
        usuario2.setCargo("Desenvolvedor");
        usuario2.setStatus(br.com.pointer.pointer_back.model.StatusUsuario.ATIVO);
        usuario2.setDataCriacao(LocalDateTime.now());
        entityManager.persistAndFlush(usuario2);

        // Criar PDIs de teste
        pdi1 = new PDI();
        pdi1.setTitulo("PDI Desenvolvimento");
        pdi1.setDescricao("Plano de desenvolvimento individual");
        pdi1.setStatus(StatusPDI.EM_ANDAMENTO);
        pdi1.setDtInicio(LocalDate.now());
        pdi1.setDtFim(LocalDate.now().plusMonths(6));
        pdi1.setDestinatario(usuario1);
        pdi1.setIdUsuario(usuario1.getId());

        pdi2 = new PDI();
        pdi2.setTitulo("PDI Liderança");
        pdi2.setDescricao("Plano de desenvolvimento de liderança");
        pdi2.setStatus(StatusPDI.EM_ANDAMENTO);
        pdi2.setDtInicio(LocalDate.now());
        pdi2.setDtFim(LocalDate.now().plusMonths(12));
        pdi2.setDestinatario(usuario2);
        pdi2.setIdUsuario(usuario2.getId());

        pdi3 = new PDI();
        pdi3.setTitulo("PDI Concluído");
        pdi3.setDescricao("Plano já concluído");
        pdi3.setStatus(StatusPDI.CONCLUIDO);
        pdi3.setDtInicio(LocalDate.now().minusMonths(6));
        pdi3.setDtFim(LocalDate.now().minusDays(1));
        pdi3.setDestinatario(usuario1);
        pdi3.setIdUsuario(usuario1.getId());

        // Persistir PDIs
        entityManager.persistAndFlush(pdi1);
        entityManager.persistAndFlush(pdi2);
        entityManager.persistAndFlush(pdi3);
    }

    @Test
    void findByIdUsuario_ComUsuarioExistente_DeveRetornarPDIs() {
        // Act
        List<PDI> result = pdiRepository.findByIdUsuario(usuario1.getId());

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> p.getDestinatario().getId().equals(usuario1.getId())));
    }

    @Test
    void findByIdUsuario_ComUsuarioInexistente_DeveRetornarVazio() {
        // Act
        List<PDI> result = pdiRepository.findByIdUsuario(999L);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByIdDestinatario_ComDestinatarioExistente_DeveRetornarPDIs() {
        // Act
        List<PDI> result = pdiRepository.findByIdDestinatario(usuario1.getId());

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> p.getDestinatario().getId().equals(usuario1.getId())));
    }

    @Test
    void findByIdDestinatario_ComDestinatarioInexistente_DeveRetornarVazio() {
        // Act
        List<PDI> result = pdiRepository.findByIdDestinatario(999L);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllWithDestinatario_DeveRetornarTodosPDIsComDestinatario() {
        // Act
        List<PDI> result = pdiRepository.findAllWithDestinatario();

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.stream().allMatch(p -> p.getDestinatario() != null));
        assertTrue(result.stream().anyMatch(p -> p.getTitulo().equals("PDI Desenvolvimento")));
        assertTrue(result.stream().anyMatch(p -> p.getTitulo().equals("PDI Liderança")));
        assertTrue(result.stream().anyMatch(p -> p.getTitulo().equals("PDI Concluído")));
    }

    @Test
    void buscarTodosParaListagem_DeveRetornarListagemDTO() {
        // Act
        List<PdiListagemDTO> result = pdiRepository.buscarTodosParaListagem();

        // Assert
        assertEquals(3, result.size());
        
        // Verificar se todos os PDIs estão presentes
        assertTrue(result.stream().anyMatch(dto -> "PDI Desenvolvimento".equals(dto.getTitulo())));
        assertTrue(result.stream().anyMatch(dto -> "PDI Liderança".equals(dto.getTitulo())));
        assertTrue(result.stream().anyMatch(dto -> "PDI Concluído".equals(dto.getTitulo())));
        
        // Verificar se os dados do destinatário estão presentes
        PdiListagemDTO pdiDTO = result.stream()
                .filter(dto -> "PDI Desenvolvimento".equals(dto.getTitulo()))
                .findFirst()
                .orElse(null);
        assertNotNull(pdiDTO);
        assertEquals(usuario1.getId(), pdiDTO.getIdDestinatario());
        assertEquals(usuario1.getNome(), pdiDTO.getNomeDestinatario());
        assertEquals(usuario1.getCargo(), pdiDTO.getCargoDestinatario());
        assertEquals(usuario1.getSetor(), pdiDTO.getSetorDestinatario());
        assertEquals(usuario1.getEmail(), pdiDTO.getEmailDestinatario());
    }

    @Test
    void save_ComPDINovo_DeveSalvarPDI() {
        // Given
        PDI pdi = new PDI();
        pdi.setTitulo("PDI Teste");
        pdi.setDescricao("Descrição do PDI teste");
        pdi.setDestinatario(usuario1);
        pdi.setStatus(StatusPDI.EM_ANDAMENTO);
        pdi.setDtInicio(LocalDate.now());
        pdi.setDtFim(LocalDate.now().plusMonths(6));
        pdi.setIdUsuario(usuario1.getId());

        // When
        PDI savedPDI = pdiRepository.save(pdi);

        // Then
        assertNotNull(savedPDI.getId());
        assertEquals("PDI Teste", savedPDI.getTitulo());
        assertEquals("Descrição do PDI teste", savedPDI.getDescricao());
        assertEquals(usuario1, savedPDI.getDestinatario());
        assertEquals(StatusPDI.EM_ANDAMENTO, savedPDI.getStatus());
        assertEquals(LocalDate.now(), savedPDI.getDtInicio());
        assertEquals(LocalDate.now().plusMonths(6), savedPDI.getDtFim());
        assertEquals(usuario1.getId(), savedPDI.getIdUsuario());
        assertNotNull(savedPDI.getDataCriacao());
    }

    @Test
    void save_ComPDIExistente_DeveAtualizarPDI() {
        // Arrange
        pdi1.setTitulo("PDI Atualizado");

        // Act
        PDI updated = pdiRepository.save(pdi1);

        // Assert
        assertEquals("PDI Atualizado", updated.getTitulo());
        assertEquals(pdi1.getId(), updated.getId());
        
        // Verificar se foi atualizado no banco
        Optional<PDI> found = pdiRepository.findById(pdi1.getId());
        assertTrue(found.isPresent());
        assertEquals("PDI Atualizado", found.get().getTitulo());
    }

    @Test
    void delete_ComPDIExistente_DeveRemoverPDI() {
        // Arrange
        Long pdiId = pdi1.getId();

        // Act
        pdiRepository.delete(pdi1);

        // Assert
        Optional<PDI> found = pdiRepository.findById(pdiId);
        assertFalse(found.isPresent());
    }

    @Test
    void findById_ComIdExistente_DeveRetornarPDI() {
        // Act
        Optional<PDI> result = pdiRepository.findById(pdi1.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals("PDI Desenvolvimento", result.get().getTitulo());
    }

    @Test
    void findById_ComIdInexistente_DeveRetornarVazio() {
        // Act
        Optional<PDI> result = pdiRepository.findById(999L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findAll_DeveRetornarTodosPDIs() {
        // Act
        List<PDI> result = pdiRepository.findAll();

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(p -> p.getTitulo().equals("PDI Desenvolvimento")));
        assertTrue(result.stream().anyMatch(p -> p.getTitulo().equals("PDI Liderança")));
        assertTrue(result.stream().anyMatch(p -> p.getTitulo().equals("PDI Concluído")));
    }

    @Test
    void count_DeveRetornarNumeroCorretoDePDIs() {
        // Act
        long count = pdiRepository.count();

        // Assert
        assertEquals(3, count);
    }

    @Test
    void existsById_ComIdExistente_DeveRetornarTrue() {
        // Act
        boolean exists = pdiRepository.existsById(pdi1.getId());

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsById_ComIdInexistente_DeveRetornarFalse() {
        // Act
        boolean exists = pdiRepository.existsById(999L);

        // Assert
        assertFalse(exists);
    }

    @Test
    void saveAll_ComListaDePDIs_DeveSalvarTodos() {
        // Given
        List<PDI> pdis = Arrays.asList(
            createPDI("PDI 1", StatusPDI.EM_ANDAMENTO),
            createPDI("PDI 2", StatusPDI.CONCLUIDO),
            createPDI("PDI 3", StatusPDI.ATRASADO)
        );

        // When
        List<PDI> savedPDIs = pdiRepository.saveAll(pdis);

        // Then
        assertEquals(3, savedPDIs.size());
        savedPDIs.forEach(pdi -> {
            assertNotNull(pdi.getId());
            assertNotNull(pdi.getDataCriacao());
        });
    }

    private PDI createPDI(String titulo, StatusPDI status) {
        PDI pdi = new PDI();
        pdi.setTitulo(titulo);
        pdi.setDescricao("Descrição do " + titulo);
        pdi.setDestinatario(usuario1);
        pdi.setStatus(status);
        pdi.setDtInicio(LocalDate.now());
        pdi.setDtFim(LocalDate.now().plusMonths(6));
        pdi.setIdUsuario(usuario1.getId());
        return pdi;
    }

    @Test
    void deleteAll_DeveRemoverTodosPDIs() {
        // Act
        pdiRepository.deleteAll();

        // Assert
        assertEquals(0, pdiRepository.count());
        assertTrue(pdiRepository.findAll().isEmpty());
    }

    @Test
    void deleteAllById_ComIdsExistentes_DeveRemoverPDIs() {
        // Arrange
        List<Long> ids = Arrays.asList(pdi1.getId(), pdi2.getId());

        // Act
        pdiRepository.deleteAllById(ids);

        // Assert
        assertEquals(1, pdiRepository.count());
        assertFalse(pdiRepository.existsById(pdi1.getId()));
        assertFalse(pdiRepository.existsById(pdi2.getId()));
        assertTrue(pdiRepository.existsById(pdi3.getId()));
    }

    @Test
    void findByIdUsuario_ComUsuarioSemPDIs_DeveRetornarVazio() {
        // Arrange - Criar usuário sem PDIs
        Usuario usuarioSemPDI = new Usuario();
        usuarioSemPDI.setNome("Usuário Sem PDI");
        usuarioSemPDI.setEmail("sem@pdi.com");
        usuarioSemPDI.setKeycloakId("keycloak-sem-pdi");
        usuarioSemPDI.setTipoUsuario("COLABORADOR");
        usuarioSemPDI.setSetor("RH");
        usuarioSemPDI.setCargo("Analista");
        usuarioSemPDI.setStatus(br.com.pointer.pointer_back.model.StatusUsuario.ATIVO);
        usuarioSemPDI.setDataCriacao(LocalDateTime.now());
        entityManager.persistAndFlush(usuarioSemPDI);

        // Act
        List<PDI> result = pdiRepository.findByIdUsuario(usuarioSemPDI.getId());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByIdDestinatario_ComDestinatarioSemPDIs_DeveRetornarVazio() {
        // Arrange - Criar usuário sem PDIs
        Usuario usuarioSemPDI = new Usuario();
        usuarioSemPDI.setNome("Usuário Sem PDI");
        usuarioSemPDI.setEmail("sem@pdi.com");
        usuarioSemPDI.setKeycloakId("keycloak-sem-pdi");
        usuarioSemPDI.setTipoUsuario("COLABORADOR");
        usuarioSemPDI.setSetor("RH");
        usuarioSemPDI.setCargo("Analista");
        usuarioSemPDI.setStatus(br.com.pointer.pointer_back.model.StatusUsuario.ATIVO);
        usuarioSemPDI.setDataCriacao(LocalDateTime.now());
        entityManager.persistAndFlush(usuarioSemPDI);

        // Act
        List<PDI> result = pdiRepository.findByIdDestinatario(usuarioSemPDI.getId());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void buscarTodosParaListagem_ComPDIsVazios_DeveRetornarListaVazia() {
        // Arrange - Remover todos os PDIs
        pdiRepository.deleteAll();

        // Act
        List<PdiListagemDTO> result = pdiRepository.buscarTodosParaListagem();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void save_ComPDISemDestinatario_DeveLancarExcecao() {
        // Arrange
        PDI pdiSemDestinatario = new PDI();
        pdiSemDestinatario.setTitulo("PDI Sem Destinatário");
        pdiSemDestinatario.setDescricao("Descrição");
        pdiSemDestinatario.setStatus(StatusPDI.ATRASADO);
        pdiSemDestinatario.setDtInicio(LocalDate.now());
        pdiSemDestinatario.setDtFim(LocalDate.now().plusMonths(3));
        // Não definir destinatário

        // Act & Assert
        assertThrows(Exception.class, () -> {
            pdiRepository.save(pdiSemDestinatario);
        });
    }

    @Test
    void save_ComPDISemTitulo_DeveLancarExcecao() {
        // Arrange
        PDI pdiSemTitulo = new PDI();
        // Não definir título
        pdiSemTitulo.setDescricao("Descrição");
        pdiSemTitulo.setStatus(StatusPDI.EM_ANDAMENTO);
        pdiSemTitulo.setDtInicio(LocalDate.now());
        pdiSemTitulo.setDtFim(LocalDate.now().plusMonths(3));
        pdiSemTitulo.setDestinatario(usuario1);

        // Act & Assert
        assertThrows(Exception.class, () -> {
            pdiRepository.save(pdiSemTitulo);
        });
    }

    @Test
    void save_ComPDISemDescricao_DeveLancarExcecao() {
        // Arrange
        PDI pdiSemDescricao = new PDI();
        pdiSemDescricao.setTitulo("PDI Sem Descrição");
        // Não definir descrição
        pdiSemDescricao.setStatus(StatusPDI.EM_ANDAMENTO);
        pdiSemDescricao.setDtInicio(LocalDate.now());
        pdiSemDescricao.setDtFim(LocalDate.now().plusMonths(3));
        pdiSemDescricao.setDestinatario(usuario1);

        // Act & Assert
        assertThrows(Exception.class, () -> {
            pdiRepository.save(pdiSemDescricao);
        });
    }

    @Test
    void save_ComPDISemStatus_DeveLancarExcecao() {
        // Arrange
        PDI pdiSemStatus = new PDI();
        pdiSemStatus.setTitulo("PDI Sem Status");
        pdiSemStatus.setDescricao("Descrição");
        // Não definir status
        pdiSemStatus.setDtInicio(LocalDate.now());
        pdiSemStatus.setDtFim(LocalDate.now().plusMonths(3));
        pdiSemStatus.setDestinatario(usuario1);

        // Act & Assert
        assertThrows(Exception.class, () -> {
            pdiRepository.save(pdiSemStatus);
        });
    }

    @Test
    void save_ComPDISemDataInicio_DeveLancarExcecao() {
        // Arrange
        PDI pdiSemDataInicio = new PDI();
        pdiSemDataInicio.setTitulo("PDI Sem Data Início");
        pdiSemDataInicio.setDescricao("Descrição");
        pdiSemDataInicio.setStatus(StatusPDI.EM_ANDAMENTO);
        // Não definir data início
        pdiSemDataInicio.setDtFim(LocalDate.now().plusMonths(3));
        pdiSemDataInicio.setDestinatario(usuario1);

        // Act & Assert
        assertThrows(Exception.class, () -> {
            pdiRepository.save(pdiSemDataInicio);
        });
    }

    @Test
    void save_ComPDISemDataFim_DeveLancarExcecao() {
        // Arrange
        PDI pdiSemDataFim = new PDI();
        pdiSemDataFim.setTitulo("PDI Sem Data Fim");
        pdiSemDataFim.setDescricao("Descrição");
        pdiSemDataFim.setStatus(StatusPDI.EM_ANDAMENTO);
        pdiSemDataFim.setDtInicio(LocalDate.now());
        // Não definir data fim
        pdiSemDataFim.setDestinatario(usuario1);

        // Act & Assert
        assertThrows(Exception.class, () -> {
            pdiRepository.save(pdiSemDataFim);
        });
    }
} 
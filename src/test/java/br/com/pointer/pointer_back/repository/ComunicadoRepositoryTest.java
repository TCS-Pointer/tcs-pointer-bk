package br.com.pointer.pointer_back.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import br.com.pointer.pointer_back.model.Comunicado;
import br.com.pointer.pointer_back.model.Usuario;

@DataJpaTest
@ActiveProfiles("test")
class ComunicadoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ComunicadoRepository comunicadoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Comunicado comunicado1;
    private Comunicado comunicado2;
    private Comunicado comunicado3;

    @BeforeEach
    void setUp() {
        // Limpar dados anteriores
        entityManager.clear();

        // Criar comunicados de teste
        comunicado1 = new Comunicado();
        comunicado1.setTitulo("Comunicado Geral");
        comunicado1.setDescricao("Este é um comunicado geral para todos");
        Set<String> setores1 = new HashSet<>(Arrays.asList("TI", "RH"));
        comunicado1.setSetores(setores1);
        comunicado1.setApenasGestores(false);
        comunicado1.setAtivo(true);
        comunicado1.setDataPublicacao(LocalDateTime.now());

        comunicado2 = new Comunicado();
        comunicado2.setTitulo("Comunicado para Gestores");
        comunicado2.setDescricao("Este é um comunicado apenas para gestores");
        Set<String> setores2 = new HashSet<>(Arrays.asList("TI"));
        comunicado2.setSetores(setores2);
        comunicado2.setApenasGestores(true);
        comunicado2.setAtivo(true);
        comunicado2.setDataPublicacao(LocalDateTime.now());

        comunicado3 = new Comunicado();
        comunicado3.setTitulo("Comunicado Inativo");
        comunicado3.setDescricao("Este comunicado está inativo");
        Set<String> setores3 = new HashSet<>(Arrays.asList("RH"));
        comunicado3.setSetores(setores3);
        comunicado3.setApenasGestores(false);
        comunicado3.setAtivo(false);
        comunicado3.setDataPublicacao(LocalDateTime.now());

        // Persistir comunicados
        entityManager.persistAndFlush(comunicado1);
        entityManager.persistAndFlush(comunicado2);
        entityManager.persistAndFlush(comunicado3);
    }

    @Test
    void findByFilters_ComFiltrosVazios_DeveRetornarComunicadosAtivos() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Comunicado> result = comunicadoRepository.findByFilters(null, null, null, pageRequest);

        // Assert
        assertEquals(2, result.getTotalElements()); // Apenas comunicados ativos
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().stream().allMatch(Comunicado::isAtivo));
    }

    @Test
    void findByFilters_ComFiltroSetor_DeveRetornarComunicadosDoSetor() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Comunicado> result = comunicadoRepository.findByFilters("TI", null, null, pageRequest);

        // Assert
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream().allMatch(c -> c.getSetores().contains("TI")));
    }

    @Test
    void findByFilters_ComFiltroSetorInexistente_DeveRetornarVazio() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Comunicado> result = comunicadoRepository.findByFilters("Marketing", null, null, pageRequest);

        // Assert
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void findByFilters_ComFiltroTitulo_DeveRetornarComunicadosComTitulo() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Comunicado> result = comunicadoRepository.findByFilters(null, "Geral", null, pageRequest);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("Comunicado Geral", result.getContent().get(0).getTitulo());
    }

    @Test
    void findByFilters_ComFiltroTituloCaseInsensitive_DeveRetornarComunicados() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Comunicado> result = comunicadoRepository.findByFilters(null, "geral", null, pageRequest);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("Comunicado Geral", result.getContent().get(0).getTitulo());
    }

    @Test
    void findByFilters_ComFiltroApenasGestoresTrue_DeveRetornarComunicadosParaGestores() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Comunicado> result = comunicadoRepository.findByFilters(null, null, true, pageRequest);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().get(0).isApenasGestores());
    }

    @Test
    void findByFilters_ComFiltroApenasGestoresFalse_DeveRetornarComunicadosGerais() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Comunicado> result = comunicadoRepository.findByFilters(null, null, false, pageRequest);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertFalse(result.getContent().get(0).isApenasGestores());
    }

    @Test
    void findByFilters_ComMultiplosFiltros_DeveRetornarComunicadosFiltrados() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Comunicado> result = comunicadoRepository.findByFilters("TI", "Gestores", true, pageRequest);

        // Assert
        assertEquals(1, result.getTotalElements());
        Comunicado comunicado = result.getContent().get(0);
        assertTrue(comunicado.getSetores().contains("TI"));
        assertTrue(comunicado.getTitulo().toLowerCase().contains("gestores"));
        assertTrue(comunicado.isApenasGestores());
    }

    @Test
    void findByFilters_ComPaginacao_DeveRetornarPaginaCorreta() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 1);

        // Act
        Page<Comunicado> result = comunicadoRepository.findByFilters(null, null, null, pageRequest);

        // Assert
        assertEquals(2, result.getTotalElements()); // Total de comunicados ativos
        assertEquals(1, result.getContent().size()); // Apenas 1 por página
        assertEquals(2, result.getTotalPages());
    }

    @Test
    void save_ComComunicadoNovo_DeveSalvarComunicado() {
        // Arrange
        Comunicado novoComunicado = new Comunicado();
        novoComunicado.setTitulo("Novo Comunicado");
        novoComunicado.setDescricao("Descrição do novo comunicado");
        Set<String> setores = new HashSet<>(Arrays.asList("Marketing"));
        novoComunicado.setSetores(setores);
        novoComunicado.setApenasGestores(false);
        novoComunicado.setAtivo(true);
        novoComunicado.setDataPublicacao(LocalDateTime.now());

        // Act
        Comunicado saved = comunicadoRepository.save(novoComunicado);

        // Assert
        assertNotNull(saved.getId());
        assertEquals("Novo Comunicado", saved.getTitulo());
        assertEquals("Descrição do novo comunicado", saved.getDescricao());
        
        // Verificar se foi persistido no banco
        Optional<Comunicado> found = comunicadoRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Novo Comunicado", found.get().getTitulo());
    }

    @Test
    void save_ComComunicadoExistente_DeveAtualizarComunicado() {
        // Arrange
        comunicado1.setTitulo("Comunicado Atualizado");

        // Act
        Comunicado updated = comunicadoRepository.save(comunicado1);

        // Assert
        assertEquals("Comunicado Atualizado", updated.getTitulo());
        assertEquals(comunicado1.getId(), updated.getId());
        
        // Verificar se foi atualizado no banco
        Optional<Comunicado> found = comunicadoRepository.findById(comunicado1.getId());
        assertTrue(found.isPresent());
        assertEquals("Comunicado Atualizado", found.get().getTitulo());
    }

    @Test
    void delete_ComComunicadoExistente_DeveRemoverComunicado() {
        // Arrange
        Long comunicadoId = comunicado1.getId();

        // Act
        comunicadoRepository.delete(comunicado1);

        // Assert
        Optional<Comunicado> found = comunicadoRepository.findById(comunicadoId);
        assertFalse(found.isPresent());
    }

    @Test
    void findById_ComIdExistente_DeveRetornarComunicado() {
        // Act
        Optional<Comunicado> result = comunicadoRepository.findById(comunicado1.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Comunicado Geral", result.get().getTitulo());
    }

    @Test
    void findById_ComIdInexistente_DeveRetornarVazio() {
        // Act
        Optional<Comunicado> result = comunicadoRepository.findById(999L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findAll_DeveRetornarTodosComunicados() {
        // Act
        List<Comunicado> result = comunicadoRepository.findAll();

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(c -> c.getTitulo().equals("Comunicado Geral")));
        assertTrue(result.stream().anyMatch(c -> c.getTitulo().equals("Comunicado para Gestores")));
        assertTrue(result.stream().anyMatch(c -> c.getTitulo().equals("Comunicado Inativo")));
    }

    @Test
    void count_DeveRetornarNumeroCorretoDeComunicados() {
        // Act
        long count = comunicadoRepository.count();

        // Assert
        assertEquals(3, count);
    }

    @Test
    void existsById_ComIdExistente_DeveRetornarTrue() {
        // Act
        boolean exists = comunicadoRepository.existsById(comunicado1.getId());

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsById_ComIdInexistente_DeveRetornarFalse() {
        // Act
        boolean exists = comunicadoRepository.existsById(999L);

        // Assert
        assertFalse(exists);
    }

    @Test
    void saveAll_ComListaDeComunicados_DeveSalvarTodos() {
        // Arrange
        Comunicado novo1 = new Comunicado();
        novo1.setTitulo("Novo 1");
        novo1.setDescricao("Descrição 1");
        Set<String> setores1 = new HashSet<>(Arrays.asList("Marketing"));
        novo1.setSetores(setores1);
        novo1.setApenasGestores(false);
        novo1.setAtivo(true);
        novo1.setDataPublicacao(LocalDateTime.now());

        Comunicado novo2 = new Comunicado();
        novo2.setTitulo("Novo 2");
        novo2.setDescricao("Descrição 2");
        Set<String> setores2 = new HashSet<>(Arrays.asList("Vendas"));
        novo2.setSetores(setores2);
        novo2.setApenasGestores(true);
        novo2.setAtivo(true);
        novo2.setDataPublicacao(LocalDateTime.now());

        List<Comunicado> comunicados = Arrays.asList(novo1, novo2);

        // Act
        List<Comunicado> saved = comunicadoRepository.saveAll(comunicados);

        // Assert
        assertEquals(2, saved.size());
        assertTrue(saved.stream().allMatch(c -> c.getId() != null));
        
        // Verificar se foram persistidos no banco
        assertEquals(5, comunicadoRepository.count());
    }

    @Test
    void deleteAll_DeveRemoverTodosComunicados() {
        // Act
        comunicadoRepository.deleteAll();

        // Assert
        assertEquals(0, comunicadoRepository.count());
        assertTrue(comunicadoRepository.findAll().isEmpty());
    }

    @Test
    void deleteAllById_ComIdsExistentes_DeveRemoverComunicados() {
        // Arrange
        List<Long> ids = Arrays.asList(comunicado1.getId(), comunicado2.getId());

        // Act
        comunicadoRepository.deleteAllById(ids);

        // Assert
        assertEquals(1, comunicadoRepository.count());
        assertFalse(comunicadoRepository.existsById(comunicado1.getId()));
        assertFalse(comunicadoRepository.existsById(comunicado2.getId()));
        assertTrue(comunicadoRepository.existsById(comunicado3.getId()));
    }

    @Test
    void findByFilters_ComSetorVazio_DeveRetornarTodosComunicadosAtivos() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Comunicado> result = comunicadoRepository.findByFilters("", null, null, pageRequest);

        // Assert
        assertEquals(2, result.getTotalElements()); // Apenas comunicados ativos
        assertEquals(2, result.getContent().size());
    }

    @Test
    void findByFilters_ComTituloVazio_DeveRetornarTodosComunicadosAtivos() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Comunicado> result = comunicadoRepository.findByFilters(null, "", null, pageRequest);

        // Assert
        assertEquals(2, result.getTotalElements()); // Apenas comunicados ativos
        assertEquals(2, result.getContent().size());
    }

    @Test
    void findByFilters_ComFiltroTituloParcial_DeveRetornarComunicados() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Comunicado> result = comunicadoRepository.findByFilters(null, "Comunicado", null, pageRequest);

        // Assert
        assertEquals(2, result.getTotalElements()); // Apenas comunicados ativos
        assertTrue(result.getContent().stream().allMatch(c -> c.getTitulo().contains("Comunicado")));
    }

    @Test
    void findByFilters_ComFiltroSetorEspecifico_DeveRetornarComunicadosDoSetor() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Comunicado> result = comunicadoRepository.findByFilters("RH", null, null, pageRequest);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().get(0).getSetores().contains("RH"));
    }

    @Test
    void findByFilters_ComFiltroApenasGestoresNull_DeveRetornarTodosComunicadosAtivos() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Comunicado> result = comunicadoRepository.findByFilters(null, null, null, pageRequest);

        // Assert
        assertEquals(2, result.getTotalElements()); // Apenas comunicados ativos
        assertEquals(2, result.getContent().size());
    }
} 
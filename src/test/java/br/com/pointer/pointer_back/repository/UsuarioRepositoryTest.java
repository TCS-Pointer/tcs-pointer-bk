package br.com.pointer.pointer_back.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import br.com.pointer.pointer_back.dto.TipoUsuarioStatsDTO;
import br.com.pointer.pointer_back.model.StatusUsuario;
import br.com.pointer.pointer_back.model.Usuario;

@DataJpaTest
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario1;
    private Usuario usuario2;
    private Usuario usuario3;

    @BeforeEach
    void setUp() {
        // Limpar dados anteriores
        entityManager.clear();

        // Criar usuários de teste
        usuario1 = new Usuario();
        usuario1.setNome("João Silva");
        usuario1.setEmail("joao@teste.com");
        usuario1.setKeycloakId("keycloak-1");
        usuario1.setTipoUsuario("ADMIN");
        usuario1.setSetor("TI");
        usuario1.setCargo("Desenvolvedor");
        usuario1.setStatus(StatusUsuario.ATIVO);
        usuario1.setDataCriacao(LocalDateTime.now());

        usuario2 = new Usuario();
        usuario2.setNome("Maria Santos");
        usuario2.setEmail("maria@teste.com");
        usuario2.setKeycloakId("keycloak-2");
        usuario2.setTipoUsuario("GESTOR");
        usuario2.setSetor("RH");
        usuario2.setCargo("Gerente");
        usuario2.setStatus(StatusUsuario.ATIVO);
        usuario2.setDataCriacao(LocalDateTime.now());

        usuario3 = new Usuario();
        usuario3.setNome("Pedro Costa");
        usuario3.setEmail("pedro@teste.com");
        usuario3.setKeycloakId("keycloak-3");
        usuario3.setTipoUsuario("COLABORADOR");
        usuario3.setSetor("TI");
        usuario3.setCargo("Analista");
        usuario3.setStatus(StatusUsuario.INATIVO);
        usuario3.setDataCriacao(LocalDateTime.now());

        // Persistir usuários
        entityManager.persistAndFlush(usuario1);
        entityManager.persistAndFlush(usuario2);
        entityManager.persistAndFlush(usuario3);
    }

    @Test
    void findByKeycloakId_ComKeycloakIdExistente_DeveRetornarUsuario() {
        // Act
        Optional<Usuario> result = usuarioRepository.findByKeycloakId("keycloak-1");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("João Silva", result.get().getNome());
        assertEquals("joao@teste.com", result.get().getEmail());
    }

    @Test
    void findByKeycloakId_ComKeycloakIdInexistente_DeveRetornarVazio() {
        // Act
        Optional<Usuario> result = usuarioRepository.findByKeycloakId("keycloak-inexistente");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findByEmail_ComEmailExistente_DeveRetornarUsuario() {
        // Act
        Optional<Usuario> result = usuarioRepository.findByEmail("joao@teste.com");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("João Silva", result.get().getNome());
        assertEquals("keycloak-1", result.get().getKeycloakId());
    }

    @Test
    void findByEmail_ComEmailInexistente_DeveRetornarVazio() {
        // Act
        Optional<Usuario> result = usuarioRepository.findByEmail("inexistente@teste.com");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void existsByEmail_ComEmailExistente_DeveRetornarTrue() {
        // Act
        boolean exists = usuarioRepository.existsByEmail("joao@teste.com");

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsByEmail_ComEmailInexistente_DeveRetornarFalse() {
        // Act
        boolean exists = usuarioRepository.existsByEmail("inexistente@teste.com");

        // Assert
        assertFalse(exists);
    }

    @Test
    void findByIdIn_ComIdsExistentes_DeveRetornarUsuarios() {
        // Arrange
        List<Long> ids = Arrays.asList(usuario1.getId(), usuario2.getId());

        // Act
        List<Usuario> result = usuarioRepository.findByIdIn(ids);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(u -> u.getNome().equals("João Silva")));
        assertTrue(result.stream().anyMatch(u -> u.getNome().equals("Maria Santos")));
    }

    @Test
    void findByIdIn_ComIdsInexistentes_DeveRetornarListaVazia() {
        // Arrange
        List<Long> ids = Arrays.asList(999L, 1000L);

        // Act
        List<Usuario> result = usuarioRepository.findByIdIn(ids);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByStatusAndKeycloakIdNot_ComStatusEKeycloakId_DeveRetornarUsuarios() {
        // Act
        List<Usuario> result = usuarioRepository.findByStatusAndKeycloakIdNot(StatusUsuario.ATIVO, "keycloak-1");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Maria Santos", result.get(0).getNome());
        assertEquals(StatusUsuario.ATIVO, result.get(0).getStatus());
    }

    @Test
    void findBySetor_ComSetorExistente_DeveRetornarUsuarios() {
        // Act
        List<Usuario> result = usuarioRepository.findBySetor("TI", "keycloak-1");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Pedro Costa", result.get(0).getNome());
        assertEquals("TI", result.get(0).getSetor());
    }

    @Test
    void findBySetorAndStatus_ComSetorEStatusExistentes_DeveRetornarUsuarios() {
        // Act
        List<Usuario> result = usuarioRepository.findBySetorAndStatus("TI", StatusUsuario.ATIVO);

        // Assert
        assertEquals(1, result.size());
        assertEquals("João Silva", result.get(0).getNome());
        assertEquals("TI", result.get(0).getSetor());
        assertEquals(StatusUsuario.ATIVO, result.get(0).getStatus());
    }

    @Test
    void findByFilters_ComFiltrosVazios_DeveRetornarTodosUsuarios() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Usuario> result = usuarioRepository.findByFilters(null, null, null, null, pageRequest);

        // Assert
        assertEquals(3, result.getTotalElements());
        assertEquals(3, result.getContent().size());
    }

    @Test
    void findByFilters_ComFiltroTipoUsuario_DeveRetornarUsuariosFiltrados() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Usuario> result = usuarioRepository.findByFilters("ADMIN", null, null, null, pageRequest);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("João Silva", result.getContent().get(0).getNome());
        assertEquals("ADMIN", result.getContent().get(0).getTipoUsuario());
    }

    @Test
    void findByFilters_ComFiltroSetor_DeveRetornarUsuariosFiltrados() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Usuario> result = usuarioRepository.findByFilters(null, "TI", null, null, pageRequest);

        // Assert
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream().allMatch(u -> "TI".equals(u.getSetor())));
    }

    @Test
    void findByFilters_ComFiltroStatus_DeveRetornarUsuariosFiltrados() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Usuario> result = usuarioRepository.findByFilters(null, null, StatusUsuario.ATIVO, null, pageRequest);

        // Assert
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream().allMatch(u -> StatusUsuario.ATIVO.equals(u.getStatus())));
    }

    @Test
    void findByFilters_ComFiltroNome_DeveRetornarUsuariosFiltrados() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Usuario> result = usuarioRepository.findByFilters(null, null, null, "João", pageRequest);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("João Silva", result.getContent().get(0).getNome());
    }

    @Test
    void findByFilters_ComFiltroNomeCaseInsensitive_DeveRetornarUsuariosFiltrados() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Usuario> result = usuarioRepository.findByFilters(null, null, null, "joão", pageRequest);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("João Silva", result.getContent().get(0).getNome());
    }

    @Test
    void findByFilters_ComMultiplosFiltros_DeveRetornarUsuariosFiltrados() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Usuario> result = usuarioRepository.findByFilters("ADMIN", "TI", StatusUsuario.ATIVO, "João", pageRequest);

        // Assert
        assertEquals(1, result.getTotalElements());
        Usuario usuario = result.getContent().get(0);
        assertEquals("João Silva", usuario.getNome());
        assertEquals("ADMIN", usuario.getTipoUsuario());
        assertEquals("TI", usuario.getSetor());
        assertEquals(StatusUsuario.ATIVO, usuario.getStatus());
    }

    @Test
    void findByFilters_ComPaginacao_DeveRetornarPaginaCorreta() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 2);

        // Act
        Page<Usuario> result = usuarioRepository.findByFilters(null, null, null, null, pageRequest);

        // Assert
        assertEquals(3, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalPages());
    }

    @Test
    void findTipoUsuarioStats_DeveRetornarEstatisticas() {
        // Act
        List<TipoUsuarioStatsDTO> result = usuarioRepository.findTipoUsuarioStats();

        // Assert
        assertEquals(3, result.size());
        
        // Verificar se todos os tipos estão presentes
        assertTrue(result.stream().anyMatch(stats -> "ADMIN".equals(stats.getTipoUsuario())));
        assertTrue(result.stream().anyMatch(stats -> "GESTOR".equals(stats.getTipoUsuario())));
        assertTrue(result.stream().anyMatch(stats -> "COLABORADOR".equals(stats.getTipoUsuario())));
        
        // Verificar contagem correta
        TipoUsuarioStatsDTO adminStats = result.stream()
                .filter(stats -> "ADMIN".equals(stats.getTipoUsuario()))
                .findFirst()
                .orElse(null);
        assertNotNull(adminStats);
        assertEquals(1L, adminStats.getTotal());
    }

    @Test
    void findDistinctSetores_DeveRetornarSetoresUnicos() {
        // Act
        List<String> result = usuarioRepository.findDistinctSetores();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains("TI"));
        assertTrue(result.contains("RH"));
        assertTrue(result.stream().allMatch(setor -> setor != null && !setor.isEmpty()));
    }

    @Test
    void save_ComUsuarioNovo_DeveSalvarUsuario() {
        // Arrange
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome("Novo Usuário");
        novoUsuario.setEmail("novo@teste.com");
        novoUsuario.setKeycloakId("keycloak-novo");
        novoUsuario.setTipoUsuario("COLABORADOR");
        novoUsuario.setSetor("Marketing");
        novoUsuario.setCargo("Analista");
        novoUsuario.setStatus(StatusUsuario.ATIVO);
        novoUsuario.setDataCriacao(LocalDateTime.now());

        // Act
        Usuario saved = usuarioRepository.save(novoUsuario);

        // Assert
        assertNotNull(saved.getId());
        assertEquals("Novo Usuário", saved.getNome());
        assertEquals("novo@teste.com", saved.getEmail());
        
        // Verificar se foi persistido no banco
        Optional<Usuario> found = usuarioRepository.findByEmail("novo@teste.com");
        assertTrue(found.isPresent());
        assertEquals("Novo Usuário", found.get().getNome());
    }

    @Test
    void save_ComUsuarioExistente_DeveAtualizarUsuario() {
        // Arrange
        usuario1.setNome("João Silva Atualizado");

        // Act
        Usuario updated = usuarioRepository.save(usuario1);

        // Assert
        assertEquals("João Silva Atualizado", updated.getNome());
        assertEquals(usuario1.getId(), updated.getId());
        
        // Verificar se foi atualizado no banco
        Optional<Usuario> found = usuarioRepository.findById(usuario1.getId());
        assertTrue(found.isPresent());
        assertEquals("João Silva Atualizado", found.get().getNome());
    }

    @Test
    void delete_ComUsuarioExistente_DeveRemoverUsuario() {
        // Arrange
        Long userId = usuario1.getId();

        // Act
        usuarioRepository.delete(usuario1);

        // Assert
        Optional<Usuario> found = usuarioRepository.findById(userId);
        assertFalse(found.isPresent());
    }

    @Test
    void findById_ComIdExistente_DeveRetornarUsuario() {
        // Act
        Optional<Usuario> result = usuarioRepository.findById(usuario1.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals("João Silva", result.get().getNome());
    }

    @Test
    void findById_ComIdInexistente_DeveRetornarVazio() {
        // Act
        Optional<Usuario> result = usuarioRepository.findById(999L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findAll_DeveRetornarTodosUsuarios() {
        // Act
        List<Usuario> result = usuarioRepository.findAll();

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(u -> u.getNome().equals("João Silva")));
        assertTrue(result.stream().anyMatch(u -> u.getNome().equals("Maria Santos")));
        assertTrue(result.stream().anyMatch(u -> u.getNome().equals("Pedro Costa")));
    }

    @Test
    void count_DeveRetornarNumeroCorretoDeUsuarios() {
        // Act
        long count = usuarioRepository.count();

        // Assert
        assertEquals(3, count);
    }

    @Test
    void existsById_ComIdExistente_DeveRetornarTrue() {
        // Act
        boolean exists = usuarioRepository.existsById(usuario1.getId());

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsById_ComIdInexistente_DeveRetornarFalse() {
        // Act
        boolean exists = usuarioRepository.existsById(999L);

        // Assert
        assertFalse(exists);
    }

    @Test
    void saveAll_ComListaDeUsuarios_DeveSalvarTodos() {
        // Arrange
        Usuario novo1 = new Usuario();
        novo1.setNome("Novo 1");
        novo1.setEmail("novo1@teste.com");
        novo1.setKeycloakId("keycloak-novo1");
        novo1.setTipoUsuario("COLABORADOR");
        novo1.setSetor("Marketing");
        novo1.setCargo("Analista");
        novo1.setStatus(StatusUsuario.ATIVO);
        novo1.setDataCriacao(LocalDateTime.now());

        Usuario novo2 = new Usuario();
        novo2.setNome("Novo 2");
        novo2.setEmail("novo2@teste.com");
        novo2.setKeycloakId("keycloak-novo2");
        novo2.setTipoUsuario("COLABORADOR");
        novo2.setSetor("Vendas");
        novo2.setCargo("Vendedor");
        novo2.setStatus(StatusUsuario.ATIVO);
        novo2.setDataCriacao(LocalDateTime.now());

        List<Usuario> usuarios = Arrays.asList(novo1, novo2);

        // Act
        List<Usuario> saved = usuarioRepository.saveAll(usuarios);

        // Assert
        assertEquals(2, saved.size());
        assertTrue(saved.stream().allMatch(u -> u.getId() != null));
        
        // Verificar se foram persistidos no banco
        assertEquals(5, usuarioRepository.count());
    }

    @Test
    void deleteAll_DeveRemoverTodosUsuarios() {
        // Act
        usuarioRepository.deleteAll();

        // Assert
        assertEquals(0, usuarioRepository.count());
        assertTrue(usuarioRepository.findAll().isEmpty());
    }

    @Test
    void deleteAllById_ComIdsExistentes_DeveRemoverUsuarios() {
        // Arrange
        List<Long> ids = Arrays.asList(usuario1.getId(), usuario2.getId());

        // Act
        usuarioRepository.deleteAllById(ids);

        // Assert
        assertEquals(1, usuarioRepository.count());
        assertFalse(usuarioRepository.existsById(usuario1.getId()));
        assertFalse(usuarioRepository.existsById(usuario2.getId()));
        assertTrue(usuarioRepository.existsById(usuario3.getId()));
    }
} 
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
import org.springframework.test.context.ActiveProfiles;

import br.com.pointer.pointer_back.model.Comunicado;
import br.com.pointer.pointer_back.model.ComunicadoLeitura;
import br.com.pointer.pointer_back.model.Usuario;

@DataJpaTest
@ActiveProfiles("test")
class ComunicadoLeituraRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ComunicadoLeituraRepository comunicadoLeituraRepository;

    @Autowired
    private ComunicadoRepository comunicadoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private ComunicadoLeitura leitura1;
    private ComunicadoLeitura leitura2;
    private ComunicadoLeitura leitura3;
    private Comunicado comunicado1;
    private Comunicado comunicado2;
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

        // Criar comunicados de teste
        comunicado1 = new Comunicado();
        comunicado1.setTitulo("Comunicado 1");
        comunicado1.setDescricao("Descrição do comunicado 1");
        Set<String> setores1 = new HashSet<>(Arrays.asList("TI", "RH"));
        comunicado1.setSetores(setores1);
        comunicado1.setApenasGestores(false);
        comunicado1.setAtivo(true);
        comunicado1.setDataPublicacao(LocalDateTime.now());
        entityManager.persistAndFlush(comunicado1);

        comunicado2 = new Comunicado();
        comunicado2.setTitulo("Comunicado 2");
        comunicado2.setDescricao("Descrição do comunicado 2");
        Set<String> setores2 = new HashSet<>(Arrays.asList("TI"));
        comunicado2.setSetores(setores2);
        comunicado2.setApenasGestores(true);
        comunicado2.setAtivo(true);
        comunicado2.setDataPublicacao(LocalDateTime.now());
        entityManager.persistAndFlush(comunicado2);

        // Criar leituras de teste
        leitura1 = new ComunicadoLeitura();
        leitura1.setComunicadoId(comunicado1.getId());
        leitura1.setUsuarioId(usuario1.getId());
        leitura1.setDtLeitura(LocalDateTime.now());

        leitura2 = new ComunicadoLeitura();
        leitura2.setComunicadoId(comunicado1.getId());
        leitura2.setUsuarioId(usuario2.getId());
        leitura2.setDtLeitura(LocalDateTime.now());

        leitura3 = new ComunicadoLeitura();
        leitura3.setComunicadoId(comunicado2.getId());
        leitura3.setUsuarioId(usuario1.getId());
        leitura3.setDtLeitura(LocalDateTime.now());

        // Persistir leituras
        entityManager.persistAndFlush(leitura1);
        entityManager.persistAndFlush(leitura2);
        entityManager.persistAndFlush(leitura3);
    }

    @Test
    void findByComunicadoIdAndUsuarioId_ComLeituraExistente_DeveRetornarLeitura() {
        // Act
        Optional<ComunicadoLeitura> result = comunicadoLeituraRepository.findByComunicadoIdAndUsuarioId(
                comunicado1.getId(), usuario1.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(comunicado1.getId(), result.get().getComunicadoId());
        assertEquals(usuario1.getId(), result.get().getUsuarioId());
    }

    @Test
    void findByComunicadoIdAndUsuarioId_ComLeituraInexistente_DeveRetornarVazio() {
        // Act
        Optional<ComunicadoLeitura> result = comunicadoLeituraRepository.findByComunicadoIdAndUsuarioId(
                999L, 999L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findByComunicadoId_ComComunicadoExistente_DeveRetornarLeituras() {
        // Act
        List<ComunicadoLeitura> result = comunicadoLeituraRepository.findByComunicadoId(comunicado1.getId());

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(l -> l.getComunicadoId().equals(comunicado1.getId())));
    }

    @Test
    void findByComunicadoId_ComComunicadoInexistente_DeveRetornarVazio() {
        // Act
        List<ComunicadoLeitura> result = comunicadoLeituraRepository.findByComunicadoId(999L);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void countByComunicadoId_ComComunicadoExistente_DeveRetornarContagem() {
        // Act
        int count = comunicadoLeituraRepository.countByComunicadoId(comunicado1.getId());

        // Assert
        assertEquals(2, count);
    }

    @Test
    void countByComunicadoId_ComComunicadoInexistente_DeveRetornarZero() {
        // Act
        int count = comunicadoLeituraRepository.countByComunicadoId(999L);

        // Assert
        assertEquals(0, count);
    }

    @Test
    void existsByComunicadoIdAndUsuarioId_ComLeituraExistente_DeveRetornarTrue() {
        // Act
        boolean exists = comunicadoLeituraRepository.existsByComunicadoIdAndUsuarioId(
                comunicado1.getId(), usuario1.getId());

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsByComunicadoIdAndUsuarioId_ComLeituraInexistente_DeveRetornarFalse() {
        // Act
        boolean exists = comunicadoLeituraRepository.existsByComunicadoIdAndUsuarioId(999L, 999L);

        // Assert
        assertFalse(exists);
    }

    @Test
    void save_ComLeituraNova_DeveSalvarLeitura() {
        // Arrange
        ComunicadoLeitura novaLeitura = new ComunicadoLeitura();
        novaLeitura.setComunicadoId(comunicado2.getId());
        novaLeitura.setUsuarioId(usuario2.getId());
        novaLeitura.setDtLeitura(LocalDateTime.now());

        // Act
        ComunicadoLeitura saved = comunicadoLeituraRepository.save(novaLeitura);

        // Assert
        assertNotNull(saved.getId());
        assertEquals(comunicado2.getId(), saved.getComunicadoId());
        assertEquals(usuario2.getId(), saved.getUsuarioId());
        
        // Verificar se foi persistido no banco
        Optional<ComunicadoLeitura> found = comunicadoLeituraRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals(comunicado2.getId(), found.get().getComunicadoId());
    }

    @Test
    void save_ComLeituraExistente_DeveAtualizarLeitura() {
        // Arrange
        LocalDateTime novaData = LocalDateTime.now().plusHours(1);
        leitura1.setDtLeitura(novaData);

        // Act
        ComunicadoLeitura updated = comunicadoLeituraRepository.save(leitura1);

        // Assert
        assertEquals(novaData, updated.getDtLeitura());
        assertEquals(leitura1.getId(), updated.getId());
        
        // Verificar se foi atualizado no banco
        Optional<ComunicadoLeitura> found = comunicadoLeituraRepository.findById(leitura1.getId());
        assertTrue(found.isPresent());
        assertEquals(novaData, found.get().getDtLeitura());
    }

    @Test
    void delete_ComLeituraExistente_DeveRemoverLeitura() {
        // Arrange
        Long leituraId = leitura1.getId();

        // Act
        comunicadoLeituraRepository.delete(leitura1);

        // Assert
        Optional<ComunicadoLeitura> found = comunicadoLeituraRepository.findById(leituraId);
        assertFalse(found.isPresent());
    }

    @Test
    void findById_ComIdExistente_DeveRetornarLeitura() {
        // Act
        Optional<ComunicadoLeitura> result = comunicadoLeituraRepository.findById(leitura1.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(comunicado1.getId(), result.get().getComunicadoId());
        assertEquals(usuario1.getId(), result.get().getUsuarioId());
    }

    @Test
    void findById_ComIdInexistente_DeveRetornarVazio() {
        // Act
        Optional<ComunicadoLeitura> result = comunicadoLeituraRepository.findById(999L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findAll_DeveRetornarTodasLeituras() {
        // Act
        List<ComunicadoLeitura> result = comunicadoLeituraRepository.findAll();

        // Assert
        assertEquals(3, result.size());
    }

    @Test
    void count_DeveRetornarNumeroCorretoDeLeituras() {
        // Act
        long count = comunicadoLeituraRepository.count();

        // Assert
        assertEquals(3, count);
    }

    @Test
    void existsById_ComIdExistente_DeveRetornarTrue() {
        // Act
        boolean exists = comunicadoLeituraRepository.existsById(leitura1.getId());

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsById_ComIdInexistente_DeveRetornarFalse() {
        // Act
        boolean exists = comunicadoLeituraRepository.existsById(999L);

        // Assert
        assertFalse(exists);
    }

    @Test
    void saveAll_ComListaDeLeituras_DeveSalvarTodas() {
        // Arrange
        ComunicadoLeitura nova1 = new ComunicadoLeitura();
        nova1.setComunicadoId(comunicado1.getId());
        nova1.setUsuarioId(usuario1.getId());
        nova1.setDtLeitura(LocalDateTime.now());

        ComunicadoLeitura nova2 = new ComunicadoLeitura();
        nova2.setComunicadoId(comunicado2.getId());
        nova2.setUsuarioId(usuario2.getId());
        nova2.setDtLeitura(LocalDateTime.now());

        List<ComunicadoLeitura> leituras = Arrays.asList(nova1, nova2);

        // Act
        List<ComunicadoLeitura> saved = comunicadoLeituraRepository.saveAll(leituras);

        // Assert
        assertEquals(2, saved.size());
        assertTrue(saved.stream().allMatch(l -> l.getId() != null));
        
        // Verificar se foram persistidos no banco
        assertEquals(5, comunicadoLeituraRepository.count());
    }

    @Test
    void deleteAll_DeveRemoverTodasLeituras() {
        // Act
        comunicadoLeituraRepository.deleteAll();

        // Assert
        assertEquals(0, comunicadoLeituraRepository.count());
        assertTrue(comunicadoLeituraRepository.findAll().isEmpty());
    }

    @Test
    void deleteAllById_ComIdsExistentes_DeveRemoverLeituras() {
        // Arrange
        List<Long> ids = Arrays.asList(leitura1.getId(), leitura2.getId());

        // Act
        comunicadoLeituraRepository.deleteAllById(ids);

        // Assert
        assertEquals(1, comunicadoLeituraRepository.count());
        assertFalse(comunicadoLeituraRepository.existsById(leitura1.getId()));
        assertFalse(comunicadoLeituraRepository.existsById(leitura2.getId()));
        assertTrue(comunicadoLeituraRepository.existsById(leitura3.getId()));
    }

    @Test
    void findByComunicadoId_ComComunicadoSemLeituras_DeveRetornarVazio() {
        // Arrange - Criar comunicado sem leituras
        Comunicado comunicadoSemLeituras = new Comunicado();
        comunicadoSemLeituras.setTitulo("Comunicado Sem Leituras");
        comunicadoSemLeituras.setDescricao("Descrição");
        Set<String> setores = new HashSet<>(Arrays.asList("Marketing"));
        comunicadoSemLeituras.setSetores(setores);
        comunicadoSemLeituras.setApenasGestores(false);
        comunicadoSemLeituras.setAtivo(true);
        comunicadoSemLeituras.setDataPublicacao(LocalDateTime.now());
        entityManager.persistAndFlush(comunicadoSemLeituras);

        // Act
        List<ComunicadoLeitura> result = comunicadoLeituraRepository.findByComunicadoId(comunicadoSemLeituras.getId());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void countByComunicadoId_ComComunicadoSemLeituras_DeveRetornarZero() {
        // Arrange - Criar comunicado sem leituras
        Comunicado comunicadoSemLeituras = new Comunicado();
        comunicadoSemLeituras.setTitulo("Comunicado Sem Leituras");
        comunicadoSemLeituras.setDescricao("Descrição");
        Set<String> setores = new HashSet<>(Arrays.asList("Marketing"));
        comunicadoSemLeituras.setSetores(setores);
        comunicadoSemLeituras.setApenasGestores(false);
        comunicadoSemLeituras.setAtivo(true);
        comunicadoSemLeituras.setDataPublicacao(LocalDateTime.now());
        entityManager.persistAndFlush(comunicadoSemLeituras);

        // Act
        int count = comunicadoLeituraRepository.countByComunicadoId(comunicadoSemLeituras.getId());

        // Assert
        assertEquals(0, count);
    }

    @Test
    void save_ComLeituraSemComunicado_DeveLancarExcecao() {
        // Arrange
        ComunicadoLeitura leituraSemComunicado = new ComunicadoLeitura();
        // Não definir comunicado
        leituraSemComunicado.setUsuarioId(usuario1.getId());
        leituraSemComunicado.setDtLeitura(LocalDateTime.now());

        // Act & Assert
        assertThrows(Exception.class, () -> {
            comunicadoLeituraRepository.save(leituraSemComunicado);
        });
    }

    @Test
    void save_ComLeituraSemUsuario_DeveLancarExcecao() {
        // Arrange
        ComunicadoLeitura leituraSemUsuario = new ComunicadoLeitura();
        leituraSemUsuario.setComunicadoId(comunicado1.getId());
        // Não definir usuário
        leituraSemUsuario.setDtLeitura(LocalDateTime.now());

        // Act & Assert
        assertThrows(Exception.class, () -> {
            comunicadoLeituraRepository.save(leituraSemUsuario);
        });
    }

    @Test
    void save_ComLeituraSemDataLeitura_DeveLancarExcecao() {
        // Arrange
        ComunicadoLeitura leituraSemData = new ComunicadoLeitura();
        leituraSemData.setComunicadoId(comunicado1.getId());
        leituraSemData.setUsuarioId(usuario1.getId());
        // Não definir data de leitura

        // Act & Assert
        assertThrows(Exception.class, () -> {
            comunicadoLeituraRepository.save(leituraSemData);
        });
    }

    @Test
    void findByComunicadoId_ComComunicadoComUmaLeitura_DeveRetornarUmaLeitura() {
        // Act
        List<ComunicadoLeitura> result = comunicadoLeituraRepository.findByComunicadoId(comunicado2.getId());

        // Assert
        assertEquals(1, result.size());
        assertEquals(comunicado2.getId(), result.get(0).getComunicadoId());
    }

    @Test
    void existsByComunicadoIdAndUsuarioId_ComComunicadoSemLeituraDoUsuario_DeveRetornarFalse() {
        // Act
        boolean exists = comunicadoLeituraRepository.existsByComunicadoIdAndUsuarioId(
                comunicado1.getId(), 999L); // Usuário inexistente

        // Assert
        assertFalse(exists);
    }

    @Test
    void save_ComLeituraDuplicada_DevePermitirLeituraDuplicada() {
        // Arrange - Tentar criar leitura duplicada
        ComunicadoLeitura leituraDuplicada = new ComunicadoLeitura();
        leituraDuplicada.setComunicadoId(comunicado1.getId());
        leituraDuplicada.setUsuarioId(usuario1.getId()); // Mesmo usuário e comunicado da leitura1
        leituraDuplicada.setDtLeitura(LocalDateTime.now());

        // Act
        ComunicadoLeitura saved = comunicadoLeituraRepository.save(leituraDuplicada);

        // Assert
        assertNotNull(saved.getId());
        assertEquals(comunicado1.getId(), saved.getComunicadoId());
        assertEquals(usuario1.getId(), saved.getUsuarioId());
        
        // Verificar que a leitura foi salva com sucesso
        assertTrue(comunicadoLeituraRepository.existsById(saved.getId()));
        
        // Verificar que agora temos mais leituras para o comunicado1
        List<ComunicadoLeitura> leiturasComunicado1 = comunicadoLeituraRepository.findByComunicadoId(comunicado1.getId());
        assertTrue(leiturasComunicado1.size() > 1); // Deve ter mais que 1 leitura para o comunicado1
    }
} 
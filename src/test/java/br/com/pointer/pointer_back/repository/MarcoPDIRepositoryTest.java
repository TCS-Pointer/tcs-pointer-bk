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

import br.com.pointer.pointer_back.enums.StatusMarcoPDI;
import br.com.pointer.pointer_back.enums.StatusPDI;
import br.com.pointer.pointer_back.model.MarcoPDI;
import br.com.pointer.pointer_back.model.PDI;
import br.com.pointer.pointer_back.model.Usuario;

@DataJpaTest
@ActiveProfiles("test")
class MarcoPDIRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MarcoPDIRepository marcoPDIRepository;

    @Autowired
    private PDIRepository pdiRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private MarcoPDI marco1;
    private MarcoPDI marco2;
    private MarcoPDI marco3;
    private PDI pdi1;
    private PDI pdi2;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Limpar dados anteriores
        entityManager.clear();

        // Criar usuário de teste
        usuario = new Usuario();
        usuario.setNome("João Silva");
        usuario.setEmail("joao@teste.com");
        usuario.setKeycloakId("keycloak-joao");
        usuario.setTipoUsuario("GESTOR");
        usuario.setSetor("TI");
        usuario.setCargo("Gerente");
        usuario.setStatus(br.com.pointer.pointer_back.model.StatusUsuario.ATIVO);
        usuario.setDataCriacao(LocalDateTime.now());
        entityManager.persistAndFlush(usuario);

        // Criar PDIs de teste
        pdi1 = new PDI();
        pdi1.setTitulo("PDI Desenvolvimento");
        pdi1.setDescricao("Plano de desenvolvimento individual");
        pdi1.setStatus(StatusPDI.EM_ANDAMENTO);
        pdi1.setDtInicio(LocalDate.now());
        pdi1.setDtFim(LocalDate.now().plusMonths(6));
        pdi1.setDestinatario(usuario);
        pdi1.setIdUsuario(usuario.getId());
        entityManager.persistAndFlush(pdi1);

        pdi2 = new PDI();
        pdi2.setTitulo("PDI Liderança");
        pdi2.setDescricao("Plano de desenvolvimento de liderança");
        pdi2.setStatus(StatusPDI.EM_ANDAMENTO);
        pdi2.setDtInicio(LocalDate.now());
        pdi2.setDtFim(LocalDate.now().plusMonths(12));
        pdi2.setDestinatario(usuario);
        pdi2.setIdUsuario(usuario.getId());
        entityManager.persistAndFlush(pdi2);

        // Criar marcos de teste
        marco1 = new MarcoPDI();
        marco1.setTitulo("Marco 1");
        marco1.setDescricao("Primeiro marco do PDI");
        marco1.setStatus(StatusMarcoPDI.PENDENTE);
        marco1.setDtFinal(LocalDate.now().plusMonths(2));
        marco1.setPdi(pdi1);

        marco2 = new MarcoPDI();
        marco2.setTitulo("Marco 2");
        marco2.setDescricao("Segundo marco do PDI");
        marco2.setStatus(StatusMarcoPDI.CONCLUIDO);
        marco2.setDtFinal(LocalDate.now().plusMonths(4));
        marco2.setPdi(pdi1);

        marco3 = new MarcoPDI();
        marco3.setTitulo("Marco 3");
        marco3.setDescricao("Marco de outro PDI");
        marco3.setStatus(StatusMarcoPDI.PENDENTE);
        marco3.setDtFinal(LocalDate.now().plusMonths(3));
        marco3.setPdi(pdi2);

        // Persistir marcos
        entityManager.persistAndFlush(marco1);
        entityManager.persistAndFlush(marco2);
        entityManager.persistAndFlush(marco3);
    }

    @Test
    void findByPdiId_ComPdiExistente_DeveRetornarMarcos() {
        // Act
        List<MarcoPDI> result = marcoPDIRepository.findByPdiId(pdi1.getId());

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(m -> m.getPdi().getId().equals(pdi1.getId())));
        assertTrue(result.stream().anyMatch(m -> m.getTitulo().equals("Marco 1")));
        assertTrue(result.stream().anyMatch(m -> m.getTitulo().equals("Marco 2")));
    }

    @Test
    void findByPdiId_ComPdiInexistente_DeveRetornarVazio() {
        // Act
        List<MarcoPDI> result = marcoPDIRepository.findByPdiId(999L);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByPdiId_ComPdiSemMarcos_DeveRetornarVazio() {
        // Arrange - Criar PDI sem marcos
        PDI pdiSemMarcos = new PDI();
        pdiSemMarcos.setTitulo("PDI Sem Marcos");
        pdiSemMarcos.setDescricao("PDI sem marcos");
        pdiSemMarcos.setStatus(StatusPDI.EM_ANDAMENTO);
        pdiSemMarcos.setDtInicio(LocalDate.now());
        pdiSemMarcos.setDtFim(LocalDate.now().plusMonths(3));
        pdiSemMarcos.setDestinatario(usuario);
        pdiSemMarcos.setIdUsuario(usuario.getId());
        entityManager.persistAndFlush(pdiSemMarcos);

        // Act
        List<MarcoPDI> result = marcoPDIRepository.findByPdiId(pdiSemMarcos.getId());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void save_ComMarcoNovo_DeveSalvarMarco() {
        // Arrange
        MarcoPDI novoMarco = new MarcoPDI();
        novoMarco.setTitulo("Novo Marco");
        novoMarco.setDescricao("Descrição do novo marco");
        novoMarco.setStatus(StatusMarcoPDI.PENDENTE);
        novoMarco.setDtFinal(LocalDate.now().plusMonths(1));
        novoMarco.setPdi(pdi1);

        // Act
        MarcoPDI saved = marcoPDIRepository.save(novoMarco);

        // Assert
        assertNotNull(saved.getId());
        assertEquals("Novo Marco", saved.getTitulo());
        assertEquals("Descrição do novo marco", saved.getDescricao());
        assertEquals(StatusMarcoPDI.PENDENTE, saved.getStatus());
        
        // Verificar se foi persistido no banco
        Optional<MarcoPDI> found = marcoPDIRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Novo Marco", found.get().getTitulo());
    }

    @Test
    void save_ComMarcoExistente_DeveAtualizarMarco() {
        // Arrange
        marco1.setTitulo("Marco Atualizado");

        // Act
        MarcoPDI updated = marcoPDIRepository.save(marco1);

        // Assert
        assertEquals("Marco Atualizado", updated.getTitulo());
        assertEquals(marco1.getId(), updated.getId());
        
        // Verificar se foi atualizado no banco
        Optional<MarcoPDI> found = marcoPDIRepository.findById(marco1.getId());
        assertTrue(found.isPresent());
        assertEquals("Marco Atualizado", found.get().getTitulo());
    }

    @Test
    void delete_ComMarcoExistente_DeveRemoverMarco() {
        // Arrange
        Long marcoId = marco1.getId();

        // Act
        marcoPDIRepository.delete(marco1);

        // Assert
        Optional<MarcoPDI> found = marcoPDIRepository.findById(marcoId);
        assertFalse(found.isPresent());
    }

    @Test
    void findById_ComIdExistente_DeveRetornarMarco() {
        // Act
        Optional<MarcoPDI> result = marcoPDIRepository.findById(marco1.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Marco 1", result.get().getTitulo());
    }

    @Test
    void findById_ComIdInexistente_DeveRetornarVazio() {
        // Act
        Optional<MarcoPDI> result = marcoPDIRepository.findById(999L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findAll_DeveRetornarTodosMarcos() {
        // Act
        List<MarcoPDI> result = marcoPDIRepository.findAll();

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(m -> m.getTitulo().equals("Marco 1")));
        assertTrue(result.stream().anyMatch(m -> m.getTitulo().equals("Marco 2")));
        assertTrue(result.stream().anyMatch(m -> m.getTitulo().equals("Marco 3")));
    }

    @Test
    void count_DeveRetornarNumeroCorretoDeMarcos() {
        // Act
        long count = marcoPDIRepository.count();

        // Assert
        assertEquals(3, count);
    }

    @Test
    void existsById_ComIdExistente_DeveRetornarTrue() {
        // Act
        boolean exists = marcoPDIRepository.existsById(marco1.getId());

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsById_ComIdInexistente_DeveRetornarFalse() {
        // Act
        boolean exists = marcoPDIRepository.existsById(999L);

        // Assert
        assertFalse(exists);
    }

    @Test
    void saveAll_ComListaDeMarcos_DeveSalvarTodos() {
        // Arrange
        MarcoPDI novo1 = new MarcoPDI();
        novo1.setTitulo("Novo Marco 1");
        novo1.setDescricao("Descrição 1");
        novo1.setStatus(StatusMarcoPDI.PENDENTE);
        novo1.setDtFinal(LocalDate.now().plusMonths(1));
        novo1.setPdi(pdi1);

        MarcoPDI novo2 = new MarcoPDI();
        novo2.setTitulo("Novo Marco 2");
        novo2.setDescricao("Descrição 2");
        novo2.setStatus(StatusMarcoPDI.PENDENTE);
        novo2.setDtFinal(LocalDate.now().plusMonths(2));
        novo2.setPdi(pdi2);

        List<MarcoPDI> marcos = Arrays.asList(novo1, novo2);

        // Act
        List<MarcoPDI> saved = marcoPDIRepository.saveAll(marcos);

        // Assert
        assertEquals(2, saved.size());
        assertTrue(saved.stream().allMatch(m -> m.getId() != null));
        
        // Verificar se foram persistidos no banco
        assertEquals(5, marcoPDIRepository.count());
    }

    @Test
    void deleteAll_DeveRemoverTodosMarcos() {
        // Act
        marcoPDIRepository.deleteAll();

        // Assert
        assertEquals(0, marcoPDIRepository.count());
        assertTrue(marcoPDIRepository.findAll().isEmpty());
    }

    @Test
    void deleteAllById_ComIdsExistentes_DeveRemoverMarcos() {
        // Arrange
        List<Long> ids = Arrays.asList(marco1.getId(), marco2.getId());

        // Act
        marcoPDIRepository.deleteAllById(ids);

        // Assert
        assertEquals(1, marcoPDIRepository.count());
        assertFalse(marcoPDIRepository.existsById(marco1.getId()));
        assertFalse(marcoPDIRepository.existsById(marco2.getId()));
        assertTrue(marcoPDIRepository.existsById(marco3.getId()));
    }

    @Test
    void save_ComMarcoSemPDI_DeveLancarExcecao() {
        // Arrange
        MarcoPDI marcoSemPDI = new MarcoPDI();
        marcoSemPDI.setTitulo("Marco Sem PDI");
        marcoSemPDI.setDescricao("Descrição");
        marcoSemPDI.setStatus(StatusMarcoPDI.PENDENTE);
        marcoSemPDI.setDtFinal(LocalDate.now().plusMonths(1));
        // Não definir PDI

        // Act & Assert
        assertThrows(Exception.class, () -> {
            marcoPDIRepository.save(marcoSemPDI);
        });
    }

    @Test
    void save_ComMarcoSemTitulo_DeveLancarExcecao() {
        // Arrange
        MarcoPDI marcoSemTitulo = new MarcoPDI();
        // Não definir título
        marcoSemTitulo.setDescricao("Descrição");
        marcoSemTitulo.setStatus(StatusMarcoPDI.PENDENTE);
        marcoSemTitulo.setDtFinal(LocalDate.now().plusMonths(1));
        marcoSemTitulo.setPdi(pdi1);

        // Act & Assert
        assertThrows(Exception.class, () -> {
            marcoPDIRepository.save(marcoSemTitulo);
        });
    }

    @Test
    void save_ComMarcoSemDescricao_DeveLancarExcecao() {
        // Arrange
        MarcoPDI marcoSemDescricao = new MarcoPDI();
        marcoSemDescricao.setTitulo("Marco Sem Descrição");
        // Não definir descrição
        marcoSemDescricao.setStatus(StatusMarcoPDI.PENDENTE);
        marcoSemDescricao.setDtFinal(LocalDate.now().plusMonths(1));
        marcoSemDescricao.setPdi(pdi1);

        // Act & Assert
        assertThrows(Exception.class, () -> {
            marcoPDIRepository.save(marcoSemDescricao);
        });
    }

    @Test
    void save_ComMarcoComDatasIguais_DeveSalvarMarco() {
        // Arrange
        LocalDate data = LocalDate.now();
        MarcoPDI marcoDatasIguais = new MarcoPDI();
        marcoDatasIguais.setTitulo("Marco Datas Iguais");
        marcoDatasIguais.setDescricao("Descrição");
        marcoDatasIguais.setStatus(StatusMarcoPDI.PENDENTE);
        marcoDatasIguais.setDtFinal(data);
        marcoDatasIguais.setDtFinal(data); // Mesma data
        marcoDatasIguais.setPdi(pdi1);

        // Act
        MarcoPDI saved = marcoPDIRepository.save(marcoDatasIguais);

        // Assert
        assertNotNull(saved.getId());
        assertEquals(data, saved.getDtFinal());
        assertEquals(data, saved.getDtFinal());
    }
} 
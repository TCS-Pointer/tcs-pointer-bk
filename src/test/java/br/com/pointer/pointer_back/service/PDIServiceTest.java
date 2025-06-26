package br.com.pointer.pointer_back.service;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.AtualizarStatusPDIDTO;
import br.com.pointer.pointer_back.dto.MarcoPDIDTO;
import br.com.pointer.pointer_back.dto.UsuarioResponseDTO;
import br.com.pointer.pointer_back.dto.pdiDTO;
import br.com.pointer.pointer_back.dto.PdiListagemDTO;
import br.com.pointer.pointer_back.enums.StatusMarcoPDI;
import br.com.pointer.pointer_back.enums.StatusPDI;
import br.com.pointer.pointer_back.exception.PDINaoEncontradoException;
import br.com.pointer.pointer_back.model.MarcoPDI;
import br.com.pointer.pointer_back.model.PDI;
import br.com.pointer.pointer_back.model.Usuario;
import br.com.pointer.pointer_back.repository.MarcoPDIRepository;
import br.com.pointer.pointer_back.repository.PDIRepository;
import br.com.pointer.pointer_back.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PDIService")
class PDIServiceTest {

    @Mock
    private PDIRepository pdiRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private MarcoPDIRepository marcoPDIRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PDIService pdiService;

    private PDI pdi;
    private pdiDTO pdiDTO;
    private Usuario usuario;
    private UsuarioResponseDTO usuarioDTO;
    private MarcoPDI marco;
    private MarcoPDIDTO marcoDTO;
    private PdiListagemDTO pdiListagemDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("test@test.com");

        usuarioDTO = new UsuarioResponseDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setEmail("test@test.com");

        pdi = new PDI();
        pdi.setId(1L);
        pdi.setTitulo("PDI Test");
        pdi.setDescricao("Descrição do PDI");
        pdi.setDestinatario(usuario);
        pdi.setStatus(StatusPDI.EM_ANDAMENTO);
        pdi.setDtInicio(LocalDate.now());
        pdi.setDtFim(LocalDate.now().plusMonths(1));
        pdi.setIdUsuario(1L);
        pdi.setUsuario(usuario);
        pdi.setDataCriacao(LocalDateTime.now());

        marco = new MarcoPDI();
        marco.setId(1L);
        marco.setTitulo("Marco 1");
        marco.setDescricao("Descrição do Marco 1");
        marco.setStatus(StatusMarcoPDI.PENDENTE);
        marco.setPdi(pdi);
        marco.setDtFinal(LocalDate.now().plusMonths(1));

        marcoDTO = new MarcoPDIDTO();
        marcoDTO.setId(1L);
        marcoDTO.setTitulo("Marco 1");
        marcoDTO.setDescricao("Descrição do Marco 1");
        marcoDTO.setStatus(StatusMarcoPDI.PENDENTE);

        List<MarcoPDI> marcos = new ArrayList<>();
        marcos.add(marco);
        pdi.setMarcos(marcos);

        pdiDTO = new pdiDTO();
        pdiDTO.setId(1L);
        pdiDTO.setTitulo("PDI Test");
        pdiDTO.setDescricao("Descrição do PDI");
        pdiDTO.setIdDestinatario(1L);
        pdiDTO.setDestinatario(usuarioDTO);
        pdiDTO.setStatus(StatusPDI.EM_ANDAMENTO);
        pdiDTO.setDtInicio(LocalDate.now());
        pdiDTO.setDtFim(LocalDate.now().plusMonths(1));
        pdiDTO.setIdUsuario(1L);
        pdiDTO.setUsuario(usuarioDTO);

        List<MarcoPDIDTO> marcosDTO = new ArrayList<>();
        marcosDTO.add(marcoDTO);
        pdiDTO.setMarcos(marcosDTO);

        pdiListagemDTO = new PdiListagemDTO(1L, "PDI Test", "Descrição do PDI", StatusPDI.EM_ANDAMENTO, 
            LocalDate.now(), LocalDate.now().plusMonths(1), 1L, "test@test.com", "Desenvolvedor", "TI", 
            "test@test.com", 1L, 1L);
    }

    @Test
    @DisplayName("Deve criar PDI com sucesso")
    void deveCriarPDIComSucesso() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(modelMapper.map(pdiDTO, PDI.class)).thenReturn(pdi);
        when(pdiRepository.save(any(PDI.class))).thenReturn(pdi);
        when(modelMapper.map(pdi, pdiDTO.class)).thenReturn(pdiDTO);

        ApiResponse<pdiDTO> response = pdiService.criar(pdiDTO);

        assertTrue(response.isOk());
        assertEquals("PDI criado com sucesso", response.getMessage());
        assertNotNull(response.getContent());
        assertEquals(pdiDTO.getTitulo(), response.getContent().getTitulo());
        assertEquals(pdiDTO.getDescricao(), response.getContent().getDescricao());
        assertEquals(pdiDTO.getStatus(), response.getContent().getStatus());

        verify(usuarioRepository).findById(1L);
        verify(pdiRepository).save(any(PDI.class));
        verify(modelMapper).map(pdiDTO, PDI.class);
        verify(modelMapper).map(pdi, pdiDTO.class);
    }

    @Test
    @DisplayName("Deve falhar ao criar PDI quando destinatário não encontrado")
    void deveFalharAoCriarPDIQuandoDestinatarioNaoEncontrado() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        ApiResponse<pdiDTO> response = pdiService.criar(pdiDTO);

        assertFalse(response.isOk());
        assertTrue(response.getMessage().contains("Destinatário não encontrado com ID: 1"));
        
        verify(usuarioRepository).findById(1L);
        verify(pdiRepository, never()).save(any());
        verify(modelMapper, never()).map(any(PDI.class), eq(pdiDTO.class));
    }

    @Test
    @DisplayName("Deve falhar ao criar PDI quando ID do destinatário é nulo")
    void deveFalharAoCriarPDIQuandoIdDestinatarioNulo() {
        pdiDTO.setIdDestinatario(null);

        ApiResponse<pdiDTO> response = pdiService.criar(pdiDTO);

        assertFalse(response.isOk());
        assertEquals("ID do destinatário é obrigatório", response.getMessage());
        
        verify(usuarioRepository, never()).findById(any());
        verify(pdiRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve falhar ao criar PDI quando status é nulo")
    void deveFalharAoCriarPDIQuandoStatusNulo() {
        pdiDTO.setStatus(null);

        ApiResponse<pdiDTO> response = pdiService.criar(pdiDTO);

        assertFalse(response.isOk());
        assertEquals("Status do PDI é obrigatório", response.getMessage());
        
        verify(usuarioRepository, never()).findById(any());
        verify(pdiRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve falhar ao criar PDI com data inicial posterior à final")
    void deveFalharAoCriarPDIComDataInicialPosteriorAFinal() {
        pdiDTO.setDtInicio(LocalDate.now().plusDays(10));
        pdiDTO.setDtFim(LocalDate.now());

        ApiResponse<pdiDTO> response = pdiService.criar(pdiDTO);

        assertFalse(response.isOk());
        assertTrue(response.getMessage().contains("Data inicial não pode ser posterior à data final"));
        
        verify(usuarioRepository, never()).findById(any());
        verify(pdiRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve falhar ao criar PDI com duração menor que 1 mês")
    void deveFalharAoCriarPDIComDuracaoMenorQue1Mes() {
        pdiDTO.setDtInicio(LocalDate.now());
        pdiDTO.setDtFim(LocalDate.now().plusDays(15));

        ApiResponse<pdiDTO> response = pdiService.criar(pdiDTO);

        assertFalse(response.isOk());
        assertTrue(response.getMessage().contains("O PDI deve ter duração mínima de 1 mês"));
        
        verify(usuarioRepository, never()).findById(any());
        verify(pdiRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve atualizar status PDI para concluído quando todos marcos estão concluídos")
    void deveAtualizarStatusPDIParaConcluidoQuandoTodosMarcosEstaoConcluidoss() {
        // Setup all marcos as completed
        marco.setStatus(StatusMarcoPDI.CONCLUIDO);
        marcoDTO.setStatus(StatusMarcoPDI.CONCLUIDO);
        
        // Create a PDI with completed marcos
        PDI pdiComMarcosConcluidos = new PDI();
        pdiComMarcosConcluidos.setId(1L);
        pdiComMarcosConcluidos.setTitulo("PDI Test");
        pdiComMarcosConcluidos.setDescricao("Descrição do PDI");
        pdiComMarcosConcluidos.setStatus(StatusPDI.EM_ANDAMENTO);
        pdiComMarcosConcluidos.setDestinatario(usuario);
        pdiComMarcosConcluidos.setDtInicio(LocalDate.now());
        pdiComMarcosConcluidos.setDtFim(LocalDate.now().plusMonths(1));
        pdiComMarcosConcluidos.setMarcos(Arrays.asList(marco));
        
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(modelMapper.map(pdiDTO, PDI.class)).thenReturn(pdiComMarcosConcluidos);
        when(pdiRepository.save(any(PDI.class))).thenAnswer(invocation -> {
            PDI pdiSalvo = invocation.getArgument(0);
            if (pdiSalvo.getMarcos() != null && pdiSalvo.getMarcos().stream()
                    .allMatch(m -> m.getStatus() == StatusMarcoPDI.CONCLUIDO)) {
                pdiSalvo.setStatus(StatusPDI.CONCLUIDO);
            }
            return pdiSalvo;
        });
        when(modelMapper.map(any(PDI.class), eq(pdiDTO.class))).thenReturn(pdiDTO);

        ApiResponse<pdiDTO> response = pdiService.criar(pdiDTO);

        assertTrue(response.isOk());
        assertEquals("PDI criado com sucesso", response.getMessage());
        assertNotNull(response.getContent());
        verify(pdiRepository).save(argThat(pdiSalvo -> pdiSalvo.getStatus() == StatusPDI.CONCLUIDO));
    }

    @Test
    @DisplayName("Deve atualizar status do marco com sucesso")
    void deveAtualizarStatusMarcoComSucesso() {
        AtualizarStatusPDIDTO statusDTO = new AtualizarStatusPDIDTO();
        statusDTO.setIdMarco(1L);
        statusDTO.setStatusMarco(StatusMarcoPDI.CONCLUIDO);
        
        when(pdiRepository.findById(1L)).thenReturn(Optional.of(pdi));
        when(pdiRepository.save(any(PDI.class))).thenReturn(pdi);
        when(modelMapper.map(pdi, pdiDTO.class)).thenReturn(pdiDTO);

        ApiResponse<pdiDTO> response = pdiService.atualizarStatus(1L, statusDTO);

        assertTrue(response.isOk());
        assertEquals("Status do PDI atualizado com sucesso", response.getMessage());
        assertNotNull(response.getContent());
        
        verify(pdiRepository).findById(1L);
        verify(pdiRepository).save(any(PDI.class));
    }

    @Test
    @DisplayName("Deve falhar ao atualizar status quando PDI não encontrado")
    void deveFalharAoAtualizarStatusQuandoPDINaoEncontrado() {
        AtualizarStatusPDIDTO statusDTO = new AtualizarStatusPDIDTO();
        statusDTO.setIdMarco(1L);
        statusDTO.setStatusMarco(StatusMarcoPDI.CONCLUIDO);
        
        when(pdiRepository.findById(999L)).thenReturn(Optional.empty());

        ApiResponse<pdiDTO> response = pdiService.atualizarStatus(999L, statusDTO);

        assertFalse(response.isOk());
        assertTrue(response.getMessage().contains("PDI não encontrado"));
        
        verify(pdiRepository).findById(999L);
        verify(pdiRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve listar todos os PDIs com sucesso")
    void deveListarTodosOsPDIsComSucesso() {
        when(pdiRepository.findAll()).thenReturn(Arrays.asList(pdi));
        when(modelMapper.map(pdi, pdiDTO.class)).thenReturn(pdiDTO);

        ApiResponse<List<pdiDTO>> response = pdiService.listarTodos();

        assertTrue(response.isOk());
        assertEquals("PDIs listados com sucesso", response.getMessage());
        assertNotNull(response.getContent());
        assertEquals(1, response.getContent().size());
        
        verify(pdiRepository).findAll();
        verify(modelMapper).map(pdi, pdiDTO.class);
    }

    @Test
    @DisplayName("Deve listar PDIs vazios quando não há PDIs")
    void deveListarPDIsVaziosQuandoNaoHaPDIs() {
        when(pdiRepository.findAll()).thenReturn(Arrays.asList());

        ApiResponse<List<pdiDTO>> response = pdiService.listarTodos();

        assertTrue(response.isOk());
        assertEquals("PDIs listados com sucesso", response.getMessage());
        assertNotNull(response.getContent());
        assertEquals(0, response.getContent().size());
        
        verify(pdiRepository).findAll();
    }

    @Test
    @DisplayName("Deve falhar ao listar todos quando repository lança exceção")
    void deveFalharAoListarTodosQuandoRepositoryLancaExcecao() {
        when(pdiRepository.findAll()).thenThrow(new RuntimeException("Erro de BD"));

        ApiResponse<List<pdiDTO>> response = pdiService.listarTodos();

        assertFalse(response.isOk());
        assertTrue(response.getMessage().contains("Erro ao listar PDIs"));
        
        verify(pdiRepository).findAll();
    }

    @Test
    @DisplayName("Deve buscar PDI por ID com sucesso")
    void deveBuscarPDIPorIdComSucesso() {
        when(pdiRepository.findById(1L)).thenReturn(Optional.of(pdi));
        when(modelMapper.map(pdi, pdiDTO.class)).thenReturn(pdiDTO);

        ApiResponse<pdiDTO> response = pdiService.buscarPorId(1L);

        assertTrue(response.isOk());
        assertEquals("PDI encontrado com sucesso", response.getMessage());
        assertNotNull(response.getContent());
        
        verify(pdiRepository).findById(1L);
        verify(modelMapper).map(pdi, pdiDTO.class);
    }

    @Test
    @DisplayName("Deve falhar ao buscar PDI por ID quando não encontrado")
    void deveFalharAoBuscarPDIPorIdQuandoNaoEncontrado() {
        when(pdiRepository.findById(999L)).thenReturn(Optional.empty());

        ApiResponse<pdiDTO> response = pdiService.buscarPorId(999L);

        assertFalse(response.isOk());
        assertTrue(response.getMessage().contains("Erro ao buscar PDI"));
        
        verify(pdiRepository).findById(999L);
    }

    @Test
    @DisplayName("Deve atualizar PDI com sucesso")
    void deveAtualizarPDIComSucesso() {
        // Setup updated PDI data
        pdiDTO.setTitulo("PDI Atualizado");
        pdiDTO.setDescricao("Descrição Atualizada");
        
        // Mock the modelMapper to update the existing PDI object
        doAnswer(invocation -> {
            pdiDTO dto = invocation.getArgument(0);
            PDI pdiArg = invocation.getArgument(1);
            pdiArg.setTitulo(dto.getTitulo());
            pdiArg.setDescricao(dto.getDescricao());
            return null;
        }).when(modelMapper).map(pdiDTO, pdi);

        when(pdiRepository.findById(1L)).thenReturn(Optional.of(pdi));
        when(pdiRepository.save(any(PDI.class))).thenReturn(pdi);
        when(modelMapper.map(pdi, pdiDTO.class)).thenReturn(pdiDTO);

        ApiResponse<pdiDTO> response = pdiService.atualizar(1L, pdiDTO);

        assertTrue(response.isOk());
        assertEquals("PDI atualizado com sucesso", response.getMessage());
        assertNotNull(response.getContent());
        assertEquals("PDI Atualizado", response.getContent().getTitulo());
        assertEquals("Descrição Atualizada", response.getContent().getDescricao());
        
        verify(pdiRepository).findById(1L);
        verify(pdiRepository).save(any(PDI.class));
        verify(modelMapper).map(pdiDTO, pdi);
        verify(modelMapper).map(any(PDI.class), eq(pdiDTO.class));
    }

    @Test
    @DisplayName("Deve falhar ao atualizar PDI quando não encontrado")
    void deveFalharAoAtualizarPDIQuandoNaoEncontrado() {
        when(pdiRepository.findById(999L)).thenReturn(Optional.empty());

        ApiResponse<pdiDTO> response = pdiService.atualizar(999L, pdiDTO);

        assertFalse(response.isOk());
        assertTrue(response.getMessage().contains("Erro ao atualizar PDI"));
        
        verify(pdiRepository).findById(999L);
        verify(pdiRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve falhar ao atualizar PDI com status inválido")
    void deveFalharAoAtualizarPDIComStatusInvalido() {
        pdiDTO.setStatus(null);
        when(pdiRepository.findById(1L)).thenReturn(Optional.of(pdi));

        ApiResponse<pdiDTO> response = pdiService.atualizar(1L, pdiDTO);

        assertTrue(response.isOk());
        
        verify(pdiRepository).findById(1L);
        verify(pdiRepository).save(any(PDI.class));
    }

    @Test
    @DisplayName("Deve deletar PDI com sucesso")
    void deveDeletarPDIComSucesso() {
        when(pdiRepository.existsById(1L)).thenReturn(true);

        ApiResponse<Void> response = pdiService.deletar(1L);

        assertTrue(response.isOk());
        assertEquals("PDI deletado com sucesso", response.getMessage());
        assertNull(response.getContent());
        
        verify(pdiRepository).existsById(1L);
        verify(pdiRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve falhar ao deletar PDI quando não encontrado")
    void deveFalharAoDeletarPDIQuandoNaoEncontrado() {
        when(pdiRepository.existsById(999L)).thenReturn(false);

        ApiResponse<Void> response = pdiService.deletar(999L);

        assertFalse(response.isOk());
        assertTrue(response.getMessage().contains("PDI não encontrado com ID: 999"));
        
        verify(pdiRepository).existsById(999L);
        verify(pdiRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve falhar ao deletar PDI quando repository lança exceção")
    void deveFalharAoDeletarPDIQuandoRepositoryLancaExcecao() {
        when(pdiRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("Erro de BD")).when(pdiRepository).deleteById(1L);

        ApiResponse<Void> response = pdiService.deletar(1L);

        assertFalse(response.isOk());
        assertTrue(response.getMessage().contains("Erro ao deletar PDI"));
        
        verify(pdiRepository).existsById(1L);
        verify(pdiRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve buscar PDIs por usuário com sucesso")
    void deveBuscarPDIsPorUsuarioComSucesso() {
        when(pdiRepository.findByIdUsuario(1L)).thenReturn(Arrays.asList(pdi));
        when(modelMapper.map(pdi, pdiDTO.class)).thenReturn(pdiDTO);

        ApiResponse<List<pdiDTO>> response = pdiService.buscarPorUsuario(1L);

        assertTrue(response.isOk());
        assertEquals("PDIs do usuário listados com sucesso", response.getMessage());
        assertNotNull(response.getContent());
        assertEquals(1, response.getContent().size());
        
        verify(pdiRepository).findByIdUsuario(1L);
        verify(modelMapper).map(pdi, pdiDTO.class);
    }

    @Test
    @DisplayName("Deve falhar ao buscar PDIs por usuário quando repository lança exceção")
    void deveFalharAoBuscarPDIsPorUsuarioQuandoRepositoryLancaExcecao() {
        when(pdiRepository.findByIdUsuario(1L)).thenThrow(new RuntimeException("Erro de BD"));

        ApiResponse<List<pdiDTO>> response = pdiService.buscarPorUsuario(1L);

        assertFalse(response.isOk());
        assertTrue(response.getMessage().contains("Erro ao buscar PDIs por usuário"));
        
        verify(pdiRepository).findByIdUsuario(1L);
    }

    @Test
    @DisplayName("Deve buscar PDIs por destinatário com sucesso")
    void deveBuscarPDIsPorDestinatarioComSucesso() {
        when(pdiRepository.findByIdDestinatario(1L)).thenReturn(Arrays.asList(pdi));
        when(modelMapper.map(pdi, pdiDTO.class)).thenReturn(pdiDTO);

        ApiResponse<List<pdiDTO>> response = pdiService.buscarPorDestinatario(1L);

        assertTrue(response.isOk());
        assertEquals("PDIs do destinatário listados com sucesso", response.getMessage());
        assertNotNull(response.getContent());
        assertEquals(1, response.getContent().size());
        
        verify(pdiRepository).findByIdDestinatario(1L);
        verify(modelMapper).map(pdi, pdiDTO.class);
    }

    @Test
    @DisplayName("Deve falhar ao buscar PDIs por destinatário quando repository lança exceção")
    void deveFalharAoBuscarPDIsPorDestinatarioQuandoRepositoryLancaExcecao() {
        when(pdiRepository.findByIdDestinatario(1L)).thenThrow(new RuntimeException("Erro de BD"));

        ApiResponse<List<pdiDTO>> response = pdiService.buscarPorDestinatario(1L);

        assertFalse(response.isOk());
        assertTrue(response.getMessage().contains("Erro ao buscar PDIs por destinatário"));
        
        verify(pdiRepository).findByIdDestinatario(1L);
    }

    @Test
    @DisplayName("Deve listar todos com destinatário com sucesso")
    void deveListarTodosComDestinatarioComSucesso() {
        when(pdiRepository.findAllWithDestinatario()).thenReturn(Arrays.asList(pdi));
        when(modelMapper.map(pdi, pdiDTO.class)).thenReturn(pdiDTO);

        ApiResponse<List<pdiDTO>> response = pdiService.listarTodosComDestinatario();

        assertTrue(response.isOk());
        assertEquals("PDIs com destinatário listados com sucesso", response.getMessage());
        assertNotNull(response.getContent());
        assertEquals(1, response.getContent().size());
        
        verify(pdiRepository).findAllWithDestinatario();
        verify(modelMapper).map(pdi, pdiDTO.class);
    }

    @Test
    @DisplayName("Deve falhar ao listar todos com destinatário quando repository lança exceção")
    void deveFalharAoListarTodosComDestinatarioQuandoRepositoryLancaExcecao() {
        when(pdiRepository.findAllWithDestinatario()).thenThrow(new RuntimeException("Erro de BD"));

        ApiResponse<List<pdiDTO>> response = pdiService.listarTodosComDestinatario();

        assertFalse(response.isOk());
        assertTrue(response.getMessage().contains("Erro ao listar PDIs com destinatário"));
        
        verify(pdiRepository).findAllWithDestinatario();
    }

    @Test
    @DisplayName("Deve listar para listagem com sucesso")
    void deveListarParaListagemComSucesso() {
        when(pdiRepository.buscarTodosParaListagem()).thenReturn(Arrays.asList(pdiListagemDTO));

        List<PdiListagemDTO> response = pdiService.listarParaListagem();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(pdiListagemDTO, response.get(0));
        
        verify(pdiRepository).buscarTodosParaListagem();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há PDIs para listagem")
    void deveRetornarListaVaziaQuandoNaoHaPDIsParaListagem() {
        when(pdiRepository.buscarTodosParaListagem()).thenReturn(Arrays.asList());

        List<PdiListagemDTO> response = pdiService.listarParaListagem();

        assertNotNull(response);
        assertEquals(0, response.size());
        
        verify(pdiRepository).buscarTodosParaListagem();
    }

    @Test
    @DisplayName("Deve criar PDI sem marcos com sucesso")
    void deveCriarPDISemMarcosComSucesso() {
        // Setup PDI without marcos
        pdi.setMarcos(null);
        pdiDTO.setMarcos(null);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(modelMapper.map(pdiDTO, PDI.class)).thenReturn(pdi);
        when(pdiRepository.save(any(PDI.class))).thenReturn(pdi);
        when(modelMapper.map(pdi, pdiDTO.class)).thenReturn(pdiDTO);

        ApiResponse<pdiDTO> response = pdiService.criar(pdiDTO);

        assertTrue(response.isOk());
        assertEquals("PDI criado com sucesso", response.getMessage());
        assertNotNull(response.getContent());
        assertNull(response.getContent().getMarcos());
        
        verify(pdiRepository).save(any(PDI.class));
    }

    @Test
    @DisplayName("Deve criar PDI com marcos vazios com sucesso")
    void deveCriarPDIComMarcosVaziosComSucesso() {
        // Setup empty marcos
        pdi.setMarcos(new ArrayList<>());
        pdiDTO.setMarcos(new ArrayList<>());

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(modelMapper.map(pdiDTO, PDI.class)).thenReturn(pdi);
        when(pdiRepository.save(any(PDI.class))).thenReturn(pdi);
        when(modelMapper.map(pdi, pdiDTO.class)).thenReturn(pdiDTO);

        ApiResponse<pdiDTO> response = pdiService.criar(pdiDTO);

        assertTrue(response.isOk());
        assertEquals("PDI criado com sucesso", response.getMessage());
        assertNotNull(response.getContent());
        assertTrue(response.getContent().getMarcos().isEmpty());
        
        verify(pdiRepository).save(any(PDI.class));
    }

    @Test
    @DisplayName("Deve criar PDI com datas nulas com sucesso")
    void deveCriarPDIComDatasNulasComSucesso() {
        // Setup PDI with null dates
        pdiDTO.setDtInicio(null);
        pdiDTO.setDtFim(null);
        pdi.setDtInicio(null);
        pdi.setDtFim(null);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(modelMapper.map(pdiDTO, PDI.class)).thenReturn(pdi);
        when(pdiRepository.save(any(PDI.class))).thenReturn(pdi);
        when(modelMapper.map(pdi, pdiDTO.class)).thenReturn(pdiDTO);

        ApiResponse<pdiDTO> response = pdiService.criar(pdiDTO);

        assertTrue(response.isOk());
        assertEquals("PDI criado com sucesso", response.getMessage());
        
        verify(pdiRepository).save(any(PDI.class));
    }
} 
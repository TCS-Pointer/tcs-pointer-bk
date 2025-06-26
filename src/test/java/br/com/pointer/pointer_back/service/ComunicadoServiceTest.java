package br.com.pointer.pointer_back.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.ComunicadoDTO;
import br.com.pointer.pointer_back.dto.ComunicadoResponseDTO;
import br.com.pointer.pointer_back.exception.ComunicadoNaoEncontradoException;
import br.com.pointer.pointer_back.model.Comunicado;
import br.com.pointer.pointer_back.model.Usuario;
import br.com.pointer.pointer_back.repository.ComunicadoLeituraRepository;
import br.com.pointer.pointer_back.repository.ComunicadoRepository;
import br.com.pointer.pointer_back.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class ComunicadoServiceTest {

    @Mock
    private ComunicadoRepository comunicadoRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ComunicadoLeituraRepository comunicadoLeituraRepository;

    @InjectMocks
    private ComunicadoService comunicadoService;

    private Comunicado comunicado;
    private ComunicadoDTO comunicadoDTO;
    private ComunicadoResponseDTO comunicadoResponseDTO;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        comunicado = new Comunicado();
        comunicado.setId(1L);
        comunicado.setTitulo("Comunicado Teste");
        comunicado.setDescricao("Descrição do comunicado");
        comunicado.setDataPublicacao(LocalDateTime.now());
        comunicado.setAtivo(true);

        comunicadoDTO = new ComunicadoDTO();
        comunicadoDTO.setId(1L);
        comunicadoDTO.setTitulo("Comunicado Teste");
        comunicadoDTO.setDescricao("Descrição do comunicado");
        comunicadoDTO.setDataPublicacao(LocalDateTime.now());
        comunicadoDTO.setAtivo(true);

        comunicadoResponseDTO = new ComunicadoResponseDTO();
        comunicadoResponseDTO.setId(1L);
        comunicadoResponseDTO.setTitulo("Comunicado Teste");
        comunicadoResponseDTO.setDescricao("Descrição do comunicado");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@teste.com");
        usuario.setTipoUsuario("ADMIN");
        usuario.setSetor("TI");
    }

    @Test
    void listarTodos_Admin_DeveRetornarTodosComunicados() {
        // Arrange
        String keycloakId = "admin-keycloak-id";
        int page = 0;
        int size = 10;
        String titulo = "teste";
        
        Page<Comunicado> pageComunicados = new PageImpl<>(Arrays.asList(comunicado));
        Page<ComunicadoResponseDTO> pageResponse = new PageImpl<>(Arrays.asList(comunicadoResponseDTO));

        when(usuarioRepository.findByKeycloakId(eq(keycloakId))).thenReturn(Optional.of(usuario));
        lenient().when(comunicadoRepository.findByFilters(any(), any(), any(), any(Pageable.class))).thenReturn(pageComunicados);
        when(modelMapper.map(any(Comunicado.class), eq(ComunicadoResponseDTO.class))).thenReturn(comunicadoResponseDTO);
        when(comunicadoLeituraRepository.countByComunicadoId(eq(comunicado.getId()))).thenReturn(5);
        when(comunicadoLeituraRepository.existsByComunicadoIdAndUsuarioId(eq(comunicado.getId()), eq(usuario.getId()))).thenReturn(true);

        // Act
        ApiResponse<Page<ComunicadoResponseDTO>> response = comunicadoService.listarTodos(keycloakId, page, size, titulo);

        // Assert
        assertNotNull(response);
        assertTrue(response.isOk());
        assertEquals("Comunicados listados com sucesso", response.getMessage());
        assertEquals(1, response.getContent().getContent().size());
        
        verify(usuarioRepository).findByKeycloakId(eq(keycloakId));
        verify(comunicadoRepository, atLeastOnce()).findByFilters(any(), any(), any(), any(Pageable.class));
    }

    @Test
    void listarTodos_Gestor_DeveRetornarComunicadosDoSetor() {
        // Arrange
        String keycloakId = "gestor-keycloak-id";
        int page = 0;
        int size = 10;
        String titulo = "teste";
        
        Page<Comunicado> pageComunicados = new PageImpl<>(Arrays.asList(comunicado));

        when(usuarioRepository.findByKeycloakId(eq(keycloakId))).thenReturn(Optional.of(usuario));
        lenient().when(comunicadoRepository.findByFilters(any(), any(), any(), any(Pageable.class))).thenReturn(pageComunicados);
        when(modelMapper.map(any(Comunicado.class), eq(ComunicadoResponseDTO.class))).thenReturn(comunicadoResponseDTO);
        when(comunicadoLeituraRepository.countByComunicadoId(eq(comunicado.getId()))).thenReturn(5);
        when(comunicadoLeituraRepository.existsByComunicadoIdAndUsuarioId(eq(comunicado.getId()), eq(usuario.getId()))).thenReturn(true);

        // Act
        ApiResponse<Page<ComunicadoResponseDTO>> response = comunicadoService.listarTodos(keycloakId, page, size, titulo);

        // Assert
        assertNotNull(response);
        assertTrue(response.isOk());
        verify(comunicadoRepository, atLeastOnce()).findByFilters(any(), any(), any(), any(Pageable.class));
    }

    @Test
    void listarTodos_Colaborador_DeveRetornarComunicadosNaoRestritos() {
        // Arrange
        String keycloakId = "colaborador-keycloak-id";
        int page = 0;
        int size = 10;
        String titulo = "teste";
        
        Page<Comunicado> pageComunicados = new PageImpl<>(Arrays.asList(comunicado));

        when(usuarioRepository.findByKeycloakId(eq(keycloakId))).thenReturn(Optional.of(usuario));
        lenient().when(comunicadoRepository.findByFilters(any(), any(), any(), any(Pageable.class))).thenReturn(pageComunicados);
        when(modelMapper.map(any(Comunicado.class), eq(ComunicadoResponseDTO.class))).thenReturn(comunicadoResponseDTO);
        when(comunicadoLeituraRepository.countByComunicadoId(eq(comunicado.getId()))).thenReturn(5);
        when(comunicadoLeituraRepository.existsByComunicadoIdAndUsuarioId(eq(comunicado.getId()), eq(usuario.getId()))).thenReturn(false);

        // Act
        ApiResponse<Page<ComunicadoResponseDTO>> response = comunicadoService.listarTodos(keycloakId, page, size, titulo);

        // Assert
        assertNotNull(response);
        assertTrue(response.isOk());
        verify(comunicadoRepository, atLeastOnce()).findByFilters(any(), any(), any(), any(Pageable.class));
    }

    @Test
    void listarTodos_UsuarioNaoEncontrado_DeveRetornarErro() {
        // Arrange
        String keycloakId = "usuario-inexistente";
        when(usuarioRepository.findByKeycloakId(eq(keycloakId))).thenReturn(Optional.empty());

        // Act
        ApiResponse<Page<ComunicadoResponseDTO>> response = comunicadoService.listarTodos(keycloakId, 0, 10, "teste");

        // Assert
        assertNotNull(response);
        assertFalse(response.isOk());
        assertTrue(response.getMessage().contains("Erro ao listar comunicados"));
    }

    @Test
    void criar_ComunicadoValido_DeveCriarComSucesso() {
        // Arrange
        when(modelMapper.map(comunicadoDTO, Comunicado.class)).thenReturn(comunicado);
        when(comunicadoRepository.save(comunicado)).thenReturn(comunicado);
        when(modelMapper.map(comunicado, ComunicadoDTO.class)).thenReturn(comunicadoDTO);

        // Act
        ApiResponse<ComunicadoDTO> response = comunicadoService.criar(comunicadoDTO);

        // Assert
        assertNotNull(response);
        assertTrue(response.isOk());
        assertEquals("Comunicado criado com sucesso", response.getMessage());
        assertNotNull(response.getContent());
        assertEquals(comunicadoDTO.getTitulo(), response.getContent().getTitulo());
        
        verify(comunicadoRepository).save(comunicado);
    }

    @Test
    void criar_ErroAoSalvar_DeveRetornarErro() {
        // Arrange
        when(modelMapper.map(comunicadoDTO, Comunicado.class)).thenReturn(comunicado);
        when(comunicadoRepository.save(comunicado)).thenThrow(new RuntimeException("Erro no banco"));

        // Act
        ApiResponse<ComunicadoDTO> response = comunicadoService.criar(comunicadoDTO);

        // Assert
        assertNotNull(response);
        assertFalse(response.isOk());
        assertTrue(response.getMessage().contains("Erro ao criar comunicado"));
    }

    @Test
    void deletar_ComunicadoExistente_DeveDeletarComSucesso() {
        // Arrange
        Long comunicadoId = 1L;
        when(comunicadoRepository.findById(comunicadoId)).thenReturn(Optional.of(comunicado));
        when(comunicadoRepository.save(comunicado)).thenReturn(comunicado);

        // Act
        ApiResponse<Void> response = comunicadoService.deletar(comunicadoId);

        // Assert
        assertNotNull(response);
        assertTrue(response.isOk());
        assertEquals("Comunicado deletado com sucesso", response.getMessage());
        assertFalse(comunicado.isAtivo());
        
        verify(comunicadoRepository).findById(comunicadoId);
        verify(comunicadoRepository).save(comunicado);
    }

    @Test
    void deletar_ComunicadoNaoEncontrado_DeveRetornarErro() {
        // Arrange
        Long comunicadoId = 999L;
        when(comunicadoRepository.findById(comunicadoId)).thenReturn(Optional.empty());

        // Act
        ApiResponse<Void> response = comunicadoService.deletar(comunicadoId);

        // Assert
        assertNotNull(response);
        assertFalse(response.isOk());
        assertTrue(response.getMessage().contains("Erro ao deletar comunicado"));
    }

    @Test
    void deletar_ErroAoSalvar_DeveRetornarErro() {
        // Arrange
        Long comunicadoId = 1L;
        when(comunicadoRepository.findById(comunicadoId)).thenReturn(Optional.of(comunicado));
        when(comunicadoRepository.save(comunicado)).thenThrow(new RuntimeException("Erro no banco"));

        // Act
        ApiResponse<Void> response = comunicadoService.deletar(comunicadoId);

        // Assert
        assertNotNull(response);
        assertFalse(response.isOk());
        assertTrue(response.getMessage().contains("Erro ao deletar comunicado"));
    }

    @Test
    void criar_ComDadosValidos_DeveRetornarComunicadoCriado() {
        // Arrange
        when(modelMapper.map(any(ComunicadoDTO.class), eq(Comunicado.class))).thenReturn(comunicado);
        when(comunicadoRepository.save(any(Comunicado.class))).thenReturn(comunicado);
        when(modelMapper.map(any(Comunicado.class), eq(ComunicadoDTO.class))).thenReturn(comunicadoDTO);

        // Act
        ApiResponse<ComunicadoDTO> response = comunicadoService.criar(comunicadoDTO);

        // Assert
        assertTrue(response.isOk());
        assertEquals("Comunicado criado com sucesso", response.getMessage());
        assertNotNull(response.getContent());
        assertEquals(comunicadoDTO.getTitulo(), response.getContent().getTitulo());
        verify(comunicadoRepository).save(any(Comunicado.class));
    }

    @Test
    void listarTodos_ComSucesso_DeveRetornarListaPaginada() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Comunicado> page = new PageImpl<>(Arrays.asList(comunicado), pageRequest, 1);
        when(usuarioRepository.findByKeycloakId(anyString())).thenReturn(Optional.of(usuario));
        lenient().when(comunicadoRepository.findByFilters(any(), any(), any(), any(PageRequest.class))).thenReturn(page);
        when(modelMapper.map(any(Comunicado.class), eq(ComunicadoResponseDTO.class))).thenReturn(comunicadoResponseDTO);
        when(comunicadoLeituraRepository.countByComunicadoId(anyLong())).thenReturn(5);
        when(comunicadoLeituraRepository.existsByComunicadoIdAndUsuarioId(anyLong(), anyLong())).thenReturn(true);

        // Act
        ApiResponse<Page<ComunicadoResponseDTO>> response = comunicadoService.listarTodos("user-123", 0, 10, "teste");

        // Assert
        assertTrue(response.isOk());
        assertEquals("Comunicados listados com sucesso", response.getMessage());
        assertNotNull(response.getContent());
        assertEquals(1, response.getContent().getTotalElements());
        assertEquals(1, response.getContent().getContent().size());
        verify(usuarioRepository).findByKeycloakId("user-123");
        verify(comunicadoRepository, atLeastOnce()).findByFilters(any(), any(), any(), any(PageRequest.class));
    }

    @Test
    void listarTodos_ComListaVazia_DeveRetornarListaVazia() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Comunicado> page = new PageImpl<>(Arrays.asList(), pageRequest, 0);
        when(usuarioRepository.findByKeycloakId(anyString())).thenReturn(Optional.of(usuario));
        lenient().when(comunicadoRepository.findByFilters(any(), any(), any(), any(PageRequest.class))).thenReturn(page);

        // Act
        ApiResponse<Page<ComunicadoResponseDTO>> response = comunicadoService.listarTodos("user-123", 0, 10, null);

        // Assert
        assertTrue(response.isOk());
        assertEquals("Comunicados listados com sucesso", response.getMessage());
        assertNotNull(response.getContent());
        assertEquals(0, response.getContent().getTotalElements());
        verify(usuarioRepository).findByKeycloakId("user-123");
        verify(comunicadoRepository, atLeastOnce()).findByFilters(any(), any(), any(), any(PageRequest.class));
    }

    @Test
    void criar_ComDadosInvalidos_DeveRetornarErro() {
        // Arrange
        comunicadoDTO.setTitulo(null);
        when(modelMapper.map(any(ComunicadoDTO.class), eq(Comunicado.class))).thenThrow(new RuntimeException("Erro de mapeamento"));

        // Act
        ApiResponse<ComunicadoDTO> response = comunicadoService.criar(comunicadoDTO);

        // Assert
        assertFalse(response.isOk());
        assertTrue(response.getMessage().contains("Erro ao criar comunicado"));
        verify(comunicadoRepository, never()).save(any(Comunicado.class));
    }
} 
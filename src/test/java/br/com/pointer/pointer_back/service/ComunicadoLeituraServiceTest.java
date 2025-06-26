package br.com.pointer.pointer_back.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.ConfirmarLeituraDTO;
import br.com.pointer.pointer_back.dto.UsuarioComunicadoDTO;
import br.com.pointer.pointer_back.model.Comunicado;
import br.com.pointer.pointer_back.model.ComunicadoLeitura;
import br.com.pointer.pointer_back.model.Usuario;
import br.com.pointer.pointer_back.repository.ComunicadoLeituraRepository;
import br.com.pointer.pointer_back.repository.ComunicadoRepository;
import br.com.pointer.pointer_back.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class ComunicadoLeituraServiceTest {

    @Mock
    private ComunicadoLeituraRepository leituraRepository;

    @Mock
    private ComunicadoRepository comunicadoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ComunicadoLeituraService leituraService;

    private Comunicado comunicado;
    private Usuario usuario;
    private ComunicadoLeitura leitura;
    private ConfirmarLeituraDTO confirmarLeituraDTO;
    private UsuarioComunicadoDTO usuarioComunicadoDTO;

    @BeforeEach
    void setUp() {
        // Setup Comunicado
        comunicado = new Comunicado();
        comunicado.setId(1L);
        comunicado.setTitulo("Teste Comunicado");
        comunicado.setDescricao("Descrição do comunicado");

        // Setup Usuario
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Teste Usuario");
        usuario.setEmail("teste@test.com");
        usuario.setKeycloakId("teste-keycloak-id");

        // Setup ComunicadoLeitura
        leitura = new ComunicadoLeitura();
        leitura.setId(1L);
        leitura.setComunicadoId(1L);
        leitura.setUsuarioId(1L);
        leitura.setDtLeitura(LocalDateTime.now());

        // Setup DTOs
        confirmarLeituraDTO = new ConfirmarLeituraDTO();
        confirmarLeituraDTO.setComunicadoId(1L);
        confirmarLeituraDTO.setKeycloakId("teste-keycloak-id");

        usuarioComunicadoDTO = new UsuarioComunicadoDTO();
        usuarioComunicadoDTO.setId(1L);
        usuarioComunicadoDTO.setUsuarioId(1L);
        usuarioComunicadoDTO.setDtLeitura(LocalDateTime.now());
    }

    @Test
    void confirmarLeitura_ComunicadoEUsuarioExistentes_DeveConfirmarComSucesso() {
        // Arrange
        when(comunicadoRepository.findById(confirmarLeituraDTO.getComunicadoId()))
                .thenReturn(Optional.of(comunicado));
        when(usuarioRepository.findByKeycloakId(confirmarLeituraDTO.getKeycloakId()))
                .thenReturn(Optional.of(usuario));
        when(leituraRepository.findByComunicadoIdAndUsuarioId(comunicado.getId(), usuario.getId()))
                .thenReturn(Optional.empty());
        when(leituraRepository.save(any(ComunicadoLeitura.class))).thenReturn(leitura);

        // Act
        ApiResponse<Void> response = leituraService.confirmarLeitura(confirmarLeituraDTO);

        // Assert
        assertNotNull(response);
        assertTrue(response.isOk());
        assertEquals("Leitura confirmada com sucesso", response.getMessage());
        
        verify(comunicadoRepository).findById(confirmarLeituraDTO.getComunicadoId());
        verify(usuarioRepository).findByKeycloakId(confirmarLeituraDTO.getKeycloakId());
        verify(leituraRepository).findByComunicadoIdAndUsuarioId(comunicado.getId(), usuario.getId());
        verify(leituraRepository).save(any(ComunicadoLeitura.class));
    }

    @Test
    void confirmarLeitura_LeituraJaExiste_DeveRetornarSucessoSemCriarNova() {
        // Arrange
        when(comunicadoRepository.findById(confirmarLeituraDTO.getComunicadoId()))
                .thenReturn(Optional.of(comunicado));
        when(usuarioRepository.findByKeycloakId(confirmarLeituraDTO.getKeycloakId()))
                .thenReturn(Optional.of(usuario));
        when(leituraRepository.findByComunicadoIdAndUsuarioId(comunicado.getId(), usuario.getId()))
                .thenReturn(Optional.of(leitura));

        // Act
        ApiResponse<Void> response = leituraService.confirmarLeitura(confirmarLeituraDTO);

        // Assert
        assertNotNull(response);
        assertTrue(response.isOk());
        assertEquals("Leitura confirmada com sucesso", response.getMessage());
        
        verify(comunicadoRepository).findById(confirmarLeituraDTO.getComunicadoId());
        verify(usuarioRepository).findByKeycloakId(confirmarLeituraDTO.getKeycloakId());
        verify(leituraRepository).findByComunicadoIdAndUsuarioId(comunicado.getId(), usuario.getId());
        verify(leituraRepository, never()).save(any(ComunicadoLeitura.class));
    }

    @Test
    void confirmarLeitura_ComunicadoNaoEncontrado_DeveLancarExcecao() {
        // Arrange
        when(comunicadoRepository.findById(confirmarLeituraDTO.getComunicadoId()))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            leituraService.confirmarLeitura(confirmarLeituraDTO);
        });
        
        assertEquals("Comunicado não encontrado", exception.getMessage());
        verify(comunicadoRepository).findById(confirmarLeituraDTO.getComunicadoId());
        verify(usuarioRepository, never()).findByKeycloakId(anyString());
    }

    @Test
    void confirmarLeitura_UsuarioNaoEncontrado_DeveLancarExcecao() {
        // Arrange
        when(comunicadoRepository.findById(confirmarLeituraDTO.getComunicadoId()))
                .thenReturn(Optional.of(comunicado));
        when(usuarioRepository.findByKeycloakId(confirmarLeituraDTO.getKeycloakId()))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            leituraService.confirmarLeitura(confirmarLeituraDTO);
        });
        
        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(comunicadoRepository).findById(confirmarLeituraDTO.getComunicadoId());
        verify(usuarioRepository).findByKeycloakId(confirmarLeituraDTO.getKeycloakId());
    }

    @Test
    void listarLeitores_ComunicadoExistente_DeveRetornarListaDeLeitores() {
        // Arrange
        Long comunicadoId = 1L;
        List<ComunicadoLeitura> leituras = Arrays.asList(leitura);
        
        when(comunicadoRepository.findById(comunicadoId)).thenReturn(Optional.of(comunicado));
        when(leituraRepository.findByComunicadoId(comunicado.getId())).thenReturn(leituras);

        // Act
        ApiResponse<List<UsuarioComunicadoDTO>> response = leituraService.listarLeitores(comunicadoId);

        // Assert
        assertNotNull(response);
        assertTrue(response.isOk());
        assertEquals("Leitores listados com sucesso", response.getMessage());
        assertEquals(1, response.getContent().size());
        
        UsuarioComunicadoDTO dto = response.getContent().get(0);
        assertEquals(leitura.getId(), dto.getId());
        assertEquals(leitura.getUsuarioId(), dto.getUsuarioId());
        assertEquals(leitura.getDtLeitura(), dto.getDtLeitura());
        
        verify(comunicadoRepository).findById(comunicadoId);
        verify(leituraRepository).findByComunicadoId(comunicado.getId());
    }

    @Test
    void listarLeitores_ComunicadoNaoEncontrado_DeveLancarExcecao() {
        // Arrange
        Long comunicadoId = 999L;
        when(comunicadoRepository.findById(comunicadoId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            leituraService.listarLeitores(comunicadoId);
        });
        
        assertEquals("Comunicado não encontrado", exception.getMessage());
        verify(comunicadoRepository).findById(comunicadoId);
        verify(leituraRepository, never()).findByComunicadoId(anyLong());
    }

    @Test
    void listarLeitores_ListaVazia_DeveRetornarListaVazia() {
        // Arrange
        Long comunicadoId = 1L;
        List<ComunicadoLeitura> leituras = Arrays.asList();
        
        when(comunicadoRepository.findById(comunicadoId)).thenReturn(Optional.of(comunicado));
        when(leituraRepository.findByComunicadoId(comunicado.getId())).thenReturn(leituras);

        // Act
        ApiResponse<List<UsuarioComunicadoDTO>> response = leituraService.listarLeitores(comunicadoId);

        // Assert
        assertNotNull(response);
        assertTrue(response.isOk());
        assertEquals("Leitores listados com sucesso", response.getMessage());
        assertTrue(response.getContent().isEmpty());
        
        verify(comunicadoRepository).findById(comunicadoId);
        verify(leituraRepository).findByComunicadoId(comunicado.getId());
    }

    @Test
    void listarLeitores_MultiplosLeitores_DeveRetornarTodos() {
        // Arrange
        Long comunicadoId = 1L;
        ComunicadoLeitura leitura2 = new ComunicadoLeitura();
        leitura2.setId(2L);
        leitura2.setComunicadoId(1L);
        leitura2.setUsuarioId(2L);
        leitura2.setDtLeitura(LocalDateTime.now());
        
        List<ComunicadoLeitura> leituras = Arrays.asList(leitura, leitura2);
        
        when(comunicadoRepository.findById(comunicadoId)).thenReturn(Optional.of(comunicado));
        when(leituraRepository.findByComunicadoId(comunicado.getId())).thenReturn(leituras);

        // Act
        ApiResponse<List<UsuarioComunicadoDTO>> response = leituraService.listarLeitores(comunicadoId);

        // Assert
        assertNotNull(response);
        assertTrue(response.isOk());
        assertEquals("Leitores listados com sucesso", response.getMessage());
        assertEquals(2, response.getContent().size());
        
        verify(comunicadoRepository).findById(comunicadoId);
        verify(leituraRepository).findByComunicadoId(comunicado.getId());
    }

    @Test
    void confirmarLeitura_ComunicadoIdNulo_DeveLancarExcecao() {
        // Arrange
        confirmarLeituraDTO.setComunicadoId(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            leituraService.confirmarLeitura(confirmarLeituraDTO);
        });
        
        verify(comunicadoRepository).findById(null);
    }

    @Test
    void confirmarLeitura_KeycloakIdNulo_DeveLancarExcecao() {
        // Arrange
        confirmarLeituraDTO.setKeycloakId(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            leituraService.confirmarLeitura(confirmarLeituraDTO);
        });
        
        verify(comunicadoRepository).findById(1L);
        verify(usuarioRepository, never()).findByKeycloakId(any());
    }
} 
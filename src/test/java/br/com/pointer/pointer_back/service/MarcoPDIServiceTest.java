package br.com.pointer.pointer_back.service;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.MarcoPDIDTO;
import br.com.pointer.pointer_back.enums.StatusMarcoPDI;
import br.com.pointer.pointer_back.exception.MarcoPDINaoEncontradoException;
import br.com.pointer.pointer_back.model.MarcoPDI;
import br.com.pointer.pointer_back.model.PDI;
import br.com.pointer.pointer_back.repository.MarcoPDIRepository;
import br.com.pointer.pointer_back.repository.PDIRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class MarcoPDIServiceTest {

    @Mock
    private MarcoPDIRepository marcoPDIRepository;

    @Mock
    private PDIRepository pdiRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MarcoPDIService marcoPDIService;

    private MarcoPDI marcoPDI;
    private MarcoPDIDTO marcoPDIDTO;
    private PDI pdi;

    @BeforeEach
    void setUp() {
        // Setup PDI
        pdi = new PDI();
        pdi.setId(1L);
        pdi.setTitulo("PDI Teste");
        pdi.setDescricao("Descrição do PDI");

        // Setup Marco PDI
        marcoPDI = new MarcoPDI();
        marcoPDI.setId(1L);
        marcoPDI.setTitulo("Marco 1");
        marcoPDI.setDescricao("Descrição do Marco");
        marcoPDI.setStatus(StatusMarcoPDI.PENDENTE);
        marcoPDI.setDtFinal(LocalDate.now().plusMonths(1));
        marcoPDI.setPdi(pdi);

        // Setup Marco PDI DTO
        marcoPDIDTO = new MarcoPDIDTO();
        marcoPDIDTO.setId(1L);
        marcoPDIDTO.setTitulo("Marco 1");
        marcoPDIDTO.setDescricao("Descrição do Marco");
        marcoPDIDTO.setStatus(StatusMarcoPDI.PENDENTE);
        marcoPDIDTO.setDtFinal(LocalDate.now().plusMonths(1));
        marcoPDIDTO.setPdiId(1L);

        lenient().when(modelMapper.map(any(MarcoPDIDTO.class), eq(MarcoPDI.class))).thenReturn(marcoPDI);
        lenient().when(modelMapper.map(any(MarcoPDI.class), eq(MarcoPDIDTO.class))).thenReturn(marcoPDIDTO);
        lenient().when(modelMapper.map(any(), eq(MarcoPDI.class))).thenReturn(marcoPDI);
        lenient().when(modelMapper.map(any(), eq(MarcoPDIDTO.class))).thenReturn(marcoPDIDTO);
    }

    @Test
    void criar_DadosValidos_DeveCriarComSucesso() {
        // Arrange
        when(pdiRepository.findById(1L)).thenReturn(Optional.of(pdi));
        when(modelMapper.map(marcoPDIDTO, MarcoPDI.class)).thenReturn(marcoPDI);
        when(marcoPDIRepository.save(marcoPDI)).thenReturn(marcoPDI);
        when(modelMapper.map(marcoPDI, MarcoPDIDTO.class)).thenReturn(marcoPDIDTO);

        // Act
        ApiResponse<MarcoPDIDTO> response = marcoPDIService.criar(marcoPDIDTO);

        // Assert
        assertNotNull(response);
        assertTrue(response.isOk());
        assertEquals("Marco PDI criado com sucesso", response.getMessage());
        
        verify(pdiRepository).findById(1L);
        verify(modelMapper).map(marcoPDIDTO, MarcoPDI.class);
        verify(marcoPDIRepository).save(marcoPDI);
    }

    @Test
    void criar_PdiIdNulo_DeveRetornarErro() {
        // Arrange
        marcoPDIDTO.setPdiId(null);

        // Act
        ApiResponse<MarcoPDIDTO> response = marcoPDIService.criar(marcoPDIDTO);

        // Assert
        assertNotNull(response);
        assertFalse(response.isOk());
        assertEquals("ID do PDI é obrigatório", response.getMessage());
        
        verify(pdiRepository, never()).findById(anyLong());
        verify(marcoPDIRepository, never()).save(any());
    }

    @Test
    void criar_PdiNaoEncontrado_DeveRetornarErro() {
        // Arrange
        when(pdiRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        marcoPDIDTO.setPdiId(999L);
        ApiResponse<MarcoPDIDTO> response = marcoPDIService.criar(marcoPDIDTO);

        // Assert
        assertNotNull(response);
        assertFalse(response.isOk());
        assertTrue(response.getMessage().contains("Erro ao criar Marco PDI"));
        
        verify(pdiRepository).findById(999L);
        verify(marcoPDIRepository, never()).save(any());
    }

    @Test
    void listarPorPDI_DeveRetornarLista() {
        // Arrange
        List<MarcoPDI> marcos = Arrays.asList(marcoPDI);
        when(marcoPDIRepository.findByPdiId(1L)).thenReturn(marcos);
        when(modelMapper.map(marcoPDI, MarcoPDIDTO.class)).thenReturn(marcoPDIDTO);

        // Act
        ApiResponse<List<MarcoPDIDTO>> response = marcoPDIService.listarPorPDI(1L);

        // Assert
        assertNotNull(response);
        assertTrue(response.isOk());
        assertEquals("Marcos PDI listados com sucesso", response.getMessage());
        assertEquals(1, response.getContent().size());
        
        verify(marcoPDIRepository).findByPdiId(1L);
        verify(modelMapper).map(marcoPDI, MarcoPDIDTO.class);
    }

    @Test
    void listarPorPDI_ListaVazia_DeveRetornarListaVazia() {
        // Arrange
        when(marcoPDIRepository.findByPdiId(1L)).thenReturn(Arrays.asList());

        // Act
        ApiResponse<List<MarcoPDIDTO>> response = marcoPDIService.listarPorPDI(1L);

        // Assert
        assertNotNull(response);
        assertTrue(response.isOk());
        assertEquals("Marcos PDI listados com sucesso", response.getMessage());
        assertEquals(0, response.getContent().size());
        
        verify(marcoPDIRepository).findByPdiId(1L);
    }

    @Test
    void buscarPorId_Existente_DeveRetornarMarco() {
        // Arrange
        when(marcoPDIRepository.findById(1L)).thenReturn(Optional.of(marcoPDI));
        when(modelMapper.map(marcoPDI, MarcoPDIDTO.class)).thenReturn(marcoPDIDTO);

        // Act
        ApiResponse<MarcoPDIDTO> response = marcoPDIService.buscarPorId(1L);

        // Assert
        assertNotNull(response);
        assertTrue(response.isOk());
        assertEquals("Marco PDI encontrado com sucesso", response.getMessage());
        
        verify(marcoPDIRepository).findById(1L);
        verify(modelMapper).map(marcoPDI, MarcoPDIDTO.class);
    }

    @Test
    void buscarPorId_NaoExistente_DeveRetornarErro() {
        // Arrange
        when(marcoPDIRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        ApiResponse<MarcoPDIDTO> response = marcoPDIService.buscarPorId(999L);

        // Assert
        assertNotNull(response);
        assertFalse(response.isOk());
        assertTrue(response.getMessage().contains("Erro ao buscar Marco PDI"));
        
        verify(marcoPDIRepository).findById(999L);
    }

    @Test
    void atualizar_NaoExistente_DeveRetornarErro() {
        // Arrange
        when(marcoPDIRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        ApiResponse<MarcoPDIDTO> response = marcoPDIService.atualizar(999L, marcoPDIDTO);

        // Assert
        assertNotNull(response);
        assertFalse(response.isOk());
        assertTrue(response.getMessage().contains("Erro ao atualizar Marco PDI"));
        
        verify(marcoPDIRepository).findById(999L);
        verify(marcoPDIRepository, never()).save(any());
    }

    @Test
    void deletar_Existente_DeveDeletarComSucesso() {
        // Arrange
        when(marcoPDIRepository.existsById(1L)).thenReturn(true);
        doNothing().when(marcoPDIRepository).deleteById(1L);

        // Act
        ApiResponse<Void> response = marcoPDIService.deletar(1L);

        // Assert
        assertNotNull(response);
        assertTrue(response.isOk());
        assertEquals("Marco PDI deletado com sucesso", response.getMessage());
        
        verify(marcoPDIRepository).existsById(1L);
        verify(marcoPDIRepository).deleteById(1L);
    }

    @Test
    void deletar_NaoExistente_DeveRetornarErro() {
        // Arrange
        when(marcoPDIRepository.existsById(999L)).thenReturn(false);

        // Act
        ApiResponse<Void> response = marcoPDIService.deletar(999L);

        // Assert
        assertNotNull(response);
        assertFalse(response.isOk());
        assertEquals("Marco PDI não encontrado com ID: 999", response.getMessage());
        
        verify(marcoPDIRepository).existsById(999L);
        verify(marcoPDIRepository, never()).deleteById(anyLong());
    }
} 
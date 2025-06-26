package br.com.pointer.pointer_back.service;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.SetorCargoDTO;
import br.com.pointer.pointer_back.exception.SetorCargoInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SetorCargoServiceTest {

    @InjectMocks
    private SetorCargoService setorCargoService;

    private SetorCargoDTO dados;

    @BeforeEach
    void setUp() {
        // Setup dados de teste
        dados = new SetorCargoDTO();
        
        SetorCargoDTO.Setor setor1 = new SetorCargoDTO.Setor();
        setor1.setSetor("TI");
        setor1.setCargos(Arrays.asList("Desenvolvedor", "Analista", "Gerente"));
        
        SetorCargoDTO.Setor setor2 = new SetorCargoDTO.Setor();
        setor2.setSetor("RH");
        setor2.setCargos(Arrays.asList("Analista RH", "Coordenador", "Gerente RH"));
        
        SetorCargoDTO.Setor setor3 = new SetorCargoDTO.Setor();
        setor3.setSetor("Financeiro");
        setor3.setCargos(Arrays.asList("Contador", "Analista Financeiro", "Controller"));
        
        dados.setSetores(Arrays.asList(setor1, setor2, setor3));
        
        // Injetar dados no service
        ReflectionTestUtils.setField(setorCargoService, "dados", dados);
    }

    @Test
    void listarSetoresECargos_DadosValidos_DeveRetornarSucesso() {
        // Act
        ApiResponse<SetorCargoDTO> response = setorCargoService.listarSetoresECargos();

        // Assert
        assertNotNull(response);
        assertTrue(response.isOk());
        assertEquals("Setores e cargos listados com sucesso", response.getMessage());
        assertNotNull(response.getContent());
        assertEquals(3, response.getContent().getSetores().size());
    }

    @Test
    void validarSetorECargo_SetorECargoValidos_DevePassar() {
        // Act & Assert - não deve lançar exceção
        assertDoesNotThrow(() -> {
            setorCargoService.validarSetorECargo("TI", "Desenvolvedor");
        });
    }

    @Test
    void validarSetorECargo_SetorInvalido_DeveLancarExcecao() {
        // Act & Assert
        SetorCargoInvalidoException exception = assertThrows(SetorCargoInvalidoException.class, () -> {
            setorCargoService.validarSetorECargo("SetorInexistente", "Desenvolvedor");
        });
        
        assertEquals("Setor inválido: SetorInexistente", exception.getMessage());
    }

    @Test
    void validarSetorECargo_CargoInvalido_DeveLancarExcecao() {
        // Act & Assert
        SetorCargoInvalidoException exception = assertThrows(SetorCargoInvalidoException.class, () -> {
            setorCargoService.validarSetorECargo("TI", "CargoInexistente");
        });
        
        assertEquals("Cargo inválido: CargoInexistente para o setor: TI", exception.getMessage());
    }

    @Test
    void validarSetorECargo_SetorNulo_DeveLancarExcecao() {
        // Act & Assert
        SetorCargoInvalidoException exception = assertThrows(SetorCargoInvalidoException.class, () -> {
            setorCargoService.validarSetorECargo(null, "Desenvolvedor");
        });
        
        assertEquals("Setor inválido: null", exception.getMessage());
    }

    @Test
    void validarSetorECargo_CargoNulo_DeveLancarExcecao() {
        // Act & Assert
        SetorCargoInvalidoException exception = assertThrows(SetorCargoInvalidoException.class, () -> {
            setorCargoService.validarSetorECargo("TI", null);
        });
        
        assertEquals("Cargo inválido: null para o setor: TI", exception.getMessage());
    }

    @Test
    void isSetorValido_SetorExistente_DeveRetornarTrue() {
        // Act
        boolean resultado = setorCargoService.isSetorValido("TI");

        // Assert
        assertTrue(resultado);
    }

    @Test
    void isSetorValido_SetorInexistente_DeveRetornarFalse() {
        // Act
        boolean resultado = setorCargoService.isSetorValido("SetorInexistente");

        // Assert
        assertFalse(resultado);
    }

    @Test
    void isSetorValido_SetorNulo_DeveRetornarFalse() {
        // Act
        boolean resultado = setorCargoService.isSetorValido(null);

        // Assert
        assertFalse(resultado);
    }

    @Test
    void isSetorValido_SetorVazio_DeveRetornarFalse() {
        // Act
        boolean resultado = setorCargoService.isSetorValido("");

        // Assert
        assertFalse(resultado);
    }

    @Test
    void isCargoValido_CargoExistente_DeveRetornarTrue() {
        // Act
        boolean resultado = setorCargoService.isCargoValido("TI", "Desenvolvedor");

        // Assert
        assertTrue(resultado);
    }

    @Test
    void isCargoValido_CargoInexistente_DeveRetornarFalse() {
        // Act
        boolean resultado = setorCargoService.isCargoValido("TI", "CargoInexistente");

        // Assert
        assertFalse(resultado);
    }

    @Test
    void isCargoValido_SetorInexistente_DeveRetornarFalse() {
        // Act
        boolean resultado = setorCargoService.isCargoValido("SetorInexistente", "Desenvolvedor");

        // Assert
        assertFalse(resultado);
    }

    @Test
    void isCargoValido_SetorNulo_DeveRetornarFalse() {
        // Act
        boolean resultado = setorCargoService.isCargoValido(null, "Desenvolvedor");

        // Assert
        assertFalse(resultado);
    }

    @Test
    void isCargoValido_CargoNulo_DeveRetornarFalse() {
        // Act
        boolean resultado = setorCargoService.isCargoValido("TI", null);

        // Assert
        assertFalse(resultado);
    }

    @Test
    void getSetores_DeveRetornarListaCompleta() {
        // Act
        List<String> setores = setorCargoService.getSetores();

        // Assert
        assertNotNull(setores);
        assertEquals(3, setores.size());
        assertTrue(setores.contains("TI"));
        assertTrue(setores.contains("RH"));
        assertTrue(setores.contains("Financeiro"));
    }

    @Test
    void getCargosPorSetor_SetorExistente_DeveRetornarCargos() {
        // Act
        List<String> cargos = setorCargoService.getCargosPorSetor("TI");

        // Assert
        assertNotNull(cargos);
        assertEquals(3, cargos.size());
        assertTrue(cargos.contains("Desenvolvedor"));
        assertTrue(cargos.contains("Analista"));
        assertTrue(cargos.contains("Gerente"));
    }

    @Test
    void getCargosPorSetor_SetorInexistente_DeveRetornarListaVazia() {
        // Act
        List<String> cargos = setorCargoService.getCargosPorSetor("SetorInexistente");

        // Assert
        assertNotNull(cargos);
        assertTrue(cargos.isEmpty());
    }

    @Test
    void getCargosPorSetor_SetorNulo_DeveRetornarListaVazia() {
        // Act
        List<String> cargos = setorCargoService.getCargosPorSetor(null);

        // Assert
        assertNotNull(cargos);
        assertTrue(cargos.isEmpty());
    }

    @Test
    void getCargosPorSetor_SetorVazio_DeveRetornarListaVazia() {
        // Act
        List<String> cargos = setorCargoService.getCargosPorSetor("");

        // Assert
        assertNotNull(cargos);
        assertTrue(cargos.isEmpty());
    }

    @Test
    void validarSetorECargo_DiferentesSetores_DeveValidarCorretamente() {
        // Act & Assert - RH
        assertDoesNotThrow(() -> {
            setorCargoService.validarSetorECargo("RH", "Analista RH");
        });
        
        // Act & Assert - Financeiro
        assertDoesNotThrow(() -> {
            setorCargoService.validarSetorECargo("Financeiro", "Contador");
        });
    }

    @Test
    void isCargoValido_CargoDeOutroSetor_DeveRetornarFalse() {
        // Act
        boolean resultado = setorCargoService.isCargoValido("TI", "Analista RH");

        // Assert
        assertFalse(resultado);
    }

    @Test
    void isCargoValido_CargoComEspacos_DeveRetornarFalse() {
        // Act
        boolean resultado = setorCargoService.isCargoValido("TI", " Desenvolvedor ");

        // Assert
        assertFalse(resultado);
    }

    @Test
    void isSetorValido_SetorComEspacos_DeveRetornarFalse() {
        // Act
        boolean resultado = setorCargoService.isSetorValido(" TI ");

        // Assert
        assertFalse(resultado);
    }

    @Test
    void listarSetoresECargos_DadosNulos_DeveRetornarSucesso() {
        // Arrange
        ReflectionTestUtils.setField(setorCargoService, "dados", null);

        // Act
        ApiResponse<SetorCargoDTO> response = setorCargoService.listarSetoresECargos();

        // Assert
        assertNotNull(response);
        assertTrue(response.isOk());
        assertTrue(response.getMessage().contains("Setores e cargos listados com sucesso"));
        assertNull(response.getContent());
    }

    @Test
    void getSetores_DadosNulos_DeveRetornarListaVazia() {
        // Arrange
        ReflectionTestUtils.setField(setorCargoService, "dados", null);

        // Act
        List<String> setores = setorCargoService.getSetores();

        // Assert
        assertNotNull(setores);
        assertTrue(setores.isEmpty());
    }

    @Test
    void getCargosPorSetor_DadosNulos_DeveRetornarListaVazia() {
        // Arrange
        ReflectionTestUtils.setField(setorCargoService, "dados", null);

        // Act
        List<String> cargos = setorCargoService.getCargosPorSetor("TI");

        // Assert
        assertNotNull(cargos);
        assertTrue(cargos.isEmpty());
    }
} 
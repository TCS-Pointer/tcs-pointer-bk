package br.com.pointer.pointer_back;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ApiResponseTest {

    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
    }

    @Test
    void testIsOk_ComStatus200_DeveRetornarTrue() {
        // Arrange
        ApiResponse<String> response = ApiResponse.<String>builder()
                .status(200)
                .build();

        // Act & Assert
        assertTrue(response.isOk());
    }

    @Test
    void testIsOk_ComStatusDiferenteDe200_DeveRetornarFalse() {
        // Arrange
        ApiResponse<String> response = ApiResponse.<String>builder()
                .status(400)
                .build();

        // Act & Assert
        assertFalse(response.isOk());
    }

    @Test
    void testSuccess_ComContentEMessage_DeveCriarResponseCorreto() {
        // Arrange
        String content = "teste";
        String message = "Sucesso";

        // Act
        ApiResponse<String> response = ApiResponse.success(content, message);

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals(message, response.getMessage());
        assertEquals(content, response.getContent());
        assertNull(response.getErrors());
    }

    @Test
    void testSuccess_ComMessageApenas_DeveCriarResponseCorreto() {
        // Arrange
        String message = "Sucesso";

        // Act
        ApiResponse<String> response = ApiResponse.success(message);

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNull(response.getContent());
        assertNull(response.getErrors());
    }

    @Test
    void testError_ComMessageEStatus_DeveCriarResponseCorreto() {
        // Arrange
        String message = "Erro";
        int status = 400;

        // Act
        ApiResponse<String> response = ApiResponse.error(message, status);

        // Assert
        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNull(response.getContent());
        assertNull(response.getErrors());
    }

    @Test
    void testError_ComMessageErrorsEStatus_DeveCriarResponseCorreto() {
        // Arrange
        String message = "Erro";
        List<String> errors = Arrays.asList("Erro 1", "Erro 2");
        int status = 400;

        // Act
        ApiResponse<String> response = ApiResponse.error(message, errors, status);

        // Assert
        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
        assertEquals(errors, response.getErrors());
        assertNull(response.getContent());
    }

    @Test
    void testBadRequest_DeveCriarResponseComStatus400() {
        // Arrange
        String message = "Bad Request";

        // Act
        ApiResponse<String> response = ApiResponse.badRequest(message);

        // Assert
        assertEquals(400, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNull(response.getContent());
        assertNull(response.getErrors());
    }

    @Test
    void testNotFound_DeveCriarResponseComStatus404() {
        // Arrange
        String message = "Not Found";

        // Act
        ApiResponse<String> response = ApiResponse.notFound(message);

        // Assert
        assertEquals(404, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNull(response.getContent());
        assertNull(response.getErrors());
    }

    @Test
    void testConflict_DeveCriarResponseComStatus409() {
        // Arrange
        String message = "Conflict";

        // Act
        ApiResponse<String> response = ApiResponse.conflict(message);

        // Assert
        assertEquals(409, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNull(response.getContent());
        assertNull(response.getErrors());
    }

    @Test
    void testUnprocessableEntity_DeveCriarResponseComStatus422() {
        // Arrange
        String message = "Unprocessable Entity";

        // Act
        ApiResponse<String> response = ApiResponse.unprocessableEntity(message);

        // Assert
        assertEquals(422, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNull(response.getContent());
        assertNull(response.getErrors());
    }

    @Test
    void testInternalServerError_DeveCriarResponseComStatus500() {
        // Arrange
        String message = "Internal Server Error";

        // Act
        ApiResponse<String> response = ApiResponse.internalServerError(message);

        // Assert
        assertEquals(500, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNull(response.getContent());
        assertNull(response.getErrors());
    }

    @Test
    void testMap_ComSourceNulo_DeveRetornarNotFound() {
        // Arrange
        String message = "Not found";

        // Act
        ApiResponse<String> response = ApiResponse.map(null, String.class, message);

        // Assert
        assertEquals(404, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNull(response.getContent());
    }

    @Test
    void testMap_ComSourceValido_DeveMapearCorretamente() {
        // Arrange
        TestSource source = new TestSource("teste", 123);
        String message = "Mapped successfully";

        // Act
        ApiResponse<TestTarget> response = ApiResponse.map(source, TestTarget.class, message);

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getContent());
        assertEquals(source.getName(), response.getContent().getName());
        assertEquals(source.getValue(), response.getContent().getValue());
    }

    @Test
    void testMapList_ComListaNula_DeveRetornarListaVazia() {
        // Arrange
        String message = "Empty list";

        // Act
        ApiResponse<List<TestTarget>> response = ApiResponse.mapList(null, TestTarget.class, message);

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getContent());
        assertTrue(response.getContent().isEmpty());
    }

    @Test
    void testMapList_ComListaVazia_DeveRetornarListaVazia() {
        // Arrange
        List<TestSource> sourceList = Arrays.asList();
        String message = "Empty list";

        // Act
        ApiResponse<List<TestTarget>> response = ApiResponse.mapList(sourceList, TestTarget.class, message);

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getContent());
        assertTrue(response.getContent().isEmpty());
    }

    @Test
    void testMapList_ComListaValida_DeveMapearCorretamente() {
        // Arrange
        List<TestSource> sourceList = Arrays.asList(
                new TestSource("teste1", 123),
                new TestSource("teste2", 456)
        );
        String message = "Mapped list successfully";

        // Act
        ApiResponse<List<TestTarget>> response = ApiResponse.mapList(sourceList, TestTarget.class, message);

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getContent());
        assertEquals(2, response.getContent().size());
        assertEquals("teste1", response.getContent().get(0).getName());
        assertEquals(123, response.getContent().get(0).getValue());
        assertEquals("teste2", response.getContent().get(1).getName());
        assertEquals(456, response.getContent().get(1).getValue());
    }

    @Test
    void testMapPage_ComPageNula_DeveRetornarPageVazia() {
        // Arrange
        String message = "Empty page";

        // Act
        ApiResponse<Page<TestTarget>> response = ApiResponse.mapPage(null, TestTarget.class, message);

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getContent());
        assertTrue(response.getContent().isEmpty());
    }

    @Test
    void testMapPage_ComPageVazia_DeveRetornarPageVazia() {
        // Arrange
        Page<TestSource> sourcePage = Page.empty();
        String message = "Empty page";

        // Act
        ApiResponse<Page<TestTarget>> response = ApiResponse.mapPage(sourcePage, TestTarget.class, message);

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getContent());
        assertTrue(response.getContent().isEmpty());
    }

    @Test
    void testMapPage_ComPageValida_DeveMapearCorretamente() {
        // Arrange
        List<TestSource> sourceList = Arrays.asList(
                new TestSource("teste1", 123),
                new TestSource("teste2", 456)
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<TestSource> sourcePage = new PageImpl<>(sourceList, pageable, 2);
        String message = "Mapped page successfully";

        // Act
        ApiResponse<Page<TestTarget>> response = ApiResponse.mapPage(sourcePage, TestTarget.class, message);

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getContent());
        assertEquals(2, response.getContent().getTotalElements());
        assertEquals(2, response.getContent().getContent().size());
        assertEquals("teste1", response.getContent().getContent().get(0).getName());
        assertEquals(123, response.getContent().getContent().get(0).getValue());
        assertEquals("teste2", response.getContent().getContent().get(1).getName());
        assertEquals(456, response.getContent().getContent().get(1).getValue());
    }

    @Test
    void testMap_ComModelMapperCustomizado_ComSourceNulo_DeveRetornarNotFound() {
        // Arrange
        ModelMapper modelMapper = new ModelMapper();
        String message = "Not found";

        // Act
        ApiResponse<String> response = ApiResponse.map(null, String.class, message, modelMapper);

        // Assert
        assertEquals(404, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNull(response.getContent());
    }

    @Test
    void testMap_ComModelMapperCustomizado_ComSourceValido_DeveMapearCorretamente() {
        // Arrange
        TestSource source = new TestSource("teste", 123);
        ModelMapper modelMapper = new ModelMapper();
        String message = "Mapped successfully";

        // Act
        ApiResponse<TestTarget> response = ApiResponse.map(source, TestTarget.class, message, modelMapper);

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getContent());
        assertEquals(source.getName(), response.getContent().getName());
        assertEquals(source.getValue(), response.getContent().getValue());
    }

    @Test
    void testMapList_ComModelMapperCustomizado_ComListaNula_DeveRetornarListaVazia() {
        // Arrange
        ModelMapper modelMapper = new ModelMapper();
        String message = "Empty list";

        // Act
        ApiResponse<List<TestTarget>> response = ApiResponse.mapList(null, TestTarget.class, message, modelMapper);

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getContent());
        assertTrue(response.getContent().isEmpty());
    }

    @Test
    void testMapList_ComModelMapperCustomizado_ComListaValida_DeveMapearCorretamente() {
        // Arrange
        List<TestSource> sourceList = Arrays.asList(
                new TestSource("teste1", 123),
                new TestSource("teste2", 456)
        );
        ModelMapper modelMapper = new ModelMapper();
        String message = "Mapped list successfully";

        // Act
        ApiResponse<List<TestTarget>> response = ApiResponse.mapList(sourceList, TestTarget.class, message, modelMapper);

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getContent());
        assertEquals(2, response.getContent().size());
        assertEquals("teste1", response.getContent().get(0).getName());
        assertEquals(123, response.getContent().get(0).getValue());
        assertEquals("teste2", response.getContent().get(1).getName());
        assertEquals(456, response.getContent().get(1).getValue());
    }

    @Test
    void testMapPage_ComModelMapperCustomizado_ComPageValida_DeveMapearCorretamente() {
        // Arrange
        List<TestSource> sourceList = Arrays.asList(
                new TestSource("teste1", 123),
                new TestSource("teste2", 456)
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<TestSource> sourcePage = new PageImpl<>(sourceList, pageable, 2);
        ModelMapper modelMapper = new ModelMapper();
        String message = "Mapped page successfully";

        // Act
        ApiResponse<Page<TestTarget>> response = ApiResponse.mapPage(sourcePage, TestTarget.class, message, modelMapper);

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getContent());
        assertEquals(2, response.getContent().getTotalElements());
        assertEquals(2, response.getContent().getContent().size());
        assertEquals("teste1", response.getContent().getContent().get(0).getName());
        assertEquals(123, response.getContent().getContent().get(0).getValue());
        assertEquals("teste2", response.getContent().getContent().get(1).getName());
        assertEquals(456, response.getContent().getContent().get(1).getValue());
    }

    @Test
    void testOk_DeveCriarResponseCorreto() {
        // Arrange
        ApiResponse<String> apiResponse = new ApiResponse<>();
        String content = "teste";
        String message = "Sucesso";

        // Act
        ApiResponse<String> response = apiResponse.ok(content, message);

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals(message, response.getMessage());
        assertEquals(content, response.getContent());
        assertNull(response.getErrors());
    }

    @Test
    void testBadRequest_ComErrors_DeveCriarResponseCorreto() {
        // Arrange
        ApiResponse<String> apiResponse = new ApiResponse<>();
        String message = "Bad Request";
        List<String> errors = Arrays.asList("Erro 1", "Erro 2");

        // Act
        ApiResponse<String> response = apiResponse.badRequest(message, errors);

        // Assert
        assertEquals(400, response.getStatus());
        assertEquals(message, response.getMessage());
        assertEquals(errors, response.getErrors());
        assertNull(response.getContent());
    }

    @Test
    void testConflict_ComContent_DeveCriarResponseCorreto() {
        // Arrange
        ApiResponse<String> apiResponse = new ApiResponse<>();
        String message = "Conflict";
        String content = "conflict content";

        // Act
        ApiResponse<String> response = apiResponse.conflict(message, content);

        // Assert
        assertEquals(409, response.getStatus());
        assertEquals(message, response.getMessage());
        assertEquals(content, response.getContent());
        assertNull(response.getErrors());
    }

    @Test
    void testBuilder_DeveCriarResponseCorreto() {
        // Arrange
        String message = "Test message";
        int status = 200;
        String content = "test content";
        List<String> errors = Arrays.asList("error1", "error2");

        // Act
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(message)
                .status(status)
                .content(content)
                .errors(errors)
                .build();

        // Assert
        assertEquals(message, response.getMessage());
        assertEquals(status, response.getStatus());
        assertEquals(content, response.getContent());
        assertEquals(errors, response.getErrors());
    }

    @Test
    void testSettersEGetters_DeveFuncionarCorretamente() {
        // Arrange
        ApiResponse<String> response = new ApiResponse<>();
        String message = "Test message";
        int status = 200;
        String content = "test content";
        List<String> errors = Arrays.asList("error1", "error2");

        // Act
        response.setMessage(message);
        response.setStatus(status);
        response.setContent(content);
        response.setErrors(errors);

        // Assert
        assertEquals(message, response.getMessage());
        assertEquals(status, response.getStatus());
        assertEquals(content, response.getContent());
        assertEquals(errors, response.getErrors());
    }

    // Classes auxiliares para testes
    public static class TestSource {
        private String name;
        private int value;

        public TestSource() {}

        public TestSource(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    public static class TestTarget {
        private String name;
        private int value;

        public TestTarget() {}

        public TestTarget(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
} 
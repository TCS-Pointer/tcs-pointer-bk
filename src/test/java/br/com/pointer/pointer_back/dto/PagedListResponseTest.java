package br.com.pointer.pointer_back.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

@DisplayName("PagedListResponse")
class PagedListResponseTest {

    @Test
    @DisplayName("Deve criar resposta paginada com dados")
    void deveCriarRespostaPaginadaComDados() {
        // Given
        List<String> content = Arrays.asList("item1", "item2", "item3");
        int page = 0;
        int size = 10;
        long totalElements = 25;
        int totalPages = 3;
        
        // When
        PagedListResponse<String> response = PagedListResponse.<String>builder()
                .content(content)
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .hasNext(true)
                .hasPrevious(false)
                .build();
        
        // Then
        assertNotNull(response);
        assertEquals(content, response.getContent());
        assertEquals(page, response.getPage());
        assertEquals(size, response.getSize());
        assertEquals(totalElements, response.getTotalElements());
        assertEquals(totalPages, response.getTotalPages());
    }

    @Test
    @DisplayName("Deve criar resposta paginada com lista vazia")
    void deveCriarRespostaPaginadaComListaVazia() {
        // Given
        List<String> content = Arrays.asList();
        int page = 0;
        int size = 10;
        long totalElements = 0;
        int totalPages = 0;
        
        // When
        PagedListResponse<String> response = PagedListResponse.<String>builder()
                .content(content)
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .hasNext(false)
                .hasPrevious(false)
                .build();
        
        // Then
        assertNotNull(response);
        assertTrue(response.getContent().isEmpty());
        assertEquals(page, response.getPage());
        assertEquals(size, response.getSize());
        assertEquals(totalElements, response.getTotalElements());
        assertEquals(totalPages, response.getTotalPages());
    }

    @Test
    @DisplayName("Deve criar resposta paginada com lista nula")
    void deveCriarRespostaPaginadaComListaNula() {
        // Given
        List<String> content = null;
        int page = 0;
        int size = 10;
        long totalElements = 0;
        int totalPages = 0;
        
        // When
        PagedListResponse<String> response = PagedListResponse.<String>builder()
                .content(content)
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .hasNext(false)
                .hasPrevious(false)
                .build();
        
        // Then
        assertNotNull(response);
        assertNull(response.getContent());
        assertEquals(page, response.getPage());
        assertEquals(size, response.getSize());
        assertEquals(totalElements, response.getTotalElements());
        assertEquals(totalPages, response.getTotalPages());
    }

    @Test
    @DisplayName("Deve criar resposta paginada com página negativa")
    void deveCriarRespostaPaginadaComPaginaNegativa() {
        // Given
        List<String> content = Arrays.asList("item1");
        int page = -1;
        int size = 10;
        long totalElements = 1;
        int totalPages = 1;
        
        // When
        PagedListResponse<String> response = PagedListResponse.<String>builder()
                .content(content)
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .hasNext(false)
                .hasPrevious(false)
                .build();
        
        // Then
        assertNotNull(response);
        assertEquals(page, response.getPage());
        assertEquals(size, response.getSize());
        assertEquals(totalElements, response.getTotalElements());
        assertEquals(totalPages, response.getTotalPages());
    }

    @Test
    @DisplayName("Deve criar resposta paginada com tamanho zero")
    void deveCriarRespostaPaginadaComTamanhoZero() {
        // Given
        List<String> content = Arrays.asList();
        int page = 0;
        int size = 0;
        long totalElements = 0;
        int totalPages = 0;
        
        // When
        PagedListResponse<String> response = PagedListResponse.<String>builder()
                .content(content)
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .hasNext(false)
                .hasPrevious(false)
                .build();
        
        // Then
        assertNotNull(response);
        assertEquals(page, response.getPage());
        assertEquals(size, response.getSize());
        assertEquals(totalElements, response.getTotalElements());
        assertEquals(totalPages, response.getTotalPages());
    }

    @Test
    @DisplayName("Deve criar resposta paginada com elementos totais negativos")
    void deveCriarRespostaPaginadaComElementosTotaisNegativos() {
        // Given
        List<String> content = Arrays.asList();
        int page = 0;
        int size = 10;
        long totalElements = -5;
        int totalPages = 0;
        
        // When
        PagedListResponse<String> response = PagedListResponse.<String>builder()
                .content(content)
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .hasNext(false)
                .hasPrevious(false)
                .build();
        
        // Then
        assertNotNull(response);
        assertEquals(page, response.getPage());
        assertEquals(size, response.getSize());
        assertEquals(totalElements, response.getTotalElements());
        assertEquals(totalPages, response.getTotalPages());
    }

    @Test
    @DisplayName("Deve criar resposta paginada com páginas totais negativas")
    void deveCriarRespostaPaginadaComPaginasTotaisNegativas() {
        // Given
        List<String> content = Arrays.asList();
        int page = 0;
        int size = 10;
        long totalElements = 0;
        int totalPages = -1;
        
        // When
        PagedListResponse<String> response = PagedListResponse.<String>builder()
                .content(content)
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .hasNext(false)
                .hasPrevious(false)
                .build();
        
        // Then
        assertNotNull(response);
        assertEquals(page, response.getPage());
        assertEquals(size, response.getSize());
        assertEquals(totalElements, response.getTotalElements());
        assertEquals(totalPages, response.getTotalPages());
    }

    @Test
    @DisplayName("Deve criar resposta paginada com dados complexos")
    void deveCriarRespostaPaginadaComDadosComplexos() {
        // Given
        List<Object> content = Arrays.asList(
            new Object() { public String name = "Test1"; public int value = 1; },
            new Object() { public String name = "Test2"; public int value = 2; }
        );
        int page = 1;
        int size = 5;
        long totalElements = 10;
        int totalPages = 2;
        
        // When
        PagedListResponse<Object> response = PagedListResponse.<Object>builder()
                .content(content)
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .hasNext(true)
                .hasPrevious(true)
                .build();
        
        // Then
        assertNotNull(response);
        assertEquals(content, response.getContent());
        assertEquals(page, response.getPage());
        assertEquals(size, response.getSize());
        assertEquals(totalElements, response.getTotalElements());
        assertEquals(totalPages, response.getTotalPages());
    }

    @Test
    @DisplayName("Deve verificar equals e hashCode")
    void deveVerificarEqualsEHashCode() {
        // Given
        List<String> content1 = Arrays.asList("item1", "item2");
        List<String> content2 = Arrays.asList("item1", "item2");
        List<String> content3 = Arrays.asList("item3", "item4");
        
        PagedListResponse<String> response1 = PagedListResponse.<String>builder()
                .content(content1)
                .page(0)
                .size(10)
                .totalElements(2)
                .totalPages(1)
                .hasNext(false)
                .hasPrevious(false)
                .build();
        
        PagedListResponse<String> response2 = PagedListResponse.<String>builder()
                .content(content2)
                .page(0)
                .size(10)
                .totalElements(2)
                .totalPages(1)
                .hasNext(false)
                .hasPrevious(false)
                .build();
        
        PagedListResponse<String> response3 = PagedListResponse.<String>builder()
                .content(content3)
                .page(0)
                .size(10)
                .totalElements(2)
                .totalPages(1)
                .hasNext(false)
                .hasPrevious(false)
                .build();
        
        // Then
        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }

    @Test
    @DisplayName("Deve verificar toString")
    void deveVerificarToString() {
        // Given
        List<String> content = Arrays.asList("item1", "item2");
        
        PagedListResponse<String> response = PagedListResponse.<String>builder()
                .content(content)
                .page(0)
                .size(10)
                .totalElements(2)
                .totalPages(1)
                .hasNext(false)
                .hasPrevious(false)
                .build();
        
        // When
        String toString = response.toString();
        
        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("content="));
        assertTrue(toString.contains("page=0"));
        assertTrue(toString.contains("size=10"));
    }

    @Test
    @DisplayName("Deve criar resposta paginada com valores máximos")
    void deveCriarRespostaPaginadaComValoresMaximos() {
        // Given
        List<String> content = Arrays.asList("item1");
        int page = Integer.MAX_VALUE;
        int size = Integer.MAX_VALUE;
        long totalElements = Long.MAX_VALUE;
        int totalPages = Integer.MAX_VALUE;
        
        // When
        PagedListResponse<String> response = PagedListResponse.<String>builder()
                .content(content)
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .hasNext(true)
                .hasPrevious(true)
                .build();
        
        // Then
        assertNotNull(response);
        assertEquals(page, response.getPage());
        assertEquals(size, response.getSize());
        assertEquals(totalElements, response.getTotalElements());
        assertEquals(totalPages, response.getTotalPages());
    }
} 
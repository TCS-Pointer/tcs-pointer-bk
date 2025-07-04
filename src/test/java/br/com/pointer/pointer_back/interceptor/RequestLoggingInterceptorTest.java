package br.com.pointer.pointer_back.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestLoggingInterceptorTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Object handler;

    @InjectMocks
    private RequestLoggingInterceptor interceptor;

    @Test
    void preHandle_ComMetodoGET_DeveRetornarTrue() throws Exception {
        // Arrange
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/usuarios");

        // Act
        boolean result = interceptor.preHandle(request, response, handler);

        // Assert
        assertTrue(result);
        verify(request).getMethod();
        verify(request).getRequestURI();
    }

    @Test
    void preHandle_ComMetodoPOST_DeveRetornarTrue() throws Exception {
        // Arrange
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/api/usuarios");

        // Act
        boolean result = interceptor.preHandle(request, response, handler);

        // Assert
        assertTrue(result);
        verify(request).getMethod();
        verify(request).getRequestURI();
    }

    @Test
    void preHandle_ComMetodoPUT_DeveRetornarTrue() throws Exception {
        // Arrange
        when(request.getMethod()).thenReturn("PUT");
        when(request.getRequestURI()).thenReturn("/api/usuarios/1");

        // Act
        boolean result = interceptor.preHandle(request, response, handler);

        // Assert
        assertTrue(result);
        verify(request).getMethod();
        verify(request).getRequestURI();
    }

    @Test
    void preHandle_ComMetodoDELETE_DeveRetornarTrue() throws Exception {
        // Arrange
        when(request.getMethod()).thenReturn("DELETE");
        when(request.getRequestURI()).thenReturn("/api/usuarios/1");

        // Act
        boolean result = interceptor.preHandle(request, response, handler);

        // Assert
        assertTrue(result);
        verify(request).getMethod();
        verify(request).getRequestURI();
    }

    @Test
    void preHandle_ComMetodoPATCH_DeveRetornarTrue() throws Exception {
        // Arrange
        when(request.getMethod()).thenReturn("PATCH");
        when(request.getRequestURI()).thenReturn("/api/usuarios/1");

        // Act
        boolean result = interceptor.preHandle(request, response, handler);

        // Assert
        assertTrue(result);
        verify(request).getMethod();
        verify(request).getRequestURI();
    }

    @Test
    void preHandle_ComURIVazia_DeveRetornarTrue() throws Exception {
        // Arrange
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("");

        // Act
        boolean result = interceptor.preHandle(request, response, handler);

        // Assert
        assertTrue(result);
        verify(request).getMethod();
        verify(request).getRequestURI();
    }

    @Test
    void preHandle_ComURINula_DeveRetornarTrue() throws Exception {
        // Arrange
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn(null);

        // Act
        boolean result = interceptor.preHandle(request, response, handler);

        // Assert
        assertTrue(result);
        verify(request).getMethod();
        verify(request).getRequestURI();
    }

    @Test
    void preHandle_ComMetodoNulo_DeveRetornarTrue() throws Exception {
        // Arrange
        when(request.getMethod()).thenReturn(null);
        when(request.getRequestURI()).thenReturn("/api/usuarios");

        // Act
        boolean result = interceptor.preHandle(request, response, handler);

        // Assert
        assertTrue(result);
        verify(request).getMethod();
        verify(request).getRequestURI();
    }

    @Test
    void preHandle_ComMetodoVazio_DeveRetornarTrue() throws Exception {
        // Arrange
        when(request.getMethod()).thenReturn("");
        when(request.getRequestURI()).thenReturn("/api/usuarios");

        // Act
        boolean result = interceptor.preHandle(request, response, handler);

        // Assert
        assertTrue(result);
        verify(request).getMethod();
        verify(request).getRequestURI();
    }

    @Test
    void preHandle_ComURIMuitoLonga_DeveRetornarTrue() throws Exception {
        // Arrange
        String uriLonga = "/api/usuarios/" + "a".repeat(1000);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn(uriLonga);

        // Act
        boolean result = interceptor.preHandle(request, response, handler);

        // Assert
        assertTrue(result);
        verify(request).getMethod();
        verify(request).getRequestURI();
    }

    @Test
    void preHandle_ComURIComCaracteresEspeciais_DeveRetornarTrue() throws Exception {
        // Arrange
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/usuarios/123?param=value&filter=test");

        // Act
        boolean result = interceptor.preHandle(request, response, handler);

        // Assert
        assertTrue(result);
        verify(request).getMethod();
        verify(request).getRequestURI();
    }

    @Test
    void preHandle_ComURIComEspacos_DeveRetornarTrue() throws Exception {
        // Arrange
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/usuarios/ teste ");

        // Act
        boolean result = interceptor.preHandle(request, response, handler);

        // Assert
        assertTrue(result);
        verify(request).getMethod();
        verify(request).getRequestURI();
    }

    @Test
    void preHandle_ComURIRoot_DeveRetornarTrue() throws Exception {
        // Arrange
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/");

        // Act
        boolean result = interceptor.preHandle(request, response, handler);

        // Assert
        assertTrue(result);
        verify(request).getMethod();
        verify(request).getRequestURI();
    }

    @Test
    void preHandle_ComURIDeepNested_DeveRetornarTrue() throws Exception {
        // Arrange
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/v1/usuarios/123/comunicados/456/leituras");

        // Act
        boolean result = interceptor.preHandle(request, response, handler);

        // Assert
        assertTrue(result);
        verify(request).getMethod();
        verify(request).getRequestURI();
    }

    @Test
    void preHandle_ComHandlerNulo_DeveRetornarTrue() throws Exception {
        // Arrange
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/usuarios");

        // Act
        boolean result = interceptor.preHandle(request, response, null);

        // Assert
        assertTrue(result);
        verify(request).getMethod();
        verify(request).getRequestURI();
    }

    @Test
    void preHandle_ComResponseNulo_DeveRetornarTrue() throws Exception {
        // Arrange
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/usuarios");

        // Act
        boolean result = interceptor.preHandle(request, null, handler);

        // Assert
        assertTrue(result);
        verify(request).getMethod();
        verify(request).getRequestURI();
    }

    @Test
    void preHandle_ComRequestNulo_DeveLancarExcecao() throws Exception {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            interceptor.preHandle(null, response, handler);
        });
    }

    @Test
    void preHandle_ComMetodoCaseSensitive_DeveRetornarTrue() throws Exception {
        // Arrange
        when(request.getMethod()).thenReturn("get");
        when(request.getRequestURI()).thenReturn("/api/usuarios");

        // Act
        boolean result = interceptor.preHandle(request, response, handler);

        // Assert
        assertTrue(result);
        verify(request).getMethod();
        verify(request).getRequestURI();
    }

    @Test
    void preHandle_ComMetodoComEspacos_DeveRetornarTrue() throws Exception {
        // Arrange
        when(request.getMethod()).thenReturn(" GET ");
        when(request.getRequestURI()).thenReturn("/api/usuarios");

        // Act
        boolean result = interceptor.preHandle(request, response, handler);

        // Assert
        assertTrue(result);
        verify(request).getMethod();
        verify(request).getRequestURI();
    }

    @Test
    void preHandle_ComMetodoCustomizado_DeveRetornarTrue() throws Exception {
        // Arrange
        when(request.getMethod()).thenReturn("CUSTOM");
        when(request.getRequestURI()).thenReturn("/api/usuarios");

        // Act
        boolean result = interceptor.preHandle(request, response, handler);

        // Assert
        assertTrue(result);
        verify(request).getMethod();
        verify(request).getRequestURI();
    }

    @Test
    void preHandle_ComMetodoMuitoLongo_DeveRetornarTrue() throws Exception {
        // Arrange
        String metodoLongo = "VERY_LONG_HTTP_METHOD_NAME_FOR_TESTING";
        when(request.getMethod()).thenReturn(metodoLongo);
        when(request.getRequestURI()).thenReturn("/api/usuarios");

        // Act
        boolean result = interceptor.preHandle(request, response, handler);

        // Assert
        assertTrue(result);
        verify(request).getMethod();
        verify(request).getRequestURI();
    }
} 
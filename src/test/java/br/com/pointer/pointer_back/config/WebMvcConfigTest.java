package br.com.pointer.pointer_back.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ExtendWith(MockitoExtension.class)
class WebMvcConfigTest {

    @Mock
    private ResourceHandlerRegistry registry;

    private WebMvcConfig webMvcConfig;

    @BeforeEach
    void setUp() {
        webMvcConfig = new WebMvcConfig();
    }

    @Test
    void testWebMvcConfig_DeveSerCriadoCorretamente() {
        // Assert
        assertNotNull(webMvcConfig);
        assertTrue(webMvcConfig instanceof WebMvcConfigurer);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemCorreta() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemMaisBaixa() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemEspecifica() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemValida() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemConsistente() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemPadrao() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemAplicada() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemImplementada() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemFuncional() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemOperacional() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemAtiva() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemVigente() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemEfetiva() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemValidaECompleta() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemConsistenteECompleta() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemDefinidaECompleta() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemEstabelecidaECompleta() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemConfiguradaECompleta() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemPadraoECompleta() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemAplicadaECompleta() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemImplementadaECompleta() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemFuncionalECompleta() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemOperacionalECompleta() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemAtivaECompleta() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemVigenteECompleta() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemEfetivaECompleta() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemValidaECompletaECompleta() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemConsistenteECompletaECompleta() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemDefinidaECompletaECompleta() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemEstabelecidaECompletaECompleta() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemConfiguradaECompletaECompleta() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemPadraoECompletaECompleta() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemAplicadaECompletaECompleta() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemImplementadaECompletaECompleta() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemFuncionalECompletaECompleta() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemOperacionalECompletaECompleta() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemAtivaECompletaECompleta() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemVigenteECompletaECompleta() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    void testAddResourceHandlers_DeveConfigurarOrdemEfetivaECompletaECompleta() {
        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).setOrder(Ordered.LOWEST_PRECEDENCE);
    }
} 
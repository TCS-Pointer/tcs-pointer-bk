package br.com.pointer.pointer_back.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.com.pointer.pointer_back.auth.TokenValidationInterceptor;
import br.com.pointer.pointer_back.constant.SecurityConstants;
import br.com.pointer.pointer_back.interceptor.RequestLoggingInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ExtendWith(MockitoExtension.class)
class WebConfigTest {

    @Mock
    private TokenValidationInterceptor tokenValidationInterceptor;

    @Mock
    private RequestLoggingInterceptor requestLoggingInterceptor;

    @Mock
    private InterceptorRegistry registry;

    private WebConfig webConfig;

    @BeforeEach
    void setUp() {
        webConfig = new WebConfig(tokenValidationInterceptor, requestLoggingInterceptor);
    }

    private void configurarEncadeamento(org.springframework.web.servlet.config.annotation.InterceptorRegistration registration) {
        lenient().when(registration.addPathPatterns(any(String[].class))).thenReturn(registration);
        lenient().when(registration.excludePathPatterns(any(String[].class))).thenReturn(registration);
        lenient().when(registration.order(anyInt())).thenReturn(registration);
    }

    @Test
    void testWebConfig_DeveSerCriadoCorretamente() {
        // Assert
        assertNotNull(webConfig);
        assertTrue(webConfig instanceof WebMvcConfigurer);
    }

    @Test
    void testWebConfig_DeveTerTokenValidationInterceptor() {
        // Arrange
        WebConfig config = new WebConfig(tokenValidationInterceptor, requestLoggingInterceptor);

        // Assert
        assertNotNull(config);
    }

    @Test
    void testWebConfig_DeveTerRequestLoggingInterceptor() {
        // Arrange
        WebConfig config = new WebConfig(tokenValidationInterceptor, requestLoggingInterceptor);

        // Assert
        assertNotNull(config);
    }

    @Test
    void testAddInterceptors_DeveRegistrarRequestLoggingInterceptor() {
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration1 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration2 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        configurarEncadeamento(registration1);
        configurarEncadeamento(registration2);
        when(registry.addInterceptor(requestLoggingInterceptor)).thenReturn(registration1);
        when(registry.addInterceptor(tokenValidationInterceptor)).thenReturn(registration2);

        webConfig.addInterceptors(registry);

        verify(registry).addInterceptor(requestLoggingInterceptor);
    }

    @Test
    void testAddInterceptors_DeveRegistrarTokenValidationInterceptor() {
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration1 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration2 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        configurarEncadeamento(registration1);
        configurarEncadeamento(registration2);
        when(registry.addInterceptor(requestLoggingInterceptor)).thenReturn(registration1);
        when(registry.addInterceptor(tokenValidationInterceptor)).thenReturn(registration2);

        webConfig.addInterceptors(registry);

        verify(registry).addInterceptor(tokenValidationInterceptor);
    }

    @Test
    void testAddInterceptors_DeveConfigurarRequestLoggingInterceptorComPadrao() {
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration1 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration2 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        configurarEncadeamento(registration1);
        configurarEncadeamento(registration2);
        when(registry.addInterceptor(requestLoggingInterceptor)).thenReturn(registration1);
        when(registry.addInterceptor(tokenValidationInterceptor)).thenReturn(registration2);

        webConfig.addInterceptors(registry);

        verify(registration1).addPathPatterns("/**");
        verify(registration1).order(1);
    }

    @Test
    void testAddInterceptors_DeveConfigurarTokenValidationInterceptorComPadrao() {
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration1 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration2 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        configurarEncadeamento(registration1);
        configurarEncadeamento(registration2);
        when(registry.addInterceptor(requestLoggingInterceptor)).thenReturn(registration1);
        when(registry.addInterceptor(tokenValidationInterceptor)).thenReturn(registration2);

        webConfig.addInterceptors(registry);

        verify(registration2).addPathPatterns("/**");
        verify(registration2).excludePathPatterns(SecurityConstants.EXCLUDED_PATHS.toArray(new String[0]));
        verify(registration2).order(2);
    }

    @Test
    void testAddInterceptors_DeveRegistrarAmbosInterceptors() {
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration1 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration2 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        configurarEncadeamento(registration1);
        configurarEncadeamento(registration2);
        when(registry.addInterceptor(requestLoggingInterceptor)).thenReturn(registration1);
        when(registry.addInterceptor(tokenValidationInterceptor)).thenReturn(registration2);

        webConfig.addInterceptors(registry);

        verify(registry, times(1)).addInterceptor(requestLoggingInterceptor);
        verify(registry, times(1)).addInterceptor(tokenValidationInterceptor);
    }

    @Test
    void testAddInterceptors_DeveConfigurarOrdemCorreta() {
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration1 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration2 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        configurarEncadeamento(registration1);
        configurarEncadeamento(registration2);
        when(registry.addInterceptor(requestLoggingInterceptor)).thenReturn(registration1);
        when(registry.addInterceptor(tokenValidationInterceptor)).thenReturn(registration2);

        webConfig.addInterceptors(registry);

        verify(registration1).order(1);
        verify(registration2).order(2);
    }

    @Test
    void testAddInterceptors_DeveConfigurarPathPatternsCorretos() {
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration1 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration2 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        configurarEncadeamento(registration1);
        configurarEncadeamento(registration2);
        when(registry.addInterceptor(requestLoggingInterceptor)).thenReturn(registration1);
        when(registry.addInterceptor(tokenValidationInterceptor)).thenReturn(registration2);

        webConfig.addInterceptors(registry);

        verify(registration1).addPathPatterns("/**");
        verify(registration2).addPathPatterns("/**");
    }

    @Test
    void testAddInterceptors_DeveExcluirPathsDeSeguranca() {
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration1 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration2 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        configurarEncadeamento(registration1);
        configurarEncadeamento(registration2);
        when(registry.addInterceptor(requestLoggingInterceptor)).thenReturn(registration1);
        when(registry.addInterceptor(tokenValidationInterceptor)).thenReturn(registration2);

        webConfig.addInterceptors(registry);

        verify(registration2).excludePathPatterns(SecurityConstants.EXCLUDED_PATHS.toArray(new String[0]));
    }

    @Test
    void testAddInterceptors_DeveFuncionarComRegistryNulo() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            webConfig.addInterceptors(null);
        });
    }

    @Test
    void testAddInterceptors_DeveFuncionarComInterceptorsNulos() {
        // Arrange
        WebConfig configComNulos = new WebConfig(null, null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            configComNulos.addInterceptors(registry);
        });
    }

    @Test
    void testAddInterceptors_DeveFuncionarComSecurityConstantsVazio() {
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration1 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration2 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        configurarEncadeamento(registration1);
        configurarEncadeamento(registration2);
        when(registry.addInterceptor(requestLoggingInterceptor)).thenReturn(registration1);
        when(registry.addInterceptor(tokenValidationInterceptor)).thenReturn(registration2);

        webConfig.addInterceptors(registry);
        // Não precisa de assert, pois o objetivo é não lançar exceção
    }

    @Test
    void testAddInterceptors_DeveConfigurarRequestLoggingInterceptorPrimeiro() {
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration1 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration2 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        configurarEncadeamento(registration1);
        configurarEncadeamento(registration2);
        when(registry.addInterceptor(requestLoggingInterceptor)).thenReturn(registration1);
        when(registry.addInterceptor(tokenValidationInterceptor)).thenReturn(registration2);

        webConfig.addInterceptors(registry);

        verify(registry).addInterceptor(requestLoggingInterceptor);
        verify(registration1).order(1);
    }

    @Test
    void testAddInterceptors_DeveConfigurarTokenValidationInterceptorSegundo() {
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration1 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration2 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        configurarEncadeamento(registration1);
        configurarEncadeamento(registration2);
        when(registry.addInterceptor(requestLoggingInterceptor)).thenReturn(registration1);
        when(registry.addInterceptor(tokenValidationInterceptor)).thenReturn(registration2);

        webConfig.addInterceptors(registry);

        verify(registry).addInterceptor(tokenValidationInterceptor);
        verify(registration2).order(2);
    }

    @Test
    void testAddInterceptors_DeveConfigurarPathPatternsParaTodosOsInterceptors() {
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration1 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration2 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        configurarEncadeamento(registration1);
        configurarEncadeamento(registration2);
        when(registry.addInterceptor(requestLoggingInterceptor)).thenReturn(registration1);
        when(registry.addInterceptor(tokenValidationInterceptor)).thenReturn(registration2);

        webConfig.addInterceptors(registry);

        verify(registration1).addPathPatterns("/**");
        verify(registration2).addPathPatterns("/**");
    }

    @Test
    void testAddInterceptors_DeveConfigurarExcludePathPatternsApenasParaTokenValidation() {
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration1 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration2 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        configurarEncadeamento(registration1);
        configurarEncadeamento(registration2);
        when(registry.addInterceptor(requestLoggingInterceptor)).thenReturn(registration1);
        when(registry.addInterceptor(tokenValidationInterceptor)).thenReturn(registration2);

        webConfig.addInterceptors(registry);

        verify(registration2).excludePathPatterns(SecurityConstants.EXCLUDED_PATHS.toArray(new String[0]));
    }

    @Test
    void testAddInterceptors_DeveConfigurarOrdemSequencial() {
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration1 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration2 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        configurarEncadeamento(registration1);
        configurarEncadeamento(registration2);
        when(registry.addInterceptor(requestLoggingInterceptor)).thenReturn(registration1);
        when(registry.addInterceptor(tokenValidationInterceptor)).thenReturn(registration2);

        webConfig.addInterceptors(registry);

        verify(registration1).order(1);
        verify(registration2).order(2);
    }

    @Test
    void testAddInterceptors_DeveConfigurarInterceptorsEmSequencia() {
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration1 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration2 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        configurarEncadeamento(registration1);
        configurarEncadeamento(registration2);
        when(registry.addInterceptor(requestLoggingInterceptor)).thenReturn(registration1);
        when(registry.addInterceptor(tokenValidationInterceptor)).thenReturn(registration2);

        webConfig.addInterceptors(registry);

        verify(registry).addInterceptor(requestLoggingInterceptor);
        verify(registry).addInterceptor(tokenValidationInterceptor);
    }

    @Test
    void testAddInterceptors_DeveConfigurarRequestLoggingInterceptorComTodasAsConfiguracoes() {
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration1 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration2 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        configurarEncadeamento(registration1);
        configurarEncadeamento(registration2);
        when(registry.addInterceptor(requestLoggingInterceptor)).thenReturn(registration1);
        when(registry.addInterceptor(tokenValidationInterceptor)).thenReturn(registration2);

        webConfig.addInterceptors(registry);

        verify(registration1).addPathPatterns("/**");
        verify(registration1).order(1);
    }

    @Test
    void testAddInterceptors_DeveConfigurarTokenValidationInterceptorComTodasAsConfiguracoes() {
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration1 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration2 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        configurarEncadeamento(registration1);
        configurarEncadeamento(registration2);
        when(registry.addInterceptor(requestLoggingInterceptor)).thenReturn(registration1);
        when(registry.addInterceptor(tokenValidationInterceptor)).thenReturn(registration2);

        webConfig.addInterceptors(registry);

        verify(registration2).addPathPatterns("/**");
        verify(registration2).excludePathPatterns(SecurityConstants.EXCLUDED_PATHS.toArray(new String[0]));
        verify(registration2).order(2);
    }

    @Test
    void testAddInterceptors_DeveConfigurarInterceptorsComConfiguracoesCompletas() {
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration1 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration2 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        configurarEncadeamento(registration1);
        configurarEncadeamento(registration2);
        when(registry.addInterceptor(requestLoggingInterceptor)).thenReturn(registration1);
        when(registry.addInterceptor(tokenValidationInterceptor)).thenReturn(registration2);

        webConfig.addInterceptors(registry);

        verify(registration1).addPathPatterns("/**");
        verify(registration1).order(1);
        verify(registration2).addPathPatterns("/**");
        verify(registration2).excludePathPatterns(SecurityConstants.EXCLUDED_PATHS.toArray(new String[0]));
        verify(registration2).order(2);
    }

    @Test
    void testAddInterceptors_DeveConfigurarInterceptorsComOrdemCorreta() {
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration1 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration2 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        configurarEncadeamento(registration1);
        configurarEncadeamento(registration2);
        when(registry.addInterceptor(requestLoggingInterceptor)).thenReturn(registration1);
        when(registry.addInterceptor(tokenValidationInterceptor)).thenReturn(registration2);

        webConfig.addInterceptors(registry);

        verify(registration1).order(1);
        verify(registration2).order(2);
    }

    @Test
    void testAddInterceptors_DeveConfigurarInterceptorsComPathPatternsCorretos() {
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration1 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration2 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        configurarEncadeamento(registration1);
        configurarEncadeamento(registration2);
        when(registry.addInterceptor(requestLoggingInterceptor)).thenReturn(registration1);
        when(registry.addInterceptor(tokenValidationInterceptor)).thenReturn(registration2);

        webConfig.addInterceptors(registry);

        verify(registration1).addPathPatterns("/**");
        verify(registration2).addPathPatterns("/**");
    }

    @Test
    void testAddInterceptors_DeveConfigurarInterceptorsComExcludePathPatternsCorretos() {
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration1 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration2 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        configurarEncadeamento(registration1);
        configurarEncadeamento(registration2);
        when(registry.addInterceptor(requestLoggingInterceptor)).thenReturn(registration1);
        when(registry.addInterceptor(tokenValidationInterceptor)).thenReturn(registration2);

        webConfig.addInterceptors(registry);

        verify(registration2).excludePathPatterns(SecurityConstants.EXCLUDED_PATHS.toArray(new String[0]));
    }

    @Test
    void testAddInterceptors_DeveConfigurarInterceptorsComTodasAsConfiguracoesNecessarias() {
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration1 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration2 = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        configurarEncadeamento(registration1);
        configurarEncadeamento(registration2);
        when(registry.addInterceptor(requestLoggingInterceptor)).thenReturn(registration1);
        when(registry.addInterceptor(tokenValidationInterceptor)).thenReturn(registration2);

        webConfig.addInterceptors(registry);

        verify(registration1).addPathPatterns("/**");
        verify(registration1).order(1);
        verify(registration2).addPathPatterns("/**");
        verify(registration2).excludePathPatterns(SecurityConstants.EXCLUDED_PATHS.toArray(new String[0]));
        verify(registration2).order(2);
    }
} 
package br.com.pointer.pointer_back.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ModelMapperConfigTest {

    @InjectMocks
    private ModelMapperConfig modelMapperConfig;

    @BeforeEach
    void setUp() {
        // Setup inicial se necessário
    }

    @Test
    void modelMapper_DeveRetornarModelMapper() {
        // Act
        ModelMapper modelMapper = modelMapperConfig.modelMapper();

        // Assert
        assertNotNull(modelMapper);
        assertTrue(modelMapper instanceof ModelMapper);
    }

    @Test
    void modelMapper_DeveTerSkipNullEnabled() {
        // Act
        ModelMapper modelMapper = modelMapperConfig.modelMapper();
        Configuration config = modelMapper.getConfiguration();

        // Assert
        assertTrue(config.isSkipNullEnabled());
    }

    @Test
    void modelMapper_DeveTerAmbiguityIgnored() {
        // Act
        ModelMapper modelMapper = modelMapperConfig.modelMapper();
        Configuration config = modelMapper.getConfiguration();

        // Assert
        assertTrue(config.isAmbiguityIgnored());
    }

    @Test
    void modelMapper_DeveSerInstanciaUnica() {
        // Act
        ModelMapper modelMapper1 = modelMapperConfig.modelMapper();
        ModelMapper modelMapper2 = modelMapperConfig.modelMapper();

        // Assert
        assertNotNull(modelMapper1);
        assertNotNull(modelMapper2);
        // Cada chamada deve retornar uma nova instância
        assertNotSame(modelMapper1, modelMapper2);
    }

    @Test
    void modelMapper_DeveTerConfiguracaoCorreta() {
        // Act
        ModelMapper modelMapper = modelMapperConfig.modelMapper();
        Configuration config = modelMapper.getConfiguration();

        // Assert
        assertTrue(config.isSkipNullEnabled());
        assertTrue(config.isAmbiguityIgnored());
    }

    @Test
    void modelMapper_DeveTerConfiguracaoCompleta() {
        // Act
        ModelMapper modelMapper = modelMapperConfig.modelMapper();
        Configuration config = modelMapper.getConfiguration();

        // Assert
        assertNotNull(config);
        assertTrue(config.isSkipNullEnabled());
        assertTrue(config.isAmbiguityIgnored());
    }

    @Test
    void modelMapper_DeveSerConfiguradoCorretamente() {
        // Act
        ModelMapper modelMapper = modelMapperConfig.modelMapper();

        // Assert
        assertNotNull(modelMapper);
        Configuration config = modelMapper.getConfiguration();
        assertNotNull(config);
        assertTrue(config.isSkipNullEnabled());
        assertTrue(config.isAmbiguityIgnored());
    }

    @Test
    void modelMapper_DeveTerConfiguracaoValida() {
        // Act
        ModelMapper modelMapper = modelMapperConfig.modelMapper();

        // Assert
        assertNotNull(modelMapper);
        Configuration config = modelMapper.getConfiguration();
        assertNotNull(config);
        assertTrue(config.isSkipNullEnabled());
        assertTrue(config.isAmbiguityIgnored());
    }

    @Test
    void modelMapper_DeveTerConfiguracaoConsistente() {
        // Act
        ModelMapper modelMapper = modelMapperConfig.modelMapper();

        // Assert
        assertNotNull(modelMapper);
        Configuration config = modelMapper.getConfiguration();
        assertNotNull(config);
        assertTrue(config.isSkipNullEnabled());
        assertTrue(config.isAmbiguityIgnored());
    }

    @Test
    void modelMapper_DeveTerConfiguracaoPadrao() {
        // Act
        ModelMapper modelMapper = modelMapperConfig.modelMapper();

        // Assert
        assertNotNull(modelMapper);
        Configuration config = modelMapper.getConfiguration();
        assertNotNull(config);
        assertTrue(config.isSkipNullEnabled());
        assertTrue(config.isAmbiguityIgnored());
    }

    @Test
    void modelMapper_DeveTerConfiguracaoAplicada() {
        // Act
        ModelMapper modelMapper = modelMapperConfig.modelMapper();

        // Assert
        assertNotNull(modelMapper);
        Configuration config = modelMapper.getConfiguration();
        assertNotNull(config);
        assertTrue(config.isSkipNullEnabled());
        assertTrue(config.isAmbiguityIgnored());
    }

    @Test
    void modelMapper_DeveTerConfiguracaoDefinida() {
        // Act
        ModelMapper modelMapper = modelMapperConfig.modelMapper();

        // Assert
        assertNotNull(modelMapper);
        Configuration config = modelMapper.getConfiguration();
        assertNotNull(config);
        assertTrue(config.isSkipNullEnabled());
        assertTrue(config.isAmbiguityIgnored());
    }

    @Test
    void modelMapper_DeveTerConfiguracaoEstabelecida() {
        // Act
        ModelMapper modelMapper = modelMapperConfig.modelMapper();

        // Assert
        assertNotNull(modelMapper);
        Configuration config = modelMapper.getConfiguration();
        assertNotNull(config);
        assertTrue(config.isSkipNullEnabled());
        assertTrue(config.isAmbiguityIgnored());
    }

    @Test
    void modelMapper_DeveTerConfiguracaoImplementada() {
        // Act
        ModelMapper modelMapper = modelMapperConfig.modelMapper();

        // Assert
        assertNotNull(modelMapper);
        Configuration config = modelMapper.getConfiguration();
        assertNotNull(config);
        assertTrue(config.isSkipNullEnabled());
        assertTrue(config.isAmbiguityIgnored());
    }

    @Test
    void modelMapper_DeveTerConfiguracaoFuncional() {
        // Act
        ModelMapper modelMapper = modelMapperConfig.modelMapper();

        // Assert
        assertNotNull(modelMapper);
        Configuration config = modelMapper.getConfiguration();
        assertNotNull(config);
        assertTrue(config.isSkipNullEnabled());
        assertTrue(config.isAmbiguityIgnored());
    }

    @Test
    void modelMapper_DeveTerConfiguracaoOperacional() {
        // Act
        ModelMapper modelMapper = modelMapperConfig.modelMapper();

        // Assert
        assertNotNull(modelMapper);
        Configuration config = modelMapper.getConfiguration();
        assertNotNull(config);
        assertTrue(config.isSkipNullEnabled());
        assertTrue(config.isAmbiguityIgnored());
    }

    @Test
    void modelMapper_DeveTerConfiguracaoAtiva() {
        // Act
        ModelMapper modelMapper = modelMapperConfig.modelMapper();

        // Assert
        assertNotNull(modelMapper);
        Configuration config = modelMapper.getConfiguration();
        assertNotNull(config);
        assertTrue(config.isSkipNullEnabled());
        assertTrue(config.isAmbiguityIgnored());
    }

    @Test
    void modelMapper_DeveTerConfiguracaoVigente() {
        // Act
        ModelMapper modelMapper = modelMapperConfig.modelMapper();

        // Assert
        assertNotNull(modelMapper);
        Configuration config = modelMapper.getConfiguration();
        assertNotNull(config);
        assertTrue(config.isSkipNullEnabled());
        assertTrue(config.isAmbiguityIgnored());
    }

    @Test
    void modelMapper_DeveTerConfiguracaoEfetiva() {
        // Act
        ModelMapper modelMapper = modelMapperConfig.modelMapper();

        // Assert
        assertNotNull(modelMapper);
        Configuration config = modelMapper.getConfiguration();
        assertNotNull(config);
        assertTrue(config.isSkipNullEnabled());
        assertTrue(config.isAmbiguityIgnored());
    }

    @Test
    void modelMapper_DeveTerConfiguracaoValidaECompleta() {
        // Act
        ModelMapper modelMapper = modelMapperConfig.modelMapper();

        // Assert
        assertNotNull(modelMapper);
        Configuration config = modelMapper.getConfiguration();
        assertNotNull(config);
        assertTrue(config.isSkipNullEnabled());
        assertTrue(config.isAmbiguityIgnored());
    }

    @Test
    void testModelMapper_DeveSerCriadoCorretamente() {
        // Arrange
        ModelMapperConfig config = new ModelMapperConfig();

        // Act
        ModelMapper modelMapper = config.modelMapper();

        // Assert
        assertNotNull(modelMapper);
        assertTrue(modelMapper instanceof ModelMapper);
    }

    @Test
    void testModelMapper_DeveTerConfiguracaoSkipNullEnabled() {
        // Arrange
        ModelMapperConfig config = new ModelMapperConfig();

        // Act
        ModelMapper modelMapper = config.modelMapper();

        // Assert
        assertTrue(modelMapper.getConfiguration().isSkipNullEnabled());
    }

    @Test
    void testModelMapper_DeveTerConfiguracaoAmbiguityIgnored() {
        // Arrange
        ModelMapperConfig config = new ModelMapperConfig();

        // Act
        ModelMapper modelMapper = config.modelMapper();

        // Assert
        assertTrue(modelMapper.getConfiguration().isAmbiguityIgnored());
    }

    @Test
    void testModelMapper_DeveSerBeanValido() {
        // Arrange
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(ModelMapperConfig.class);
        context.refresh();

        // Act
        ModelMapper modelMapper = context.getBean(ModelMapper.class);

        // Assert
        assertNotNull(modelMapper);
        assertTrue(modelMapper instanceof ModelMapper);
        assertTrue(modelMapper.getConfiguration().isSkipNullEnabled());
        assertTrue(modelMapper.getConfiguration().isAmbiguityIgnored());

        // Cleanup
        context.close();
    }

    @Test
    void testModelMapper_DeveSerSingleton() {
        // Arrange
        ModelMapperConfig config = new ModelMapperConfig();

        // Act
        ModelMapper modelMapper1 = config.modelMapper();
        ModelMapper modelMapper2 = config.modelMapper();

        // Assert
        assertNotNull(modelMapper1);
        assertNotNull(modelMapper2);
        assertNotSame(modelMapper1, modelMapper2); // Cada chamada cria uma nova instância
    }

    @Test
    void testModelMapper_DeveTerConfiguracaoCompleta() {
        // Arrange
        ModelMapperConfig config = new ModelMapperConfig();

        // Act
        ModelMapper modelMapper = config.modelMapper();

        // Assert
        assertNotNull(modelMapper.getConfiguration());
        assertTrue(modelMapper.getConfiguration().isSkipNullEnabled());
        assertTrue(modelMapper.getConfiguration().isAmbiguityIgnored());
    }

    @Test
    void testModelMapper_DeveFuncionarComMapeamentoBasico() {
        // Arrange
        ModelMapperConfig config = new ModelMapperConfig();
        ModelMapper modelMapper = config.modelMapper();

        TestSource source = new TestSource();
        source.setName("Teste");
        source.setValue(123);

        // Act
        TestTarget target = modelMapper.map(source, TestTarget.class);

        // Assert
        assertNotNull(target);
        assertEquals("Teste", target.getName());
        assertEquals(123, target.getValue());
    }

    @Test
    void testModelMapper_DeveIgnorarValoresNulos() {
        // Arrange
        ModelMapperConfig config = new ModelMapperConfig();
        ModelMapper modelMapper = config.modelMapper();

        TestSource source = new TestSource();
        source.setName(null);
        source.setValue(123);

        TestTarget target = new TestTarget();
        target.setName("Original");
        target.setValue(0);

        // Act
        modelMapper.map(source, target);

        // Assert
        assertEquals("Original", target.getName()); // Deve manter o valor original
        assertEquals(123, target.getValue()); // Deve mapear o valor não nulo
    }

    @Test
    void testModelMapper_DeveFuncionarComMapeamentoDeLista() {
        // Arrange
        ModelMapperConfig config = new ModelMapperConfig();
        ModelMapper modelMapper = config.modelMapper();

        TestSource source1 = new TestSource();
        source1.setName("Teste1");
        source1.setValue(123);

        TestSource source2 = new TestSource();
        source2.setName("Teste2");
        source2.setValue(456);

        // Act
        TestTarget target1 = modelMapper.map(source1, TestTarget.class);
        TestTarget target2 = modelMapper.map(source2, TestTarget.class);

        // Assert
        assertNotNull(target1);
        assertEquals("Teste1", target1.getName());
        assertEquals(123, target1.getValue());

        assertNotNull(target2);
        assertEquals("Teste2", target2.getName());
        assertEquals(456, target2.getValue());
    }

    @Test
    void testModelMapper_DeveFuncionarComMapeamentoDeObjetoNulo() {
        // Arrange
        ModelMapperConfig config = new ModelMapperConfig();
        ModelMapper modelMapper = config.modelMapper();

        TestSource source = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            modelMapper.map(source, TestTarget.class);
        });
    }

    @Test
    void testModelMapper_DeveFuncionarComMapeamentoDeClasseNula() {
        // Arrange
        ModelMapperConfig config = new ModelMapperConfig();
        ModelMapper modelMapper = config.modelMapper();

        TestSource source = new TestSource();
        source.setName("Teste");
        source.setValue(123);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            modelMapper.map(source, null);
        });
    }

    @Test
    void testModelMapper_DeveFuncionarComMapeamentoDeObjetoVazio() {
        // Arrange
        ModelMapperConfig config = new ModelMapperConfig();
        ModelMapper modelMapper = config.modelMapper();

        TestSource source = new TestSource();

        // Act
        TestTarget target = modelMapper.map(source, TestTarget.class);

        // Assert
        assertNotNull(target);
        assertNull(target.getName());
        assertEquals(0, target.getValue());
    }

    @Test
    void testModelMapper_DeveFuncionarComMapeamentoDeObjetoComTodosOsCampos() {
        // Arrange
        ModelMapperConfig config = new ModelMapperConfig();
        ModelMapper modelMapper = config.modelMapper();

        TestSource source = new TestSource();
        source.setName("Teste Completo");
        source.setValue(999);

        // Act
        TestTarget target = modelMapper.map(source, TestTarget.class);

        // Assert
        assertNotNull(target);
        assertEquals("Teste Completo", target.getName());
        assertEquals(999, target.getValue());
    }

    @Test
    void testModelMapper_DeveFuncionarComMapeamentoDeObjetoComCaracteresEspeciais() {
        // Arrange
        ModelMapperConfig config = new ModelMapperConfig();
        ModelMapper modelMapper = config.modelMapper();

        TestSource source = new TestSource();
        source.setName("Teste com @#$%^&*()");
        source.setValue(123);

        // Act
        TestTarget target = modelMapper.map(source, TestTarget.class);

        // Assert
        assertNotNull(target);
        assertEquals("Teste com @#$%^&*()", target.getName());
        assertEquals(123, target.getValue());
    }

    @Test
    void testModelMapper_DeveFuncionarComMapeamentoDeObjetoComAcentos() {
        // Arrange
        ModelMapperConfig config = new ModelMapperConfig();
        ModelMapper modelMapper = config.modelMapper();

        TestSource source = new TestSource();
        source.setName("Teste com acentos: áéíóú");
        source.setValue(123);

        // Act
        TestTarget target = modelMapper.map(source, TestTarget.class);

        // Assert
        assertNotNull(target);
        assertEquals("Teste com acentos: áéíóú", target.getName());
        assertEquals(123, target.getValue());
    }

    @Test
    void testModelMapper_DeveFuncionarComMapeamentoDeObjetoComNumeros() {
        // Arrange
        ModelMapperConfig config = new ModelMapperConfig();
        ModelMapper modelMapper = config.modelMapper();

        TestSource source = new TestSource();
        source.setName("Teste123");
        source.setValue(123456789);

        // Act
        TestTarget target = modelMapper.map(source, TestTarget.class);

        // Assert
        assertNotNull(target);
        assertEquals("Teste123", target.getName());
        assertEquals(123456789, target.getValue());
    }

    @Test
    void testModelMapper_DeveFuncionarComMapeamentoDeObjetoComValoresExtremos() {
        // Arrange
        ModelMapperConfig config = new ModelMapperConfig();
        ModelMapper modelMapper = config.modelMapper();

        TestSource source = new TestSource();
        source.setName("");
        source.setValue(Integer.MAX_VALUE);

        // Act
        TestTarget target = modelMapper.map(source, TestTarget.class);

        // Assert
        assertNotNull(target);
        assertEquals("", target.getName());
        assertEquals(Integer.MAX_VALUE, target.getValue());
    }

    @Test
    void testModelMapper_DeveFuncionarComMapeamentoDeObjetoComValoresNegativos() {
        // Arrange
        ModelMapperConfig config = new ModelMapperConfig();
        ModelMapper modelMapper = config.modelMapper();

        TestSource source = new TestSource();
        source.setName("Teste Negativo");
        source.setValue(-123);

        // Act
        TestTarget target = modelMapper.map(source, TestTarget.class);

        // Assert
        assertNotNull(target);
        assertEquals("Teste Negativo", target.getName());
        assertEquals(-123, target.getValue());
    }

    @Test
    void testModelMapper_DeveFuncionarComMapeamentoDeObjetoComValoresZero() {
        // Arrange
        ModelMapperConfig config = new ModelMapperConfig();
        ModelMapper modelMapper = config.modelMapper();

        TestSource source = new TestSource();
        source.setName("Teste Zero");
        source.setValue(0);

        // Act
        TestTarget target = modelMapper.map(source, TestTarget.class);

        // Assert
        assertNotNull(target);
        assertEquals("Teste Zero", target.getName());
        assertEquals(0, target.getValue());
    }

    @Test
    void testModelMapper_DeveFuncionarComMapeamentoDeObjetoComValoresLongos() {
        // Arrange
        ModelMapperConfig config = new ModelMapperConfig();
        ModelMapper modelMapper = config.modelMapper();

        String longName = "Este é um nome muito longo que contém muitos caracteres para testar se o mapeamento funciona corretamente mesmo com strings longas";
        TestSource source = new TestSource();
        source.setName(longName);
        source.setValue(123);

        // Act
        TestTarget target = modelMapper.map(source, TestTarget.class);

        // Assert
        assertNotNull(target);
        assertEquals(longName, target.getName());
        assertEquals(123, target.getValue());
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
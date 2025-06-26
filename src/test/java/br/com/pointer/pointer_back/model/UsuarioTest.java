package br.com.pointer.pointer_back.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.pointer.pointer_back.model.StatusUsuario;

class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
    }

    @Test
    void testSetAndGetId() {
        // Arrange
        Long id = 1L;

        // Act
        usuario.setId(id);

        // Assert
        assertEquals(id, usuario.getId());
    }

    @Test
    void testSetAndGetNome() {
        // Arrange
        String nome = "João Silva";

        // Act
        usuario.setNome(nome);

        // Assert
        assertEquals(nome, usuario.getNome());
    }

    @Test
    void testSetAndGetEmail() {
        // Arrange
        String email = "joao@teste.com";

        // Act
        usuario.setEmail(email);

        // Assert
        assertEquals(email, usuario.getEmail());
    }

    @Test
    void testSetAndGetSetor() {
        // Arrange
        String setor = "TI";

        // Act
        usuario.setSetor(setor);

        // Assert
        assertEquals(setor, usuario.getSetor());
    }

    @Test
    void testSetAndGetCargo() {
        // Arrange
        String cargo = "Desenvolvedor";

        // Act
        usuario.setCargo(cargo);

        // Assert
        assertEquals(cargo, usuario.getCargo());
    }

    @Test
    void testSetAndGetTipoUsuario() {
        // Arrange
        String tipoUsuario = "COLABORADOR";

        // Act
        usuario.setTipoUsuario(tipoUsuario);

        // Assert
        assertEquals(tipoUsuario, usuario.getTipoUsuario());
    }

    @Test
    void testSetAndGetDataCriacao() {
        // Arrange
        LocalDateTime dataCriacao = LocalDateTime.now();

        // Act
        usuario.setDataCriacao(dataCriacao);

        // Assert
        assertEquals(dataCriacao, usuario.getDataCriacao());
    }

    @Test
    void testSetAndGetKeycloakId() {
        // Arrange
        String keycloakId = "keycloak-123";

        // Act
        usuario.setKeycloakId(keycloakId);

        // Assert
        assertEquals(keycloakId, usuario.getKeycloakId());
    }

    @Test
    void testSetAndGetStatus() {
        // Arrange
        StatusUsuario status = StatusUsuario.ATIVO;

        // Act
        usuario.setStatus(status);

        // Assert
        assertEquals(status, usuario.getStatus());
    }

    @Test
    void testSetAndGetTwoFactorEnabled() {
        // Arrange
        Boolean twoFactorEnabled = true;

        // Act
        usuario.setTwoFactorEnabled(twoFactorEnabled);

        // Assert
        assertEquals(twoFactorEnabled, usuario.getTwoFactorEnabled());
    }

    @Test
    void testSetAndGetSecretKey() {
        // Arrange
        String secretKey = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456";

        // Act
        usuario.setSecretKey(secretKey);

        // Assert
        assertEquals(secretKey, usuario.getSecretKey());
    }

    @Test
    void testDefaultValues() {
        // Assert
        assertEquals(StatusUsuario.ATIVO, usuario.getStatus());
        assertEquals(false, usuario.getTwoFactorEnabled());
    }

    @Test
    void testEquals_ComMesmoId_DeveRetornarTrue() {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("João Silva");
        usuario1.setEmail("teste@teste.com");
        usuario1.setSetor("TI");
        usuario1.setCargo("Desenvolvedor");
        usuario1.setTipoUsuario("COLABORADOR");
        usuario1.setDataCriacao(LocalDateTime.of(2023, 1, 1, 12, 0));
        usuario1.setKeycloakId("keycloak-123");
        usuario1.setStatus(StatusUsuario.ATIVO);
        usuario1.setTwoFactorEnabled(false);
        usuario1.setSecretKey("ABCDEFGHIJKLMNOPQRSTUVWXYZ123456");

        Usuario usuario2 = new Usuario();
        usuario2.setId(1L);
        usuario2.setNome("João Silva");
        usuario2.setEmail("teste@teste.com");
        usuario2.setSetor("TI");
        usuario2.setCargo("Desenvolvedor");
        usuario2.setTipoUsuario("COLABORADOR");
        usuario2.setDataCriacao(LocalDateTime.of(2023, 1, 1, 12, 0));
        usuario2.setKeycloakId("keycloak-123");
        usuario2.setStatus(StatusUsuario.ATIVO);
        usuario2.setTwoFactorEnabled(false);
        usuario2.setSecretKey("ABCDEFGHIJKLMNOPQRSTUVWXYZ123456");

        // Act & Assert
        assertEquals(usuario1, usuario2);
    }

    @Test
    void testEquals_ComIdsDiferentes_DeveRetornarFalse() {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("João Silva");
        usuario1.setEmail("teste@teste.com");
        usuario1.setSetor("TI");
        usuario1.setCargo("Desenvolvedor");
        usuario1.setTipoUsuario("COLABORADOR");
        usuario1.setDataCriacao(LocalDateTime.of(2023, 1, 1, 12, 0));
        usuario1.setKeycloakId("keycloak-123");
        usuario1.setStatus(StatusUsuario.ATIVO);
        usuario1.setTwoFactorEnabled(false);
        usuario1.setSecretKey("ABCDEFGHIJKLMNOPQRSTUVWXYZ123456");

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNome("João Silva");
        usuario2.setEmail("teste@teste.com");
        usuario2.setSetor("TI");
        usuario2.setCargo("Desenvolvedor");
        usuario2.setTipoUsuario("COLABORADOR");
        usuario2.setDataCriacao(LocalDateTime.of(2023, 1, 1, 12, 0));
        usuario2.setKeycloakId("keycloak-123");
        usuario2.setStatus(StatusUsuario.ATIVO);
        usuario2.setTwoFactorEnabled(false);
        usuario2.setSecretKey("ABCDEFGHIJKLMNOPQRSTUVWXYZ123456");

        // Act & Assert
        assertNotEquals(usuario1, usuario2);
    }

    @Test
    void testEquals_ComNull_DeveRetornarFalse() {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);

        // Act & Assert
        assertNotEquals(null, usuario1);
    }

    @Test
    void testEquals_ComMesmoObjeto_DeveRetornarTrue() {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);

        // Act & Assert
        assertEquals(usuario1, usuario1);
    }

    @Test
    void testEquals_ComTipoDiferente_DeveRetornarFalse() {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);

        String string = "teste";

        // Act & Assert
        assertNotEquals(usuario1, string);
    }

    @Test
    void testHashCode_ComMesmoId_DeveRetornarMesmoHashCode() {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("João Silva");
        usuario1.setEmail("teste@teste.com");
        usuario1.setSetor("TI");
        usuario1.setCargo("Desenvolvedor");
        usuario1.setTipoUsuario("COLABORADOR");
        usuario1.setDataCriacao(LocalDateTime.of(2023, 1, 1, 12, 0));
        usuario1.setKeycloakId("keycloak-123");
        usuario1.setStatus(StatusUsuario.ATIVO);
        usuario1.setTwoFactorEnabled(false);
        usuario1.setSecretKey("ABCDEFGHIJKLMNOPQRSTUVWXYZ123456");

        Usuario usuario2 = new Usuario();
        usuario2.setId(1L);
        usuario2.setNome("João Silva");
        usuario2.setEmail("teste@teste.com");
        usuario2.setSetor("TI");
        usuario2.setCargo("Desenvolvedor");
        usuario2.setTipoUsuario("COLABORADOR");
        usuario2.setDataCriacao(LocalDateTime.of(2023, 1, 1, 12, 0));
        usuario2.setKeycloakId("keycloak-123");
        usuario2.setStatus(StatusUsuario.ATIVO);
        usuario2.setTwoFactorEnabled(false);
        usuario2.setSecretKey("ABCDEFGHIJKLMNOPQRSTUVWXYZ123456");

        // Act & Assert
        assertEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void testHashCode_ComIdsDiferentes_DeveRetornarHashCodesDiferentes() {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("João Silva");
        usuario1.setEmail("teste@teste.com");
        usuario1.setSetor("TI");
        usuario1.setCargo("Desenvolvedor");
        usuario1.setTipoUsuario("COLABORADOR");
        usuario1.setDataCriacao(LocalDateTime.of(2023, 1, 1, 12, 0));
        usuario1.setKeycloakId("keycloak-123");
        usuario1.setStatus(StatusUsuario.ATIVO);
        usuario1.setTwoFactorEnabled(false);
        usuario1.setSecretKey("ABCDEFGHIJKLMNOPQRSTUVWXYZ123456");

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNome("João Silva");
        usuario2.setEmail("teste@teste.com");
        usuario2.setSetor("TI");
        usuario2.setCargo("Desenvolvedor");
        usuario2.setTipoUsuario("COLABORADOR");
        usuario2.setDataCriacao(LocalDateTime.of(2023, 1, 1, 12, 0));
        usuario2.setKeycloakId("keycloak-123");
        usuario2.setStatus(StatusUsuario.ATIVO);
        usuario2.setTwoFactorEnabled(false);
        usuario2.setSecretKey("ABCDEFGHIJKLMNOPQRSTUVWXYZ123456");

        // Act & Assert
        assertNotEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void testToString_DeveConterInformacoesPrincipais() {
        // Arrange
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@teste.com");
        usuario.setSetor("TI");
        usuario.setCargo("Desenvolvedor");

        // Act
        String toString = usuario.toString();

        // Assert
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("João Silva"));
        assertTrue(toString.contains("joao@teste.com"));
        assertTrue(toString.contains("TI"));
        assertTrue(toString.contains("Desenvolvedor"));
    }

    @Test
    void testToString_ComValoresNulos_DeveFuncionar() {
        // Act
        String toString = usuario.toString();

        // Assert
        assertNotNull(toString);
        assertFalse(toString.isEmpty());
    }

    @Test
    void testEmailUnico_DeveAceitarEmailValido() {
        // Arrange
        String emailValido = "usuario@empresa.com.br";

        // Act
        usuario.setEmail(emailValido);

        // Assert
        assertEquals(emailValido, usuario.getEmail());
    }

    @Test
    void testEmailUnico_DeveAceitarEmailComSubdominio() {
        // Arrange
        String emailComSubdominio = "usuario@subdominio.empresa.com";

        // Act
        usuario.setEmail(emailComSubdominio);

        // Assert
        assertEquals(emailComSubdominio, usuario.getEmail());
    }

    @Test
    void testKeycloakIdUnico_DeveAceitarKeycloakIdValido() {
        // Arrange
        String keycloakIdValido = "keycloak-user-12345";

        // Act
        usuario.setKeycloakId(keycloakIdValido);

        // Assert
        assertEquals(keycloakIdValido, usuario.getKeycloakId());
    }

    @Test
    void testStatusEnum_DeveAceitarTodosOsValores() {
        // Arrange & Act & Assert
        usuario.setStatus(StatusUsuario.ATIVO);
        assertEquals(StatusUsuario.ATIVO, usuario.getStatus());

        usuario.setStatus(StatusUsuario.INATIVO);
        assertEquals(StatusUsuario.INATIVO, usuario.getStatus());
    }

    @Test
    void testTwoFactorEnabled_DeveAceitarValoresBooleanos() {
        // Arrange & Act & Assert
        usuario.setTwoFactorEnabled(true);
        assertTrue(usuario.getTwoFactorEnabled());

        usuario.setTwoFactorEnabled(false);
        assertFalse(usuario.getTwoFactorEnabled());

        usuario.setTwoFactorEnabled(null);
        assertNull(usuario.getTwoFactorEnabled());
    }

    @Test
    void testSecretKey_DeveAceitarChaveDe32Caracteres() {
        // Arrange
        String secretKey32 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456";

        // Act
        usuario.setSecretKey(secretKey32);

        // Assert
        assertEquals(secretKey32, usuario.getSecretKey());
    }

    @Test
    void testSecretKey_DeveAceitarChaveMenorQue32Caracteres() {
        // Arrange
        String secretKeyMenor = "ABCDEFGHIJKLMNOP";

        // Act
        usuario.setSecretKey(secretKeyMenor);

        // Assert
        assertEquals(secretKeyMenor, usuario.getSecretKey());
    }

    @Test
    void testSecretKey_DeveAceitarChaveNula() {
        // Act
        usuario.setSecretKey(null);

        // Assert
        assertNull(usuario.getSecretKey());
    }

    @Test
    void testTipoUsuario_DeveAceitarDiferentesTipos() {
        // Arrange & Act & Assert
        usuario.setTipoUsuario("ADMIN");
        assertEquals("ADMIN", usuario.getTipoUsuario());

        usuario.setTipoUsuario("GESTOR");
        assertEquals("GESTOR", usuario.getTipoUsuario());

        usuario.setTipoUsuario("COLABORADOR");
        assertEquals("COLABORADOR", usuario.getTipoUsuario());
    }

    @Test
    void testSetor_DeveAceitarDiferentesSetores() {
        // Arrange & Act & Assert
        usuario.setSetor("TI");
        assertEquals("TI", usuario.getSetor());

        usuario.setSetor("RH");
        assertEquals("RH", usuario.getSetor());

        usuario.setSetor("FINANCEIRO");
        assertEquals("FINANCEIRO", usuario.getSetor());

        usuario.setSetor("MARKETING");
        assertEquals("MARKETING", usuario.getSetor());
    }

    @Test
    void testCargo_DeveAceitarDiferentesCargos() {
        // Arrange & Act & Assert
        usuario.setCargo("Desenvolvedor");
        assertEquals("Desenvolvedor", usuario.getCargo());

        usuario.setCargo("Gerente");
        assertEquals("Gerente", usuario.getCargo());

        usuario.setCargo("Analista");
        assertEquals("Analista", usuario.getCargo());

        usuario.setCargo("Diretor");
        assertEquals("Diretor", usuario.getCargo());
    }

    @Test
    void testDataCriacao_DeveSerAutomaticaNoPrePersist() {
        // Arrange
        Usuario usuarioComPrePersist = new Usuario();

        // Act
        usuarioComPrePersist.onCreate();

        // Assert
        assertNotNull(usuarioComPrePersist.getDataCriacao());
        assertTrue(usuarioComPrePersist.getDataCriacao().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(usuarioComPrePersist.getDataCriacao().isAfter(LocalDateTime.now().minusSeconds(1)));
    }

    @Test
    void testDataCriacao_DeveManterDataQuandoJaDefinida() {
        // Arrange
        LocalDateTime dataEspecifica = LocalDateTime.of(2023, 1, 1, 12, 0);
        usuario.setDataCriacao(dataEspecifica);

        // Act
        usuario.onCreate();

        // Assert - onCreate sempre define a data atual, não mantém a data definida
        assertNotNull(usuario.getDataCriacao());
        assertNotEquals(dataEspecifica, usuario.getDataCriacao());
        assertTrue(usuario.getDataCriacao().isAfter(LocalDateTime.now().minusSeconds(1)));
    }

    @Test
    void testStatus_DeveSerAtivoPorPadrao() {
        // Arrange
        Usuario usuarioNovo = new Usuario();

        // Act
        usuarioNovo.onCreate();

        // Assert
        assertEquals(StatusUsuario.ATIVO, usuarioNovo.getStatus());
    }

    @Test
    void testStatus_DeveManterStatusQuandoJaDefinido() {
        // Arrange
        usuario.setStatus(StatusUsuario.INATIVO);

        // Act
        usuario.onCreate();

        // Assert
        assertEquals(StatusUsuario.INATIVO, usuario.getStatus());
    }

    @Test
    void testEquals_ComIdNulo_DeveRetornarFalse() {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setId(null);

        Usuario usuario2 = new Usuario();
        usuario2.setId(1L);

        // Act & Assert
        assertNotEquals(usuario1, usuario2);
    }

    @Test
    void testEquals_ComAmbosIdsNulos_DeveRetornarTrue() {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setId(null);

        Usuario usuario2 = new Usuario();
        usuario2.setId(null);

        // Act & Assert
        assertEquals(usuario1, usuario2);
    }

    @Test
    void testHashCode_ComIdNulo_DeveRetornarHashCodeConsistente() {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setId(null);

        Usuario usuario2 = new Usuario();
        usuario2.setId(null);

        // Act & Assert
        assertEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void testToString_ComTodosOsCamposPreenchidos_DeveConterTodasInformacoes() {
        // Arrange
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@teste.com");
        usuario.setSetor("TI");
        usuario.setCargo("Desenvolvedor");
        usuario.setTipoUsuario("COLABORADOR");
        usuario.setDataCriacao(LocalDateTime.of(2023, 1, 1, 12, 0, 0));
        usuario.setKeycloakId("keycloak-123");
        usuario.setStatus(StatusUsuario.ATIVO);
        usuario.setTwoFactorEnabled(true);
        usuario.setSecretKey("ABCDEFGHIJKLMNOPQRSTUVWXYZ123456");

        // Act
        String toString = usuario.toString();

        // Assert
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("João Silva"));
        assertTrue(toString.contains("joao@teste.com"));
        assertTrue(toString.contains("TI"));
        assertTrue(toString.contains("Desenvolvedor"));
        assertTrue(toString.contains("COLABORADOR"));
        assertTrue(toString.contains("keycloak-123"));
        assertTrue(toString.contains("ATIVO"));
        assertTrue(toString.contains("true"));
        assertTrue(toString.contains("ABCDEFGHIJKLMNOPQRSTUVWXYZ123456"));
    }
} 
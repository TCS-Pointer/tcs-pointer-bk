package br.com.pointer.pointer_back.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import br.com.pointer.pointer_back.exception.EmailInvalidoException;
import br.com.pointer.pointer_back.exception.KeycloakException;
import br.com.pointer.pointer_back.exception.SenhaInvalidaException;

class ValidationUtilTest {

    @Test
    void testValidarNome_ComNomeValido_DevePassar() {
        // Arrange
        String nome = "João Silva";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarNome(nome);
        });
    }

    @Test
    void testValidarNome_ComNomeComTresPartes_DevePassar() {
        // Arrange
        String nome = "João Silva Santos";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarNome(nome);
        });
    }

    @Test
    void testValidarNome_ComNomeComAcentos_DevePassar() {
        // Arrange
        String nome = "João Silva";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarNome(nome);
        });
    }

    @Test
    void testValidarNome_ComNomeComCedilha_DevePassar() {
        // Arrange
        String nome = "François Silva";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarNome(nome);
        });
    }

    @Test
    void testValidarNome_ComNomeNulo_DeveLancarExcecao() {
        // Arrange
        String nome = null;

        // Act & Assert
        assertThrows(KeycloakException.class, () -> {
            ValidationUtil.validarNome(nome);
        });
    }

    @Test
    void testValidarNome_ComNomeVazio_DeveLancarExcecao() {
        // Arrange
        String nome = "";

        // Act & Assert
        assertThrows(KeycloakException.class, () -> {
            ValidationUtil.validarNome(nome);
        });
    }

    @Test
    void testValidarNome_ComNomeComEspacosApenas_DeveLancarExcecao() {
        // Arrange
        String nome = "   ";

        // Act & Assert
        assertThrows(KeycloakException.class, () -> {
            ValidationUtil.validarNome(nome);
        });
    }

    @Test
    void testValidarNome_ComApenasUmaPalavra_DeveLancarExcecao() {
        // Arrange
        String nome = "João";

        // Act & Assert
        assertThrows(KeycloakException.class, () -> {
            ValidationUtil.validarNome(nome);
        });
    }

    @Test
    void testValidarNome_ComParteComMenosDe2Caracteres_DeveLancarExcecao() {
        // Arrange
        String nome = "João A Silva";

        // Act & Assert
        assertThrows(KeycloakException.class, () -> {
            ValidationUtil.validarNome(nome);
        });
    }

    @Test
    void testValidarNome_ComNomeComMenosDe5Caracteres_DeveLancarExcecao() {
        // Arrange
        String nome = "Jo Si";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarNome(nome);
        });
    }

    @Test
    void testValidarNome_ComNomeComMenosDe5Caracteres_DeveLancarExcecao2() {
        // Arrange
        String nome = "Jo S";

        // Act & Assert
        assertThrows(KeycloakException.class, () -> {
            ValidationUtil.validarNome(nome);
        });
    }

    @Test
    void testValidarNome_ComCaracteresEspeciais_DeveLancarExcecao() {
        // Arrange
        String nome = "João Silva-Santos";

        // Act & Assert
        assertThrows(KeycloakException.class, () -> {
            ValidationUtil.validarNome(nome);
        });
    }

    @Test
    void testValidarNome_ComNumeros_DeveLancarExcecao() {
        // Arrange
        String nome = "João Silva 123";

        // Act & Assert
        assertThrows(KeycloakException.class, () -> {
            ValidationUtil.validarNome(nome);
        });
    }

    @Test
    void testValidarNome_ComPontuacao_DeveLancarExcecao() {
        // Arrange
        String nome = "João Silva, Jr.";

        // Act & Assert
        assertThrows(KeycloakException.class, () -> {
            ValidationUtil.validarNome(nome);
        });
    }

    @Test
    void testValidarEmail_ComEmailValido_DevePassar() {
        // Arrange
        String email = "joao.silva@empresa.com";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarEmail(email);
        });
    }

    @Test
    void testValidarEmail_ComEmailComSubdominio_DevePassar() {
        // Arrange
        String email = "joao.silva@subdominio.empresa.com";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarEmail(email);
        });
    }

    @Test
    void testValidarEmail_ComEmailComCaracteresEspeciais_DevePassar() {
        // Arrange
        String email = "joao.silva+tag@empresa.com";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarEmail(email);
        });
    }

    @Test
    void testValidarEmail_ComEmailComHifen_DevePassar() {
        // Arrange
        String email = "joao-silva@empresa.com";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarEmail(email);
        });
    }

    @Test
    void testValidarEmail_ComEmailComPonto_DevePassar() {
        // Arrange
        String email = "joao.silva@empresa.com";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarEmail(email);
        });
    }

    @Test
    void testValidarEmail_ComEmailNulo_DeveLancarExcecao() {
        // Arrange
        String email = null;

        // Act & Assert
        assertThrows(EmailInvalidoException.class, () -> {
            ValidationUtil.validarEmail(email);
        });
    }

    @Test
    void testValidarEmail_ComEmailVazio_DeveLancarExcecao() {
        // Arrange
        String email = "";

        // Act & Assert
        assertThrows(EmailInvalidoException.class, () -> {
            ValidationUtil.validarEmail(email);
        });
    }

    @Test
    void testValidarEmail_ComEmailSemArroba_DeveLancarExcecao() {
        // Arrange
        String email = "joao.silva.empresa.com";

        // Act & Assert
        assertThrows(EmailInvalidoException.class, () -> {
            ValidationUtil.validarEmail(email);
        });
    }

    @Test
    void testValidarEmail_ComEmailSemDominio_DeveLancarExcecao() {
        // Arrange
        String email = "joao.silva@";

        // Act & Assert
        assertThrows(EmailInvalidoException.class, () -> {
            ValidationUtil.validarEmail(email);
        });
    }

    @Test
    void testValidarEmail_ComEmailSemUsuario_DeveLancarExcecao() {
        // Arrange
        String email = "@empresa.com";

        // Act & Assert
        assertThrows(EmailInvalidoException.class, () -> {
            ValidationUtil.validarEmail(email);
        });
    }

    @Test
    void testValidarSenha_ComSenhaValida_DevePassar() {
        // Arrange
        String senha = "senha123";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarSenha(senha);
        });
    }

    @Test
    void testValidarSenha_ComSenhaCom8Caracteres_DevePassar() {
        // Arrange
        String senha = "12345678";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarSenha(senha);
        });
    }

    @Test
    void testValidarSenha_ComSenhaComMaisDe8Caracteres_DevePassar() {
        // Arrange
        String senha = "senha123456";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarSenha(senha);
        });
    }

    @Test
    void testValidarSenha_ComSenhaNula_DeveLancarExcecao() {
        // Arrange
        String senha = null;

        // Act & Assert
        assertThrows(SenhaInvalidaException.class, () -> {
            ValidationUtil.validarSenha(senha);
        });
    }

    @Test
    void testValidarSenha_ComSenhaComMenosDe8Caracteres_DeveLancarExcecao() {
        // Arrange
        String senha = "1234567";

        // Act & Assert
        assertThrows(SenhaInvalidaException.class, () -> {
            ValidationUtil.validarSenha(senha);
        });
    }

    @Test
    void testValidarSenha_ComSenhaVazia_DeveLancarExcecao() {
        // Arrange
        String senha = "";

        // Act & Assert
        assertThrows(SenhaInvalidaException.class, () -> {
            ValidationUtil.validarSenha(senha);
        });
    }

    @Test
    void testValidarUserId_ComUserIdValido_DevePassar() {
        // Arrange
        String userId = "user123";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarUserId(userId);
        });
    }

    @Test
    void testValidarUserId_ComUserIdComEspacos_DevePassar() {
        // Arrange
        String userId = " user123 ";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarUserId(userId);
        });
    }

    @Test
    void testValidarUserId_ComUserIdNulo_DeveLancarExcecao() {
        // Arrange
        String userId = null;

        // Act & Assert
        assertThrows(KeycloakException.class, () -> {
            ValidationUtil.validarUserId(userId);
        });
    }

    @Test
    void testValidarUserId_ComUserIdVazio_DeveLancarExcecao() {
        // Arrange
        String userId = "";

        // Act & Assert
        assertThrows(KeycloakException.class, () -> {
            ValidationUtil.validarUserId(userId);
        });
    }

    @Test
    void testValidarUserId_ComUserIdComEspacosApenas_DeveLancarExcecao() {
        // Arrange
        String userId = "   ";

        // Act & Assert
        assertThrows(KeycloakException.class, () -> {
            ValidationUtil.validarUserId(userId);
        });
    }

    @Test
    void testValidarRoles_ComRolesValidas_DevePassar() {
        // Arrange
        Set<String> roles = new HashSet<>();
        roles.add("ADMIN");
        roles.add("USER");

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarRoles(roles);
        });
    }

    @Test
    void testValidarRoles_ComUmaRole_DevePassar() {
        // Arrange
        Set<String> roles = new HashSet<>();
        roles.add("USER");

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarRoles(roles);
        });
    }

    @Test
    void testValidarRoles_ComRolesNulas_DeveLancarExcecao() {
        // Arrange
        Set<String> roles = null;

        // Act & Assert
        assertThrows(KeycloakException.class, () -> {
            ValidationUtil.validarRoles(roles);
        });
    }

    @Test
    void testValidarRoles_ComRolesVazias_DeveLancarExcecao() {
        // Arrange
        Set<String> roles = new HashSet<>();

        // Act & Assert
        assertThrows(KeycloakException.class, () -> {
            ValidationUtil.validarRoles(roles);
        });
    }

    @Test
    void testValidarSenhaComplexa_ComSenhaValida_DevePassar() {
        // Arrange
        String senha = "Senha123!";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarSenhaComplexa(senha);
        });
    }

    @Test
    void testValidarSenhaComplexa_ComSenhaComTodosOsRequisitos_DevePassar() {
        // Arrange
        String senha = "MinhaSenha123@";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarSenhaComplexa(senha);
        });
    }

    @Test
    void testValidarSenhaComplexa_ComSenhaNula_DeveLancarExcecao() {
        // Arrange
        String senha = null;

        // Act & Assert
        assertThrows(SenhaInvalidaException.class, () -> {
            ValidationUtil.validarSenhaComplexa(senha);
        });
    }

    @Test
    void testValidarSenhaComplexa_ComSenhaComMenosDe8Caracteres_DeveLancarExcecao() {
        // Arrange
        String senha = "Senha1!";

        // Act & Assert
        assertThrows(SenhaInvalidaException.class, () -> {
            ValidationUtil.validarSenhaComplexa(senha);
        });
    }

    @Test
    void testValidarSenhaComplexa_ComSenhaSemMaiuscula_DeveLancarExcecao() {
        // Arrange
        String senha = "senha123!";

        // Act & Assert
        assertThrows(SenhaInvalidaException.class, () -> {
            ValidationUtil.validarSenhaComplexa(senha);
        });
    }

    @Test
    void testValidarSenhaComplexa_ComSenhaSemNumero_DeveLancarExcecao() {
        // Arrange
        String senha = "SenhaABC!";

        // Act & Assert
        assertThrows(SenhaInvalidaException.class, () -> {
            ValidationUtil.validarSenhaComplexa(senha);
        });
    }

    @Test
    void testValidarSenhaComplexa_ComSenhaSemCaractereEspecial_DeveLancarExcecao() {
        // Arrange
        String senha = "Senha123";

        // Act & Assert
        assertThrows(SenhaInvalidaException.class, () -> {
            ValidationUtil.validarSenhaComplexa(senha);
        });
    }

    @Test
    void testValidarSenhaComplexa_ComSenhaComDiferentesCaracteresEspeciais_DevePassar() {
        // Arrange
        String[] senhas = {
            "Senha123!",
            "Senha123@",
            "Senha123#",
            "Senha123$",
            "Senha123%",
            "Senha123^",
            "Senha123&",
            "Senha123*",
            "Senha123(",
            "Senha123)"
        };

        // Act & Assert
        for (String senha : senhas) {
            assertDoesNotThrow(() -> {
                ValidationUtil.validarSenhaComplexa(senha);
            });
        }
    }

    @Test
    void testValidarNome_ComNomeComEspacosExtras_DevePassar() {
        // Arrange
        String nome = "  João   Silva  ";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarNome(nome);
        });
    }

    @Test
    void testValidarNome_ComNomeComTil_DevePassar() {
        // Arrange
        String nome = "João Silva";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarNome(nome);
        });
    }

    @Test
    void testValidarEmail_ComEmailComUnderscore_DevePassar() {
        // Arrange
        String email = "joao_silva@empresa.com";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarEmail(email);
        });
    }

    @Test
    void testValidarEmail_ComEmailComMais_DevePassar() {
        // Arrange
        String email = "joao+silva@empresa.com";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarEmail(email);
        });
    }

    @Test
    void testValidarEmail_ComEmailComPontoNoUsuario_DevePassar() {
        // Arrange
        String email = "joao.silva@empresa.com";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarEmail(email);
        });
    }

    @Test
    void testValidarEmail_ComEmailComHifenNoUsuario_DevePassar() {
        // Arrange
        String email = "joao-silva@empresa.com";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarEmail(email);
        });
    }

    @Test
    void testValidarSenhaComplexa_ComSenhaCom8CaracteresExatos_DevePassar() {
        // Arrange
        String senha = "Senha1!@";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarSenhaComplexa(senha);
        });
    }

    @Test
    void testValidarSenhaComplexa_ComSenhaComCaracteresEspeciaisNoMeio_DevePassar() {
        // Arrange
        String senha = "Senha!123";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarSenhaComplexa(senha);
        });
    }

    @Test
    void testValidarSenhaComplexa_ComSenhaComNumerosNoMeio_DevePassar() {
        // Arrange
        String senha = "Senha!123";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarSenhaComplexa(senha);
        });
    }

    @Test
    void testValidarSenhaComplexa_ComSenhaComMaiusculaNoMeio_DevePassar() {
        // Arrange
        String senha = "senha!123A";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarSenhaComplexa(senha);
        });
    }

    @Test
    void testValidarRoles_ComRolesComEspacos_DevePassar() {
        // Arrange
        Set<String> roles = new HashSet<>();
        roles.add(" ADMIN ");
        roles.add(" USER ");

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarRoles(roles);
        });
    }

    @Test
    void testValidarRoles_ComRolesComStringVazia_DevePassar() {
        // Arrange
        Set<String> roles = new HashSet<>();
        roles.add("");
        roles.add("USER");

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarRoles(roles);
        });
    }

    @Test
    void testValidarUserId_ComUserIdComCaracteresEspeciais_DevePassar() {
        // Arrange
        String userId = "user-123_456";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarUserId(userId);
        });
    }

    @Test
    void testValidarUserId_ComUserIdComNumeros_DevePassar() {
        // Arrange
        String userId = "123456";

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validarUserId(userId);
        });
    }
} 
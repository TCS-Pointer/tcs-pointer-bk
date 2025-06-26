package br.com.pointer.pointer_back.security;

import br.com.pointer.pointer_back.constant.SecurityConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @InjectMocks
    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        // Setup inicial se necessário
    }

    @Test
    void passwordEncoder_DeveRetornarBCryptPasswordEncoder() {
        // Act
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // Assert
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    void passwordEncoder_DeveCodificarSenha() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String senhaOriginal = "senha123";

        // Act
        String senhaCodificada = passwordEncoder.encode(senhaOriginal);

        // Assert
        assertNotNull(senhaCodificada);
        assertNotEquals(senhaOriginal, senhaCodificada);
        assertTrue(senhaCodificada.startsWith("$2a$"));
    }

    @Test
    void passwordEncoder_DeveVerificarSenhaCorreta() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String senhaOriginal = "senha123";
        String senhaCodificada = passwordEncoder.encode(senhaOriginal);

        // Act
        boolean senhaCorreta = passwordEncoder.matches(senhaOriginal, senhaCodificada);

        // Assert
        assertTrue(senhaCorreta);
    }

    @Test
    void passwordEncoder_DeveRejeitarSenhaIncorreta() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String senhaOriginal = "senha123";
        String senhaCodificada = passwordEncoder.encode(senhaOriginal);
        String senhaIncorreta = "senha456";

        // Act
        boolean senhaCorreta = passwordEncoder.matches(senhaIncorreta, senhaCodificada);

        // Assert
        assertFalse(senhaCorreta);
    }

    @Test
    void passwordEncoder_DeveGerarCodificacoesDiferentesParaMesmaSenha() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String senha = "senha123";

        // Act
        String codificacao1 = passwordEncoder.encode(senha);
        String codificacao2 = passwordEncoder.encode(senha);

        // Assert
        assertNotEquals(codificacao1, codificacao2);
        assertTrue(passwordEncoder.matches(senha, codificacao1));
        assertTrue(passwordEncoder.matches(senha, codificacao2));
    }

    @Test
    void corsConfigurationSource_DeveRetornarUrlBasedCorsConfigurationSource() {
        // Act
        CorsConfigurationSource corsConfigurationSource = securityConfig.corsConfigurationSource();

        // Assert
        assertNotNull(corsConfigurationSource);
        assertTrue(corsConfigurationSource instanceof UrlBasedCorsConfigurationSource);
    }

    @Test
    void passwordEncoder_DeveCodificarSenhaVazia() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String senhaVazia = "";

        // Act
        String senhaCodificada = passwordEncoder.encode(senhaVazia);

        // Assert
        assertNotNull(senhaCodificada);
        assertNotEquals(senhaVazia, senhaCodificada);
        assertTrue(senhaCodificada.startsWith("$2a$"));
        assertTrue(passwordEncoder.matches(senhaVazia, senhaCodificada));
    }

    @Test
    void passwordEncoder_DeveCodificarSenhaComCaracteresEspeciais() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String senhaEspecial = "senha@123#456$789%";

        // Act
        String senhaCodificada = passwordEncoder.encode(senhaEspecial);

        // Assert
        assertNotNull(senhaCodificada);
        assertNotEquals(senhaEspecial, senhaCodificada);
        assertTrue(senhaCodificada.startsWith("$2a$"));
        assertTrue(passwordEncoder.matches(senhaEspecial, senhaCodificada));
    }

    @Test
    void passwordEncoder_DeveCodificarSenhaLonga() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String senhaLonga = "Esta é uma senha muito longa com muitos caracteres para testar o comportamento do BCrypt com textos extensos";

        // Act
        String senhaCodificada = passwordEncoder.encode(senhaLonga);

        // Assert
        assertNotNull(senhaCodificada);
        assertNotEquals(senhaLonga, senhaCodificada);
        assertTrue(senhaCodificada.startsWith("$2a$"));
        assertTrue(passwordEncoder.matches(senhaLonga, senhaCodificada));
    }

    @Test
    void passwordEncoder_DeveCodificarSenhaComEspacos() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String senhaComEspacos = "senha com espaços";

        // Act
        String senhaCodificada = passwordEncoder.encode(senhaComEspacos);

        // Assert
        assertNotNull(senhaCodificada);
        assertNotEquals(senhaComEspacos, senhaCodificada);
        assertTrue(senhaCodificada.startsWith("$2a$"));
        assertTrue(passwordEncoder.matches(senhaComEspacos, senhaCodificada));
    }

    @Test
    void passwordEncoder_DeveCodificarSenhaComNumeros() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String senhaNumerica = "123456789";

        // Act
        String senhaCodificada = passwordEncoder.encode(senhaNumerica);

        // Assert
        assertNotNull(senhaCodificada);
        assertNotEquals(senhaNumerica, senhaCodificada);
        assertTrue(senhaCodificada.startsWith("$2a$"));
        assertTrue(passwordEncoder.matches(senhaNumerica, senhaCodificada));
    }

    @Test
    void passwordEncoder_DeveCodificarSenhaComLetrasMaiusculas() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String senhaMaiuscula = "SENHA123";

        // Act
        String senhaCodificada = passwordEncoder.encode(senhaMaiuscula);

        // Assert
        assertNotNull(senhaCodificada);
        assertNotEquals(senhaMaiuscula, senhaCodificada);
        assertTrue(senhaCodificada.startsWith("$2a$"));
        assertTrue(passwordEncoder.matches(senhaMaiuscula, senhaCodificada));
    }

    @Test
    void passwordEncoder_DeveCodificarSenhaComLetrasMinusculas() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String senhaMinuscula = "senha123";

        // Act
        String senhaCodificada = passwordEncoder.encode(senhaMinuscula);

        // Assert
        assertNotNull(senhaCodificada);
        assertNotEquals(senhaMinuscula, senhaCodificada);
        assertTrue(senhaCodificada.startsWith("$2a$"));
        assertTrue(passwordEncoder.matches(senhaMinuscula, senhaCodificada));
    }

    @Test
    void passwordEncoder_DeveCodificarSenhaMista() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String senhaMista = "SeNhA123@#";

        // Act
        String senhaCodificada = passwordEncoder.encode(senhaMista);

        // Assert
        assertNotNull(senhaCodificada);
        assertNotEquals(senhaMista, senhaCodificada);
        assertTrue(senhaCodificada.startsWith("$2a$"));
        assertTrue(passwordEncoder.matches(senhaMista, senhaCodificada));
    }
} 
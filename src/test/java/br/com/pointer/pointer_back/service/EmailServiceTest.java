package br.com.pointer.pointer_back.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import org.springframework.core.env.Environment;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private Environment environment;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "sendGridApiKey", "test-api-key");
        ReflectionTestUtils.setField(emailService, "frontendUrl", "http://localhost:3000");
    }

    @Test
    void sendPasswordEmail_ComDadosValidos_DeveEnviarEmail() {
        // Arrange
        EmailService spyEmailService = spy(emailService);
        
        // Act
        spyEmailService.sendPasswordEmail("teste@exemplo.com", "senha123", "João Silva");

        // Assert
        // Em ambiente de teste, o email não é realmente enviado
        // Apenas verifica se não há exceção
        assertDoesNotThrow(() -> spyEmailService.sendPasswordEmail("teste@exemplo.com", "senha123", "João Silva"));
    }

    @Test
    void verifyCode_ComEmailNulo_DeveRetornarFalse() {
        // Act
        boolean result = emailService.verifyCode(null, "123456");

        // Assert
        assertFalse(result);
    }

    @Test
    void verifyCode_ComCodigoNulo_DeveRetornarFalse() {
        // Act
        boolean result = emailService.verifyCode("teste@exemplo.com", null);

        // Assert
        assertFalse(result);
    }

    @Test
    void verifyCode_ComCodigoInvalido_DeveRetornarFalse() {
        // Act
        boolean result = emailService.verifyCode("teste@exemplo.com", "codigo-invalido");

        // Assert
        assertFalse(result);
    }


    @Test
    void removeVerificationCode_ComEmailNulo_DeveNaoFazerNada() {
        // Act & Assert
        // Não deve lançar exceção
        assertDoesNotThrow(() -> emailService.removeVerificationCode(null));
    }

    @Test
    void sendPasswordEmail_ComEmailNulo_DeveLancarExcecao() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> 
            emailService.sendPasswordEmail(null, "senha123", "João Silva"));
    }

    @Test
    void sendPrimeiroAcessoEmail_ComEmailNulo_DeveLancarExcecao() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> 
            emailService.sendPrimeiroAcessoEmail(null, "João Silva", "token123"));
    }

    @Test
    void sendTwoFactorDisabledEmail_ComEmailNulo_DeveLancarExcecao() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> 
            emailService.sendTwoFactorDisabledEmail(null, "João Silva"));
    }

    @Test
    void sendVerificationCodeEmail_ComEmailNulo_DeveLancarExcecao() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> 
            emailService.sendVerificationCodeEmail(null, "João Silva"));
    }

    @Test
    void sendPasswordEmail_ComSenhaNula_DeveLancarExcecao() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> 
            emailService.sendPasswordEmail("teste@exemplo.com", null, "João Silva"));
    }

    @Test
    void sendPrimeiroAcessoEmail_ComNomeNulo_DeveLancarExcecao() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> 
            emailService.sendPrimeiroAcessoEmail("teste@exemplo.com", null, "token123"));
    }

    @Test
    void sendTwoFactorDisabledEmail_ComNomeNulo_DeveLancarExcecao() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> 
            emailService.sendTwoFactorDisabledEmail("teste@exemplo.com", null));
    }

    @Test
    void sendVerificationCodeEmail_ComNomeNulo_DeveLancarExcecao() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> 
            emailService.sendVerificationCodeEmail("teste@exemplo.com", null));
    }
} 
package br.com.pointer.pointer_back.service;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.TwoFactorSetupDTO;
import br.com.pointer.pointer_back.dto.TwoFactorStatusDTO;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TwoFactorService")
class TwoFactorServiceTest {

    @Mock
    private GoogleAuthenticator gAuth;

    private TwoFactorService twoFactorService;

    private GoogleAuthenticatorKey mockKey;
    private String email;
    private String secretKey;
    private String qrCodeUrl;

    @BeforeEach
    void setUp() {
        email = "teste@exemplo.com";
        secretKey = "JBSWY3DPEHPK3PXP";
        qrCodeUrl = "otpauth://totp/Pointer:teste@exemplo.com?secret=JBSWY3DPEHPK3PXP&issuer=Pointer";

        // Setup mock key
        mockKey = new GoogleAuthenticatorKey.Builder(secretKey).build();
        
        // Setup service with mock
        twoFactorService = new TwoFactorService(gAuth);
    }

    @Test
    @DisplayName("Deve gerar QR code URL com sucesso")
    void deveGerarQRCodeUrlComSucesso() {
        // Given
        try (MockedStatic<GoogleAuthenticatorQRGenerator> qrGeneratorMock = mockStatic(GoogleAuthenticatorQRGenerator.class)) {
            qrGeneratorMock.when(() -> GoogleAuthenticatorQRGenerator.getOtpAuthURL(
                    eq("Pointer"), eq(email), any(GoogleAuthenticatorKey.class)))
                    .thenReturn(qrCodeUrl);

            // When
            String result = twoFactorService.generateQRCodeUrl(secretKey, email, "Pointer");

            // Then
            assertEquals(qrCodeUrl, result);
        }
    }

    @Test
    @DisplayName("Deve gerar secret key com sucesso")
    void deveGerarSecretKeyComSucesso() {
        // Arrange
        when(gAuth.createCredentials()).thenReturn(mockKey);

        // When
        String result = twoFactorService.generateSecretKey();

        // Then
        assertNotNull(result);
        assertEquals(secretKey, result);
        verify(gAuth).createCredentials();
    }

    @Test
    @DisplayName("Deve validar código 2FA com sucesso")
    void deveValidarCodigo2FAComSucesso() {
        // Given
        int code = 123456;
        when(gAuth.authorize(secretKey, code)).thenReturn(true);

        // When
        boolean result = twoFactorService.validateCode(secretKey, code);

        // Then
        assertTrue(result);
        verify(gAuth).authorize(secretKey, code);
    }

    @Test
    @DisplayName("Deve falhar na validação de código 2FA inválido")
    void deveFalharNaValidacaoCodigo2FAInvalido() {
        // Given
        int code = 000000;
        when(gAuth.authorize(secretKey, code)).thenReturn(false);

        // When
        boolean result = twoFactorService.validateCode(secretKey, code);

        // Then
        assertFalse(result);
        verify(gAuth).authorize(secretKey, code);
    }
} 
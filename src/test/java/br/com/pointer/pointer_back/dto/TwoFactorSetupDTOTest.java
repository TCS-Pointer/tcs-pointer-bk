package br.com.pointer.pointer_back.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TwoFactorSetupDTOTest {

    private TwoFactorSetupDTO setup;

    @BeforeEach
    void setUp() {
        setup = new TwoFactorSetupDTO();
    }

    @Test
    @DisplayName("Deve criar TwoFactorSetupDTO com todos os campos")
    void deveCriarTwoFactorSetupDTOComTodosOsCampos() {
        // Arrange
        setup.setQrCodeUrl("https://example.com/qr-code");
        setup.setSecretKey("JBSWY3DPEHPK3PXP");

        // Assert
        assertEquals("https://example.com/qr-code", setup.getQrCodeUrl());
        assertEquals("JBSWY3DPEHPK3PXP", setup.getSecretKey());
    }

    @Test
    @DisplayName("Deve criar TwoFactorSetupDTO com valores padrão")
    void deveCriarTwoFactorSetupDTOComValoresPadrao() {
        // Assert
        assertNull(setup.getQrCodeUrl());
        assertNull(setup.getSecretKey());
    }

    @Test
    @DisplayName("Deve atualizar campos do TwoFactorSetupDTO")
    void deveAtualizarCamposDoTwoFactorSetupDTO() {
        // Arrange
        setup.setQrCodeUrl("https://original.com/qr-code");
        setup.setSecretKey("ORIGINAL-KEY");

        // Act
        setup.setQrCodeUrl("https://updated.com/qr-code");
        setup.setSecretKey("UPDATED-KEY");

        // Assert
        assertEquals("https://updated.com/qr-code", setup.getQrCodeUrl());
        assertEquals("UPDATED-KEY", setup.getSecretKey());
    }

    @Test
    @DisplayName("Deve definir QR Code URL")
    void deveDefinirQrCodeUrl() {
        // Act
        setup.setQrCodeUrl("https://example.com/qr-code");

        // Assert
        assertEquals("https://example.com/qr-code", setup.getQrCodeUrl());
    }

    @Test
    @DisplayName("Deve definir Secret Key")
    void deveDefinirSecretKey() {
        // Act
        setup.setSecretKey("JBSWY3DPEHPK3PXP");

        // Assert
        assertEquals("JBSWY3DPEHPK3PXP", setup.getSecretKey());
    }

    @Test
    @DisplayName("Deve definir QR Code URL como null")
    void deveDefinirQrCodeUrlComoNull() {
        // Act
        setup.setQrCodeUrl(null);

        // Assert
        assertNull(setup.getQrCodeUrl());
    }

    @Test
    @DisplayName("Deve definir Secret Key como null")
    void deveDefinirSecretKeyComoNull() {
        // Act
        setup.setSecretKey(null);

        // Assert
        assertNull(setup.getSecretKey());
    }

    @Test
    @DisplayName("Deve definir QR Code URL vazia")
    void deveDefinirQrCodeUrlVazia() {
        // Act
        setup.setQrCodeUrl("");

        // Assert
        assertEquals("", setup.getQrCodeUrl());
    }

    @Test
    @DisplayName("Deve definir Secret Key vazia")
    void deveDefinirSecretKeyVazia() {
        // Act
        setup.setSecretKey("");

        // Assert
        assertEquals("", setup.getSecretKey());
    }

    @Test
    @DisplayName("Deve definir QR Code URL com caracteres especiais")
    void deveDefinirQrCodeUrlComCaracteresEspeciais() {
        // Act
        setup.setQrCodeUrl("https://example.com/qr-code?param=value&token=abc123");

        // Assert
        assertEquals("https://example.com/qr-code?param=value&token=abc123", setup.getQrCodeUrl());
    }

    @Test
    @DisplayName("Deve definir Secret Key com caracteres especiais")
    void deveDefinirSecretKeyComCaracteresEspeciais() {
        // Act
        setup.setSecretKey("JBSWY3DPEHPK3PXP!@#$%^&*()");

        // Assert
        assertEquals("JBSWY3DPEHPK3PXP!@#$%^&*()", setup.getSecretKey());
    }

    @Test
    @DisplayName("Deve definir QR Code URL com espaços")
    void deveDefinirQrCodeUrlComEspacos() {
        // Act
        setup.setQrCodeUrl("https://example.com/qr code");

        // Assert
        assertEquals("https://example.com/qr code", setup.getQrCodeUrl());
    }

    @Test
    @DisplayName("Deve definir Secret Key com espaços")
    void deveDefinirSecretKeyComEspacos() {
        // Act
        setup.setSecretKey("JBSWY3DPEHPK3PXP ");

        // Assert
        assertEquals("JBSWY3DPEHPK3PXP ", setup.getSecretKey());
    }

    @Test
    @DisplayName("Deve testar equals e hashCode com objetos iguais")
    void deveTestarEqualsEHashCodeComObjetosIguais() {
        // Arrange
        TwoFactorSetupDTO setup1 = new TwoFactorSetupDTO();
        setup1.setQrCodeUrl("https://example.com/qr-code");
        setup1.setSecretKey("JBSWY3DPEHPK3PXP");

        TwoFactorSetupDTO setup2 = new TwoFactorSetupDTO();
        setup2.setQrCodeUrl("https://example.com/qr-code");
        setup2.setSecretKey("JBSWY3DPEHPK3PXP");

        TwoFactorSetupDTO setup3 = new TwoFactorSetupDTO();
        setup3.setQrCodeUrl("https://example.com/qr-code");
        setup3.setSecretKey("JBSWY3DPEHPK3PXP");

        // Assert
        assertEquals(setup1, setup2);
        assertEquals(setup2, setup3);
        assertEquals(setup1, setup3);
        assertEquals(setup1.hashCode(), setup2.hashCode());
        assertEquals(setup2.hashCode(), setup3.hashCode());
    }

    @Test
    @DisplayName("Deve testar equals e hashCode com objetos diferentes")
    void deveTestarEqualsEHashCodeComObjetosDiferentes() {
        // Arrange
        TwoFactorSetupDTO setup1 = new TwoFactorSetupDTO();
        setup1.setQrCodeUrl("https://example1.com/qr-code");
        setup1.setSecretKey("JBSWY3DPEHPK3PXP");

        TwoFactorSetupDTO setup2 = new TwoFactorSetupDTO();
        setup2.setQrCodeUrl("https://example2.com/qr-code");
        setup2.setSecretKey("JBSWY3DPEHPK3PXP");

        TwoFactorSetupDTO setup3 = new TwoFactorSetupDTO();
        setup3.setQrCodeUrl("https://example1.com/qr-code");
        setup3.setSecretKey("DIFFERENT-KEY");

        // Assert
        assertNotEquals(setup1, setup2);
        assertNotEquals(setup1, setup3);
        assertNotEquals(setup2, setup3);
        assertNotEquals(setup1.hashCode(), setup2.hashCode());
        assertNotEquals(setup1.hashCode(), setup3.hashCode());
        assertNotEquals(setup2.hashCode(), setup3.hashCode());
    }

    @Test
    @DisplayName("Deve testar toString")
    void deveTestarToString() {
        // Arrange
        setup.setQrCodeUrl("https://example.com/qr-code");
        setup.setSecretKey("JBSWY3DPEHPK3PXP");

        // Act
        String result = setup.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("https://example.com/qr-code"));
        assertTrue(result.contains("JBSWY3DPEHPK3PXP"));
    }
} 
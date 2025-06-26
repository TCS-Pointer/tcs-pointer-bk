package br.com.pointer.pointer_back.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

@DisplayName("ConfirmarLeituraDTO")
class ConfirmarLeituraDTOTest {

    @Test
    @DisplayName("Deve criar ConfirmarLeitura com todos os campos")
    void deveCriarConfirmarLeituraComTodosOsCampos() {
        // Given
        Long comunicadoId = 1L;
        String keycloakId = "123e4567-e89b-12d3-a456-426614174000";
        
        // When
        ConfirmarLeituraDTO confirmarLeitura = new ConfirmarLeituraDTO();
        confirmarLeitura.setComunicadoId(comunicadoId);
        confirmarLeitura.setKeycloakId(keycloakId);
        
        // Then
        assertNotNull(confirmarLeitura);
        assertEquals(comunicadoId, confirmarLeitura.getComunicadoId());
        assertEquals(keycloakId, confirmarLeitura.getKeycloakId());
    }

    @Test
    @DisplayName("Deve criar ConfirmarLeitura com campos nulos")
    void deveCriarConfirmarLeituraComCamposNulos() {
        // Given
        ConfirmarLeituraDTO confirmarLeitura = new ConfirmarLeituraDTO();
        
        // When & Then
        assertNotNull(confirmarLeitura);
        assertNull(confirmarLeitura.getComunicadoId());
        assertNull(confirmarLeitura.getKeycloakId());
    }

    @Test
    @DisplayName("Deve criar ConfirmarLeitura com campos vazios")
    void deveCriarConfirmarLeituraComCamposVazios() {
        // Given
        ConfirmarLeituraDTO confirmarLeitura = new ConfirmarLeituraDTO();
        confirmarLeitura.setComunicadoId(0L);
        confirmarLeitura.setKeycloakId("");
        
        // When & Then
        assertNotNull(confirmarLeitura);
        assertEquals(0L, confirmarLeitura.getComunicadoId());
        assertEquals("", confirmarLeitura.getKeycloakId());
    }

    @Test
    @DisplayName("Deve criar ConfirmarLeitura com comunicadoId positivo")
    void deveCriarConfirmarLeituraComComunicadoIdPositivo() {
        // Given
        ConfirmarLeituraDTO confirmarLeitura = new ConfirmarLeituraDTO();
        Long comunicadoId = 123L;
        
        // When
        confirmarLeitura.setComunicadoId(comunicadoId);
        
        // Then
        assertEquals(comunicadoId, confirmarLeitura.getComunicadoId());
    }

    @Test
    @DisplayName("Deve criar ConfirmarLeitura com comunicadoId zero")
    void deveCriarConfirmarLeituraComComunicadoIdZero() {
        // Given
        ConfirmarLeituraDTO confirmarLeitura = new ConfirmarLeituraDTO();
        Long comunicadoId = 0L;
        
        // When
        confirmarLeitura.setComunicadoId(comunicadoId);
        
        // Then
        assertEquals(comunicadoId, confirmarLeitura.getComunicadoId());
    }

    @Test
    @DisplayName("Deve criar ConfirmarLeitura com comunicadoId negativo")
    void deveCriarConfirmarLeituraComComunicadoIdNegativo() {
        // Given
        ConfirmarLeituraDTO confirmarLeitura = new ConfirmarLeituraDTO();
        Long comunicadoId = -1L;
        
        // When
        confirmarLeitura.setComunicadoId(comunicadoId);
        
        // Then
        assertEquals(comunicadoId, confirmarLeitura.getComunicadoId());
    }

    @Test
    @DisplayName("Deve criar ConfirmarLeitura com keycloakId válido")
    void deveCriarConfirmarLeituraComKeycloakIdValido() {
        // Given
        ConfirmarLeituraDTO confirmarLeitura = new ConfirmarLeituraDTO();
        String keycloakId = "123e4567-e89b-12d3-a456-426614174000";
        
        // When
        confirmarLeitura.setKeycloakId(keycloakId);
        
        // Then
        assertEquals(keycloakId, confirmarLeitura.getKeycloakId());
    }

    @Test
    @DisplayName("Deve criar ConfirmarLeitura com keycloakId sem hífens")
    void deveCriarConfirmarLeituraComKeycloakIdSemHifens() {
        // Given
        ConfirmarLeituraDTO confirmarLeitura = new ConfirmarLeituraDTO();
        String keycloakId = "123e4567e89b12d3a456426614174000";
        
        // When
        confirmarLeitura.setKeycloakId(keycloakId);
        
        // Then
        assertEquals(keycloakId, confirmarLeitura.getKeycloakId());
    }

    @Test
    @DisplayName("Deve criar ConfirmarLeitura com keycloakId curto")
    void deveCriarConfirmarLeituraComKeycloakIdCurto() {
        // Given
        ConfirmarLeituraDTO confirmarLeitura = new ConfirmarLeituraDTO();
        String keycloakId = "123456";
        
        // When
        confirmarLeitura.setKeycloakId(keycloakId);
        
        // Then
        assertEquals(keycloakId, confirmarLeitura.getKeycloakId());
    }

    @Test
    @DisplayName("Deve criar ConfirmarLeitura com keycloakId longo")
    void deveCriarConfirmarLeituraComKeycloakIdLongo() {
        // Given
        ConfirmarLeituraDTO confirmarLeitura = new ConfirmarLeituraDTO();
        String keycloakId = "123e4567-e89b-12d3-a456-426614174000-123e4567-e89b-12d3-a456-426614174000";
        
        // When
        confirmarLeitura.setKeycloakId(keycloakId);
        
        // Then
        assertEquals(keycloakId, confirmarLeitura.getKeycloakId());
    }

    @Test
    @DisplayName("Deve criar ConfirmarLeitura com keycloakId com caracteres especiais")
    void deveCriarConfirmarLeituraComKeycloakIdComCaracteresEspeciais() {
        // Given
        ConfirmarLeituraDTO confirmarLeitura = new ConfirmarLeituraDTO();
        String keycloakId = "123e4567-e89b-12d3-a456-426614174000@#$%^&*()";
        
        // When
        confirmarLeitura.setKeycloakId(keycloakId);
        
        // Then
        assertEquals(keycloakId, confirmarLeitura.getKeycloakId());
    }

    @Test
    @DisplayName("Deve verificar equals e hashCode")
    void deveVerificarEqualsEHashCode() {
        // Given
        ConfirmarLeituraDTO confirmarLeitura1 = new ConfirmarLeituraDTO();
        confirmarLeitura1.setComunicadoId(1L);
        confirmarLeitura1.setKeycloakId("123e4567-e89b-12d3-a456-426614174000");
        
        ConfirmarLeituraDTO confirmarLeitura2 = new ConfirmarLeituraDTO();
        confirmarLeitura2.setComunicadoId(1L);
        confirmarLeitura2.setKeycloakId("123e4567-e89b-12d3-a456-426614174000");
        
        ConfirmarLeituraDTO confirmarLeitura3 = new ConfirmarLeituraDTO();
        confirmarLeitura3.setComunicadoId(2L);
        confirmarLeitura3.setKeycloakId("987fc654-321a-98b7-6543-987654321000");
        
        // When & Then
        assertEquals(confirmarLeitura1, confirmarLeitura2);
        assertNotEquals(confirmarLeitura1, confirmarLeitura3);
        assertEquals(confirmarLeitura1.hashCode(), confirmarLeitura2.hashCode());
        assertNotEquals(confirmarLeitura1.hashCode(), confirmarLeitura3.hashCode());
    }

    @Test
    @DisplayName("Deve verificar toString")
    void deveVerificarToString() {
        // Given
        ConfirmarLeituraDTO confirmarLeitura = new ConfirmarLeituraDTO();
        confirmarLeitura.setComunicadoId(1L);
        confirmarLeitura.setKeycloakId("123e4567-e89b-12d3-a456-426614174000");
        
        // When
        String toString = confirmarLeitura.toString();
        
        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("comunicadoId=1"));
        assertTrue(toString.contains("keycloakId=123e4567-e89b-12d3-a456-426614174000"));
    }

    @Test
    @DisplayName("Deve criar ConfirmarLeitura com valores extremos")
    void deveCriarConfirmarLeituraComValoresExtremos() {
        // Given
        ConfirmarLeituraDTO confirmarLeitura = new ConfirmarLeituraDTO();
        Long comunicadoIdMaximo = Long.MAX_VALUE;
        String keycloakIdExtremo = "A".repeat(1000);
        
        // When
        confirmarLeitura.setComunicadoId(comunicadoIdMaximo);
        confirmarLeitura.setKeycloakId(keycloakIdExtremo);
        
        // Then
        assertEquals(comunicadoIdMaximo, confirmarLeitura.getComunicadoId());
        assertEquals(keycloakIdExtremo, confirmarLeitura.getKeycloakId());
    }

    @Test
    @DisplayName("Deve criar ConfirmarLeitura com espaços em branco")
    void deveCriarConfirmarLeituraComEspacosEmBranco() {
        // Given
        ConfirmarLeituraDTO confirmarLeitura = new ConfirmarLeituraDTO();
        String keycloakIdComEspacos = "  123e4567-e89b-12d3-a456-426614174000  ";
        
        // When
        confirmarLeitura.setKeycloakId(keycloakIdComEspacos);
        
        // Then
        assertEquals(keycloakIdComEspacos, confirmarLeitura.getKeycloakId());
    }

    @Test
    @DisplayName("Deve criar ConfirmarLeitura com keycloakId com quebras de linha")
    void deveCriarConfirmarLeituraComKeycloakIdComQuebrasDeLinha() {
        // Given
        ConfirmarLeituraDTO confirmarLeitura = new ConfirmarLeituraDTO();
        String keycloakIdComQuebras = "123e4567-e89b-12d3-a456-426614174000\n123e4567-e89b-12d3-a456-426614174000";
        
        // When
        confirmarLeitura.setKeycloakId(keycloakIdComQuebras);
        
        // Then
        assertEquals(keycloakIdComQuebras, confirmarLeitura.getKeycloakId());
    }

    @Test
    @DisplayName("Deve criar ConfirmarLeitura com keycloakId com tabs")
    void deveCriarConfirmarLeituraComKeycloakIdComTabs() {
        // Given
        ConfirmarLeituraDTO confirmarLeitura = new ConfirmarLeituraDTO();
        String keycloakIdComTabs = "123e4567-e89b-12d3-a456-426614174000\t123e4567-e89b-12d3-a456-426614174000";
        
        // When
        confirmarLeitura.setKeycloakId(keycloakIdComTabs);
        
        // Then
        assertEquals(keycloakIdComTabs, confirmarLeitura.getKeycloakId());
    }

    @Test
    @DisplayName("Deve criar ConfirmarLeitura com keycloakId nulo")
    void deveCriarConfirmarLeituraComKeycloakIdNulo() {
        // Given
        ConfirmarLeituraDTO confirmarLeitura = new ConfirmarLeituraDTO();
        
        // When
        confirmarLeitura.setKeycloakId(null);
        
        // Then
        assertNull(confirmarLeitura.getKeycloakId());
    }

    @Test
    @DisplayName("Deve criar ConfirmarLeitura com comunicadoId muito grande")
    void deveCriarConfirmarLeituraComComunicadoIdMuitoGrande() {
        // Given
        ConfirmarLeituraDTO confirmarLeitura = new ConfirmarLeituraDTO();
        Long comunicadoIdMuitoGrande = 999999999999L;
        
        // When
        confirmarLeitura.setComunicadoId(comunicadoIdMuitoGrande);
        
        // Then
        assertEquals(comunicadoIdMuitoGrande, confirmarLeitura.getComunicadoId());
    }
} 
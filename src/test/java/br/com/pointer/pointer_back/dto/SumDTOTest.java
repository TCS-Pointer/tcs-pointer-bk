package br.com.pointer.pointer_back.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.math.BigDecimal;

@DisplayName("SumDTO")
class SumDTOTest {

    @Test
    @DisplayName("Deve criar SumDTO com valor positivo")
    void deveCriarSumDTOComValorPositivo() {
        SumDTO sum = new SumDTO();
        BigDecimal valor = new BigDecimal("100.50");
        sum.setSum(valor);
        assertEquals(valor, sum.getSum());
    }

    @Test
    @DisplayName("Deve criar SumDTO com valor zero")
    void deveCriarSumDTOComValorZero() {
        // Given
        SumDTO sum = new SumDTO();
        BigDecimal valor = BigDecimal.ZERO;
        
        // When
        sum.setSum(valor);
        
        // Then
        assertEquals(valor, sum.getSum());
    }

    @Test
    @DisplayName("Deve criar SumDTO com valor negativo")
    void deveCriarSumDTOComValorNegativo() {
        // Given
        SumDTO sum = new SumDTO();
        BigDecimal valor = new BigDecimal("-50.25");
        
        // When
        sum.setSum(valor);
        
        // Then
        assertEquals(valor, sum.getSum());
    }

    @Test
    @DisplayName("Deve criar SumDTO com valor nulo")
    void deveCriarSumDTOComValorNulo() {
        // Given
        SumDTO sum = new SumDTO();
        
        // When
        sum.setSum(null);
        
        // Then
        assertNull(sum.getSum());
    }

    @Test
    @DisplayName("Deve criar SumDTO com valor decimal grande")
    void deveCriarSumDTOComValorDecimalGrande() {
        // Given
        SumDTO sum = new SumDTO();
        BigDecimal valor = new BigDecimal("999999.999999");
        
        // When
        sum.setSum(valor);
        
        // Then
        assertEquals(valor, sum.getSum());
    }

    @Test
    @DisplayName("Deve verificar equals e hashCode")
    void deveVerificarEqualsEHashCode() {
        // Given
        SumDTO sum1 = new SumDTO();
        sum1.setSum(new BigDecimal("100.50"));
        
        SumDTO sum2 = new SumDTO();
        sum2.setSum(new BigDecimal("100.50"));
        
        SumDTO sum3 = new SumDTO();
        sum3.setSum(new BigDecimal("200.75"));
        
        // When & Then
        assertEquals(sum1, sum2);
        assertNotEquals(sum1, sum3);
        assertEquals(sum1.hashCode(), sum2.hashCode());
        assertNotEquals(sum1.hashCode(), sum3.hashCode());
    }

    @Test
    @DisplayName("Deve verificar toString")
    void deveVerificarToString() {
        // Given
        SumDTO sum = new SumDTO();
        sum.setSum(new BigDecimal("100.50"));
        
        // When
        String toString = sum.toString();
        
        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("sum=100.50"));
    }

    @Test
    @DisplayName("Deve criar SumDTO com construtor")
    void deveCriarSumDTOComConstrutor() {
        // Given
        BigDecimal valor = new BigDecimal("150.75");
        
        // When
        SumDTO sum = new SumDTO(valor);
        
        // Then
        assertEquals(valor, sum.getSum());
    }

    @Test
    @DisplayName("Deve criar SumDTO com construtor vazio")
    void deveCriarSumDTOComConstrutorVazio() {
        // When
        SumDTO sum = new SumDTO();
        
        // Then
        assertNotNull(sum);
    }
} 
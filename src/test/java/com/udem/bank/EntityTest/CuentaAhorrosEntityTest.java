package com.udem.bank.EntityTest;

import com.udem.bank.persistence.entity.CuentaAhorrosEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CuentaAhorrosEntityTest {

    private CuentaAhorrosEntity cuentaAhorros;

    @BeforeEach
    public void setUp() {
        cuentaAhorros = new CuentaAhorrosEntity();
    }

    @Test
    @DisplayName("Test PrePersist - Saldo Inicial")
    public void testPrePersist_SaldoInicial() {
        BigDecimal saldoEsperado = BigDecimal.ZERO;

        cuentaAhorros.prePersist();


        assertEquals(saldoEsperado, cuentaAhorros.getSaldoInicial());
    }

    @Test
    @DisplayName("Test CuentaAhorrosEntity - Construcción y Valores Iniciales")
    public void testCuentaAhorrosEntity_ConstruccionYValoresIniciales() {

        // Crear una nueva entidad CuentaAhorrosEntity

        CuentaAhorrosEntity cuentaAhorros = new CuentaAhorrosEntity();
        cuentaAhorros.setId(1);
        cuentaAhorros.setSaldoActual(BigDecimal.ZERO);
        cuentaAhorros.setSaldoInicial(BigDecimal.ZERO);
        cuentaAhorros.setCreatedAt(LocalDateTime.now());
        cuentaAhorros.setUpdatedAt(LocalDateTime.now());

        LocalDateTime now = LocalDateTime.now();


        assertNotNull(cuentaAhorros.getId());
        assertEquals(BigDecimal.ZERO, cuentaAhorros.getSaldoActual());
        assertEquals(BigDecimal.ZERO, cuentaAhorros.getSaldoInicial());
        assertNotNull(cuentaAhorros.getCreatedAt());
        assertNotNull(cuentaAhorros.getUpdatedAt());
        assertEquals(now.getYear(), cuentaAhorros.getCreatedAt().getYear());
        assertEquals(now.getYear(), cuentaAhorros.getUpdatedAt().getYear());
    }

}

package com.udem.bank.ServiceTests;

import com.udem.bank.persistence.entity.CuentaAhorrosEntity;
import com.udem.bank.persistence.repository.CuentaAhorrosRepository;
import com.udem.bank.service.CuentaAhorrosService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CuentaAhorrosServiceTest {

    @InjectMocks
    private CuentaAhorrosService cuentaAhorrosService;

    @Mock
    private CuentaAhorrosRepository cuentaAhorrosRepository;

    @Test
    public void testGetAll() {
        // Configuración del escenario para las pruebas
        List<CuentaAhorrosEntity> cuentasAhorrosList = new ArrayList<>();
        // Ejecución de la prueba
        when(cuentaAhorrosRepository.findAll()).thenReturn(cuentasAhorrosList);

        List<CuentaAhorrosEntity> result = cuentaAhorrosService.getAll();
        // Verificación si la prueba esta funcionando
        assertEquals(cuentasAhorrosList, result);
    }

    @Test
    public void testGet() {
        CuentaAhorrosEntity cuentaAhorros = new CuentaAhorrosEntity();
        when(cuentaAhorrosRepository.findById(1)).thenReturn(Optional.of(cuentaAhorros));

        CuentaAhorrosEntity result = cuentaAhorrosService.get(1);

        assertEquals(cuentaAhorros, result);
    }

    @Test
    public void testSave() {
        CuentaAhorrosEntity cuentaAhorros = new CuentaAhorrosEntity();
        when(cuentaAhorrosRepository.save(cuentaAhorros)).thenReturn(cuentaAhorros);

        CuentaAhorrosEntity result = cuentaAhorrosService.save(cuentaAhorros);

        assertEquals(cuentaAhorros, result);
    }

    @Test
    public void testExists() {
        when(cuentaAhorrosRepository.existsById(1)).thenReturn(true);

        boolean result = cuentaAhorrosService.exists(1);

        assert(result);
    }

    @Test
    public void testDeleteCuentaAhorros() {
        cuentaAhorrosService.deleteCuentaAhorros(1);

        verify(cuentaAhorrosRepository, times(1)).deleteById(1);
    }
}

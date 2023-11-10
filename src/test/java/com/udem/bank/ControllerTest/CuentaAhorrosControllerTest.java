package com.udem.bank.ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udem.bank.persistence.entity.CuentaAhorrosEntity;
import com.udem.bank.service.CuentaAhorrosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.udem.bank.web.controller.CuentaAhorrosController;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CuentaAhorrosControllerTest {

    @Mock
    private CuentaAhorrosService cuentaAhorrosService;

    @InjectMocks
    private CuentaAhorrosController cuentaAhorrosController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAllCuentasAhorros() {
        // Configuración del escenario para las pruebas

        CuentaAhorrosEntity cuenta1 = new CuentaAhorrosEntity();
        CuentaAhorrosEntity cuenta2 = new CuentaAhorrosEntity();
        List<CuentaAhorrosEntity> cuentasAhorrosMock = Arrays.asList(cuenta1, cuenta2);


        // Ejecución de la prueba
        when(cuentaAhorrosService.getAll()).thenReturn(cuentasAhorrosMock);

        ResponseEntity<List<CuentaAhorrosEntity>> response = cuentaAhorrosController.getAll();

        // Verificación si la prueba esta funcionando

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cuentasAhorrosMock, response.getBody());
    }

    @Test
    public void testGetCuentaAhorrosById() {
        int idCuentaAhorros = 1;
        CuentaAhorrosEntity cuentaAhorrosMock = new CuentaAhorrosEntity();
        cuentaAhorrosMock.setId(idCuentaAhorros);

        when(cuentaAhorrosService.get(idCuentaAhorros)).thenReturn(cuentaAhorrosMock);

        ResponseEntity<CuentaAhorrosEntity> response = cuentaAhorrosController.get(idCuentaAhorros);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cuentaAhorrosMock, response.getBody());
    }

    @Test
    public void testAddCuentaAhorros() {
        CuentaAhorrosEntity cuentaAhorrosToAdd = new CuentaAhorrosEntity();
        cuentaAhorrosToAdd.setId(1);

        when(cuentaAhorrosService.save(cuentaAhorrosToAdd)).thenReturn(cuentaAhorrosToAdd);
        when(cuentaAhorrosService.exists(cuentaAhorrosToAdd.getId())).thenReturn(false);

        ResponseEntity<CuentaAhorrosEntity> response = cuentaAhorrosController.add(cuentaAhorrosToAdd);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cuentaAhorrosToAdd, response.getBody());
    }

    @Test
    public void testAddCuentaAhorros_BadRequest() {
        CuentaAhorrosEntity cuentaAhorrosToAdd = new CuentaAhorrosEntity();
        cuentaAhorrosToAdd.setId(1);

        when(cuentaAhorrosService.exists(cuentaAhorrosToAdd.getId())).thenReturn(true);

        ResponseEntity<CuentaAhorrosEntity> response = cuentaAhorrosController.add(cuentaAhorrosToAdd);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testUpdateCuentaAhorros() {
        CuentaAhorrosEntity cuentaAhorrosToUpdate = new CuentaAhorrosEntity();
        cuentaAhorrosToUpdate.setId(1);

        when(cuentaAhorrosService.exists(cuentaAhorrosToUpdate.getId())).thenReturn(true);
        when(cuentaAhorrosService.save(cuentaAhorrosToUpdate)).thenReturn(cuentaAhorrosToUpdate);


        ResponseEntity<CuentaAhorrosEntity> response = cuentaAhorrosController.update(cuentaAhorrosToUpdate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cuentaAhorrosToUpdate, response.getBody());
    }

    @Test
    public void testUpdateCuentaAhorros_BadRequest() {
        CuentaAhorrosEntity cuentaAhorrosToUpdate = new CuentaAhorrosEntity();
        cuentaAhorrosToUpdate.setId(1);

        when(cuentaAhorrosService.exists(cuentaAhorrosToUpdate.getId())).thenReturn(false);

        ResponseEntity<CuentaAhorrosEntity> response = cuentaAhorrosController.update(cuentaAhorrosToUpdate);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testDeleteCuentaAhorros_BadRequest() {
        int idCuentaAhorros = 1;

        when(cuentaAhorrosService.exists(idCuentaAhorros)).thenReturn(false);

        ResponseEntity<Void> response = cuentaAhorrosController.delete(idCuentaAhorros);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}

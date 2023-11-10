package com.udem.bank.ControllerTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udem.bank.persistence.entity.GrupoAhorroEntity;
import com.udem.bank.service.GrupoAhorrosService;
import com.udem.bank.service.GrupoXUsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.udem.bank.web.controller.GrupoAhorrosController;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GrupoAhorrosControllerTest {

    @Mock
    private GrupoAhorrosService grupoAhorrosService;

    @Mock
    private GrupoXUsuarioService grupoXUsuarioService;

    @InjectMocks
    private GrupoAhorrosController grupoAhorrosController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAllGruposAhorros() {
        // Configuración del escenario para las pruebas
        GrupoAhorroEntity grupo1 = new GrupoAhorroEntity();
        GrupoAhorroEntity grupo2 = new GrupoAhorroEntity();
        List<GrupoAhorroEntity> gruposAhorrosMock = Arrays.asList(grupo1, grupo2);

        when(grupoAhorrosService.getAll()).thenReturn(gruposAhorrosMock);

        // Ejecución de la prueba
        ResponseEntity<List<GrupoAhorroEntity>> response = grupoAhorrosController.getAll();

        // Verificación si la prueba esta funcionando
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(gruposAhorrosMock, response.getBody());
    }

    @Test
    public void testGetGrupoAhorroById() {
        int idGrupoAhorro = 1;
        GrupoAhorroEntity grupoAhorroMock = new GrupoAhorroEntity();
        grupoAhorroMock.setId(idGrupoAhorro);

        when(grupoAhorrosService.get(idGrupoAhorro)).thenReturn(grupoAhorroMock);

        ResponseEntity<GrupoAhorroEntity> response = grupoAhorrosController.get(idGrupoAhorro);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(grupoAhorroMock, response.getBody());
    }

    @Test
    public void testAddGrupoAhorro() {
        GrupoAhorroEntity grupoAhorroToAdd = new GrupoAhorroEntity();
        grupoAhorroToAdd.setId(1);

        when(grupoAhorrosService.save(grupoAhorroToAdd)).thenReturn(grupoAhorroToAdd);
        when(grupoAhorrosService.exists(grupoAhorroToAdd.getId())).thenReturn(false);

        ResponseEntity<GrupoAhorroEntity> response = grupoAhorrosController.add(grupoAhorroToAdd);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(grupoAhorroToAdd, response.getBody());
    }

    @Test
    public void testAddGrupoAhorro_BadRequest() {
        GrupoAhorroEntity grupoAhorroToAdd = new GrupoAhorroEntity();
        grupoAhorroToAdd.setId(1);

        when(grupoAhorrosService.exists(grupoAhorroToAdd.getId())).thenReturn(true);

        ResponseEntity<GrupoAhorroEntity> response = grupoAhorrosController.add(grupoAhorroToAdd);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testUpdateGrupoAhorro() {
        GrupoAhorroEntity grupoAhorroToUpdate = new GrupoAhorroEntity();
        grupoAhorroToUpdate.setId(1);

        when(grupoAhorrosService.exists(grupoAhorroToUpdate.getId())).thenReturn(true);
        when(grupoAhorrosService.save(grupoAhorroToUpdate)).thenReturn(grupoAhorroToUpdate);


        ResponseEntity<GrupoAhorroEntity> response = grupoAhorrosController.update(grupoAhorroToUpdate, null);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(grupoAhorroToUpdate, response.getBody());
    }

}
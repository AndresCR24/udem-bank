package com.udem.bank.ControllerTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udem.bank.persistence.entity.UsuarioEntity;
import com.udem.bank.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.udem.bank.web.controller.UsuarioController;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAllUsuarios() {
        // Configuración del escenario para las pruebas
        UsuarioEntity usuario1 = new UsuarioEntity();
        UsuarioEntity usuario2 = new UsuarioEntity();
        List<UsuarioEntity> usuariosMock = Arrays.asList(usuario1, usuario2);

        // Ejecución de la prueba
        when(usuarioService.getAll()).thenReturn(usuariosMock);

        ResponseEntity<List<UsuarioEntity>> response = usuarioController.getAll();
        // Verificación si la prueba esta funcionando
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuariosMock, response.getBody());
    }

    @Test
    public void testGetUsuarioById() {
        int idUsuario = 1;
        UsuarioEntity usuarioMock = new UsuarioEntity();
        usuarioMock.setId(idUsuario);

        when(usuarioService.get(idUsuario)).thenReturn(usuarioMock);

        ResponseEntity<UsuarioEntity> response = usuarioController.get(idUsuario);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarioMock, response.getBody());
    }

    @Test
    public void testAddUsuario() {
        UsuarioEntity usuarioToAdd = new UsuarioEntity();
        usuarioToAdd.setId(1);

        when(usuarioService.save(usuarioToAdd)).thenReturn(usuarioToAdd);

        ResponseEntity<UsuarioEntity> response = usuarioController.add(usuarioToAdd);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarioToAdd, response.getBody());
    }

    @Test
    public void testUpdateUsuario() {
        UsuarioEntity usuarioToUpdate = new UsuarioEntity();
        usuarioToUpdate.setId(1);

        when(usuarioService.exists(usuarioToUpdate.getId())).thenReturn(true);
        when(usuarioService.save(usuarioToUpdate)).thenReturn(usuarioToUpdate);

        ResponseEntity<UsuarioEntity> response = usuarioController.update(usuarioToUpdate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarioToUpdate, response.getBody());
    }

    @Test
    public void testUpdateUsuario_BadRequest() {
        UsuarioEntity usuarioToUpdate = new UsuarioEntity();
        usuarioToUpdate.setId(1);

        when(usuarioService.exists(usuarioToUpdate.getId())).thenReturn(false);

        ResponseEntity<UsuarioEntity> response = usuarioController.update(usuarioToUpdate);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testDeleteUsuario() {
        int idUsuario = 1;

        when(usuarioService.exists(idUsuario)).thenReturn(true);

        ResponseEntity<Void> response = usuarioController.delete(idUsuario);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(usuarioService, times(1)).deleteUsuario(idUsuario);
    }

    @Test
    public void testDeleteUsuario_BadRequest() {
        int idUsuario = 1;

        when(usuarioService.exists(idUsuario)).thenReturn(false);

        ResponseEntity<Void> response = usuarioController.delete(idUsuario);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}


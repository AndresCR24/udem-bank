package com.udem.bank.ServiceTests;
import com.udem.bank.service.UsuarioService;

import com.udem.bank.persistence.entity.UsuarioEntity;
import com.udem.bank.persistence.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAll() {
        // Configuración del escenario para las pruebas
        List<UsuarioEntity> usuariosList = new ArrayList<>();
        when(usuarioRepository.findAll()).thenReturn(usuariosList);
        // Ejecución de la prueba

        List<UsuarioEntity> result = usuarioService.getAll();
        // Verificación si la prueba esta funcionando

        assertEquals(usuariosList, result);
    }

    @Test
    public void testGet() {
        UsuarioEntity usuario = new UsuarioEntity();
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        UsuarioEntity result = usuarioService.get(1);

        assertEquals(usuario, result);
    }

    @Test
    public void testSave() {
        UsuarioEntity usuario = new UsuarioEntity();
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        UsuarioEntity result = usuarioService.save(usuario);

        assertEquals(usuario, result);
    }

    @Test
    public void testExists() {
        when(usuarioRepository.existsById(1)).thenReturn(true);

        boolean result = usuarioService.exists(1);

        assert(result);
    }

    @Test
    public void testDeleteUsuario() {
        usuarioService.deleteUsuario(1);

        verify(usuarioRepository, times(1)).deleteById(1);
    }
}


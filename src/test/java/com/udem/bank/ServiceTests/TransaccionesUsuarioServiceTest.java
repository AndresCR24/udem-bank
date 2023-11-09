package com.udem.bank.ServiceTests;

import com.udem.bank.persistence.entity.*;
import com.udem.bank.persistence.repository.GrupoAhorroRepository;
import com.udem.bank.persistence.repository.PrestamoGrupoRepository;
import com.udem.bank.persistence.repository.TransaccionesUsuarioRepository;
import com.udem.bank.persistence.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;
import com.udem.bank.service.TransaccionesUsuarioService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TransaccionesUsuarioServiceTest {

    @InjectMocks
    private TransaccionesUsuarioService transaccionesUsuarioService;

    @Mock
    private TransaccionesUsuarioRepository transaccionesUsuarioRepository;

    @Mock
    private GrupoAhorroRepository grupoAhorroRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAll() {
        List<TransaccionesUsuarioEntity> transaccionesList = new ArrayList<>();
        when(transaccionesUsuarioRepository.findAll()).thenReturn(transaccionesList);

        List<TransaccionesUsuarioEntity> result = transaccionesUsuarioService.getAll();

        assertEquals(transaccionesList, result);
    }

    @Test
    public void testGet() {
        TransaccionesUsuarioEntity transaccion = new TransaccionesUsuarioEntity();
        when(transaccionesUsuarioRepository.findById(1)).thenReturn(Optional.of(transaccion));

        TransaccionesUsuarioEntity result = transaccionesUsuarioService.get(1);

        assertEquals(transaccion, result);
    }

    @Test
    public void testSave() {
        TransaccionesUsuarioEntity transaccion = new TransaccionesUsuarioEntity();
        when(transaccionesUsuarioRepository.save(transaccion)).thenReturn(transaccion);

        TransaccionesUsuarioEntity result = transaccionesUsuarioService.save(transaccion);

        assertEquals(transaccion, result);
    }

    @Test
    public void testExists() {
        when(transaccionesUsuarioRepository.existsById(1)).thenReturn(true);

        boolean result = transaccionesUsuarioService.exists(1);

        assert(result);
    }

    @Test
    public void testDeleteTransaccion() {
        transaccionesUsuarioService.deleteTransaccion(1);

        verify(transaccionesUsuarioRepository, times(1)).deleteById(1);
    }


}


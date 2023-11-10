package com.udem.bank.ServiceTests;
import  com.udem.bank.service.InvitacionesService;

import com.udem.bank.persistence.entity.GrupoAhorroEntity;
import com.udem.bank.persistence.entity.InvitacionesEntity;
import com.udem.bank.persistence.entity.UsuarioEntity;
import com.udem.bank.persistence.repository.GrupoAhorroRepository;
import com.udem.bank.persistence.repository.InvitacionesRepository;
import com.udem.bank.persistence.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class InvitacionesServiceTest {

    @InjectMocks
    private InvitacionesService invitacionesService;

    @Mock
    private InvitacionesRepository invitacionesRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private GrupoAhorroRepository grupoAhorroRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAll() {
        // Configuración del escenario para las pruebas
        List<InvitacionesEntity> invitacionesList = new ArrayList<>();
        // Ejecución de la prueba
        when(invitacionesRepository.findAll()).thenReturn(invitacionesList);

        List<InvitacionesEntity> result = invitacionesService.getAll();
        // Verificación si la prueba esta funcionando
        assertEquals(invitacionesList, result);
    }

    @Test
    public void testGet() {
        InvitacionesEntity invitacion = new InvitacionesEntity();
        when(invitacionesRepository.findById(1)).thenReturn(Optional.of(invitacion));

        InvitacionesEntity result = invitacionesService.get(1);

        assertEquals(invitacion, result);
    }

    @Test
    public void testSave() {
        InvitacionesEntity invitacion = new InvitacionesEntity();
        when(invitacionesRepository.save(invitacion)).thenReturn(invitacion);

        InvitacionesEntity result = invitacionesService.save(invitacion);

        assertEquals(invitacion, result);
    }

    @Test
    public void testExists() {
        when(invitacionesRepository.existsById(1)).thenReturn(true);

        boolean result = invitacionesService.exists(1);

        assertTrue(result);
    }

    @Test
    public void testDelete() {
        invitacionesService.delete(1);
        verify(invitacionesRepository, times(1)).deleteById(1);
    }

    @Test
    public void testCrearInvitacion() {
        InvitacionesEntity invitacionNueva = new InvitacionesEntity();
        invitacionNueva.setId(1);
        invitacionNueva.setIdUsuario(1);
        invitacionNueva.setIdGrupoAhorro(1);
        invitacionNueva.setCodigoInvitacion(UUID.randomUUID().toString());

        when(invitacionesRepository.countByUsuarioInvitacionesIdAndGrupoAhorroInvitacionId(1, 1)).thenReturn(1L);
        when(invitacionesRepository.save(any(InvitacionesEntity.class))).thenReturn(invitacionNueva);

        InvitacionesEntity result = invitacionesService.crearInvitacion(1, 1);

        assertNotNull(result);
        assertEquals(1, result.getIdUsuario());
        assertEquals(1, result.getIdGrupoAhorro());
    }
}

package com.udem.bank.EntityTest;

import com.udem.bank.persistence.entity.InvitacionesEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InvitacionesEntityTest {

    @Test
    public void testInvitacionesEntity() {
        // Crear una nueva entidad InvitacionesEntity
        InvitacionesEntity invitacion = new InvitacionesEntity();
        invitacion.setId(1);
        invitacion.setCodigoInvitacion("10noviembre");
        invitacion.setUsado(false);

        assertNotNull(invitacion.getId());
        assertEquals("10noviembre", invitacion.getCodigoInvitacion());
        assertFalse(invitacion.isUsado());
    }
}

package com.udem.bank.EntityTest;

import com.udem.bank.persistence.entity.UsuarioEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioEntityTest {

    @Test
    public void testUsuarioEntity() {
        // Crear una nueva entidad UsuarioEntity
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(1);
        usuario.setUser("JhonathanProfeDelAño");
        usuario.setPassword("grave");

        assertNotNull(usuario.getId());
        assertEquals("JhonathanProfeDelAño", usuario.getUser());
        assertEquals("grave", usuario.getPassword());
    }
}


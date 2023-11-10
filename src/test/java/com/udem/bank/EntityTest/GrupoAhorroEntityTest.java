package com.udem.bank.EntityTest;
import com.udem.bank.persistence.entity.GrupoAhorroEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class GrupoAhorroEntityTest {

    @Test
    public void testGrupoAhorroEntity() {
        // Crear una nueva entidad GrupoAhorroEntity
        GrupoAhorroEntity grupo = new GrupoAhorroEntity();
        grupo.setId(1);
        grupo.setNombreGrupo("PagaDiarios S.A.");
        grupo.setSaldo(BigDecimal.ZERO);

        // Verificación
        assertNotNull(grupo.getId());
        assertEquals("PagaDiarios S.A.", grupo.getNombreGrupo());
        assertEquals(BigDecimal.ZERO, grupo.getSaldo());
    }
}

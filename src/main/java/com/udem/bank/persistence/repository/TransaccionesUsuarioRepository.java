package com.udem.bank.persistence.repository;

import com.udem.bank.persistence.entity.TransaccionesUsuarioEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface TransaccionesUsuarioRepository extends ListCrudRepository<TransaccionesUsuarioEntity, Integer> {
    @Query("SELECT SUM(t.monto) FROM TransaccionesUsuarioEntity t WHERE t.idGrupo = :groupId AND t.idUsuario = :userId")
    BigDecimal totalContributionByUserToGroup(@Param("userId") Integer userId, @Param("groupId") Integer groupId);

}

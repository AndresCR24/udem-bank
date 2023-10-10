package com.udem.bank.persistence.repository;

import com.udem.bank.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends ListCrudRepository<UsuarioEntity, Integer> {
    //Obligatorio utilizar el @Query para poder hacer la verificacion de usuarios debido a la relacion
    //ManyToMany de usuario con grupoAhorro como se crea una tabla intermedia que tiene servicio pero no
    //Repository toca hacer el codigo SQL con @Query
    @Query("SELECT count(g) FROM UsuarioEntity u JOIN u.grupoAhorro g WHERE u.id = :idUsuario")
    long countGruposByUsuarioId(@Param("idUsuario") Integer idUsuario);
}

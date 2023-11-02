package com.udem.bank.persistence.repository;

import com.udem.bank.persistence.entity.PrestamoGrupoEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface PrestamoGrupoRepository extends ListCrudRepository<PrestamoGrupoEntity, Integer> {
    Optional<PrestamoGrupoEntity> findByIdAndUsuarioPrestamo_Id(Integer idPrestamoGrupo, Integer idUsuario);

}

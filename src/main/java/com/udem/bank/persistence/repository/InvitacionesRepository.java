package com.udem.bank.persistence.repository;

import com.udem.bank.persistence.entity.InvitacionesEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface InvitacionesRepository extends ListCrudRepository<InvitacionesEntity, Integer> {
    long countByUsuarioInvitacionesIdAndGrupoAhorroInvitacionId(Integer idUsuario, Integer idGrupo);
    Optional<InvitacionesEntity> findByCodigoInvitacion(String codigoInvitacion);

}

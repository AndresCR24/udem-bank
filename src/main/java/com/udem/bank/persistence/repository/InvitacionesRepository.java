package com.udem.bank.persistence.repository;

import com.udem.bank.persistence.entity.InvitacionesEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface InvitacionesRepository extends ListCrudRepository<InvitacionesEntity, Integer> {
    long countByUsuarioInvitacionesIdAndGrupoAhorroInvitacionId(Integer idUsuario, Integer idGrupo);

}

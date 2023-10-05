package com.udem.bank.persistence.repository;

import com.udem.bank.persistence.entity.TransaccionesUsuarioEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface TransaccionesUsuarioRepository extends ListCrudRepository<TransaccionesUsuarioEntity, Integer> {
}

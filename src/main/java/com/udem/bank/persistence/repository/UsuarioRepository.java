package com.udem.bank.persistence.repository;

import com.udem.bank.persistence.entity.UsuarioEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface UsuarioRepository extends ListCrudRepository<UsuarioEntity, Integer> {
}

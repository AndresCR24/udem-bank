package com.udem.bank.persistence.repository;

import com.udem.bank.persistence.entity.PrestamoUsuarioEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface PrestamosUsuarioRepository extends ListCrudRepository<PrestamoUsuarioEntity, Integer> {
}

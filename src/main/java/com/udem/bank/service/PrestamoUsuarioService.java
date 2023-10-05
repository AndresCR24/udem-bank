package com.udem.bank.service;

import com.udem.bank.persistence.entity.PrestamoGrupoEntity;
import com.udem.bank.persistence.entity.PrestamoUsuarioEntity;
import com.udem.bank.persistence.repository.PrestamosUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrestamoUsuarioService {

    private final PrestamosUsuarioRepository prestamosUsuarioRepository;

    @Autowired
    public PrestamoUsuarioService(PrestamosUsuarioRepository prestamosUsuarioRepository) {
        this.prestamosUsuarioRepository = prestamosUsuarioRepository;
    }

    //Devolver lista de cuentas
    public List<PrestamoUsuarioEntity> getAll()
    {
        return this.prestamosUsuarioRepository.findAll();
    }

    //Devolver usuario por su id
    public PrestamoUsuarioEntity get(int idUsuario)
    {
        return this.prestamosUsuarioRepository.findById(idUsuario).orElse(null);
    }

    public PrestamoUsuarioEntity save(PrestamoUsuarioEntity prestamoUsuario)
    {
        return this.prestamosUsuarioRepository.save(prestamoUsuario);
    }
    //Validar si un id existe
    public boolean exists(int idPrestamoUsuario)
    {
        return this.prestamosUsuarioRepository.existsById(idPrestamoUsuario);
    }
}

package com.udem.bank.service;

import com.udem.bank.persistence.entity.UsuarioEntity;
import com.udem.bank.persistence.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    //Devolver lista de usuarios
    public List<UsuarioEntity> getAll()
    {
        return this.usuarioRepository.findAll();
    }

    //Devolver usuario por su id
    public UsuarioEntity get(int idUsuario)
    {
        return this.usuarioRepository.findById(idUsuario).orElse(null);
    }

    public UsuarioEntity save(UsuarioEntity usuario)
    {
        return this.usuarioRepository.save(usuario);
    }

    //Validar si un id existe
    public boolean exists(int idUsuario)
    {
        return this.usuarioRepository.existsById(idUsuario);
    }

}

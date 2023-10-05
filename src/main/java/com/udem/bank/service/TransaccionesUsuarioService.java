package com.udem.bank.service;

import com.udem.bank.persistence.entity.PrestamoUsuarioEntity;
import com.udem.bank.persistence.entity.TransaccionesUsuarioEntity;
import com.udem.bank.persistence.repository.TransaccionesUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransaccionesUsuarioService {

    private final TransaccionesUsuarioRepository transaccionesUsuarioRepository;

    @Autowired
    public TransaccionesUsuarioService(TransaccionesUsuarioRepository transaccionesUsuarioRepository) {
        this.transaccionesUsuarioRepository = transaccionesUsuarioRepository;
    }

    //Devolver lista de cuentas
    public List<TransaccionesUsuarioEntity> getAll()
    {
        return this.transaccionesUsuarioRepository.findAll();
    }

    //Devolver usuario por su id
    public TransaccionesUsuarioEntity get(int idUsuario)
    {
        return this.transaccionesUsuarioRepository.findById(idUsuario).orElse(null);
    }

    public TransaccionesUsuarioEntity save(TransaccionesUsuarioEntity transaccionesUsuario)
    {
        return this.transaccionesUsuarioRepository.save(transaccionesUsuario);
    }
    //Validar si un id existe
    public boolean exists(int idTransaccionUsuario)
    {
        return this.transaccionesUsuarioRepository.existsById(idTransaccionUsuario);
    }
}

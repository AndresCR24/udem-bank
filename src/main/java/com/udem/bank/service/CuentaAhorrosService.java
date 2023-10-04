package com.udem.bank.service;

import com.udem.bank.persistence.entity.CuentaAhorrosEntity;
import com.udem.bank.persistence.entity.UsuarioEntity;
import com.udem.bank.persistence.repository.CuentaAhorrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuentaAhorrosService {

    private final CuentaAhorrosRepository cuentaAhorrosRepository;

    @Autowired
    public CuentaAhorrosService(CuentaAhorrosRepository cuentaAhorrosRepository) {
        this.cuentaAhorrosRepository = cuentaAhorrosRepository;
    }

    //Devolver lista de cuentas
    public List<CuentaAhorrosEntity> getAll()
    {
        return this.cuentaAhorrosRepository.findAll();
    }

    //Devolver usuario por su id
    public CuentaAhorrosEntity get(int idCuenta)
    {
        return this.cuentaAhorrosRepository.findById(idCuenta).orElse(null);
    }

    public CuentaAhorrosEntity save(CuentaAhorrosEntity cuentaAhorros)
    {
        return this.cuentaAhorrosRepository.save(cuentaAhorros);
    }
    //Validar si un id existe
    public boolean exists(int idCuenta)
    {
        return this.cuentaAhorrosRepository.existsById(idCuenta);
    }
}

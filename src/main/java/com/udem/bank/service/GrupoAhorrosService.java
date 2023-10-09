package com.udem.bank.service;

import com.udem.bank.persistence.entity.CuentaAhorrosEntity;
import com.udem.bank.persistence.entity.GrupoAhorroEntity;
import com.udem.bank.persistence.repository.GrupoAhorroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrupoAhorrosService {

    private final GrupoAhorroRepository grupoAhorroRepository;

    @Autowired
    public GrupoAhorrosService(GrupoAhorroRepository grupoAhorroRepository) {
        this.grupoAhorroRepository = grupoAhorroRepository;
    }

    //Devolver lista de cuentas
    public List<GrupoAhorroEntity> getAll()
    {
        return this.grupoAhorroRepository.findAll();
    }

    //Devolver usuario por su id
    public GrupoAhorroEntity get(int idGrupo)
    {
        return this.grupoAhorroRepository.findById(idGrupo).orElse(null);
    }

    public GrupoAhorroEntity save(GrupoAhorroEntity grupoAhorro)
    {
        return this.grupoAhorroRepository.save(grupoAhorro);
    }
    //Validar si un id existe
    public boolean exists(int idCuenta)
    {
        return this.grupoAhorroRepository.existsById(idCuenta);
    }

    public void deleteGrupoAhorro(int idGrupo){
        this.grupoAhorroRepository.deleteById(idGrupo);
    }
}

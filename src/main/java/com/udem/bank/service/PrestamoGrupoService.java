package com.udem.bank.service;

import com.udem.bank.persistence.entity.GrupoAhorroEntity;
import com.udem.bank.persistence.entity.PrestamoGrupoEntity;
import com.udem.bank.persistence.repository.PrestamoGrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrestamoGrupoService {

    private final PrestamoGrupoRepository prestamoGrupoRepository;

    @Autowired
    public PrestamoGrupoService(PrestamoGrupoRepository prestamoGrupoRepository) {
        this.prestamoGrupoRepository = prestamoGrupoRepository;
    }

    //Devolver lista de cuentas
    public List<PrestamoGrupoEntity> getAll()
    {
        return this.prestamoGrupoRepository.findAll();
    }

    //Devolver usuario por su id
    public PrestamoGrupoEntity get(int idGrupo)
    {
        return this.prestamoGrupoRepository.findById(idGrupo).orElse(null);
    }

    public PrestamoGrupoEntity save(PrestamoGrupoEntity prestamoGrupo)
    {
        return this.prestamoGrupoRepository.save(prestamoGrupo);
    }
    //Validar si un id existe
    public boolean exists(int idPrestamo)
    {
        return this.prestamoGrupoRepository.existsById(idPrestamo);
    }

    public void deletePrestamoGrupo(int idPrestamoGrupo){
        this.prestamoGrupoRepository.deleteById(idPrestamoGrupo);
    }
}

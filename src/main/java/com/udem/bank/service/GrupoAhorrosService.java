package com.udem.bank.service;

import com.udem.bank.persistence.entity.CuentaAhorrosEntity;
import com.udem.bank.persistence.entity.GrupoAhorroEntity;
import com.udem.bank.persistence.entity.UsuarioEntity;
import com.udem.bank.persistence.repository.GrupoAhorroRepository;
import com.udem.bank.persistence.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GrupoAhorrosService {
    private static final BigDecimal UDEMBANK_COMMISSION_RATE = new BigDecimal("0.05"); // 5% as a decimal

    private final GrupoAhorroRepository grupoAhorroRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public GrupoAhorrosService(GrupoAhorroRepository grupoAhorroRepository, UsuarioRepository usuarioRepository) {
        this.grupoAhorroRepository = grupoAhorroRepository;
        this.usuarioRepository = usuarioRepository;
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

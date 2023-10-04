package com.udem.bank.service;

import com.udem.bank.persistence.repository.GrupoAhorroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GrupoAhorrosService {

    private final GrupoAhorroRepository grupoAhorroRepository;

    @Autowired
    public GrupoAhorrosService(GrupoAhorroRepository grupoAhorroRepository) {
        this.grupoAhorroRepository = grupoAhorroRepository;
    }


}

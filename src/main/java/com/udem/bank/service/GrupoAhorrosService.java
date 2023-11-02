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
import java.util.*;

@Service
public class GrupoAhorrosService {
    private static final BigDecimal UDEMBANK_COMMISSION_RATE = new BigDecimal("0.05"); // 5% as a decimal
    //Excluir grupo de ahorro del banco
    private static final List<Integer> excluirGrupoBanco = Arrays.asList(35, 36);


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

    //Bono del 10% al grupo de ahorro con mas dinero

    public void aumentarSaldoAlGrupoConMasIngresos() {
        // 1. Obtener el grupo de ahorro que más dinero ha ingresado y que no está en la lista de excluidos
        GrupoAhorroEntity grupoConMasIngresos = grupoAhorroRepository
                .findAll()
                .stream()
                .filter(grupo -> !excluirGrupoBanco.contains(grupo.getId()))  // filtrar los grupos excluidos
                .max(Comparator.comparing(GrupoAhorroEntity::getSaldo))
                .orElse(null);

        if (grupoConMasIngresos != null) {
            // 2. Aumentar el saldo del grupo en un 10%
            BigDecimal saldoActual = grupoConMasIngresos.getSaldo();
            // 10% de incremento gracias a las propiedades de BigDecimal
            BigDecimal incremento = saldoActual.multiply(BigDecimal.valueOf(0.10));
            grupoConMasIngresos.setSaldo(saldoActual.add(incremento));

            // 3. Guardar el grupo de ahorro actualizado
            grupoAhorroRepository.save(grupoConMasIngresos);
        }
    }
}

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

    //Constante para la respectiva comision del banco
    private static final BigDecimal COMISION_UDEMBANK = new BigDecimal("0.05"); // 5% as a decimal

    //Excluir grupo de ahorro del banco
    private static final List<Integer> excluirGrupoBanco = Arrays.asList(1);

    //Aqui se inyectan las dependencias -> en este caso son interfaces que extienden de LiscrudRepository de spring
    //Gracias a esto podemos manejar todas las operaciones CRUD de SPRING FRAMEWORK
    private final GrupoAhorroRepository grupoAhorroRepository;
    private final UsuarioRepository usuarioRepository;

    //Constructor que inicializa los repositorios
    @Autowired
    public GrupoAhorrosService(GrupoAhorroRepository grupoAhorroRepository, UsuarioRepository usuarioRepository) {
        this.grupoAhorroRepository = grupoAhorroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    //Devuelve todos los grupos de ahorro
    public List<GrupoAhorroEntity> getAll()
    {
        return this.grupoAhorroRepository.findAll();
    }

    //Devolve un grupo de ahorro por su ID
    public GrupoAhorroEntity get(int idGrupo)
    {
        return this.grupoAhorroRepository.findById(idGrupo).orElse(null);
    }

    //Guarda o actualiza un grupo de ahorro
    public GrupoAhorroEntity save(GrupoAhorroEntity grupoAhorro)
    {
        return this.grupoAhorroRepository.save(grupoAhorro);
    }

    //Validar si un id existe
    public boolean exists(int idCuenta)
    {
        return this.grupoAhorroRepository.existsById(idCuenta);
    }

    //ELimina un grupo de ahorro
    public void deleteGrupoAhorro(int idGrupo){
        this.grupoAhorroRepository.deleteById(idGrupo);
    }

    //Bono del 10% al grupo de ahorro con mas dinero
    public void aumentarSaldoAlGrupoConMasIngresos() {
        // 1. Obtener el grupo de ahorro que más dinero ha ingresado y que no está en la lista de excluidos
        GrupoAhorroEntity grupoConMasIngresos = grupoAhorroRepository
                .findAll() //Metodo de CRUD REPOSITORY -> retorna todos los grupos de ahorro en este caso
                .stream()//Metodo de CRUD REPOSITORY -> permite realizar operaciones de proceso de datos
                //Si un grupo de ahorro esta excluido **Por pertenecer al banco** lo retira del flujo
                .filter(grupo -> !excluirGrupoBanco.contains(grupo.getId()))
                //con .max buscamos el grupo de ahorro con el saldo mas alto
                .max(Comparator.comparing(GrupoAhorroEntity::getSaldo))
                .orElse(null);

        if (grupoConMasIngresos != null) {
            //Se optiene el saldo atual del grupo de ahorros
            BigDecimal saldoActual = grupoConMasIngresos.getSaldo();
            //Se calcula el 10% de bono que se le debe dar al grupo de ahorro que ganara el bono
            //Se utiliza motodos de BigDecimal para hacerlo directamente
            BigDecimal incremento = saldoActual.multiply(BigDecimal.valueOf(0.10));
            //se agrega el nuevo saldo al grupo de ahorro
            grupoConMasIngresos.setSaldo(saldoActual.add(incremento));

            //Se guarda en el grupo de ahorro en la base de datos con el metodo save de CRUD REPOSITORY de SPRING
            grupoAhorroRepository.save(grupoConMasIngresos);
        }
    }
}

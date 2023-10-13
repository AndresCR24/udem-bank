package com.udem.bank.service;

import com.udem.bank.persistence.entity.GrupoAhorroEntity;
import com.udem.bank.persistence.entity.PrestamoUsuarioEntity;
import com.udem.bank.persistence.entity.TransaccionesUsuarioEntity;
import com.udem.bank.persistence.entity.UsuarioEntity;
import com.udem.bank.persistence.repository.GrupoAhorroRepository;
import com.udem.bank.persistence.repository.TransaccionesUsuarioRepository;
import com.udem.bank.persistence.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransaccionesUsuarioService {

    private final TransaccionesUsuarioRepository transaccionesUsuarioRepository;
    private final GrupoAhorroRepository grupoAhorroRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public TransaccionesUsuarioService(TransaccionesUsuarioRepository transaccionesUsuarioRepository, GrupoAhorroRepository grupoAhorroRepository, UsuarioRepository usuarioRepository) {
        this.transaccionesUsuarioRepository = transaccionesUsuarioRepository;
        this.grupoAhorroRepository = grupoAhorroRepository;
        this.usuarioRepository = usuarioRepository;
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

    public void deleteTransaccion(int idTransaccion){
        this.transaccionesUsuarioRepository.deleteById(idTransaccion);
    }

    //Realizar una transaccion
    @Transactional
    public TransaccionesUsuarioEntity registrarDepositoAGrupo(Integer idUsuario, Integer idGrupo, BigDecimal monto) {
        // Validaciones...

        // Actualizar el saldo del usuario
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario != null) {
            BigDecimal saldoActualUsuario = usuario.getCuentaAhorros().getSaldoActual();
            if (saldoActualUsuario.compareTo(monto) < 0) {
                // Manejar el caso donde el usuario no tiene suficiente saldo
              *  throw new InsufficientFundsException("No hay saldo suficiente");
            }
           * usuario.setSaldo(saldoActualUsuario.subtract(monto)); // Resta el monto del saldo actual del usuario
            usuarioRepository.save(usuario);
        } else {
            // Manejar el caso donde el usuario no es encontrado
            throw new UserNotFoundException("Usuario no encontrado");
        }

        // Actualizar el saldo del grupo
        GrupoAhorroEntity grupo = grupoAhorroRepository.findById(idGrupo).orElse(null);
        if (grupo != null) {
            BigDecimal saldoActualGrupo = grupo.getSaldo();
            grupo.setSaldo(saldoActualGrupo.add(monto));
            grupoAhorroRepository.save(grupo);
        } else {
          *  throw new GroupNotFoundException("Grupo no encontrado");
        }

        TransaccionesUsuarioEntity transaccion = new TransaccionesUsuarioEntity();
        // ... [resto del código para registrar la transacción]
        return transaccionesUsuarioRepository.save(transaccion);
    }


}

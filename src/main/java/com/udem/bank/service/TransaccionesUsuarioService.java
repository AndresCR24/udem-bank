package com.udem.bank.service;

import com.udem.bank.persistence.entity.*;

import com.udem.bank.persistence.repository.GrupoAhorroRepository;
import com.udem.bank.persistence.repository.PrestamoGrupoRepository;
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
    private final PrestamoGrupoRepository prestamoGrupoRepository;

    public TransaccionesUsuarioService(TransaccionesUsuarioRepository transaccionesUsuarioRepository, GrupoAhorroRepository grupoAhorroRepository, UsuarioRepository usuarioRepository, PrestamoGrupoRepository prestamoGrupoRepository) {
        this.transaccionesUsuarioRepository = transaccionesUsuarioRepository;
        this.grupoAhorroRepository = grupoAhorroRepository;
        this.usuarioRepository = usuarioRepository;
        this.prestamoGrupoRepository = prestamoGrupoRepository;
    }

    //Devolver lista de cuentas
    public List<TransaccionesUsuarioEntity> getAll() {
        return this.transaccionesUsuarioRepository.findAll();
    }

    //Devolver usuario por su id
    public TransaccionesUsuarioEntity get(int idUsuario) {
        return this.transaccionesUsuarioRepository.findById(idUsuario).orElse(null);
    }

    public TransaccionesUsuarioEntity save(TransaccionesUsuarioEntity transaccionesUsuario) {
        return this.transaccionesUsuarioRepository.save(transaccionesUsuario);
    }

    //Validar si un id existe
    public boolean exists(int idTransaccionUsuario) {
        return this.transaccionesUsuarioRepository.existsById(idTransaccionUsuario);
    }

    public void deleteTransaccion(int idTransaccion) {
        this.transaccionesUsuarioRepository.deleteById(idTransaccion);
    }
    //Metodo anterior que genera fallos lo dejo de guia pra hacer futuros metodos (servicios) que pueden ser utiles
    /*
        @Transactional
        public TransaccionesUsuarioEntity depositarEnGrupoAhorro(int idUsuario, int idGrupoAhorro, BigDecimal monto)
                throws UsuarioNoEncontradoException, GrupoNoEncontradoException, FondosInsuficientesException {

            // Validar si el usuario existe
            UsuarioEntity usuario = usuarioRepository.findById(idUsuario)
                    .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

            // Validar si el grupo de ahorro existe
            GrupoAhorroEntity grupoAhorro = grupoAhorroRepository.findById(idGrupoAhorro)
                    .orElseThrow(() -> new GrupoNoEncontradoException("Grupo de ahorro no encontrado"));

            // Crear una nueva transacción de usuario
            TransaccionesUsuarioEntity nuevaTransaccion = new TransaccionesUsuarioEntity();
            nuevaTransaccion.setUsuarioTransacciones(usuario);
            nuevaTransaccion.setGrupoAhorroTransacciones(grupoAhorro); // Asumiendo que tienes esta relación en tu entidad
            nuevaTransaccion.setMonto(monto);
            // Asumiendo que tienes algún tipo de propiedad para indicar el tipo de transacción
            // nuevaTransaccion.setTipoTransaccion(TipoTransaccion.DEPOSITO);

            // Aquí podrías insertar lógicas adicionales como verificar si el usuario tiene suficientes fondos, etc.

            // Actualizar el saldo del grupo de ahorro. Esto presupone que tienes un método en tu entidad GrupoAhorroEntity para manejar el saldo.
            GrupoAhorrosService.addFondos(monto); // Este método debería implementarse en tu entidad para ajustar el saldo

            // Guardar las entidades actualizadas; el manejo de transacciones debería asegurar la integridad de este proceso
            transaccionesUsuarioRepository.save(nuevaTransaccion);
            grupoAhorroRepository.save(grupoAhorro);

            return nuevaTransaccion;
        }
        */
    @Transactional
    public TransaccionesUsuarioEntity registrarDepositoAGrupo(Integer idUsuario, Integer idGrupo, BigDecimal monto) {

        // Actualizar saldo
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario != null) {
            BigDecimal saldoActualUsuario = usuario.getCuentaAhorros().getSaldoActual();
            if (saldoActualUsuario.compareTo(monto) < 0) {
                // Manejar el caso donde el usuario no tiene suficiente saldo
                //throw new InsufficientFundsException("No hay saldo suficiente");
            }
            usuario.getCuentaAhorros().setSaldoActual(saldoActualUsuario.subtract(monto)); // Resta el monto del saldo actual del usuario
            usuarioRepository.save(usuario);
        } else {
            // Caso donde el usuario no es encontrado **Cuando terminemos**
            //throw a UserNotFoundException("Usuario no encontrado");
        }

        // Actualizar el saldo
        GrupoAhorroEntity grupo = grupoAhorroRepository.findById(idGrupo).orElse(null);
        if (grupo != null) {
            BigDecimal saldoActualGrupo = grupo.getSaldo();
            grupo.setSaldo(saldoActualGrupo.add(monto));
            grupoAhorroRepository.save(grupo);
        } else {
            //Manejar la exception **cuando terminemos el programa**
            //throw a GroupNotFoundException("Grupo no encontrado");
        }

        TransaccionesUsuarioEntity transaccion = new TransaccionesUsuarioEntity();
        transaccion.setIdUsuario(idUsuario);
        transaccion.setIdGrupo(idGrupo); // Set el valor al id del grupo que corresponde
        transaccion.setMonto(monto);

        return transaccionesUsuarioRepository.save(transaccion);
    }

}

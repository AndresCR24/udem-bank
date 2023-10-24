package com.udem.bank.service;

import com.udem.bank.persistence.entity.GrupoAhorroEntity;
import com.udem.bank.persistence.entity.PrestamoGrupoEntity;
import com.udem.bank.persistence.entity.UsuarioEntity;
import com.udem.bank.persistence.repository.GrupoAhorroRepository;
import com.udem.bank.persistence.repository.PrestamoGrupoRepository;
import com.udem.bank.persistence.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PrestamoGrupoService {

    private final PrestamoGrupoRepository prestamoGrupoRepository;
    private final UsuarioRepository usuarioRepository;
    private final GrupoAhorroRepository grupoAhorroRepository;

    @Autowired
    public PrestamoGrupoService(PrestamoGrupoRepository prestamoGrupoRepository, UsuarioRepository usuarioRepository, GrupoAhorroRepository grupoAhorroRepository) {
        this.prestamoGrupoRepository = prestamoGrupoRepository;
        this.usuarioRepository = usuarioRepository;
        this.grupoAhorroRepository = grupoAhorroRepository;
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

    @Transactional
    public PrestamoGrupoEntity solicitarPrestamo(Integer idUsuario, Integer idGrupo, BigDecimal monto, Integer plazoPrestamo) {
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        GrupoAhorroEntity grupo = grupoAhorroRepository.findById(idGrupo)
                .orElseThrow(() -> new IllegalArgumentException("Grupo de ahorro no encontrado"));

        // Validar si el usuario pertenece al grupo
        if (!usuario.getGrupoAhorro().contains(grupo)) {
            throw new IllegalArgumentException("El usuario no pertenece al grupo de ahorro");
        }

        // Validar si el grupo tiene suficiente saldo para prestar
        if (grupo.getSaldo().compareTo(monto) < 0) {
            throw new IllegalArgumentException("El grupo de ahorro no tiene suficiente saldo para prestar");
        }

        // Propiedades del servicio
        PrestamoGrupoEntity prestamo = new PrestamoGrupoEntity();
        prestamo.setIdUsuario(usuario.getId());
        prestamo.setIdGrupo(grupo.getId());
        prestamo.setMonto(monto);
        //El saldo que se el usuario debe pagar --> por ahora es el mismo que solicita
        //Modificarlo para que cobre intereses del 3%
        prestamo.setSaldoPendiente(monto);
        //Se Obtiene del JSON la cantidad de meses a los que esta el plazo
        prestamo.setPlazoPrestamo(plazoPrestamo);

        //actualizar el saldo que tiene el grupo
        grupo.setSaldo(grupo.getSaldo().subtract(monto));

        //Actualizar el saldo del usuario que hace el prestamo
        usuario.getCuentaAhorros().setSaldoActual(usuario.getCuentaAhorros().getSaldoActual().add(monto));
        // Guardar las entidades actualizadas
        prestamoGrupoRepository.save(prestamo);
        grupoAhorroRepository.save(grupo);

        
        return prestamo;
    }
}

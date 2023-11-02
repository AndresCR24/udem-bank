package com.udem.bank.service;

import com.udem.bank.persistence.entity.GrupoAhorroEntity;
import com.udem.bank.persistence.entity.UsuarioEntity;
import com.udem.bank.persistence.repository.GrupoAhorroRepository;
import com.udem.bank.persistence.repository.InvitacionesRepository;
import com.udem.bank.persistence.repository.UsuarioRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class GrupoXUsuarioService {
    private static final BigDecimal COMMISSION_BANCO = new BigDecimal("0.005"); // 5% as a decimal

    private final UsuarioRepository usuarioRepository;

    private final GrupoAhorroRepository grupoAhorroRepository;

    //Inyectar invitaciones para poder relacionarlas con los grupos
    private final InvitacionesRepository invitacionesRepository;

    @Autowired
    public GrupoXUsuarioService(UsuarioRepository usuarioRepository, GrupoAhorroRepository grupoAhorroRepository, InvitacionesRepository invitacionesRepository) {
        this.usuarioRepository = usuarioRepository;
        this.grupoAhorroRepository = grupoAhorroRepository;
        this.invitacionesRepository = invitacionesRepository;
    }

    public GrupoAhorroEntity crearGrupoConUsuario(GrupoAhorroEntity grupo, Integer idUsuario) {
        // Primero, guarda el grupo
        GrupoAhorroEntity grupoGuardado = grupoAhorroRepository.save(grupo);

        // Luego, asocia el grupo con el usuario
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if(usuario != null) {
            usuario.getGrupoAhorro().add(grupoGuardado);
            usuarioRepository.save(usuario);
        }

        return grupoGuardado;
    }

    public void addUsuarioToGrupo(Integer usuarioId, Integer grupoId) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId).orElse(null);
        GrupoAhorroEntity grupo = grupoAhorroRepository.findById(grupoId).orElse(null);

        grupo.getUsuarios().add(usuario);
        usuario.getGrupoAhorro().add(grupo);
        grupoAhorroRepository.save(grupo);
    }

    //Poder eliminar un grupo si tiene usuarios
    public void deleteGrupoAhorro(Integer idGrupo) {
        GrupoAhorroEntity grupo = grupoAhorroRepository.findById(idGrupo).orElse(null);

        if (grupo != null) {
            // Paso 2: Desvincular el Grupo de cada Usuario
            for (UsuarioEntity usuario : grupo.getUsuarios()) {
                usuario.getGrupoAhorro().remove(grupo);
                usuarioRepository.save(usuario);
            }

            // Paso 3: Eliminar el Grupo
            grupoAhorroRepository.delete(grupo);
        }
    }

    //Verificar si esta en 3 grupos de ahorro
    public boolean asociarUsuarioAGrupo(Integer idUsuario, Integer idGrupo) {
        // Verificar cuántos grupos ya tiene el usuario
        long count = usuarioRepository.countGruposByUsuarioId(idUsuario);
        if (count >= 3)
        {
            // El usuario ya tiene 3 grupos asociados
            return false;
        }
        //Logica pra agregar un usuario a un grupo --> es la misma del metodo de arriba pero este ya no
        //se utiliza en el codigo
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario).orElse(null);
        GrupoAhorroEntity grupo = grupoAhorroRepository.findById(idGrupo).orElse(null);

        grupo.getUsuarios().add(usuario);
        usuario.getGrupoAhorro().add(grupo);
        grupoAhorroRepository.save(grupo);

        return true;
    }

    //Disolver grupo ahorro

    public void eliminarGrupoAhorro(Integer idGrupo) {
        // 1. Obtener el grupo de ahorro
        GrupoAhorroEntity grupo = grupoAhorroRepository.findById(idGrupo)
                .orElseThrow(() -> new RuntimeException("Grupo de ahorro no encontrado"));

        // Inicializar manualmente la colección
        Hibernate.initialize(grupo.getUsuarios());

        // 2. Distribuir el saldo entre sus usuarios
        BigDecimal totalBalance = grupo.getSaldo();
        BigDecimal comisionUdem = totalBalance.multiply(COMMISSION_BANCO);
        BigDecimal balanceToDistribute = totalBalance.subtract(comisionUdem);

        // Crear una lista temporal de usuarios para evitar ConcurrentModificationException
        List<UsuarioEntity> tempUsuarios = new ArrayList<>(grupo.getUsuarios());
        for (UsuarioEntity usuario : tempUsuarios) {
            BigDecimal userBalance = usuario.getCuentaAhorros().getSaldoActual();
            BigDecimal newBalance = userBalance.add(balanceToDistribute.divide(BigDecimal.valueOf(grupo.getUsuarios().size()), RoundingMode.HALF_UP));
            usuario.getCuentaAhorros().setSaldoActual(newBalance);
            usuarioRepository.save(usuario);  // Guardar el saldo actualizado del usuario
        }

        // 3. Añadir comisión al grupo de ahorro de UdemBank
        GrupoAhorroEntity grupoUdemBank = grupoAhorroRepository.findById(36)
                .orElseThrow(() -> new RuntimeException("Grupo de ahorro UdemBank no encontrado"));
        grupoUdemBank.setSaldo(grupoUdemBank.getSaldo().add(comisionUdem));
        grupoAhorroRepository.save(grupoUdemBank);

        // 4. Eliminar el grupo
        //grupoAhorroRepository.delete(grupo);
        deleteGrupoAhorro(idGrupo);
    }


}

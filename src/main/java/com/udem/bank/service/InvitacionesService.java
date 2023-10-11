package com.udem.bank.service;

import com.udem.bank.persistence.entity.GrupoAhorroEntity;
import com.udem.bank.persistence.entity.InvitacionesEntity;
import com.udem.bank.persistence.entity.UsuarioEntity;
import com.udem.bank.persistence.repository.GrupoAhorroRepository;
import com.udem.bank.persistence.repository.InvitacionesRepository;
import com.udem.bank.persistence.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvitacionesService
{
    private final InvitacionesRepository invitacionesRepository;
    private final UsuarioRepository usuarioRepository;
    private final GrupoAhorroRepository grupoAhorroRepository;

    @Autowired
    public InvitacionesService(InvitacionesRepository invitacionesRepository, UsuarioRepository usuarioRepository, GrupoAhorroRepository grupoAhorroRepository) {
        this.invitacionesRepository = invitacionesRepository;
        this.usuarioRepository = usuarioRepository;
        this.grupoAhorroRepository = grupoAhorroRepository;
    }

    //Devolver lista de cuentas
    public List<InvitacionesEntity> getAll()
    {
        return this.invitacionesRepository.findAll();
    }

    //Devolver usuario por su id
    public InvitacionesEntity get(int idInvitacion)
    {
        return this.invitacionesRepository.findById(idInvitacion).orElse(null);
    }

    public InvitacionesEntity save(InvitacionesEntity invitaciones)
    {
        return this.invitacionesRepository.save(invitaciones);
    }
    //Validar si un id existe
    public boolean exists(int idInvitaciones)
    {
        return this.invitacionesRepository.existsById(idInvitaciones);
    }

    public void delete(int idInvitacion){
        this.invitacionesRepository.deleteById(idInvitacion);
    }
    //Crear invitacion con usuario
    public InvitacionesEntity crearInvitacion(Integer idUsuario, Integer idGrupo) {
        long count = invitacionesRepository.countByUsuarioInvitacionesIdAndGrupoAhorroInvitacionId(idUsuario, idGrupo);
        if(count >= 2) {
            // Lanzar una excepción o devolver null
            throw new IllegalArgumentException("Ya existen 2 invitaciones para este usuario y grupo.");
        }
        // Generar un código de invitación único.
        String codigoInvitacion = UUID.randomUUID().toString(); //UUID genera un codigo unico

        InvitacionesEntity nuevaInvitacion = new InvitacionesEntity();
        nuevaInvitacion.setIdUsuario(idUsuario);   //Asignarle el idUsuario
        nuevaInvitacion.setIdGrupoAhorro(idGrupo); //Asignarle el idGrupo
        nuevaInvitacion.setCodigoInvitacion(codigoInvitacion); //Asignar el codigo de la invitacion

        // Guardar la nueva invitación en la base de datos
        return invitacionesRepository.save(nuevaInvitacion);
    }

    //Usar las invitaciones para entrar a un grupo

    public boolean unirseAlGrupo(String codigoInvitacion, Integer idUsuario) {
        Optional<InvitacionesEntity> optionalInvitacion = invitacionesRepository.findByCodigoInvitacion(codigoInvitacion);

        if (!optionalInvitacion.isPresent() || optionalInvitacion.get().isUsado()) {
            return false;
        }
        InvitacionesEntity invitacion = optionalInvitacion.get();

        UsuarioEntity usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            return false;
        }

        GrupoAhorroEntity grupo = grupoAhorroRepository.findById(invitacion.getIdGrupoAhorro()).orElse(null);
        if (grupo == null) {
            return false;
        }

        usuario.getGrupoAhorro().add(grupo);
        usuarioRepository.save(usuario);

        // Opcional: marcar invitación como usada
        invitacion.setUsado(true);
        invitacionesRepository.save(invitacion);

        return true;
    }
}

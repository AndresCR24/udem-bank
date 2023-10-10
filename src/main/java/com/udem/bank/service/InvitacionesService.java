package com.udem.bank.service;

import com.udem.bank.persistence.entity.GrupoAhorroEntity;
import com.udem.bank.persistence.entity.InvitacionesEntity;
import com.udem.bank.persistence.repository.InvitacionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class InvitacionesService
{
    private final InvitacionesRepository invitacionesRepository;

    @Autowired
    public InvitacionesService(InvitacionesRepository invitacionesRepository) {
        this.invitacionesRepository = invitacionesRepository;
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
}

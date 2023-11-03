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
    //Repositorios inyecatados que se utilizan para interactuar con CRUD REPOSITORY que maneja todos los metodos Spring
    private final InvitacionesRepository invitacionesRepository;
    private final UsuarioRepository usuarioRepository;
    private final GrupoAhorroRepository grupoAhorroRepository;

    //Constructor de aqui se inyectan las dependencias la etiqueta @Autowired le indica a Spring que debe inyectar desde
    //aqui automaticamente
    @Autowired
    public InvitacionesService(InvitacionesRepository invitacionesRepository, UsuarioRepository usuarioRepository, GrupoAhorroRepository grupoAhorroRepository) {
        this.invitacionesRepository = invitacionesRepository;
        this.usuarioRepository = usuarioRepository;
        this.grupoAhorroRepository = grupoAhorroRepository;
    }

    //Metodo que devuelve todas las invitaciones generadas
    public List<InvitacionesEntity> getAll()
    {
        return this.invitacionesRepository.findAll();
    }

    //Metodo que devuelve la invitacion por su ID
    public InvitacionesEntity get(int idInvitacion)
    {
        return this.invitacionesRepository.findById(idInvitacion).orElse(null);
    }

    //Metodo para guardar o actualizar una invitación
    public InvitacionesEntity save(InvitacionesEntity invitaciones)
    {
        return this.invitacionesRepository.save(invitaciones);
    }

    //Validar si un id existe
    public boolean exists(int idInvitaciones)
    {
        return this.invitacionesRepository.existsById(idInvitaciones);
    }

    //Metodo que elimina una invitación basada en su ID.
    public void delete(int idInvitacion){
        this.invitacionesRepository.deleteById(idInvitacion);
    }

    //Metodo para Crear invitacion con usuario con limite de 2 invitaciones por grupo
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

    //Metodo(servicio) para usar las invitaciones y entrar a un grupo de ahorro
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

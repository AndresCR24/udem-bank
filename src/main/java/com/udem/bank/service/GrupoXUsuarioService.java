package com.udem.bank.service;

import com.udem.bank.persistence.entity.GrupoAhorroEntity;
import com.udem.bank.persistence.entity.PrestamoUsuarioEntity;
import com.udem.bank.persistence.entity.UsuarioEntity;
import com.udem.bank.persistence.repository.GrupoAhorroRepository;
import com.udem.bank.persistence.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GrupoXUsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final GrupoAhorroRepository grupoAhorroRepository;

    @Autowired
    public GrupoXUsuarioService(UsuarioRepository usuarioRepository, GrupoAhorroRepository grupoAhorroRepository) {
        this.usuarioRepository = usuarioRepository;
        this.grupoAhorroRepository = grupoAhorroRepository;
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

}

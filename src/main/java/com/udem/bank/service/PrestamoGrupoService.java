package com.udem.bank.service;

import com.udem.bank.persistence.entity.GrupoAhorroEntity;
import com.udem.bank.persistence.entity.PrestamoGrupoEntity;
import com.udem.bank.persistence.entity.UsuarioEntity;
import com.udem.bank.persistence.repository.GrupoAhorroRepository;
import com.udem.bank.persistence.repository.PrestamoGrupoRepository;
import com.udem.bank.persistence.repository.UsuarioRepository;
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

    //Solicitar un prestamo a un grupo de ahorro
    public PrestamoGrupoEntity solicitarPrestamo(Integer idUsuario, Integer idGrupo, BigDecimal monto) {
        // Verificar si el usuario y el grupo existen
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        GrupoAhorroEntity grupo = grupoAhorroRepository.findById(idGrupo)
                .orElseThrow(() -> new IllegalArgumentException("Grupo de ahorro no encontrado"));

        // Validar si el usuario pertenece al grupo
        if (!grupo.getUsuarios().contains(usuario)) {
            throw new IllegalArgumentException("El usuario no pertenece al grupo de ahorro");
        }

        // Realizar otras validaciones según tus reglas de negocio

        // Crear la entidad de préstamo
        PrestamoGrupoEntity prestamo = new PrestamoGrupoEntity();
        //prestamo.setUsuario(usuario);
        prestamo.setGrupoAhorro(grupo);
        prestamo.setMonto(monto);
        //prestamo.setEstado("Pendiente"); // Puedes definir estados como "Pendiente", "Aprobado", "Rechazado", etc.

        // Guardar la solicitud de préstamo en la base de datos
        prestamoGrupoRepository.save(prestamo);

        // Puedes realizar notificaciones aquí, por ejemplo, notificar al grupo sobre la solicitud.

        return prestamo;
    }
}

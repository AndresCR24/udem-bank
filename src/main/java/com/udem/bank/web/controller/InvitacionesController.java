package com.udem.bank.web.controller;

import com.udem.bank.persistence.entity.InvitacionesEntity;
import com.udem.bank.persistence.entity.PrestamoGrupoEntity;
import com.udem.bank.service.InvitacionesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invitaciones")
public class InvitacionesController
{
    private final InvitacionesService invitacionesService;

    public InvitacionesController(InvitacionesService invitacionesService) {
        this.invitacionesService = invitacionesService;
    }

    @GetMapping
    public ResponseEntity<List<InvitacionesEntity>> getAll()
    {
        return ResponseEntity.ok(this.invitacionesService.getAll());
    }

    @GetMapping("/{idInvitacion}")
    public ResponseEntity<InvitacionesEntity> get(@PathVariable int idInvitacion)
    {
        return ResponseEntity.ok(this.invitacionesService.get(idInvitacion));
    }

    @PostMapping
    public ResponseEntity<InvitacionesEntity> add(@RequestBody InvitacionesEntity invitaciones)
    {
        if (invitaciones.getId() == null || !this.invitacionesService.exists(invitaciones.getId()))
        {
            return ResponseEntity.ok(this.invitacionesService.save(invitaciones));
        }
        return ResponseEntity.badRequest().build();
    }
    @PutMapping
    public ResponseEntity<InvitacionesEntity> update(@RequestBody InvitacionesEntity invitaciones)
    {
        if(invitaciones.getId() != null && this.invitacionesService.exists(invitaciones.getId()))
        {
            return ResponseEntity.ok(this.invitacionesService.save(invitaciones));
        }

        return ResponseEntity.badRequest().build();
    }
    @DeleteMapping("/{idInvitacion}")
    public ResponseEntity<Void> delete(@PathVariable int idInvitacion){
        if (invitacionesService.exists(idInvitacion)){
            invitacionesService.delete(idInvitacion);
            ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();

    }

    //Crear invitacion con usuario
    @PostMapping("/crear/{idUsuario}/grupo/{idGrupo}")
    public ResponseEntity<InvitacionesEntity> crearInvitacion(@PathVariable Integer idUsuario, @PathVariable Integer idGrupo) {
        try {
            InvitacionesEntity invitacionCreada = invitacionesService.crearInvitacion(idUsuario, idGrupo);
            return ResponseEntity.ok(invitacionCreada);
        } catch (Exception e)
        {
            // Aquí puedes manejar cualquier excepción que pueda surgir durante la creación de la invitación.
            return ResponseEntity.badRequest().body(null);
        }
    }

    //Controlador para que un usuario entre con la invitacion al grupo
    @PutMapping("/unirse/{codigoInvitacion}/usuario/{idUsuario}")
    public ResponseEntity<Boolean> unirseAlGrupo(@PathVariable String codigoInvitacion, @PathVariable Integer idUsuario) {
        boolean resultado = invitacionesService.unirseAlGrupo(codigoInvitacion, idUsuario);
        if (!resultado) {
            return ResponseEntity.badRequest().body(false);
        }
        return ResponseEntity.ok(true);
    }
}

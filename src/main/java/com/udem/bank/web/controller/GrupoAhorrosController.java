package com.udem.bank.web.controller;

import com.udem.bank.persistence.entity.CuentaAhorrosEntity;
import com.udem.bank.persistence.entity.GrupoAhorroEntity;
import com.udem.bank.service.GrupoAhorrosService;
import com.udem.bank.service.GrupoXUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grupo")
public class GrupoAhorrosController
{
    private final GrupoAhorrosService grupoAhorrosService;
    private final GrupoXUsuarioService grupoXUsuarioService;

    @Autowired
    public GrupoAhorrosController(GrupoAhorrosService grupoAhorrosService, GrupoXUsuarioService grupoXUsuarioService) {
        this.grupoAhorrosService = grupoAhorrosService;
        this.grupoXUsuarioService = grupoXUsuarioService;
    }

    @GetMapping
    public ResponseEntity<List<GrupoAhorroEntity>> getAll()
    {
        return ResponseEntity.ok(this.grupoAhorrosService.getAll());
    }

    @GetMapping("/{idGrupo}")
    public ResponseEntity<GrupoAhorroEntity> get(@PathVariable int idGrupo)
    {
        return ResponseEntity.ok(this.grupoAhorrosService.get(idGrupo));
    }

    @PostMapping
    public ResponseEntity<GrupoAhorroEntity> add(@RequestBody GrupoAhorroEntity grupoAhorro)
    {
        if (grupoAhorro.getId() == null || !this.grupoAhorrosService.exists(grupoAhorro.getId()))
        {
            return ResponseEntity.ok(this.grupoAhorrosService.save(grupoAhorro));
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping
    public ResponseEntity<GrupoAhorroEntity> update(@RequestBody GrupoAhorroEntity grupoAhorro, @PathVariable Integer idUsuario)
    {
        if(grupoAhorro.getId() != null && this.grupoAhorrosService.exists(grupoAhorro.getId()))
        {
            return ResponseEntity.ok(this.grupoAhorrosService.save(grupoAhorro));
        }

        return ResponseEntity.badRequest().build();
    }
   @DeleteMapping("/{idGrupo}")
    public ResponseEntity<Void>delete(@PathVariable int idGrupo){
        if (this.grupoAhorrosService.exists(idGrupo)){
            this.grupoXUsuarioService.deleteGrupoAhorro(idGrupo); //Modificacion al metodo delete original para
            return ResponseEntity.ok().build();                   //poder eliminar si hay usuarios relacionados
        }
        return ResponseEntity.badRequest().build();
    }



    //Crear un grupo de ahorro con usuario
    @PostMapping("/crearConUsuario/{idUsuario}")
    public ResponseEntity<GrupoAhorroEntity> add2(@RequestBody GrupoAhorroEntity grupoAhorro, @PathVariable int idUsuario)
    {
        return ResponseEntity.ok(this.grupoXUsuarioService.crearGrupoConUsuario(grupoAhorro, idUsuario));

    }
    //Agregar usaurios a un grupo de ahorro con limite de 3 grupos de ahorro
    @PutMapping("/asociarUsuario/{idUsuario}/{idGrupo}")
    public ResponseEntity<String> asociarUsuarioAGrupo(@PathVariable Integer idUsuario, @PathVariable Integer idGrupo) {
        boolean success = grupoXUsuarioService.asociarUsuarioAGrupo(idUsuario, idGrupo);

        if (!success) {
            return ResponseEntity.badRequest().body("El usuario ya está asociado a 3 grupos de ahorro.");
        }

        return ResponseEntity.ok("Usuario asociado exitosamente.");
    }

    @DeleteMapping("/eliminar/{idGrupo}")
    public ResponseEntity<String> eliminarGrupoAhorro(@PathVariable int idGrupo) {
        System.out.println("Intentando eliminar el grupo con ID: " + idGrupo);  // Log statement
        try {
            grupoXUsuarioService.eliminarGrupoAhorro(idGrupo);
            return new ResponseEntity<>("Grupo de ahorro eliminado con éxito.", HttpStatus.OK);


        } catch (RuntimeException e) {
            e.printStackTrace();  // This will print the exception stack trace
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    //Aumentar saldo 10% al grupo con mas ingreso
    @PostMapping("/aumentarSaldoAlGrupoConMasIngresos")
    public ResponseEntity<Void> aumentarSaldoAlGrupoConMasIngresos() {
        grupoAhorrosService.aumentarSaldoAlGrupoConMasIngresos();
        return ResponseEntity.ok().build();
    }

}

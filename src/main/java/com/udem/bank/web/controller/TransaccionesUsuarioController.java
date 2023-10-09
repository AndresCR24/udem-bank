package com.udem.bank.web.controller;

import com.udem.bank.persistence.entity.PrestamoGrupoEntity;
import com.udem.bank.persistence.entity.TransaccionesUsuarioEntity;
import com.udem.bank.service.TransaccionesUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaccionesusuario")
public class TransaccionesUsuarioController {

    private final TransaccionesUsuarioService transaccionesUsuarioService;

    @Autowired
    public TransaccionesUsuarioController(TransaccionesUsuarioService transaccionesUsuarioService) {
        this.transaccionesUsuarioService = transaccionesUsuarioService;
    }


    @GetMapping
    public ResponseEntity<List<TransaccionesUsuarioEntity>> getAll()
    {
        return ResponseEntity.ok(this.transaccionesUsuarioService.getAll());
    }

    @GetMapping("/{idPrestamoGrupo}")
    public ResponseEntity<TransaccionesUsuarioEntity> get(@PathVariable int idTransaccion)
    {
        return ResponseEntity.ok(this.transaccionesUsuarioService.get(idTransaccion));
    }

    @PostMapping
    public ResponseEntity<TransaccionesUsuarioEntity> add(@RequestBody TransaccionesUsuarioEntity transaccionesUsuario)
    {
        if (transaccionesUsuario.getId() == null || !this.transaccionesUsuarioService.exists(transaccionesUsuario.getId()))
        {
            return ResponseEntity.ok(this.transaccionesUsuarioService.save(transaccionesUsuario));
        }
        return ResponseEntity.badRequest().build();
    }
    @PutMapping
    public ResponseEntity<TransaccionesUsuarioEntity> update(@RequestBody TransaccionesUsuarioEntity transaccionesUsuario)
    {
        if(transaccionesUsuario.getId() != null && this.transaccionesUsuarioService.exists(transaccionesUsuario.getId()))
        {
            return ResponseEntity.ok(this.transaccionesUsuarioService.save(transaccionesUsuario));
        }

        return ResponseEntity.badRequest().build();
    }
    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable int idTransaccion){
        if (this.transaccionesUsuarioService.exists(idTransaccion)){
            this.transaccionesUsuarioService.deleteTransaccion(idTransaccion);
            ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}

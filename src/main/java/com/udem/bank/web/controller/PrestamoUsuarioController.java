package com.udem.bank.web.controller;

import com.udem.bank.persistence.entity.PrestamoGrupoEntity;
import com.udem.bank.persistence.entity.PrestamoUsuarioEntity;
import com.udem.bank.service.PrestamoUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamousuario")
public class PrestamoUsuarioController {

    private final PrestamoUsuarioService prestamoUsuarioService;

    @Autowired
    public PrestamoUsuarioController(PrestamoUsuarioService prestamoUsuarioService) {
        this.prestamoUsuarioService = prestamoUsuarioService;
    }


    @GetMapping
    public ResponseEntity<List<PrestamoUsuarioEntity>> getAll()
    {
        return ResponseEntity.ok(this.prestamoUsuarioService.getAll());
    }

    @GetMapping("/{idPrestamoUsuario}")
    public ResponseEntity<PrestamoUsuarioEntity> get(@PathVariable int idPrestamoUsuario)
    {
        return ResponseEntity.ok(this.prestamoUsuarioService.get(idPrestamoUsuario));
    }

    @PostMapping
    public ResponseEntity<PrestamoUsuarioEntity> add(@RequestBody PrestamoUsuarioEntity prestamoUsuario)
    {
        if (prestamoUsuario.getId() == null || !this.prestamoUsuarioService.exists(prestamoUsuario.getId()))
        {
            return ResponseEntity.ok(this.prestamoUsuarioService.save(prestamoUsuario));
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping
    public ResponseEntity<PrestamoUsuarioEntity> update(@RequestBody PrestamoUsuarioEntity prestamoUsuario)
    {
        if(prestamoUsuario.getId() != null && this.prestamoUsuarioService.exists(prestamoUsuario.getId()))
        {
            return ResponseEntity.ok(this.prestamoUsuarioService.save(prestamoUsuario));
        }

        return ResponseEntity.badRequest().build();
    }
    @DeleteMapping
    public ResponseEntity<Void>delete(@PathVariable int idPrestamo){
        if (this.prestamoUsuarioService.exists(idPrestamo)){
            this.prestamoUsuarioService.deletePrestamo(idPrestamo);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    //Pagar deuda
}

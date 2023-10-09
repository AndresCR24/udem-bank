package com.udem.bank.web.controller;

import com.udem.bank.persistence.entity.CuentaAhorrosEntity;
import com.udem.bank.persistence.entity.UsuarioEntity;
import com.udem.bank.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    //Operaciones basicas CRUD
    @GetMapping
    public ResponseEntity<List<UsuarioEntity>> getAll()
    {
        return ResponseEntity.ok(this.usuarioService.getAll());
    }

    @GetMapping("/{idusuario}")
    public ResponseEntity<UsuarioEntity> get(@PathVariable int idUSuario)
    {
        return ResponseEntity.ok(this.usuarioService.get(idUSuario));
    }

    @PostMapping
    public ResponseEntity<UsuarioEntity> add(@RequestBody UsuarioEntity usuario)
    {
        return ResponseEntity.ok(this.usuarioService.save(usuario));
    }

    //controlador para actualizar
    @PutMapping
    public ResponseEntity<UsuarioEntity> update(@RequestBody UsuarioEntity usuario)
    {
        if(usuario.getId() != null && this.usuarioService.exists(usuario.getId()))
        {
            return ResponseEntity.ok(this.usuarioService.save(usuario));
        }

        return ResponseEntity.badRequest().build();
    }
    @DeleteMapping("/{id_usuario}")
    public ResponseEntity<Void> delete(@PathVariable int idUsuario){
        if (this.usuarioService.exists(idUsuario)){
            this.usuarioService.deleteUsuario(idUsuario);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}

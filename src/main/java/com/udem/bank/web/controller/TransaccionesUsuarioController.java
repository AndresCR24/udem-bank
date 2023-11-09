package com.udem.bank.web.controller;

import com.udem.bank.persistence.entity.PrestamoGrupoEntity;
import com.udem.bank.persistence.entity.TransaccionesUsuarioEntity;
import com.udem.bank.service.PrestamoGrupoService;
import com.udem.bank.service.TransaccionesUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;



import java.util.Map;

@RestController
@RequestMapping("/api/transaccionesusuario")
public class TransaccionesUsuarioController {

    private final TransaccionesUsuarioService transaccionesUsuarioService;
    private final PrestamoGrupoService prestamoGrupoService;

    public TransaccionesUsuarioController(TransaccionesUsuarioService transaccionesUsuarioService, PrestamoGrupoService prestamoGrupoService) {
        this.transaccionesUsuarioService = transaccionesUsuarioService;
        this.prestamoGrupoService = prestamoGrupoService;
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

    @PostMapping("/depositar/{idUsuario}/grupo/{idGrupo}")
    public ResponseEntity<TransaccionesUsuarioEntity> depositarMontoAGrupo(@PathVariable Integer idUsuario,
                                                                           @PathVariable Integer idGrupo,
                                                                           @RequestBody Map<String, BigDecimal> requestMap) {
        BigDecimal monto = requestMap.get("monto");
        if (monto == null) {
            return ResponseEntity.badRequest().body(null);
        }
        TransaccionesUsuarioEntity transaccion = transaccionesUsuarioService.registrarDepositoAGrupo(idUsuario, idGrupo, monto);
        return ResponseEntity.ok(transaccion);
    }

}

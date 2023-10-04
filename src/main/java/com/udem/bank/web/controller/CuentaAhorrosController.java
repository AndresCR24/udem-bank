package com.udem.bank.web.controller;

import com.udem.bank.persistence.entity.CuentaAhorrosEntity;
import com.udem.bank.persistence.entity.UsuarioEntity;
import com.udem.bank.service.CuentaAhorrosService;
import com.udem.bank.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuenta")
public class CuentaAhorrosController {

    private final CuentaAhorrosService cuentaAhorrosService;

    @Autowired
    public CuentaAhorrosController(CuentaAhorrosService cuentaAhorrosService) {
        this.cuentaAhorrosService = cuentaAhorrosService;
    }

    @GetMapping
    public ResponseEntity<List<CuentaAhorrosEntity>> getAll()
    {
        return ResponseEntity.ok(this.cuentaAhorrosService.getAll());
    }

    @GetMapping("/{idcuenta}")
    public ResponseEntity<CuentaAhorrosEntity> get(@PathVariable int idCuenta)
    {
        return ResponseEntity.ok(this.cuentaAhorrosService.get(idCuenta));
    }

    @PostMapping
    public ResponseEntity<CuentaAhorrosEntity> add(@RequestBody CuentaAhorrosEntity cuenta)
    {
        return ResponseEntity.ok(this.cuentaAhorrosService.save(cuenta));
    }

}

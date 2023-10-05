package com.udem.bank.web.controller;

import com.udem.bank.persistence.entity.CuentaAhorrosEntity;
import com.udem.bank.persistence.entity.GrupoAhorroEntity;
import com.udem.bank.service.GrupoAhorrosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grupo")
public class GrupoAhorrosController
{
    private final GrupoAhorrosService grupoAhorrosService;

    @Autowired
    public GrupoAhorrosController(GrupoAhorrosService grupoAhorrosService) {
        this.grupoAhorrosService = grupoAhorrosService;
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
    public ResponseEntity<GrupoAhorroEntity> update(@RequestBody GrupoAhorroEntity grupoAhorro)
    {
        if(grupoAhorro.getId() != null && this.grupoAhorrosService.exists(grupoAhorro.getId()))
        {
            return ResponseEntity.ok(this.grupoAhorrosService.save(grupoAhorro));
        }

        return ResponseEntity.badRequest().build();
    }
}

package com.udem.bank.web.controller;

import com.udem.bank.persistence.entity.CuentaAhorrosEntity;
import com.udem.bank.persistence.entity.PrestamoGrupoEntity;
import com.udem.bank.service.PrestamoGrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamosgrupo")
public class PrestamosGrupoController {

    private final PrestamoGrupoService prestamoGrupoService;

    @Autowired
    public PrestamosGrupoController(PrestamoGrupoService prestamoGrupoService) {
        this.prestamoGrupoService = prestamoGrupoService;
    }

    @GetMapping
    public ResponseEntity<List<PrestamoGrupoEntity>> getAll()
    {
        return ResponseEntity.ok(this.prestamoGrupoService.getAll());
    }

    @GetMapping("/{idPrestamoGrupo}")
    public ResponseEntity<PrestamoGrupoEntity> get(@PathVariable int idPrestamoGrupo)
    {
        return ResponseEntity.ok(this.prestamoGrupoService.get(idPrestamoGrupo));
    }

    @PostMapping
    public ResponseEntity<PrestamoGrupoEntity> add(@RequestBody PrestamoGrupoEntity prestamoGrupo)
    {
        if (prestamoGrupo.getId() == null || !this.prestamoGrupoService.exists(prestamoGrupo.getId()))
        {
            return ResponseEntity.ok(this.prestamoGrupoService.save(prestamoGrupo));
        }
        return ResponseEntity.badRequest().build();
    }
    @PutMapping
    public ResponseEntity<PrestamoGrupoEntity> update(@RequestBody PrestamoGrupoEntity prestamoGrupo)
    {
        if(prestamoGrupo.getId() != null && this.prestamoGrupoService.exists(prestamoGrupo.getId()))
        {
            return ResponseEntity.ok(this.prestamoGrupoService.save(prestamoGrupo));
        }

        return ResponseEntity.badRequest().build();
    }
    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable int idPrestamoGrupo){
        if (prestamoGrupoService.exists(idPrestamoGrupo)){
            prestamoGrupoService.deletePrestamoGrupo(idPrestamoGrupo);
            ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();

    }
}

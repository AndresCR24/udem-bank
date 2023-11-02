package com.udem.bank.web.controller;

import com.udem.bank.persistence.entity.CuentaAhorrosEntity;
import com.udem.bank.persistence.entity.PrestamoGrupoEntity;
import com.udem.bank.persistence.entity.UsuarioEntity;
import com.udem.bank.service.PrestamoGrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prestamosgrupo")
public class PrestamosGrupoController {

    private final PrestamoGrupoService prestamoGrupoService;

    @Autowired
    public PrestamosGrupoController(PrestamoGrupoService prestamoGrupoService) {
        this.prestamoGrupoService = prestamoGrupoService;
    }

    @GetMapping
    public ResponseEntity<List<PrestamoGrupoEntity>> getAll() {
        return ResponseEntity.ok(this.prestamoGrupoService.getAll());
    }

    @GetMapping("/{idPrestamoGrupo}")
    public ResponseEntity<PrestamoGrupoEntity> get(@PathVariable int idPrestamoGrupo) {
        return ResponseEntity.ok(this.prestamoGrupoService.get(idPrestamoGrupo));
    }

    @PostMapping
    public ResponseEntity<PrestamoGrupoEntity> add(@RequestBody PrestamoGrupoEntity prestamoGrupo) {
        if (prestamoGrupo.getId() == null || !this.prestamoGrupoService.exists(prestamoGrupo.getId())) {
            return ResponseEntity.ok(this.prestamoGrupoService.save(prestamoGrupo));
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping
    public ResponseEntity<PrestamoGrupoEntity> update(@RequestBody PrestamoGrupoEntity prestamoGrupo) {
        if (prestamoGrupo.getId() != null && this.prestamoGrupoService.exists(prestamoGrupo.getId())) {
            return ResponseEntity.ok(this.prestamoGrupoService.save(prestamoGrupo));
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable int idPrestamoGrupo) {
        if (prestamoGrupoService.exists(idPrestamoGrupo)) {
            prestamoGrupoService.deletePrestamoGrupo(idPrestamoGrupo);
            ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();

    }

    @PostMapping("/solicitar/{idUsuario}/grupo/{idGrupo}")
    public ResponseEntity<?> solicitarPrestamoAGrupo(@PathVariable Integer idUsuario, @PathVariable Integer idGrupo,
                                                     @RequestBody Map<String, Object> requestMap) {
        BigDecimal monto = new BigDecimal(requestMap.get("monto").toString());
        Integer plazoPrestamo = Integer.valueOf(requestMap.get("plazoPrestamo").toString());

        try {
            PrestamoGrupoEntity prestamo = prestamoGrupoService.solicitarPrestamo(idUsuario, idGrupo, monto, plazoPrestamo);
            return ResponseEntity.ok(prestamo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Controlle de solicitar prestamo a un grupo de ahorro al que no se pertenece
    @PostMapping("/solicitarAOtroGrupo/{idUsuario}/grupo/{idGrupo}")
    public ResponseEntity<?> solicitarPrestamoAGrupoNoPertece(@PathVariable Integer idUsuario, @PathVariable Integer idGrupo,
                                                              @RequestBody Map<String, Object> requestMap) {
        BigDecimal monto = new BigDecimal(requestMap.get("monto").toString());
        Integer plazoPrestamo = Integer.valueOf(requestMap.get("plazoPrestamo").toString());

        try {
            PrestamoGrupoEntity prestamo = prestamoGrupoService.solicitarPrestamoAotroGrupo(idUsuario, idGrupo, monto, plazoPrestamo);
            return ResponseEntity.ok(prestamo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
    @PostMapping("/{idPrestamo}/pagar")
    public ResponseEntity<?> pagarPrestamo(@PathVariable Integer idPrestamo, @RequestBody Map<String, Object> requestMap) {
        Integer idUsuario = Integer.valueOf(requestMap.get("idUsuario").toString());
        BigDecimal monto = new BigDecimal(requestMap.get("monto").toString());

        try {
            PrestamoGrupoEntity prestamo = prestamoGrupoService.pagarPrestamo(idUsuario, idPrestamo, monto);
            return ResponseEntity.ok(prestamo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


     */

}

package com.udem.bank.web.controller;

import com.udem.bank.persistence.entity.InvitacionesEntity;
import com.udem.bank.persistence.entity.PagoDeudaEntity;
import com.udem.bank.persistence.entity.PrestamoGrupoEntity;
import com.udem.bank.service.PagoDeudaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pago")
public class PagoDeudaController {

    private final PagoDeudaService pagoDeudaService;

    @Autowired
    public PagoDeudaController(PagoDeudaService pagoDeudaService) {
        this.pagoDeudaService = pagoDeudaService;
    }

    @GetMapping
    public ResponseEntity<List<PagoDeudaEntity>> getAll()
    {
        return ResponseEntity.ok(this.pagoDeudaService.getAll());
    }

    @GetMapping("/{idPago}")
    public ResponseEntity<PagoDeudaEntity> get(@PathVariable int idInvitacion)
    {
        return ResponseEntity.ok(this.pagoDeudaService.get(idInvitacion));
    }

    @PostMapping("/pagar")
    public void pagarDeudaAGrupo(@RequestBody Map<String, Object> request) {
        int idPrestamo = (Integer) request.get("idPrestamo");
        BigDecimal montoPago = new BigDecimal(request.get("montoPago").toString());

        pagoDeudaService.pagarDeudaAGrupo(idPrestamo, montoPago);
    }

}

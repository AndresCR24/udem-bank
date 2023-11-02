package com.udem.bank.service;

import com.udem.bank.persistence.entity.*;
import com.udem.bank.persistence.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PagoDeudaService {
    //Creacion de una constante statica para poder entregar el porcentaje que le corresponde a UdemBank
    private static final BigDecimal COMMISSION_RATE = new BigDecimal("0.01");

    private final PagoDeudaRepository pagoDeudaRepository;
    private final PrestamoGrupoRepository prestamoGrupoRepository;
    private final GrupoAhorroRepository grupoAhorroRepository;
    private final TransaccionesUsuarioRepository transaccionesUsuarioRepository;
    private final PrestamoGrupoService prestamoGrupoService;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public PagoDeudaService(PagoDeudaRepository pagoDeudaRepository, PrestamoGrupoRepository prestamoGrupoRepository, GrupoAhorroRepository grupoAhorroRepository, TransaccionesUsuarioRepository transaccionesUsuarioRepository, PrestamoGrupoService prestamoGrupoService, UsuarioRepository usuarioRepository) {
        this.pagoDeudaRepository = pagoDeudaRepository;
        this.prestamoGrupoRepository = prestamoGrupoRepository;
        this.grupoAhorroRepository = grupoAhorroRepository;
        this.transaccionesUsuarioRepository = transaccionesUsuarioRepository;
        this.prestamoGrupoService = prestamoGrupoService;
        this.usuarioRepository = usuarioRepository;
    }

    //Devolver lista de cuentas
    public List<PagoDeudaEntity> getAll() {
        return this.pagoDeudaRepository.findAll();
    }

    //Devolver usuario por su id
    public PagoDeudaEntity get(int idInvitacion) {
        return this.pagoDeudaRepository.findById(idInvitacion).orElse(null);
    }

    //Pagar deuda
    @Transactional
    public void pagarDeudaAGrupo(int idPrestamo, BigDecimal montoPago) {
        // Retrieve the loan record
        Optional<PrestamoGrupoEntity> prestamoOpt = prestamoGrupoRepository.findById(idPrestamo);

        if (!prestamoOpt.isPresent()) {
            throw new RuntimeException("Préstamo no encontrado con el ID: " + idPrestamo);
        }

        PrestamoGrupoEntity prestamo = prestamoOpt.get();

        // Check user's balance
        UsuarioEntity usuario = prestamo.getUsuarioPrestamo();
        BigDecimal saldoActualUsuario = usuario.getCuentaAhorros().getSaldoActual();

        if(saldoActualUsuario.compareTo(montoPago) < 0) {
            throw new RuntimeException("Saldo insuficiente para realizar el pago.");
        }

        // Deduct the amount paid from the pending balance of the loan
        BigDecimal saldoPendiente = prestamo.getSaldoPendiente();
        saldoPendiente = saldoPendiente.subtract(montoPago);
        prestamo.setSaldoPendiente(saldoPendiente);

        // Deduct the amount paid from the user's balance
        saldoActualUsuario = saldoActualUsuario.subtract(montoPago);
        usuario.getCuentaAhorros().setSaldoActual(saldoActualUsuario);

        // Add the payment amount to the group's balance
        GrupoAhorroEntity grupo = prestamo.getGrupoAhorro();
        BigDecimal saldoActualGrupo = grupo.getSaldo();
        saldoActualGrupo = saldoActualGrupo.add(montoPago);
        grupo.setSaldo(saldoActualGrupo);

        //Comsion para UdemBank
        BigDecimal commission = montoPago.multiply(COMMISSION_RATE);
        //Buscar por id el grupo de UdemBank para realizar el pago
        GrupoAhorroEntity udemBankGroup = grupoAhorroRepository.findById(36).orElseThrow(
                () -> new RuntimeException("Grupo UdemBank no encontrado")
        );
        BigDecimal saldoActualUdemBank = udemBankGroup.getSaldo();
        saldoActualUdemBank = saldoActualUdemBank.add(commission);
        udemBankGroup.setSaldo(saldoActualUdemBank);

        PagoDeudaEntity nuevoPago = new PagoDeudaEntity();
        nuevoPago.setIdUsuario(usuario.getId());
        nuevoPago.setIdPrestamo(idPrestamo);
        nuevoPago.setMonto(montoPago);


        // guardando los datos
        prestamoGrupoRepository.save(prestamo);
        usuarioRepository.save(usuario);
        grupoAhorroRepository.save(grupo);
        grupoAhorroRepository.save(udemBankGroup);
        pagoDeudaRepository.save(nuevoPago);
    }

}

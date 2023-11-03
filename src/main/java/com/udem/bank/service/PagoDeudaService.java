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
    //Creacion de una constante para la comision de UdemBank
    private static final BigDecimal COMMISSION_BANCO = new BigDecimal("0.01");

    //Inyeccion de repositories para poder utilizar CRUD REPOSITORY y asi utilizar todos los metodos de Spring
    private final PagoDeudaRepository pagoDeudaRepository;
    private final PrestamoGrupoRepository prestamoGrupoRepository;
    private final GrupoAhorroRepository grupoAhorroRepository;
    private final TransaccionesUsuarioRepository transaccionesUsuarioRepository;
    private final PrestamoGrupoService prestamoGrupoService;
    private final UsuarioRepository usuarioRepository;

    //Constructor de aqui se inyectan las dependencias la etiqueta @Autowired le indica a Spring que debe inyectar desde
    //aqui automaticamente
    @Autowired
    public PagoDeudaService(PagoDeudaRepository pagoDeudaRepository, PrestamoGrupoRepository prestamoGrupoRepository, GrupoAhorroRepository grupoAhorroRepository, TransaccionesUsuarioRepository transaccionesUsuarioRepository, PrestamoGrupoService prestamoGrupoService, UsuarioRepository usuarioRepository) {
        this.pagoDeudaRepository = pagoDeudaRepository;
        this.prestamoGrupoRepository = prestamoGrupoRepository;
        this.grupoAhorroRepository = grupoAhorroRepository;
        this.transaccionesUsuarioRepository = transaccionesUsuarioRepository;
        this.prestamoGrupoService = prestamoGrupoService;
        this.usuarioRepository = usuarioRepository;
    }

    //Metodo(servicio) para devolver lista de pagos
    public List<PagoDeudaEntity> getAll() {
        return this.pagoDeudaRepository.findAll();
    }

    //Metodo(servicio) para devolver un pago por id
    public PagoDeudaEntity get(int idInvitacion) {
        return this.pagoDeudaRepository.findById(idInvitacion).orElse(null);
    }

    //Metodo(servicio) que le permite al usaurio pagar una deuda que tenga con un grupo de ahorro
    @Transactional
    public void pagarDeudaAGrupo(int idPrestamo, BigDecimal montoPago) {

        //consulta el registro del prestamo en la base de datos por su ID
        Optional<PrestamoGrupoEntity> prestamoOpt = prestamoGrupoRepository.findById(idPrestamo);

        //Verificar si el ID del prestamo fue encontrado -> si no fue lanza una excepcion
        if (!prestamoOpt.isPresent()) {
            throw new RuntimeException("Préstamo no encontrado con el ID: " + idPrestamo);
        }

        //Obtiene el registro del prestamo si si existe
        PrestamoGrupoEntity prestamo = prestamoOpt.get();

        //Se obtiene el usuario asociado al prestamo
        UsuarioEntity usuario = prestamo.getUsuarioPrestamo();

        //Se obtiene el saldo actual del usuario que esta asociado al prestamo
        BigDecimal saldoActualUsuario = usuario.getCuentaAhorros().getSaldoActual();

        //Verifica si el usuario tiene el suficiente saldo para realizar el pago
        if(saldoActualUsuario.compareTo(montoPago) < 0) {
            throw new RuntimeException("Saldo insuficiente para realizar el pago.");
        }

        //Calcula el nuevo saldo pendiente con el que queda el usuario luego de realizar el pago
        BigDecimal saldoPendiente = prestamo.getSaldoPendiente();
        saldoPendiente = saldoPendiente.subtract(montoPago);
        prestamo.setSaldoPendiente(saldoPendiente);

        //Le resta la cantidad pagada al dinero que tiene el usario en su cuenta de ahorro
        saldoActualUsuario = saldoActualUsuario.subtract(montoPago);
        usuario.getCuentaAhorros().setSaldoActual(saldoActualUsuario);

        //Agrega el pago al saldo asociado al grupo de ahorro
        GrupoAhorroEntity grupo = prestamo.getGrupoAhorro();
        BigDecimal saldoActualGrupo = grupo.getSaldo();
        saldoActualGrupo = saldoActualGrupo.add(montoPago);
        grupo.setSaldo(saldoActualGrupo);

        //Se calcula la comision que le corresponde a UdemBank por prestar el servicio
        BigDecimal commission = montoPago.multiply(COMMISSION_BANCO);

        //Busca el grupo que le corresponde a UdemBank por su ID
        GrupoAhorroEntity udemBankGroup = grupoAhorroRepository.findById(36)
                .orElseThrow(() -> new RuntimeException("Grupo UdemBank no encontrado"));

        //Agrega la comision al saldo del grupo de ahorro de UdemBank
        BigDecimal saldoActualUdemBank = udemBankGroup.getSaldo();
        saldoActualUdemBank = saldoActualUdemBank.add(commission);
        udemBankGroup.setSaldo(saldoActualUdemBank);

        //Crea el nuevo registro
        PagoDeudaEntity nuevoPago = new PagoDeudaEntity();
        nuevoPago.setIdUsuario(usuario.getId());
        nuevoPago.setIdPrestamo(idPrestamo);
        nuevoPago.setMonto(montoPago);


        //Guarda todos los registros actualizados en la base de datos
        prestamoGrupoRepository.save(prestamo);
        usuarioRepository.save(usuario);
        grupoAhorroRepository.save(grupo);
        grupoAhorroRepository.save(udemBankGroup);
        pagoDeudaRepository.save(nuevoPago);
    }

}

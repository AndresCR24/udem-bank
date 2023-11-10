package com.udem.bank.ServiceTests;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.udem.bank.persistence.entity.*;
import com.udem.bank.persistence.repository.*;
import com.udem.bank.service.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PagoDeudaServiceTest {

    @Mock
    private PagoDeudaRepository pagoDeudaRepository;

    @Mock
    private PrestamoGrupoRepository prestamoGrupoRepository;

    @Mock
    private GrupoAhorroRepository grupoAhorroRepository;

    @Mock
    private TransaccionesUsuarioRepository transaccionesUsuarioRepository;

    @Mock
    private PrestamoGrupoService prestamoGrupoService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private PagoDeudaService pagoDeudaService;

    public PagoDeudaServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPagarDeudaAGrupo_SaldoSuficiente() {
        int idPrestamo = 1;
        BigDecimal montoPago = new BigDecimal("50"); // Monto menor o igual al saldo actual
        GrupoAhorroEntity udemBankGroup = new GrupoAhorroEntity();
        udemBankGroup.setId(1); // Ajusta el ID según el ID real de Grupo UdemBank
        udemBankGroup.setSaldo(new BigDecimal("0"));
        when(grupoAhorroRepository.findById(1)).thenReturn(Optional.of(udemBankGroup));


        UsuarioEntity usuarioMock = new UsuarioEntity();
        CuentaAhorrosEntity cuentaAhorrosMock = new CuentaAhorrosEntity();
        GrupoAhorroEntity grupoAhorroMock = new GrupoAhorroEntity();
        cuentaAhorrosMock.setSaldoActual(new BigDecimal("100")); //
        usuarioMock.setCuentaAhorros(cuentaAhorrosMock);

        PrestamoGrupoEntity prestamoMock = new PrestamoGrupoEntity();
        prestamoMock.setSaldoPendiente(new BigDecimal("100"));
        prestamoMock.setUsuarioPrestamo(usuarioMock);
        prestamoMock.setGrupoAhorro(grupoAhorroMock);

        // Asignar el saldo al grupo de ahorro mock
        grupoAhorroMock.setSaldo(new BigDecimal("200")); // Saldo actual del grupo mock

        when(prestamoGrupoRepository.findById(idPrestamo)).thenReturn(Optional.of(prestamoMock));

        // Ejecución de la prueba
        pagoDeudaService.pagarDeudaAGrupo(idPrestamo, montoPago);


        // Verificación de que se realizó el pago correctamente
        verify(prestamoGrupoRepository, times(1)).findById(idPrestamo);
        verify(usuarioRepository, times(1)).save(usuarioMock);
        verify(grupoAhorroRepository, times(1)).save(grupoAhorroMock);
        verify(pagoDeudaRepository, times(1)).save(any());
        verify(grupoAhorroRepository, times(1)).save(udemBankGroup);
    }


    @Test
    public void testPagarDeudaAGrupo_SaldoInsuficiente() {
        // Configuración del escenario para las pruebas
        int idPrestamo = 1;
        BigDecimal montoPago = new BigDecimal("100");

        UsuarioEntity usuarioMock = new UsuarioEntity();
        CuentaAhorrosEntity cuentaAhorrosMock = new CuentaAhorrosEntity();
        GrupoAhorroEntity grupoAhorroMock = new GrupoAhorroEntity();
        cuentaAhorrosMock.setSaldoActual(new BigDecimal("50"));
        usuarioMock.setCuentaAhorros(cuentaAhorrosMock);

        PrestamoGrupoEntity prestamoMock = new PrestamoGrupoEntity();
        prestamoMock.setSaldoPendiente(new BigDecimal("200"));
        prestamoMock.setUsuarioPrestamo(usuarioMock);
        prestamoMock.setGrupoAhorro(grupoAhorroMock); // Asignar el grupo al préstamo

        when(prestamoGrupoRepository.findById(idPrestamo)).thenReturn(Optional.of(prestamoMock));

        // Ejecución de la prueba
        assertThrows(RuntimeException.class, () -> pagoDeudaService.pagarDeudaAGrupo(idPrestamo, montoPago));


        // Verificación de la prueba
        verify(prestamoGrupoRepository, times(1)).findById(idPrestamo);
        verify(usuarioRepository, never()).save(any());
        verify(grupoAhorroRepository, never()).save(any());
        verify(pagoDeudaRepository, never()).save(any());
    }
}

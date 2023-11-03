package com.udem.bank.service;

import com.udem.bank.persistence.entity.GrupoAhorroEntity;
import com.udem.bank.persistence.entity.UsuarioEntity;
import com.udem.bank.persistence.repository.GrupoAhorroRepository;
import com.udem.bank.persistence.repository.InvitacionesRepository;
import com.udem.bank.persistence.repository.UsuarioRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class GrupoXUsuarioService {
    //Comision que se queda el banco
    private static final BigDecimal COMMISSION_BANCO = new BigDecimal("0.005"); // 5% as a decimal

    //Inyeccion de repositorios para poder utilizar todos los metodos de CRUD REPOSITORY que ya trae SPRING
    private final UsuarioRepository usuarioRepository;

    private final GrupoAhorroRepository grupoAhorroRepository;

    //Inyectar invitaciones para poder relacionarlas con los grupos
    private final InvitacionesRepository invitacionesRepository;

    //Constructor de aqui se inyectan las dependencias la etiqueta @Autowired le indica a Spring que debe inyectar desde
    //aqui automaticamente
    @Autowired
    public GrupoXUsuarioService(UsuarioRepository usuarioRepository, GrupoAhorroRepository grupoAhorroRepository, InvitacionesRepository invitacionesRepository) {
        this.usuarioRepository = usuarioRepository;
        this.grupoAhorroRepository = grupoAhorroRepository;
        this.invitacionesRepository = invitacionesRepository;
    }

    //Metodo que crea un grupo de ahorro y le asocia un Usuario
    public GrupoAhorroEntity crearGrupoConUsuario(GrupoAhorroEntity grupo, Integer idUsuario) {
        // Primero, guarda el grupo
        GrupoAhorroEntity grupoGuardado = grupoAhorroRepository.save(grupo);

        // Luego, asocia el grupo con el usuario
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if(usuario != null) {
            usuario.getGrupoAhorro().add(grupoGuardado);
            usuarioRepository.save(usuario);
        }

        return grupoGuardado;
    }

    //Agrega un usuario a un grupo de ahorro
    public void addUsuarioToGrupo(Integer usuarioId, Integer grupoId) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId).orElse(null);
        GrupoAhorroEntity grupo = grupoAhorroRepository.findById(grupoId).orElse(null);

        grupo.getUsuarios().add(usuario);
        usuario.getGrupoAhorro().add(grupo);
        grupoAhorroRepository.save(grupo);
    }

    //Poder eliminar un grupo si tiene usuarios
    public void deleteGrupoAhorro(Integer idGrupo) {
        GrupoAhorroEntity grupo = grupoAhorroRepository.findById(idGrupo).orElse(null);

        if (grupo != null) {
            // Paso 2: Desvincular el Grupo de cada Usuario
            for (UsuarioEntity usuario : grupo.getUsuarios()) {
                usuario.getGrupoAhorro().remove(grupo);
                usuarioRepository.save(usuario);
            }

            // Paso 3: Eliminar el Grupo
            grupoAhorroRepository.delete(grupo);
        }
    }

    //Metodo para asegurar que no se este en mas de 3 grupos de ahorro
    public boolean asociarUsuarioAGrupo(Integer idUsuario, Integer idGrupo) {
        // Verificar cuántos grupos ya tiene el usuario
        long count = usuarioRepository.countGruposByUsuarioId(idUsuario);
        if (count >= 3)
        {
            // El usuario ya tiene 3 grupos asociados
            return false;
        }
        //Logica pra agregar un usuario a un grupo --> es la misma del metodo de arriba pero este ya no
        //se utiliza en el codigo
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario).orElse(null);
        GrupoAhorroEntity grupo = grupoAhorroRepository.findById(idGrupo).orElse(null);

        grupo.getUsuarios().add(usuario);
        usuario.getGrupoAhorro().add(grupo);
        grupoAhorroRepository.save(grupo);

        return true;
    }

    //Metodo para disolver un grupo de ahorro -> Distribuye el saldo entre los usuario del grupo de ahorro
    //Tambien entrega un comision del 5% a UdemBank por el servicio
    public void eliminarGrupoAhorro(Integer idGrupo) {
        // 1. Obtener el grupo de ahorro
        GrupoAhorroEntity grupo = grupoAhorroRepository.findById(idGrupo)
                .orElseThrow(() -> new RuntimeException("Grupo de ahorro no encontrado"));

        // Inicializar manualmente la colección
        //Toco hacer esto ya que en Hibernate automaticamente se cargan los datos de froma "lazy" y en este caso
        //Era necesario inicializarla explicitamente para poder acceder directaemente
        Hibernate.initialize(grupo.getUsuarios());

        // 2. Distribuir el saldo entre sus usuarios
        BigDecimal totalBalance = grupo.getSaldo(); //Se obtiene el saldo total del grupo
        BigDecimal comisionUdem = totalBalance.multiply(COMMISSION_BANCO); //Se calcula la comision con la constante
        BigDecimal balanceToDistribute = totalBalance.subtract(comisionUdem);//Se calcula el saldo que se entregara a los usuarios

        // Crear una lista temporal de los usuarios para no tener errores con modificaciones de los datos originales
        //mientras se estan realianzando las operaciones y consultas
        List<UsuarioEntity> tempUsuarios = new ArrayList<>(grupo.getUsuarios());
        //se itera sobre cada usaurio del grupo y se obtiene su saldo actual, su nuevo saldo, y se guarda el usaurio
        //con su nuevo saldo
        for (UsuarioEntity usuario : tempUsuarios) {
            BigDecimal userBalance = usuario.getCuentaAhorros().getSaldoActual();
            BigDecimal newBalance = userBalance.add(balanceToDistribute.divide(BigDecimal.valueOf(grupo.getUsuarios().size()), RoundingMode.HALF_UP));
            usuario.getCuentaAhorros().setSaldoActual(newBalance);
            usuarioRepository.save(usuario);  // Guardar el saldo actualizado del usuario
        }

        //Se busca el grupo de ahorro de UdemBank por su ID para que se le pase la comision correspondiente al saldo
        //de su grupo de ahorro
        GrupoAhorroEntity grupoUdemBank = grupoAhorroRepository.findById(36)
                .orElseThrow(() -> new RuntimeException("Grupo de ahorro UdemBank no encontrado"));
        grupoUdemBank.setSaldo(grupoUdemBank.getSaldo().add(comisionUdem));
        grupoAhorroRepository.save(grupoUdemBank);//Se guarda el saldo actualizado

        //Se utiliza el metodo anterior para eliminar un grupo de ahorro para realizar al final la eliminacion del mismo
        deleteGrupoAhorro(idGrupo);
    }


}

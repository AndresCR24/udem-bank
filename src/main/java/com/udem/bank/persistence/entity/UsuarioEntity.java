package com.udem.bank.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_cuenta_ahorros", nullable = false)
    private Integer idCuentaAhorros;

    @Column(nullable = false, length = 30)
    private String user;

    @Column(nullable = false, length = 20)
    private String password;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, updatable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    //Relaciones
    @OneToOne
    @JoinColumn(name = "id_cuenta_ahorros", referencedColumnName = "id", insertable = false, updatable = false)
    private CuentaAhorrosEntity cuentaAhorros;

    @OneToMany(mappedBy = "usuarioPrestamo")
    private List<PrestamoUsuarioEntity> prestamosUsuario;

    @OneToMany(mappedBy = "usuarioTransacciones")
    private List<TransaccionesUsuarioEntity> transaccionesUsuario;

    //Relacion ManyToMany
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "grupo_x_usuario", joinColumns = @JoinColumn(name = "id_usuario", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_grupo", referencedColumnName = "id")
    )
    private List<GrupoAhorroEntity> grupoAhorro;
}

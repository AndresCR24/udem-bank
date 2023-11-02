package com.udem.bank.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago_deuda")
@Getter
@Setter
@NoArgsConstructor
public class PagoDeudaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "id_prestamo", nullable = false)
    private Integer idPrestamo;

    @Column(name = "mes_pagado")
    private Integer mesPagado;

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    //Relaciones
    @ManyToOne
    private UsuarioEntity usuarioRelacion;

    @OneToOne
    private PrestamoGrupoEntity prestamoGrupoRelacion;
}

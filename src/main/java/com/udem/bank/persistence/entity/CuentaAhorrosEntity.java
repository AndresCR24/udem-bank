package com.udem.bank.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cuenta_ahorros")
@Getter
@Setter
@NoArgsConstructor
public class CuentaAhorrosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Integer id;

    @Column(name = "saldo_actual", nullable = false, columnDefinition = "DECIMAL(15,2)")
    private BigDecimal saldoActual;

    @Column(name = "saldo_inicial", nullable = false, columnDefinition = "DECIMAL(15,2)")
    private BigDecimal saldoInicial;
    @PrePersist
    public void prePersist() {
        this.saldoInicial = BigDecimal.ZERO;
    }

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

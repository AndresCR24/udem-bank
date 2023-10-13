package com.udem.bank.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacciones_usuario")
@Getter
@Setter
@NoArgsConstructor
public class TransaccionesUsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "id_grupo", nullable = false)
    private Integer idGrupo;

    @Column(nullable = false, columnDefinition = "DECIMAL(15,2)")
    private BigDecimal monto;

    @Column(name = "tipo_transaccion", length = 20)
    private String tipo;

    @Column(name = "saldo_final", columnDefinition = "DECIMAL(15,2)")
    private BigDecimal saldoFinal;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    //Relaciones
    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", insertable = false, updatable = false)
    private UsuarioEntity usuarioTransacciones;

    @ManyToOne
    @JoinColumn(name = "id_grupo", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    private GrupoAhorroEntity grupoAhorroTransacciones;
}

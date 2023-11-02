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
@Table(name = "prestamo_grupo")
@Getter
@Setter
@NoArgsConstructor
public class PrestamoGrupoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_grupo", nullable = false)
    private Integer idGrupo;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "plazo_prestamo", nullable = false)
    private Integer plazoPrestamo;

    @Column(nullable = false, columnDefinition = "DECIMAL(15,2)")
    private BigDecimal monto;

    @Column(name = "saldo_pendiente", columnDefinition = "DECIMAL(15,2)")
    private BigDecimal saldoPendiente;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    //Relaciones
    @ManyToOne
    @JoinColumn(name = "id_grupo", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    private GrupoAhorroEntity grupoAhorro;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    private UsuarioEntity usuarioPrestamo;
}

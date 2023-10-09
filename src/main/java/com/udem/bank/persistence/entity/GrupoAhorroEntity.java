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
import java.util.List;

@Entity
@Table(name = "grupo_de_ahorro")
@Getter
@Setter
@NoArgsConstructor
public class GrupoAhorroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_grupo", nullable = false, length = 30)
    private String nombreGrupo;

    @Column(nullable = false, columnDefinition = "DECIMAL(15,2)")
    private BigDecimal saldo;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    //Relaciones
    @ManyToMany(mappedBy = "grupoAhorro")
    private List<UsuarioEntity> usuarios;

    @OneToMany(mappedBy = "grupoAhorro")
    private List<PrestamoGrupoEntity> prestamoGrupo;
}

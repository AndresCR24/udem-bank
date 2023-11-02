package com.udem.bank.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "grupo_de_ahorro")
@SQLDelete(sql = "UPDATE grupo_de_ahorro SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
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

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    //Relaciones
    @ManyToMany(mappedBy = "grupoAhorro")

    private List<UsuarioEntity> usuarios;

    @OneToMany(mappedBy = "grupoAhorro")
    @JsonIgnore
    private List<PrestamoGrupoEntity> prestamoGrupo;

    @OneToMany(mappedBy = "grupoAhorroInvitacion")
    @JsonIgnore
    private List<InvitacionesEntity> invitacionesGrupo;

    @OneToMany(mappedBy = "grupoAhorroTransacciones")
    @JsonIgnore
    private List<TransaccionesUsuarioEntity> transaccionesUsuarioGrupo;
}

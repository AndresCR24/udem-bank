package com.udem.bank.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "invitaciones")
@Getter
@Setter
@NoArgsConstructor
public class InvitacionesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "id_cuenta_ahorro")
    private Integer idGrupoAhorro;

    @Column(name = "codigo_invitacion", unique = true, nullable = false)
    private String codigoInvitacion;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    //Relaciones
    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    private UsuarioEntity usuarioInvitaciones;

    @ManyToOne
    @JoinColumn(name = "id_cuenta_ahorro", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    private GrupoAhorroEntity grupoAhorroInvitacion;

}

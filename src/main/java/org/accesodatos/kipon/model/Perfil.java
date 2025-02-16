package org.accesodatos.kipon.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perfil")
    private Long id;

    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;

    @Column(unique = true)
    private String telefono;
    private String direccion;

    @OneToOne (fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name="id_usuario", nullable = false, unique = true)
    private Usuario usuario;
}

package org.accesodatos.kipon.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Hucha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id_hucha")
    private Long id;

    @Column(name = "nombre_hucha", nullable = false)
    private String nombre;

    @Column(name = "cantidad_total")
    private Double cantidadTotal;

    @Column(name = "objetivo_ahorro")
    private Double objetivoAhorro;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name= "fecha_objetivo")
    private LocalDate fechaObjetivo;

    @ManyToOne
    @JoinColumn(name="id_administrador", nullable = false)
    private Usuario administrador;

    @OneToMany(mappedBy = "hucha", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransaccionAhorro> transaccionesAhorro;

    @OneToMany(mappedBy = "hucha")
    private List<UsuarioHucha> usuarios;
}

package org.accesodatos.kipon.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "Transacciones_Ahorro")
public class Ahorro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ahorro")
    private Long id;

    private Double cantidad;
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name="id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name ="id_hucha", nullable = false)
    private Hucha hucha;

    @Column(name ="saldo_posterior", nullable = false)
    private Double cantidadPosterior;
}

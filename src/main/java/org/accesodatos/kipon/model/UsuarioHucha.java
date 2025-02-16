package org.accesodatos.kipon.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.accesodatos.kipon.model.key.UsuarioHuchaKey;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
public class UsuarioHucha {
    @EmbeddedId
    private UsuarioHuchaKey id;

    @ManyToOne
    @MapsId("idUsuario")
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @MapsId("idHucha")
    @JoinColumn(name = "id_hucha")
    private Hucha hucha;

    @Column(name = "fecha_ingreso")
    private LocalDate fechaIngreso;

    private String rol;

}

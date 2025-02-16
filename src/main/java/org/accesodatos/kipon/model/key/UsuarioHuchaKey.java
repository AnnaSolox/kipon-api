package org.accesodatos.kipon.model.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class UsuarioHuchaKey implements Serializable {

    @Column(name="id_usuario")
    private Long idUsuario;
    @Column(name="id_hucha")
    private Long idHucha;
}

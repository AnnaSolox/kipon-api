package org.accesodatos.kipon.dtos.response;

import lombok.Data;

@Data
public class UsuarioRolDTO {
    private UsuarioSimpleDTO usuario;
    private String rol;
}

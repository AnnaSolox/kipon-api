package org.accesodatos.kipon.dtos.response;

import lombok.Data;

import java.util.List;

@Data
public class UsuarioSimpleDTO {
    private Long id;
    private String nombreUsuario;
    private List<TransaccionAhorroDTO> transaccionAhorro;
}

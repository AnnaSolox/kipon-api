package org.accesodatos.kipon.dtos.response;

import lombok.Data;
import org.accesodatos.kipon.model.Hucha;

import java.time.LocalDate;
import java.util.List;

@Data
public class UsuarioDTO {
    private Long id;
    private String nombreUsuario;
    private String email;
    private LocalDate fechaRegistro;
    private PerfilDTO perfil;
    private List<String> huchasAdministradas;
}

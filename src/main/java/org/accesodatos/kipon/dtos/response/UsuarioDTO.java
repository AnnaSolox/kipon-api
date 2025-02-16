package org.accesodatos.kipon.dtos.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String email;
    private LocalDate fechaRegistro;
    private PerfilDTO perfil;
    private List<HuchaRolDTO> huchas;
}

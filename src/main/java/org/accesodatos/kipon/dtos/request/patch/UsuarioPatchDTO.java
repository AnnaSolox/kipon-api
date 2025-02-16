package org.accesodatos.kipon.dtos.request.patch;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioPatchDTO {
    @Size(min = 8, max = 50, message = "La contraseña debe tener entre 8 y 50 caracteres")
    private String password;

    @Email(message = "El formato del email es inválido")
    private String email;

    @Size(max = 50, message = "El nombre de usuario no puede superar los 50 caracteres")
    private String nombre;

    @Valid
    private PerfilPatchDTO perfil;
}

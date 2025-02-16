package org.accesodatos.kipon.dtos.request.create;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UsuarioCreateDTO {
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(max = 50, message = "El nombre de usuario no puede superar los 50 caracteres")
    private String nombre;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, max = 50, message = "La contraseña debe tener entre 8 y 50 caracteres")
    private String password;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El formato del email es inválido")
    private String email;

    private LocalDate fechaRegistro;

    @Valid
    @NotNull(message = "El perfil es obligatorio.")
    private PerfilCreateDTO perfil;
}

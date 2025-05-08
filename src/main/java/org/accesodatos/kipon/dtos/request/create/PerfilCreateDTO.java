package org.accesodatos.kipon.dtos.request.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PerfilCreateDTO {
    @NotBlank(message = "El nombre completo no puede estar vacío")
    @Size(max = 100, message = "El nombre completo no puede superar los 100 caracteres")
    private String nombreCompleto;

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Pattern(regexp = "[0-9]+", message = "El teléfono solo puede contener números (sin espacios ni otros caracteres)")
    private String telefono;

    @NotBlank(message = "La dirección no puede estar vacía")
    @Size(max = 50, message = "La dirección no puede superar los 50 caracteres")
    private String direccion;

    private String fotoPerfil;
}

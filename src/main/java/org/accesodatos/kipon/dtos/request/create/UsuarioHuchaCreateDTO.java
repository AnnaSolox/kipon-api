package org.accesodatos.kipon.dtos.request.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UsuarioHuchaCreateDTO {
    @NotNull(message = "El ID del Usuario es obligatorio")
    private Long idUsuario;

    @NotNull(message = "El ID de la Hucha es obligatorio")
    private Long idHucha;

    @NotBlank(message = "El rol no puede estar vacío")
    @Pattern(regexp = "Administrador|Miembro", message = "El tipo de rol ha de ser 'Administrador' o 'Miembro'")
    private String rol;
}

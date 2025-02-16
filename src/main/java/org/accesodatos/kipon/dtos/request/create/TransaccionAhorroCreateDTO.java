package org.accesodatos.kipon.dtos.request.create;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransaccionAhorroCreateDTO {
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;

    @NotNull(message = "La cantidad de la transacción es obligatoria")
    @Min(value = 0, message = "La cantidad de la transacción no puede ser menor a 0")
    private Double cantidad;
}

package org.accesodatos.kipon.dtos.request.patch;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HuchaPatchDTO {
    @Size(max = 100, message = "El nombre no puede contener más de 100 caracteres")
    private String nombre;

    @Min(value = 0, message = "El objetivo de ahorro no puede ser negativo")
    private Double objetivoAhorro;

    private LocalDate fechaObjetivo;

    private Long idAdministrador;

    private String fotoHucha;
}

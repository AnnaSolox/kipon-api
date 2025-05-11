package org.accesodatos.kipon.dtos.request.create;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HuchaCreateDTO {
    @NotBlank(message = "El nombre de la hucha no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede contener más de 100 caracteres")
    private String nombre;

    @NotNull(message = "El objetivo de ahorro es obligatorio")
    @Min(value = 0, message = "El objetivo de ahorro no puede ser negativo")
    private Double objetivoAhorro;

    private LocalDate fechaObjetivo;

    private String fotoHucha;
}

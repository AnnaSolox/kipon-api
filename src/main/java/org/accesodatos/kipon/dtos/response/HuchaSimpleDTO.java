package org.accesodatos.kipon.dtos.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HuchaSimpleDTO {
    private Long id;
    private String nombre;
    private LocalDate fechaObjetivo;
    private Double cantidadTotal;
    private Double objetivoAhorro;
}

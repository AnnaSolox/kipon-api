package org.accesodatos.kipon.dtos.response;

import lombok.Data;

@Data
public class HuchaSimpleDTO {
    private Long id;
    private String nombre;
    private Double cantidadTotal;
    private Double objetivoAhorro;
}

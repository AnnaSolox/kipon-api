package org.accesodatos.kipon.dtos.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class HuchaDTO {
    private Long id;
    private String nombre;
    private Double cantidadTotal;
    private Double objetivoAhorro;
    private LocalDateTime fechaCreacion;
    private LocalDate fechaObjetivo;
    private String administrador;
    private List<TransaccionAhorroDTO> transaccionesAhorro;
}

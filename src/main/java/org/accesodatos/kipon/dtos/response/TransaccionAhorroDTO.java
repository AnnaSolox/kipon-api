package org.accesodatos.kipon.dtos.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TransaccionAhorroDTO {
    private Long id;
    private Double cantidad;
    private LocalDate fecha;
}

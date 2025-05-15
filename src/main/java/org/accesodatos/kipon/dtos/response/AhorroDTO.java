package org.accesodatos.kipon.dtos.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AhorroDTO {
    private Long id;
    private String usuario;
    private Double cantidad;
    private LocalDate fecha;
    private String foto;
}

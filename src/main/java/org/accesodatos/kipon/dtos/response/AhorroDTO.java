package org.accesodatos.kipon.dtos.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AhorroDTO {
    private Long id;
    private Long idUsuario;
    private Long idHucha;
    private String nombreUsuario;
    private String nombreHucha;
    private Double cantidad;
    private Double saldoActualHucha;
    private LocalDate fecha;
    private String fotoUsuario;
    private String fotoHucha;
}

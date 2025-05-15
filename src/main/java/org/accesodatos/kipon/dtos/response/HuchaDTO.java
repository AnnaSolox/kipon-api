package org.accesodatos.kipon.dtos.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private String fotoHucha;
    private List<UsuarioRolDTO> usuarios;
    private List<AhorroDTO> ahorros;
}

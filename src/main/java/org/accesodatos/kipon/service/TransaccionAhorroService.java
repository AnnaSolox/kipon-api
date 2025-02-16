package org.accesodatos.kipon.service;

import org.accesodatos.kipon.dtos.response.TransaccionAhorroDTO;

import java.util.List;

public interface TransaccionAhorroService {
    List<TransaccionAhorroDTO> obtenerTodasLasTransaccionesAhorro();
    TransaccionAhorroDTO obtenerTransaccionAhorroPorId(Long id);
}

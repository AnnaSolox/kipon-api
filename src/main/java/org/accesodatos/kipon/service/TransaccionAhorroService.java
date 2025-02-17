package org.accesodatos.kipon.service;

import org.accesodatos.kipon.dtos.request.create.TransaccionAhorroCreateDTO;
import org.accesodatos.kipon.dtos.response.TransaccionAhorroDTO;

import java.util.List;

public interface TransaccionAhorroService {
    List<TransaccionAhorroDTO> obtenerTransaccionesAhorroDeHucha();
    TransaccionAhorroDTO crearTransaccion(Long idHucha, TransaccionAhorroCreateDTO dto);
}

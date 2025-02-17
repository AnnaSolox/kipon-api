package org.accesodatos.kipon.service;

import org.accesodatos.kipon.dtos.request.create.AhorroCreateDTO;
import org.accesodatos.kipon.dtos.response.AhorroDTO;

import java.util.List;

public interface AhorroService {
    AhorroDTO crearAhorro(Long idHucha, AhorroCreateDTO dto);
}

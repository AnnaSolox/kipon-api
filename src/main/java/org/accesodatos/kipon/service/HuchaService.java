package org.accesodatos.kipon.service;

import org.accesodatos.kipon.dtos.response.HuchaDTO;
import org.accesodatos.kipon.model.Usuario;

import java.util.List;

public interface HuchaService {
    List<HuchaDTO> obtenerTodasLasHuchas();
    HuchaDTO obtenerHuchaPorId(Long id);
    List<HuchaDTO> obtenerHuchasPorIdAdministrador(Long id);
}

package org.accesodatos.kipon.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.accesodatos.kipon.dtos.request.create.HuchaCreateDTO;
import org.accesodatos.kipon.dtos.response.HuchaDTO;
import org.accesodatos.kipon.model.Usuario;

import java.util.List;

public interface HuchaService {
    List<HuchaDTO> obtenerTodasLasHuchas();
    HuchaDTO obtenerHuchaPorId(Long id);
    List<HuchaDTO> obtenerHuchasPorIdAdministrador(Long id);
    HuchaDTO crearHucha(HuchaCreateDTO dto);
    HuchaDTO actualizarHuchaParcial(Long id, JsonNode patch);
    void eliminarHucha(Long id);
}

package org.accesodatos.kipon.service;

import org.accesodatos.kipon.dtos.response.PerfilDTO;

import java.util.List;

public interface PerfilService {
    List<PerfilDTO> obtenerTodosLosPerfiles();
    PerfilDTO obtenerPerfilPorId(Long id);
}

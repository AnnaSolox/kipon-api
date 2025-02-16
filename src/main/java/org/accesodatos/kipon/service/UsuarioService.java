package org.accesodatos.kipon.service;

import org.accesodatos.kipon.dtos.response.UsuarioDTO;

import java.util.List;

public interface UsuarioService {
    List<UsuarioDTO> obtenerTodosLosUsuarios();
    UsuarioDTO obtenerUsuarioPorId(Long id);
}

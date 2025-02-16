package org.accesodatos.kipon.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.accesodatos.kipon.dtos.request.create.UsuarioCreateDTO;
import org.accesodatos.kipon.dtos.request.update.UsuarioUpdateDTO;
import org.accesodatos.kipon.dtos.response.UsuarioDTO;

import java.util.List;

public interface UsuarioService {
    List<UsuarioDTO> obtenerTodosLosUsuarios();
    UsuarioDTO obtenerUsuarioPorId(Long id);
    UsuarioDTO crearUsuario(UsuarioCreateDTO dto);
    UsuarioDTO actualizarUsuario(Long id, UsuarioUpdateDTO dto);
    UsuarioDTO actualizarUsuarioParcial(Long id, JsonNode patch);
    void eliminarUsuario(Long id);
}

package org.accesodatos.kipon.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.accesodatos.kipon.dtos.request.create.UsuarioCreateDTO;
import org.accesodatos.kipon.dtos.request.update.UsuarioUpdateDTO;
import org.accesodatos.kipon.dtos.response.UsuarioDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UsuarioService extends UserDetailsService {
    List<UsuarioDTO> obtenerTodosLosUsuarios();
    List<UsuarioDTO> obtenerUsuariosCoincidentes(String nombreParcial);

    UsuarioDTO obtenerUsuarioPorId(Long id);
    UsuarioDTO obtenerUsuarioPorNombreUsuario(String nombreUsuario);
    UsuarioDTO crearUsuario(UsuarioCreateDTO dto);
    UsuarioDTO actualizarUsuario(Long id, UsuarioUpdateDTO dto);
    UsuarioDTO actualizarUsuarioParcial(Long id, JsonNode patch);
    void eliminarUsuario(Long id);
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}

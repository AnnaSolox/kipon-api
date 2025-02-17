package org.accesodatos.kipon.service;

import jakarta.transaction.Transactional;
import org.accesodatos.kipon.dtos.request.create.UsuarioHuchaCreateDTO;
import org.accesodatos.kipon.dtos.response.UsuarioDTO;

public interface UsuarioHuchaService {
    UsuarioDTO añadirUsuarioHucha(UsuarioHuchaCreateDTO dto);

    @Transactional
    void eliminarUsarioHucha(Long idUsuario, Long idHucha);
}

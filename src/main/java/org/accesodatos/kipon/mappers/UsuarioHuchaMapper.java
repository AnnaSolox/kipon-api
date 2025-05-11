package org.accesodatos.kipon.mappers;

import org.accesodatos.kipon.dtos.request.create.UsuarioHuchaCreateDTO;
import org.accesodatos.kipon.dtos.response.HuchaRolDTO;
import org.accesodatos.kipon.dtos.response.UsuarioRolDTO;
import org.accesodatos.kipon.model.UsuarioHucha;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {HuchaMapper.class, UsuarioMapper.class})
public interface UsuarioHuchaMapper {
    HuchaRolDTO toDTO (UsuarioHucha usuarioHucha);

    UsuarioRolDTO toDTOUsuario (UsuarioHucha usuarioHcuha);

    UsuarioHucha toEntity (UsuarioHuchaCreateDTO dto);
}

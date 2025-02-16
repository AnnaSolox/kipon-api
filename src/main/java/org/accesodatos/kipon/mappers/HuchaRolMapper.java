package org.accesodatos.kipon.mappers;

import org.accesodatos.kipon.dtos.response.HuchaRolDTO;
import org.accesodatos.kipon.model.UsuarioHucha;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = HuchaMapper.class)
public interface HuchaRolMapper {
    HuchaRolDTO toDTO (UsuarioHucha usuarioHucha);
}

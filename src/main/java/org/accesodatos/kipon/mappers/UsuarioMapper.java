package org.accesodatos.kipon.mappers;

import org.accesodatos.kipon.dtos.response.UsuarioDTO;
import org.accesodatos.kipon.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Map;

@Mapper(componentModel = "spring", uses = {HuchaRolMapper.class, PerfilMapper.class})
public interface UsuarioMapper {
    UsuarioDTO toDTO(Usuario usuario);

}

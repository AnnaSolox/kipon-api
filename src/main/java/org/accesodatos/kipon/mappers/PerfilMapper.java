package org.accesodatos.kipon.mappers;

import org.accesodatos.kipon.dtos.response.PerfilDTO;
import org.accesodatos.kipon.model.Perfil;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PerfilMapper {
    PerfilDTO toTDO (Perfil perfil);
}

package org.accesodatos.kipon.mappers;

import org.accesodatos.kipon.dtos.request.create.AhorroCreateDTO;
import org.accesodatos.kipon.dtos.response.AhorroDTO;
import org.accesodatos.kipon.model.Ahorro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AhorroMapper {
    @Mapping(source = "usuario.nombre", target = "usuario")
    AhorroDTO toDTO (Ahorro ahorro);

    Ahorro toEntity (AhorroCreateDTO dto);
}

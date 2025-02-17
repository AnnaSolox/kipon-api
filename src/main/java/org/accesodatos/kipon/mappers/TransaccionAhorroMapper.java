package org.accesodatos.kipon.mappers;

import org.accesodatos.kipon.dtos.request.create.TransaccionAhorroCreateDTO;
import org.accesodatos.kipon.dtos.response.TransaccionAhorroDTO;
import org.accesodatos.kipon.model.TransaccionAhorro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransaccionAhorroMapper {
    @Mapping(source = "usuario.nombre", target = "usuario")
    TransaccionAhorroDTO toDTO (TransaccionAhorro transaccionAhorro);

    TransaccionAhorro toEntity (TransaccionAhorroCreateDTO dto);
}

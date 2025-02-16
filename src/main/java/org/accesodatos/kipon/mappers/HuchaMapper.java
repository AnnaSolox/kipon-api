package org.accesodatos.kipon.mappers;

import org.accesodatos.kipon.dtos.response.HuchaDTO;
import org.accesodatos.kipon.model.Hucha;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = TransaccionAhorroMapper.class)
public interface HuchaMapper {
    @Mapping(source = "administrador.nombre", target = "administrador")
    HuchaDTO toDTO (Hucha hucha);
}

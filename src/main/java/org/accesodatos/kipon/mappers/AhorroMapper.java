package org.accesodatos.kipon.mappers;

import org.accesodatos.kipon.dtos.request.create.AhorroCreateDTO;
import org.accesodatos.kipon.dtos.response.AhorroDTO;
import org.accesodatos.kipon.model.Ahorro;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AhorroMapper {
    @Mapping(source = "usuario.nombre", target = "usuario")
    @Mapping(source = "usuario.perfil.fotoPerfil", target = "foto")
    AhorroDTO toDTO (Ahorro ahorro);

    Ahorro toEntity (AhorroCreateDTO dto);
}

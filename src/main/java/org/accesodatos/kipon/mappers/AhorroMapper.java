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
    @Mapping(source = "usuario.nombre", target = "nombreUsuario")
    @Mapping(source = "hucha.nombre", target = "nombreHucha")
    @Mapping(source = "usuario.perfil.fotoPerfil", target = "fotoUsuario")
    @Mapping(source = "hucha.fotoHucha", target = "fotoHucha")
    @Mapping(source = "usuario.id", target = "idUsuario")
    @Mapping(source = "hucha.id", target = "idHucha")
    @Mapping(source = "hucha.cantidadTotal", target = "saldoActualHucha")
    AhorroDTO toDTO (Ahorro ahorro);

    Ahorro toEntity (AhorroCreateDTO dto);
}

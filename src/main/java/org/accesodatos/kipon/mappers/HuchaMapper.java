package org.accesodatos.kipon.mappers;

import org.accesodatos.kipon.dtos.request.create.HuchaCreateDTO;
import org.accesodatos.kipon.dtos.request.patch.HuchaPatchDTO;
import org.accesodatos.kipon.dtos.response.HuchaDTO;
import org.accesodatos.kipon.model.Hucha;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {AhorroMapper.class, UsuarioHuchaMapper.class})
public interface HuchaMapper {
    @Mapping(source = "administrador.nombre", target = "administrador")
    HuchaDTO toDTO(Hucha hucha);

    Hucha toEntity(HuchaCreateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromPatchDTO(HuchaPatchDTO dto, @MappingTarget Hucha hucha);
}
package org.accesodatos.kipon.mappers;

import org.accesodatos.kipon.dtos.request.create.PerfilCreateDTO;
import org.accesodatos.kipon.dtos.request.patch.PerfilPatchDTO;
import org.accesodatos.kipon.dtos.request.update.PerfilUpdateDTO;
import org.accesodatos.kipon.dtos.response.PerfilDTO;
import org.accesodatos.kipon.model.Perfil;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PerfilMapper {
    PerfilDTO toTDO (Perfil perfil);

    Perfil toEntity (PerfilCreateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto (PerfilUpdateDTO dto, @MappingTarget Perfil perfil);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromPatchDto (PerfilPatchDTO dto, @MappingTarget Perfil perfil);
}

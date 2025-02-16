package org.accesodatos.kipon.mappers;

import org.accesodatos.kipon.dtos.request.create.UsuarioCreateDTO;
import org.accesodatos.kipon.dtos.request.patch.UsuarioPatchDTO;
import org.accesodatos.kipon.dtos.request.update.UsuarioUpdateDTO;
import org.accesodatos.kipon.dtos.response.UsuarioDTO;
import org.accesodatos.kipon.model.Usuario;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {UsuarioHuchaMapper.class, PerfilMapper.class})
public interface UsuarioMapper {
    UsuarioDTO toDTO(Usuario usuario);

    Usuario toEntity (UsuarioCreateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(UsuarioUpdateDTO dto, @MappingTarget Usuario usuario);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromPatchDTO(UsuarioPatchDTO dto, @MappingTarget Usuario usuario);

}

package org.accesodatos.kipon.mappers;

import org.accesodatos.kipon.dtos.request.create.UsuarioCreateDTO;
import org.accesodatos.kipon.dtos.request.patch.UsuarioPatchDTO;
import org.accesodatos.kipon.dtos.request.update.UsuarioUpdateDTO;
import org.accesodatos.kipon.dtos.response.UsuarioDTO;
import org.accesodatos.kipon.dtos.response.UsuarioSimpleDTO;
import org.accesodatos.kipon.model.Usuario;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {PerfilMapper.class, AhorroMapper.class})
public interface UsuarioMapper {

    UsuarioDTO toDTO(Usuario usuario);

    Usuario toEntity (UsuarioCreateDTO dto);

    @Mapping(source = "perfil.fotoPerfil", target = "foto")
    UsuarioSimpleDTO toSimpleDTO(Usuario usuario);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(UsuarioUpdateDTO dto, @MappingTarget Usuario usuario);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromPatchDTO(UsuarioPatchDTO dto, @MappingTarget Usuario usuario);

}

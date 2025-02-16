package org.accesodatos.kipon.mappers;

import org.accesodatos.kipon.dtos.response.UsuarioDTO;
import org.accesodatos.kipon.model.Hucha;
import org.accesodatos.kipon.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(source = "huchasAdministradas", target = "huchasAdemnistadas", qualifiedByName =
            "mapListHuchasAdministradas")
    UsuarioDTO toDTO(Usuario usuario);

    @Named("mapListHuchasAdministradas")
    default List<String> mapListHuchasAdministradas (List<Hucha> huchasAdministradas){
        return huchasAdministradas.stream().map(Hucha::getNombre).toList();
    }
}

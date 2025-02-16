package org.accesodatos.kipon.service.impl;

import lombok.RequiredArgsConstructor;
import org.accesodatos.kipon.dtos.response.UsuarioDTO;
import org.accesodatos.kipon.mappers.UsuarioMapper;
import org.accesodatos.kipon.repository.UsuarioRepository;
import org.accesodatos.kipon.service.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Override
    public List<UsuarioDTO> obtenerTodosLosUsuarios() {

        return usuarioRepository.findAllByOrderByIdAsc()
                .stream()
                .map(usuarioMapper::toDTO)
                .toList();
    }

    @Override
    public UsuarioDTO obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toDTO)
                .orElse(null);
    }
}

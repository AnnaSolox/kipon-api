package org.accesodatos.kipon.service.impl;

import lombok.RequiredArgsConstructor;
import org.accesodatos.kipon.dtos.response.HuchaDTO;
import org.accesodatos.kipon.dtos.response.UsuarioDTO;
import org.accesodatos.kipon.mappers.HuchaMapper;
import org.accesodatos.kipon.model.Usuario;
import org.accesodatos.kipon.repository.HuchaRepository;
import org.accesodatos.kipon.repository.UsuarioRepository;
import org.accesodatos.kipon.service.HuchaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HuchaServiceImpl implements HuchaService {
    private final HuchaRepository huchaRepository;
    private final HuchaMapper huchaMapper;
    private final UsuarioRepository usuarioRepository;

    @Override
    public List<HuchaDTO> obtenerTodasLasHuchas() {
        return huchaRepository.findAllByOrderByIdAsc()
                .stream()
                .map(huchaMapper::toDTO)
                .toList();
    }

    @Override
    public HuchaDTO obtenerHuchaPorId(Long id) {
        return huchaRepository.findById(id)
                .map(huchaMapper::toDTO)
                .orElse(null);
    }

    @Override
    public List<HuchaDTO> obtenerHuchasPorIdAdministrador(Long id) {
        return huchaRepository.findByAdministradorId(id)
                .stream()
                .map(huchaMapper::toDTO)
                .toList();
    }
}

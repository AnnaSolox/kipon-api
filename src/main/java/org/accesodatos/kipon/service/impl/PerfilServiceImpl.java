package org.accesodatos.kipon.service.impl;

import lombok.RequiredArgsConstructor;
import org.accesodatos.kipon.dtos.response.PerfilDTO;
import org.accesodatos.kipon.mappers.PerfilMapper;
import org.accesodatos.kipon.repository.PerfilRepository;
import org.accesodatos.kipon.service.PerfilService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PerfilServiceImpl implements PerfilService {
    private final PerfilRepository perfilRepository;
    private final PerfilMapper perfilMapper;

    @Override
    public List<PerfilDTO> obtenerTodosLosPerfiles() {
        return perfilRepository.findAllByOrderByIdAsc()
                .stream()
                .map(perfilMapper::toTDO)
                .toList();
    }

    @Override
    public PerfilDTO obtenerPerfilPorId(Long id) {
        return perfilRepository.findById(id)
                .map(perfilMapper::toTDO)
                .orElse(null);
    }
}

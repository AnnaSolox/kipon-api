package org.accesodatos.kipon.service.impl;

import lombok.RequiredArgsConstructor;
import org.accesodatos.kipon.dtos.response.TransaccionAhorroDTO;
import org.accesodatos.kipon.mappers.TransaccionAhorroMapper;
import org.accesodatos.kipon.repository.HuchaRepository;
import org.accesodatos.kipon.repository.TransaccionAhorroRepository;
import org.accesodatos.kipon.repository.UsuarioRepository;
import org.accesodatos.kipon.service.TransaccionAhorroService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransaccionAhorroServiceImpl implements TransaccionAhorroService {
    private final TransaccionAhorroRepository transaccionAhorroRepository;
    private final TransaccionAhorroMapper transaccionAhorroMapper;
    private final UsuarioRepository usuarioRepository;
    private final HuchaRepository huchaRepository;


    @Override
    public List<TransaccionAhorroDTO> obtenerTodasLasTransaccionesAhorro() {
        return transaccionAhorroRepository.findAllByOrderByIdAsc()
                .stream()
                .map(transaccionAhorroMapper::toDTO)
                .toList();
    }

    @Override
    public TransaccionAhorroDTO obtenerTransaccionAhorroPorId(Long id) {
        return transaccionAhorroRepository.findById(id)
                .map(transaccionAhorroMapper::toDTO)
                .orElse(null);
    }
}

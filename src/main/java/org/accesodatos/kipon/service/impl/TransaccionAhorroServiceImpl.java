package org.accesodatos.kipon.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.accesodatos.kipon.dtos.request.create.TransaccionAhorroCreateDTO;
import org.accesodatos.kipon.dtos.response.TransaccionAhorroDTO;
import org.accesodatos.kipon.mappers.TransaccionAhorroMapper;
import org.accesodatos.kipon.model.Hucha;
import org.accesodatos.kipon.model.TransaccionAhorro;
import org.accesodatos.kipon.model.Usuario;
import org.accesodatos.kipon.model.UsuarioHucha;
import org.accesodatos.kipon.repository.HuchaRepository;
import org.accesodatos.kipon.repository.TransaccionAhorroRepository;
import org.accesodatos.kipon.repository.UsuarioHuchaRepository;
import org.accesodatos.kipon.repository.UsuarioRepository;
import org.accesodatos.kipon.service.TransaccionAhorroService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransaccionAhorroServiceImpl implements TransaccionAhorroService {
    private final TransaccionAhorroRepository transaccionAhorroRepository;
    private final TransaccionAhorroMapper transaccionAhorroMapper;
    private final UsuarioRepository usuarioRepository;
    private final HuchaRepository huchaRepository;
    private final UsuarioHuchaRepository usuarioHuchaRepository;

    @Override
    public List<TransaccionAhorroDTO> obtenerTransaccionesAhorroDeHucha() {
        return transaccionAhorroRepository.findAllByOrderByIdAsc()
                .stream()
                .map(transaccionAhorroMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public TransaccionAhorroDTO crearTransaccion(Long idHucha, TransaccionAhorroCreateDTO dto) {
        Hucha hucha = huchaRepository.findById(idHucha)
                .orElseThrow(() -> new NoSuchElementException("Hucha con id " + idHucha + " no encontrada"));

        usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new NoSuchElementException("Usuario con id " + dto.getIdUsuario() + " no " +
                        "encontrado"));

        Optional<UsuarioHucha> usuarioHucha = usuarioHuchaRepository.findByUsuarioIdAndHuchaId(dto.getIdUsuario(), idHucha);
        if (usuarioHucha.isEmpty()) {
            throw new IllegalArgumentException("El usuario con id " + dto.getIdUsuario() + " no está asociado a la hucha con id " + idHucha);
        }

        TransaccionAhorro transaccion = transaccionAhorroMapper.toEntity(dto);
        transaccion.setHucha(hucha);
        transaccion.setFecha(LocalDate.now());

        hucha.setCantidadTotal(hucha.getCantidadTotal() + transaccion.getCantidad());

        transaccionAhorroRepository.save(transaccion);
        huchaRepository.save(hucha);

        return transaccionAhorroMapper.toDTO(transaccion);
    }
}

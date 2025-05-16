package org.accesodatos.kipon.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.accesodatos.kipon.dtos.request.create.AhorroCreateDTO;
import org.accesodatos.kipon.dtos.response.AhorroDTO;
import org.accesodatos.kipon.mappers.AhorroMapper;
import org.accesodatos.kipon.model.Hucha;
import org.accesodatos.kipon.model.Ahorro;
import org.accesodatos.kipon.model.Usuario;
import org.accesodatos.kipon.model.UsuarioHucha;
import org.accesodatos.kipon.repository.HuchaRepository;
import org.accesodatos.kipon.repository.AhorroRepository;
import org.accesodatos.kipon.repository.UsuarioHuchaRepository;
import org.accesodatos.kipon.repository.UsuarioRepository;
import org.accesodatos.kipon.service.AhorroService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AhorroServiceImpl implements AhorroService {
    private final AhorroRepository ahorroRepository;
    private final AhorroMapper ahorroMapper;
    private final UsuarioRepository usuarioRepository;
    private final HuchaRepository huchaRepository;
    private final UsuarioHuchaRepository usuarioHuchaRepository;

    @Override
    @Transactional
    public AhorroDTO crearAhorro(Long idHucha, AhorroCreateDTO dto) {
        Hucha hucha = huchaRepository.findById(idHucha)
                .orElseThrow(() -> new NoSuchElementException("Hucha con id " + idHucha + " no encontrada"));

        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new NoSuchElementException("Usuario con id " + dto.getIdUsuario() + " no encontrado"));


        Optional<UsuarioHucha> usuarioHucha = usuarioHuchaRepository.findByUsuarioIdAndHuchaId(dto.getIdUsuario(), idHucha);
        if (usuarioHucha.isEmpty()) {
            throw new IllegalArgumentException("El usuario con id " + dto.getIdUsuario() + " no está asociado a la hucha con id " + idHucha);
        }

        Ahorro ahorro = ahorroMapper.toEntity(dto);
        ahorro.setHucha(hucha);
        ahorro.setUsuario(usuario);
        ahorro.setFecha(LocalDate.now());

        Double saldoPosterior = hucha.getCantidadTotal() + (ahorro.getCantidad());
        ahorro.setCantidadPosterior(saldoPosterior);
        hucha.setCantidadTotal(saldoPosterior);

        ahorroRepository.save(ahorro);
        huchaRepository.save(hucha);

        return ahorroMapper.toDTO(ahorro);
    }

}

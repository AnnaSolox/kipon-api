package org.accesodatos.kipon.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.accesodatos.kipon.dtos.request.create.UsuarioHuchaCreateDTO;
import org.accesodatos.kipon.dtos.response.UsuarioDTO;
import org.accesodatos.kipon.mappers.UsuarioMapper;
import org.accesodatos.kipon.model.Hucha;
import org.accesodatos.kipon.model.TransaccionAhorro;
import org.accesodatos.kipon.model.Usuario;
import org.accesodatos.kipon.model.UsuarioHucha;
import org.accesodatos.kipon.model.key.UsuarioHuchaKey;
import org.accesodatos.kipon.repository.HuchaRepository;
import org.accesodatos.kipon.repository.TransaccionAhorroRepository;
import org.accesodatos.kipon.repository.UsuarioHuchaRepository;
import org.accesodatos.kipon.repository.UsuarioRepository;
import org.accesodatos.kipon.service.UsuarioHuchaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioHuchaServiceImpl implements UsuarioHuchaService {
    private final UsuarioRepository usuarioRepository;
    private final HuchaRepository huchaRepository;
    private final UsuarioHuchaRepository usuarioHuchaRepository;
    private final TransaccionAhorroRepository transaccionAhorroRepository;
    private final UsuarioMapper usuarioMapper;

    @Override
    @Transactional
    public UsuarioDTO añadirUsuarioHucha(UsuarioHuchaCreateDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con id: " + dto.getIdUsuario()));

        Hucha hucha = huchaRepository.findById(dto.getIdHucha())
                .orElseThrow(() -> new NoSuchElementException("Hucha no encontrada con id: " + dto.getIdHucha()));

        // Verificar si el usuario ya está asociado a la hucha
        Optional<UsuarioHucha> usuarioHuchaExistente = usuarioHuchaRepository.findByUsuarioIdAndHuchaId(
                dto.getIdUsuario(),
                dto.getIdHucha()
        );

        if (usuarioHuchaExistente.isPresent()) {
            throw new IllegalStateException("El usuario ya está asociado a esta hucha");
        }

        if ("Administrador".equalsIgnoreCase(dto.getRol())) {
            throw new IllegalStateException("No se puede asociar un nuevo usuario con el rol Administrador");
        }

        // Si no está, crear una nueva asociación
        UsuarioHucha usuarioHucha = new UsuarioHucha();
        UsuarioHuchaKey key = new UsuarioHuchaKey(dto.getIdUsuario(), dto.getIdHucha());
        usuarioHucha.setId(key);
        usuarioHucha.setUsuario(usuario);
        usuarioHucha.setHucha(hucha);
        usuarioHucha.setRol(dto.getRol());

        // Agregar la relación en ambas entidades
        hucha.getUsuarios().add(usuarioHucha);
        usuario.getHuchas().add(usuarioHucha);


        // Guardar los cambios en ambas entidades
        usuarioRepository.save(usuario);
        huchaRepository.save(hucha);

        // Devolver un DTO con la información actualizada
        return usuarioMapper.toDTO(usuario);
    }

    @Transactional
    @Override
    public void eliminarUsarioHucha(Long idUsuario, Long idHucha) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con id: " + idUsuario));

        Hucha hucha = huchaRepository.findById(idHucha)
                .orElseThrow(() -> new NoSuchElementException("Hucha no encontrada con id: " + idHucha));

        // Verificar si el usuario ya está asociado a la hucha
        Optional<UsuarioHucha> usuarioHuchaExistente = usuarioHuchaRepository.findByUsuarioIdAndHuchaId(idUsuario, idHucha);

        if (usuarioHuchaExistente.isEmpty()) {
            throw new NoSuchElementException("No se puede eliminar porque el usuario no pertenece a esta hucha");
        }

        // Comprobar si es administrador
        if (usuarioHuchaExistente.get().getRol().equals("Administrador")) {
            throw new IllegalStateException("No se puede eliminar al administrador de la hucha");
        }

        // Obtener todas las transacciones del usuario en esta hucha
        List<TransaccionAhorro> transacciones = transaccionAhorroRepository.findTransaccionesByUserAndHucha(idUsuario,
                idHucha);

        Double totalTransacciones = transacciones.stream()
                .map(TransaccionAhorro::getCantidad)
                .reduce(0.0, Double::sum);

        hucha.setCantidadTotal(hucha.getCantidadTotal() - totalTransacciones);
        transaccionAhorroRepository.deleteAll(transacciones);

        // Eliminar la asociación UsuarioHucha
        UsuarioHucha usuarioHucha = usuarioHuchaExistente.get();
        hucha.getUsuarios().remove(usuarioHucha);
        usuarioHucha.getUsuario().getHuchas().remove(usuarioHucha);


        // Guardar la hucha actualizada
        huchaRepository.save(hucha);
    }
}

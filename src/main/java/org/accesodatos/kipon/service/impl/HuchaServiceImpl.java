package org.accesodatos.kipon.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.accesodatos.kipon.dtos.request.create.HuchaCreateDTO;
import org.accesodatos.kipon.dtos.request.create.UsuarioHuchaCreateDTO;
import org.accesodatos.kipon.dtos.request.patch.HuchaPatchDTO;
import org.accesodatos.kipon.dtos.response.HuchaDTO;
import org.accesodatos.kipon.dtos.response.UsuarioDTO;
import org.accesodatos.kipon.mappers.HuchaMapper;
import org.accesodatos.kipon.model.Hucha;
import org.accesodatos.kipon.model.Usuario;
import org.accesodatos.kipon.model.UsuarioHucha;
import org.accesodatos.kipon.model.key.UsuarioHuchaKey;
import org.accesodatos.kipon.repository.HuchaRepository;
import org.accesodatos.kipon.repository.UsuarioRepository;
import org.accesodatos.kipon.service.HuchaService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class HuchaServiceImpl implements HuchaService {
    private final HuchaRepository huchaRepository;
    private final HuchaMapper huchaMapper;
    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper objectMapper;
    private final Validator validator;

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

    @Override
    @Transactional
    public HuchaDTO crearHucha(HuchaCreateDTO dto) {
        Usuario administrador = usuarioRepository.findById(dto.getIdAdministrador())
                .orElseThrow(() -> new NoSuchElementException("Usuario con id " + dto.getIdAdministrador() + " no encontrado"));

        Hucha hucha = huchaMapper.toEntity(dto);
        hucha.setAdministrador(administrador);
        hucha.setFechaCreacion(LocalDateTime.now());

        Hucha huchaGuardada = huchaRepository.save(hucha);

        return huchaMapper.toDTO(huchaGuardada);
    }

    @Override
    @Transactional
    public UsuarioHuchaCreateDTO añadirUsuarioHucha(UsuarioHuchaCreateDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con id: " + dto.getIdUsuario()));

        Hucha hucha = huchaRepository.findById(dto.getIdHucha())
                .orElseThrow(() -> new NoSuchElementException("Hucha no encontrada con id: " + dto.getIdHucha()));

        // Verificar si el usuario ya está asociado a la hucha
        Optional<UsuarioHucha> usuarioHuchaExistente = hucha.getUsuarios().stream()
                .filter(uh -> uh.getUsuario().equals(usuario))
                .findFirst();

        if (usuarioHuchaExistente.isPresent()) {
            // Si el usuario ya pertenece a la hucha, solo actualizar el rol
            usuarioHuchaExistente.get().setRol(dto.getRol());
        } else {
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
        }

        // Guardar los cambios en ambas entidades
        usuarioRepository.save(usuario);
        huchaRepository.save(hucha);

        // Devolver un DTO con la información actualizada
        return dto;
    }


    @Override
    @Transactional
    public HuchaDTO actualizarHuchaParcial(Long id, JsonNode patch) {
        Hucha huchaExistente = huchaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Hucha no encontrada con id: " + id));

        HuchaPatchDTO huchaPatchDTO = objectMapper.convertValue(patch, HuchaPatchDTO.class);

        try{
            objectMapper.readerForUpdating(huchaPatchDTO).readValue(patch);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al procesar el JSON: " + e.getMessage());
        }

        // Validar el DTO PATCH
        Set<ConstraintViolation<HuchaPatchDTO>> violations = validator.validate(huchaPatchDTO);
        if (!violations.isEmpty()) {
            Map<String, String> errores = new HashMap<>();
            for (ConstraintViolation<HuchaPatchDTO> violation : violations) {
                errores.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new ConstraintViolationException(errores.toString(), violations);
        }

        // Actualizar la entidad existente con los cambios del DTO PATCH
        huchaMapper.updateEntityFromPatchDTO(huchaPatchDTO, huchaExistente);

        // Verificar si se está actualizando el administrador
        if (huchaPatchDTO.getIdAdministrador() != null) {
            Usuario nuevoAdministrador = usuarioRepository.findById(huchaPatchDTO.getIdAdministrador())
                    .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con id: " + huchaPatchDTO.getIdAdministrador()));

            Usuario anteriorAdministrador = huchaExistente.getAdministrador();

            if (anteriorAdministrador != null && !anteriorAdministrador.equals(nuevoAdministrador)) {
                // Cambiar el rol del anterior administrador a "Miembro"
                for (UsuarioHucha usuarioHucha : huchaExistente.getUsuarios()) {
                    if (usuarioHucha.getUsuario().equals(anteriorAdministrador)) {
                        usuarioHucha.setRol("Miembro");
                        break;
                    }
                }
            }

            // Asignar el nuevo administrador
            huchaExistente.setAdministrador(nuevoAdministrador);

            // Verificar si el nuevo administrador ya está en la lista de UsuarioHucha
            boolean existeAsociacion = huchaExistente.getUsuarios().stream()
                    .anyMatch(usuarioHucha -> usuarioHucha.getUsuario().equals(nuevoAdministrador));

            if (!existeAsociacion) {
                UsuarioHucha usuarioHucha = new UsuarioHucha();
                UsuarioHuchaKey key = new UsuarioHuchaKey(nuevoAdministrador.getId(), huchaExistente.getId());
                usuarioHucha.setId(key);
                usuarioHucha.setUsuario(nuevoAdministrador);
                usuarioHucha.setHucha(huchaExistente);
                usuarioHucha.setRol("Administrador");

                huchaExistente.getUsuarios().add(usuarioHucha);
                nuevoAdministrador.getHuchas().add(usuarioHucha);
            } else {
                // Si ya existe, solo cambiar el rol
                for (UsuarioHucha usuarioHucha : huchaExistente.getUsuarios()) {
                    if (usuarioHucha.getUsuario().equals(nuevoAdministrador)) {
                        usuarioHucha.setRol("Administrador");
                        break;
                    }
                }
            }
        }

        // Guardar la entidad actualizada (con la asociación sincronizada)
        Hucha huchaActualizada = huchaRepository.save(huchaExistente);

        return huchaMapper.toDTO(huchaActualizada);
    }

    @Override
    @Transactional
    public void eliminarHucha(Long id) {
        Hucha hucha = huchaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Hucha no encontrada con id: " + id));

        huchaRepository.delete(hucha);
    }
}

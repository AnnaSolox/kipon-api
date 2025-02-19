package org.accesodatos.kipon.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.accesodatos.kipon.dtos.request.create.HuchaCreateDTO;
import org.accesodatos.kipon.dtos.request.patch.HuchaPatchDTO;
import org.accesodatos.kipon.dtos.response.HuchaDTO;
import org.accesodatos.kipon.mappers.HuchaMapper;
import org.accesodatos.kipon.mappers.UsuarioMapper;
import org.accesodatos.kipon.model.Hucha;
import org.accesodatos.kipon.model.Usuario;
import org.accesodatos.kipon.model.UsuarioHucha;
import org.accesodatos.kipon.model.key.UsuarioHuchaKey;
import org.accesodatos.kipon.repository.HuchaRepository;
import org.accesodatos.kipon.repository.UsuarioRepository;
import org.accesodatos.kipon.service.HuchaService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private final UsuarioMapper usuarioMapper;

    @Override
    public List<HuchaDTO> obtenerTodasLasHuchas() {
        return huchaRepository.findAllByOrderByIdAsc()
                .stream()
                .map(huchaMapper::toDTO)
                .toList();
    }

    @Override
    public HuchaDTO obtenerHuchaPorId(Long id) {
        Hucha hucha = huchaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Hucha con id " + id + " no encontrada"));
        return huchaMapper.toDTO(hucha);
    }

    @Override
    public List<HuchaDTO> obtenerHuchasPorIdAdministrador(Long id) {
        if (!usuarioRepository.existsById(id)){
            throw new NoSuchElementException("El usuario con id "+ id + " no existe");
        }

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
        hucha.setCantidadTotal(0.0);
        hucha.setAdministrador(administrador);
        hucha.setFechaCreacion(LocalDateTime.now());
        hucha.setUsuarios(new ArrayList<>());

        huchaRepository.save(hucha);

        //Creamos la asociación mediante la entidad UsuarioHucha
        UsuarioHucha usuarioHucha = new UsuarioHucha();
        UsuarioHuchaKey key = new UsuarioHuchaKey(administrador.getId(), hucha.getId());
        usuarioHucha.setId(key);
        usuarioHucha.setUsuario(administrador);
        usuarioHucha.setHucha(hucha);
        usuarioHucha.setRol("Administrador");
        usuarioHucha.setFechaIngreso(LocalDate.now());

        // Actualizamos ambas entidades
        hucha.getUsuarios().add(usuarioHucha);
        administrador.getHuchas().add(usuarioHucha);

        usuarioRepository.save(administrador);

        Hucha huchaGuardada = huchaRepository.save(hucha);

        return huchaMapper.toDTO(huchaGuardada);
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


            //Comprobamos si el usuario existe en la hucha
            boolean existeEnHucha = huchaExistente.getUsuarios().stream()
                    .anyMatch(usuarioHucha -> usuarioHucha.getUsuario().equals(nuevoAdministrador));

            if (!existeEnHucha) {
                throw new IllegalStateException("El usuario no puede ser administrador porque no es miembro de la hucha");
            }

            // Guardamos el anterior administrador
            Usuario anteriorAdministrador = huchaExistente.getAdministrador();

            // Si hay un administrador anterior y es diferente al nuevo, cambiar el rol del anterior a "miembro"
            if (anteriorAdministrador != null && !anteriorAdministrador.equals(nuevoAdministrador)) {
                for (UsuarioHucha usuarioHucha : huchaExistente.getUsuarios()) {
                    if (usuarioHucha.getUsuario().equals(anteriorAdministrador)) {
                        usuarioHucha.setRol("Miembro");
                        break;
                    }
                }
            }

            // Asignar el nuevo administrador
            huchaExistente.setAdministrador(nuevoAdministrador);

            // Actualizar el rol del nuevo administrador
            for (UsuarioHucha usuarioHucha : huchaExistente.getUsuarios()) {
                if (usuarioHucha.getUsuario().equals(nuevoAdministrador)) {
                    usuarioHucha.setRol("Administrador");
                    break;
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

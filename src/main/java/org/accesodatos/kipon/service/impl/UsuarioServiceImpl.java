package org.accesodatos.kipon.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.accesodatos.kipon.dtos.request.create.UsuarioCreateDTO;
import org.accesodatos.kipon.dtos.request.patch.UsuarioPatchDTO;
import org.accesodatos.kipon.dtos.request.update.UsuarioUpdateDTO;
import org.accesodatos.kipon.dtos.response.UsuarioDTO;
import org.accesodatos.kipon.mappers.UsuarioMapper;
import org.accesodatos.kipon.model.Usuario;
import org.accesodatos.kipon.repository.UsuarioRepository;
import org.accesodatos.kipon.service.UsuarioService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    @Override
    public List<UsuarioDTO> obtenerTodosLosUsuarios() {

        return usuarioRepository.findAllByOrderByIdAsc()
                .stream()
                .map(usuarioMapper::toDTO)
                .toList();
    }

    @Override
    public UsuarioDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario con id " + id + " no encontrado"));
        return usuarioMapper.toDTO(usuario);
    }

    @Override
    @Transactional
    public UsuarioDTO crearUsuario(UsuarioCreateDTO dto) {
        Usuario usuario = usuarioMapper.toEntity(dto);

        if(dto.getFechaRegistro() == null) {
            usuario.setFechaRegistro(LocalDate.now());
        }

        //Sincronizar relación bidireccional
        usuario.getPerfil().setUsuario(usuario);

        //Persistir el usuario
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(usuarioGuardado);
    }

    @Override
    public UsuarioDTO actualizarUsuario(Long id, UsuarioUpdateDTO dto) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con id: " + id));

        usuarioMapper.updateEntityFromDTO(dto, usuarioExistente);

        //Sincronizar relación bidireccional
        usuarioExistente.getPerfil().setUsuario(usuarioExistente);

        Usuario usuarioActualizado = usuarioRepository.save(usuarioExistente);

        return usuarioMapper.toDTO(usuarioActualizado);
    }

    @Override
    @Transactional
    public UsuarioDTO actualizarUsuarioParcial(Long id, JsonNode patch) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con id: " + id));

        UsuarioPatchDTO usuarioPatchDTO = objectMapper.convertValue(patch, UsuarioPatchDTO.class);

        try{
            objectMapper.readerForUpdating(usuarioPatchDTO).readValue(patch);
        } catch (Exception e){
            throw  new IllegalArgumentException("Error al procesar el JSON: " + e.getMessage());
        }

        System.out.println("usuarioPatchDTO = " + usuarioPatchDTO);
        // Validar el DTO PATCH
        Set<ConstraintViolation<UsuarioPatchDTO>> violations = validator.validate(usuarioPatchDTO);
        if (!violations.isEmpty()) {
            Map<String, String> errores = new HashMap<>();
            for (ConstraintViolation<UsuarioPatchDTO> violation : violations) {
                errores.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new ConstraintViolationException(errores.toString(), violations);
        }

        // Actualizar la entidad existente con los cambios del DTO PATCH
        usuarioMapper.updateEntityFromPatchDTO(usuarioPatchDTO, usuarioExistente);

        // Sincronizar la relación bidireccional
        usuarioExistente.getPerfil().setUsuario(usuarioExistente);

        // Guardar la entidad actualizada
        Usuario usuarioActualizado = usuarioRepository.save(usuarioExistente);

        return usuarioMapper.toDTO(usuarioActualizado);
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("El usuario con ID " + id + "no existe."));

        //Verificar que no tiene huchas asociadas
        if (!usuario.getHuchas().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar el usuario con cuentas asociadas");
        }

        usuarioRepository.delete(usuario);
    }
}

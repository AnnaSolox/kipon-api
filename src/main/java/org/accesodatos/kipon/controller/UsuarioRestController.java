package org.accesodatos.kipon.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.accesodatos.kipon.dtos.request.patch.UsuarioPatchDTO;
import org.accesodatos.kipon.dtos.request.update.UsuarioUpdateDTO;
import org.accesodatos.kipon.dtos.response.UsuarioDTO;
import org.accesodatos.kipon.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kipon/usuarios")
public class UsuarioRestController {
    private final UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Obtiene todos los usuarios")
    public ResponseEntity<List<UsuarioDTO>> obtenerTodosLosUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodosLosUsuarios();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un usuario por su ID")
    public ResponseEntity<UsuarioDTO> obtenerUsuariosPorId(@PathVariable Long id) {
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorId(id);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifica los datos de un usuario")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateDTO dto) {
        UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuario(id, dto);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Modifica los datos de un usuario")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "JSON con los datos del usuario a modificar. Todos los campos son opcionales.",
            content = @Content(schema = @Schema(implementation = UsuarioPatchDTO.class))
    )
    public ResponseEntity<UsuarioDTO> actualizarUsuarioParcial(@PathVariable Long id, @RequestBody JsonNode patch) {
        UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuarioParcial(id, patch);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un usuario por su ID")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}

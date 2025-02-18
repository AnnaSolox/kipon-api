package org.accesodatos.kipon.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.accesodatos.kipon.dtos.request.create.AhorroCreateDTO;
import org.accesodatos.kipon.dtos.request.create.HuchaCreateDTO;
import org.accesodatos.kipon.dtos.request.create.UsuarioHuchaCreateDTO;
import org.accesodatos.kipon.dtos.request.patch.HuchaPatchDTO;
import org.accesodatos.kipon.dtos.response.AhorroDTO;
import org.accesodatos.kipon.dtos.response.HuchaDTO;
import org.accesodatos.kipon.dtos.response.UsuarioDTO;
import org.accesodatos.kipon.service.AhorroService;
import org.accesodatos.kipon.service.HuchaService;
import org.accesodatos.kipon.service.UsuarioHuchaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kipon/huchas")
public class HuchasRestController {
    private final HuchaService huchaService;
    private final UsuarioHuchaService usuarioHuchaService;
    private final AhorroService ahorroService;

    @GetMapping
    @Operation(summary = "Obtiene todas las huchas")
    public ResponseEntity<List<HuchaDTO>> obtenerTodasLasHuchas(){
        List<HuchaDTO> huchas = huchaService.obtenerTodasLasHuchas();
        if (huchas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(huchas);
    }

    @PostMapping
    @Operation(summary = "Crea una hucha")
    public ResponseEntity<HuchaDTO> crearHucha(@Valid @RequestBody HuchaCreateDTO dto) {
        HuchaDTO huchaCreada = huchaService.crearHucha(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(huchaCreada);
    }

    @PostMapping("/{id}/ahorros")
    @Operation(summary = "Crea una transacción de ahorro")
    public ResponseEntity<AhorroDTO> crearAhorro(@PathVariable Long id,
                                                            @Valid @RequestBody AhorroCreateDTO dto){
        AhorroDTO transaccionCreada = ahorroService.crearAhorro(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaccionCreada);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene una hucha por su ID")
    public ResponseEntity<HuchaDTO> obtenerHuchaPorId(@PathVariable Long id){
        HuchaDTO hucha = huchaService.obtenerHuchaPorId(id);

        if (hucha == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(hucha);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Modifica los datos de una hucha")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "JSON con los datos de la hucha modificar. Todos los campos son opcionales.",
            content = @Content(schema = @Schema(implementation = HuchaPatchDTO.class))
    )
    public ResponseEntity<HuchaDTO> actualizarHuchaParcial(@PathVariable Long id, @RequestBody JsonNode patch){
        HuchaDTO huchaActualizada = huchaService.actualizarHuchaParcial(id, patch);
        return ResponseEntity.ok(huchaActualizada);
    }

    @GetMapping("/administrador/{id}")
    @Operation(summary = "Obtiene las huchas de un administrador por su ID ")
    public ResponseEntity<List<HuchaDTO>> obtenerHuchasPorIdAdministrador(@PathVariable Long id){
        List<HuchaDTO> huchas = huchaService.obtenerHuchasPorIdAdministrador(id);
        if (huchas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(huchas);
    }

    @PostMapping("/usuario-hucha")
    @Operation(summary = "Añade un usuario a una hucha")
    public ResponseEntity<UsuarioDTO> añadirUsuarioHucha(@Valid @RequestBody UsuarioHuchaCreateDTO dto) {
        UsuarioDTO respuesta = usuarioHuchaService.añadirUsuarioHucha(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @DeleteMapping("/usuario-hucha/{idUsuario}-{idHucha}")
    @Operation(summary = "Elimina un usuario de una hucha")
    public ResponseEntity<Void> eliminarUsuarioHucha(@PathVariable Long idUsuario, @PathVariable Long idHucha){
        usuarioHuchaService.eliminarUsarioHucha(idUsuario, idHucha);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina una hucha")
    public ResponseEntity<Void> elminarHucha(@PathVariable Long id) {
        huchaService.eliminarHucha(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

}

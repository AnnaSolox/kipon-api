package org.accesodatos.kipon.controller;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.accesodatos.kipon.dtos.request.create.HuchaCreateDTO;
import org.accesodatos.kipon.dtos.request.create.AhorroCreateDTO;
import org.accesodatos.kipon.dtos.request.create.UsuarioHuchaCreateDTO;
import org.accesodatos.kipon.dtos.response.HuchaDTO;
import org.accesodatos.kipon.dtos.response.AhorroDTO;
import org.accesodatos.kipon.dtos.response.UsuarioDTO;
import org.accesodatos.kipon.service.HuchaService;
import org.accesodatos.kipon.service.AhorroService;
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
    public ResponseEntity<List<HuchaDTO>> obtenerTodasLasHuchas(){
        List<HuchaDTO> huchas = huchaService.obtenerTodasLasHuchas();
        if (huchas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(huchas);
    }

    @PostMapping
    public ResponseEntity<HuchaDTO> crearHucha(@Valid @RequestBody HuchaCreateDTO dto) {
        HuchaDTO huchaCreada = huchaService.crearHucha(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(huchaCreada);
    }

    @PostMapping("/{id}/ahorros")
    public ResponseEntity<AhorroDTO> crearAhorro(@PathVariable Long id,
                                                            @Valid @RequestBody AhorroCreateDTO dto){
        AhorroDTO transaccionCreada = ahorroService.crearAhorro(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaccionCreada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HuchaDTO> obtenerHuchaPorId(@PathVariable Long id){
        HuchaDTO hucha = huchaService.obtenerHuchaPorId(id);

        if (hucha == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(hucha);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HuchaDTO> actualizarHuchaParcial(@PathVariable Long id, @RequestBody JsonNode patch){
        HuchaDTO huchaActualizada = huchaService.actualizarHuchaParcial(id, patch);
        return ResponseEntity.ok(huchaActualizada);
    }

    @GetMapping("/administrador/{id}")
    public ResponseEntity<List<HuchaDTO>> obtenerHuchasPorIdAdministrador(@PathVariable Long id){
        List<HuchaDTO> huchas = huchaService.obtenerHuchasPorIdAdministrador(id);
        if (huchas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(huchas);
    }

    @PostMapping("/usuario-hucha")
    public ResponseEntity<UsuarioDTO> añadirUsuarioHucha(@Valid @RequestBody UsuarioHuchaCreateDTO dto) {
        UsuarioDTO respuesta = usuarioHuchaService.añadirUsuarioHucha(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @DeleteMapping("/usuario-hucha/{idUsuario}-{idHucha}")
    public ResponseEntity<Void> eliminarUsuarioHucha(@PathVariable Long idUsuario, @PathVariable Long idHucha){
        usuarioHuchaService.eliminarUsarioHucha(idUsuario, idHucha);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> elminarHucha(@PathVariable Long id) {
        huchaService.eliminarHucha(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

}

package org.accesodatos.kipon.controller;

import lombok.RequiredArgsConstructor;
import org.accesodatos.kipon.dtos.response.HuchaDTO;
import org.accesodatos.kipon.service.HuchaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kipon/huchas")
public class HuchasRestController {
    private final HuchaService huchaService;

    @GetMapping
    public ResponseEntity<List<HuchaDTO>> obtenerTodasLasHuchas(){
        List<HuchaDTO> huchas = huchaService.obtenerTodasLasHuchas();
        if (huchas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(huchas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HuchaDTO> obtenerHuchaPorId(@PathVariable Long id){
        HuchaDTO huchaDTO = huchaService.obtenerHuchaPorId(id);

        if (huchaDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(huchaDTO);
    }

    @GetMapping("/administrador/{id}")
    public ResponseEntity<List<HuchaDTO>> obtenerHuchasPorIdAdministrador(@PathVariable Long id){
        List<HuchaDTO> huchas = huchaService.obtenerHuchasPorIdAdministrador(id);
        if (huchas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(huchas);
    }

}

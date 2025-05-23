package org.accesodatos.kipon.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.accesodatos.kipon.dtos.request.create.LoginDTO;
import org.accesodatos.kipon.dtos.request.create.UsuarioCreateDTO;
import org.accesodatos.kipon.dtos.response.UsuarioDTO;
import org.accesodatos.kipon.jwt.JwtService;
import org.accesodatos.kipon.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kipon/auth")
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    @Operation(summary = "Inicio de sesión de un usuario")
    public ResponseEntity<String> loguearUsuario(@RequestBody LoginDTO loginDTO) {
        System.out.println("Login Request - Username: " + loginDTO.getUserName() + ", Password: " + loginDTO.getPassword());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUserName(), loginDTO.getPassword()));
            String token = jwtService.generateToken(loginDTO.getUserName());
            return ResponseEntity.ok(token);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nombre de usuario o contraseña incorrectos");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al autenticar el usuario");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody UsuarioCreateDTO dto) {
        try {
            UsuarioDTO usuarioCreado = usuarioService.crearUsuario(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
        } catch (Exception e) {
            Map<String, String> errorResponse = Map.of("error", "Error inesperado");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}

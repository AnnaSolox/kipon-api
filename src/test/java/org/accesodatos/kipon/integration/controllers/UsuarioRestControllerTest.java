package org.accesodatos.kipon.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.accesodatos.kipon.controller.UsuarioRestController;
import org.accesodatos.kipon.dtos.request.create.PerfilCreateDTO;
import org.accesodatos.kipon.dtos.request.create.UsuarioCreateDTO;
import org.accesodatos.kipon.dtos.request.update.PerfilUpdateDTO;
import org.accesodatos.kipon.dtos.request.update.UsuarioUpdateDTO;
import org.accesodatos.kipon.dtos.response.PerfilDTO;
import org.accesodatos.kipon.dtos.response.UsuarioDTO;
import org.accesodatos.kipon.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioRestController.class)
@ExtendWith(SpringExtension.class)
public class UsuarioRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioCreateDTO usuarioCreateDTO;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp(){
        // UsuarioCreateDTO para el request
        usuarioCreateDTO = new UsuarioCreateDTO();
        usuarioCreateDTO.setNombre("usuarioTest");
        usuarioCreateDTO.setPassword("passwordTest");
        usuarioCreateDTO.setEmail("usuarioEmail@test.com");
        usuarioCreateDTO.setFechaRegistro(LocalDate.now());

        PerfilCreateDTO perfilCreateDTO = new PerfilCreateDTO();
        perfilCreateDTO.setNombreCompleto("NombreCompletoTest");
        perfilCreateDTO.setTelefono("123456789");
        perfilCreateDTO.setDireccion("DireccionTest");

        usuarioCreateDTO.setPerfil(perfilCreateDTO);

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNombre("usuarioTest");
        usuarioDTO.setEmail("usuarioEmail@test.com");
        usuarioDTO.setFechaRegistro(LocalDate.now());
    }

    @Test
    void obtenerTodosLosUsuario_Exito() throws Exception {
        //Prueba para GET /kipon/usuarios

        //GIVEN
        List<UsuarioDTO> usuarios = new java.util.ArrayList<>(List.of(usuarioDTO));

        when(usuarioService.obtenerTodosLosUsuarios()).thenReturn(usuarios);

        // WHEN & THEN
        mockMvc.perform(get("/kipon/usuarios"))
                .andExpect(status().isOk());

        verify(usuarioService, times(1)).obtenerTodosLosUsuarios();
    }

    @Test
    void obtenerUsuarioPorId_Exito() throws Exception{
        //Prueba para GET /kipon/usuarios/{id} cuando el usuario existe

        //GIVEN
        when(usuarioService.obtenerUsuarioPorId(1L)).thenReturn(usuarioDTO);

        // WHEN & THEN
        mockMvc.perform(get("/kipon/usuarios/1"))
                .andExpect(status().isOk());

        verify(usuarioService).obtenerUsuarioPorId(1L);
    }

    @Test
    void obtenerUsuarioPorId_NoExiste() throws Exception {
        //Prueba para GET /kipon/usuarios/{id} cuando el usuario no exite

        //GIVEN
        when(usuarioService.obtenerUsuarioPorId(2L)).thenReturn(null);

        // WHEN & THEN
        mockMvc.perform(get("/kipon/usuarios/2"))
                .andExpect(status().isNotFound());

        verify(usuarioService, times(1)).obtenerUsuarioPorId(2L);
    }

    @Test
    void crearUsuario_Exito() throws Exception {
        // Prueba para POST /kipon/usuarios con datos válidos

        // GIVEN
        when(usuarioService.crearUsuario(any(UsuarioCreateDTO.class))).thenReturn(usuarioDTO);

        String jsonBody = objectMapper.writeValueAsString(usuarioCreateDTO);

        // WHEN & THEN
        mockMvc.perform(post("/kipon/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated());

        verify(usuarioService).crearUsuario(any(UsuarioCreateDTO.class));
    }

    @Test
    void crearUsuario_DatosInvalidos() throws Exception {
        // Prueba para POST /kipon/usuarios con datos inválidos

        // GIVEN
        usuarioCreateDTO.setPassword("");
        String jsonBody = objectMapper.writeValueAsString(usuarioCreateDTO);

        // WHEN & THEN: Se espera 400 Bad Request
        mockMvc.perform(post("/kipon/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest());

        verify(usuarioService, never()).crearUsuario(any(UsuarioCreateDTO.class));
    }

    @Test
    void actualizarUsuario_Exito() throws Exception {
        // Prueba para PUT o PATCH /kipon/usuarios/{id} con datos válidos

        // GIVEN
        PerfilUpdateDTO perfilUpdateDTO = new PerfilUpdateDTO();
        perfilUpdateDTO.setTelefono("1234567890");
        perfilUpdateDTO.setDireccion("DireccionTestUpdate");

        UsuarioUpdateDTO usuarioUpdateDTO = new UsuarioUpdateDTO();
        usuarioUpdateDTO.setNombre("usuarioTest");
        usuarioUpdateDTO.setEmail("usuarioActualizado@test.com");
        usuarioUpdateDTO.setPassword("nuevoPassword");
        usuarioUpdateDTO.setPerfil(perfilUpdateDTO);

        String jsonBody = objectMapper.writeValueAsString(usuarioUpdateDTO);

        PerfilDTO perfilDTO = new PerfilDTO();
        perfilDTO.setTelefono("1234567890");
        perfilDTO.setDireccion("DireccionTestUpdate");

        usuarioDTO.setEmail("usuarioActualizado@test.com");
        usuarioDTO.setPerfil(perfilDTO);


        when(usuarioService.actualizarUsuario(eq(1L), any(UsuarioUpdateDTO.class))).thenReturn(usuarioDTO);

        // WHEN & THEN
        mockMvc.perform(put("/kipon/usuarios/1", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("usuarioActualizado@test.com"))
                .andExpect(jsonPath("$.perfil.telefono").value("1234567890"))
                .andExpect(jsonPath("$.perfil.direccion").value("DireccionTestUpdate"));

        verify(usuarioService).actualizarUsuario(eq(1L), any(UsuarioUpdateDTO.class));
    }

    @Test
    void eliminarUsuario_Exito() throws Exception {
        // Prueba para DELETE /kipon/usuarios/{id} cuando el usuario existe

        // GIVEN
        doNothing().when(usuarioService).eliminarUsuario(1L);

        // WHEN & THEN
        mockMvc.perform(delete("/kipon/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(usuarioService).eliminarUsuario(1L);
    }
}

package org.accesodatos.kipon.integration.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.accesodatos.kipon.controller.HuchasRestController;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HuchasRestController.class)
@ExtendWith(SpringExtension.class)
public class HuchasRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HuchaService huchaService;

    @MockitoBean
    private UsuarioHuchaService usuarioHuchaService;

    @MockitoBean
    private AhorroService ahorroService;

    @Autowired
    private ObjectMapper objectMapper;

    private HuchaCreateDTO huchaCreateDTO;
    private HuchaDTO huchaDTO;

    @BeforeEach
    void setUp() {
        // HuchaCreateDTO para el request
        huchaCreateDTO = new HuchaCreateDTO();
        huchaCreateDTO.setNombre("HuchaTest");
        huchaCreateDTO.setObjetivoAhorro(1000.0);
        huchaCreateDTO.setFechaObjetivo(LocalDate.now().plusMonths(3));

        huchaDTO = new HuchaDTO();
        huchaDTO.setId(1L);
        huchaDTO.setNombre("HuchaTest");
        huchaDTO.setCantidadTotal(500.0);
        huchaDTO.setObjetivoAhorro(1000.0);
        huchaDTO.setFechaCreacion(LocalDateTime.now());
        huchaDTO.setFechaObjetivo(LocalDate.now().plusMonths(6));
        huchaDTO.setAdministrador("AdministradorTest");
        huchaDTO.setAhorros(new ArrayList<>());
    }

    @Test
    void obtenerTodasLasHuchas_Exito() throws Exception {
        //Prueba para GET /kipon/huchas

        // GIVEN
        List<HuchaDTO> huchas = List.of(huchaDTO);
        when(huchaService.obtenerTodasLasHuchas()).thenReturn(huchas);

        // WHEN & THEN
        mockMvc.perform(get("/kipon/huchas"))
                .andExpect(status().isOk());

        verify(huchaService).obtenerTodasLasHuchas();
    }

    @Test
    void obtenerHuchaPorId_Exito() throws Exception {
        //Prueba para GET /kipon/huchas/{id} cuando la hucha existe

        // GIVEN
        when(huchaService.obtenerHuchaPorId(1L)).thenReturn(huchaDTO);

        // WHEN & THEN
        mockMvc.perform(get("/kipon/huchas/1"))
                .andExpect(status().isOk());

        verify(huchaService).obtenerHuchaPorId(1L);
    }

    @Test
    void obtenerHuchaPorId_NoExiste() throws Exception {
        //Prueba para GET /kipon/huchas/{id} cuando la hucha no existe

        // GIVEN
        when(huchaService.obtenerHuchaPorId(2L)).thenReturn(null);

        // WHEN & THEN
        mockMvc.perform(get("/kipon/huchas/2"))
                .andExpect(status().isNotFound());

        verify(huchaService).obtenerHuchaPorId(2L);
    }

    @Test
    void obtenerHuchasPorIdAdministrador_Exito() throws Exception {
        //Prueba para GET /kipon/huchas/administrador/{id} con datos válidos

        // GIVEN
        List<HuchaDTO> huchas = List.of(huchaDTO);
        when(huchaService.obtenerHuchasPorIdAdministrador(1L)).thenReturn(huchas);

        // WHEN & THEN
        mockMvc.perform(get("/kipon/huchas/administrador/{id}", 1L))
                .andExpect(status().isOk());

        verify(huchaService).obtenerHuchasPorIdAdministrador(1L);
    }

    @Test
    void obtenerHuchasPorIdAdministrador_NoContent() throws Exception {
        //Prueba para GET /kipon/huchas/administrador/{id} con datos inválidos

        // GIVEN: Lista vacía
        when(huchaService.obtenerHuchasPorIdAdministrador(2L)).thenReturn(new ArrayList<>());

        // WHEN & THEN
        mockMvc.perform(get("/kipon/huchas/administrador/{id}", 2L))
                .andExpect(status().isNoContent());

        verify(huchaService).obtenerHuchasPorIdAdministrador(2L);
    }

    @Test
    void crearHucha_Exito() throws Exception {
        //Prueba para POST /kipon/huchas

        // GIVEN
        when(huchaService.crearHucha(any(HuchaCreateDTO.class))).thenReturn(huchaDTO);

        String jsonBody = objectMapper.writeValueAsString(huchaCreateDTO);

        // WHEN & THEN
        mockMvc.perform(post("/kipon/huchas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated());

        verify(huchaService).crearHucha(any(HuchaCreateDTO.class));
    }

    @Test
    void crearHucha_DatosInvalidos() throws Exception {
        //Prueba para POST /kipon/huchas con datos inválidos

        // GIVEN: Datos inválidos (nombre vacío)
        huchaCreateDTO.setNombre("");
        String jsonBody = objectMapper.writeValueAsString(huchaCreateDTO);

        // WHEN & THEN
        mockMvc.perform(post("/kipon/huchas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest());

        verify(huchaService, never()).crearHucha(any(HuchaCreateDTO.class));
    }

    @Test
    void actualizarHuchaParcial_Exito() throws Exception {
        //Prueba para PATCH /kipon/huchas/{id}

        // GIVEN
        HuchaPatchDTO huchaPatchDTO = new HuchaPatchDTO();
        huchaPatchDTO.setNombre("Nuevo NuevoNombreHucha");

        String jsonNode = objectMapper.writeValueAsString(huchaPatchDTO);

        HuchaDTO huchaActualizada = new HuchaDTO();
        huchaActualizada.setNombre("NuevoNombreHucha");
        huchaActualizada.setObjetivoAhorro(1500.0);

        huchaDTO.setNombre("NuevoNombreHucha");
        huchaDTO.setObjetivoAhorro(1500.0);

        when(huchaService.actualizarHuchaParcial(eq(1L), any(JsonNode.class)))
                .thenReturn(huchaActualizada);;

        // WHEN & THEN
        mockMvc.perform(patch("/kipon/huchas/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonNode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("NuevoNombreHucha"))
                .andExpect(jsonPath("$.objetivoAhorro").value(1500.0));

        // Verificamos que se haya llamado al método del servicio
        verify(huchaService).actualizarHuchaParcial(eq(1L), any(JsonNode.class));
    }

    @Test
    void crearAhorro_Exito() throws Exception {
        //Prueba para POST /kipon/huchas/1/ahorros

        // GIVEN
        AhorroCreateDTO ahorroCreateDTO = new AhorroCreateDTO();
        ahorroCreateDTO.setIdUsuario(1L);
        ahorroCreateDTO.setCantidad(200.0);

        AhorroDTO ahorroDTO = new AhorroDTO();
        ahorroDTO.setId(1L);
        ahorroDTO.setNombreUsuario("usuarioTest");
        ahorroDTO.setCantidad(200.0);

        when(ahorroService.crearAhorro(eq(1L), any(AhorroCreateDTO.class))).thenReturn(ahorroDTO);

        String jsonBody = objectMapper.writeValueAsString(ahorroCreateDTO);

        // WHEN & THEN
        mockMvc.perform(post("/kipon/huchas/1/ahorros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated());

        verify(ahorroService, times(1)).crearAhorro(eq(1L), any(AhorroCreateDTO.class));
    }

    @Test
    void crearAhorro_DatosInvalidos() throws Exception {
        //Prueba para POST /kipon/huchas/1/ahorros con datos inválidos

        // GIVEN
        AhorroCreateDTO ahorroCreateDTO = new AhorroCreateDTO();
        ahorroCreateDTO.setIdUsuario(null);
        ahorroCreateDTO.setCantidad(-100.0);

        String jsonBody = objectMapper.writeValueAsString(ahorroCreateDTO);

        // WHEN & THEN: Se espera que la validación falle y se retorne 400 Bad Request
        mockMvc.perform(post("/kipon/huchas/1/ahorros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest());

        // Verificamos que el método del servicio no sea invocado
        verify(ahorroService, never()).crearAhorro(anyLong(), any(AhorroCreateDTO.class));
    }

    @Test
    void añadirUsuarioHucha_Exito() throws Exception {
        //Prueba para POST /kipon/huchas/usuario-hucha con datos validos

        // GIVEN
        UsuarioHuchaCreateDTO usuarioHuchaCreateDTO = new UsuarioHuchaCreateDTO();
        usuarioHuchaCreateDTO.setIdUsuario(1L);
        usuarioHuchaCreateDTO.setIdHucha(1L);
        usuarioHuchaCreateDTO.setRol("Miembro");

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNombre("usuarioTest");

        when(usuarioHuchaService.añadirUsuarioHucha(any(UsuarioHuchaCreateDTO.class))).thenReturn(usuarioDTO);

        String jsonBody = objectMapper.writeValueAsString(usuarioHuchaCreateDTO);

        // WHEN & THEN
        mockMvc.perform(post("/kipon/huchas/usuario-hucha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated());

        verify(usuarioHuchaService).añadirUsuarioHucha(any(UsuarioHuchaCreateDTO.class));
    }

    @Test
    void añadirUsuarioHucha_DatosInvalidos() throws Exception {
        //Prueba para POST /kipon/huchas/usuario-hucha con datos inválidos

        // GIVEN
        UsuarioHuchaCreateDTO usuarioHuchaCreateDTO = new UsuarioHuchaCreateDTO();
        usuarioHuchaCreateDTO.setIdUsuario(1L);
        usuarioHuchaCreateDTO.setIdHucha(1L);
        usuarioHuchaCreateDTO.setRol("Invalido");

        String jsonBody = objectMapper.writeValueAsString(usuarioHuchaCreateDTO);

        // WHEN & THEN
        mockMvc.perform(post("/kipon/huchas/usuario-hucha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest());

        // Verificamos que el método del servicio no sea invocado
        verify(usuarioHuchaService, never()).añadirUsuarioHucha(any(UsuarioHuchaCreateDTO.class));
    }

    @Test
    void eliminarUsuarioHucha_Exito() throws Exception {
        //Prueba para DELETE /kipon/huchas/usuario-hucha/{idUsuario}-{idHucha} con datos válidos
        // GIVEN
        Long idUsuario = 1L;
        Long idHucha = 1L;

        doNothing().when(usuarioHuchaService).eliminarUsarioHucha(idUsuario, idHucha);

        // WHEN & THEN
        mockMvc.perform(delete("/kipon/huchas/usuario-hucha/{idUsuario}-{idHucha}", idUsuario, idHucha))
                .andExpect(status().isNoContent());

        verify(usuarioHuchaService).eliminarUsarioHucha(idUsuario, idHucha);
    }

    @Test
    void eliminarHucha_Exito() throws Exception {
        //Prueba para DELETE /kipon/huchas/{id} cuando el usuario existe

        // GIVEN
        doNothing().when(huchaService).eliminarHucha(1L);

        // WHEN & THEN
        mockMvc.perform(delete("/kipon/huchas/1"))
                .andExpect(status().isNoContent());

        verify(huchaService, times(1)).eliminarHucha(1L);
    }
}

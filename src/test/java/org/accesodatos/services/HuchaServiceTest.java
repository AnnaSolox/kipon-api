package org.accesodatos.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validator;
import org.accesodatos.kipon.dtos.request.create.HuchaCreateDTO;
import org.accesodatos.kipon.dtos.request.patch.HuchaPatchDTO;
import org.accesodatos.kipon.dtos.response.HuchaDTO;
import org.accesodatos.kipon.mappers.HuchaMapper;
import org.accesodatos.kipon.model.Hucha;
import org.accesodatos.kipon.model.Usuario;
import org.accesodatos.kipon.repository.HuchaRepository;
import org.accesodatos.kipon.repository.UsuarioRepository;
import org.accesodatos.kipon.service.impl.HuchaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HuchaServiceTest {
    @Mock
    private HuchaRepository huchaRepository;
    @Mock
    private HuchaMapper huchaMapper;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Spy
    private ObjectMapper objectMapper;
    @Mock
    private Validator validator;

    @InjectMocks
    private HuchaServiceImpl huchaService;

    private Hucha hucha;
    private HuchaCreateDTO huchaCreateDTO;
    private HuchaPatchDTO huchaPatchDTO;
    private HuchaDTO huchaDTO;
    private Usuario usuario;

    @BeforeEach
    void setUp(){
        // Crear un usuario (administrador)
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("usuarioAdministrador");

        // Crear una hucha
        hucha = new Hucha();
        hucha.setId(1L);
        hucha.setNombre("Hucha de Ahorro");
        hucha.setCantidadTotal(0.0);
        hucha.setObjetivoAhorro(500.0);
        hucha.setFechaCreacion(LocalDateTime.now());
        hucha.setFechaObjetivo(LocalDate.now().plusMonths(6));
        hucha.setAdministrador(usuario);
        hucha.setAhorros(new ArrayList<>());
        hucha.setUsuarios(new ArrayList<>());

        // Crear un HuchaCreateDTO
        huchaCreateDTO = new HuchaCreateDTO();
        huchaCreateDTO.setNombre("Hucha de Ahorro");
        huchaCreateDTO.setObjetivoAhorro(500.0);
        huchaCreateDTO.setFechaObjetivo(LocalDate.now().plusMonths(6));
        huchaCreateDTO.setIdAdministrador(1L);

        // Crear un HuchaPatchDTO
        huchaPatchDTO = new HuchaPatchDTO();

        // Crear un HuchaDTO
        huchaDTO = new HuchaDTO();
        huchaDTO.setId(1L);
        huchaDTO.setNombre("Hucha de Ahorro");
        huchaDTO.setCantidadTotal(0.0);
        huchaDTO.setObjetivoAhorro(500.0);
        huchaDTO.setFechaCreacion(LocalDateTime.now());
        huchaDTO.setFechaObjetivo(LocalDate.now().plusMonths(6));
        huchaDTO.setAdministrador("usuarioAdministrador");
        huchaDTO.setAhorros(Collections.emptyList());
    }

    @Test
    void obtenerTodasLasHuchas_Exito(){
        //GIVEN
        when(huchaRepository.findAllByOrderByIdAsc()).thenReturn(Collections.singletonList(hucha));
        when(huchaMapper.toDTO(any(Hucha.class))).thenReturn(huchaDTO);

        //WHEN
        List<HuchaDTO> resultado = huchaService.obtenerTodasLasHuchas();

        //THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(huchaDTO.getNombre(), resultado.get(0).getNombre());
    }

    @Test
    void obtenerHuchaPorId_Existe(){
        //GIVEN
        when(huchaRepository.findById(1L)).thenReturn(Optional.of(hucha));
        when(huchaMapper.toDTO(any(Hucha.class))).thenReturn(huchaDTO);

        //WHEN
        HuchaDTO resultado = huchaService.obtenerHuchaPorId(1L);

        //THEN
        assertNotNull(resultado);
        assertEquals("Hucha de Ahorro", resultado.getNombre());
        assertEquals("usuarioAdministrador", resultado.getAdministrador());
    }

    @Test
    void obtenerHuchaPorId_NoExiste(){
        //GIVEN
        when(huchaRepository.findById(999L)).thenReturn(Optional.empty());

        //WHEN && THEN
        assertThrows(NoSuchElementException.class, () -> huchaService.obtenerHuchaPorId(999L));
    }

    @Test
    void obtenerHuchaPorIdAdministrador_Existe(){
        //GIVEN
        List<Hucha> huchas = List.of(new Hucha());
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(huchaRepository.findByAdministradorId(1L)).thenReturn(huchas);
        when(huchaMapper.toDTO(any(Hucha.class))).thenReturn(huchaDTO);

        //WHEN
        List<HuchaDTO> resultado = huchaService.obtenerHuchasPorIdAdministrador(1L);

        //THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Hucha de Ahorro", resultado.get(0).getNombre());
        assertEquals(500.0, resultado.get(0).getObjetivoAhorro());
    }

    @Test
    void obtenerHuchaPorAdministrador_NoExiste(){
        when(usuarioRepository.existsById(999L)).thenReturn(false);

        //WHEN && THEN
        assertThrows(NoSuchElementException.class, () -> huchaService.obtenerHuchasPorIdAdministrador(999L));
    }

    @Test
    void crearHucha_Exito(){
        //GIVEN
        when(huchaMapper.toEntity(huchaCreateDTO)).thenReturn(hucha);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(huchaRepository.save(any(Hucha.class))).thenReturn(hucha);
        when(huchaMapper.toDTO(any(Hucha.class))).thenReturn(huchaDTO);

        //WHEN
        HuchaDTO resultado = huchaService.crearHucha(huchaCreateDTO);

        //THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Hucha de Ahorro", resultado.getNombre());
        assertEquals(500.0, resultado.getObjetivoAhorro());
    }

    @Test
    void eliminarHucha_Exito(){
        //GIVEN
        when(huchaRepository.findById(1L)).thenReturn(Optional.of(hucha));

        //WHEN
        huchaService.eliminarHucha(1L);

        //THEN
        verify(huchaRepository).delete(any(Hucha.class));
    }

    @Test
    void actualizarHuchaParcial_Exito() throws JsonProcessingException {
        //GIVEN
        when(huchaRepository.findById(1L)).thenReturn(Optional.of(hucha));
        when(huchaRepository.save(any(Hucha.class))).thenReturn(hucha);

        //Stubbear convertValue para que devuelva un HuchaPatchDTO no nulo
        doReturn(new HuchaPatchDTO())
                .when(objectMapper).convertValue(any(JsonNode.class), eq(HuchaPatchDTO.class));

        //Stubbear readerForUpdating para que devuelva un ObjectReader real
        doReturn(new ObjectMapper().readerForUpdating(new HuchaPatchDTO()))
                .when(objectMapper).readerForUpdating(any(HuchaPatchDTO.class));

        //Simular actualización del patch sobre el nombre
        hucha.setNombre("Hucha de Ahorro Patcheada");
        huchaDTO.setNombre("Hucha de Ahorro Patcheada");
        when(huchaMapper.toDTO(any(Hucha.class))).thenReturn(huchaDTO);

        //Preparar el JSON de actualización para cambiar el nombre de la hucha
        String jsonPatch = "{\"nombre\": \"Hucha de Ahorro Patcheada\"}";
        JsonNode patchNode = objectMapper.readTree(jsonPatch);

        //WHEN
        HuchaDTO resultado = huchaService.actualizarHuchaParcial(1L, patchNode);

        //THEN
        assertNotNull(resultado);
        assertEquals("Hucha de Ahorro Patcheada", resultado.getNombre(),
                "El nombre debe ser actualizado a Hucha de Ahorro Patcheada");
    }

    @Test
    void eliminarHucha_NoExiste(){
        //GIVEN
        when(huchaRepository.findById(1L)).thenReturn(Optional.empty());

        //WHEN & THEN
        assertThrows(NoSuchElementException.class, () -> huchaService.eliminarHucha(1L));
    }
}

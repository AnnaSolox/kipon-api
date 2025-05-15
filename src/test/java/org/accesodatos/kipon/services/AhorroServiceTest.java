package org.accesodatos.kipon.services;

import jakarta.validation.Validator;
import org.accesodatos.kipon.dtos.request.create.AhorroCreateDTO;
import org.accesodatos.kipon.dtos.response.AhorroDTO;
import org.accesodatos.kipon.mappers.AhorroMapper;
import org.accesodatos.kipon.model.Ahorro;
import org.accesodatos.kipon.model.Hucha;
import org.accesodatos.kipon.model.Usuario;
import org.accesodatos.kipon.model.UsuarioHucha;
import org.accesodatos.kipon.repository.AhorroRepository;
import org.accesodatos.kipon.repository.HuchaRepository;
import org.accesodatos.kipon.repository.UsuarioHuchaRepository;
import org.accesodatos.kipon.repository.UsuarioRepository;
import org.accesodatos.kipon.service.impl.AhorroServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AhorroServiceTest {
    @Mock
    private AhorroRepository ahorroRepository;
    @Mock
    private AhorroMapper ahorroMapper;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private HuchaRepository huchaRepository;
    @Mock
    private UsuarioHuchaRepository usuarioHuchaRepository;
    @Mock
    private Validator validator;

    @InjectMocks
    private AhorroServiceImpl ahorroService;

    private Hucha hucha;
    private Usuario usuario;
    private AhorroCreateDTO ahorroCreateDTO;
    private Ahorro ahorro;
    private AhorroDTO ahorroDTO;
    private UsuarioHucha usuarioHucha;

    @BeforeEach
    void setUp(){
        usuario = new Usuario();
        usuario.setId(10L);
        usuario.setNombre("usuarioTest");
        usuario.setPassword("passwordTest");
        usuario.setEmail("usuariotest@email.com");
        usuario.setFechaRegistro(LocalDate.now());
        usuario.setHuchas(new ArrayList<>());

        hucha = new Hucha();
        hucha.setId(1L);
        hucha.setNombre("Hucha de Ahorro");
        hucha.setCantidadTotal(0.0);
        hucha.setObjetivoAhorro(500.0);
        hucha.setFechaCreacion(LocalDateTime.now());
        hucha.setFechaObjetivo(LocalDate.now().plusMonths(6));
        hucha.setAdministrador(usuario);

        // Crear un AhorroCreateDTO
        ahorroCreateDTO = new AhorroCreateDTO();
        ahorroCreateDTO.setIdUsuario(usuario.getId());
        ahorroCreateDTO.setCantidad(50.0);


        ahorro = new Ahorro();
        ahorro.setId(100L);
        ahorro.setCantidad(50.0);
        ahorro.setFecha(LocalDate.now());
        ahorro.setUsuario(usuario);
        ahorro.setHucha(hucha);

        ahorroDTO = new AhorroDTO();
        ahorroDTO.setId(100L);
        ahorroDTO.setCantidad(50.0);
        ahorroDTO.setFecha(LocalDate.now());
        ahorroDTO.setNombreUsuario(usuario.getNombre());

        usuarioHucha = new UsuarioHucha();
        usuarioHucha.setUsuario(usuario);
        usuarioHucha.setHucha(hucha);

        hucha.setUsuarios(Collections.singletonList(usuarioHucha));
    }

    @Test
    void crearAhorro_Exito(){
        //GIVEN
        when(huchaRepository.findById(1L)).thenReturn(Optional.of(hucha));
        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuario));
        when(usuarioHuchaRepository.findByUsuarioIdAndHuchaId(10L, 1L))
                .thenReturn(Optional.of(usuarioHucha));
        when(ahorroMapper.toEntity(ahorroCreateDTO)).thenReturn(ahorro);
        when(ahorroRepository.save(any(Ahorro.class))).thenReturn(ahorro);
        when(huchaRepository.save(any(Hucha.class))).thenReturn(hucha);
        when(ahorroMapper.toDTO(any(Ahorro.class))).thenReturn(ahorroDTO);

        //WHEN
        AhorroDTO resultado = ahorroService.crearAhorro(1L, ahorroCreateDTO);

        //THEN
        assertNotNull(resultado);
        assertEquals(100L, resultado.getId());
        assertEquals(50.0, resultado.getCantidad());

        verify(huchaRepository).save(any(Hucha.class));
        verify(ahorroRepository).save(any(Ahorro.class));
    }

    @Test
    void crearAhorro_NoExito(){
        //GIVEN
        when(huchaRepository.findById(1L)).thenReturn(Optional.of(hucha));
        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuario));
        //Simular que la asociación no existe
        when(usuarioHuchaRepository.findByUsuarioIdAndHuchaId(10L, 1L)).thenReturn(Optional.empty());

        //WHEN & THEN
        assertThrows(IllegalArgumentException.class, () -> ahorroService.crearAhorro(1L, ahorroCreateDTO));
    }
}

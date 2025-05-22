package org.accesodatos.kipon.services;

import org.accesodatos.kipon.config.security.SecurityUtils;
import org.accesodatos.kipon.dtos.request.create.UsuarioHuchaCreateDTO;
import org.accesodatos.kipon.dtos.response.UsuarioDTO;
import org.accesodatos.kipon.mappers.UsuarioMapper;
import org.accesodatos.kipon.model.*;
import org.accesodatos.kipon.model.key.UsuarioHuchaKey;
import org.accesodatos.kipon.repository.AhorroRepository;
import org.accesodatos.kipon.repository.HuchaRepository;
import org.accesodatos.kipon.repository.UsuarioHuchaRepository;
import org.accesodatos.kipon.repository.UsuarioRepository;
import org.accesodatos.kipon.service.impl.UsuarioHuchaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioHuchaTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private HuchaRepository huchaRepository;
    @Mock
    private AhorroRepository ahorroRepository;
    @Mock
    private UsuarioHuchaRepository usuarioHuchaRepository;
    @Mock
    private SecurityUtils securityUtils;

    @Mock
    UsuarioMapper usuarioMapper;
    @InjectMocks
    private UsuarioHuchaServiceImpl usuarioHuchaService;

    private Usuario usuario;
    private Hucha hucha;
    private UsuarioHucha usuarioHucha;
    private UsuarioHuchaCreateDTO usuarioHuchaCreateDTO;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("usuarioTest");
        usuario.setPassword("passwordTest");
        usuario.setEmail("usuariotest@email.com");
        usuario.setFechaRegistro(LocalDate.now());
        usuario.setHuchas(new ArrayList<>());

        Perfil perfil = new Perfil();
        perfil.setId(1L);
        perfil.setNombreCompleto("Usuario Test");
        perfil.setTelefono("123456789");
        perfil.setDireccion("Calle del testing, 23");
        perfil.setUsuario(usuario);
        usuario.setPerfil(perfil);

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNombre("usuarioTest");
        usuarioDTO.setEmail("usuariotest@email.com");

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

        usuarioHucha = new UsuarioHucha();
        UsuarioHuchaKey key = new UsuarioHuchaKey(usuario.getId(), hucha.getId());
        usuarioHucha.setId(key);
        usuarioHucha.setUsuario(usuario);
        usuarioHucha.setHucha(hucha);
        usuarioHucha.setRol("Miembro");
        usuarioHucha.setFechaIngreso(LocalDate.now());
        hucha.getUsuarios().add(usuarioHucha);
        usuario.getHuchas().add(usuarioHucha);

        usuarioHuchaCreateDTO = new UsuarioHuchaCreateDTO();
        usuarioHuchaCreateDTO.setIdUsuario(usuario.getId());
        usuarioHuchaCreateDTO.setIdHucha(hucha.getId());
        usuarioHuchaCreateDTO.setRol("Miembro");
    }

    @Test
    void añadirUsuarioHucha_Exito() {
        //GIVEN
        when(securityUtils.obtenerEmailUsuarioDesdeContexto()).thenReturn("usuariotest@email.com");
        when(securityUtils.esAdministradorDeHucha(any(Hucha.class), eq("usuariotest@email.com"))).thenReturn(true);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(huchaRepository.findById(1L)).thenReturn(Optional.of(hucha));
        when(usuarioHuchaRepository.findByUsuarioIdAndHuchaId(1L, 1L)).thenReturn(Optional.empty());
        when(usuarioMapper.toDTO(any(Usuario.class))).thenReturn(usuarioDTO);

        hucha.getUsuarios().clear();
        usuario.getHuchas().clear();

        //WHEN
        UsuarioDTO resultado = usuarioHuchaService.añadirUsuarioHucha(usuarioHuchaCreateDTO);


        //THEN
        assertNotNull(resultado);
        assertEquals(1, hucha.getUsuarios().size());
        assertEquals(1, usuario.getHuchas().size());
    }

    @Test
    void añadirUsuarioHucha_YaExisteError() {
        //GIVEN
        when(securityUtils.obtenerEmailUsuarioDesdeContexto()).thenReturn("usuariotest@email.com");
        when(securityUtils.esAdministradorDeHucha(any(Hucha.class), eq("usuariotest@email.com"))).thenReturn(true);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(huchaRepository.findById(1L)).thenReturn(Optional.of(hucha));
        when(usuarioHuchaRepository.findByUsuarioIdAndHuchaId(1L, 1L)).thenReturn(Optional.of(usuarioHucha));


        //WHEN & THEN
        assertThrows(IllegalStateException.class, () -> usuarioHuchaService.añadirUsuarioHucha(usuarioHuchaCreateDTO));
    }

    @Test
    void añadirUsuarioHucha_AdministradorError(){
        //GIVEN
        when(securityUtils.obtenerEmailUsuarioDesdeContexto()).thenReturn("usuariotest@email.com");
        when(securityUtils.esAdministradorDeHucha(any(Hucha.class), eq("usuariotest@email.com"))).thenReturn(true);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(huchaRepository.findById(1L)).thenReturn(Optional.of(hucha));
        when(usuarioHuchaRepository.findByUsuarioIdAndHuchaId(1L, 1L)).thenReturn(Optional.of(usuarioHucha));

        hucha.getUsuarios().clear();
        usuario.getHuchas().clear();
        usuarioHuchaCreateDTO.setRol("Administrador");

        //WHEN & THEN
        assertThrows(IllegalStateException.class, () -> usuarioHuchaService.añadirUsuarioHucha(usuarioHuchaCreateDTO));
    }

    @Test
    void eliminarUsuarioHucha_Exito(){
        //GIVEN
        Ahorro ahorro1 = new Ahorro();
        ahorro1.setCantidad(30.0);

        Ahorro ahorro2 = new Ahorro();
        ahorro2.setCantidad(70.0);

        List<Ahorro> ahorros = List.of(ahorro1, ahorro2);

        hucha.setCantidadTotal(150.0);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(huchaRepository.findById(1L)).thenReturn(Optional.of(hucha));
        when(usuarioHuchaRepository.findByUsuarioIdAndHuchaId(1L, 1L)).thenReturn(Optional.of(usuarioHucha));
        when(ahorroRepository.findAhorrosByUsuarioHucha(1L, 1L)).thenReturn(ahorros);
        when(huchaRepository.save(any(Hucha.class))).thenReturn(hucha);

        //WHEN
        usuarioHuchaService.eliminarUsarioHucha(1L, 1L);

        //THEN
        //Comprobar que se han restado los ahorros del usuario eliminado a la cantidad total.
        assertEquals(50.0, hucha.getCantidadTotal());
        //Comprobar que no existe asociación entre el usuario y la hucha
        assertFalse(hucha.getUsuarios().contains(usuarioHucha));
        assertFalse(usuario.getHuchas().contains(usuarioHucha));
    }

    @Test
    void eliminarUsuarioHucha_AdministradorNoExito(){
        //GIVEN
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(huchaRepository.findById(1L)).thenReturn(Optional.of(hucha));

        usuarioHucha.setRol("Administrador");
        when(usuarioHuchaRepository.findByUsuarioIdAndHuchaId(1L, 1L)).thenReturn(Optional.of(usuarioHucha));

        //WHEN & THEN
        assertThrows(IllegalStateException.class, () -> usuarioHuchaService.eliminarUsarioHucha(1L, 1L));
    }
}

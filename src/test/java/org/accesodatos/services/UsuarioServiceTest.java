package org.accesodatos.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.accesodatos.kipon.dtos.request.create.UsuarioCreateDTO;
import org.accesodatos.kipon.dtos.request.patch.UsuarioPatchDTO;
import org.accesodatos.kipon.dtos.request.update.UsuarioUpdateDTO;
import org.accesodatos.kipon.dtos.response.PerfilDTO;
import org.accesodatos.kipon.dtos.response.UsuarioDTO;
import org.accesodatos.kipon.mappers.UsuarioMapper;
import org.accesodatos.kipon.model.Perfil;
import org.accesodatos.kipon.model.Usuario;
import org.accesodatos.kipon.repository.UsuarioRepository;
import org.accesodatos.kipon.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private UsuarioMapper usuarioMapper;
    @Spy
    private ObjectMapper objectMapper;
    @Mock
    private Validator validator;

    @InjectMocks
    //Implementación del servicio:
    private UsuarioServiceImpl usuarioService;

    //Variables de prueba:
    private Usuario usuario;
    private UsuarioCreateDTO usuarioCreateDTO;
    private UsuarioUpdateDTO usuarioUpdateDTO;
    private UsuarioPatchDTO usuarioPatchDTO;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp(){
        //Configurar un usuario completo con su perfil
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

        //DTO para crear el usuario
        usuarioCreateDTO = new UsuarioCreateDTO();
        usuarioCreateDTO.setNombre("usuarioTest");
        usuarioCreateDTO.setPassword("passwordTest");
        usuarioCreateDTO.setEmail("usuariotest@email.com");

        //DTO de update
        usuarioUpdateDTO = new UsuarioUpdateDTO();
        usuarioUpdateDTO.setPassword("nuevaPassword");
        usuarioUpdateDTO.setEmail("nuevotest@email.com");

        //DTO de patch
        usuarioPatchDTO = new UsuarioPatchDTO();

        //DTO response
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNombre("usuarioTest");
        usuarioDTO.setEmail("usuariotest@email.com");

        PerfilDTO perfilDTO = new PerfilDTO();
        perfilDTO.setTelefono("123456789");
        perfilDTO.setDireccion("Calle del testing, 23");
        perfilDTO.setNombreCompleto("Usuario Test");
        usuarioDTO.setPerfil(perfilDTO);
    }

    @Test
    void obtenerTodosLosUsuarios_Exito(){
        //GIVEN
        when(usuarioRepository.findAllByOrderByIdAsc()).thenReturn(Collections.singletonList(usuario));
        when(usuarioMapper.toDTO(any(Usuario.class))).thenReturn(usuarioDTO);

        //WHEN
        List<UsuarioDTO> resultado = usuarioService.obtenerTodosLosUsuarios();

        //THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(usuarioDTO.getNombre(), resultado.get(0).getNombre());
    }

    @Test
    void obtenerUsuarioPorId_Existe(){
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toDTO(any(Usuario.class))).thenReturn(usuarioDTO);

        // WHEN
        UsuarioDTO resultado = usuarioService.obtenerUsuarioPorId(1L);

        //THEN
        assertNotNull(resultado);
        assertEquals("usuarioTest", resultado.getNombre());
        assertEquals("usuariotest@email.com", resultado.getEmail());
    }

    @Test
    void obtenerUsuarioPorId_NoExiste(){
        //GIVEN
        when(usuarioRepository.findById(57L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(NoSuchElementException.class, () -> usuarioService.obtenerUsuarioPorId(57L));
    }

    @Test
    void crearUsuario_Exito(){
        //GIVEN
        when(usuarioMapper.toEntity(usuarioCreateDTO)).thenReturn(usuario);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toDTO(any(Usuario.class))).thenReturn(usuarioDTO);

        //WHEN
        UsuarioDTO resultado = usuarioService.crearUsuario(usuarioCreateDTO);

        //THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("usuarioTest", resultado.getNombre());
    }

    @Test
    void actualizarUsuario_Exito(){
        //GIVEN
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toDTO(any(Usuario.class))).thenReturn(usuarioDTO);

        //simular cambios en el DTO de actualización
        usuarioUpdateDTO.setPassword("nuevoPassword");
        usuarioUpdateDTO.setEmail("nuevotest@email.com");

        //WHEN
        UsuarioDTO resultado = usuarioService.actualizarUsuario(1L, usuarioUpdateDTO);

        //THEN
        assertNotNull(resultado);
    }

    @Test
    void actualizarUsuarioParcial_Exito() throws JsonProcessingException {
        //GIVEN
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        //Stubear convertValue para que devuelva un UsuarioPatchDTO no nulo
        doReturn(new UsuarioPatchDTO())
                .when(objectMapper).convertValue(any(JsonNode.class), eq(UsuarioPatchDTO.class));

        //Stubear readerForUpdating para qeu devuelva un ObjectReader real
        doReturn(new ObjectMapper().readerForUpdating(new UsuarioPatchDTO()))
                .when(objectMapper).readerForUpdating(any(UsuarioPatchDTO.class));

        //Simular actualización del patch sobre el teléfono
        usuario.getPerfil().setTelefono("987654321");
        usuarioDTO.getPerfil().setTelefono("987654321");
        when(usuarioMapper.toDTO(any(Usuario.class))).thenReturn(usuarioDTO);

        //Prepara el JSON de actualización parcial para cambiar el teléfono del perfil
        String jsonPatch = "{\"perfil\": {\"telefono\": \"987654321\"}}";
        JsonNode patchNode = objectMapper.readTree(jsonPatch);

        //WHEN
        UsuarioDTO resultado = usuarioService.actualizarUsuarioParcial(1L, patchNode);

        //THEN
        assertNotNull(resultado);
        assertEquals("987654321", resultado.getPerfil().getTelefono(),
                "El teléfono deber ser actualizado a 987654321");
    }

    @Test
    void eliminarUsuario_Exito(){
        //GIVEN
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).delete(any(Usuario.class));

        //WHEN
        usuarioService.eliminarUsuario(1L);

        //THEN
        verify(usuarioRepository).delete(any(Usuario.class));
    }

    @Test
    void eliminarUsuario_NoExiste(){
        //GIVEN
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        //WHEN & THEN
        assertThrows(IllegalStateException.class, ()-> usuarioService.eliminarUsuario(1L));
    }
}

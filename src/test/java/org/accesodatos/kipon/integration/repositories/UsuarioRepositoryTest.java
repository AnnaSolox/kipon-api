package org.accesodatos.kipon.integration.repositories;

import org.accesodatos.kipon.model.Perfil;
import org.accesodatos.kipon.model.Usuario;
import org.accesodatos.kipon.repository.HuchaRepository;
import org.accesodatos.kipon.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
public class UsuarioRepositoryTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16.3");

    //Configuración para que Spring se conecte a este contenedor
    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");
    }

    //Inyección de repositorios
    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;
    private Perfil perfil;

    @BeforeEach
    void setUp() {
        // Creamos un usuario con perfil
        usuario = new Usuario();
        usuario.setNombre("usuarioTest");
        usuario.setPassword("passwordTest");
        usuario.setEmail("usuario@test.com");
        usuario.setFechaRegistro(LocalDate.now());

        perfil = new Perfil();
        perfil.setNombreCompleto("Nombre Completo");
        perfil.setTelefono("123456789");
        perfil.setDireccion("Direccion Test");

        // Sincronización bidireccional
        perfil.setUsuario(usuario);
        usuario.setPerfil(perfil);

        usuarioRepository.save(usuario);
    }

    @Test
    void guardarYRecuperarUsuario_Exito() {
        // GIVEN: Crear un usuario y sus dependencias
        // Crear usuario
        Usuario usuario = new Usuario();
        usuario.setNombre("nuevoUserTest");
        usuario.setPassword("nuevoUserpasswordTest");
        usuario.setEmail("nuevoUser@test.com");
        usuario.setFechaRegistro(LocalDate.now());

        // Crear perfil
        Perfil perfil = new Perfil();
        perfil.setNombreCompleto("Nuevo User");
        perfil.setTelefono("123456745");
        perfil.setDireccion("Direccion Nuevo User Test");

        perfil.setUsuario(usuario);
        usuario.setPerfil(perfil);

        //WHEN
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        Optional<Usuario> encontrarUsuario = usuarioRepository.findById(usuarioGuardado.getId());

        //THEN
        assertTrue(encontrarUsuario.isPresent());
        Usuario usuarioEncontrado = encontrarUsuario.get();

        //Verificar Usuario
        assertEquals("nuevoUserTest", usuarioEncontrado.getNombre());
        assertEquals("nuevoUserpasswordTest", usuarioEncontrado.getPassword());
        assertEquals("nuevoUser@test.com", usuarioEncontrado.getEmail());
        assertEquals(LocalDate.now(), usuarioEncontrado.getFechaRegistro());

        //Verificar Perfil
        assertNotNull(usuarioEncontrado.getPerfil());
        assertEquals("Nuevo User", usuarioEncontrado.getPerfil().getNombreCompleto());
        assertEquals("123456745", usuarioEncontrado.getPerfil().getTelefono());
        assertEquals("Direccion Nuevo User Test", usuarioEncontrado.getPerfil().getDireccion());

    }

    @Test
    void updateUsuario_Exito() {
        // Implementar la prueba que actualice datos del usuario y verifique los cambios
        //GIVEN
        usuario.setNombre("usuarioTestUpdate");
        usuario.setPassword("passwordTestUpdate");
        usuario.setEmail("emailTestUpdate@test.com");

        //WHEN
        perfil.setNombreCompleto("Nombre Completo Update");
        perfil.setTelefono("1234567890");
        usuarioRepository.save(usuario);

        //THEN
        Usuario usuarioActualizado = usuarioRepository.findById(usuario.getId()).orElseThrow();
        assertEquals("usuarioTestUpdate", usuarioActualizado.getNombre());
        assertEquals("passwordTestUpdate", usuarioActualizado.getPassword());
        assertEquals("emailTestUpdate@test.com", usuarioActualizado.getEmail());
        assertEquals("Nombre Completo Update", usuarioActualizado.getPerfil().getNombreCompleto());
        assertEquals("1234567890", usuarioActualizado.getPerfil().getTelefono());
    }

    @Test
    void deleteUsuario_Exito() {
        // Implementar la prueba que elimine un usuario y verifique que ya no se encuentra
        //WHEN
        usuarioRepository.deleteById(usuario.getId());

        //THEN
        assertFalse(usuarioRepository.findById(usuario.getId()).isPresent());
    }

    @Test
    void findAllUsuariosOrdenadosAscendentementePorId_Exito() {
        // Implementar la prueba que recupere una lista de usuarios y verifique el tamaño esperado
        //GIVEN
        Usuario usuario2 = new Usuario();
        usuario2.setNombre("usuarioTest2");
        usuario2.setPassword("passwordTest2");
        usuario2.setEmail("emailUsuario2@test.com");
        usuario2.setFechaRegistro(LocalDate.now());

        //WHEN
        usuarioRepository.save(usuario2);
        List<Usuario> usuarios = usuarioRepository.findAllByOrderByIdAsc();

        //THEN
        assertEquals(2, usuarios.size());
    }
}



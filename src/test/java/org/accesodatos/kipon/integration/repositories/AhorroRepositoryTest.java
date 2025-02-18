package org.accesodatos.kipon.integration.repositories;
import org.accesodatos.kipon.model.Ahorro;
import org.accesodatos.kipon.model.Hucha;
import org.accesodatos.kipon.model.Usuario;
import org.accesodatos.kipon.repository.AhorroRepository;
import org.accesodatos.kipon.repository.HuchaRepository;
import org.accesodatos.kipon.repository.UsuarioHuchaRepository;
import org.accesodatos.kipon.repository.UsuarioRepository;
import org.accesodatos.kipon.service.AhorroService;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
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
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
public class AhorroRepositoryTest {
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

    @Autowired
    private AhorroRepository ahorroRepository;
    @Autowired
    private HuchaRepository huchaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;
    private Hucha hucha;
    private Ahorro ahorro;

    @BeforeEach
    void setUp() {
        // Creamos un usuario
        usuario = new Usuario();
        usuario.setNombre("usuarioTest");
        usuario.setPassword("passwordTest");
        usuario.setEmail("usuario@test.com");
        usuario.setFechaRegistro(LocalDate.now());
        usuarioRepository.save(usuario);

        // Creamos una hucha
        hucha = new Hucha();
        hucha.setNombre("Hucha de prueba");
        hucha.setFechaCreacion(LocalDateTime.now());
        hucha.setCantidadTotal(100.0);
        hucha.setAdministrador(usuario);
        huchaRepository.save(hucha);

        ahorro = new Ahorro();
    }

    @Test
    void crearAhorro_Exito() {
        // GIVEN: Crear un DTO de Ahorro para registrar un ahorro en la hucha
        ahorro.setUsuario(usuario);
        ahorro.setHucha(hucha);
        ahorro.setCantidad(50.0);
        ahorro.setFecha(LocalDate.now());

        // WHEN: Crear el ahorro
        ahorroRepository.save(ahorro);

        // THEN: Verificar que el ahorro fue creado correctamente
        Ahorro ahorroRecuperado = ahorroRepository.findById(ahorro.getId()).orElse(null);
        assertNotNull(ahorroRecuperado);
        assertEquals(50.0, ahorroRecuperado.getCantidad());
        assertEquals(usuario.getId(), ahorroRecuperado.getUsuario().getId());
        assertEquals(hucha.getId(), ahorroRecuperado.getHucha().getId());
    }

    @Test
    void crearAhorro_UsuarioNoAsociadoAlaHucha_Excepcion() {
        // GIVEN: Crear un objeto Ahorro para registrar un ahorro con un usuario no asociado a la hucha
        Usuario usuarioNoAsociado = new Usuario();
        usuarioNoAsociado.setNombre("usuarioNoAsociado");
        usuarioNoAsociado.setPassword("passwordTest");
        usuarioNoAsociado.setEmail("usuarioNoAsociado@test.com");
        usuarioNoAsociado.setFechaRegistro(LocalDate.now());
        usuarioRepository.save(usuarioNoAsociado);

        ahorro.setUsuario(usuarioNoAsociado);
        ahorro.setHucha(hucha);
        ahorro.setCantidad(50.0);
        ahorro.setFecha(LocalDate.now());

        // WHEN: Intentar guardar el ahorro, esperando que lance una excepción
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ahorroRepository.save(ahorro); // El usuario no está asociado a la hucha
        });

        // THEN: Verificar que la excepción es la esperada
        assertEquals("El usuario con id " + usuarioNoAsociado.getId() + " no está asociado a la hucha con id " + hucha.getId(), exception.getMessage());
    }
}

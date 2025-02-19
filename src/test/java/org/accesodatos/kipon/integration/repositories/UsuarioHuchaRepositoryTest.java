package org.accesodatos.kipon.integration.repositories;

import org.accesodatos.kipon.model.Ahorro;
import org.accesodatos.kipon.model.Hucha;
import org.accesodatos.kipon.model.Usuario;
import org.accesodatos.kipon.model.UsuarioHucha;
import org.accesodatos.kipon.model.key.UsuarioHuchaKey;
import org.accesodatos.kipon.repository.AhorroRepository;
import org.accesodatos.kipon.repository.HuchaRepository;
import org.accesodatos.kipon.repository.UsuarioHuchaRepository;
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
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
public class UsuarioHuchaRepositoryTest {
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
    private UsuarioHuchaRepository usuarioHuchaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private HuchaRepository huchaRepository;
    @Autowired
    private AhorroRepository ahorroRepository;

    private UsuarioHucha usuarioHucha;
    private Usuario usuario;
    private Hucha hucha;

    @BeforeEach
    void setUp() {
        // Creamos un usuario con perfil
        usuario = new Usuario();
        usuario.setNombre("usuarioTest");
        usuario.setPassword("passwordTest");
        usuario.setEmail("usuario@test.com");
        usuario.setFechaRegistro(LocalDate.now());
        usuarioRepository.save(usuario);

        hucha = new Hucha();
        hucha.setNombre("Hucha de prueba");
        hucha.setFechaCreacion(LocalDateTime.now());
        hucha.setCantidadTotal(200.0);
        hucha.setAdministrador(usuario);
        huchaRepository.save(hucha);

        UsuarioHuchaKey usuarioHuchaKey = new UsuarioHuchaKey();
        usuarioHuchaKey.setIdUsuario(usuario.getId());
        usuarioHuchaKey.setIdHucha(hucha.getId());

        // Crear entidad UsuarioHucha
        usuarioHucha = new UsuarioHucha();
        usuarioHucha.setId(usuarioHuchaKey);
        usuarioHucha.setUsuario(usuario);
        usuarioHucha.setHucha(hucha);
        usuarioHucha.setFechaIngreso(LocalDate.now());
        usuarioHucha.setRol("Administrador");

        // Guardar UsuarioHucha
        usuarioHuchaRepository.save(usuarioHucha);

        Ahorro ahorro1 = new Ahorro();
        ahorro1.setCantidad(50.0);
        ahorro1.setFecha(LocalDate.now());
        ahorro1.setUsuario(usuario);
        ahorro1.setHucha(hucha);
        ahorroRepository.save(ahorro1);

        Ahorro ahorro2 = new Ahorro();
        ahorro2.setCantidad(30.0);
        ahorro2.setFecha(LocalDate.now());
        ahorro2.setUsuario(usuario);
        ahorro2.setHucha(hucha);
        ahorroRepository.save(ahorro2);
    }

    @Test
    void añadirUsuarioAHucha_Exito() {
        // GIVEN
        // Crear un nuevo usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre("nuevoUsuarioTest");
        nuevoUsuario.setPassword("nuevoPasswordTest");
        nuevoUsuario.setEmail("nuevoUsuario@test.com");
        nuevoUsuario.setFechaRegistro(LocalDate.now());
        usuarioRepository.save(nuevoUsuario);

        // Crear clave compuesta para UsuarioHucha
        UsuarioHuchaKey usuarioHuchaKey = new UsuarioHuchaKey();
        usuarioHuchaKey.setIdUsuario(nuevoUsuario.getId());
        usuarioHuchaKey.setIdHucha(hucha.getId());

        // Crear entidad UsuarioHucha
        usuarioHucha = new UsuarioHucha();
        usuarioHucha.setId(usuarioHuchaKey);
        usuarioHucha.setUsuario(nuevoUsuario);
        usuarioHucha.setHucha(hucha);
        usuarioHucha.setFechaIngreso(LocalDate.now());
        usuarioHucha.setRol("Miembro");

        // WHEN: Añadir el nuevo usuario a la hucha
        usuarioHuchaRepository.save(usuarioHucha);

        // THEN: Verificar que la relación Usuario-Hucha se haya guardado correctamente
        Optional<UsuarioHucha> usuarioHuchaRecuperado =
                usuarioHuchaRepository.findByUsuarioIdAndHuchaId(nuevoUsuario.getId(), hucha.getId());

        assertTrue(usuarioHuchaRecuperado.isPresent());
        UsuarioHucha usuarioHuchaEncontrado = usuarioHuchaRecuperado.get();

        assertEquals("Miembro", usuarioHuchaEncontrado.getRol());
        assertEquals(nuevoUsuario.getId(), usuarioHuchaEncontrado.getUsuario().getId());
        assertEquals(hucha.getId(), usuarioHuchaEncontrado.getHucha().getId());
    }

    @Test
    void eliminarUsuarioDeHucha_Exito(){
        Double cantidadTotalAhorros = ahorroRepository.findAhorrosByUsuarioHucha(usuario.getId(), hucha.getId()).stream()
                .mapToDouble(Ahorro::getCantidad)
                .sum();

        // WHEN: Eliminar la relación Usuario-Hucha
        usuarioHuchaRepository.delete(usuarioHucha);

        // THEN: Verificar que la relación Usuario-Hucha se haya eliminado correctamente
        Optional<UsuarioHucha> usuarioHuchaRecuperado =
                usuarioHuchaRepository.findByUsuarioIdAndHuchaId(usuario.getId(), hucha.getId());

        assertFalse(usuarioHuchaRecuperado.isPresent());

        Hucha huchaRecuperada = huchaRepository.findById(hucha.getId()).orElse(null);
        assertNotNull(huchaRecuperada);

        huchaRecuperada.setCantidadTotal(huchaRecuperada.getCantidadTotal() - cantidadTotalAhorros);
        huchaRepository.save(huchaRecuperada);
        assertEquals(200.0 - cantidadTotalAhorros, huchaRecuperada.getCantidadTotal());
    }
}

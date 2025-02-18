package org.accesodatos.kipon.integration.repositories;

import org.accesodatos.kipon.model.Ahorro;
import org.accesodatos.kipon.model.Hucha;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
public class HuchaRepositoryTest {
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
    private HuchaRepository huchaRepository;
    @Autowired
    UsuarioRepository usuarioRepository;

    private Hucha hucha;
    private Usuario usuario;
    private Ahorro ahorro;

    @BeforeEach
    void setUp(){
        usuario = new Usuario();
        usuario.setNombre("usuarioTest");
        usuario.setPassword("passwordTest");
        usuario.setEmail("usuario@test.com");
        usuario.setFechaRegistro(LocalDate.now());

        usuarioRepository.save(usuario);
    }

    @Test
    void guardarYRecuperarHucha_Exito() {
        // GIVEN: Crear una hucha
        hucha = new Hucha();
        hucha.setNombre("Hucha de prueba");
        hucha.setFechaCreacion(LocalDateTime.now());
        hucha.setCantidadTotal(300.0);
        hucha.setAdministrador(usuario);

        // WHEN: Recuperar la hucha guardada
        huchaRepository.save(hucha);
        Optional<Hucha> huchaRecuperada = huchaRepository.findById(hucha.getId());

        // THEN: Verificar que la hucha se haya guardado y recuperado correctamente
        assertTrue(huchaRecuperada.isPresent());
        Hucha huchaEncontrada = huchaRecuperada.get();

        assertEquals("Hucha de prueba", huchaEncontrada.getNombre());
        assertEquals(300.0, huchaEncontrada.getCantidadTotal());
        assertNotNull(huchaEncontrada.getFechaCreacion());
        assertEquals(usuario, huchaEncontrada.getAdministrador());
    }

    @Test
    void guardarYRecuperarHuchasPorIdAscendente_Exito() {
        // GIVEN: Crear múltiples huchas
        Hucha hucha1 = new Hucha();
        hucha1.setNombre("Hucha 1");
        hucha1.setFechaCreacion(LocalDateTime.now());
        hucha1.setCantidadTotal(200.0);
        hucha1.setAdministrador(usuario);

        Hucha hucha2 = new Hucha();
        hucha2.setNombre("Hucha 2");
        hucha2.setFechaCreacion(LocalDateTime.now());
        hucha2.setCantidadTotal(500.0);
        hucha2.setAdministrador(usuario);

        huchaRepository.save(hucha1);
        huchaRepository.save(hucha2);

        // WHEN: Recuperar las huchas guardadas
        List<Hucha> huchasRecuperadas = huchaRepository.findAllByOrderByIdAsc();

        // THEN: Verificar que se han guardado y ordenado correctamente
        assertEquals(2, huchasRecuperadas.size());
        assertEquals("Hucha 1", huchasRecuperadas.get(0).getNombre());
        assertEquals("Hucha 2", huchasRecuperadas.get(1).getNombre());
        assertEquals(200.0, huchasRecuperadas.get(0).getCantidadTotal());
        assertEquals(500.0, huchasRecuperadas.get(1).getCantidadTotal());
    }

    @Test
    void buscarHuchasPorAdministradorId_Exito() {
        // GIVEN: Crear y guardar varias huchas con el mismo administrador
        Hucha hucha1 = new Hucha();
        hucha1.setNombre("Hucha 1");
        hucha1.setFechaCreacion(LocalDateTime.now());
        hucha1.setCantidadTotal(200.0);
        hucha1.setAdministrador(usuario);

        Hucha hucha2 = new Hucha();
        hucha2.setNombre("Hucha 2");
        hucha2.setFechaCreacion(LocalDateTime.now());
        hucha2.setCantidadTotal(300.0);
        hucha2.setAdministrador(usuario);

        Hucha hucha3 = new Hucha();
        hucha3.setNombre("Hucha 3");
        hucha3.setFechaCreacion(LocalDateTime.now());
        hucha3.setCantidadTotal(100.0);
        hucha3.setAdministrador(usuario);

        huchaRepository.save(hucha1);
        huchaRepository.save(hucha2);
        huchaRepository.save(hucha3);

        // WHEN
        List<Hucha> huchasPorAdministrador = huchaRepository.findByAdministradorId(usuario.getId());

        // THEN
        assertEquals(3, huchasPorAdministrador.size());
        assertTrue(huchasPorAdministrador.stream().allMatch(hucha -> hucha.getAdministrador().equals(usuario)));
    }

    @Test
    void actualizarCantidadDeHucha_Exito() {
        // GIVEN: Crear una hucha
        Hucha hucha = new Hucha();
        hucha.setNombre("Hucha de prueba");
        hucha.setFechaCreacion(LocalDateTime.now());
        hucha.setCantidadTotal(300.0);
        hucha.setAdministrador(usuario);

        huchaRepository.save(hucha);

        // WHEN: Actualizar la cantidad total de la hucha
        hucha.setCantidadTotal(400.0);
        huchaRepository.save(hucha);

        // THEN: Verificar que la cantidad se actualizó correctamente
        Hucha huchaActualizada = huchaRepository.findById(hucha.getId()).orElseThrow();
        assertEquals(400.0, huchaActualizada.getCantidadTotal());
    }

    @Test
    void eliminarHucha_Exito() {
        // GIVEN: Crear una hucha
        Hucha hucha = new Hucha();
        hucha.setNombre("Hucha para eliminar");
        hucha.setFechaCreacion(LocalDateTime.now());
        hucha.setCantidadTotal(300.0);
        hucha.setAdministrador(usuario);

        huchaRepository.save(hucha);

        // WHEN: Eliminar la hucha
        huchaRepository.deleteById(hucha.getId());

        // THEN: Verificar que la hucha ha sido eliminada correctamente
        assertFalse(huchaRepository.findById(hucha.getId()).isPresent());
    }
}

package org.accesodatos.kipon.repository;

import org.accesodatos.kipon.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository <Usuario, Long> {
    List<Usuario> findAllByOrderByIdAsc();
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByNombre(String nombre);
    List<Usuario> findByNombreContainingIgnoreCase(String nombreParcial);
}

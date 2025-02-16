package org.accesodatos.kipon.repository;

import org.accesodatos.kipon.model.Perfil;
import org.accesodatos.kipon.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerfilRepository extends JpaRepository <Perfil, Long> {
}

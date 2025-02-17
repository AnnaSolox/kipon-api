package org.accesodatos.kipon.repository;

import org.accesodatos.kipon.model.UsuarioHucha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioHuchaRepository extends JpaRepository<UsuarioHucha, Long> {
    Optional<UsuarioHucha> findByUsuarioIdAndHuchaId(Long usuarioId, Long huchaId);
}

package org.accesodatos.kipon.repository;

import org.accesodatos.kipon.model.Hucha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HuchaRepository extends JpaRepository<Hucha, Long> {
    List<Hucha> findAllByOrderByIdAsc();
    @Query("SELECT h FROM Hucha h WHERE h.administrador.id = :administradorId")
    List<Hucha> findByAdministradorId(@Param("administradorId") Long administradorId);
}

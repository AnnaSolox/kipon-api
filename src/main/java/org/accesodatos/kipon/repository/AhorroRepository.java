package org.accesodatos.kipon.repository;

import org.accesodatos.kipon.model.Ahorro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AhorroRepository extends JpaRepository <Ahorro, Long> {
    List<Ahorro> findAllByOrderByIdAsc();
    @Query("SELECT t FROM Ahorro t WHERE t.usuario.id = :userId AND t.hucha.id = :huchaId")
    List<Ahorro> findAhorrosByUsuarioHucha(
            @Param("userId") Long userId,
            @Param("huchaId") Long huchaId
    );
}

package org.accesodatos.kipon.repository;

import org.accesodatos.kipon.model.TransaccionAhorro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransaccionAhorroRepository extends JpaRepository <TransaccionAhorro, Long> {
    List<TransaccionAhorro> findAllByOrderByIdAsc();
    @Query("SELECT t FROM TransaccionAhorro t WHERE t.usuario.id = :userId AND t.hucha.id = :huchaId")
    List<TransaccionAhorro> findTransaccionesByUserAndHucha(
            @Param("userId") Long userId,
            @Param("huchaId") Long huchaId
    );
}

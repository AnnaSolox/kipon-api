package org.accesodatos.kipon.repository;

import org.accesodatos.kipon.model.TransaccionAhorro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransaccionAhorroRepository extends JpaRepository <TransaccionAhorro, Long> {
    List<TransaccionAhorro> findAllByOrderByIdAsc();
}

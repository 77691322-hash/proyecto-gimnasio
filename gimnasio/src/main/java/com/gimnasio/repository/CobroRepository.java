package com.gimnasio.repository;

import com.gimnasio.model.Cobro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CobroRepository extends JpaRepository<Cobro, Integer> {

    List<Cobro> findAllByOrderByFechaDesc();

    List<Cobro> findByFechaBetween(LocalDateTime desde, LocalDateTime hasta);
}

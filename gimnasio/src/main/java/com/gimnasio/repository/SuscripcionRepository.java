package com.gimnasio.repository;

import com.gimnasio.model.Suscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SuscripcionRepository extends JpaRepository<Suscripcion, Integer> {

    List<Suscripcion> findByFechaFinGreaterThanEqual(LocalDate fecha);

    List<Suscripcion> findByFechaInicioBetween(LocalDate desde, LocalDate hasta);

    long countByFechaFinGreaterThanEqual(LocalDate fecha);

    long countByFechaFinBetween(LocalDate desde, LocalDate hasta);
}

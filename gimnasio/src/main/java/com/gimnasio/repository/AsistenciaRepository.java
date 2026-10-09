package com.gimnasio.repository;

import com.gimnasio.model.Asistencia;
import com.gimnasio.model.Personal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Integer> {

    Optional<Asistencia> findFirstByPersonalAndFechaAndHoraSalidaIsNull(Personal personal, LocalDate fecha);

    List<Asistencia> findByFechaBetweenOrderByFechaDescHoraEntradaDesc(LocalDate desde, LocalDate hasta);

    List<Asistencia> findByPersonalIdPersonalAndFechaBetweenOrderByFechaDescHoraEntradaDesc(Integer idPersonal, LocalDate desde, LocalDate hasta);

    long countByFechaAndHoraSalidaIsNull(LocalDate fecha);
}

package com.gimnasio.repository;

import com.gimnasio.model.Paquete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaqueteRepository extends JpaRepository<Paquete, Integer> {

    List<Paquete> findByEstadoTrue();

    long countByEstadoTrue();
}

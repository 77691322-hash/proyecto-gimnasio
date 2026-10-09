package com.gimnasio.repository;

import com.gimnasio.model.Personal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonalRepository extends JpaRepository<Personal, Integer> {

    Optional<Personal> findByDni(String dni);

    Optional<Personal> findByDniAndEstadoTrue(String dni);
}

package com.gimnasio.repository;

import com.gimnasio.model.ProductoServicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoServicioRepository extends JpaRepository<ProductoServicio, Integer> {

    List<ProductoServicio> findByEstadoTrue();
}

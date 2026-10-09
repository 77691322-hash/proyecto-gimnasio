package com.gimnasio.repository;

import com.gimnasio.model.SolicitudCompra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitudCompraRepository extends JpaRepository<SolicitudCompra, Integer> {

    List<SolicitudCompra> findAllByOrderByFechaDesc();
}

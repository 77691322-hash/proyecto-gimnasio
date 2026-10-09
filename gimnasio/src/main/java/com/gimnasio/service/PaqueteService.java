package com.gimnasio.service;

import com.gimnasio.model.DetallePaquete;
import com.gimnasio.model.Paquete;
import com.gimnasio.model.ProductoServicio;
import com.gimnasio.repository.PaqueteRepository;
import com.gimnasio.repository.ProductoServicioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaqueteService {

    private final PaqueteRepository paqueteRepository;
    private final ProductoServicioRepository productoRepository;

    public PaqueteService(PaqueteRepository paqueteRepository, ProductoServicioRepository productoRepository) {
        this.paqueteRepository = paqueteRepository;
        this.productoRepository = productoRepository;
    }

    public List<Paquete> listar() {
        return paqueteRepository.findAll();
    }

    public List<Paquete> listarActivos() {
        return paqueteRepository.findByEstadoTrue();
    }

    public Paquete buscar(Integer id) {
        return paqueteRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void guardar(Paquete datos, List<Integer> idsProductos) {
        List<ProductoServicio> productosActivos = new ArrayList<>();
        if (idsProductos != null) {
            for (ProductoServicio producto : productoRepository.findAllById(idsProductos)) {
                if (producto.isEstado()) {
                    productosActivos.add(producto);
                }
            }
        }
        if (productosActivos.isEmpty()) {
            throw new ReglaNegocioException("El paquete debe contener al menos un producto o servicio activo.");
        }

        Paquete paquete = datos.getIdPaquete() == null ? new Paquete() : buscar(datos.getIdPaquete());
        paquete.setNombre(datos.getNombre());
        paquete.setDescripcion(datos.getDescripcion());
        paquete.setPrecio(datos.getPrecio());
        paquete.setEstado(datos.isEstado());
        paquete.getDetalles().clear();
        for (ProductoServicio producto : productosActivos) {
            paquete.getDetalles().add(new DetallePaquete(paquete, producto));
        }
        paqueteRepository.save(paquete);
    }

    public void cambiarEstado(Integer id) {
        Paquete paquete = buscar(id);
        paquete.setEstado(!paquete.isEstado());
        paqueteRepository.save(paquete);
    }
}

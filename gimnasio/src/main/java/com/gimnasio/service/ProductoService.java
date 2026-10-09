package com.gimnasio.service;

import com.gimnasio.model.Categoria;
import com.gimnasio.model.ProductoServicio;
import com.gimnasio.repository.CategoriaRepository;
import com.gimnasio.repository.ProductoServicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoServicioRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoServicioRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<ProductoServicio> listar() {
        return productoRepository.findAll();
    }

    public List<ProductoServicio> listarActivos() {
        return productoRepository.findByEstadoTrue();
    }

    public ProductoServicio buscar(Integer id) {
        return productoRepository.findById(id).orElseThrow();
    }

    public void guardar(ProductoServicio producto, Integer idCategoria) {
        Categoria categoria = categoriaRepository.findById(idCategoria).orElse(null);
        if (categoria == null || !categoria.isEstado()) {
            throw new ReglaNegocioException("El producto o servicio debe pertenecer a una categoría activa.");
        }
        producto.setCategoria(categoria);
        productoRepository.save(producto);
    }

    public void cambiarEstado(Integer id) {
        ProductoServicio producto = buscar(id);
        producto.setEstado(!producto.isEstado());
        productoRepository.save(producto);
    }
}

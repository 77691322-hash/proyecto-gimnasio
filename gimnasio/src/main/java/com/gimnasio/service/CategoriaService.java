package com.gimnasio.service;

import com.gimnasio.model.Categoria;
import com.gimnasio.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    public List<Categoria> listarActivas() {
        return categoriaRepository.findByEstadoTrue();
    }

    public Categoria buscar(Integer id) {
        return categoriaRepository.findById(id).orElseThrow();
    }

    public void guardar(Categoria categoria) {
        categoriaRepository.save(categoria);
    }

    public void cambiarEstado(Integer id) {
        Categoria categoria = buscar(id);
        categoria.setEstado(!categoria.isEstado());
        categoriaRepository.save(categoria);
    }
}

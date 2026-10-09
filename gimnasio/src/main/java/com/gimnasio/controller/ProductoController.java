package com.gimnasio.controller;

import com.gimnasio.model.ProductoServicio;
import com.gimnasio.service.CategoriaService;
import com.gimnasio.service.ProductoService;
import com.gimnasio.service.ReglaNegocioException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public ProductoController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productoService.listar());
        return "productos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        return mostrarFormulario(new ProductoServicio(), null, model);
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        ProductoServicio producto = productoService.buscar(id);
        return mostrarFormulario(producto, producto.getCategoria().getIdCategoria(), model);
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute ProductoServicio producto, @RequestParam Integer idCategoria,
                          Model model, RedirectAttributes mensajes) {
        try {
            productoService.guardar(producto, idCategoria);
            mensajes.addFlashAttribute("mensaje", "Producto o servicio guardado correctamente.");
            return "redirect:/productos";
        } catch (ReglaNegocioException e) {
            model.addAttribute("error", e.getMessage());
            return mostrarFormulario(producto, idCategoria, model);
        }
    }

    @PostMapping("/estado/{id}")
    public String cambiarEstado(@PathVariable Integer id) {
        productoService.cambiarEstado(id);
        return "redirect:/productos";
    }

    private String mostrarFormulario(ProductoServicio producto, Integer idCategoria, Model model) {
        model.addAttribute("producto", producto);
        model.addAttribute("idCategoria", idCategoria);
        model.addAttribute("categorias", categoriaService.listarActivas());
        return "productos/formulario";
    }
}

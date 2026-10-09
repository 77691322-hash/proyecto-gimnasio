package com.gimnasio.controller;

import com.gimnasio.model.DetallePaquete;
import com.gimnasio.model.Paquete;
import com.gimnasio.service.PaqueteService;
import com.gimnasio.service.ProductoService;
import com.gimnasio.service.ReglaNegocioException;
import com.gimnasio.service.SolicitudService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/paquetes")
public class PaqueteController {

    private final PaqueteService paqueteService;
    private final ProductoService productoService;
    private final SolicitudService solicitudService;

    public PaqueteController(PaqueteService paqueteService, ProductoService productoService,
                             SolicitudService solicitudService) {
        this.paqueteService = paqueteService;
        this.productoService = productoService;
        this.solicitudService = solicitudService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("paquetes", paqueteService.listar());
        return "paquetes/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        return mostrarFormulario(new Paquete(), new ArrayList<>(), model);
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Paquete paquete = paqueteService.buscar(id);
        List<Integer> seleccionados = new ArrayList<>();
        for (DetallePaquete detalle : paquete.getDetalles()) {
            seleccionados.add(detalle.getProducto().getIdProducto());
        }
        return mostrarFormulario(paquete, seleccionados, model);
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Paquete paquete, @RequestParam(required = false) List<Integer> productos,
                          Model model, RedirectAttributes mensajes) {
        try {
            paqueteService.guardar(paquete, productos);
            mensajes.addFlashAttribute("mensaje", "Paquete guardado correctamente.");
            return "redirect:/paquetes";
        } catch (ReglaNegocioException e) {
            model.addAttribute("error", e.getMessage());
            return mostrarFormulario(paquete, productos == null ? new ArrayList<>() : productos, model);
        }
    }

    @PostMapping("/estado/{id}")
    public String cambiarEstado(@PathVariable Integer id) {
        paqueteService.cambiarEstado(id);
        return "redirect:/paquetes";
    }

    @GetMapping("/catalogo")
    public String catalogo(Model model) {
        model.addAttribute("paquetes", paqueteService.listarActivos());
        return "paquetes/catalogo";
    }

    @PostMapping("/solicitar")
    public String solicitar(@RequestParam String dni, @RequestParam Integer idPaquete, RedirectAttributes mensajes) {
        try {
            solicitudService.registrar(dni, idPaquete);
            mensajes.addFlashAttribute("mensaje", "Solicitud registrada. Acércate a recepción para completar el pago.");
        } catch (ReglaNegocioException e) {
            mensajes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/paquetes/catalogo";
    }

    private String mostrarFormulario(Paquete paquete, List<Integer> seleccionados, Model model) {
        model.addAttribute("paquete", paquete);
        model.addAttribute("seleccionados", seleccionados);
        model.addAttribute("productos", productoService.listarActivos());
        return "paquetes/formulario";
    }
}

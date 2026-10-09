package com.gimnasio.controller;

import com.gimnasio.model.Cobro;
import com.gimnasio.service.CobroService;
import com.gimnasio.service.ReglaNegocioException;
import com.gimnasio.service.SolicitudService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/cobros")
public class CobroController {

    private final CobroService cobroService;
    private final SolicitudService solicitudService;

    public CobroController(CobroService cobroService, SolicitudService solicitudService) {
        this.cobroService = cobroService;
        this.solicitudService = solicitudService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("cobros", cobroService.listar());
        return "cobros/lista";
    }

    @GetMapping("/solicitudes")
    public String solicitudes(Model model) {
        model.addAttribute("solicitudes", solicitudService.listar());
        return "cobros/solicitudes";
    }

    @GetMapping("/nuevo")
    public String nuevo(@RequestParam Integer solicitud, Model model) {
        LocalDate hoy = LocalDate.now();
        model.addAttribute("solicitud", solicitudService.buscar(solicitud));
        model.addAttribute("fechaInicio", hoy);
        model.addAttribute("fechaFin", cobroService.calcularVencimiento(hoy));
        return "cobros/nuevo";
    }

    @PostMapping("/registrar")
    public String registrar(@RequestParam Integer idSolicitud, @RequestParam String metodoPago,
                            RedirectAttributes mensajes) {
        try {
            Cobro cobro = cobroService.registrarCobro(idSolicitud, metodoPago);
            mensajes.addFlashAttribute("mensaje", "Cobro registrado y suscripción generada.");
            return "redirect:/cobros/comprobante/" + cobro.getIdCobro();
        } catch (ReglaNegocioException e) {
            mensajes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cobros/solicitudes";
        }
    }

    @PostMapping("/rechazar/{id}")
    public String rechazar(@PathVariable Integer id, RedirectAttributes mensajes) {
        try {
            solicitudService.rechazar(id);
            mensajes.addFlashAttribute("mensaje", "Solicitud rechazada.");
        } catch (ReglaNegocioException e) {
            mensajes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cobros/solicitudes";
    }

    @GetMapping("/comprobante/{id}")
    public String comprobante(@PathVariable Integer id, Model model) {
        model.addAttribute("cobro", cobroService.buscar(id));
        return "cobros/comprobante";
    }
}

package com.gimnasio.controller;

import com.gimnasio.model.Asistencia;
import com.gimnasio.service.AsistenciaService;
import com.gimnasio.service.PersonalService;
import com.gimnasio.service.ReglaNegocioException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/asistencia")
public class AsistenciaController {

    private final AsistenciaService asistenciaService;
    private final PersonalService personalService;

    public AsistenciaController(AsistenciaService asistenciaService, PersonalService personalService) {
        this.asistenciaService = asistenciaService;
        this.personalService = personalService;
    }

    @GetMapping("/marcar")
    public String mostrarKiosco() {
        return "asistencia/marcar";
    }

    @PostMapping("/marcar")
    public String marcar(@RequestParam String dni, RedirectAttributes mensajes) {
        try {
            Asistencia asistencia = asistenciaService.marcar(dni);
            String nombre = asistencia.getPersonal().getNombre();
            if (asistencia.getHoraSalida() == null) {
                mensajes.addFlashAttribute("mensaje", "Entrada registrada: " + nombre + " a las " + asistencia.getHoraEntrada());
            } else {
                mensajes.addFlashAttribute("mensaje", "Salida registrada: " + nombre + " a las " + asistencia.getHoraSalida());
            }
        } catch (ReglaNegocioException e) {
            mensajes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/asistencia/marcar";
    }

    @GetMapping("/historial")
    public String historial(@RequestParam(required = false) LocalDate desde,
                            @RequestParam(required = false) LocalDate hasta,
                            @RequestParam(required = false) Integer idPersonal, Model model) {
        if (hasta == null) {
            hasta = LocalDate.now();
        }
        if (desde == null) {
            desde = hasta.minusDays(7);
        }
        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);
        model.addAttribute("idPersonal", idPersonal);
        model.addAttribute("trabajadores", personalService.listar());
        model.addAttribute("asistencias", asistenciaService.historial(desde, hasta, idPersonal));
        return "asistencia/historial";
    }
}

package com.gimnasio.controller;

import com.gimnasio.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String mostrar(@RequestParam(required = false) LocalDate desde,
                          @RequestParam(required = false) LocalDate hasta, Model model) {
        if (hasta == null) {
            hasta = LocalDate.now();
        }
        if (desde == null) {
            desde = hasta.minusDays(14);
        }
        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);

        model.addAttribute("clientesActivos", dashboardService.clientesActivos());
        model.addAttribute("suscripcionesVigentes", dashboardService.suscripcionesVigentes());
        model.addAttribute("suscripcionesPorVencer", dashboardService.suscripcionesPorVencer());
        model.addAttribute("paquetesActivos", dashboardService.paquetesActivos());
        model.addAttribute("personalPresente", dashboardService.personalPresente());

        model.addAttribute("ingresosPorDia", dashboardService.ingresosPorDia(desde, hasta));
        model.addAttribute("cobrosPorDia", dashboardService.cobrosPorDia(desde, hasta));
        model.addAttribute("asistenciasPorDia", dashboardService.asistenciasPorDia(desde, hasta));
        model.addAttribute("ingresosPorMes", dashboardService.ingresosPorMes());
        model.addAttribute("suscripcionesPorMes", dashboardService.suscripcionesPorMes());
        return "dashboard";
    }
}

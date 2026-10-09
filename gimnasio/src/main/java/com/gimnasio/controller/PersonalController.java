package com.gimnasio.controller;

import com.gimnasio.model.Personal;
import com.gimnasio.service.PersonalService;
import com.gimnasio.service.ReglaNegocioException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/personal")
public class PersonalController {

    private final PersonalService personalService;

    public PersonalController(PersonalService personalService) {
        this.personalService = personalService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("trabajadores", personalService.listar());
        return "personal/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("personal", new Personal());
        return "personal/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        model.addAttribute("personal", personalService.buscar(id));
        return "personal/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Personal personal, Model model, RedirectAttributes mensajes) {
        try {
            personalService.guardar(personal);
            mensajes.addFlashAttribute("mensaje", "Personal guardado correctamente.");
            return "redirect:/personal";
        } catch (ReglaNegocioException e) {
            model.addAttribute("error", e.getMessage());
            return "personal/formulario";
        }
    }

    @PostMapping("/estado/{id}")
    public String cambiarEstado(@PathVariable Integer id) {
        personalService.cambiarEstado(id);
        return "redirect:/personal";
    }
}

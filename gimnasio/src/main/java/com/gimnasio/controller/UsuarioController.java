package com.gimnasio.controller;

import com.gimnasio.model.Usuario;
import com.gimnasio.service.ReglaNegocioException;
import com.gimnasio.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listar());
        return "usuarios/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        model.addAttribute("usuario", usuarioService.buscar(id));
        return "usuarios/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Usuario usuario, Model model, RedirectAttributes mensajes) {
        try {
            usuarioService.guardar(usuario);
            mensajes.addFlashAttribute("mensaje", "Usuario guardado correctamente.");
            return "redirect:/usuarios";
        } catch (ReglaNegocioException e) {
            model.addAttribute("error", e.getMessage());
            return "usuarios/formulario";
        }
    }

    @PostMapping("/estado/{id}")
    public String cambiarEstado(@PathVariable Integer id, HttpSession session, RedirectAttributes mensajes) {
        Usuario actual = (Usuario) session.getAttribute("usuario");
        if (actual.getIdUsuario().equals(id)) {
            mensajes.addFlashAttribute("error", "No puedes desactivar tu propio usuario.");
        } else {
            usuarioService.cambiarEstado(id);
        }
        return "redirect:/usuarios";
    }
}

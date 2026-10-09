    package com.gimnasio.controller;

    import com.gimnasio.model.Usuario;
    import com.gimnasio.service.ReglaNegocioException;
    import com.gimnasio.service.UsuarioService;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpSession;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestParam;

    @Controller
    public class LoginController {

        private final UsuarioService usuarioService;

        public LoginController(UsuarioService usuarioService) {
            this.usuarioService = usuarioService;
        }

        @GetMapping("/")
        public String inicio() {
            return "redirect:/login";
        }

        @GetMapping("/login")
        public String mostrarLogin() {
            return "login";
        }

        @PostMapping("/login")
        public String autenticar(@RequestParam String usuario, @RequestParam String contrasena,
                                 HttpServletRequest request, Model model) {
            try {
                Usuario encontrado = usuarioService.autenticar(usuario, contrasena);
                request.getSession().invalidate();
                request.getSession().setAttribute("usuario", encontrado);
                if (encontrado.getRol().equals("ADMINISTRADOR")) {
                    return "redirect:/dashboard";
                }
                return "redirect:/asistencia/marcar";
            } catch (ReglaNegocioException e) {
                model.addAttribute("error", e.getMessage());
                return "login";
            }
        }

        @GetMapping("/logout")
        public String cerrarSesion(HttpSession session) {
            session.invalidate();
            return "redirect:/login";
        }
    }

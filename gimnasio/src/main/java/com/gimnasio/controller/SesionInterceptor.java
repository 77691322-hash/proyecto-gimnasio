package com.gimnasio.controller;

import com.gimnasio.model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.List;

public class SesionInterceptor implements HandlerInterceptor {

    private static final List<String> RUTAS_DE_ADMINISTRADOR = List.of(
            "/dashboard", "/categorias", "/productos", "/paquetes", "/personal", "/usuarios", "/asistencia/historial");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        if (usuario == null) {
            response.sendRedirect("/login");
            return false;
        }
        if (!usuario.getRol().equals("ADMINISTRADOR")) {
            for (String ruta : RUTAS_DE_ADMINISTRADOR) {
                if (request.getRequestURI().startsWith(ruta)) {
                    response.sendRedirect("/clientes");
                    return false;
                }
            }
        }
        return true;
    }
}

package com.gimnasio.controller;

import com.gimnasio.model.Cliente;
import com.gimnasio.model.Suscripcion;
import com.gimnasio.service.ClienteService;
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

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listar(@RequestParam(defaultValue = "") String buscar,
                         @RequestParam(defaultValue = "") String membresia, Model model) {
        Map<Integer, Suscripcion> membresias = clienteService.membresiasVigentes();
        List<Cliente> clientes = clienteService.buscarPorNombreODni(buscar);
        if (membresia.equals("vigente")) {
            clientes.removeIf(cliente -> !membresias.containsKey(cliente.getIdCliente()));
        }
        if (membresia.equals("sin")) {
            clientes.removeIf(cliente -> membresias.containsKey(cliente.getIdCliente()));
        }
        model.addAttribute("clientes", clientes);
        model.addAttribute("membresias", membresias);
        model.addAttribute("buscar", buscar);
        model.addAttribute("membresia", membresia);
        return "clientes/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        model.addAttribute("cliente", clienteService.buscar(id));
        return "clientes/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Cliente cliente, Model model, RedirectAttributes mensajes) {
        try {
            clienteService.guardar(cliente);
            mensajes.addFlashAttribute("mensaje", "Cliente guardado correctamente.");
            return "redirect:/clientes";
        } catch (ReglaNegocioException e) {
            model.addAttribute("error", e.getMessage());
            return "clientes/formulario";
        }
    }

    @PostMapping("/estado/{id}")
    public String cambiarEstado(@PathVariable Integer id) {
        clienteService.cambiarEstado(id);
        return "redirect:/clientes";
    }
}

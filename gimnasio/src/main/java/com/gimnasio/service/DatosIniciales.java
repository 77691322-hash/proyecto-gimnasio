package com.gimnasio.service;

import com.gimnasio.model.Categoria;
import com.gimnasio.model.Cliente;
import com.gimnasio.model.Paquete;
import com.gimnasio.model.Personal;
import com.gimnasio.model.ProductoServicio;
import com.gimnasio.model.Usuario;
import com.gimnasio.repository.CategoriaRepository;
import com.gimnasio.repository.ClienteRepository;
import com.gimnasio.repository.PersonalRepository;
import com.gimnasio.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DatosIniciales implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final CategoriaRepository categoriaRepository;
    private final ProductoService productoService;
    private final PaqueteService paqueteService;
    private final ClienteRepository clienteRepository;
    private final PersonalRepository personalRepository;

    public DatosIniciales(UsuarioRepository usuarioRepository, UsuarioService usuarioService,
                          CategoriaRepository categoriaRepository, ProductoService productoService,
                          PaqueteService paqueteService, ClienteRepository clienteRepository,
                          PersonalRepository personalRepository) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.categoriaRepository = categoriaRepository;
        this.productoService = productoService;
        this.paqueteService = paqueteService;
        this.clienteRepository = clienteRepository;
        this.personalRepository = personalRepository;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            return;
        }

        usuarioService.guardar(new Usuario("admin", "admin@gymsystem.pe", "admin123", "ADMINISTRADOR"));
        usuarioService.guardar(new Usuario("recepcion", "recepcion@gymsystem.pe", "recepcion123", "PERSONAL"));

        Categoria musculacion = categoriaRepository.save(new Categoria("Musculación", "Zonas de máquinas y peso libre"));
        Categoria cardio = categoriaRepository.save(new Categoria("Cardio", "Zona de caminadoras, bicicletas y elípticas"));
        Categoria clases = categoriaRepository.save(new Categoria("Clases grupales", "Clases dirigidas por un instructor"));

        ProductoServicio maquinas = new ProductoServicio("Acceso a máquinas y pesas", "Uso libre de la zona de musculación", new BigDecimal("60.00"));
        ProductoServicio zonaCardio = new ProductoServicio("Acceso a zona de cardio", "Uso libre de la zona de cardio", new BigDecimal("40.00"));
        ProductoServicio funcional = new ProductoServicio("Clase de funcional", "Circuito funcional con instructor", new BigDecimal("50.00"));
        ProductoServicio baile = new ProductoServicio("Clase de baile", "Sesiones grupales de baile", new BigDecimal("45.00"));
        productoService.guardar(maquinas, musculacion.getIdCategoria());
        productoService.guardar(zonaCardio, cardio.getIdCategoria());
        productoService.guardar(funcional, clases.getIdCategoria());
        productoService.guardar(baile, clases.getIdCategoria());

        paqueteService.guardar(new Paquete("Básico", "Acceso a la zona de musculación", new BigDecimal("80.00")),
                List.of(maquinas.getIdProducto()));
        paqueteService.guardar(new Paquete("Plata", "Musculación y cardio", new BigDecimal("110.00")),
                List.of(maquinas.getIdProducto(), zonaCardio.getIdProducto()));
        paqueteService.guardar(new Paquete("Golden", "Todas las zonas y clases grupales", new BigDecimal("160.00")),
                List.of(maquinas.getIdProducto(), zonaCardio.getIdProducto(), funcional.getIdProducto(), baile.getIdProducto()));

        clienteRepository.save(new Cliente("Juan Pérez", "70000001", "900000001", "juan.perez@correo.com"));
        clienteRepository.save(new Cliente("María Torres", "70000002", "900000002", "maria.torres@correo.com"));

        personalRepository.save(new Personal("Carlos Rojas", "40000001", "Instructor", "Lunes a sábado, 06:00 - 14:00"));
        personalRepository.save(new Personal("Ana Vega", "40000002", "Recepcionista", "Lunes a sábado, 14:00 - 22:00"));
    }
}

package com.gimnasio.service;

import com.gimnasio.model.Cliente;
import com.gimnasio.model.Paquete;
import com.gimnasio.model.SolicitudCompra;
import com.gimnasio.repository.ClienteRepository;
import com.gimnasio.repository.PaqueteRepository;
import com.gimnasio.repository.SolicitudCompraRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitudService {

    private final SolicitudCompraRepository solicitudRepository;
    private final ClienteRepository clienteRepository;
    private final PaqueteRepository paqueteRepository;

    public SolicitudService(SolicitudCompraRepository solicitudRepository, ClienteRepository clienteRepository,
                            PaqueteRepository paqueteRepository) {
        this.solicitudRepository = solicitudRepository;
        this.clienteRepository = clienteRepository;
        this.paqueteRepository = paqueteRepository;
    }

    public List<SolicitudCompra> listar() {
        return solicitudRepository.findAllByOrderByFechaDesc();
    }

    public SolicitudCompra buscar(Integer id) {
        return solicitudRepository.findById(id).orElseThrow();
    }

    public void registrar(String dni, Integer idPaquete) {
        Cliente cliente = clienteRepository.findByDni(dni).orElse(null);
        if (cliente == null || !cliente.isEstado()) {
            throw new ReglaNegocioException("No hay un cliente activo con ese DNI. Regístrate primero en recepción.");
        }
        Paquete paquete = paqueteRepository.findById(idPaquete).orElse(null);
        if (paquete == null || !paquete.isEstado()) {
            throw new ReglaNegocioException("El paquete seleccionado ya no está disponible.");
        }
        solicitudRepository.save(new SolicitudCompra(cliente, paquete));
    }

    public void rechazar(Integer id) {
        SolicitudCompra solicitud = buscar(id);
        if (!solicitud.getEstado().equals("PENDIENTE")) {
            throw new ReglaNegocioException("La solicitud ya fue atendida.");
        }
        solicitud.setEstado("RECHAZADA");
        solicitudRepository.save(solicitud);
    }
}

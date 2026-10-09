package com.gimnasio.service;

import com.gimnasio.model.Cobro;
import com.gimnasio.model.Paquete;
import com.gimnasio.model.SolicitudCompra;
import com.gimnasio.model.Suscripcion;
import com.gimnasio.repository.CobroRepository;
import com.gimnasio.repository.SolicitudCompraRepository;
import com.gimnasio.repository.SuscripcionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CobroService {

    private final CobroRepository cobroRepository;
    private final SuscripcionRepository suscripcionRepository;
    private final SolicitudCompraRepository solicitudRepository;

    public CobroService(CobroRepository cobroRepository, SuscripcionRepository suscripcionRepository,
                        SolicitudCompraRepository solicitudRepository) {
        this.cobroRepository = cobroRepository;
        this.suscripcionRepository = suscripcionRepository;
        this.solicitudRepository = solicitudRepository;
    }

    public List<Cobro> listar() {
        return cobroRepository.findAllByOrderByFechaDesc();
    }

    public Cobro buscar(Integer id) {
        return cobroRepository.findById(id).orElseThrow();
    }

    public LocalDate calcularVencimiento(LocalDate fechaInicio) {
        return fechaInicio.plusMonths(1);
    }

    @Transactional
    public Cobro registrarCobro(Integer idSolicitud, String metodoPago) {
        SolicitudCompra solicitud = solicitudRepository.findById(idSolicitud).orElseThrow();
        Paquete paquete = solicitud.getPaquete();
        if (!solicitud.getEstado().equals("PENDIENTE")) {
            throw new ReglaNegocioException("La solicitud ya fue atendida.");
        }
        if (!paquete.isEstado()) {
            throw new ReglaNegocioException("El paquete solicitado ya no está activo.");
        }

        // La suscripción se crea primero porque todo cobro debe quedar asociado a una (RN05).
        LocalDate hoy = LocalDate.now();
        Suscripcion suscripcion = new Suscripcion(solicitud.getCliente(), paquete, hoy, calcularVencimiento(hoy));
        suscripcionRepository.save(suscripcion);

        Cobro cobro = new Cobro(suscripcion, paquete.getPrecio(), metodoPago);
        cobroRepository.save(cobro);

        solicitud.setEstado("ATENDIDA");
        solicitudRepository.save(solicitud);
        return cobro;
    }
}

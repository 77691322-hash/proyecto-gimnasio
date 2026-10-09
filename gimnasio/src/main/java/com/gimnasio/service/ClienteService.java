package com.gimnasio.service;

import com.gimnasio.model.Cliente;
import com.gimnasio.model.Suscripcion;
import com.gimnasio.repository.ClienteRepository;
import com.gimnasio.repository.SuscripcionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final SuscripcionRepository suscripcionRepository;

    public ClienteService(ClienteRepository clienteRepository, SuscripcionRepository suscripcionRepository) {
        this.clienteRepository = clienteRepository;
        this.suscripcionRepository = suscripcionRepository;
    }

    public List<Cliente> buscarPorNombreODni(String texto) {
        return new ArrayList<>(clienteRepository.findByNombreContainingIgnoreCaseOrDniContaining(texto, texto));
    }

    public Cliente buscar(Integer id) {
        return clienteRepository.findById(id).orElseThrow();
    }

    public void guardar(Cliente cliente) {
        Cliente existente = clienteRepository.findByDni(cliente.getDni()).orElse(null);
        if (existente != null && !existente.getIdCliente().equals(cliente.getIdCliente())) {
            throw new ReglaNegocioException("Ya existe un cliente registrado con ese DNI.");
        }
        clienteRepository.save(cliente);
    }

    public void cambiarEstado(Integer id) {
        Cliente cliente = buscar(id);
        cliente.setEstado(!cliente.isEstado());
        clienteRepository.save(cliente);
    }

    public Map<Integer, Suscripcion> membresiasVigentes() {
        Map<Integer, Suscripcion> vigentes = new HashMap<>();
        for (Suscripcion suscripcion : suscripcionRepository.findByFechaFinGreaterThanEqual(LocalDate.now())) {
            vigentes.put(suscripcion.getCliente().getIdCliente(), suscripcion);
        }
        return vigentes;
    }
}

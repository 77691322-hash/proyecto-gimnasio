package com.gimnasio.service;

import com.gimnasio.model.Asistencia;
import com.gimnasio.model.Cobro;
import com.gimnasio.model.Suscripcion;
import com.gimnasio.repository.AsistenciaRepository;
import com.gimnasio.repository.ClienteRepository;
import com.gimnasio.repository.CobroRepository;
import com.gimnasio.repository.PaqueteRepository;
import com.gimnasio.repository.SuscripcionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class DashboardService {

    private final ClienteRepository clienteRepository;
    private final SuscripcionRepository suscripcionRepository;
    private final PaqueteRepository paqueteRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final CobroRepository cobroRepository;

    public DashboardService(ClienteRepository clienteRepository, SuscripcionRepository suscripcionRepository,
                            PaqueteRepository paqueteRepository, AsistenciaRepository asistenciaRepository,
                            CobroRepository cobroRepository) {
        this.clienteRepository = clienteRepository;
        this.suscripcionRepository = suscripcionRepository;
        this.paqueteRepository = paqueteRepository;
        this.asistenciaRepository = asistenciaRepository;
        this.cobroRepository = cobroRepository;
    }

    public long clientesActivos() {
        return clienteRepository.countByEstadoTrue();
    }

    public long suscripcionesVigentes() {
        return suscripcionRepository.countByFechaFinGreaterThanEqual(LocalDate.now());
    }

    public long suscripcionesPorVencer() {
        LocalDate hoy = LocalDate.now();
        return suscripcionRepository.countByFechaFinBetween(hoy, hoy.plusDays(7));
    }

    public long paquetesActivos() {
        return paqueteRepository.countByEstadoTrue();
    }

    public long personalPresente() {
        return asistenciaRepository.countByFechaAndHoraSalidaIsNull(LocalDate.now());
    }

    public List<Barra> ingresosPorDia(LocalDate desde, LocalDate hasta) {
        Map<String, BigDecimal> totales = new TreeMap<>();
        for (Cobro cobro : cobrosEntre(desde, hasta)) {
            totales.merge(cobro.getFecha().toLocalDate().toString(), cobro.getMonto(), BigDecimal::add);
        }
        return convertirABarras(totales);
    }

    public List<Barra> cobrosPorDia(LocalDate desde, LocalDate hasta) {
        Map<String, BigDecimal> totales = new TreeMap<>();
        for (Cobro cobro : cobrosEntre(desde, hasta)) {
            totales.merge(cobro.getFecha().toLocalDate().toString(), BigDecimal.ONE, BigDecimal::add);
        }
        return convertirABarras(totales);
    }

    public List<Barra> asistenciasPorDia(LocalDate desde, LocalDate hasta) {
        Map<String, BigDecimal> totales = new TreeMap<>();
        for (Asistencia asistencia : asistenciaRepository.findByFechaBetweenOrderByFechaDescHoraEntradaDesc(desde, hasta)) {
            totales.merge(asistencia.getFecha().toString(), BigDecimal.ONE, BigDecimal::add);
        }
        return convertirABarras(totales);
    }

    public List<Barra> ingresosPorMes() {
        LocalDate hoy = LocalDate.now();
        Map<String, BigDecimal> totales = new TreeMap<>();
        for (Cobro cobro : cobrosEntre(hoy.withDayOfYear(1), hoy)) {
            totales.merge(cobro.getFecha().toLocalDate().toString().substring(0, 7), cobro.getMonto(), BigDecimal::add);
        }
        return convertirABarras(totales);
    }

    public List<Barra> suscripcionesPorMes() {
        LocalDate hoy = LocalDate.now();
        Map<String, BigDecimal> totales = new TreeMap<>();
        for (Suscripcion suscripcion : suscripcionRepository.findByFechaInicioBetween(hoy.withDayOfYear(1), hoy)) {
            totales.merge(suscripcion.getFechaInicio().toString().substring(0, 7), BigDecimal.ONE, BigDecimal::add);
        }
        return convertirABarras(totales);
    }

    private List<Cobro> cobrosEntre(LocalDate desde, LocalDate hasta) {
        return cobroRepository.findByFechaBetween(desde.atStartOfDay(), hasta.plusDays(1).atStartOfDay());
    }

    private List<Barra> convertirABarras(Map<String, BigDecimal> totales) {
        BigDecimal maximo = BigDecimal.ONE;
        for (BigDecimal valor : totales.values()) {
            maximo = maximo.max(valor);
        }
        List<Barra> barras = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> total : totales.entrySet()) {
            int porcentaje = total.getValue().multiply(BigDecimal.valueOf(100)).divide(maximo, 0, RoundingMode.HALF_UP).intValue();
            barras.add(new Barra(total.getKey(), total.getValue(), porcentaje));
        }
        return barras;
    }
}

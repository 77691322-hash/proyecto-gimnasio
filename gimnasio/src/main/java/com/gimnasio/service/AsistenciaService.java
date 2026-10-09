package com.gimnasio.service;

import com.gimnasio.model.Asistencia;
import com.gimnasio.model.Personal;
import com.gimnasio.repository.AsistenciaRepository;
import com.gimnasio.repository.PersonalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final PersonalRepository personalRepository;

    public AsistenciaService(AsistenciaRepository asistenciaRepository, PersonalRepository personalRepository) {
        this.asistenciaRepository = asistenciaRepository;
        this.personalRepository = personalRepository;
    }

    public Asistencia marcar(String dni) {
        Personal personal = personalRepository.findByDniAndEstadoTrue(dni).orElse(null);
        if (personal == null) {
            throw new ReglaNegocioException("El DNI no corresponde a un trabajador activo.");
        }

        // Si hoy tiene una entrada sin salida se registra la salida; si no, una nueva entrada.
        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now().withNano(0);
        Asistencia asistencia = asistenciaRepository.findFirstByPersonalAndFechaAndHoraSalidaIsNull(personal, hoy).orElse(null);
        if (asistencia == null) {
            asistencia = new Asistencia(personal, hoy, ahora);
        } else {
            asistencia.setHoraSalida(ahora);
        }
        return asistenciaRepository.save(asistencia);
    }

    public List<Asistencia> historial(LocalDate desde, LocalDate hasta, Integer idPersonal) {
        if (idPersonal == null) {
            return asistenciaRepository.findByFechaBetweenOrderByFechaDescHoraEntradaDesc(desde, hasta);
        }
        return asistenciaRepository.findByPersonalIdPersonalAndFechaBetweenOrderByFechaDescHoraEntradaDesc(idPersonal, desde, hasta);
    }
}

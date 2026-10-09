package com.gimnasio.service;

import com.gimnasio.model.Personal;
import com.gimnasio.repository.PersonalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonalService {

    private final PersonalRepository personalRepository;

    public PersonalService(PersonalRepository personalRepository) {
        this.personalRepository = personalRepository;
    }

    public List<Personal> listar() {
        return personalRepository.findAll();
    }

    public Personal buscar(Integer id) {
        return personalRepository.findById(id).orElseThrow();
    }

    public void guardar(Personal personal) {
        Personal existente = personalRepository.findByDni(personal.getDni()).orElse(null);
        if (existente != null && !existente.getIdPersonal().equals(personal.getIdPersonal())) {
            throw new ReglaNegocioException("Ya existe un trabajador registrado con ese DNI.");
        }
        personalRepository.save(personal);
    }

    public void cambiarEstado(Integer id) {
        Personal personal = buscar(id);
        personal.setEstado(!personal.isEstado());
        personalRepository.save(personal);
    }
}

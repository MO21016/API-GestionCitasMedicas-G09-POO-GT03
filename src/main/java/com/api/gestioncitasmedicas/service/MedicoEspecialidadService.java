package com.api.gestioncitasmedicas.service;

import com.api.gestioncitasmedicas.dto.AsignarEspecialidadDTO;
import com.api.gestioncitasmedicas.entity.MedicoEspecialidad;
import com.api.gestioncitasmedicas.repository.EspecialidadRepository;
import com.api.gestioncitasmedicas.repository.MedicoEspecialidadRepository;
import com.api.gestioncitasmedicas.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MedicoEspecialidadService {

    private final MedicoRepository medicoRepository;
    private final EspecialidadRepository especialidadRepository;
    private final MedicoEspecialidadRepository medicoEspecialidadRepository;

    // Asignar una especialidad a un médico
    @Transactional
    public void asignarEspecialidadAMedico(AsignarEspecialidadDTO dto) {
        // Validar que el médico exista
        if (!medicoRepository.existsById(dto.getIdMedico())) {
            throw new RuntimeException("Médico no encontrado con ID: " + dto.getIdMedico());
        }

        // Validar que la especialidad exista
        if (!especialidadRepository.existsById(dto.getIdEspecialidad())) {
            throw new RuntimeException("Especialidad no encontrada con ID: " + dto.getIdEspecialidad());
        }

        // Validar que la relación no exista ya
        if (medicoEspecialidadRepository.existsByIdMedicoAndIdEspecialidad(
                dto.getIdMedico(), dto.getIdEspecialidad())) {
            throw new RuntimeException("El médico ya tiene asignada esta especialidad");
        }

        // Crear la relación
        MedicoEspecialidad relacion = new MedicoEspecialidad();
        relacion.setIdMedico(dto.getIdMedico());
        relacion.setIdEspecialidad(dto.getIdEspecialidad());

        medicoEspecialidadRepository.save(relacion);
    }

    // Desasignar una especialidad de un médico
    @Transactional
    public void desasignarEspecialidadDeMedico(Long idMedico, Long idEspecialidad) {
        // Validar que la relación exista
        if (!medicoEspecialidadRepository.existsByIdMedicoAndIdEspecialidad(idMedico, idEspecialidad)) {
            throw new RuntimeException("La relación médico-especialidad no existe");
        }

        medicoEspecialidadRepository.deleteByIdMedicoAndIdEspecialidad(idMedico, idEspecialidad);
    }
}
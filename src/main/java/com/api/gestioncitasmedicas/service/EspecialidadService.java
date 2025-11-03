package com.api.gestioncitasmedicas.service;

import com.api.gestioncitasmedicas.dto.ActualizarEspecialidadDTO;
import com.api.gestioncitasmedicas.dto.CrearEspecialidadDTO;
import com.api.gestioncitasmedicas.dto.EspecialidadDTO;
import com.api.gestioncitasmedicas.entity.Especialidad;
import com.api.gestioncitasmedicas.repository.EspecialidadRepository;
import com.api.gestioncitasmedicas.repository.MedicoEspecialidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EspecialidadService {

    private final EspecialidadRepository especialidadRepository;
    private final MedicoEspecialidadRepository medicoEspecialidadRepository;

    // Listar todas las especialidades
    public List<EspecialidadDTO> obtenerTodas() {
        return especialidadRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener una especialidad por ID
    public EspecialidadDTO obtenerPorId(Long id) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con ID: " + id));
        return convertirADTO(especialidad);
    }

    // Crear una nueva especialidad
    public EspecialidadDTO crear(CrearEspecialidadDTO dto) {
        // Validar que no exista una especialidad con el mismo nombre
        if (especialidadRepository.existsByNombreEspecialidad(dto.getNombreEspecialidad())) {
            throw new RuntimeException("Ya existe una especialidad con el nombre: " + dto.getNombreEspecialidad());
        }

        Especialidad especialidad = new Especialidad();
        especialidad.setNombreEspecialidad(dto.getNombreEspecialidad());
        especialidad.setDescripcion(dto.getDescripcion());

        Especialidad guardada = especialidadRepository.save(especialidad);
        return convertirADTO(guardada);
    }

    // Actualizar una especialidad
    public EspecialidadDTO actualizar(Long id, ActualizarEspecialidadDTO dto) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con ID: " + id));

        // Actualizar solo los campos que vienen (no nulos)
        if (dto.getNombreEspecialidad() != null && !dto.getNombreEspecialidad().isEmpty()) {
            // Validar que el nuevo nombre no esté en uso por otra especialidad
            if (especialidadRepository.existsByNombreEspecialidad(dto.getNombreEspecialidad()) &&
                    !especialidad.getNombreEspecialidad().equals(dto.getNombreEspecialidad())) {
                throw new RuntimeException("Ya existe una especialidad con el nombre: " + dto.getNombreEspecialidad());
            }
            especialidad.setNombreEspecialidad(dto.getNombreEspecialidad());
        }

        if (dto.getDescripcion() != null) {
            especialidad.setDescripcion(dto.getDescripcion());
        }

        Especialidad actualizada = especialidadRepository.save(especialidad);
        return convertirADTO(actualizada);
    }

    // Eliminar una especialidad
    public void eliminar(Long id) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con ID: " + id));

        // Verificar que no tenga médicos asociados
        long medicosAsociados = medicoEspecialidadRepository.findByIdEspecialidad(id).size();
        if (medicosAsociados > 0) {
            throw new RuntimeException(
                    "No se puede eliminar la especialidad porque tiene " + medicosAsociados + " médicos asociados"
            );
        }

        especialidadRepository.delete(especialidad);
    }

    // Método privado: convertir Entity a DTO
    private EspecialidadDTO convertirADTO(Especialidad especialidad) {
        EspecialidadDTO dto = new EspecialidadDTO();
        dto.setIdEspecialidad(especialidad.getIdEspecialidad());
        dto.setNombreEspecialidad(especialidad.getNombreEspecialidad());
        dto.setDescripcion(especialidad.getDescripcion());

        // Calcular cantidad de médicos
        int cantidadMedicos = medicoEspecialidadRepository
                .findByIdEspecialidad(especialidad.getIdEspecialidad())
                .size();
        dto.setCantidadMedicos(cantidadMedicos);

        return dto;
    }
}
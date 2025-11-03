package com.api.gestioncitasmedicas.service;

import com.api.gestioncitasmedicas.dto.CrearEspecialidadDTO;
import com.api.gestioncitasmedicas.dto.ActualizarEspecialidadDTO;
import com.api.gestioncitasmedicas.entity.Especialidad;
import com.api.gestioncitasmedicas.repository.EspecialidadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EspecialidadService {

    private final EspecialidadRepository especialidadRepository;

    public EspecialidadService(EspecialidadRepository especialidadRepository) {
        this.especialidadRepository = especialidadRepository;
    }

    // ✅ 1. Listar todas las especialidades
    public List<Especialidad> listarTodas() {
        return especialidadRepository.findAll();
    }

    // ✅ 2. Crear nueva especialidad usando DTO
    public Especialidad crearEspecialidad(CrearEspecialidadDTO dto) {
        // Validar si el nombre ya existe
        if (especialidadRepository.existsByNombreEspecialidad(dto.getNombreEspecialidad())) {
            throw new RuntimeException("Ya existe una especialidad con ese nombre");
        }

        Especialidad nueva = new Especialidad();
        nueva.setNombreEspecialidad(dto.getNombreEspecialidad());
        nueva.setDescripcion(dto.getDescripcion());

        return especialidadRepository.save(nueva);
    }

    // ✅ 3. Actualizar una especialidad existente usando DTO
    public Especialidad actualizarEspecialidad(Long id, ActualizarEspecialidadDTO dto) {
        Optional<Especialidad> optionalEspecialidad = especialidadRepository.findById(id);
        if (optionalEspecialidad.isEmpty()) {
            throw new RuntimeException("Especialidad no encontrada con ID: " + id);
        }

        Especialidad existente = optionalEspecialidad.get();

        // Validar si el nuevo nombre ya pertenece a otra especialidad
        if (!existente.getNombreEspecialidad().equalsIgnoreCase(dto.getNombreEspecialidad())
                && especialidadRepository.existsByNombreEspecialidad(dto.getNombreEspecialidad())) {
            throw new RuntimeException("Ya existe otra especialidad con ese nombre");
        }

        existente.setNombreEspecialidad(dto.getNombreEspecialidad());
        existente.setDescripcion(dto.getDescripcion());

        return especialidadRepository.save(existente);
    }

    // ✅ 4. Eliminar una especialidad por ID
    public void eliminarEspecialidad(Long id) {
        if (!especialidadRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: la especialidad no existe con ID " + id);
        }
        especialidadRepository.deleteById(id);
    }

    // ✅ 5. Buscar por ID (opcional, útil para validaciones)
    public Especialidad obtenerPorId(Long id) {
        return especialidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con ID: " + id));
    }
}

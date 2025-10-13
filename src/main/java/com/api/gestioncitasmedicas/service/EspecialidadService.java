package com.api.gestioncitasmedicas.service;

import com.api.gestioncitasmedicas.entity.Especialidad;
import com.api.gestioncitasmedicas.repository.EspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecialidadService {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    // ✅ Listar todas las especialidades
    public List<Especialidad> listarTodas() {
        return especialidadRepository.findAll();
    }

    // ✅ Buscar una especialidad por ID
    public Especialidad obtenerPorId(Long id) {
        // 🔧 Aquí estaba el error
        return especialidadRepository.findById(id).orElse(null);
    }

    // ✅ Buscar una especialidad por nombre
    public Especialidad obtenerPorNombre(String nombre) {
        return especialidadRepository.findByNombreEspecialidad(nombre).orElse(null);
    }

    // ✅ Guardar o actualizar una especialidad
    public Especialidad guardar(Especialidad especialidad) {
        return especialidadRepository.save(especialidad);
    }

    public Especialidad actualizarEspecialidad(Long id, Especialidad especialidadActualizada) {
        Especialidad existente = especialidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

        // Actualizar los campos
        existente.setNombreEspecialidad(especialidadActualizada.getNombreEspecialidad());
        existente.setDescripcion(especialidadActualizada.getDescripcion());

        return especialidadRepository.save(existente);
    }


    // ✅ Eliminar especialidad por ID
    public void eliminarEspecialidad(Long id) {
        Especialidad existente = especialidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con ID: " + id));
        especialidadRepository.delete(existente);
    }
}
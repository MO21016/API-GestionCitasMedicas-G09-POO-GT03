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

    // ✅ Eliminar especialidad por ID
    public void eliminar(Long id) {
        especialidadRepository.deleteById(id);
    }

    // Insertar nueva especialidad (verifica duplicados)
    public Especialidad insertarEspecialidad(Especialidad especialidad) {
        if (especialidadRepository.findByNombreEspecialidad(especialidad.getNombreEspecialidad()).isPresent()) {
            throw new RuntimeException("Ya existe una especialidad con ese nombre");
        }
        return especialidadRepository.save(especialidad);
    }

}
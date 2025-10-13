package com.api.gestioncitasmedicas.repository;

import com.api.gestioncitasmedicas.entity.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {

    // Buscar por nombre
    Optional<Especialidad> findByNombreEspecialidad(String nombreEspecialidad);

    // Verificar si existe por nombre
    boolean existsByNombreEspecialidad(String nombreEspecialidad);
}
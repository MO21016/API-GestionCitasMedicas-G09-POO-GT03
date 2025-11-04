package com.api.gestioncitasmedicas.repository;

import com.api.gestioncitasmedicas.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    // Buscar por nombre o apellido (búsqueda parcial, case insensitive)
    List<Paciente> findByNombrePacienteContainingIgnoreCaseOrApellidoPacienteContainingIgnoreCase(
            String nombre, String apellido
    );

    // Verificar si existe un paciente con ese correo
    boolean existsByCorreoPaciente(String correoPaciente);
}
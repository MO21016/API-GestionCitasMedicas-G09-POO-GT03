package com.api.gestioncitasmedicas.repository;

import com.api.gestioncitasmedicas.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    // Buscar por correo
    Optional<Paciente> findByCorreoPaciente(String correoPaciente);

    // Verificar si existe por correo
    boolean existsByCorreoPaciente(String correoPaciente);

    // Buscar por nombre o apellido (búsqueda parcial)
    List<Paciente> findByNombrePacienteContainingIgnoreCaseOrApellidoPacienteContainingIgnoreCase(
            String nombre, String apellido
    );
}
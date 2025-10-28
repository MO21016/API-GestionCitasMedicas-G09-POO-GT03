package com.api.gestioncitasmedicas.repository;

import com.api.gestioncitasmedicas.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    // ✅ Verificar si existe un médico con un correo específico
    boolean existsByCorreoMedico(String correoMedico);

    // ✅ Buscar médico por correo (útil para validaciones o autenticación)
    Optional<Medico> findByCorreoMedico(String correoMedico);

    // ✅ Buscar médicos por el nombre de una especialidad
    List<Medico> findByEspecialidades_NombreEspecialidad(String nombreEspecialidad);
}

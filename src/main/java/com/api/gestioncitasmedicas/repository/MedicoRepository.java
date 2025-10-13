package com.api.gestioncitasmedicas.repository;

import com.api.gestioncitasmedicas.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    // Buscar por correo
    Optional<Medico> findByCorreoMedico(String correoMedico);

    // Verificar si existe por correo
    boolean existsByCorreoMedico(String correoMedico);

    // Buscar médicos por especialidad
    @Query("SELECT m FROM Medico m JOIN m.especialidades e WHERE e.idEspecialidad = :idEspecialidad")
    List<Medico> findByEspecialidadId(@Param("idEspecialidad") Long idEspecialidad);
}
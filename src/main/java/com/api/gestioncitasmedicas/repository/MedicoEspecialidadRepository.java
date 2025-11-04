package com.api.gestioncitasmedicas.repository;

import com.api.gestioncitasmedicas.entity.MedicoEspecialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MedicoEspecialidadRepository extends JpaRepository<MedicoEspecialidad, Long> {

    // Verificar si existe la relación
    boolean existsByIdMedicoAndIdEspecialidad(Long idMedico, Long idEspecialidad);

    // Eliminar relación específica
    @Transactional
    void deleteByIdMedicoAndIdEspecialidad(Long idMedico, Long idEspecialidad);

    // Obtener todas las especialidades de un médico
    List<MedicoEspecialidad> findByIdMedico(Long idMedico);

    // Obtener todos los médicos de una especialidad
    List<MedicoEspecialidad> findByIdEspecialidad(Long idEspecialidad);

    // Eliminar todas las especialidades de un médico (para cuando se elimina el médico)
    @Transactional
    void deleteAllByIdMedico(Long idMedico);
}
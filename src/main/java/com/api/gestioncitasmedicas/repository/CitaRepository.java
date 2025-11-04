package com.api.gestioncitasmedicas.repository;

import com.api.gestioncitasmedicas.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    // Buscar citas por paciente
    List<Cita> findByPacienteIdPaciente(Long idPaciente);

    // Buscar citas por médico
    List<Cita> findByMedicoIdMedico(Long idMedico);

    // Buscar citas por estado
    List<Cita> findByEstadoCita(Cita.EstadoCita estadoCita);

    // Buscar citas por fecha
    List<Cita> findByFechaCita(LocalDate fechaCita);

    // CRÍTICO: Verificar disponibilidad del médico
    @Query("SELECT c FROM Cita c WHERE c.medico.idMedico = :idMedico " +
            "AND c.fechaCita = :fecha AND c.horaCita = :hora " +
            "AND c.estadoCita != 'CANCELADA'")
    Optional<Cita> findByMedicoAndFechaAndHora(
            @Param("idMedico") Long idMedico,
            @Param("fecha") LocalDate fecha,
            @Param("hora") LocalTime hora
    );

    // Buscar citas en un rango de fechas
    List<Cita> findByFechaCitaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    // Contar citas de un médico (para validación al eliminar)
    long countByMedicoIdMedico(Long idMedico);
}
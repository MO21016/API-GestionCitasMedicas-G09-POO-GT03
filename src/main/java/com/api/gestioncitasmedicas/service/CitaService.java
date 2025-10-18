package com.api.gestioncitasmedicas.service;

import com.api.gestioncitasmedicas.dto.CitaRequest;
import com.api.gestioncitasmedicas.dto.CitaResponse;
import com.api.gestioncitasmedicas.entity.*;
import com.api.gestioncitasmedicas.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final EspecialidadRepository especialidadRepository;

    // ✅ Obtener todas las citas
    public List<CitaResponse> getAllCitas() {
        return citaRepository.findAll().stream()
                .map(CitaResponse::new)
                .collect(Collectors.toList());
    }

    // ✅ Obtener cita por ID
    public Optional<CitaResponse> getCitaById(Long id) {
        return citaRepository.findById(id)
                .map(CitaResponse::new);
    }

    // ✅ CREAR CITA CON TODAS LAS VALIDACIONES
    @Transactional
    public CitaResponse createCita(CitaRequest citaRequest) {
        // Validar que no sea fin de semana
        validarDiaHabil(citaRequest.getFechaCita());

        // Validar horario laboral (8:00 - 18:00)
        validarHorarioLaboral(citaRequest.getHoraCita());

        // Validar que la cita no sea en el pasado
        validarFechaFutura(citaRequest.getFechaCita(), citaRequest.getHoraCita());

        // Validar entidades existentes
        Paciente paciente = validarPacienteExistente(citaRequest.getIdPaciente());
        Medico medico = validarMedicoExistente(citaRequest.getIdMedico());
        Especialidad especialidad = validarEspecialidadExistente(citaRequest.getIdEspecialidad());

        // Validar que el médico tenga la especialidad
        validarMedicoEspecialidad(medico, especialidad);

        // Validar disponibilidad del médico
        validarDisponibilidadMedico(medico.getIdMedico(), citaRequest.getFechaCita(), citaRequest.getHoraCita());

        // Crear la cita
        Cita cita = new Cita();
        cita.setFechaCita(citaRequest.getFechaCita());
        cita.setHoraCita(citaRequest.getHoraCita());
        cita.setMotivoCita(citaRequest.getMotivoCita());
        cita.setEstadoCita(Cita.EstadoCita.PENDIENTE);
        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setEspecialidad(especialidad);

        Cita citaGuardada = citaRepository.save(cita);
        return new CitaResponse(citaGuardada);
    }

    // ✅ VALIDACION: Días hábiles (lunes a viernes)
    private void validarDiaHabil(LocalDate fecha) {
        if (fecha.getDayOfWeek() == DayOfWeek.SATURDAY || fecha.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new RuntimeException("No se pueden agendar citas los fines de semana");
        }
    }

    // ✅ VALIDACION: Horario laboral (8:00 - 18:00)
    private void validarHorarioLaboral(LocalTime hora) {
        LocalTime horaInicio = LocalTime.of(8, 0);
        LocalTime horaFin = LocalTime.of(18, 0);

        if (hora.isBefore(horaInicio) || hora.isAfter(horaFin)) {
            throw new RuntimeException("El horario debe ser entre 8:00 y 18:00 horas");
        }
    }

    // ✅ VALIDACION: Fecha futura
    private void validarFechaFutura(LocalDate fecha, LocalTime hora) {
        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        if (fecha.isBefore(hoy) || (fecha.equals(hoy) && hora.isBefore(ahora))) {
            throw new RuntimeException("No se pueden programar citas en fechas/horas pasadas");
        }
    }

    // ✅ VALIDACION: Paciente existe
    private Paciente validarPacienteExistente(Long idPaciente) {
        return pacienteRepository.findById(idPaciente)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + idPaciente));
    }

    // ✅ VALIDACION: Médico existe
    private Medico validarMedicoExistente(Long idMedico) {
        return medicoRepository.findById(idMedico)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado con ID: " + idMedico));
    }

    // ✅ VALIDACION: Especialidad existe
    private Especialidad validarEspecialidadExistente(Long idEspecialidad) {
        return especialidadRepository.findById(idEspecialidad)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con ID: " + idEspecialidad));
    }

    // ✅ VALIDACION: Médico tiene la especialidad
    private void validarMedicoEspecialidad(Medico medico, Especialidad especialidad) {
        boolean tieneEspecialidad = medico.getEspecialidades().stream()
                .anyMatch(esp -> esp.getIdEspecialidad().equals(especialidad.getIdEspecialidad()));

        if (!tieneEspecialidad) {
            throw new RuntimeException("El médico no tiene la especialidad seleccionada");
        }
    }

    // ✅ VALIDACION: Disponibilidad del médico
    private void validarDisponibilidadMedico(Long idMedico, LocalDate fecha, LocalTime hora) {
        Optional<Cita> citaExistente = citaRepository.findByMedicoAndFechaAndHora(idMedico, fecha, hora);

        if (citaExistente.isPresent()) {
            throw new RuntimeException("El médico ya tiene una cita programada para " + fecha + " a las " + hora);
        }
    }

    // ✅ Actualizar estado de cita
    @Transactional
    public CitaResponse updateEstadoCita(Long id, Cita.EstadoCita nuevoEstado) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + id));

        cita.setEstadoCita(nuevoEstado);
        Cita citaActualizada = citaRepository.save(cita);
        return new CitaResponse(citaActualizada);
    }

    // ✅ Cancelar cita
    @Transactional
    public CitaResponse cancelarCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + id));

        // Validar que no se cancele una cita completada
        if (cita.getEstadoCita() == Cita.EstadoCita.COMPLETADA) {
            throw new RuntimeException("No se puede cancelar una cita ya completada");
        }

        cita.setEstadoCita(Cita.EstadoCita.CANCELADA);
        Cita citaCancelada = citaRepository.save(cita);
        return new CitaResponse(citaCancelada);
    }

    // ✅ Obtener citas por paciente
    public List<CitaResponse> getCitasByPaciente(Long idPaciente) {
        return citaRepository.findByPacienteIdPaciente(idPaciente).stream()
                .map(CitaResponse::new)
                .collect(Collectors.toList());
    }

    // ✅ Obtener citas por médico
    public List<CitaResponse> getCitasByMedico(Long idMedico) {
        return citaRepository.findByMedicoIdMedico(idMedico).stream()
                .map(CitaResponse::new)
                .collect(Collectors.toList());
    }

    // ✅ Obtener citas por estado
    public List<CitaResponse> getCitasByEstado(Cita.EstadoCita estado) {
        return citaRepository.findByEstadoCita(estado).stream()
                .map(CitaResponse::new)
                .collect(Collectors.toList());
    }

    // ✅ Verificar disponibilidad
    public boolean verificarDisponibilidad(Long idMedico, LocalDate fecha, LocalTime hora) {
        // Validar día hábil y horario laboral primero
        try {
            validarDiaHabil(fecha);
            validarHorarioLaboral(hora);
            validarFechaFutura(fecha, hora);
        } catch (RuntimeException e) {
            return false;
        }

        return citaRepository.findByMedicoAndFechaAndHora(idMedico, fecha, hora).isEmpty();
    }
}
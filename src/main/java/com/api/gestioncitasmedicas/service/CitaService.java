package com.api.gestioncitasmedicas.service;

import com.api.gestioncitasmedicas.dto.ActualizarCitaDTO;
import com.api.gestioncitasmedicas.dto.CitaDTO;
import com.api.gestioncitasmedicas.dto.CrearCitaDTO;
import com.api.gestioncitasmedicas.entity.Cita;
import com.api.gestioncitasmedicas.entity.Especialidad;
import com.api.gestioncitasmedicas.entity.Medico;
import com.api.gestioncitasmedicas.entity.Paciente;
import com.api.gestioncitasmedicas.repository.CitaRepository;
import com.api.gestioncitasmedicas.repository.EspecialidadRepository;
import com.api.gestioncitasmedicas.repository.MedicoEspecialidadRepository;
import com.api.gestioncitasmedicas.repository.MedicoRepository;
import com.api.gestioncitasmedicas.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final EspecialidadRepository especialidadRepository;
    private final MedicoEspecialidadRepository medicoEspecialidadRepository;

    // Horarios válidos (8 slots por día)
    private static final List<LocalTime> HORARIOS_VALIDOS = Arrays.asList(
            LocalTime.of(8, 0),   // 08:00
            LocalTime.of(9, 0),   // 09:00
            LocalTime.of(10, 0),  // 10:00
            LocalTime.of(11, 0),  // 11:00
            LocalTime.of(13, 0),  // 13:00 (después del almuerzo)
            LocalTime.of(14, 0),  // 14:00
            LocalTime.of(15, 0),  // 15:00
            LocalTime.of(16, 0)   // 16:00
    );

    // Listar todas las citas
    public List<CitaDTO> obtenerTodas() {
        return citaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener una cita por ID
    public CitaDTO obtenerPorId(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + id));
        return convertirADTO(cita);
    }

    // Filtrar citas por médico, paciente o estado
    public List<CitaDTO> filtrar(Long idMedico, Long idPaciente, String estado) {
        List<Cita> citas;

        if (idMedico != null) {
            citas = citaRepository.findByMedicoIdMedico(idMedico);
        } else if (idPaciente != null) {
            citas = citaRepository.findByPacienteIdPaciente(idPaciente);
        } else if (estado != null) {
            Cita.EstadoCita estadoCita = Cita.EstadoCita.valueOf(estado.toUpperCase());
            citas = citaRepository.findByEstadoCita(estadoCita);
        } else {
            citas = citaRepository.findAll();
        }

        return citas.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Crear una nueva cita (CON TODAS LAS VALIDACIONES)
    public CitaDTO crear(CrearCitaDTO dto) {
        // VALIDACIÓN 1: Verificar que el paciente existe
        Paciente paciente = pacienteRepository.findById(dto.getIdPaciente())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + dto.getIdPaciente()));

        // VALIDACIÓN 2: Verificar que el médico existe
        Medico medico = medicoRepository.findById(dto.getIdMedico())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado con ID: " + dto.getIdMedico()));

        // VALIDACIÓN 3: Verificar que la especialidad existe
        Especialidad especialidad = especialidadRepository.findById(dto.getIdEspecialidad())
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con ID: " + dto.getIdEspecialidad()));

        // VALIDACIÓN 4: Verificar que el médico tiene esa especialidad
        boolean medicoTieneEspecialidad = medicoEspecialidadRepository.existsByIdMedicoAndIdEspecialidad(
                dto.getIdMedico(), dto.getIdEspecialidad()
        );
        if (!medicoTieneEspecialidad) {
            throw new RuntimeException(
                    "El médico " + medico.getNombreMedico() + " " + medico.getApellidoMedico() +
                            " no tiene la especialidad de " + especialidad.getNombreEspecialidad()
            );
        }

        // VALIDACIÓN 5: Verificar que la fecha sea día hábil (lunes a viernes)
        if (!esDiaHabil(dto.getFechaCita())) {
            throw new RuntimeException("No se pueden agendar citas los fines de semana (sábado o domingo)");
        }

        // VALIDACIÓN 6: Verificar que la hora sea válida
        if (!esHorarioValido(dto.getHoraCita())) {
            throw new RuntimeException(
                    "Horario no válido. Los horarios disponibles son: 08:00, 09:00, 10:00, 11:00, 13:00, 14:00, 15:00, 16:00. " +
                            "No hay citas de 12:00 a 13:00 (hora de almuerzo)."
            );
        }

        // VALIDACIÓN 7: Verificar que el médico esté disponible (no tenga otra cita a esa hora)
        boolean medicoDisponible = citaRepository.findByMedicoAndFechaAndHora(
                dto.getIdMedico(), dto.getFechaCita(), dto.getHoraCita()
        ).isEmpty();

        if (!medicoDisponible) {
            throw new RuntimeException(
                    "El médico ya tiene una cita agendada el " + dto.getFechaCita() +
                            " a las " + dto.getHoraCita() + ". Por favor, elija otro horario."
            );
        }

        // Si todas las validaciones pasan, crear la cita
        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setEspecialidad(especialidad);
        cita.setFechaCita(dto.getFechaCita());
        cita.setHoraCita(dto.getHoraCita());
        cita.setMotivoCita(dto.getMotivoCita());
        cita.setEstadoCita(Cita.EstadoCita.PENDIENTE); // Por defecto es PENDIENTE

        Cita guardada = citaRepository.save(cita);
        return convertirADTO(guardada);
    }

    // Actualizar una cita
    public CitaDTO actualizar(Long id, ActualizarCitaDTO dto) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + id));

        // Solo se puede actualizar si NO está completada o cancelada
        if (cita.getEstadoCita() == Cita.EstadoCita.COMPLETADA ||
                cita.getEstadoCita() == Cita.EstadoCita.CANCELADA) {
            throw new RuntimeException(
                    "No se puede actualizar una cita que ya está " + cita.getEstadoCita()
            );
        }

        // Actualizar solo los campos que vienen
        if (dto.getFechaCita() != null) {
            // Validar día hábil
            if (!esDiaHabil(dto.getFechaCita())) {
                throw new RuntimeException("No se pueden agendar citas los fines de semana");
            }
            cita.setFechaCita(dto.getFechaCita());
        }

        if (dto.getHoraCita() != null) {
            // Validar horario válido
            if (!esHorarioValido(dto.getHoraCita())) {
                throw new RuntimeException("Horario no válido");
            }

            // Validar disponibilidad del médico
            boolean medicoDisponible = citaRepository.findByMedicoAndFechaAndHora(
                    cita.getMedico().getIdMedico(),
                    dto.getFechaCita() != null ? dto.getFechaCita() : cita.getFechaCita(),
                    dto.getHoraCita()
            ).isEmpty();

            if (!medicoDisponible) {
                throw new RuntimeException("El médico ya tiene una cita a esa hora");
            }

            cita.setHoraCita(dto.getHoraCita());
        }

        if (dto.getMotivoCita() != null && !dto.getMotivoCita().isEmpty()) {
            cita.setMotivoCita(dto.getMotivoCita());
        }

        Cita actualizada = citaRepository.save(cita);
        return convertirADTO(actualizada);
    }

    // Cambiar el estado de una cita
    public CitaDTO cambiarEstado(Long id, String nuevoEstado) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + id));

        Cita.EstadoCita estadoActual = cita.getEstadoCita();
        Cita.EstadoCita estadoNuevo;

        try {
            estadoNuevo = Cita.EstadoCita.valueOf(nuevoEstado.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado inválido: " + nuevoEstado);
        }

        // Validar transiciones válidas
        if (!esTransicionValida(estadoActual, estadoNuevo)) {
            throw new RuntimeException(
                    "No se puede cambiar el estado de " + estadoActual + " a " + estadoNuevo
            );
        }

        cita.setEstadoCita(estadoNuevo);
        Cita actualizada = citaRepository.save(cita);
        return convertirADTO(actualizada);
    }

    // Eliminar una cita (solo si está cancelada o pendiente)
    public void eliminar(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + id));

        // Solo se puede eliminar si está PENDIENTE o CANCELADA
        if (cita.getEstadoCita() == Cita.EstadoCita.COMPLETADA) {
            throw new RuntimeException("No se puede eliminar una cita que ya fue completada");
        }

        citaRepository.delete(cita);
    }

    // ========== MÉTODOS PRIVADOS DE VALIDACIÓN ==========

    // Verificar si es día hábil (lunes a viernes)
    private boolean esDiaHabil(LocalDate fecha) {
        DayOfWeek dia = fecha.getDayOfWeek();
        return dia != DayOfWeek.SATURDAY && dia != DayOfWeek.SUNDAY;
    }

    // Verificar si el horario es válido
    private boolean esHorarioValido(LocalTime hora) {
        return HORARIOS_VALIDOS.contains(hora);
    }

    // Verificar si la transición de estado es válida
    private boolean esTransicionValida(Cita.EstadoCita actual, Cita.EstadoCita nuevo) {
        switch (actual) {
            case PENDIENTE:
                return nuevo == Cita.EstadoCita.CONFIRMADA || nuevo == Cita.EstadoCita.CANCELADA;
            case CONFIRMADA:
                return nuevo == Cita.EstadoCita.COMPLETADA || nuevo == Cita.EstadoCita.CANCELADA;
            case COMPLETADA:
            case CANCELADA:
                return false; // No se puede cambiar desde estos estados
            default:
                return false;
        }
    }

    // Convertir Entity a DTO
    private CitaDTO convertirADTO(Cita cita) {
        CitaDTO dto = new CitaDTO();
        dto.setIdCita(cita.getIdCita());
        dto.setFechaCita(cita.getFechaCita());
        dto.setHoraCita(cita.getHoraCita());
        dto.setMotivoCita(cita.getMotivoCita());
        dto.setEstadoCita(cita.getEstadoCita().toString());

        // Información del paciente
        CitaDTO.PacienteInfoDTO pacienteInfo = new CitaDTO.PacienteInfoDTO();
        pacienteInfo.setIdPaciente(cita.getPaciente().getIdPaciente());
        pacienteInfo.setNombreCompleto(
                cita.getPaciente().getNombrePaciente() + " " + cita.getPaciente().getApellidoPaciente()
        );
        dto.setPaciente(pacienteInfo);

        // Información del médico
        CitaDTO.MedicoInfoDTO medicoInfo = new CitaDTO.MedicoInfoDTO();
        medicoInfo.setIdMedico(cita.getMedico().getIdMedico());
        medicoInfo.setNombreCompleto(
                "Dr. " + cita.getMedico().getNombreMedico() + " " + cita.getMedico().getApellidoMedico()
        );
        dto.setMedico(medicoInfo);

        // Información de la especialidad
        CitaDTO.EspecialidadInfoDTO especialidadInfo = new CitaDTO.EspecialidadInfoDTO();
        especialidadInfo.setIdEspecialidad(cita.getEspecialidad().getIdEspecialidad());
        especialidadInfo.setNombreEspecialidad(cita.getEspecialidad().getNombreEspecialidad());
        dto.setEspecialidad(especialidadInfo);

        return dto;
    }
}
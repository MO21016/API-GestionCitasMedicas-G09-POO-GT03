package com.api.gestioncitasmedicas.service;

import com.api.gestioncitasmedicas.dto.ActualizarPacienteDTO;
import com.api.gestioncitasmedicas.dto.CrearPacienteDTO;
import com.api.gestioncitasmedicas.dto.PacienteDTO;
import com.api.gestioncitasmedicas.entity.Paciente;
import com.api.gestioncitasmedicas.repository.CitaRepository;
import com.api.gestioncitasmedicas.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final CitaRepository citaRepository;

    // Listar todos los pacientes
    public List<PacienteDTO> obtenerTodos() {
        return pacienteRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener un paciente por ID
    public PacienteDTO obtenerPorId(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));
        return convertirADTO(paciente);
    }

    // Buscar pacientes por nombre o apellido
    public List<PacienteDTO> buscarPorNombre(String termino) {
        List<Paciente> pacientes = pacienteRepository.findByNombrePacienteContainingIgnoreCaseOrApellidoPacienteContainingIgnoreCase(
                termino, termino
        );
        return pacientes.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Crear un nuevo paciente
    public PacienteDTO crear(CrearPacienteDTO dto) {
        // Validar que el correo no esté duplicado
        if (pacienteRepository.existsByCorreoPaciente(dto.getCorreoPaciente())) {
            throw new RuntimeException("Ya existe un paciente con el correo: " + dto.getCorreoPaciente());
        }

        Paciente paciente = new Paciente();
        paciente.setNombrePaciente(dto.getNombrePaciente());
        paciente.setApellidoPaciente(dto.getApellidoPaciente());
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setTelefonoPaciente(dto.getTelefonoPaciente());
        paciente.setCorreoPaciente(dto.getCorreoPaciente());

        Paciente guardado = pacienteRepository.save(paciente);
        return convertirADTO(guardado);
    }

    // Actualizar un paciente
    public PacienteDTO actualizar(Long id, ActualizarPacienteDTO dto) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));

        // Actualizar solo los campos que vienen (no nulos)
        if (dto.getNombrePaciente() != null && !dto.getNombrePaciente().isEmpty()) {
            paciente.setNombrePaciente(dto.getNombrePaciente());
        }

        if (dto.getApellidoPaciente() != null && !dto.getApellidoPaciente().isEmpty()) {
            paciente.setApellidoPaciente(dto.getApellidoPaciente());
        }

        if (dto.getFechaNacimiento() != null) {
            paciente.setFechaNacimiento(dto.getFechaNacimiento());
        }

        if (dto.getTelefonoPaciente() != null && !dto.getTelefonoPaciente().isEmpty()) {
            paciente.setTelefonoPaciente(dto.getTelefonoPaciente());
        }

        if (dto.getCorreoPaciente() != null && !dto.getCorreoPaciente().isEmpty()) {
            // Validar que el nuevo correo no esté en uso por OTRO paciente
            if (pacienteRepository.existsByCorreoPaciente(dto.getCorreoPaciente()) &&
                    !paciente.getCorreoPaciente().equals(dto.getCorreoPaciente())) {
                throw new RuntimeException("Ya existe un paciente con el correo: " + dto.getCorreoPaciente());
            }
            paciente.setCorreoPaciente(dto.getCorreoPaciente());
        }

        Paciente actualizado = pacienteRepository.save(paciente);
        return convertirADTO(actualizado);
    }

    // Eliminar un paciente
    public void eliminar(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));

        // Verificar que no tenga citas asociadas
        long citasAsociadas = citaRepository.countByPacienteIdPaciente(id);
        if (citasAsociadas > 0) {
            throw new RuntimeException(
                    "No se puede eliminar el paciente porque tiene " + citasAsociadas + " citas asociadas"
            );
        }

        pacienteRepository.delete(paciente);
    }

    // Método privado: convertir Entity a DTO
    private PacienteDTO convertirADTO(Paciente paciente) {
        PacienteDTO dto = new PacienteDTO();
        dto.setIdPaciente(paciente.getIdPaciente());
        dto.setNombrePaciente(paciente.getNombrePaciente());
        dto.setApellidoPaciente(paciente.getApellidoPaciente());
        dto.setFechaNacimiento(paciente.getFechaNacimiento());
        dto.setTelefonoPaciente(paciente.getTelefonoPaciente());
        dto.setCorreoPaciente(paciente.getCorreoPaciente());

        // CALCULAR la edad
        int edad = calcularEdad(paciente.getFechaNacimiento());
        dto.setEdad(edad);

        // CALCULAR cantidad de citas
        long cantidadCitas = citaRepository.countByPacienteIdPaciente(paciente.getIdPaciente());
        dto.setCantidadCitas((int) cantidadCitas);

        return dto;
    }

    // Método privado: calcular edad a partir de fecha de nacimiento
    private int calcularEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            return 0;
        }
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
}
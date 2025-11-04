package com.api.gestioncitasmedicas.service;

import com.api.gestioncitasmedicas.dto.*;
import com.api.gestioncitasmedicas.entity.Paciente;
import com.api.gestioncitasmedicas.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    // Obtener todos
    public List<PacienteDTO> obtenerTodos() {
        return pacienteRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener uno por ID
    public PacienteDTO obtenerPorId(Long id) {
        Paciente p = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente no encontrado"));
        return convertirADTO(p);
    }

    // Crear paciente
    public PacienteDTO crear(CrearPacienteDTO dto) {
        if (pacienteRepository.existsByCorreoPaciente(dto.getCorreo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo ya está registrado");
        }

        if (!fechaNacimientoValida(dto.getFechaNacimiento())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fecha de nacimiento inválida");
        }

        Paciente p = new Paciente();
        p.setNombrePaciente(dto.getNombre());
        p.setApellidoPaciente(dto.getApellido());
        p.setFechaNacimiento(dto.getFechaNacimiento());
        p.setTelefonoPaciente(dto.getTelefono());
        p.setCorreoPaciente(dto.getCorreo());

        Paciente guardado = pacienteRepository.save(p);
        return convertirADTO(guardado);
    }

    // Actualizar paciente
    public PacienteDTO actualizar(Long id, ActualizarPacienteDTO dto) {
        Paciente p = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente no encontrado"));

        if (dto.getCorreo() != null && !dto.getCorreo().equals(p.getCorreoPaciente())
                && pacienteRepository.existsByCorreoPaciente(dto.getCorreo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo ya está registrado");
        }

        if (dto.getFechaNacimiento() != null && !fechaNacimientoValida(dto.getFechaNacimiento())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fecha de nacimiento inválida");
        }

        if (dto.getNombre() != null) p.setNombrePaciente(dto.getNombre());
        if (dto.getApellido() != null) p.setApellidoPaciente(dto.getApellido());
        if (dto.getFechaNacimiento() != null) p.setFechaNacimiento(dto.getFechaNacimiento());
        if (dto.getTelefono() != null) p.setTelefonoPaciente(dto.getTelefono());
        if (dto.getCorreo() != null) p.setCorreoPaciente(dto.getCorreo());

        Paciente actualizado = pacienteRepository.save(p);
        return convertirADTO(actualizado);
    }

    // Eliminar paciente
    public void eliminar(Long id) {
        if (!pacienteRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente no encontrado");
        }
        pacienteRepository.deleteById(id);
    }

    // Buscar pacientes por nombre o apellido
    public List<PacienteDTO> buscarPorTermino(String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            return obtenerTodos();
        }
        return pacienteRepository
                .findByNombrePacienteContainingIgnoreCaseOrApellidoPacienteContainingIgnoreCase(termino, termino)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Métodos auxiliares
    private PacienteDTO convertirADTO(Paciente p) {
        PacienteDTO dto = new PacienteDTO();
        dto.setIdPaciente(p.getIdPaciente());
        dto.setNombre(p.getNombrePaciente());
        dto.setApellido(p.getApellidoPaciente());
        dto.setFechaNacimiento(p.getFechaNacimiento());
        dto.setTelefono(p.getTelefonoPaciente());
        dto.setCorreo(p.getCorreoPaciente());
        dto.setEdad(calcularEdad(p.getFechaNacimiento()));
        return dto;
    }

    private int calcularEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) return 0;
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    private boolean fechaNacimientoValida(LocalDate fecha) {
        if (fecha == null || !fecha.isBefore(LocalDate.now())) return false;
        return calcularEdad(fecha) <= 150;
    }
}

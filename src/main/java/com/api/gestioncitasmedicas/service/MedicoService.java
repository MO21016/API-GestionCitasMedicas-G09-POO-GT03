package com.api.gestioncitasmedicas.service;

import com.api.gestioncitasmedicas.DTO.*;
import com.api.gestioncitasmedicas.entity.*;
import com.api.gestioncitasmedicas.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final EspecialidadRepository especialidadRepository;

    public List<MedicoDTO> listarTodos() {
        return medicoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public MedicoDTO obtenerPorId(Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Médico no encontrado con ID: " + id));
        return convertirADTO(medico);
    }

    @Transactional
    public MedicoDTO crear(CrearMedicoDTO dto) {
        if (medicoRepository.existsByCorreoMedico(dto.getCorreo())) {
            throw new IllegalArgumentException("Ya existe un médico con ese correo");
        }

        Medico medico = new Medico();
        medico.setNombreMedico(dto.getNombre());
        medico.setApellidoMedico(dto.getApellido());
        medico.setTelefonoMedico(dto.getTelefono());
        medico.setCorreoMedico(dto.getCorreo());

        List<Especialidad> especialidades = especialidadRepository.findAllById(dto.getEspecialidadesIds());
        if (especialidades.isEmpty()) {
            throw new IllegalArgumentException("Debe asignar especialidades válidas");
        }

        medico.setEspecialidades(especialidades);
        medicoRepository.save(medico);
        return convertirADTO(medico);
    }

    @Transactional
    public MedicoDTO actualizar(Long id, ActualizarMedicoDTO dto) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Médico no encontrado con ID: " + id));

        if (dto.getNombre() != null) medico.setNombreMedico(dto.getNombre());
        if (dto.getApellido() != null) medico.setApellidoMedico(dto.getApellido());
        if (dto.getTelefono() != null) medico.setTelefonoMedico(dto.getTelefono());
        if (dto.getCorreo() != null) medico.setCorreoMedico(dto.getCorreo());

        if (dto.getEspecialidadesIds() != null && !dto.getEspecialidadesIds().isEmpty()) {
            List<Especialidad> especialidades = especialidadRepository.findAllById(dto.getEspecialidadesIds());
            medico.setEspecialidades(especialidades);
        }

        medicoRepository.save(medico);
        return convertirADTO(medico);
    }

    @Transactional
    public void eliminar(Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Médico no encontrado con ID: " + id));
        medicoRepository.delete(medico);
    }

    private MedicoDTO convertirADTO(Medico medico) {
        MedicoDTO dto = new MedicoDTO();
        dto.setId(medico.getIdMedico());
        dto.setNombre(medico.getNombreMedico());
        dto.setApellido(medico.getApellidoMedico());
        dto.setTelefono(medico.getTelefonoMedico());
        dto.setCorreo(medico.getCorreoMedico());
        dto.setEspecialidades(
                medico.getEspecialidades().stream()
                        .map(Especialidad::getNombreEspecialidad)
                        .collect(Collectors.toList())
        );
        return dto;
    }
}



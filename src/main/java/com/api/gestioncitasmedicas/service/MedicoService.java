package com.api.gestioncitasmedicas.service;

import com.api.gestioncitasmedicas.dto.ActualizarMedicoDTO;
import com.api.gestioncitasmedicas.dto.CrearMedicoDTO;
import com.api.gestioncitasmedicas.dto.MedicoDTO;
import com.api.gestioncitasmedicas.entity.Especialidad;
import com.api.gestioncitasmedicas.entity.Medico;
import com.api.gestioncitasmedicas.entity.MedicoEspecialidad;
import com.api.gestioncitasmedicas.repository.CitaRepository;
import com.api.gestioncitasmedicas.repository.MedicoEspecialidadRepository;
import com.api.gestioncitasmedicas.repository.MedicoRepository;
import com.api.gestioncitasmedicas.repository.EspecialidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final MedicoEspecialidadRepository medicoEspecialidadRepository;
    private final EspecialidadRepository especialidadRepository;
    private final CitaRepository citaRepository;

    // Listar todos los médicos
    public List<MedicoDTO> obtenerTodos() {
        return medicoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener un médico por ID
    public MedicoDTO obtenerPorId(Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado con ID: " + id));
        return convertirADTO(medico);
    }

    // Crear un nuevo médico
    public MedicoDTO crear(CrearMedicoDTO dto) {
        // Validar que el correo no esté duplicado
        if (medicoRepository.existsByCorreoMedico(dto.getCorreoMedico())) {
            throw new RuntimeException("Ya existe un médico con el correo: " + dto.getCorreoMedico());
        }

        Medico medico = new Medico();
        medico.setNombreMedico(dto.getNombreMedico());
        medico.setApellidoMedico(dto.getApellidoMedico());
        medico.setTelefonoMedico(dto.getTelefonoMedico());
        medico.setCorreoMedico(dto.getCorreoMedico());

        Medico guardado = medicoRepository.save(medico);
        return convertirADTO(guardado);
    }

    // Actualizar un médico
    public MedicoDTO actualizar(Long id, ActualizarMedicoDTO dto) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado con ID: " + id));

        // Actualizar solo los campos que vienen (no nulos)
        if (dto.getNombreMedico() != null && !dto.getNombreMedico().isEmpty()) {
            medico.setNombreMedico(dto.getNombreMedico());
        }

        if (dto.getApellidoMedico() != null && !dto.getApellidoMedico().isEmpty()) {
            medico.setApellidoMedico(dto.getApellidoMedico());
        }

        if (dto.getTelefonoMedico() != null && !dto.getTelefonoMedico().isEmpty()) {
            medico.setTelefonoMedico(dto.getTelefonoMedico());
        }

        if (dto.getCorreoMedico() != null && !dto.getCorreoMedico().isEmpty()) {
            // Validar que el nuevo correo no esté en uso por OTRO médico
            if (medicoRepository.existsByCorreoMedico(dto.getCorreoMedico()) &&
                    !medico.getCorreoMedico().equals(dto.getCorreoMedico())) {
                throw new RuntimeException("Ya existe un médico con el correo: " + dto.getCorreoMedico());
            }
            medico.setCorreoMedico(dto.getCorreoMedico());
        }

        Medico actualizado = medicoRepository.save(medico);
        return convertirADTO(actualizado);
    }

    // Eliminar un médico
    public void eliminar(Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado con ID: " + id));

        // Verificar que no tenga citas asociadas
        long citasAsociadas = citaRepository.countByMedicoIdMedico(id);
        if (citasAsociadas > 0) {
            throw new RuntimeException(
                    "No se puede eliminar el médico porque tiene " + citasAsociadas + " citas asociadas"
            );
        }

        // Eliminar las relaciones médico-especialidad primero
        medicoEspecialidadRepository.deleteAllByIdMedico(id);

        // Ahora sí eliminar el médico
        medicoRepository.delete(medico);
    }

    // Obtener especialidades de un médico
    public List<String> obtenerEspecialidadesDeMedico(Long idMedico) {
        // Verificar que el médico existe
        if (!medicoRepository.existsById(idMedico)) {
            throw new RuntimeException("Médico no encontrado con ID: " + idMedico);
        }

        List<MedicoEspecialidad> relaciones = medicoEspecialidadRepository.findByIdMedico(idMedico);

        return relaciones.stream()
                .map(relacion -> {
                    Especialidad especialidad = especialidadRepository.findById(relacion.getIdEspecialidad())
                            .orElse(null);
                    return especialidad != null ? especialidad.getNombreEspecialidad() : null;
                })
                .filter(nombre -> nombre != null)
                .collect(Collectors.toList());
    }

    // Método privado: convertir Entity a DTO
    private MedicoDTO convertirADTO(Medico medico) {
        MedicoDTO dto = new MedicoDTO();
        dto.setIdMedico(medico.getIdMedico());
        dto.setNombreMedico(medico.getNombreMedico());
        dto.setApellidoMedico(medico.getApellidoMedico());
        dto.setTelefonoMedico(medico.getTelefonoMedico());
        dto.setCorreoMedico(medico.getCorreoMedico());

        // Obtener las especialidades del médico
        List<String> especialidades = obtenerEspecialidadesDeMedico(medico.getIdMedico());
        dto.setEspecialidades(especialidades);
        dto.setCantidadEspecialidades(especialidades.size());

        return dto;
    }
}
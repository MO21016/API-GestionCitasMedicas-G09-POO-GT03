package com.api.gestioncitasmedicas.service;

import com.api.gestioncitasmedicas.dto.AsignarEspecialidadDTO;
import com.api.gestioncitasmedicas.entity.MedicoEspecialidad;
import com.api.gestioncitasmedicas.repository.EspecialidadRepository;
import com.api.gestioncitasmedicas.repository.MedicoEspecialidadRepository;
import com.api.gestioncitasmedicas.repository.MedicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicoEspecialidadServiceTest {

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private EspecialidadRepository especialidadRepository;

    @Mock
    private MedicoEspecialidadRepository medicoEspecialidadRepository;

    @InjectMocks
    private MedicoEspecialidadService medicoEspecialidadService;

    private AsignarEspecialidadDTO asignarDTO;

    @BeforeEach
    void setUp() {
        asignarDTO = new AsignarEspecialidadDTO();
        asignarDTO.setIdMedico(1L);
        asignarDTO.setIdEspecialidad(1L);
    }

    @Test
    void testAsignarEspecialidadAMedico_Exitoso() {
        // ARRANGE
        when(medicoRepository.existsById(1L)).thenReturn(true);
        when(especialidadRepository.existsById(1L)).thenReturn(true);
        when(medicoEspecialidadRepository.existsByIdMedicoAndIdEspecialidad(1L, 1L)).thenReturn(false);
        when(medicoEspecialidadRepository.save(any(MedicoEspecialidad.class)))
                .thenReturn(new MedicoEspecialidad());

        // ACT
        medicoEspecialidadService.asignarEspecialidadAMedico(asignarDTO);

        // ASSERT
        verify(medicoRepository, times(1)).existsById(1L);
        verify(especialidadRepository, times(1)).existsById(1L);
        verify(medicoEspecialidadRepository, times(1))
                .existsByIdMedicoAndIdEspecialidad(1L, 1L);
        verify(medicoEspecialidadRepository, times(1)).save(any(MedicoEspecialidad.class));
    }

    @Test
    void testAsignarEspecialidadAMedico_MedicoNoExiste() {
        // ARRANGE
        when(medicoRepository.existsById(1L)).thenReturn(false);

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            medicoEspecialidadService.asignarEspecialidadAMedico(asignarDTO);
        });

        assertTrue(exception.getMessage().contains("Médico no encontrado"));
        verify(medicoEspecialidadRepository, never()).save(any(MedicoEspecialidad.class));
    }

    @Test
    void testAsignarEspecialidadAMedico_EspecialidadNoExiste() {
        // ARRANGE
        when(medicoRepository.existsById(1L)).thenReturn(true);
        when(especialidadRepository.existsById(1L)).thenReturn(false);

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            medicoEspecialidadService.asignarEspecialidadAMedico(asignarDTO);
        });

        assertTrue(exception.getMessage().contains("Especialidad no encontrada"));
        verify(medicoEspecialidadRepository, never()).save(any(MedicoEspecialidad.class));
    }

    @Test
    void testAsignarEspecialidadAMedico_RelacionDuplicada() {
        // ARRANGE
        when(medicoRepository.existsById(1L)).thenReturn(true);
        when(especialidadRepository.existsById(1L)).thenReturn(true);
        when(medicoEspecialidadRepository.existsByIdMedicoAndIdEspecialidad(1L, 1L)).thenReturn(true);

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            medicoEspecialidadService.asignarEspecialidadAMedico(asignarDTO);
        });

        assertTrue(exception.getMessage().contains("ya tiene asignada esta especialidad"));
        verify(medicoEspecialidadRepository, never()).save(any(MedicoEspecialidad.class));
    }

    @Test
    void testDesasignarEspecialidadDeMedico_Exitoso() {
        // ARRANGE
        Long idMedico = 1L;
        Long idEspecialidad = 1L;

        when(medicoEspecialidadRepository.existsByIdMedicoAndIdEspecialidad(idMedico, idEspecialidad))
                .thenReturn(true);
        doNothing().when(medicoEspecialidadRepository)
                .deleteByIdMedicoAndIdEspecialidad(idMedico, idEspecialidad);

        // ACT
        medicoEspecialidadService.desasignarEspecialidadDeMedico(idMedico, idEspecialidad);

        // ASSERT
        verify(medicoEspecialidadRepository, times(1))
                .existsByIdMedicoAndIdEspecialidad(idMedico, idEspecialidad);
        verify(medicoEspecialidadRepository, times(1))
                .deleteByIdMedicoAndIdEspecialidad(idMedico, idEspecialidad);
    }

    @Test
    void testDesasignarEspecialidadDeMedico_RelacionNoExiste() {
        // ARRANGE
        Long idMedico = 1L;
        Long idEspecialidad = 1L;

        when(medicoEspecialidadRepository.existsByIdMedicoAndIdEspecialidad(idMedico, idEspecialidad))
                .thenReturn(false);

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            medicoEspecialidadService.desasignarEspecialidadDeMedico(idMedico, idEspecialidad);
        });

        assertTrue(exception.getMessage().contains("no existe"));
        verify(medicoEspecialidadRepository, never())
                .deleteByIdMedicoAndIdEspecialidad(anyLong(), anyLong());
    }
}
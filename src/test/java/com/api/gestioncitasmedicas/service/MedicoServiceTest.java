package com.api.gestioncitasmedicas.service;

import com.api.gestioncitasmedicas.dto.ActualizarMedicoDTO;
import com.api.gestioncitasmedicas.dto.CrearMedicoDTO;
import com.api.gestioncitasmedicas.dto.MedicoDTO;
import com.api.gestioncitasmedicas.entity.Especialidad;
import com.api.gestioncitasmedicas.entity.Medico;
import com.api.gestioncitasmedicas.entity.MedicoEspecialidad;
import com.api.gestioncitasmedicas.repository.CitaRepository;
import com.api.gestioncitasmedicas.repository.EspecialidadRepository;
import com.api.gestioncitasmedicas.repository.MedicoEspecialidadRepository;
import com.api.gestioncitasmedicas.repository.MedicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicoServiceTest {

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private MedicoEspecialidadRepository medicoEspecialidadRepository;

    @Mock
    private EspecialidadRepository especialidadRepository;

    @Mock
    private CitaRepository citaRepository;

    @InjectMocks
    private MedicoService medicoService;

    private Medico medico;
    private CrearMedicoDTO crearDTO;
    private ActualizarMedicoDTO actualizarDTO;
    private Especialidad especialidad1;
    private Especialidad especialidad2;
    private MedicoEspecialidad medicoEspecialidad1;
    private MedicoEspecialidad medicoEspecialidad2;

    @BeforeEach
    void setUp() {
        // Crear médico de prueba
        medico = new Medico();
        medico.setIdMedico(1L);
        medico.setNombreMedico("Carlos");
        medico.setApellidoMedico("Rodríguez");
        medico.setTelefonoMedico("77001234");
        medico.setCorreoMedico("carlos.rodriguez@hospital.com");

        // Crear especialidades de prueba
        especialidad1 = new Especialidad();
        especialidad1.setIdEspecialidad(1L);
        especialidad1.setNombreEspecialidad("Cardiología");

        especialidad2 = new Especialidad();
        especialidad2.setIdEspecialidad(2L);
        especialidad2.setNombreEspecialidad("Medicina Interna");

        // Crear relaciones médico-especialidad
        medicoEspecialidad1 = new MedicoEspecialidad();
        medicoEspecialidad1.setIdMedicoEspecialidad(1L);
        medicoEspecialidad1.setIdMedico(1L);
        medicoEspecialidad1.setIdEspecialidad(1L);

        medicoEspecialidad2 = new MedicoEspecialidad();
        medicoEspecialidad2.setIdMedicoEspecialidad(2L);
        medicoEspecialidad2.setIdMedico(1L);
        medicoEspecialidad2.setIdEspecialidad(2L);

        // Crear CrearMedicoDTO
        crearDTO = new CrearMedicoDTO();
        crearDTO.setNombreMedico("Ana");
        crearDTO.setApellidoMedico("Martínez");
        crearDTO.setTelefonoMedico("77005678");
        crearDTO.setCorreoMedico("ana.martinez@hospital.com");

        // Crear ActualizarMedicoDTO
        actualizarDTO = new ActualizarMedicoDTO();
        actualizarDTO.setNombreMedico("Carlos Alberto");
        actualizarDTO.setTelefonoMedico("77009999");
    }

    @Test
    void testObtenerTodos() {
        // ARRANGE
        Medico medico2 = new Medico();
        medico2.setIdMedico(2L);
        medico2.setNombreMedico("Laura");
        medico2.setApellidoMedico("García");
        medico2.setTelefonoMedico("77002222");
        medico2.setCorreoMedico("laura.garcia@hospital.com");

        List<Medico> medicos = Arrays.asList(medico, medico2);
        when(medicoRepository.findAll()).thenReturn(medicos);
        when(medicoRepository.existsById(anyLong())).thenReturn(true);
        when(medicoEspecialidadRepository.findByIdMedico(anyLong())).thenReturn(new ArrayList<>());

        // ACT
        List<MedicoDTO> resultado = medicoService.obtenerTodos();

        // ASSERT
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Carlos", resultado.get(0).getNombreMedico());
        assertEquals("Laura", resultado.get(1).getNombreMedico());
        verify(medicoRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId_Exitoso() {
        // ARRANGE
        Long id = 1L;
        List<MedicoEspecialidad> relaciones = Arrays.asList(medicoEspecialidad1, medicoEspecialidad2);

        when(medicoRepository.findById(id)).thenReturn(Optional.of(medico));
        when(medicoRepository.existsById(id)).thenReturn(true);
        when(medicoEspecialidadRepository.findByIdMedico(id)).thenReturn(relaciones);
        when(especialidadRepository.findById(1L)).thenReturn(Optional.of(especialidad1));
        when(especialidadRepository.findById(2L)).thenReturn(Optional.of(especialidad2));

        // ACT
        MedicoDTO resultado = medicoService.obtenerPorId(id);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(id, resultado.getIdMedico());
        assertEquals("Carlos", resultado.getNombreMedico());
        assertEquals(2, resultado.getCantidadEspecialidades());
        assertTrue(resultado.getEspecialidades().contains("Cardiología"));
        assertTrue(resultado.getEspecialidades().contains("Medicina Interna"));
        verify(medicoRepository, times(1)).findById(id);
    }

    @Test
    void testObtenerPorId_NoExiste() {
        // ARRANGE
        Long id = 999L;
        when(medicoRepository.findById(id)).thenReturn(Optional.empty());

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            medicoService.obtenerPorId(id);
        });

        assertTrue(exception.getMessage().contains("no encontrado"));
        verify(medicoRepository, times(1)).findById(id);
    }

    @Test
    void testCrear_Exitoso() {
        // ARRANGE
        when(medicoRepository.existsByCorreoMedico(anyString())).thenReturn(false);
        when(medicoRepository.save(any(Medico.class))).thenReturn(medico);
        when(medicoRepository.existsById(anyLong())).thenReturn(true);
        when(medicoEspecialidadRepository.findByIdMedico(anyLong())).thenReturn(new ArrayList<>());

        // ACT
        MedicoDTO resultado = medicoService.crear(crearDTO);

        // ASSERT
        assertNotNull(resultado);
        assertEquals("Carlos", resultado.getNombreMedico());
        verify(medicoRepository, times(1)).save(any(Medico.class));
    }

    @Test
    void testCrear_CorreoDuplicado() {
        // ARRANGE
        when(medicoRepository.existsByCorreoMedico(anyString())).thenReturn(true);

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            medicoService.crear(crearDTO);
        });

        assertTrue(exception.getMessage().contains("Ya existe un médico con el correo"));
        verify(medicoRepository, never()).save(any(Medico.class));
    }

    @Test
    void testActualizar_Exitoso() {
        // ARRANGE
        Long id = 1L;
        when(medicoRepository.findById(id)).thenReturn(Optional.of(medico));
        when(medicoRepository.save(any(Medico.class))).thenReturn(medico);
        when(medicoRepository.existsById(id)).thenReturn(true);
        when(medicoEspecialidadRepository.findByIdMedico(anyLong())).thenReturn(new ArrayList<>());

        // ACT
        MedicoDTO resultado = medicoService.actualizar(id, actualizarDTO);

        // ASSERT
        assertNotNull(resultado);
        verify(medicoRepository, times(1)).findById(id);
        verify(medicoRepository, times(1)).save(any(Medico.class));
    }

    @Test
    void testActualizar_CorreoDuplicado() {
        // ARRANGE
        Long id = 1L;
        actualizarDTO.setCorreoMedico("otro@hospital.com");

        when(medicoRepository.findById(id)).thenReturn(Optional.of(medico));
        when(medicoRepository.existsByCorreoMedico(anyString())).thenReturn(true);

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            medicoService.actualizar(id, actualizarDTO);
        });

        assertTrue(exception.getMessage().contains("Ya existe un médico con el correo"));
        verify(medicoRepository, never()).save(any(Medico.class));
    }

    @Test
    void testEliminar_Exitoso() {
        // ARRANGE
        Long id = 1L;
        when(medicoRepository.findById(id)).thenReturn(Optional.of(medico));
        when(citaRepository.countByMedicoIdMedico(id)).thenReturn(0L);
        doNothing().when(medicoEspecialidadRepository).deleteAllByIdMedico(id);
        doNothing().when(medicoRepository).delete(any(Medico.class));

        // ACT
        medicoService.eliminar(id);

        // ASSERT
        verify(medicoRepository, times(1)).findById(id);
        verify(citaRepository, times(1)).countByMedicoIdMedico(id);
        verify(medicoEspecialidadRepository, times(1)).deleteAllByIdMedico(id);
        verify(medicoRepository, times(1)).delete(any(Medico.class));
    }

    @Test
    void testEliminar_ConCitasAsociadas() {
        // ARRANGE
        Long id = 1L;
        when(medicoRepository.findById(id)).thenReturn(Optional.of(medico));
        when(citaRepository.countByMedicoIdMedico(id)).thenReturn(3L);

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            medicoService.eliminar(id);
        });

        assertTrue(exception.getMessage().contains("tiene"));
        assertTrue(exception.getMessage().contains("citas asociadas"));
        verify(medicoEspecialidadRepository, never()).deleteAllByIdMedico(anyLong());
        verify(medicoRepository, never()).delete(any(Medico.class));
    }

    @Test
    void testObtenerEspecialidadesDeMedico_Exitoso() {
        // ARRANGE
        Long idMedico = 1L;
        List<MedicoEspecialidad> relaciones = Arrays.asList(medicoEspecialidad1, medicoEspecialidad2);

        when(medicoRepository.existsById(idMedico)).thenReturn(true);
        when(medicoEspecialidadRepository.findByIdMedico(idMedico)).thenReturn(relaciones);
        when(especialidadRepository.findById(1L)).thenReturn(Optional.of(especialidad1));
        when(especialidadRepository.findById(2L)).thenReturn(Optional.of(especialidad2));

        // ACT
        List<String> resultado = medicoService.obtenerEspecialidadesDeMedico(idMedico);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains("Cardiología"));
        assertTrue(resultado.contains("Medicina Interna"));
        verify(medicoEspecialidadRepository, times(1)).findByIdMedico(idMedico);
    }

    @Test
    void testObtenerEspecialidadesDeMedico_MedicoNoExiste() {
        // ARRANGE
        Long idMedico = 999L;
        when(medicoRepository.existsById(idMedico)).thenReturn(false);

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            medicoService.obtenerEspecialidadesDeMedico(idMedico);
        });

        assertTrue(exception.getMessage().contains("no encontrado"));
        verify(medicoEspecialidadRepository, never()).findByIdMedico(anyLong());
    }

    @Test
    void testObtenerEspecialidadesDeMedico_SinEspecialidades() {
        // ARRANGE
        Long idMedico = 1L;
        when(medicoRepository.existsById(idMedico)).thenReturn(true);
        when(medicoEspecialidadRepository.findByIdMedico(idMedico)).thenReturn(new ArrayList<>());

        // ACT
        List<String> resultado = medicoService.obtenerEspecialidadesDeMedico(idMedico);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(0, resultado.size());
        verify(medicoEspecialidadRepository, times(1)).findByIdMedico(idMedico);
    }
}
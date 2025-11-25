package com.api.gestioncitasmedicas.service;

import com.api.gestioncitasmedicas.dto.ActualizarEspecialidadDTO;
import com.api.gestioncitasmedicas.dto.CrearEspecialidadDTO;
import com.api.gestioncitasmedicas.dto.EspecialidadDTO;
import com.api.gestioncitasmedicas.entity.Especialidad;
import com.api.gestioncitasmedicas.entity.MedicoEspecialidad;
import com.api.gestioncitasmedicas.repository.EspecialidadRepository;
import com.api.gestioncitasmedicas.repository.MedicoEspecialidadRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EspecialidadServiceTest {

    @Mock
    private EspecialidadRepository especialidadRepository;

    @Mock
    private MedicoEspecialidadRepository medicoEspecialidadRepository;

    @InjectMocks
    private EspecialidadService especialidadService;

    private Especialidad especialidad;
    private CrearEspecialidadDTO crearDTO;
    private ActualizarEspecialidadDTO actualizarDTO;

    @BeforeEach
    void setUp() {
        // Crear especialidad de prueba
        especialidad = new Especialidad();
        especialidad.setIdEspecialidad(1L);
        especialidad.setNombreEspecialidad("Cardiología");
        especialidad.setDescripcion("Especialidad del corazón");

        // Crear DTO de prueba
        crearDTO = new CrearEspecialidadDTO();
        crearDTO.setNombreEspecialidad("Cardiología");
        crearDTO.setDescripcion("Especialidad del corazón");

        // Crear ActualizarDTO de prueba
        actualizarDTO = new ActualizarEspecialidadDTO();
        actualizarDTO.setNombreEspecialidad("Cardiología Avanzada");
        actualizarDTO.setDescripcion("Descripción actualizada");
    }

    @Test
    void testObtenerTodas() {
        // ARRANGE
        Especialidad especialidad2 = new Especialidad();
        especialidad2.setIdEspecialidad(2L);
        especialidad2.setNombreEspecialidad("Pediatría");
        especialidad2.setDescripcion("Atención a niños");

        List<Especialidad> especialidades = Arrays.asList(especialidad, especialidad2);
        when(especialidadRepository.findAll()).thenReturn(especialidades);
        when(medicoEspecialidadRepository.findByIdEspecialidad(anyLong())).thenReturn(new ArrayList<>());

        // ACT
        List<EspecialidadDTO> resultado = especialidadService.obtenerTodas();

        // ASSERT
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Cardiología", resultado.get(0).getNombreEspecialidad());
        assertEquals("Pediatría", resultado.get(1).getNombreEspecialidad());
        verify(especialidadRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId_Exitoso() {
        // ARRANGE
        Long id = 1L;
        when(especialidadRepository.findById(id)).thenReturn(Optional.of(especialidad));
        when(medicoEspecialidadRepository.findByIdEspecialidad(id)).thenReturn(new ArrayList<>());

        // ACT
        EspecialidadDTO resultado = especialidadService.obtenerPorId(id);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(id, resultado.getIdEspecialidad());
        assertEquals("Cardiología", resultado.getNombreEspecialidad());
        verify(especialidadRepository, times(1)).findById(id);
    }

    @Test
    void testObtenerPorId_NoExiste() {
        // ARRANGE
        Long id = 999L;
        when(especialidadRepository.findById(id)).thenReturn(Optional.empty());

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            especialidadService.obtenerPorId(id);
        });

        assertTrue(exception.getMessage().contains("no encontrada"));
        verify(especialidadRepository, times(1)).findById(id);
    }

    @Test
    void testCrear_Exitoso() {
        // ARRANGE
        when(especialidadRepository.existsByNombreEspecialidad(anyString())).thenReturn(false);
        when(especialidadRepository.save(any(Especialidad.class))).thenReturn(especialidad);
        when(medicoEspecialidadRepository.findByIdEspecialidad(anyLong())).thenReturn(new ArrayList<>());

        // ACT
        EspecialidadDTO resultado = especialidadService.crear(crearDTO);

        // ASSERT
        assertNotNull(resultado);
        assertEquals("Cardiología", resultado.getNombreEspecialidad());
        verify(especialidadRepository, times(1)).save(any(Especialidad.class));
    }

    @Test
    void testCrear_NombreDuplicado() {
        // ARRANGE
        when(especialidadRepository.existsByNombreEspecialidad(anyString())).thenReturn(true);

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            especialidadService.crear(crearDTO);
        });

        assertTrue(exception.getMessage().contains("Ya existe una especialidad"));
        verify(especialidadRepository, never()).save(any(Especialidad.class));
    }

    @Test
    void testActualizar() {
        // ARRANGE
        Long id = 1L;
        when(especialidadRepository.findById(id)).thenReturn(Optional.of(especialidad));
        when(especialidadRepository.existsByNombreEspecialidad(anyString())).thenReturn(false);
        when(especialidadRepository.save(any(Especialidad.class))).thenReturn(especialidad);
        when(medicoEspecialidadRepository.findByIdEspecialidad(anyLong())).thenReturn(new ArrayList<>());

        // ACT
        EspecialidadDTO resultado = especialidadService.actualizar(id, actualizarDTO);

        // ASSERT
        assertNotNull(resultado);
        verify(especialidadRepository, times(1)).findById(id);
        verify(especialidadRepository, times(1)).save(any(Especialidad.class));
    }

    @Test
    void testEliminar_Exitoso() {
        // ARRANGE
        Long id = 1L;
        when(especialidadRepository.findById(id)).thenReturn(Optional.of(especialidad));
        when(medicoEspecialidadRepository.findByIdEspecialidad(id)).thenReturn(new ArrayList<>());
        doNothing().when(especialidadRepository).delete(any(Especialidad.class));

        // ACT
        especialidadService.eliminar(id);

        // ASSERT
        verify(especialidadRepository, times(1)).findById(id);
        verify(especialidadRepository, times(1)).delete(any(Especialidad.class));
    }

    @Test
    void testEliminar_ConMedicosAsociados() {
        // ARRANGE
        Long id = 1L;
        List<MedicoEspecialidad> medicosAsociados = Arrays.asList(new MedicoEspecialidad(), new MedicoEspecialidad());

        when(especialidadRepository.findById(id)).thenReturn(Optional.of(especialidad));
        when(medicoEspecialidadRepository.findByIdEspecialidad(id)).thenReturn(medicosAsociados);

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            especialidadService.eliminar(id);
        });

        assertTrue(exception.getMessage().contains("tiene"));
        assertTrue(exception.getMessage().contains("médicos asociados"));
        verify(especialidadRepository, never()).delete(any(Especialidad.class));
    }
}
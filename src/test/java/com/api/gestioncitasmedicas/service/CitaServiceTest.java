package com.api.gestioncitasmedicas.service;

import com.api.gestioncitasmedicas.dto.ActualizarCitaDTO;
import com.api.gestioncitasmedicas.dto.CitaDTO;
import com.api.gestioncitasmedicas.dto.CrearCitaDTO;
import com.api.gestioncitasmedicas.entity.Cita;
import com.api.gestioncitasmedicas.entity.Especialidad;
import com.api.gestioncitasmedicas.entity.Medico;
import com.api.gestioncitasmedicas.entity.Paciente;
import com.api.gestioncitasmedicas.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaServiceTest {

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private EspecialidadRepository especialidadRepository;

    @Mock
    private MedicoEspecialidadRepository medicoEspecialidadRepository;

    @InjectMocks
    private CitaService citaService;

    private Cita cita;
    private Medico medico;
    private Paciente paciente;
    private Especialidad especialidad;
    private CrearCitaDTO crearDTO;
    private ActualizarCitaDTO actualizarDTO;

    @BeforeEach
    void setUp() {
        // Crear médico
        medico = new Medico();
        medico.setIdMedico(1L);
        medico.setNombreMedico("Carlos");
        medico.setApellidoMedico("Rodríguez");

        // Crear paciente
        paciente = new Paciente();
        paciente.setIdPaciente(1L);
        paciente.setNombrePaciente("Juan");
        paciente.setApellidoPaciente("Pérez");

        // Crear especialidad
        especialidad = new Especialidad();
        especialidad.setIdEspecialidad(1L);
        especialidad.setNombreEspecialidad("Cardiología");

        // Crear cita
        cita = new Cita();
        cita.setIdCita(1L);
        cita.setMedico(medico);
        cita.setPaciente(paciente);
        cita.setEspecialidad(especialidad);
        cita.setFechaCita(obtenerProximoLunes());
        cita.setHoraCita(LocalTime.of(9, 0));
        cita.setMotivoCita("Consulta de rutina");
        cita.setEstadoCita(Cita.EstadoCita.PENDIENTE);

        // Crear CrearCitaDTO
        crearDTO = new CrearCitaDTO();
        crearDTO.setIdPaciente(1L);
        crearDTO.setIdMedico(1L);
        crearDTO.setIdEspecialidad(1L);
        crearDTO.setFechaCita(obtenerProximoLunes());
        crearDTO.setHoraCita(LocalTime.of(9, 0));
        crearDTO.setMotivoCita("Consulta de rutina");

        // Crear ActualizarCitaDTO
        actualizarDTO = new ActualizarCitaDTO();
        actualizarDTO.setFechaCita(obtenerProximoLunes().plusDays(1));
        actualizarDTO.setHoraCita(LocalTime.of(10, 0));
    }

    // Método auxiliar para obtener el próximo lunes
    private LocalDate obtenerProximoLunes() {
        LocalDate hoy = LocalDate.now();
        while (hoy.getDayOfWeek() != DayOfWeek.MONDAY) {
            hoy = hoy.plusDays(1);
        }
        return hoy;
    }

    @Test
    void testObtenerTodas() {
        // ARRANGE
        Cita cita2 = new Cita();
        cita2.setIdCita(2L);
        cita2.setMedico(medico);
        cita2.setPaciente(paciente);
        cita2.setEspecialidad(especialidad);
        cita2.setFechaCita(obtenerProximoLunes());
        cita2.setHoraCita(LocalTime.of(10, 0));
        cita2.setEstadoCita(Cita.EstadoCita.CONFIRMADA);

        when(citaRepository.findAll()).thenReturn(Arrays.asList(cita, cita2));

        // ACT
        List<CitaDTO> resultado = citaService.obtenerTodas();

        // ASSERT
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(citaRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId_Exitoso() {
        // ARRANGE
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));

        // ACT
        CitaDTO resultado = citaService.obtenerPorId(1L);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdCita());
        assertEquals("PENDIENTE", resultado.getEstadoCita());
        verify(citaRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerPorId_NoExiste() {
        // ARRANGE
        when(citaRepository.findById(999L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            citaService.obtenerPorId(999L);
        });

        assertTrue(exception.getMessage().contains("no encontrada"));
    }

    @Test
    void testCrear_Exitoso() {
        // ARRANGE
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(especialidadRepository.findById(1L)).thenReturn(Optional.of(especialidad));
        when(medicoEspecialidadRepository.existsByIdMedicoAndIdEspecialidad(1L, 1L)).thenReturn(true);
        when(citaRepository.findByMedicoAndFechaAndHora(anyLong(), any(LocalDate.class), any(LocalTime.class)))
                .thenReturn(Optional.empty());
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        // ACT
        CitaDTO resultado = citaService.crear(crearDTO);

        // ASSERT
        assertNotNull(resultado);
        assertEquals("PENDIENTE", resultado.getEstadoCita());
        verify(citaRepository, times(1)).save(any(Cita.class));
    }

    @Test
    void testCrear_PacienteNoExiste() {
        // ARRANGE
        when(pacienteRepository.findById(1L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            citaService.crear(crearDTO);
        });

        assertTrue(exception.getMessage().contains("Paciente no encontrado"));
        verify(citaRepository, never()).save(any(Cita.class));
    }

    @Test
    void testCrear_MedicoNoExiste() {
        // ARRANGE
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(1L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            citaService.crear(crearDTO);
        });

        assertTrue(exception.getMessage().contains("Médico no encontrado"));
        verify(citaRepository, never()).save(any(Cita.class));
    }

    @Test
    void testCrear_EspecialidadNoExiste() {
        // ARRANGE
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(especialidadRepository.findById(1L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            citaService.crear(crearDTO);
        });

        assertTrue(exception.getMessage().contains("Especialidad no encontrada"));
        verify(citaRepository, never()).save(any(Cita.class));
    }

    @Test
    void testCrear_MedicoSinEspecialidad() {
        // ARRANGE
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(especialidadRepository.findById(1L)).thenReturn(Optional.of(especialidad));
        when(medicoEspecialidadRepository.existsByIdMedicoAndIdEspecialidad(1L, 1L)).thenReturn(false);

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            citaService.crear(crearDTO);
        });

        assertTrue(exception.getMessage().contains("no tiene la especialidad"));
        verify(citaRepository, never()).save(any(Cita.class));
    }

    @Test
    void testCrear_DiaInhabil_Sabado() {
        // ARRANGE
        LocalDate sabado = obtenerProximoLunes().plusDays(5); // Próximo sábado
        crearDTO.setFechaCita(sabado);

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(especialidadRepository.findById(1L)).thenReturn(Optional.of(especialidad));
        when(medicoEspecialidadRepository.existsByIdMedicoAndIdEspecialidad(1L, 1L)).thenReturn(true);

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            citaService.crear(crearDTO);
        });

        assertTrue(exception.getMessage().contains("fines de semana"));
        verify(citaRepository, never()).save(any(Cita.class));
    }

    @Test
    void testCrear_DiaInhabil_Domingo() {
        // ARRANGE
        LocalDate domingo = obtenerProximoLunes().plusDays(6); // Próximo domingo
        crearDTO.setFechaCita(domingo);

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(especialidadRepository.findById(1L)).thenReturn(Optional.of(especialidad));
        when(medicoEspecialidadRepository.existsByIdMedicoAndIdEspecialidad(1L, 1L)).thenReturn(true);

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            citaService.crear(crearDTO);
        });

        assertTrue(exception.getMessage().contains("fines de semana"));
        verify(citaRepository, never()).save(any(Cita.class));
    }

    @Test
    void testCrear_HoraInvalida_HoraAlmuerzo() {
        // ARRANGE
        crearDTO.setHoraCita(LocalTime.of(12, 0)); // Hora de almuerzo

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(especialidadRepository.findById(1L)).thenReturn(Optional.of(especialidad));
        when(medicoEspecialidadRepository.existsByIdMedicoAndIdEspecialidad(1L, 1L)).thenReturn(true);

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            citaService.crear(crearDTO);
        });

        assertTrue(exception.getMessage().contains("Horario no válido"));
        assertTrue(exception.getMessage().contains("almuerzo"));
        verify(citaRepository, never()).save(any(Cita.class));
    }

    @Test
    void testCrear_HoraInvalida_FueraDeRango() {
        // ARRANGE
        crearDTO.setHoraCita(LocalTime.of(17, 0)); // Fuera de horario

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(especialidadRepository.findById(1L)).thenReturn(Optional.of(especialidad));
        when(medicoEspecialidadRepository.existsByIdMedicoAndIdEspecialidad(1L, 1L)).thenReturn(true);

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            citaService.crear(crearDTO);
        });

        assertTrue(exception.getMessage().contains("Horario no válido"));
        verify(citaRepository, never()).save(any(Cita.class));
    }

    @Test
    void testCrear_MedicoNoDisponible() {
        // ARRANGE
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(especialidadRepository.findById(1L)).thenReturn(Optional.of(especialidad));
        when(medicoEspecialidadRepository.existsByIdMedicoAndIdEspecialidad(1L, 1L)).thenReturn(true);
        when(citaRepository.findByMedicoAndFechaAndHora(anyLong(), any(LocalDate.class), any(LocalTime.class)))
                .thenReturn(Optional.of(cita)); // Ya hay una cita

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            citaService.crear(crearDTO);
        });

        assertTrue(exception.getMessage().contains("ya tiene una cita agendada"));
        verify(citaRepository, never()).save(any(Cita.class));
    }

    @Test
    void testActualizar_Exitoso() {
        // ARRANGE
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.findByMedicoAndFechaAndHora(anyLong(), any(LocalDate.class), any(LocalTime.class)))
                .thenReturn(Optional.empty());
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        // ACT
        CitaDTO resultado = citaService.actualizar(1L, actualizarDTO);

        // ASSERT
        assertNotNull(resultado);
        verify(citaRepository, times(1)).save(any(Cita.class));
    }

    @Test
    void testActualizar_CitaCompletada() {
        // ARRANGE
        cita.setEstadoCita(Cita.EstadoCita.COMPLETADA);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            citaService.actualizar(1L, actualizarDTO);
        });

        assertTrue(exception.getMessage().contains("ya está"));
        verify(citaRepository, never()).save(any(Cita.class));
    }

    @Test
    void testCambiarEstado_PendienteAConfirmada() {
        // ARRANGE
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        // ACT
        CitaDTO resultado = citaService.cambiarEstado(1L, "CONFIRMADA");

        // ASSERT
        assertNotNull(resultado);
        verify(citaRepository, times(1)).save(any(Cita.class));
    }

    @Test
    void testCambiarEstado_ConfirmadaACompletada() {
        // ARRANGE
        cita.setEstadoCita(Cita.EstadoCita.CONFIRMADA);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        // ACT
        CitaDTO resultado = citaService.cambiarEstado(1L, "COMPLETADA");

        // ASSERT
        assertNotNull(resultado);
        verify(citaRepository, times(1)).save(any(Cita.class));
    }

    @Test
    void testCambiarEstado_TransicionInvalida() {
        // ARRANGE
        cita.setEstadoCita(Cita.EstadoCita.COMPLETADA);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            citaService.cambiarEstado(1L, "PENDIENTE");
        });

        assertTrue(exception.getMessage().contains("No se puede cambiar el estado"));
        verify(citaRepository, never()).save(any(Cita.class));
    }

    @Test
    void testEliminar_Exitoso() {
        // ARRANGE
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        doNothing().when(citaRepository).delete(any(Cita.class));

        // ACT
        citaService.eliminar(1L);

        // ASSERT
        verify(citaRepository, times(1)).delete(any(Cita.class));
    }

    @Test
    void testEliminar_CitaCompletada() {
        // ARRANGE
        cita.setEstadoCita(Cita.EstadoCita.COMPLETADA);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            citaService.eliminar(1L);
        });

        assertTrue(exception.getMessage().contains("No se puede eliminar una cita que ya fue completada"));
        verify(citaRepository, never()).delete(any(Cita.class));
    }

    @Test
    void testFiltrarPorMedico() {
        // ARRANGE
        when(citaRepository.findByMedicoIdMedico(1L)).thenReturn(Arrays.asList(cita));

        // ACT
        List<CitaDTO> resultado = citaService.filtrar(1L, null, null);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(citaRepository, times(1)).findByMedicoIdMedico(1L);
    }

    @Test
    void testFiltrarPorPaciente() {
        // ARRANGE
        when(citaRepository.findByPacienteIdPaciente(1L)).thenReturn(Arrays.asList(cita));

        // ACT
        List<CitaDTO> resultado = citaService.filtrar(null, 1L, null);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(citaRepository, times(1)).findByPacienteIdPaciente(1L);
    }

    @Test
    void testFiltrarPorEstado() {
        // ARRANGE
        when(citaRepository.findByEstadoCita(Cita.EstadoCita.PENDIENTE)).thenReturn(Arrays.asList(cita));

        // ACT
        List<CitaDTO> resultado = citaService.filtrar(null, null, "PENDIENTE");

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(citaRepository, times(1)).findByEstadoCita(Cita.EstadoCita.PENDIENTE);
    }
}
package com.api.gestioncitasmedicas.service;

import com.api.gestioncitasmedicas.dto.ActualizarPacienteDTO;
import com.api.gestioncitasmedicas.dto.CrearPacienteDTO;
import com.api.gestioncitasmedicas.dto.PacienteDTO;
import com.api.gestioncitasmedicas.entity.Paciente;
import com.api.gestioncitasmedicas.repository.CitaRepository;
import com.api.gestioncitasmedicas.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private CitaRepository citaRepository;

    @InjectMocks
    private PacienteService pacienteService;

    private Paciente paciente;
    private CrearPacienteDTO crearDTO;
    private ActualizarPacienteDTO actualizarDTO;

    @BeforeEach
    void setUp() {
        // Crear paciente de prueba (25 años)
        paciente = new Paciente();
        paciente.setIdPaciente(1L);
        paciente.setNombrePaciente("Juan");
        paciente.setApellidoPaciente("Pérez");
        paciente.setFechaNacimiento(LocalDate.now().minusYears(25)); // 25 años
        paciente.setTelefonoPaciente("77001234");
        paciente.setCorreoPaciente("juan.perez@email.com");

        // Crear CrearPacienteDTO
        crearDTO = new CrearPacienteDTO();
        crearDTO.setNombrePaciente("María");
        crearDTO.setApellidoPaciente("González");
        crearDTO.setFechaNacimiento(LocalDate.now().minusYears(30));
        crearDTO.setTelefonoPaciente("77005678");
        crearDTO.setCorreoPaciente("maria.gonzalez@email.com");

        // Crear ActualizarPacienteDTO
        actualizarDTO = new ActualizarPacienteDTO();
        actualizarDTO.setNombrePaciente("Juan Carlos");
        actualizarDTO.setTelefonoPaciente("77009999");
    }

    @Test
    void testObtenerTodos() {
        // ARRANGE
        Paciente paciente2 = new Paciente();
        paciente2.setIdPaciente(2L);
        paciente2.setNombrePaciente("Ana");
        paciente2.setApellidoPaciente("Martínez");
        paciente2.setFechaNacimiento(LocalDate.now().minusYears(35));
        paciente2.setTelefonoPaciente("77002222");
        paciente2.setCorreoPaciente("ana.martinez@email.com");

        List<Paciente> pacientes = Arrays.asList(paciente, paciente2);
        when(pacienteRepository.findAll()).thenReturn(pacientes);
        when(citaRepository.countByPacienteIdPaciente(anyLong())).thenReturn(0L);

        // ACT
        List<PacienteDTO> resultado = pacienteService.obtenerTodos();

        // ASSERT
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombrePaciente());
        assertEquals("Ana", resultado.get(1).getNombrePaciente());
        assertEquals(25, resultado.get(0).getEdad()); // Verifica cálculo de edad
        assertEquals(35, resultado.get(1).getEdad());
        verify(pacienteRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId_Exitoso() {
        // ARRANGE
        Long id = 1L;
        when(pacienteRepository.findById(id)).thenReturn(Optional.of(paciente));
        when(citaRepository.countByPacienteIdPaciente(id)).thenReturn(3L);

        // ACT
        PacienteDTO resultado = pacienteService.obtenerPorId(id);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(id, resultado.getIdPaciente());
        assertEquals("Juan", resultado.getNombrePaciente());
        assertEquals("Pérez", resultado.getApellidoPaciente());
        assertEquals(25, resultado.getEdad());
        assertEquals(3, resultado.getCantidadCitas());
        verify(pacienteRepository, times(1)).findById(id);
    }

    @Test
    void testObtenerPorId_NoExiste() {
        // ARRANGE
        Long id = 999L;
        when(pacienteRepository.findById(id)).thenReturn(Optional.empty());

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pacienteService.obtenerPorId(id);
        });

        assertTrue(exception.getMessage().contains("no encontrado"));
        verify(pacienteRepository, times(1)).findById(id);
    }

    @Test
    void testBuscarPorNombre() {
        // ARRANGE
        String termino = "Juan";
        when(pacienteRepository.findByNombrePacienteContainingIgnoreCaseOrApellidoPacienteContainingIgnoreCase(
                termino, termino)).thenReturn(Arrays.asList(paciente));
        when(citaRepository.countByPacienteIdPaciente(anyLong())).thenReturn(0L);

        // ACT
        List<PacienteDTO> resultado = pacienteService.buscarPorNombre(termino);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombrePaciente());
        verify(pacienteRepository, times(1))
                .findByNombrePacienteContainingIgnoreCaseOrApellidoPacienteContainingIgnoreCase(termino, termino);
    }

    @Test
    void testCrear_Exitoso() {
        // ARRANGE
        when(pacienteRepository.existsByCorreoPaciente(anyString())).thenReturn(false);
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);
        when(citaRepository.countByPacienteIdPaciente(anyLong())).thenReturn(0L);

        // ACT
        PacienteDTO resultado = pacienteService.crear(crearDTO);

        // ASSERT
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombrePaciente());
        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }

    @Test
    void testCrear_CorreoDuplicado() {
        // ARRANGE
        when(pacienteRepository.existsByCorreoPaciente(anyString())).thenReturn(true);

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pacienteService.crear(crearDTO);
        });

        assertTrue(exception.getMessage().contains("Ya existe un paciente con el correo"));
        verify(pacienteRepository, never()).save(any(Paciente.class));
    }

    @Test
    void testActualizar_Exitoso() {
        // ARRANGE
        Long id = 1L;
        when(pacienteRepository.findById(id)).thenReturn(Optional.of(paciente));
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);
        when(citaRepository.countByPacienteIdPaciente(anyLong())).thenReturn(0L);

        // ACT
        PacienteDTO resultado = pacienteService.actualizar(id, actualizarDTO);

        // ASSERT
        assertNotNull(resultado);
        verify(pacienteRepository, times(1)).findById(id);
        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }

    @Test
    void testActualizar_CorreoDuplicado() {
        // ARRANGE
        Long id = 1L;
        actualizarDTO.setCorreoPaciente("otro@email.com");

        when(pacienteRepository.findById(id)).thenReturn(Optional.of(paciente));
        when(pacienteRepository.existsByCorreoPaciente(anyString())).thenReturn(true);

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pacienteService.actualizar(id, actualizarDTO);
        });

        assertTrue(exception.getMessage().contains("Ya existe un paciente con el correo"));
        verify(pacienteRepository, never()).save(any(Paciente.class));
    }

    @Test
    void testEliminar_Exitoso() {
        // ARRANGE
        Long id = 1L;
        when(pacienteRepository.findById(id)).thenReturn(Optional.of(paciente));
        when(citaRepository.countByPacienteIdPaciente(id)).thenReturn(0L);
        doNothing().when(pacienteRepository).delete(any(Paciente.class));

        // ACT
        pacienteService.eliminar(id);

        // ASSERT
        verify(pacienteRepository, times(1)).findById(id);
        verify(citaRepository, times(1)).countByPacienteIdPaciente(id);
        verify(pacienteRepository, times(1)).delete(any(Paciente.class));
    }

    @Test
    void testEliminar_ConCitasAsociadas() {
        // ARRANGE
        Long id = 1L;
        when(pacienteRepository.findById(id)).thenReturn(Optional.of(paciente));
        when(citaRepository.countByPacienteIdPaciente(id)).thenReturn(5L);

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pacienteService.eliminar(id);
        });

        assertTrue(exception.getMessage().contains("tiene"));
        assertTrue(exception.getMessage().contains("citas asociadas"));
        verify(pacienteRepository, never()).delete(any(Paciente.class));
    }

    @Test
    void testCalculoEdad_Correcto() {
        // ARRANGE
        LocalDate fechaNacimiento = LocalDate.of(2000, 1, 1);
        paciente.setFechaNacimiento(fechaNacimiento);

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(citaRepository.countByPacienteIdPaciente(anyLong())).thenReturn(0L);

        // ACT
        PacienteDTO resultado = pacienteService.obtenerPorId(1L);

        // ASSERT
        int edadEsperada = LocalDate.now().getYear() - 2000;
        assertEquals(edadEsperada, resultado.getEdad());
    }

    @Test
    void testCalculoEdad_FechaNula() {
        // ARRANGE
        paciente.setFechaNacimiento(null);

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(citaRepository.countByPacienteIdPaciente(anyLong())).thenReturn(0L);

        // ACT
        PacienteDTO resultado = pacienteService.obtenerPorId(1L);

        // ASSERT
        assertEquals(0, resultado.getEdad());
    }
}
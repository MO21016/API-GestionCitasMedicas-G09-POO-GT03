-- ============================================
-- Script de Datos de Prueba
-- Sistema de Gestión de Citas Médicas
-- ============================================

USE gestion_citas_medicas;

-- ============================================
-- ESPECIALIDADES (10 especialidades comunes)
-- ============================================
INSERT INTO especialidad (nombre_especialidad, descripcion) VALUES
('Cardiología', 'Diagnóstico y tratamiento de enfermedades del corazón'),
('Pediatría', 'Atención médica de bebés, niños y adolescentes'),
('Dermatología', 'Diagnóstico y tratamiento de enfermedades de la piel'),
('Traumatología', 'Tratamiento de lesiones del sistema musculoesquelético'),
('Ginecología', 'Atención de la salud de la mujer'),
('Oftalmología', 'Diagnóstico y tratamiento de enfermedades de los ojos'),
('Neurología', 'Tratamiento de enfermedades del sistema nervioso'),
('Psiquiatría', 'Diagnóstico y tratamiento de trastornos mentales'),
('Medicina General', 'Atención médica integral y preventiva'),
('Odontología', 'Diagnóstico y tratamiento de enfermedades bucales');

-- ============================================
-- MÉDICOS (12 médicos)
-- ============================================
INSERT INTO medico (nombre_medico, apellido_medico, telefono_medico, correo_medico) VALUES
('Carlos', 'Rodríguez', '77001234', 'carlos.rodriguez@hospital.com'),
('María', 'González', '77005678', 'maria.gonzalez@hospital.com'),
('José', 'Martínez', '77009012', 'jose.martinez@hospital.com'),
('Ana', 'López', '77003456', 'ana.lopez@hospital.com'),
('Roberto', 'Hernández', '77007890', 'roberto.hernandez@hospital.com'),
('Laura', 'García', '77002345', 'laura.garcia@hospital.com'),
('Pedro', 'Ramírez', '77006789', 'pedro.ramirez@hospital.com'),
('Carmen', 'Torres', '77000123', 'carmen.torres@hospital.com'),
('Luis', 'Flores', '77004567', 'luis.flores@hospital.com'),
('Patricia', 'Morales', '77008901', 'patricia.morales@hospital.com'),
('Miguel', 'Castillo', '77001122', 'miguel.castillo@hospital.com'),
('Sofia', 'Vásquez', '77005566', 'sofia.vasquez@hospital.com');

-- ============================================
-- ASIGNACIÓN DE ESPECIALIDADES A MÉDICOS
-- ============================================
INSERT INTO medico_especialidad (id_medico, id_especialidad) VALUES
(1, 1), (1, 9),  -- Dr. Carlos - Cardiología y Medicina General
(2, 2),          -- Dra. María - Pediatría
(3, 3),          -- Dr. José - Dermatología
(4, 4), (4, 9),  -- Dra. Ana - Traumatología y Medicina General
(5, 5),          -- Dr. Roberto - Ginecología
(6, 6),          -- Dra. Laura - Oftalmología
(7, 7), (7, 9),  -- Dr. Pedro - Neurología y Medicina General
(8, 8),          -- Dra. Carmen - Psiquiatría
(9, 9),          -- Dr. Luis - Medicina General
(10, 10),        -- Dra. Patricia - Odontología
(11, 1),         -- Dr. Miguel - Cardiología
(12, 2), (12, 9);-- Dra. Sofia - Pediatría y Medicina General

-- ============================================
-- PACIENTES (15 pacientes)
-- ============================================
INSERT INTO paciente (nombre_paciente, apellido_paciente, fecha_nacimiento, telefono_paciente, correo_paciente) VALUES
('Juan', 'Pérez', '1985-03-15', '70001234', 'juan.perez@email.com'),
('María', 'Sánchez', '1990-07-22', '70005678', 'maria.sanchez@email.com'),
('Carlos', 'Mendoza', '1978-11-30', '70009012', 'carlos.mendoza@email.com'),
('Ana', 'Reyes', '1995-01-10', '70003456', 'ana.reyes@email.com'),
('Roberto', 'Cruz', '1982-05-18', '70007890', 'roberto.cruz@email.com'),
('Laura', 'Díaz', '1988-09-25', '70002345', 'laura.diaz@email.com'),
('Pedro', 'Romero', '2010-02-14', '70006789', 'pedro.romero@email.com'),
('Carmen', 'Silva', '1975-12-05', '70000123', 'carmen.silva@email.com'),
('Luis', 'Ortiz', '1992-04-20', '70004567', 'luis.ortiz@email.com'),
('Patricia', 'Navarro', '1987-08-12', '70008901', 'patricia.navarro@email.com'),
('Miguel', 'Campos', '2015-06-30', '70001122', 'miguel.campos@email.com'),
('Sofía', 'Aguilar', '1993-10-08', '70005566', 'sofia.aguilar@email.com'),
('Diego', 'Mejía', '1980-03-27', '70009900', 'diego.mejia@email.com'),
('Gabriela', 'Ramos', '1998-07-15', '70003344', 'gabriela.ramos@email.com'),
('Fernando', 'Vargas', '1970-11-22', '70007788', 'fernando.vargas@email.com');

-- ============================================
-- CITAS (20 citas de ejemplo)
-- ============================================
INSERT INTO cita (fecha_cita, hora_cita, motivo_cita, estado_cita, id_paciente, id_medico, id_especialidad) VALUES
-- Citas Confirmadas
('2025-10-15', '08:00:00', 'Chequeo cardiológico de rutina', 'Confirmada', 1, 1, 1),
('2025-10-15', '09:00:00', 'Control de crecimiento', 'Confirmada', 7, 2, 2),
('2025-10-15', '10:00:00', 'Consulta por acné', 'Confirmada', 4, 3, 3),
('2025-10-15', '14:00:00', 'Dolor de rodilla', 'Confirmada', 3, 4, 4),
('2025-10-16', '08:00:00', 'Control prenatal', 'Confirmada', 6, 5, 5),
-- Citas Pendientes
('2025-10-17', '08:00:00', 'Revisión de vista', 'Pendiente', 8, 6, 6),
('2025-10-17', '09:00:00', 'Dolor de cabeza recurrente', 'Pendiente', 9, 7, 7),
('2025-10-17', '10:00:00', 'Consulta por ansiedad', 'Pendiente', 10, 8, 8),
('2025-10-17', '14:00:00', 'Chequeo general', 'Pendiente', 2, 9, 9),
('2025-10-18', '08:00:00', 'Limpieza dental', 'Pendiente', 11, 10, 10),
-- Citas Completadas
('2025-10-01', '08:00:00', 'Electrocardiograma', 'Completada', 5, 1, 1),
('2025-10-02', '09:00:00', 'Vacunación infantil', 'Completada', 11, 12, 2),
('2025-10-03', '10:00:00', 'Tratamiento dermatológico', 'Completada', 12, 3, 3),
('2025-10-04', '14:00:00', 'Fisioterapia', 'Completada', 13, 4, 4),
('2025-10-05', '08:00:00', 'Ecografía', 'Completada', 14, 5, 5),
-- Citas Canceladas
('2025-10-20', '08:00:00', 'Consulta oftalmológica', 'Cancelada', 15, 6, 6),
('2025-10-20', '09:00:00', 'Evaluación neurológica', 'Cancelada', 1, 7, 7),
('2025-10-20', '10:00:00', 'Terapia psicológica', 'Cancelada', 2, 8, 8),
('2025-10-21', '08:00:00', 'Consulta general', 'Cancelada', 3, 9, 9),
('2025-10-21', '09:00:00', 'Extracción dental', 'Cancelada', 4, 10, 10);
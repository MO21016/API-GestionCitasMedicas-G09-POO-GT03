-- ============================================
-- Script de Creación de Base de Datos
-- Sistema de Gestión de Citas Médicas
-- ============================================

-- NOTA: Ejecutar primero manualmente en MySQL Workbench
CREATE DATABASE IF NOT EXISTS gestion_citas_medicas;
USE gestion_citas_medicas;

-- ============================================
-- TABLA: medico
-- ============================================
CREATE TABLE IF NOT EXISTS medico (
    id_medico BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_medico VARCHAR(100) NOT NULL,
    apellido_medico VARCHAR(100) NOT NULL,
    telefono_medico VARCHAR(15) NOT NULL,
    correo_medico VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_correo_medico (correo_medico),
    INDEX idx_nombre_medico (nombre_medico, apellido_medico)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: especialidad
-- ============================================
CREATE TABLE IF NOT EXISTS especialidad (
    id_especialidad BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_especialidad VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_nombre_especialidad (nombre_especialidad)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: medico_especialidad (Relación N:M)
-- ============================================
CREATE TABLE IF NOT EXISTS medico_especialidad (
    id_medico_especialidad BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_medico BIGINT NOT NULL,
    id_especialidad BIGINT NOT NULL,
    fecha_asignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_medico) REFERENCES medico(id_medico) ON DELETE CASCADE,
    FOREIGN KEY (id_especialidad) REFERENCES especialidad(id_especialidad) ON DELETE CASCADE,
    UNIQUE KEY unique_medico_especialidad (id_medico, id_especialidad),
    INDEX idx_medico (id_medico),
    INDEX idx_especialidad (id_especialidad)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: paciente
-- ============================================
CREATE TABLE IF NOT EXISTS paciente (
    id_paciente BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_paciente VARCHAR(100) NOT NULL,
    apellido_paciente VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    telefono_paciente VARCHAR(15) NOT NULL,
    correo_paciente VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_correo_paciente (correo_paciente),
    INDEX idx_nombre_paciente (nombre_paciente, apellido_paciente)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: cita
-- ============================================
CREATE TABLE IF NOT EXISTS cita (
    id_cita BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_cita DATE NOT NULL,
    hora_cita TIME NOT NULL,
    motivo_cita VARCHAR(255) NOT NULL,
    estado_cita ENUM('Pendiente', 'Confirmada', 'Cancelada', 'Completada') NOT NULL DEFAULT 'Pendiente',
    id_paciente BIGINT NOT NULL,
    id_medico BIGINT NOT NULL,
    id_especialidad BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_paciente) REFERENCES paciente(id_paciente) ON DELETE RESTRICT,
    FOREIGN KEY (id_medico) REFERENCES medico(id_medico) ON DELETE RESTRICT,
    FOREIGN KEY (id_especialidad) REFERENCES especialidad(id_especialidad) ON DELETE RESTRICT,
    INDEX idx_fecha_cita (fecha_cita),
    INDEX idx_estado_cita (estado_cita),
    INDEX idx_medico_fecha (id_medico, fecha_cita, hora_cita),
    INDEX idx_paciente (id_paciente)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
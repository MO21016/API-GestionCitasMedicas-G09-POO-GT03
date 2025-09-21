# API de Gestión de Citas Médicas

## Descripción del Proyecto
Este proyecto corresponde al **Proyecto de Ciclo** de la asignatura **Programación Orientada a Objetos (POO)** de la **Universidad de El Salvador – Facultad Multidisciplinaria de Occidente** 

Se trata de una **API RESTful** desarrollada en **Spring Boot**, cuyo objetivo es gestionar el proceso de citas médicas entre pacientes y médicos

La API permite:
- Registrar **médicos** y sus **especialidades**
- Registrar **pacientes**
- Agendar **citas médicas**, verificando:
  - Disponibilidad del médico
  - Que la especialidad del médico corresponda con la consulta
- Consultar y cancelar citas médicas

El desarrollo sigue los principios de **POO, arquitectura en capas (Controller–Service–Repository)**, uso de **DTOs, validaciones y Lombok** para mantener un código limpio y desacoplado

---

## Integrantes del Equipo
| Nombre                          | Carnet   |
|---------------------------------|----------|
| Jason Alexander Molina Ortiz    | MO21016  |
| Jose Manuel Mendoza Vasquez     | MV24005  |
| Vladimir Ernesto Reyes Zaldaña  | RZ23001  |
| Andrés Zavala Alvarado          | ZA21010  |
| José Ricardo González           | GG22088  |

---

## Tecnologías Utilizadas
- **Java 17**
- **Spring Boot 3.x**
  - Spring Web
  - Spring Data JPA
  - Spring Validation
- **Lombok**
- **Maven** como gestor de dependencias
- **H2 Database** (para desarrollo y pruebas)
- **JUnit 5** para pruebas unitarias
- **PlantUML** para la documentación UML

---

## Estructura del Proyecto
```

gestioncitasmedicas/
├── src/
│   ├── main/java/...    # Código fuente
│   └── test/java/...    # Pruebas unitarias
├── docs/
│   └── uml/             # Diagramas UML en .puml y .png
├── pom.xml              # Configuración Maven
└── README.md

````

---

## Diagramas UML

Los diagramas que representan el modelo de dominio y la lógica del sistema se encuentran en la carpeta [`docs/uml`](docs/uml)

Estos diagramas permiten entender la estructura de clases, la organización de la base de datos y las interacciones entre actores y el sistema

---

### Diagrama de Clases
![Diagrama de Clases](docs/uml/diagrama_clases.png)

El **diagrama de clases** representa las entidades principales del sistema y sus relaciones:
- **Paciente**: entidad que puede solicitar una o más citas médicas
- **Médico**: entidad que puede estar asociada a una o varias especialidades y atender múltiples citas
- **Especialidad**: área de atención médica que se asigna a médicos y a las citas
- **Cita**: entidad que relaciona a un paciente, un médico y una especialidad en una fecha y hora específicas

Relaciones importantes:
- Un **paciente** puede solicitar varias **citas**
- Un **médico** atiende varias **citas**
- Un **médico** puede tener múltiples **especialidades** (relación N:M)
- Una **cita** debe estar vinculada a una **especialidad**

---

### Diagrama Entidad–Relación
![Diagrama ER](docs/uml/diagrama_entidadRelacion.png)

El **diagrama entidad–relación (ER)** muestra la estructura de la base de datos:
- **Paciente**, **Médico**, **Especialidad** y **Cita** son tablas principales
- La relación N:M entre **Médico** y **Especialidad** se resuelve con una tabla intermedia `Medico_Especialidad`
- Las llaves foráneas (`FK`) permiten vincular cada cita con un paciente, un médico y una especialidad

Este modelo asegura la integridad referencial y soporta las consultas necesarias (ejemplo: ver agenda de un médico o historial de un paciente)

---

### Diagrama de Casos de Uso
![Casos de Uso](docs/uml/diagrama_casos.png)

El **diagrama de casos de uso** describe las interacciones de los actores con el sistema:
- **Administrador**: registra médicos, pacientes y asigna especialidades
- **Paciente**: solicita y cancela citas, además puede consultar sus citas programadas
- **Médico**: consulta sus citas agendadas

Flujos destacados:
- Al **solicitar una cita**, el sistema incluye automáticamente los pasos de:
    - Verificar especialidad del médico
    - Verificar disponibilidad del médico
    - Confirmar la cita si las validaciones son exitosas

---

## Casos de Uso Principales

1. **Registrar pacientes**  
   Permite almacenar los datos de un nuevo paciente en el sistema


2. **Registrar médicos y asignar especialidades**  
   Un médico puede ser registrado y se le asignan una o varias especialidades médicas


3. **Solicitar una cita médica**  
   El paciente selecciona médico, especialidad, fecha y hora. El sistema valida que:
    - El médico tenga la especialidad requerida
    - El médico esté disponible en la fecha/hora solicitada


4. **Verificar disponibilidad del médico**  
   El sistema revisa si el médico ya tiene otra cita en el horario solicitado


5. **Confirmar una cita**  
   Una vez validadas especialidad y disponibilidad, el sistema registra la cita como **confirmada**


6. **Cancelar una cita**  
   El paciente puede cancelar una cita previamente registrada


7. **Consultar citas programadas**
    - El **paciente** puede consultar sus próximas citas
    - El **médico** puede consultar las citas en su agenda
    - El **administrador** puede consultar el listado general de citas

---

## Cómo ejecutar el proyecto

### Requisitos previos
- [Java 17](https://adoptium.net/)
- [Maven](https://maven.apache.org/)
- IDE recomendado: [IntelliJ IDEA](https://www.jetbrains.com/idea/)

### Pasos
1. Clonar el repositorio:
   ```bash
   git clone https://github.com/usuario/gestioncitasmedicas.git
   cd gestioncitasmedicas

2. Compilar y ejecutar:

   ```bash
   mvn spring-boot:run
   ```
3. Acceder a la API en:

   ```
   http://localhost:8080
   ```

---

## Endpoints principales (previstos)

* `POST /pacientes` → Registrar paciente
* `POST /medicos` → Registrar médico
* `POST /medicos/{id}/especialidades` → Asignar especialidad a médico
* `POST /citas` → Agendar cita
* `GET /citas/{id}` → Consultar cita
* `DELETE /citas/{id}` → Cancelar cita

---

## Licencia

Este proyecto es de carácter **académico** y fue desarrollado con fines **educativos** en la asignatura **Programación Orientada a Objetos, Ciclo II-2025**, de la **Universidad de El Salvador – Facultad Multidisciplinaria de Occidente**
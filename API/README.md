# 🏥 Medical API - Sistema de Gestión de Citas Médicas

## ⚠️ ESTADO DEL PROYECTO: EN DESARROLLO ACTIVO 🚧

¡Bienvenido a este proyecto se encuentra actualmente en **fase de desarrollo (WIP - Work In Progress)**. Estamos construyendo una base sólida para un sistema de gestión de citas médicas altamente seguro y eficiente.

---

## 💡 ¿En que consiste?

Es una API RESTful diseñada para gestionar de manera centralizada la programación, cancelación y seguimiento de citas médicas. Su objetivo principal es ofrecer un *backend* robusto para clínicas y consultorios.

### 🔒 Seguridad y Roles

Uno de los pilares de este proyecto es la seguridad y la gestión de permisos:

* **Spring Security:** Utilizado para implementar mecanismos de autenticación (JWT/OAuth2) y garantizar que todos los *endpoints* estén protegidos.
* **Multi-Role Access Control:** El sistema maneja distintos roles clave, asegurando que cada usuario (Paciente, Doctor, Administrador) tenga permisos estrictos y definidos.

### 🩺 Funcionalidades Clave (Planificadas/En Curso)

* **Gestión de Citas:** Crear, modificar y cancelar citas.
* **Perfiles de Usuarios:** Registro y gestión de Pacientes, Doctores y Administradores.
* **Disponibilidad Médica:** Definición de horarios y disponibilidad de los doctores.
* **Historial y Logs:** Registro de eventos del sistema (próximamente).

---

## 🛠️ Tecnologías Utilizadas

| Categoría | Herramientas                                           |
| :--- |:-------------------------------------------------------|
| **Backend** | `Java 21`, `Spring Boot 3.3.0`, `Spring Web`, `Lombok` |
| **Seguridad** | 🛡️ **Spring Security** (Autenticación y Autorización) |
| **Persistencia** | `Spring Data JPA`, `PostgreSQL`                        |
| **Utilidades** | `Maven`, `Springdoc OpenAPI (Swagger UI)`              |

---

## 🚀 Instalación y Ejecución

_(Nota: Estos pasos son placeholder hasta finalizar la fase inicial de desarrollo)_

1.  Clona el repositorio.
2.  Configura la base de datos `PostgreSQL` en el archivo de propiedades.
3.  Ejecuta la aplicación usando `./mvnw spring-boot:run`.

---

## 🤝 Contribuciones y Feedback

Dado que el proyecto está en desarrollo activo, tu feedback es muy valioso. Si tienes ideas o detectas un problema, por favor, abre un *Issue* en GitHub.

---

## 👤 Autor

**Jostin Soza**
🔗 [https://github.com/SozaJostin-Sc]

---

## 📜 Licencia

Este proyecto está bajo la Licencia **MIT**.
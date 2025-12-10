# 📦 Sistema de Gestión de Inventario

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)

> Un sistema web robusto y eficiente para el control de stock, gestión de almacenes y generación de reportes, diseñado con una arquitectura MVC limpia y escalable.

---

## 📋 Tabla de Contenidos
- [✨ Características](#-características)
- [🛠️ Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [🚀 Instalación y Configuración](#-instalación-y-configuración)
- [📖 Uso del Sistema](#-uso-del-sistema)
- [🏗️ Arquitectura](#-arquitectura)
- [📄 Reportes](#-reportes)

---

## ✨ Características

### 🔐 Seguridad y Roles
*   **Autenticación Robusta**: Sistema de login seguro.
*   **Control de Acceso Basado en Roles (RBAC)**:
    *   👨‍💼 **Administrador**: Acceso total (CRUD, reportes financieros, gestión de usuarios).
    *   👷 **Empleado**: Acceso de consulta y generación de solicitudes de almacén.

### 📦 Gestión de Inventario
*   **CRUD Completo**: Crear, Leer, Actualizar y Eliminar productos.
*   **Búsqueda Avanzada**: Filtrado dinámico por palabra clave, sede, categoría y marca.
*   **Papelera de Reciclaje**: Sistema de "Soft Delete" que permite restaurar productos eliminados accidentalmente.
*   **Control de Estado**: Marcar productos como disponibles/no disponibles sin eliminarlos.

### 📊 Reportes y Documentos
*   **📄 Solicitudes en TXT**: Generación automática de comprobantes de solicitud de productos.
*   **📈 Valorización de Stock (PDF)**: Reporte financiero detallado del valor total del inventario.
*   **history Historial de Consultas (PDF)**: Registro y exportación de movimientos y consultas realizadas.

---

## 🛠️ Tecnologías Utilizadas

Este proyecto sigue los mejores estándares de desarrollo moderno con Java:

*   **Backend**: Java 17, Spring Boot (Web, Data JPA, Security).
*   **Base de Datos**: PostgreSQL.
*   **Frontend**: HTML5, CSS3, Bootstrap 5, Thymeleaf.
*   **Herramientas**: Maven (Gestión de dependencias), iText (Generación de PDFs).

---

## 🚀 Instalación y Configuración

### Prerrequisitos
*   Java JDK 17 o superior.
*   Maven.
*   PostgreSQL instalado y corriendo.

### Pasos
1.  **Clonar el repositorio**
    ```bash
    git clone https://github.com/tu-usuario/modulo-inventario.git
    cd modulo-inventario
    ```

2.  **Configurar Base de Datos**
    Abre `src/main/resources/application.properties` y ajusta tus credenciales:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/inventario_db
    spring.datasource.username=tu_usuario
    spring.datasource.password=tu_contraseña
    ```

3.  **Ejecutar la Aplicación**
    ```bash
    mvn spring-boot:run
    ```

4.  **Acceder**
    Abre tu navegador en: `http://localhost:8080`

---

## 🏗️ Arquitectura

El sistema implementa una arquitectura **MVC (Modelo-Vista-Controlador)** estricta, siguiendo los principios **SOLID**:

*   **Controller**: Maneja las peticiones HTTP (`InventarioController`).
*   **Service**: Contiene la lógica de negocio (`ProductoService`, `SolicitudService`).
*   **Repository (DAO)**: Abstracción de acceso a datos con Spring Data JPA.
*   **Entity**: Modelado de datos (`Producto`, `Solicitud`).

---

## 📄 Licencia

Este proyecto es de uso educativo y privado.

# EcoGrow - Sistema de Gestión de Reforestación y Áreas de Vida

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge\&logo=openjdk\&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-21-5382A1?style=for-the-badge\&logo=java\&logoColor=white)
![Scene Builder](https://img.shields.io/badge/Scene%20Builder-FXML-007ACC?style=for-the-badge)
![Design Patterns](https://img.shields.io/badge/Design%20Patterns-GoF-8E44AD?style=for-the-badge)
![MySQL](https://img.shields.io/badge/MySQL-Database-4479A1?style=for-the-badge\&logo=mysql\&logoColor=white)
![phpMyAdmin](https://img.shields.io/badge/phpMyAdmin-Database%20Management-6C78AF?style=for-the-badge\&logo=phpmyadmin\&logoColor=white)
![Linux](https://img.shields.io/badge/Linux-FCC624?style=for-the-badge\&logo=linux\&logoColor=black)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge\&logo=git\&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge\&logo=github\&logoColor=white)


**EcoGrow** es una aplicación de escritorio desarrollada para apoyar la gestión ambiental y el seguimiento de procesos de reforestación y conservación de áreas de vida.

El sistema permite centralizar diferentes procesos relacionados con la gestión de usuarios, áreas de vida, taxonomía de árboles, actividades de plantación, censos forestales y documentación asociada.

El proyecto fue desarrollado como proyecto integrador académico, aplicando principios de ingeniería de software, programación orientada a objetos, acceso a bases de datos y patrones de diseño **GoF (Gang of Four)**.

---

## Objetivos

Los principales objetivos de EcoGrow son:

* Gestionar usuarios y sus respectivos roles dentro del sistema.
* Administrar áreas de vida destinadas a la conservación.
* Registrar y consultar información relacionada con especies de árboles.
* Gestionar actividades de plantación y reforestación.
* Registrar censos forestales para realizar seguimiento al estado de las áreas.
* Gestionar documentación relacionada con las áreas de vida.
* Mantener organizada la información mediante una base de datos relacional.
* Aplicar patrones de diseño para mejorar la organización y mantenibilidad del software.

---

## Características principales

### Autenticación y gestión de usuarios

El sistema cuenta con un módulo de usuarios que permite gestionar las credenciales y los diferentes roles de acceso.

Entre sus funcionalidades se encuentran:

* Inicio de sesión.
* Registro de usuarios.
* Gestión de roles.
* Control de acceso a diferentes módulos.

### Gestión de áreas de vida

Permite administrar las áreas destinadas a la conservación y reforestación.

Se puede almacenar información relacionada con:

* Nombre del área.
* Ubicación.
* Dimensiones.
* Estado de conservación.
* Información asociada a la documentación del área.

###  Taxonomía de árboles

El sistema permite registrar y consultar información relacionada con las especies de árboles utilizadas en los procesos de reforestación.

La información contempla diferentes niveles taxonómicos como:

* Especie.
* Género.
* Familia.
* Nombre común.

### Actividades de plantación

Permite registrar las actividades de reforestación realizadas en las diferentes áreas de vida.

Cada actividad puede relacionar:

* El árbol o especie utilizada.
* El área donde se realizó la plantación.
* La fecha de la actividad.
* Información asociada a la actividad.

### Censos forestales

Permite registrar información relacionada con el seguimiento y estado de las áreas forestales.

Entre los datos gestionados se encuentran métricas relacionadas con:

* Cobertura vegetal.
* Estado general del área.
* Seguimiento de las condiciones del ecosistema.

### Documentación

Permite gestionar documentación relacionada con las áreas de vida, incluyendo información sobre:

* Documentos oficiales.
* Autoridades emisoras.
* Información legal.
* Soportes relacionados con la protección de las áreas.

---

## Arquitectura y patrones de diseño

Para organizar el código y facilitar su mantenimiento, el proyecto utiliza una arquitectura basada en la separación de responsabilidades entre:

* Controladores.
* Modelos.
* DAO.
* Fachadas.
* Utilidades.
* Vistas.

Además, se aplican patrones de diseño **GoF (Gang of Four)**.

### 1. Singleton

El patrón **Singleton** se utiliza en la gestión de recursos que requieren una instancia controlada, particularmente en componentes relacionados con la conexión y administración de la base de datos.

En el proyecto existe la clase:

```text
ConnectionManager
```

El objetivo es centralizar la gestión de la conexión y evitar que diferentes partes de la aplicación implementen de forma independiente la lógica de conexión a la base de datos.

### 2. Facade

El patrón **Facade (Fachada)** permite proporcionar una interfaz simplificada para interactuar con diferentes componentes del sistema.

En el proyecto existe el paquete:

```text
facade/
```

y la clase:

```text
UserFacade.java
```

La fachada permite encapsular determinadas operaciones y reducir el acoplamiento entre la interfaz de usuario y la lógica interna del sistema.

---

## Tecnologías

| Tecnología / Herramienta | Uso                                                 |
| ------------------------ | --------------------------------------------------- |
| **Java 21**              | Lenguaje principal de programación                  |
| **JavaFX**               | Desarrollo de la interfaz gráfica                   |
| **FXML**                 | Definición de las interfaces gráficas               |
| **Scene Builder**        | Diseño visual de interfaces JavaFX                  |
| **MySQL**                | Sistema gestor de base de datos                     |
| **phpMyAdmin**           | Administración y gestión de la base de datos        |
| **Maven**                | Gestión de dependencias y construcción del proyecto |
| **Git**                  | Control de versiones                                |
| **GitHub**               | Alojamiento y gestión del repositorio               |
| **Linux / Arch Linux**   | Entorno de desarrollo                               |

---

## Base de datos

EcoGrow utiliza **MySQL** como sistema gestor de base de datos.

La administración de la base de datos se realiza mediante **phpMyAdmin**, facilitando tareas como:

* Creación y modificación de tablas.
* Administración de registros.
* Gestión de relaciones.
* Ejecución de consultas SQL.
* Importación y exportación de bases de datos.

El proyecto incluye el archivo:

```text
ecogrow (1).sql
```

que contiene la estructura y los datos correspondientes a la base de datos del proyecto.

### Modelo relacional

La base de datos está organizada mediante diferentes entidades relacionadas entre sí y utiliza claves foráneas para mantener la integridad referencial.

Principales tablas:

* **`users`** — Gestión de usuarios, credenciales y roles.
* **`living_areas`** — Información de las áreas de vida.
* **`tree_taxonomy`** — Información taxonómica de los árboles.
* **`planting_activities`** — Registro de actividades de plantación.
* **`forest_censuses`** — Registro de censos forestales.
* **`documentation`** — Documentación relacionada con las áreas de vida.

---

## Interfaz gráfica

La interfaz de usuario está desarrollada utilizando **JavaFX** y **FXML**.

Los archivos FXML permiten separar la definición visual de la interfaz de la lógica implementada en Java.

Para el diseño de las interfaces se utiliza **Scene Builder**, facilitando la creación y edición visual de los componentes JavaFX.

La aplicación contiene diferentes vistas, entre ellas:

* Login.
* Registro de usuarios.
* Dashboard.
* Menú de usuario.
* Menú de administrador.
* Gestión de áreas de vida.
* Gestión de actividades de plantación.
* Gestión de taxonomía.
* Censos forestales.
* Documentación.

---

##  Estructura del proyecto

La estructura principal del código fuente se organiza de la siguiente manera:

```text
prubaproyect/
├── ecogrow (1).sql
├── effective-pom.xml
├── pom.xml
├── README.md
│
└── src/
    └── main/
        └── java/
            ├── com/
            │   └── usta/
            │       ├── App.java
            │       │
            │       ├── controllers/
            │       │   ├── DashBoardController.java
            │       │   ├── DocumentationController.java
            │       │   ├── ForestCensusesController.java
            │       │   ├── FrontPageController.java
            │       │   ├── LivingAreasController.java
            │       │   ├── LoginController.java
            │       │   ├── MenuController.java
            │       │   ├── PanelController.java
            │       │   ├── PlantingActivitiesController.java
            │       │   ├── SingUpController.java
            │       │   └── TreeTaxonomyController.java
            │       │
            │       ├── facade/
            │       │   └── UserFacade.java
            │       │
            │       ├── models/
            │       │   ├── documentations/
            │       │   ├── forestCensuses/
            │       │   ├── livingAreas/
            │       │   ├── plantingActivities/
            │       │   ├── treetaxonomy/
            │       │   └── user/
            │       │
            │       ├── utils/
            │       │   ├── ConnectionManager.java
            │       │   ├── GenericDao.java
            │       │   ├── Session.java
            │       │   └── Window.java
            │       │
            │       └── views/
            │           ├── css/
            │           │   └── Styles.css
            │           ├── Images/
            │           ├── Dashboard.fxml
            │           ├── Documentation.fxml
            │           ├── ForestCensuses.fxml
            │           ├── FrontPage.fxml
            │           ├── LivingAreas.fxml
            │           ├── login.fxml
            │           ├── Panel.fxml
            │           ├── PlantingActivities.fxml
            │           ├── SingUp.fxml
            │           └── tree.fxml
            │
            └── module-info.java
```

## Instalación y ejecución

### Requisitos

Antes de ejecutar el proyecto se recomienda tener instalado:

* Java JDK 21.
* JavaFX.
* Maven.
* MySQL.
* phpMyAdmin.
* Git.

### 1. Clonar el repositorio

```bash
git clone
```

### 2. Crear la base de datos

Abrir phpMyAdmin y crear/importar la base de datos utilizando el archivo:

```text
ecogrow (1).sql
```

### 3. Configurar la conexión

Configurar los parámetros de conexión de acuerdo con el entorno local:

```text
Host
Puerto
Usuario
Contraseña
Base de datos
```

### 4. Compilar el proyecto

Desde la carpeta raíz del proyecto:

```bash
mvn clean install
```

### 5. Ejecutar la aplicación

Ejecutar la clase principal:

```text
com.usta.App
```

---


##  Principios aplicados

Durante el desarrollo se tuvieron en cuenta diferentes principios de programación y diseño de software:

* Programación Orientada a Objetos.
* Separación de responsabilidades.
* Reutilización de código.
* Encapsulamiento.
* Abstracción.
* Acceso estructurado a datos mediante DAO.
* Aplicación de patrones de diseño GoF.
* Separación entre lógica de aplicación e interfaz gráfica.

---

## Proyecto académico

**EcoGrow** fue desarrollado como proyecto académico integrador con el propósito de aplicar conocimientos de:

* Programación Orientada a Objetos.
* Java.
* JavaFX.
* Bases de datos.
* SQL.
* Arquitectura de software.
* Patrones de diseño.
* Control de versiones.

---

## Tecnologías principales

```text
Java 21
   │
   ├── JavaFX
   │     └── FXML
   │           └── Scene Builder
   │
   ├── Maven
   │
   ├── MySQL
   │     └── phpMyAdmin
   │
   └── Git / GitHub
```

---

## Estado del proyecto

🟢 **Proyecto académico en desarrollo.**

El proyecto puede continuar evolucionando mediante la incorporación de nuevas funcionalidades, mejoras de seguridad, optimización de consultas y ampliación de los módulos de gestión ambiental.

---

## EcoGrow

> **Tecnología al servicio de la conservación y la reforestación. :)**

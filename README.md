# EcoGrow - Sistema de Gestión de Reforestación y Áreas de Vida

![Java](https://shields.io)
![Design Patterns](https://shields.io)
![Linux](https://shields.io)

**EcoGrow** es una solución de software orientada a la gestión ambiental y la reforestación controlada. Su objetivo principal es resolver un problema crítico en la gestión ecológica: la falta de herramientas centralizadas para planificar, ejecutar y auditar proyectos de siembra de árboles en zonas vulnerables.

Desarrollado como proyecto integrador académico de segundo semestre, destaca por el uso de buenas prácticas de ingeniería de software, control de accesos por roles y la implementación de patrones de diseño GoF (*Gang of Four*).

---

##  Características Clave (Impacto Técnico)
*   **Autenticación y Control de Roles:** Registro seguro de usuarios diferenciando privilegios del sistema (ej. `environmental authority`).
*   **Gestión de Áreas de Vida:** Mapeo, dimensionamiento (en hectáreas/metros cuadrados) y clasificación según su estado de conservación (`Critical`, `Endangered`, `Vulnerable`, `Stable`, `Improving`, `At Risk`).
*   **Trazabilidad de Plantaciones:** Registro histórico de actividades de reforestación enlazadas directamente a taxonomías específicas de árboles y zonas geográficas.
*   **Módulo de Observabilidad:** Censos forestales integrados para evaluar el porcentaje de cobertura vegetal y la evolución del ecosistema en tiempo real.

---

## Arquitectura y Patrones de Diseño Aplicados

Para garantizar que el sistema sea fácil de mantener y escalar, se implementaron dos patrones de diseño estructurales y de creación fundamentales en el desarrollo de software profesional:

### 1. Patrón de Diseño: Singleton (Instancia Única)
El patrón **Singleton** se utilizó principalmente en la clase encargada de gestionar la conexión a la base de datos (por ejemplo, `DatabaseConnection`).

*   **¿Por qué se usó?** En aplicaciones reales, abrir una conexión nueva a la base de datos por cada clic o petición del usuario destruye el rendimiento del servidor.
*   **¿Cómo funciona en el código?** El patrón restringe la creación de objetos de esa clase a **una sola instancia global**. Cada vez que un controlador o repositorio necesita comunicarse con MariaDB, el sistema reutiliza la misma conexión activa en memoria, optimizando drásticamente el uso de recursos y memoria en el entorno local.

### 2. Patrón de Diseño: Facade (Fachada)
El patrón **Facade** se aplicó en la capa de servicios o controladores lógicos (por ejemplo, una clase unificada como `EcoGrowFacade` o el controlador principal).

*   **¿Por qué se usó?** La lógica detrás de registrar una plantación requiere tocar múltiples tablas a la vez: verificar el usuario, validar el estado del área de vida y buscar la taxonomía del árbol. Mostrar toda esa complejidad al flujo principal de la interfaz de usuario hace que el código sea difícil de leer.
*   **¿Cómo funciona en el código?** Este patrón actúa como una **interfaz simplificada y unificada** frente a un subsistema complejo. La interfaz de usuario solo llama a un método sencillo (ej. `facade.registrarNuevaPlantacion(...)`), y la Fachada se encarga de coordinar internamente todas las operaciones con los repositorios y modelos de forma oculta para el cliente.

---

## Stack Tecnológico
*   **Lenguaje:** Java 21 (JavaFx)
*   **Entorno de Desarrollo Nativo:** Arch Linux
*   **Motor de Base de Datos:** MySQL (Administrado mediante phpMyAdmin)
*   **Gestión de Versiones:** Git & GitHub
---

## Modelo Relacional de Datos
La base de datos se modeló de forma estructurada asegurando la integridad referencial mediante restricciones de claves foráneas (`FOREIGN KEY`) y limpiezas lógicas en cascada:

*   **`users`**: Control de credenciales, datos de contacto y tipificación de roles.
*   **`living_areas`**: Zonas geográficas destinadas a la conservación ecológica.
*   **`tree_taxonomy`**: Diccionario botánico sistemático (Especie, Género, Familia y Nombre común).
*   **`planting_activities`**: Relación multifactorial que asocia qué árbol fue plantado, en qué área de vida y en qué fecha exacta.
*   **`forest_censuses`**: Métricas de rendimiento ambiental (cobertura vegetal y estado de salud general).
*   **`documentation`**: Soporte legal, autoridades emisoras y documentación oficial que valida la protección de las áreas de vida.

---

## Arquitectura del Proyecto (Estructura de Carpetas)
A continuación se presenta la jerarquía limpia del código fuente, organizada para separar los datos, la lógica operativa bajo los patrones mencionados y la interfaz de usuario:

```└── prubaproyect
    ├── ecogrow (1).sql
    ├── effective-pom.xml
    ├── pom.xml
    ├── README.md
    ├── src
    │   └── main
    │       └── java
    │           ├── com
    │           │   └── usta
    │           │       ├── App.java
    │           │       ├── controllers
    │           │       │   ├── DashBoardController.java
    │           │       │   ├── DocumentationController.java
    │           │       │   ├── ForestCensusesController.java
    │           │       │   ├── FrontPageController.java
    │           │       │   ├── LivingAreasController.java
    │           │       │   ├── LoginController.java
    │           │       │   ├── MenuController.java
    │           │       │   ├── PanelController.java
    │           │       │   ├── PlantingActivitiesController.java
    │           │       │   ├── SingUpController.java
    │           │       │   └── TreeTaxonomyController.java
    │           │       ├── facade
    │           │       │   └── UserFacade.java
    │           │       ├── models
    │           │       │   ├── documentations
    │           │       │   │   ├── DocumentationImplDAO.java
    │           │       │   │   └── Documentation.java
    │           │       │   ├── forestCensuses
    │           │       │   │   ├── ForestCensusesImplDAO.java
    │           │       │   │   └── ForestCensuses.java
    │           │       │   ├── livingAreas
    │           │       │   │   ├── LivingAreasImplDAO.java
    │           │       │   │   └── LivingAreas.java
    │           │       │   ├── plantingActivities
    │           │       │   │   ├── PlantingActivitiesImplDAO.java
    │           │       │   │   └── PlantingActivities.java
    │           │       │   ├── treetaxonomy
    │           │       │   │   ├── TreeTaxonomyImplDao.java
    │           │       │   │   └── TreeTaxonomy.java
    │           │       │   └── user
    │           │       │       ├── UserImplDAO.java
    │           │       │       └── User.java
    │           │       ├── utils
    │           │       │   ├── ConnectionManager.java
    │           │       │   ├── GenericDao.java
    │           │       │   ├── Session.java
    │           │       │   └── Window.java
    │           │       └── views
    │           │           ├── css
    │           │           │   └── Styles.css
    │           │           ├── Dashboard.fxml
    │           │           ├── Documentation.fxml
    │           │           ├── ForestCensuses.fxml
    │           │           ├── FrontPage.fxml
    │           │           ├── Images
    │           │           │   ├── 10.jpg
    │           │           │   ├── 11.jpg
    │           │           │   ├── 12.jpg
    │           │           │   ├── 2.jpg
    │           │           │   ├── 3.jpg
    │           │           │   ├── 4.jpg
    │           │           │   ├── 5.jpg
    │           │           │   ├── 6.jpg
    │           │           │   ├── 8.jpg
    │           │           │   ├── 9.jpg
    │           │           │   ├── key_13525410.png
    │           │           │   ├── login.png
    │           │           │   └── ONU-portada-1000x562.jpg
    │           │           ├── LivingAreas.fxml
    │           │           ├── login.fxml
    │           │           ├── menu
    │           │           │   ├── AdminMenu.fxml
    │           │           │   └── UserMenu.fxml
    │           │           ├── Panel.fxml
    │           │           ├── PlantingActivities.fxml
    │           │           ├── SingUp.fxml
    │           │           └── tree.fxml
    │           └── module-info.java
    └── target
        ├── bloop-bsp-clients-classes
        │   ├── classes-Metals-EDEOlPmXQ-ukQwLuzgm74Q==
        │   └── test-classes-Metals-EDEOlPmXQ-ukQwLuzgm74Q==
        ├── bloop-internal-classes
        ├── classes
        │   ├── com
        │   │   └── usta
        │   │       ├── App.class
        │   │       ├── controllers
        │   │       │   ├── DashBoardController.class
        │   │       │   ├── DocumentationController.class
        │   │       │   ├── ForestCensusesController.class
        │   │       │   ├── FrontPageController.class
        │   │       │   ├── LivingAreasController.class
        │   │       │   ├── LoginController.class
        │   │       │   ├── MenuController.class
        │   │       │   ├── PanelController.class
        │   │       │   ├── PlantingActivitiesController.class
        │   │       │   ├── SingUpController.class
        │   │       │   └── TreeTaxonomyController.class
        │   │       ├── facade
        │   │       │   └── UserFacade.class
        │   │       ├── models
        │   │       │   ├── documentations
        │   │       │   │   ├── Documentation.class
        │   │       │   │   └── DocumentationImplDAO.class
        │   │       │   ├── forestCensuses
        │   │       │   │   ├── ForestCensuses.class
        │   │       │   │   └── ForestCensusesImplDAO.class
        │   │       │   ├── livingAreas
        │   │       │   │   ├── LivingAreas.class
        │   │       │   │   └── LivingAreasImplDAO.class
        │   │       │   ├── plantingActivities
        │   │       │   │   ├── PlantingActivities.class
        │   │       │   │   └── PlantingActivitiesImplDAO.class
        │   │       │   ├── treetaxonomy
        │   │       │   │   ├── TreeTaxonomy.class
        │   │       │   │   └── TreeTaxonomyImplDao.class
        │   │       │   └── user
        │   │       │       ├── User.class
        │   │       │       └── UserImplDAO.class
        │   │       ├── utils
        │   │       │   ├── ConnectionManager.class
        │   │       │   ├── GenericDao.class
        │   │       │   ├── Session.class
        │   │       │   └── Window.class
        │   │       └── views
        │   │           ├── css
        │   │           │   └── Styles.css
        │   │           ├── Dashboard.fxml
        │   │           ├── Documentation.fxml
        │   │           ├── ForestCensuses.fxml
        │   │           ├── FrontPage.fxml
        │   │           ├── Images
        │   │           │   ├── 10.jpg
        │   │           │   ├── 11.jpg
        │   │           │   ├── 12.jpg
        │   │           │   ├── 2.jpg
        │   │           │   ├── 3.jpg
        │   │           │   ├── 4.jpg
        │   │           │   ├── 5.jpg
        │   │           │   ├── 6.jpg
        │   │           │   ├── 8.jpg
        │   │           │   ├── 9.jpg
        │   │           │   ├── key_13525410.png
        │   │           │   ├── login.png
        │   │           │   └── ONU-portada-1000x562.jpg
        │   │           ├── LivingAreas.fxml
        │   │           ├── login.fxml
        │   │           ├── menu
        │   │           │   ├── AdminMenu.fxml
        │   │           │   └── UserMenu.fxml
        │   │           ├── Panel.fxml
        │   │           ├── PlantingActivities.fxml
        │   │           ├── SingUp.fxml
        │   │           └── tree.fxml
        │   └── module-info.class
        └── test-classes

```

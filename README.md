 ## Arquitectura y Patrones de Diseño Aplicados

Para garantizar que el sistema sea fácil de mantener y escalar, se implementaron dos patrones de diseño estructurales y de creación 
fundamentales en el desarrollo de software profesional:

### 1. Patrón de Diseño: Singleton (Instancia Única)
El patrón **Singleton** se utilizó principalmente en la clase encargada de gestionar la conexión a la base de datos 
(por ejemplo, `DatabaseConnection`).

*   **¿Por qué se usó?** En aplicaciones reales, abrir una conexión nueva a la base de datos por cada clic o petición del usuario destruye el rendimiento del servidor.
*   **¿Cómo funciona en el código?** El patrón restringe la creación de objetos de esa clase a **una sola instancia global**. Cada vez que un controlador o repositorio necesita comunicarse con MariaDB, el sistema reutiliza la misma conexión activa en memoria, optimizando drásticamente el uso de recursos y memoria en el entorno local.

### 2. Patrón de Diseño: Facade (Fachada)
El patrón **Facade** se aplicó en la capa de servicios o controladores lógicos (por ejemplo, una clase unificada como `EcoGrowFacade`,  el controlador principal).

*   **¿Por qué se usó?** La lógica detrás de registrar una plantación requiere tocar múltiples tablas a la vez: verificar el usuario, validar el estado del área de vida y buscar la taxonomía del árbol. Mostrar toda esa complejidad al flujo principal de la interfaz de usuario hace que el código sea difícil de leer.
*   **¿Cómo funciona en el código?** Este patrón actúa como una **interfaz simplificada y unificada** frente a un subsistema complejo. La interfaz de usuario solo llama a un método sencillo (ej. `facade.registrarNuevaPlantacion(...)`), y la Fachada se encarga de coordinar internamente todas las operaciones con los repositorios y modelos de forma oculta para el cliente.


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

# 🌱 EcoGrow — Reforestation & Living Areas Management System

![Java](https://img.shields.io/badge/Java-25_LTS-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-25-5382A1?style=for-the-badge&logo=java&logoColor=white)
![FXML](https://img.shields.io/badge/FXML-Scene_Builder-007ACC?style=for-the-badge)
![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL%20%2F%20MariaDB-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![HikariCP](https://img.shields.io/badge/HikariCP-Connection_Pool-2C3E50?style=for-the-badge)
![BCrypt](https://img.shields.io/badge/BCrypt-Password_Hashing-16A34A?style=for-the-badge)
![Design Patterns](https://img.shields.io/badge/Design_Patterns-GoF-8E44AD?style=for-the-badge)
![Linux](https://img.shields.io/badge/Linux-FCC624?style=for-the-badge&logo=linux&logoColor=black)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)

**EcoGrow** is a desktop application that supports environmental management and the tracking of reforestation and conservation processes across *living areas*.

It centralizes user management, living areas, tree taxonomy, planting activities, forest censuses and legal documentation in a single, modern interface with a dashboard, light/dark themes and role-based access control.

The project started as an academic capstone applying software engineering principles, object-oriented programming, database access and **GoF (Gang of Four)** design patterns. Version 2.0 is a full modernization of the original: new UI, secure authentication, connection pooling, asynchronous data access and a cleaner architecture.

<!-- Add screenshots here, e.g.:
![Dashboard](docs/screenshots/dashboard.png)
![Living areas](docs/screenshots/living-areas.png)
-->

---

##  Features

### Dashboard
- Key metrics: living areas, total hectares, trees planted, registered species, average vegetation coverage and documents.
- Areas grouped by conservation status.
- Average vegetation coverage per area (bar chart).
- Latest planting activities.

### Living areas
Name, location, size in hectares, conservation status (*Critical, Endangered, At risk, Vulnerable, Stable, Improving*) and creation date.

### Tree taxonomy
Catalog of species with common name, scientific name, genus and family.

### Planting activities
Planting, replanting, maintenance, irrigation, pruning and monitoring. Each activity links an area, an optional species, a date, the number of trees planted and a description.

### Forest censuses
Vegetation coverage (%), ecosystem state and field observations per area and date.

### Documentation
Official documents, issuing authority, document number, issue date and the associated living area.

### Users and roles
| Role | Permissions |
|---|---|
| **Administrator** | Full access + user management |
| **Environmental authority** | Create, edit and delete records |
| **Owner** | Read-only |

Public sign-up only allows *Environmental authority* and *Owner*. Administrators can only be created by another administrator.

### User experience
- Resizable window with a sidebar layout.
- Searchable and sortable tables.
- Inline form validation and non-blocking toast notifications.
- Light and dark themes (the choice is remembered).
- Vector icons that follow the theme.
- Database work runs off the UI thread, so the interface never freezes.

---

## Architecture & design patterns

The code follows a separation of concerns between **controllers, models, DAOs, facades, UI helpers and utilities**.

```text
com.usta.ecogrow
├── App / Launcher          JavaFX entry points
├── config/                 AppConfig (properties + environment variables)
├── db/                     Database (Singleton + HikariCP) · Bootstrap (schema + seed)
├── security/               PasswordHasher (BCrypt)
├── model/                  Immutable records + enums (Role, ConservationStatus)
├── dao/                    CrudDao<T> + one DAO per table (plain JDBC)
├── facade/                 UserFacade, DashboardFacade
├── ui/                     Navigator, Session, Toast, Icons, Form, Dialogs, Badges
├── util/                   Async (virtual threads), Fmt, Errors
└── controllers/            CrudController<T> base + one controller per screen
```

### GoF patterns

| Pattern | Where | Purpose |
|---|---|---|
| **Singleton** | `Database`, `Session`, `Navigator` | One controlled instance of the connection pool, the authenticated user and the view router. |
| **Facade** | `UserFacade`, `DashboardFacade` | A simplified API for the UI: authentication, registration and dashboard metrics without exposing DAOs, validation or hashing. |
| **Template Method** | `CrudController<T>` | Defines the flow of every management screen (load, filter, select, validate, save, delete); each screen only implements the specific steps. |

Other principles applied: OOP, separation of concerns, DRY, encapsulation, abstraction, DAO pattern and separation between UI and application logic.

---

## Tech stack

| Technology | Purpose |
|---|---|
| **Java 25 (LTS)** | Language (records, pattern matching, virtual threads) |
| **JavaFX 25** | Desktop UI toolkit |
| **FXML + Scene Builder** | Declarative, visually editable views |
| **JavaFX CSS** | Custom theme with light/dark tokens |
| **MySQL / MariaDB** | Relational database |
| **MySQL Connector/J** | JDBC driver |
| **HikariCP** | JDBC connection pool |
| **BCrypt** (`at.favre.lib:bcrypt`) | Password hashing |
| **SLF4J Simple** | Logging for the pool |
| **Maven** | Build and dependency management |
| **Git / GitHub** | Version control |
| **Arch Linux** | Development environment |

---

## Database

EcoGrow uses **MySQL** (MariaDB also works). The application **creates everything on first launch** — the database, the tables, the initial administrator and optional demo data — so there is no SQL file to import manually.

### Relational model

| Table | Description |
|---|---|
| `users` | Accounts, BCrypt password hashes and roles |
| `living_areas` | Conservation areas (name, location, hectares, status) |
| `tree_species` | Taxonomy of tree species |
| `planting_activities` | Planting work, linked to an area and optionally a species |
| `forest_censuses` | Vegetation coverage and ecosystem state per area |
| `documentation` | Legal documents, optionally linked to an area |

Foreign keys use `ON DELETE` rules to keep referential integrity: deleting an area removes its plantings and censuses, while its documents are kept without an area. The schema lives in [`src/main/resources/db/schema.sql`](src/main/resources/db/schema.sql).

---

## Getting started

### Requirements
- **JDK 25** (or adjust the Java/JavaFX versions in `pom.xml`)
- **Maven 3.9+**
- **MySQL or MariaDB** running locally
- Git

### 1. Clone the repository
```bash
git clone https://github.com/<your-user>/<your-repo>.git
cd <your-repo>
```

### 2. Prepare the database server
Make sure MySQL/MariaDB is running and create a user for the app. The database itself is created automatically.

```sql
CREATE USER 'ecogrow'@'localhost' IDENTIFIED BY 'your-password';
GRANT ALL PRIVILEGES ON ecogrow_v2.* TO 'ecogrow'@'localhost';
FLUSH PRIVILEGES;
```

> **Arch Linux:** `sudo pacman -S mariadb`, then `sudo mariadb-install-db --user=mysql --basedir=/usr --datadir=/var/lib/mysql` and `sudo systemctl enable --now mariadb`.

### 3. Configure the connection
Create an `ecogrow.properties` file in the project root (it is git-ignored). Any key from [`src/main/resources/ecogrow.properties`](src/main/resources/ecogrow.properties) can be overridden:

```properties
db.url=jdbc:mysql://localhost:3306/ecogrow_v2?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false
db.user=ecogrow
db.password=your-password

# Initial administrator (created only if no admin exists)
admin.email=admin@ecogrow.local
admin.password=EcoGrow2026!

# Load demo data when the database is empty
seed.demo=true
```

Environment variables also work: `ECOGROW_DB_URL`, `ECOGROW_DB_USER`, `ECOGROW_DB_PASSWORD`, `ECOGROW_ADMIN_EMAIL`, `ECOGROW_ADMIN_PASSWORD`, `ECOGROW_SEED_DEMO`.

### 4. Run
```bash
mvn clean javafx:run
```

From an IDE, run `com.usta.ecogrow.Launcher` (not `App`) to avoid the *"JavaFX runtime components are missing"* error.

### 5. Sign in
| | |
|---|---|
| **Email** | `admin@ecogrow.local` |
| **Password** | `EcoGrow2026!` |

> Change the default administrator password right after the first login.

---

##  Editing the UI with Scene Builder

The views are plain FXML files in `src/main/resources/com/usta/ecogrow/views/`, so they can still be edited visually.

1. Open any `.fxml` file in **Scene Builder**.
2. Load the theme: *Preview ▸ Scene Style Sheets ▸ Add…* → `css/ecogrow.css`.
3. To add an icon to a button or label, set **Code ▸ User Data** to `icon:add` (or `icon:leaf:28` to set a size). Available icons are listed in `ui/Icons.java`.
4. Keep every `fx:id` and handler name in sync with its controller. All management screens share the ids defined in `CrudController`: `table`, `searchField`, `newButton`, `saveButton`, `deleteButton`, `formTitle`, `countLabel` and `formPane`.

---

## Security

- Passwords are hashed with **BCrypt** (cost 12); plain-text passwords are never stored.
- All SQL uses **prepared statements**.
- Login errors do not reveal whether an email exists, and a dummy hash check equalizes response time.
- Password policy: at least 8 characters, combining letters and numbers.
- Administrator accounts cannot be self-registered, the last administrator cannot be deleted, and users cannot delete their own account.
- Database credentials live outside the source code (properties file or environment variables).

---

## Project structure

```text
EcoGrow/
├── pom.xml
├── README.md
└── src/main
    ├── java/com/usta/ecogrow/
    │   ├── App.java · Launcher.java
    │   ├── config/ · db/ · security/ · model/
    │   ├── dao/ · facade/ · ui/ · util/
    │   └── controllers/
    └── resources/
        ├── ecogrow.properties
        ├── db/                 schema.sql · seed.sql
        └── com/usta/ecogrow/
            ├── css/ecogrow.css
            └── views/          *.fxml
```

---

## What's new in 2.0

- Complete UI redesign: sidebar navigation, dashboard with charts, light/dark theme, toast notifications, resizable window.
- Passwords moved from plain text to **BCrypt**.
- Single shared JDBC connection replaced by a **HikariCP pool**.
- Automatic schema creation and optional demo data (no manual SQL import).
- Asynchronous data access with **virtual threads**.
- Six near-identical controllers replaced by one generic `CrudController<T>`.
- Models rewritten as immutable **records**.
- Role-based access control (administrator, authority, owner).
- Updated dependencies (`mysql-connector-j`, HikariCP, maintained BCrypt library) and Java/JavaFX 25.

---

##  Roadmap

- [ ] Full English localization of the UI.
- [ ] Export reports to PDF / CSV.
- [ ] Map view of living areas.
- [ ] Attach files to documentation records.
- [ ] Unit and integration tests.
- [ ] Native installers with `jpackage`.

---

## Academic project

EcoGrow was developed as an integrative academic project to apply knowledge of object-oriented programming, Java, JavaFX, databases and SQL, software architecture, design patterns and version control.


> **Technology at the service of conservation and reforestation.** 🌿

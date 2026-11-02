-- Esquema EcoGrow v2 (MySQL 8+ / MariaDB 10.4+). Idempotente: se ejecuta en cada arranque.

CREATE TABLE IF NOT EXISTS users (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    email         VARCHAR(120) NOT NULL,
    password_hash VARCHAR(100) NOT NULL,
    role          VARCHAR(20)  NOT NULL DEFAULT 'OWNER',
    document      VARCHAR(50)  NOT NULL DEFAULT '',
    phone         VARCHAR(20)  NOT NULL DEFAULT '',
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_users_email UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS living_areas (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(120) NOT NULL,
    location            VARCHAR(255) NOT NULL,
    hectares            DECIMAL(12,2) NOT NULL DEFAULT 0,
    conservation_status VARCHAR(20)  NOT NULL DEFAULT 'STABLE',
    creation_date       DATE NULL,
    INDEX idx_areas_status (conservation_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS tree_species (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    species     VARCHAR(100) NOT NULL,
    genus       VARCHAR(100) NOT NULL DEFAULT '',
    family      VARCHAR(100) NOT NULL DEFAULT '',
    common_name VARCHAR(100) NOT NULL DEFAULT '',
    CONSTRAINT uq_species UNIQUE (species)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS planting_activities (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    activity_date  DATE NOT NULL,
    activity_type  VARCHAR(50) NOT NULL,
    description    TEXT NULL,
    trees_planted  INT NOT NULL DEFAULT 0,
    living_area_id INT NOT NULL,
    tree_id        INT NULL,
    INDEX idx_plantings_date (activity_date),
    CONSTRAINT fk_plantings_area FOREIGN KEY (living_area_id) REFERENCES living_areas(id) ON DELETE CASCADE,
    CONSTRAINT fk_plantings_tree FOREIGN KEY (tree_id) REFERENCES tree_species(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS forest_censuses (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    census_date         DATE NOT NULL,
    vegetation_coverage DECIMAL(5,2) NOT NULL,
    ecosystem_state     VARCHAR(60) NOT NULL,
    observations        TEXT NULL,
    living_area_id      INT NOT NULL,
    INDEX idx_censuses_date (census_date),
    CONSTRAINT fk_censuses_area FOREIGN KEY (living_area_id) REFERENCES living_areas(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS documentation (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    document_type     VARCHAR(100) NOT NULL,
    document_number   VARCHAR(50)  NOT NULL,
    issue_date        DATE NULL,
    issuing_authority VARCHAR(120) NOT NULL DEFAULT '',
    living_area_id    INT NULL,
    CONSTRAINT fk_docs_area FOREIGN KEY (living_area_id) REFERENCES living_areas(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

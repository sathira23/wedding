-- Create the application database and schema for Wedding Package Management
CREATE DATABASE IF NOT EXISTS wedding_package_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE wedding_package_management;

CREATE TABLE IF NOT EXISTS event (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS venue (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS wedding_package (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    tier ENUM('BASIC','PREMIUM','LUXURY') NOT NULL,
    price DECIMAL(12,2) NOT NULL,
    inclusions VARCHAR(500) NOT NULL,
    status ENUM('DRAFT','ACTIVE','ARCHIVED') NOT NULL DEFAULT 'DRAFT',
    linked_event_id INT NULL,
    linked_venue_id INT NULL,
    organizer_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_package_event FOREIGN KEY (linked_event_id) REFERENCES event(id) ON DELETE SET NULL,
    CONSTRAINT fk_package_venue FOREIGN KEY (linked_venue_id) REFERENCES venue(id) ON DELETE SET NULL
) ENGINE=InnoDB;

INSERT IGNORE INTO event (name) VALUES
('Beach Celebration'),
('Garden Ceremony'),
('Ballroom Reception');

INSERT IGNORE INTO venue (name) VALUES
('Rosewood Hall'),
('Sunset Terrace'),
('Emerald Garden');

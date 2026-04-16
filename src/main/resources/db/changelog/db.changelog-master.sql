-- liquibase formatted sql

-- changeset system:1
CREATE TABLE IF NOT EXISTS initial_system_baseline (
    id INT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

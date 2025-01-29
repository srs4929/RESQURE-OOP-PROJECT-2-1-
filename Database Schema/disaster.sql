CREATE DATABASE disaster;
USE disaster;
CREATE TABLE contact (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL,
    contact VARCHAR(20) NOT NULL,
    location VARCHAR(100) NOT NULL
);
CREATE TABLE disaster_report (
    report_id INT AUTO_INCREMENT PRIMARY KEY,
    location VARCHAR(100) NOT NULL,
    disaster_type VARCHAR(50) NOT NULL,
    severity ENUM('Mild', 'Moderate', 'High', 'Severe') NOT NULL,
    status VARCHAR(50) NOT NULL,
    date_reported DATE NOT NULL
);
CREATE TABLE users (
    UserId INT AUTO_INCREMENT PRIMARY KEY,
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('Admin', 'Volunteer', 'Client') NOT NULL
);
CREATE TABLE volunteer_task (
    task_id INT AUTO_INCREMENT PRIMARY KEY,
    task_title VARCHAR(255) NOT NULL,
    task_description TEXT,
    location VARCHAR(255),
    assigned_date DATE,
    status VARCHAR(50),
    disaster_type VARCHAR(255)
);



-- Group 1: Shuttle Management System - Database Creation Script
-- Members: [Your Names Here]
-- Date: November 3, 2025
-- Course: ISTE-330 Database Connectivity and Access

DROP DATABASE IF EXISTS ShuttleManagementDB;
CREATE DATABASE ShuttleManagementDB;
USE ShuttleManagementDB;

-- Table 1: USERS - stores user accounts, roles, and credentials
CREATE TABLE USERS (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(64) NOT NULL,  -- SHA-256 hash
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    role ENUM('Admin', 'Driver', 'Student') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    INDEX idx_username (username),
    INDEX idx_role (role)
) ENGINE=InnoDB;

-- Table 2: ACCOUNTS - stores student ride credits
CREATE TABLE ACCOUNTS (
    account_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    balance DECIMAL(10, 2) DEFAULT 0.00,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES USERS(user_id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_account (user_id),
    CHECK (balance >= 0)
) ENGINE=InnoDB;

-- Table 3: SHUTTLE - details of each bus and assigned driver
CREATE TABLE SHUTTLE (
    shuttle_id INT AUTO_INCREMENT PRIMARY KEY,
    shuttle_number VARCHAR(20) UNIQUE NOT NULL,
    license_plate VARCHAR(20) UNIQUE NOT NULL,
    capacity INT NOT NULL,
    driver_id INT,
    status ENUM('Active', 'Maintenance', 'Inactive') DEFAULT 'Active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (driver_id) REFERENCES USERS(user_id) ON DELETE SET NULL,
    INDEX idx_driver (driver_id),
    INDEX idx_status (status),
    CHECK (capacity > 0)
) ENGINE=InnoDB;

-- Table 4: ROUTE - defines shuttle routes and their basic info
CREATE TABLE ROUTE (
    route_id INT AUTO_INCREMENT PRIMARY KEY,
    route_name VARCHAR(100) NOT NULL,
    route_code VARCHAR(20) UNIQUE NOT NULL,
    description TEXT,
    distance_km DECIMAL(5, 2),
    estimated_duration_minutes INT,
    credits_required DECIMAL(5, 2) DEFAULT 1.00,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_route_code (route_code),
    CHECK (distance_km >= 0),
    CHECK (estimated_duration_minutes > 0),
    CHECK (credits_required >= 0)
) ENGINE=InnoDB;

-- Table 5: STOP - individual stops linked to routes
CREATE TABLE STOP (
    stop_id INT AUTO_INCREMENT PRIMARY KEY,
    route_id INT NOT NULL,
    stop_name VARCHAR(100) NOT NULL,
    stop_order INT NOT NULL,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    estimated_arrival_time TIME,
    FOREIGN KEY (route_id) REFERENCES ROUTE(route_id) ON DELETE CASCADE,
    INDEX idx_route (route_id),
    UNIQUE KEY unique_route_stop_order (route_id, stop_order)
) ENGINE=InnoDB;

-- Table 6: SHUTTLE_LOCATION - real-time shuttle position updates
CREATE TABLE SHUTTLE_LOCATION (
    location_id INT AUTO_INCREMENT PRIMARY KEY,
    shuttle_id INT NOT NULL,
    latitude DECIMAL(10, 8) NOT NULL,
    longitude DECIMAL(11, 8) NOT NULL,
    speed_kmh DECIMAL(5, 2) DEFAULT 0.00,
    heading INT,  -- 0-359 degrees
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (shuttle_id) REFERENCES SHUTTLE(shuttle_id) ON DELETE CASCADE,
    INDEX idx_shuttle_time (shuttle_id, timestamp),
    CHECK (speed_kmh >= 0),
    CHECK (heading >= 0 AND heading <= 359)
) ENGINE=InnoDB;

-- Table 7: STUDENT_ROUTE_REGISTRATION - student-route enrollment records
CREATE TABLE STUDENT_ROUTE_REGISTRATION (
    registration_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    route_id INT NOT NULL,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expiry_date DATE,
    status ENUM('Active', 'Expired', 'Cancelled') DEFAULT 'Active',
    credits_paid DECIMAL(5, 2) NOT NULL,
    FOREIGN KEY (student_id) REFERENCES USERS(user_id) ON DELETE CASCADE,
    FOREIGN KEY (route_id) REFERENCES ROUTE(route_id) ON DELETE CASCADE,
    INDEX idx_student (student_id),
    INDEX idx_route (route_id),
    INDEX idx_status (status),
    UNIQUE KEY unique_active_registration (student_id, route_id, status)
) ENGINE=InnoDB;

-- Table 8: SHUTTLE_SCHEDULE - timetable for each route
CREATE TABLE SHUTTLE_SCHEDULE (
    schedule_id INT AUTO_INCREMENT PRIMARY KEY,
    shuttle_id INT NOT NULL,
    route_id INT NOT NULL,
    day_of_week ENUM('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday') NOT NULL,
    departure_time TIME NOT NULL,
    arrival_time TIME NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (shuttle_id) REFERENCES SHUTTLE(shuttle_id) ON DELETE CASCADE,
    FOREIGN KEY (route_id) REFERENCES ROUTE(route_id) ON DELETE CASCADE,
    INDEX idx_shuttle (shuttle_id),
    INDEX idx_route (route_id),
    INDEX idx_day (day_of_week)
) ENGINE=InnoDB;

-- ============================================
-- POPULATE DATABASE WITH SAMPLE DATA
-- ============================================

-- Insert Users (passwords are SHA-256 hashes)
-- Admin users: password = "admin123" -> hash
-- Driver users: password = "driver123" -> hash
-- Student users: password = "student123" -> hash

INSERT INTO USERS (username, password_hash, full_name, email, phone, role) VALUES
-- Admins (password: admin123)
('admin1', SHA2('admin123', 256), 'John Administrator', 'admin1@university.edu', '555-0101', 'Admin'),
('admin2', SHA2('admin123', 256), 'Sarah Manager', 'admin2@university.edu', '555-0102', 'Admin'),

-- Drivers (password: driver123)
('driver1', SHA2('driver123', 256), 'Mike Driver', 'driver1@university.edu', '555-0201', 'Driver'),
('driver2', SHA2('driver123', 256), 'Lisa Transport', 'driver2@university.edu', '555-0202', 'Driver'),
('driver3', SHA2('driver123', 256), 'Tom Wheeler', 'driver3@university.edu', '555-0203', 'Driver'),

-- Students (password: student123)
('student1', SHA2('student123', 256), 'Alice Johnson', 'alice.j@student.edu', '555-0301', 'Student'),
('student2', SHA2('student123', 256), 'Bob Smith', 'bob.s@student.edu', '555-0302', 'Student'),
('student3', SHA2('student123', 256), 'Carol Williams', 'carol.w@student.edu', '555-0303', 'Student'),
('student4', SHA2('student123', 256), 'David Brown', 'david.b@student.edu', '555-0304', 'Student'),
('student5', SHA2('student123', 256), 'Emma Davis', 'emma.d@student.edu', '555-0305', 'Student');

-- Insert Accounts for Students
INSERT INTO ACCOUNTS (user_id, balance) VALUES
(6, 100.00),  -- Alice
(7, 75.50),   -- Bob
(8, 150.00),  -- Carol
(9, 50.00),   -- David
(10, 200.00); -- Emma

-- Insert Shuttles
INSERT INTO SHUTTLE (shuttle_number, license_plate, capacity, driver_id, status) VALUES
('BUS-001', 'ABC-1234', 45, 3, 'Active'),
('BUS-002', 'ABC-2345', 50, 4, 'Active'),
('BUS-003', 'ABC-3456', 40, 5, 'Active'),
('BUS-004', 'ABC-4567', 45, NULL, 'Maintenance');

-- Insert Routes
INSERT INTO ROUTE (route_name, route_code, description, distance_km, estimated_duration_minutes, credits_required, is_active) VALUES
('Campus to Downtown', 'R001', 'Main route from campus to downtown area', 12.5, 35, 2.00, TRUE),
('Campus to Airport', 'R002', 'Express route to international airport', 25.0, 45, 5.00, TRUE),
('Campus Loop', 'R003', 'Internal campus circulation route', 5.0, 20, 1.00, TRUE),
('Residential Area Route', 'R004', 'Connects campus to student housing areas', 8.5, 25, 1.50, TRUE);

-- Insert Stops for Route 1 (Campus to Downtown)
INSERT INTO STOP (route_id, stop_name, stop_order, latitude, longitude, estimated_arrival_time) VALUES
(1, 'Main Campus Gate', 1, 40.7128, -74.0060, '08:00:00'),
(1, 'Library Junction', 2, 40.7138, -74.0070, '08:05:00'),
(1, 'Science Building', 3, 40.7148, -74.0080, '08:10:00'),
(1, 'Student Center', 4, 40.7158, -74.0090, '08:15:00'),
(1, 'Downtown Plaza', 5, 40.7168, -74.0100, '08:35:00');

-- Insert Stops for Route 2 (Campus to Airport)
INSERT INTO STOP (route_id, stop_name, stop_order, latitude, longitude, estimated_arrival_time) VALUES
(2, 'Main Campus Gate', 1, 40.7128, -74.0060, '09:00:00'),
(2, 'Highway Junction', 2, 40.7200, -74.0150, '09:15:00'),
(2, 'Airport Terminal 1', 3, 40.7500, -74.0500, '09:45:00');

-- Insert Stops for Route 3 (Campus Loop)
INSERT INTO STOP (route_id, stop_name, stop_order, latitude, longitude, estimated_arrival_time) VALUES
(3, 'North Campus', 1, 40.7128, -74.0060, '10:00:00'),
(3, 'Engineering Building', 2, 40.7135, -74.0065, '10:05:00'),
(3, 'Sports Complex', 3, 40.7142, -74.0070, '10:10:00'),
(3, 'South Campus', 4, 40.7150, -74.0075, '10:15:00');

-- Insert Shuttle Locations (recent positions)
INSERT INTO SHUTTLE_LOCATION (shuttle_id, latitude, longitude, speed_kmh, heading, timestamp) VALUES
(1, 40.7138, -74.0070, 35.5, 90, NOW() - INTERVAL 5 MINUTE),
(1, 40.7148, -74.0080, 40.0, 85, NOW() - INTERVAL 2 MINUTE),
(1, 40.7158, -74.0090, 30.5, 95, NOW()),
(2, 40.7200, -74.0150, 60.0, 180, NOW() - INTERVAL 10 MINUTE),
(2, 40.7350, -74.0300, 65.5, 175, NOW()),
(3, 40.7135, -74.0065, 25.0, 45, NOW());

-- Insert Student Route Registrations
INSERT INTO STUDENT_ROUTE_REGISTRATION (student_id, route_id, registration_date, expiry_date, status, credits_paid) VALUES
(6, 1, NOW() - INTERVAL 10 DAY, DATE_ADD(CURDATE(), INTERVAL 20 DAY), 'Active', 2.00),
(6, 3, NOW() - INTERVAL 5 DAY, DATE_ADD(CURDATE(), INTERVAL 25 DAY), 'Active', 1.00),
(7, 2, NOW() - INTERVAL 15 DAY, DATE_ADD(CURDATE(), INTERVAL 15 DAY), 'Active', 5.00),
(8, 1, NOW() - INTERVAL 20 DAY, DATE_ADD(CURDATE(), INTERVAL 10 DAY), 'Active', 2.00),
(8, 4, NOW() - INTERVAL 8 DAY, DATE_ADD(CURDATE(), INTERVAL 22 DAY), 'Active', 1.50),
(9, 3, NOW() - INTERVAL 12 DAY, DATE_ADD(CURDATE(), INTERVAL 18 DAY), 'Active', 1.00);

-- Insert Shuttle Schedules
INSERT INTO SHUTTLE_SCHEDULE (shuttle_id, route_id, day_of_week, departure_time, arrival_time, is_active) VALUES
-- Route 1 schedules
(1, 1, 'Monday', '08:00:00', '08:35:00', TRUE),
(1, 1, 'Tuesday', '08:00:00', '08:35:00', TRUE),
(1, 1, 'Wednesday', '08:00:00', '08:35:00', TRUE),
(1, 1, 'Thursday', '08:00:00', '08:35:00', TRUE),
(1, 1, 'Friday', '08:00:00', '08:35:00', TRUE),

-- Route 2 schedules
(2, 2, 'Monday', '09:00:00', '09:45:00', TRUE),
(2, 2, 'Wednesday', '09:00:00', '09:45:00', TRUE),
(2, 2, 'Friday', '09:00:00', '09:45:00', TRUE),

-- Route 3 schedules
(3, 3, 'Monday', '10:00:00', '10:20:00', TRUE),
(3, 3, 'Tuesday', '10:00:00', '10:20:00', TRUE),
(3, 3, 'Wednesday', '10:00:00', '10:20:00', TRUE),
(3, 3, 'Thursday', '10:00:00', '10:20:00', TRUE),
(3, 3, 'Friday', '10:00:00', '10:20:00', TRUE);

-- Create views for easier data access
CREATE VIEW vw_active_students AS
SELECT u.user_id, u.username, u.full_name, u.email, a.balance
FROM USERS u
JOIN ACCOUNTS a ON u.user_id = a.user_id
WHERE u.role = 'Student' AND u.is_active = TRUE;

CREATE VIEW vw_shuttle_status AS
SELECT s.shuttle_id, s.shuttle_number, s.license_plate, s.capacity, 
       u.full_name AS driver_name, s.status
FROM SHUTTLE s
LEFT JOIN USERS u ON s.driver_id = u.user_id;

CREATE VIEW vw_latest_shuttle_locations AS
SELECT sl1.shuttle_id, s.shuttle_number, sl1.latitude, sl1.longitude, 
       sl1.speed_kmh, sl1.timestamp
FROM SHUTTLE_LOCATION sl1
INNER JOIN (
    SELECT shuttle_id, MAX(timestamp) AS max_timestamp
    FROM SHUTTLE_LOCATION
    GROUP BY shuttle_id
) sl2 ON sl1.shuttle_id = sl2.shuttle_id AND sl1.timestamp = sl2.max_timestamp
JOIN SHUTTLE s ON sl1.shuttle_id = s.shuttle_id;

-- Grant permissions (adjust as needed for your MariaDB setup)
-- GRANT SELECT ON ShuttleManagementDB.* TO 'student_user'@'localhost';
-- GRANT SELECT, INSERT, UPDATE ON ShuttleManagementDB.* TO 'driver_user'@'localhost';
-- GRANT ALL PRIVILEGES ON ShuttleManagementDB.* TO 'admin_user'@'localhost';

-- Display confirmation message
SELECT 'Database ShuttleManagementDB created and populated successfully!' AS Status;

-- Group 2: Shuttle Management System - Stored Procedures Script
-- Members: [Your Names Here]
-- Date: November 3, 2025
-- Course: ISTE-330 Database Connectivity and Access
USE ShuttleManagementDB;
-- Drop existing procedures if they exist
DROP PROCEDURE IF EXISTS sp_display_table;
DROP PROCEDURE IF EXISTS sp_transfer_credits;
DROP PROCEDURE IF EXISTS sp_register_for_route;
DROP PROCEDURE IF EXISTS sp_update_shuttle_location;
DELIMITER $$ -- ============================================
-- Stored Procedure 1: Display any table by name
-- ============================================
CREATE PROCEDURE sp_display_table(IN table_name VARCHAR(100)) BEGIN
DECLARE table_exists INT DEFAULT 0;
-- Check if table exists
SELECT COUNT(*) INTO table_exists
FROM information_schema.tables
WHERE table_schema = 'ShuttleManagementDB'
    AND table_name = table_name;
IF table_exists = 0 THEN SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'Table does not exist in database';
ELSE -- Use prepared statement to dynamically query the table
SET @query = CONCAT('SELECT * FROM ', table_name);
PREPARE stmt
FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END IF;
END $$ -- ============================================
-- Stored Procedure 2: Transfer Credits Between Students
-- ============================================
CREATE PROCEDURE sp_transfer_credits(
    IN source_user_id INT,
    IN destination_user_id INT,
    IN transfer_amount DECIMAL(10, 2),
    OUT result_message VARCHAR(255)
) BEGIN
DECLARE source_balance DECIMAL(10, 2);
DECLARE dest_exists INT;
DECLARE source_role VARCHAR(20);
DECLARE dest_role VARCHAR(20);
DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN -- Rollback on any error
    ROLLBACK;
SET result_message = 'ERROR: Transaction failed and was rolled back';
END;
-- Validate transfer amount
IF transfer_amount <= 0 THEN
SET result_message = 'ERROR: Transfer amount must be greater than 0';
SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'Transfer amount must be positive';
END IF;
-- Start transaction
START TRANSACTION;
-- Check if source user exists and is a student
SELECT role INTO source_role
FROM USERS
WHERE user_id = source_user_id
    AND is_active = TRUE;
IF source_role IS NULL THEN ROLLBACK;
SET result_message = 'ERROR: Source user does not exist or is inactive';
SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'Source user invalid';
END IF;
IF source_role != 'Student' THEN ROLLBACK;
SET result_message = 'ERROR: Source user must be a Student';
SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'Only students can transfer credits';
END IF;
-- Check if destination user exists and is a student
SELECT role INTO dest_role
FROM USERS
WHERE user_id = destination_user_id
    AND is_active = TRUE;
IF dest_role IS NULL THEN ROLLBACK;
SET result_message = 'ERROR: Destination user does not exist or is inactive';
SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'Destination user invalid';
END IF;
IF dest_role != 'Student' THEN ROLLBACK;
SET result_message = 'ERROR: Destination user must be a Student';
SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'Can only transfer to students';
END IF;
-- Check if source and destination are the same
IF source_user_id = destination_user_id THEN ROLLBACK;
SET result_message = 'ERROR: Cannot transfer credits to yourself';
SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'Self-transfer not allowed';
END IF;
-- Deduct from source account
UPDATE ACCOUNTS
SET balance = balance - transfer_amount
WHERE user_id = source_user_id;
-- Add to destination account
UPDATE ACCOUNTS
SET balance = balance + transfer_amount
WHERE user_id = destination_user_id;
-- Check if source account balance is negative
SELECT balance INTO source_balance
FROM ACCOUNTS
WHERE user_id = source_user_id;
IF source_balance < 0 THEN ROLLBACK;
SET result_message = 'ERROR: Insufficient balance in source account';
SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'Insufficient funds';
ELSE COMMIT;
SET result_message = CONCAT(
        'SUCCESS: Transferred ',
        transfer_amount,
        ' credits successfully'
    );
END IF;
END $$ -- ============================================
-- Stored Procedure 3: Register Student for Route
-- ============================================
CREATE PROCEDURE sp_register_for_route(
    IN p_student_id INT,
    IN p_route_id INT,
    OUT result_message VARCHAR(255)
) BEGIN
DECLARE student_balance DECIMAL(10, 2);
DECLARE route_cost DECIMAL(5, 2);
DECLARE student_role VARCHAR(20);
DECLARE route_active BOOLEAN;
DECLARE existing_reg INT;
DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN ROLLBACK;
SET result_message = 'ERROR: Registration failed and was rolled back';
END;
-- Start transaction
START TRANSACTION;
-- Verify student exists and is active
SELECT role INTO student_role
FROM USERS
WHERE user_id = p_student_id
    AND is_active = TRUE;
IF student_role IS NULL
OR student_role != 'Student' THEN ROLLBACK;
SET result_message = 'ERROR: Invalid student ID';
SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'Student not found';
END IF;
-- Verify route exists and is active
SELECT is_active,
    credits_required INTO route_active,
    route_cost
FROM ROUTE
WHERE route_id = p_route_id;
IF route_active IS NULL
OR route_active = FALSE THEN ROLLBACK;
SET result_message = 'ERROR: Route does not exist or is inactive';
SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'Route not available';
END IF;
-- Check if student is already registered for this route
SELECT COUNT(*) INTO existing_reg
FROM STUDENT_ROUTE_REGISTRATION
WHERE student_id = p_student_id
    AND route_id = p_route_id
    AND status = 'Active';
IF existing_reg > 0 THEN ROLLBACK;
SET result_message = 'ERROR: Student is already registered for this route';
SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'Already registered';
END IF;
-- Get student balance
SELECT balance INTO student_balance
FROM ACCOUNTS
WHERE user_id = p_student_id;
IF student_balance < route_cost THEN ROLLBACK;
SET result_message = 'ERROR: Insufficient credits for registration';
SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'Insufficient credits';
END IF;
-- Deduct credits from student account
UPDATE ACCOUNTS
SET balance = balance - route_cost
WHERE user_id = p_student_id;
-- Register student for route (30 days validity)
INSERT INTO STUDENT_ROUTE_REGISTRATION (
        student_id,
        route_id,
        expiry_date,
        status,
        credits_paid
    )
VALUES (
        p_student_id,
        p_route_id,
        DATE_ADD(CURDATE(), INTERVAL 30 DAY),
        'Active',
        route_cost
    );
COMMIT;
SET result_message = CONCAT(
        'SUCCESS: Student registered for route. Credits deducted: ',
        route_cost
    );
END $$ -- ============================================
-- Stored Procedure 4: Update Shuttle Location (for drivers)
-- ============================================
CREATE PROCEDURE sp_update_shuttle_location(
    IN p_shuttle_id INT,
    IN p_latitude DECIMAL(10, 8),
    IN p_longitude DECIMAL(11, 8),
    IN p_speed_kmh DECIMAL(5, 2),
    IN p_heading INT,
    OUT result_message VARCHAR(255)
) BEGIN
DECLARE shuttle_exists INT;
-- Validate shuttle exists
SELECT COUNT(*) INTO shuttle_exists
FROM SHUTTLE
WHERE shuttle_id = p_shuttle_id
    AND status = 'Active';
IF shuttle_exists = 0 THEN
SET result_message = 'ERROR: Shuttle does not exist or is not active';
SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'Invalid shuttle';
END IF;
-- Validate coordinates
IF p_latitude < -90
OR p_latitude > 90
OR p_longitude < -180
OR p_longitude > 180 THEN
SET result_message = 'ERROR: Invalid GPS coordinates';
SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'Invalid coordinates';
END IF;
-- Validate speed and heading
IF p_speed_kmh < 0
OR p_heading < 0
OR p_heading > 359 THEN
SET result_message = 'ERROR: Invalid speed or heading values';
SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'Invalid speed/heading';
END IF;
-- Insert new location record
INSERT INTO SHUTTLE_LOCATION (
        shuttle_id,
        latitude,
        longitude,
        speed_kmh,
        heading,
        timestamp
    )
VALUES (
        p_shuttle_id,
        p_latitude,
        p_longitude,
        p_speed_kmh,
        p_heading,
        NOW()
    );
SET result_message = 'SUCCESS: Shuttle location updated';
END $$ DELIMITER;
-- Test the stored procedures
-- SELECT 'Stored procedures created successfully!' AS Status;
-- Example usage (commented out):
-- CALL sp_display_table('USERS');
-- CALL sp_transfer_credits(6, 7, 10.00, @msg);
-- SELECT @msg;
-- CALL sp_register_for_route(10, 1, @msg);
-- SELECT @msg;
-- CALL sp_update_shuttle_location(1, 40.7200, -74.0150, 45.5, 90, @msg);
-- SELECT @msg;
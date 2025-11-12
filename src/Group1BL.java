// Group 1: Shuttle Management System - Business Layer
// Members: 
// Date: November 3, 2025
// Course: ISTE-330 Database Connectivity and Access

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import model.*;

/**
 * Business Layer - Contains business logic for the Shuttle Management System
 * Mediates between Presentation Layer and Data Layer
 */
public class Group1BL {
    private Group1DL dataLayer;

    /**
     * Constructor - initializes data layer
     */
    public Group1BL() {
        this.dataLayer = new Group1DL();
    }

    /**
     * Constructor with custom database credentials
     */
    public Group1BL(String dbUrl, String dbUser, String dbPassword) {
        this.dataLayer = new Group1DL(dbUrl, dbUser, dbPassword);
    }

    /**
     * Core Requirement 1: Connect to database
     */
    public boolean connect() {
        return dataLayer.connectDB();
    }

    /**
     * Core Requirement 1: Disconnect from database
     */
    public boolean disconnect() {
        return dataLayer.disconnectDB();
    }

    /**
     * Check if connected
     */
    public boolean isConnected() {
        return dataLayer.isConnected();
    }

    /**
     * Core Requirement 2: Authenticate user login
     * Returns user information map if successful, null otherwise
     */
    public Map<String, Object> authenticateUser(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            System.err.println("Username cannot be empty");
            return null;
        }
        if (password == null || password.trim().isEmpty()) {
            System.err.println("Password cannot be empty");
            return null;
        }

        return dataLayer.login(username, password);
    }

    /**
     * Core Requirement 3: Admin adds a new client (student)
     */
    public boolean addNewClient(String username, String password, String fullName, 
                               String email, String phone) {
        // Validate inputs
        if (!validateUserInputs(username, password, fullName, email)) {
            return false;
        }

        return dataLayer.addUser(username, password, fullName, email, phone, "Student");
    }

    /**
     * Core Requirement 4: Admin adds a new admin
     */
    public boolean addNewAdmin(String username, String password, String fullName, 
                              String email, String phone) {
        // Validate inputs
        if (!validateUserInputs(username, password, fullName, email)) {
            return false;
        }

        return dataLayer.addUser(username, password, fullName, email, phone, "Admin");
    }

    /**
     * Core Requirement 5: Add a new driver
     */
    public boolean addNewDriver(String username, String password, String fullName, 
                               String email, String phone) {
        // Validate inputs
        if (!validateUserInputs(username, password, fullName, email)) {
            return false;
        }

        return dataLayer.addUser(username, password, fullName, email, phone, "Driver");
    }

    /**
     * Core Requirement 6: Transfer credits between students
     * This is a critical business operation with validation
     */
    public String transferCredits(int sourceUserId, int destUserId, double amount) {
        // Validate amount
        if (amount <= 0) {
            return "ERROR: Transfer amount must be greater than zero";
        }

        if (amount > 10000) {
            return "ERROR: Transfer amount exceeds maximum limit (10000 credits)";
        }

        // Round to 2 decimal places
        amount = Math.round(amount * 100.0) / 100.0;

        // Check if source has sufficient balance
        double sourceBalance = dataLayer.getAccountBalance(sourceUserId);
        if (sourceBalance < 0) {
            return "ERROR: Could not retrieve source account balance";
        }

        if (sourceBalance < amount) {
            return String.format("ERROR: Insufficient balance. Available: %.2f, Required: %.2f", 
                               sourceBalance, amount);
        }

        // Perform transfer
        return dataLayer.transferCredits(sourceUserId, destUserId, amount);
    }

    /**
     * Core Requirement 7: View account information for a student
     */
    public double viewAccountBalance(int userId) {
        return dataLayer.getAccountBalance(userId);
    }

    /**
     * Core Requirement 8: Student registers for a route
     */
    public String registerStudentForRoute(int studentId, int routeId) {
        if (routeId <= 0) {
            return "ERROR: Invalid route ID";
        }

        return dataLayer.registerForRoute(studentId, routeId);
    }

    /**
     * Core Requirement 9: View all available routes
     */
    public ResultSet viewAvailableRoutes() {
        return dataLayer.getAllRoutes();
    }

    /**
     * Core Requirement 10: View student's registered routes
     */
    public ResultSet viewStudentRegistrations(int studentId) {
        return dataLayer.getStudentRoutes(studentId);
    }

    /**
     * Core Requirement 11: View real-time shuttle locations
     */
    public ResultSet viewShuttleLocations() {
        return dataLayer.getShuttleLocations();
    }

    /**
     * Core Requirement 12: Driver updates shuttle location
     */
    public String updateLocation(int shuttleId, double latitude, double longitude, 
                                double speedKmh, int heading) {
        // Validate GPS coordinates
        if (latitude < -90 || latitude > 90) {
            return "ERROR: Invalid latitude. Must be between -90 and 90";
        }

        if (longitude < -180 || longitude > 180) {
            return "ERROR: Invalid longitude. Must be between -180 and 180";
        }

        // Validate speed
        if (speedKmh < 0 || speedKmh > 200) {
            return "ERROR: Invalid speed. Must be between 0 and 200 km/h";
        }

        // Validate heading
        if (heading < 0 || heading > 359) {
            return "ERROR: Invalid heading. Must be between 0 and 359 degrees";
        }

        return dataLayer.updateShuttleLocation(shuttleId, latitude, longitude, speedKmh, heading);
    }

    /**
     * Core Requirement 13: Admin views any table
     */
    public ResultSet viewTable(String tableName) {
        // Validate table name (prevent SQL injection)
        if (!isValidTableName(tableName)) {
            System.err.println("ERROR: Invalid table name");
            return null;
        }

        return dataLayer.displayTable(tableName);
    }

    /**
     * Core Requirement 14: Get all users (for admin view)
     */
    public ResultSet getAllUsers() {
        return dataLayer.getAllUsers();
    }

    /**
     * Core Requirement 15: Get all students with account info
     */
    public ResultSet getAllStudentsWithAccounts() {
        return dataLayer.getAllStudentsWithAccounts();
    }

    /**
     * Get shuttle assigned to driver
     */
    public ResultSet getDriverShuttle(int driverId) {
        return dataLayer.getDriverShuttle(driverId);
    }

    /**
     * Get stops for a specific route
     */
    public ResultSet getRouteStops(int routeId) {
        return dataLayer.getRouteStops(routeId);
    }

    /**
     * Get shuttle schedules for a route
     */
    public ResultSet getRouteSchedules(int routeId) {
        return dataLayer.getShuttleSchedules(routeId);
    }

    /**
     * Validate user input fields
     */
    private boolean validateUserInputs(String username, String password, 
                                      String fullName, String email) {
        if (username == null || username.trim().isEmpty()) {
            System.err.println("Username cannot be empty");
            return false;
        }

        if (username.length() < 3) {
            System.err.println("Username must be at least 3 characters");
            return false;
        }

        if (password == null || password.trim().isEmpty()) {
            System.err.println("Password cannot be empty");
            return false;
        }

        if (password.length() < 6) {
            System.err.println("Password must be at least 6 characters");
            return false;
        }

        if (fullName == null || fullName.trim().isEmpty()) {
            System.err.println("Full name cannot be empty");
            return false;
        }

        if (email == null || email.trim().isEmpty()) {
            System.err.println("Email cannot be empty");
            return false;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            System.err.println("Invalid email format");
            return false;
        }

        return true;
    }

    /**
     * Validate table name to prevent SQL injection
     */
    private boolean isValidTableName(String tableName) {
        if (tableName == null || tableName.trim().isEmpty()) {
            return false;
        }

        // Only allow alphanumeric and underscore
        return tableName.matches("^[a-zA-Z0-9_]+$");
    }

    /**
     * Format currency value
     */
    public String formatCurrency(double amount) {
        return String.format("%.2f", amount);
    }

    /**
     * Get data layer instance (if needed for direct access)
     */
    public Group1DL getDataLayer() {
        return dataLayer;
    }

    /**
     * Validate if user has sufficient permissions for an action
     */
    public boolean hasPermission(String userRole, String action) {
        switch (action) {
            case "ADD_USER":
            case "VIEW_ALL_TABLES":
            case "VIEW_ALL_USERS":
                return userRole.equals("Admin");

            case "UPDATE_LOCATION":
                return userRole.equals("Driver");

            case "TRANSFER_CREDITS":
            case "REGISTER_ROUTE":
            case "VIEW_ROUTES":
                return userRole.equals("Student");

            default:
                return false;
        }
    }

    /**
     * Generate report of student activity (for admin)
     */
    public String generateStudentReport(int studentId) {
        StringBuilder report = new StringBuilder();
        report.append("=== Student Activity Report ===\n");

        try {
            // Get balance
            double balance = dataLayer.getAccountBalance(studentId);
            report.append(String.format("Current Balance: %.2f credits\n\n", balance));

            // Get registered routes
            ResultSet routes = dataLayer.getStudentRoutes(studentId);
            report.append("Registered Routes:\n");
            int count = 0;
            while (routes != null && routes.next()) {
                count++;
                report.append(String.format("  %d. %s (%s) - Status: %s\n",
                    count,
                    routes.getString("route_name"),
                    routes.getString("route_code"),
                    routes.getString("status")));
            }

            if (count == 0) {
                report.append("  No active registrations\n");
            }

        } catch (SQLException e) {
            report.append("Error generating report: ").append(e.getMessage());
        }

        return report.toString();
    }

    // ========== NEW METHODS USING MODEL OBJECTS ==========

    /**
     * Authenticate user and return User object
     */
    public User authenticateUserObject(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            System.err.println("Username cannot be empty");
            return null;
        }
        if (password == null || password.trim().isEmpty()) {
            System.err.println("Password cannot be empty");
            return null;
        }

        return dataLayer.loginUser(username, password);
    }

    /**
     * Get all routes as List of Route objects
     */
    public List<Route> getAvailableRoutes() {
        return dataLayer.getAllRoutesAsList();
    }

    /**
     * Get all users as List of User objects
     */
    public List<User> getUsersList() {
        return dataLayer.getAllUsersAsList();
    }

    /**
     * Get all shuttles as List of Shuttle objects
     */
    public List<Shuttle> getShuttlesList() {
        return dataLayer.getAllShuttlesAsList();
    }

    /**
     * Get account information for a student
     */
    public Account getStudentAccount(int userId) {
        return dataLayer.getAccount(userId);
    }

    /**
     * Get stops for a route as List of Stop objects
     */
    public List<Stop> getStopsForRoute(int routeId) {
        return dataLayer.getRouteStopsAsList(routeId);
    }

    /**
     * Get shuttle locations as List of ShuttleLocation objects
     */
    public List<ShuttleLocation> getShuttleLocationsList() {
        return dataLayer.getShuttleLocationsAsList();
    }

    /**
     * Get student's registrations as List of StudentRouteRegistration objects
     */
    public List<StudentRouteRegistration> getStudentRegistrationsList(int studentId) {
        return dataLayer.getStudentRegistrationsAsList(studentId);
    }

    /**
     * Get shuttle schedules as List of ShuttleSchedule objects
     */
    public List<ShuttleSchedule> getSchedulesForRoute(int routeId) {
        return dataLayer.getShuttleSchedulesAsList(routeId);
    }

    /**
     * Get driver's shuttle as Shuttle object
     */
    public Shuttle getDriverShuttleObject(int driverId) {
        return dataLayer.getDriverShuttleObject(driverId);
    }

    /**
     * Add a new user using User model object
     */
    public boolean addUser(User user, String password) {
        // Validate inputs
        if (!validateUserInputs(user.getUsername(), password, user.getFullName(), user.getEmail())) {
            return false;
        }

        return dataLayer.addUser(user, password);
    }

    /**
     * Create a new student
     */
    public boolean createStudent(String username, String password, String fullName, 
                                 String email, String phone) {
        User user = new User(username, null, fullName, email, phone, "Student");
        return addUser(user, password);
    }

    /**
     * Create a new admin
     */
    public boolean createAdmin(String username, String password, String fullName, 
                               String email, String phone) {
        User user = new User(username, null, fullName, email, phone, "Admin");
        return addUser(user, password);
    }

    /**
     * Create a new driver
     */
    public boolean createDriver(String username, String password, String fullName, 
                               String email, String phone) {
        User user = new User(username, null, fullName, email, phone, "Driver");
        return addUser(user, password);
    }
}

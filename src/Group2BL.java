/*
Group 2
Members: Yosef Shibele, Syed Zain, Ismail Mohammed Habibi
Date: 11/23/2025
*/

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.*;

public class Group2BL {
    private Group2DL dataLayer;

    public Group2BL() {
        this.dataLayer = new Group2DL();
    }

    public Group2BL(String dbUrl, String dbUser, String dbPassword) {
        this.dataLayer = new Group2DL(dbUrl, dbUser, dbPassword);
    }

    public boolean connect() {
        return dataLayer.connectDB();
    }

    public boolean disconnect() {
        return dataLayer.disconnectDB();
    }

    public boolean isConnected() {
        return dataLayer.isConnected();
    }

    private boolean isValidTableName(String tableName) {
        String regex = "^[a-zA-Z0-9_]+$";
        return tableName != null && tableName.matches(regex);
    }

    public ResultSet viewTable(String tableName, boolean fullAccess, int userId) { // \ Validate table name (prevent SQL
                                                                                   // injection)
        if (!isValidTableName(tableName)) {
            System.err.println("ERROR: Invalid table name");
            return null;
        }
        return dataLayer.displayTable(tableName, fullAccess, userId);
    }

    public User authenticateUser(String username, String password) {
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

    public boolean addUser(String username, String password, String fullName,
            String email, String phone, String role) {
        if (!validateUserInputs(username, password, fullName, email))
            return false;
        User user = new User(username, null, fullName, email, phone, role);
        return dataLayer.addUser(user, password);
    }

    public ResultSet getUserSpecificData(String tableName, int userId) {
        if (!isValidTableName(tableName)) {
            System.err.println("ERROR: Invalid table name");
            return null;
        }
        return dataLayer.getUserSpecificData(tableName, userId);
    }

    public String transferCredits(int sourceUserId, int destUserId, double amount) {
        if (amount <= 0)
            return "ERROR: Transfer amount must be greater than zero";
        if (amount > 10000)
            return "ERROR: Transfer amount exceeds maximum limit (10000 credits)";
        amount = Math.round(amount * 100.0) / 100.0;

        double sourceBalance = dataLayer.getAccountBalance(sourceUserId);
        if (sourceBalance < 0)
            return "ERROR: Could not retrieve source account balance";
        if (sourceBalance < amount)
            return String.format("ERROR: Insufficient balance. Available: %.2f, Required: %.2f",
                    sourceBalance, amount);

        return dataLayer.transferCredits(sourceUserId, destUserId, amount);
    }

    public double viewAccountBalance(int userId) {
        return dataLayer.getAccountBalance(userId);
    }

    public ArrayList<Route> viewAvailableRoutes() {
        return dataLayer.getAllRoutes();
    }

    public ArrayList<User> getAllUsers() {
        return dataLayer.getAllUsersAsList();
    }

    public ArrayList<Shuttle> getAllShuttles() {
        return dataLayer.getAllShuttlesAsList();
    }

    public Account getStudentAccount(int userId) {
        return dataLayer.getAccount(userId);
    }

    public ArrayList<Stop> getStopsForRoute(int routeId) {
        return dataLayer.getRouteStopsAsList(routeId);
    }

    public ArrayList<ShuttleLocation> getShuttleLocations() {
        return dataLayer.getShuttleLocationsAsList();
    }

    public ArrayList<StudentRouteRegistration> getStudentRegistrations(int studentId) {
        return dataLayer.getStudentRegistrationsAsList(studentId);
    }

    public ArrayList<ShuttleSchedule> getSchedulesForRoute(int routeId) {
        return dataLayer.getShuttleSchedulesAsList(routeId);
    }

    public Shuttle getDriverShuttle(int driverId) {
        return dataLayer.getDriverShuttleObject(driverId);
    }

    public String getRouteCodeById(int routeId) {
        return dataLayer.getRouteCodeById(routeId);
    }

    public String getRouteNameById(int routeId) {
        return dataLayer.getRouteNameById(routeId);
    }

    public String getShuttleNumberById(int shuttleId) {
        return dataLayer.getShuttleNumberById(shuttleId);
    }

    private boolean validateUserInputs(String username, String password,
            String fullName, String email) {
        if (username == null || username.trim().isEmpty() || username.length() < 3)
            return false;
        if (password == null || password.trim().isEmpty() || password.length() < 6)
            return false;
        if (fullName == null || fullName.trim().isEmpty())
            return false;
        if (email == null || email.trim().isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            return false;
        return true;
    }

    public boolean hasPermission(String userRole, String action) {
        return switch (action) {
            case "ADD_USER", "VIEW_ALL_TABLES", "VIEW_ALL_USERS" -> userRole.equals("Admin");
            case "UPDATE_LOCATION" -> userRole.equals("Driver");
            case "TRANSFER_CREDITS", "REGISTER_ROUTE", "VIEW_ROUTES" -> userRole.equals("Student");
            default -> false;
        };
    }

    public String generateStudentReport(int studentId) {
        StringBuilder report = new StringBuilder();
        report.append("=== Student Activity Report ===\n");

        double balance = dataLayer.getAccountBalance(studentId);
        report.append(String.format("Current Balance: %.2f credits\n\n", balance));

        List<StudentRouteRegistration> registrations = dataLayer.getStudentRegistrationsAsList(studentId);
        report.append("Registered Routes:\n");
        if (registrations.isEmpty()) {
            report.append("  No active registrations\n");
        } else {
            int count = 0;
            for (StudentRouteRegistration reg : registrations) {
                count++;
                report.append(String.format("  %d. Route ID: %d - Status: %s\n",
                        count, reg.getRouteId(), reg.getStatus()));
            }
        }

        return report.toString();
    }

    public String formatCurrency(double amount) {
        return String.format("%.2f", amount);
    }

    public Group2DL getDataLayer() {
        return dataLayer;
    }

    public ArrayList<Transaction> getTransactionHistory(int userId) {
        if (userId <= 0) {
            System.err.println("ERROR: Invalid user ID");
            return new ArrayList<>();
        }

        return dataLayer.getUserTransactionHistory(userId);
    }

    public String getUserNameById(int userId) {
        return dataLayer.getUserNameById(userId);
    }

    public User getUserById(int userId) {
        return dataLayer.getUserById(userId);
    }

    public String hashPassword(String password) {
        return dataLayer.hashPassword(password);
    }
}

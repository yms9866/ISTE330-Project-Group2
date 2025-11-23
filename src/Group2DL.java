/*
Group 2
Members: Yosef Shibele, Syed Zain, Ismail Mohammed Habibi
Date: 11/17/2025
*/

import java.sql.*;
import java.awt.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import model.*;

/**
 * Data Layer - Handles all database operations for the Shuttle Management
 * System
 */
public class Group2DL {
    private Connection connection;
    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private boolean isConnected;

    public Group2DL() {
        this.dbUrl = "jdbc:mariadb://localhost:3308/ShuttleManagementDB";
        this.dbUser = "root";
        this.dbPassword = "admin";
        this.isConnected = false;
    }

    public Group2DL(String url, String user, String password) {
        this.dbUrl = url;
        this.dbUser = user;
        this.dbPassword = password;
        this.isConnected = false;
    }

    public boolean connectDB() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            isConnected = true;
            System.out.println("Connected to database successfully!");
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            isConnected = false;
            return false;
        }
    }

    public boolean disconnectDB() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                isConnected = false;
                System.out.println("Disconnected from database successfully!");
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error disconnecting: " + e.getMessage());
            return false;
        }
    }

    public boolean isConnected() {
        return isConnected && connection != null;
    }

    public Connection getConnection() {
        return connection;
    }

    /** * Display table using stored procedure */
    public ResultSet displayTable(String tableName, boolean fullAccess, int userId) {
        if (!isConnected) {
            return null;
        }
        try {
            String call = "{CALL sp_display_table(?)}";
            CallableStatement cstmt = connection.prepareCall(call);
            cstmt.setString(1, tableName);
            if (fullAccess) {
                
                return cstmt.executeQuery();
            } else {
                return getUserSpecificData(tableName, userId);
            }
        } catch (SQLException e) {
            System.err.println("Error displaying table: " + e.getMessage());
            return null;
        }
    }
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error hashing password: " + e.getMessage());
            return null;
        }
    }

    public User login(String username, String password) {
        if (!isConnected)
            return null;

        String hashedPassword = hashPassword(password);
        if (hashedPassword == null)
            return null;

        String query = "SELECT user_id, username, full_name, email, role " +
                "FROM USERS WHERE username = ? AND password_hash = ? AND is_active = TRUE";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                user.setActive(true);
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
        }
        return null;
    }

    public boolean addUser(User user, String password) {
        if (!isConnected || user == null || password == null || password.trim().isEmpty()) {
            return false;
        }

        String hashedPassword = hashPassword(password);
        if (hashedPassword == null) {
            return false;
        }

        String query = "INSERT INTO USERS (username, password_hash, full_name, email, phone, role, is_active) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, user.getFullName());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getPhone());
            pstmt.setString(6, user.getRole());
            pstmt.setBoolean(7, user.isActive());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                connection.rollback();
                return false;
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setUserId(generatedKeys.getInt(1));
                    connection.commit();
                    return true;
                } else {
                    connection.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println("Rollback error: " + ex.getMessage());
            }
            System.err.println("Error adding user: " + e.getMessage());
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }
    

    public ArrayList<Transaction> getUserTransactionHistory(int userId) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        
        if (!isConnected || userId <= 0) {
            return transactions;
        }
        
        String query = "SELECT t.transaction_id, t.recieverid, t.senderid, t.amount, t.transaction_date" +
                      " FROM TRANSACTIONS t " +
                      "INNER JOIN USERS u ON t.senderid = u.user_id " +
                      "WHERE u.user_id = ? " +
                "ORDER BY t.transaction_date DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(rs.getInt("transaction_id"));
                transaction.setRecieverid(rs.getInt("recieverid"));
                transaction.setSenderid(rs.getInt("senderid"));
                transaction.setAmount(rs.getDouble("amount"));
                transaction.setTransactionDate(rs.getTimestamp("transaction_date"));
                
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving transaction history: " + e.getMessage());
        }
        
        return transactions;
    }

    public String transferCredits(int sourceUserId, int destUserId, double amount) {
        if (!isConnected)
            return "ERROR: Not connected to database";

        String call = "{CALL sp_transfer_credits(?, ?, ?, ?)}";
        try (CallableStatement cstmt = connection.prepareCall(call)) {
            connection.setAutoCommit(false);
            
            // Execute the transfer
            cstmt.setInt(1, sourceUserId);
            cstmt.setInt(2, destUserId);
            cstmt.setDouble(3, amount);
            cstmt.registerOutParameter(4, Types.VARCHAR);
            cstmt.execute();
            
            String result = cstmt.getString(4);
            
            // If transfer was successful, record the transaction
            if (result.startsWith("SUCCESS")) {
                try {
                    
                    // Record the transaction
                    String transactionSql = "INSERT INTO TRANSACTIONS (senderid, recieverid, amount, transaction_date) " +
                                          "VALUES (?, ?, ?, NOW())";
                                          
                    try (PreparedStatement pstmt = connection.prepareStatement(transactionSql)) {
                        pstmt.setInt(1, sourceUserId);  
                        pstmt.setInt(2, destUserId);   
                        pstmt.setDouble(3, amount);
                        pstmt.executeUpdate();
                    }
                    
                    connection.commit();
                } catch (SQLException e) {
                    connection.rollback();
                    return "ERROR: Transaction could not be recorded: " + e.getMessage();
                }
            } else {
                connection.rollback();
            }
            
            return result;

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                return "ERROR: " + e.getMessage() + " (Rollback failed: " + ex.getMessage() + ")";
            }
            return "ERROR: " + e.getMessage();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }

    

    public double getAccountBalance(int userId) {
        if (!isConnected)
            return -1.0;

        String query = "SELECT balance FROM ACCOUNTS WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next())
                return rs.getDouble("balance");
        } catch (SQLException e) {
            System.err.println("Error getting balance: " + e.getMessage());
        }
        return -1.0;
    }


    public ArrayList<Route> getAllRoutes() {
        ArrayList<Route> routes = new ArrayList<>();
        if (!isConnected)
            return routes;

        String query = "SELECT route_id, route_name, route_code, description, distance_km, " +
                "estimated_duration_minutes, credits_required FROM ROUTE WHERE is_active = TRUE ORDER BY route_name";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Route route = new Route(
                        rs.getInt("route_id"),
                        rs.getString("route_name"),
                        rs.getString("route_code"),
                        rs.getString("description"),
                        rs.getBigDecimal("distance_km"),
                        rs.getInt("estimated_duration_minutes"),
                        rs.getBigDecimal("credits_required"), true, null);
                routes.add(route);
            }
        } catch (SQLException e) {
            System.err.println("Error getting routes: " + e.getMessage());
        }
        return routes;
    }

    public ArrayList<User> getAllUsersAsList() {
        ArrayList<User> users = new ArrayList<>();
        if (!isConnected)
            return users;

        String query = "SELECT user_id, username, full_name, email, phone, role, created_at, is_active " +
                "FROM USERS ORDER BY role, username";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setRole(rs.getString("role"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setActive(rs.getBoolean("is_active"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error getting users: " + e.getMessage());
        }
        return users;
    }

    public ArrayList<Shuttle> getAllShuttlesAsList() {
        ArrayList<Shuttle> shuttles = new ArrayList<>();
        if (!isConnected)
            return shuttles;

        String query = "SELECT shuttle_id, shuttle_number, license_plate, capacity, driver_id, status, created_at " +
                "FROM SHUTTLE ORDER BY shuttle_number";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Shuttle shuttle = new Shuttle();
                shuttle.setShuttleId(rs.getInt("shuttle_id"));
                shuttle.setShuttleNumber(rs.getString("shuttle_number"));
                shuttle.setLicensePlate(rs.getString("license_plate"));
                shuttle.setCapacity(rs.getInt("capacity"));
                int driverId = rs.getInt("driver_id");
                shuttle.setDriverId(rs.wasNull() ? null : driverId);
                shuttle.setStatus(rs.getString("status"));
                shuttle.setCreatedAt(rs.getTimestamp("created_at"));
                shuttles.add(shuttle);
            }
        } catch (SQLException e) {
            System.err.println("Error getting shuttles: " + e.getMessage());
        }
        return shuttles;
    }

    public Account getAccount(int userId) {
        if (!isConnected)
            return null;

        String query = "SELECT account_id, user_id, balance, last_updated FROM ACCOUNTS WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Account account = new Account();
                account.setAccountId(rs.getInt("account_id"));
                account.setUserId(rs.getInt("user_id"));
                account.setBalance(rs.getBigDecimal("balance"));
                account.setLastUpdated(rs.getTimestamp("last_updated"));
                return account;
            }
        } catch (SQLException e) {
            System.err.println("Error getting account: " + e.getMessage());
        }
        return null;
    }

    public ArrayList<Stop> getRouteStopsAsList(int routeId) {
        ArrayList<Stop> stops = new ArrayList<>();
        if (!isConnected)
            return stops;

        String query = "SELECT stop_id, route_id, stop_name, stop_order, latitude, longitude, estimated_arrival_time " +
                "FROM STOP WHERE route_id = ? ORDER BY stop_order";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, routeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Stop stop = new Stop();
                stop.setStopId(rs.getInt("stop_id"));
                stop.setRouteId(rs.getInt("route_id"));
                stop.setStopName(rs.getString("stop_name"));
                stop.setStopOrder(rs.getInt("stop_order"));
                stop.setLatitude(rs.getBigDecimal("latitude"));
                stop.setLongitude(rs.getBigDecimal("longitude"));
                stop.setEstimatedArrivalTime(rs.getTime("estimated_arrival_time"));
                stops.add(stop);
            }
        } catch (SQLException e) {
            System.err.println("Error getting stops: " + e.getMessage());
        }
        return stops;
    }

    public ArrayList<ShuttleLocation> getShuttleLocationsAsList() {
        ArrayList<ShuttleLocation> locations = new ArrayList<>();
        if (!isConnected)
            return locations;

        String query = "SELECT sl1.location_id, sl1.shuttle_id, sl1.latitude, sl1.longitude, " +
                "sl1.speed_kmh, sl1.heading, sl1.timestamp " +
                "FROM SHUTTLE_LOCATION sl1 " +
                "INNER JOIN (SELECT shuttle_id, MAX(timestamp) AS max_timestamp FROM SHUTTLE_LOCATION GROUP BY shuttle_id) sl2 "
                +
                "ON sl1.shuttle_id = sl2.shuttle_id AND sl1.timestamp = sl2.max_timestamp ORDER BY sl1.shuttle_id";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                ShuttleLocation location = new ShuttleLocation();
                location.setLocationId(rs.getInt("location_id"));
                location.setShuttleId(rs.getInt("shuttle_id"));
                location.setLatitude(rs.getBigDecimal("latitude"));
                location.setLongitude(rs.getBigDecimal("longitude"));
                location.setSpeedKmh(rs.getBigDecimal("speed_kmh"));
                int heading = rs.getInt("heading");
                location.setHeading(rs.wasNull() ? null : heading);
                location.setTimestamp(rs.getTimestamp("timestamp"));
                locations.add(location);
            }
        } catch (SQLException e) {
            System.err.println("Error getting shuttle locations: " + e.getMessage());
        }
        return locations;
    }

    public ArrayList<StudentRouteRegistration> getStudentRegistrationsAsList(int studentId) {
        ArrayList<StudentRouteRegistration> registrations = new ArrayList<>();
        if (!isConnected)
            return registrations;

        String query = "SELECT registration_id, student_id, route_id, registration_date, expiry_date, status, credits_paid "
                +
                "FROM STUDENT_ROUTE_REGISTRATION WHERE student_id = ? ORDER BY registration_date DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                StudentRouteRegistration reg = new StudentRouteRegistration();
                reg.setRegistrationId(rs.getInt("registration_id"));
                reg.setStudentId(rs.getInt("student_id"));
                reg.setRouteId(rs.getInt("route_id"));
                reg.setRegistrationDate(rs.getTimestamp("registration_date"));
                reg.setExpiryDate(rs.getDate("expiry_date"));
                reg.setStatus(rs.getString("status"));
                reg.setCreditsPaid(rs.getBigDecimal("credits_paid"));
                registrations.add(reg);
            }
        } catch (SQLException e) {
            System.err.println("Error getting registrations: " + e.getMessage());
        }
        return registrations;
    }

    public ArrayList<ShuttleSchedule> getShuttleSchedulesAsList(int routeId) {
        ArrayList<ShuttleSchedule> schedules = new ArrayList<>();
        if (!isConnected)
            return schedules;

        String query = "SELECT schedule_id, shuttle_id, route_id, day_of_week, departure_time, arrival_time, is_active "
                +
                "FROM SHUTTLE_SCHEDULE WHERE route_id = ? AND is_active = TRUE ORDER BY day_of_week, departure_time";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, routeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ShuttleSchedule schedule = new ShuttleSchedule();
                schedule.setScheduleId(rs.getInt("schedule_id"));
                schedule.setShuttleId(rs.getInt("shuttle_id"));
                schedule.setRouteId(rs.getInt("route_id"));
                schedule.setDayOfWeek(rs.getString("day_of_week"));
                schedule.setDepartureTime(rs.getTime("departure_time"));
                schedule.setArrivalTime(rs.getTime("arrival_time"));
                schedule.setActive(rs.getBoolean("is_active"));
                schedules.add(schedule);
            }
        } catch (SQLException e) {
            System.err.println("Error getting schedules: " + e.getMessage());
        }
        return schedules;
    }

    public Shuttle getDriverShuttleObject(int driverId) {
        if (!isConnected)
            return null;

        String query = "SELECT shuttle_id, shuttle_number, license_plate, capacity, driver_id, status, created_at " +
                "FROM SHUTTLE WHERE driver_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, driverId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Shuttle shuttle = new Shuttle();
                shuttle.setShuttleId(rs.getInt("shuttle_id"));
                shuttle.setShuttleNumber(rs.getString("shuttle_number"));
                shuttle.setLicensePlate(rs.getString("license_plate"));
                shuttle.setCapacity(rs.getInt("capacity"));
                shuttle.setDriverId(rs.getInt("driver_id"));
                shuttle.setStatus(rs.getString("status"));
                shuttle.setCreatedAt(rs.getTimestamp("created_at"));
                return shuttle;
            }
        } catch (SQLException e) {
            System.err.println("Error getting driver shuttle: " + e.getMessage());
        }
        return null;
    }

    public String getRouteCodeById(int routeId) {
        if (!isConnected)
            return null;

        String query = "SELECT route_code FROM ROUTE WHERE route_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, routeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("route_code");
            }
        } catch (SQLException e) {
            System.err.println("Error getting route code: " + e.getMessage());
        }
        return null;
    }

    public String getRouteNameById(int routeId) {
        if (!isConnected)
            return null;

        String query = "SELECT route_name FROM ROUTE WHERE route_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, routeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("route_name");
            }
        } catch (SQLException e) {
            System.err.println("Error getting route name: " + e.getMessage());
        }
        return null;
    }

    public String getShuttleNumberById(int shuttleId) {
        if (!isConnected)
            return null;

        String query = "SELECT shuttle_number FROM SHUTTLE WHERE shuttle_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, shuttleId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("shuttle_number");
            }
        } catch (SQLException e) {
            System.err.println("Error getting shuttle number: " + e.getMessage());
        }
        return null;
    }
    
    public ResultSet getUserSpecificData(String tableName, int userId) {
        if (!isConnected) {
            return null;
        }
        try {
            StringBuilder routIds = new StringBuilder();
            Shuttle shuttle = getDriverShuttleObject(userId);
            if (shuttle == null) {
                return null;
            }
            String query0 = "SELECT r.* FROM ROUTE r " +
                    "INNER JOIN SHUTTLE_SCHEDULE ss ON r.route_id = ss.route_id " +
                    "WHERE ss.shuttle_id = ? GROUP BY r.route_id";
            PreparedStatement pstmt0 = connection.prepareStatement(query0);
            pstmt0.setInt(1, shuttle.getShuttleId());
            ResultSet re0 = pstmt0.executeQuery();
            while (re0.next()) {
                routIds.append(re0.getInt("route_id")).append(",");
            }
            //  rmove last comma
            if (routIds.length() > 0) {
                routIds.setLength(routIds.length() - 1);
            } else {
                return null;
            }

            if (tableName.equals("SHUTTLE")) {
                String query = "SELECT * FROM SHUTTLE WHERE shuttle_id = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, shuttle.getShuttleId());
                return pstmt.executeQuery();
            } else if (tableName.equals("SHUTTLE_LOCATION")) {
                String query = "SELECT * FROM SHUTTLE_LOCATION WHERE shuttle_id = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, shuttle.getShuttleId());
                return pstmt.executeQuery();
            } else if (tableName.equals("SHUTTLE_SCHEDULE")) {
                String query = "SELECT * FROM SHUTTLE_SCHEDULE WHERE shuttle_id = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, shuttle.getShuttleId());
                return pstmt.executeQuery();
            } else if (tableName.equals("ROUTE")) {
                String query = "SELECT r.* FROM ROUTE r " +
                        "INNER JOIN SHUTTLE_SCHEDULE ss ON r.route_id = ss.route_id " +
                        "WHERE ss.shuttle_id = ? GROUP BY r.route_id";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, shuttle.getShuttleId());
                ResultSet re = pstmt.executeQuery();
                return re;
            } else if (tableName.equals("STOP")) {
                String query = "SELECT s.* FROM STOP s " +
                        "INNER JOIN ROUTE r ON s.route_id = r.route_id Where s.route_id in (" + routIds.toString()
                        + ") ORDER BY s.route_id, s.stop_order";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, shuttle.getShuttleId());
                return pstmt.executeQuery();
            } else if (tableName.equals("STUDENT_ROUTE_REGISTRATION")) {
                String query = "SELECT srr.* FROM STUDENT_ROUTE_REGISTRATION srr " +
                        "INNER JOIN ROUTE r ON srr.route_id = r.route_id " +
                        "INNER JOIN SHUTTLE_SCHEDULE ss ON r.route_id = ss.route_id " +
                        "WHERE ss.shuttle_id = ? ORDER BY srr.registration_date DESC";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, shuttle.getShuttleId());
                return pstmt.executeQuery();
            } else if (tableName.equals("ACCOUNT")) {
                String query = "SELECT a.* FROM ACCOUNTS a " +
                        "INNER JOIN USERS u ON a.user_id = u.user_id " +
                        "INNER JOIN STUDENT_ROUTE_REGISTRATION srr ON u.user_id = srr.student_id " +
                        "INNER JOIN ROUTE r ON srr.route_id = r.route_id " +
                        "INNER JOIN SHUTTLE_SCHEDULE ss ON r.route_id = ss.route_id " +
                        "WHERE ss.shuttle_id = ? GROUP BY a.account_id";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, shuttle.getShuttleId());
                return pstmt.executeQuery();
            }

            else {
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user-specific data: " + e.getMessage());
            return null;
        }

    }

    public String getUserNameById(int userId) {
        if (!isConnected)
            return null;

        String query = "SELECT full_name FROM USERS WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("full_name");
            }
        } catch (SQLException e) {
            System.err.println("Error getting user name: " + e.getMessage());
        }
        return null;
    }
    
    public User getUserById(int userId) {
        if (!isConnected)
            return null;

        String query = "SELECT user_id, username, full_name, email, phone, role, created_at, is_active " +
                "FROM USERS WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setRole(rs.getString("role"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setActive(rs.getBoolean("is_active"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by ID: " + e.getMessage());
        }
        return null;
    }
}

// Group 1: Shuttle Management System - Presentation Layer
// Members: 
// Date: November 3, 2025
// Course: ISTE-330 Database Connectivity and Access

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import model.*;

public class Group1PL extends JFrame {
    private Group1BL businessLayer;
    private User currentUser;
    private int userId;
    private String userRole;
    
    // UI Components
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    private JButton connectBtn, disconnectBtn, loginBtn;
    
    // Colors
    private final Color PRIMARY = new Color(41, 128, 185);
    private final Color SUCCESS = new Color(39, 174, 96);
    private final Color DANGER = new Color(231, 76, 60);
    private final Color WARNING = new Color(243, 156, 18);
    
    public Group1PL() {
        businessLayer = new Group1BL();
        initGUI();
    }
    
    private void initGUI() {
        setTitle("Group 1: Shuttle Management System");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        mainPanel.add(createLoginScreen(), "LOGIN");
        mainPanel.add(createAdminPanel(), "ADMIN");
        mainPanel.add(createDriverPanel(), "DRIVER");
        mainPanel.add(createStudentPanel(), "STUDENT");
        
        add(mainPanel);
        setVisible(true);
    }
    
    private JPanel createLoginScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(236, 240, 241));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(PRIMARY, 2),
            new EmptyBorder(30, 30, 30, 30)
        ));
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        c.insets = new Insets(5, 5, 5, 5);
        
        JLabel title = new JLabel("Shuttle Management System - Group 1");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(PRIMARY);
        card.add(title, c);
        
        c.gridy++; c.gridwidth = 1;
        connectBtn = new JButton("Connect");
        styleButton(connectBtn, SUCCESS);
        connectBtn.addActionListener(e -> connectDB());
        card.add(connectBtn, c);
        
        c.gridx = 1;
        disconnectBtn = new JButton("Disconnect");
        styleButton(disconnectBtn, DANGER);
        disconnectBtn.setEnabled(false);
        disconnectBtn.addActionListener(e -> disconnectDB());
        card.add(disconnectBtn, c);
        
        c.gridx = 0; c.gridy++; c.gridwidth = 2;
        card.add(new JLabel("Username:"), c);
        
        c.gridy++;
        usernameField = new JTextField(20);
        card.add(usernameField, c);
        
        c.gridy++;
        card.add(new JLabel("Password:"), c);
        
        c.gridy++;
        passwordField = new JPasswordField(20);
        passwordField.addActionListener(e -> login());
        card.add(passwordField, c);
        
        c.gridy++;
        loginBtn = new JButton("Login");
        styleButton(loginBtn, PRIMARY);
        loginBtn.setEnabled(false);
        loginBtn.addActionListener(e -> login());
        card.add(loginBtn, c);
        
        c.gridy++;
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(DANGER);
        card.add(statusLabel, c);
        
        c.gridy++;
        JTextArea creds = new JTextArea("Test Credentials:\n" +
            "Admin: admin1 / admin123\n" +
            "Driver: driver1 / driver123\n" +
            "Student: student1 / student123");
        creds.setEditable(false);
        creds.setBackground(new Color(236, 240, 241));
        creds.setFont(new Font("Monospaced", Font.PLAIN, 11));
        card.add(creds, c);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(card, gbc);
        return panel;
    }
    
    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createHeader("Admin Dashboard"), BorderLayout.NORTH);
        
        // Admin can access all tables
        String[] adminTables = {"USERS", "ACCOUNTS", "SHUTTLE", "ROUTE", "STOP", 
                                "SHUTTLE_LOCATION", "STUDENT_ROUTE_REGISTRATION", "SHUTTLE_SCHEDULE"};
        panel.add(createTableBrowserPanel(adminTables, true), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createDriverPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createHeader("Driver Dashboard"), BorderLayout.NORTH);
        
        // Driver can access shuttle-related tables
        String[] driverTables = {"SHUTTLE", "SHUTTLE_LOCATION", "SHUTTLE_SCHEDULE", "ROUTE", "STOP"};
        panel.add(createTableBrowserPanel(driverTables, false), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createHeader("Student Dashboard"), BorderLayout.NORTH);
        
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("My Account", createMyAccountPanel());
        tabs.addTab("Transfer Credits", createTransferCreditsPanel());
        tabs.addTab("Available Routes", createAvailableRoutesPanel());
        tabs.addTab("My Bookings", createMyBookingsPanel());
        tabs.addTab("My Schedule", createMySchedulePanel());
        tabs.addTab("Track Shuttles", createTrackShuttlesPanel());
        
        panel.add(tabs, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createHeader(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        label.setForeground(Color.WHITE);
        panel.add(label, BorderLayout.WEST);
        
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setBackground(PRIMARY);
        
        if (currentUser != null) {
            JLabel user = new JLabel("Welcome, " + currentUser.getFullName());
            user.setForeground(Color.WHITE);
            right.add(user);
        }
        
        JButton logout = new JButton("Logout");
        styleButton(logout, DANGER);
        logout.addActionListener(e -> logout());
        right.add(logout);
        
        panel.add(right, BorderLayout.EAST);
        return panel;
    }
    
    private JPanel createViewUsersTab() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] cols = {"ID", "Username", "Name", "Email", "Role", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            model.setRowCount(0);
            try {
                ResultSet rs = businessLayer.getAllUsers();
                while (rs != null && rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("user_id"), rs.getString("username"),
                        rs.getString("full_name"), rs.getString("email"),
                        rs.getString("role"), rs.getBoolean("is_active") ? "Active" : "Inactive"
                    });
                }
            } catch (Exception ex) {
                showError("Error: " + ex.getMessage());
            }
        });
        
        JPanel bottom = new JPanel();
        bottom.add(refresh);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }
    
    private JPanel createAddUserTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField username = new JTextField(20);
        JPasswordField password = new JPasswordField(20);
        JTextField fullName = new JTextField(20);
        JTextField email = new JTextField(20);
        JTextField phone = new JTextField(20);
        JComboBox<String> role = new JComboBox<>(new String[]{"Student", "Admin", "Driver"});
        
        c.gridx = 0; c.gridy = 0; panel.add(new JLabel("Username:"), c);
        c.gridx = 1; panel.add(username, c);
        c.gridx = 0; c.gridy++; panel.add(new JLabel("Password:"), c);
        c.gridx = 1; panel.add(password, c);
        c.gridx = 0; c.gridy++; panel.add(new JLabel("Full Name:"), c);
        c.gridx = 1; panel.add(fullName, c);
        c.gridx = 0; c.gridy++; panel.add(new JLabel("Email:"), c);
        c.gridx = 1; panel.add(email, c);
        c.gridx = 0; c.gridy++; panel.add(new JLabel("Phone:"), c);
        c.gridx = 1; panel.add(phone, c);
        c.gridx = 0; c.gridy++; panel.add(new JLabel("Role:"), c);
        c.gridx = 1; panel.add(role, c);
        
        JButton add = new JButton("Add User");
        add.addActionListener(e -> {
            String r = (String) role.getSelectedItem();
            boolean success = false;
            if (r.equals("Student")) {
                success = businessLayer.addNewClient(username.getText(), 
                    new String(password.getPassword()), fullName.getText(), 
                    email.getText(), phone.getText());
            } else if (r.equals("Admin")) {
                success = businessLayer.addNewAdmin(username.getText(), 
                    new String(password.getPassword()), fullName.getText(), 
                    email.getText(), phone.getText());
            } else {
                success = businessLayer.addNewDriver(username.getText(), 
                    new String(password.getPassword()), fullName.getText(), 
                    email.getText(), phone.getText());
            }
            if (success) {
                showSuccess("User added successfully!");
                username.setText(""); password.setText(""); fullName.setText("");
                email.setText(""); phone.setText("");
            } else {
                showError("Failed to add user");
            }
        });
        
        c.gridx = 0; c.gridy++; c.gridwidth = 2;
        panel.add(add, c);
        return panel;
    }
    
    private JPanel createTableBrowserPanel(String[] allowedTables, boolean fullAccess) {
        JPanel wrapper = new JPanel(new BorderLayout());
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(250);
        
        // Left side - Table list
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(new TitledBorder("Available Tables"));
        
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String table : allowedTables) {
            listModel.addElement(table);
        }
        JList<String> tableList = new JList<>(listModel);
        tableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        leftPanel.add(new JScrollPane(tableList), BorderLayout.CENTER);
        
        // Right side - Table content and operations
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(new TitledBorder("Table Content & Operations"));
        
        // Table display
        DefaultTableModel tableModel = new DefaultTableModel();
        JTable dataTable = new JTable(tableModel);
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane tableScroll = new JScrollPane(dataTable);
        rightPanel.add(tableScroll, BorderLayout.CENTER);
        
        // Operations panel
        JPanel opsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        opsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JButton refreshBtn = new JButton("Refresh");
        styleButton(refreshBtn, PRIMARY);
        
        JButton createBtn = new JButton("Create Record");
        styleButton(createBtn, SUCCESS);
        
        JButton updateBtn = new JButton("Update Selected");
        styleButton(updateBtn, WARNING);
        
        JButton deleteBtn = new JButton("Delete Selected");
        styleButton(deleteBtn, DANGER);
        
        opsPanel.add(refreshBtn);
        if (fullAccess || (userRole != null && userRole.equals("Admin"))) {
            opsPanel.add(createBtn);
            opsPanel.add(updateBtn);
            opsPanel.add(deleteBtn);
        }
        
        rightPanel.add(opsPanel, BorderLayout.SOUTH);
        
        // Event handlers
        tableList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableList.getSelectedValue() != null) {
                loadTableData(tableList.getSelectedValue(), tableModel);
            }
        });
        
        refreshBtn.addActionListener(e -> {
            if (tableList.getSelectedValue() != null) {
                loadTableData(tableList.getSelectedValue(), tableModel);
            }
        });
        
        createBtn.addActionListener(e -> {
            if (tableList.getSelectedValue() != null) {
                showCreateDialog(tableList.getSelectedValue(), tableModel);
            }
        });
        
        updateBtn.addActionListener(e -> {
            int row = dataTable.getSelectedRow();
            if (row >= 0 && tableList.getSelectedValue() != null) {
                showUpdateDialog(tableList.getSelectedValue(), tableModel, dataTable, row);
            } else {
                showError("Please select a row to update");
            }
        });
        
        deleteBtn.addActionListener(e -> {
            int row = dataTable.getSelectedRow();
            if (row >= 0 && tableList.getSelectedValue() != null) {
                deleteRecord(tableList.getSelectedValue(), tableModel, dataTable, row);
            } else {
                showError("Please select a row to delete");
            }
        });
        
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        
        wrapper.add(splitPane, BorderLayout.CENTER);
        return wrapper;
    }
    
    private JPanel createMyShuttlePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("My Shuttle"));
        
        JTextArea area = new JTextArea(20, 40);
        area.setEditable(false);
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                ResultSet rs = businessLayer.getDriverShuttle(userId);
                if (rs != null && rs.next()) {
                    area.setText(String.format("Shuttle: %s\nLicense: %s\nCapacity: %d\nStatus: %s",
                        rs.getString("shuttle_number"), rs.getString("license_plate"),
                        rs.getInt("capacity"), rs.getString("status")));
                } else {
                    area.setText("No shuttle assigned");
                }
            } catch (Exception ex) {
                showError("Error: " + ex.getMessage());
            }
        });
        
        JPanel bottom = new JPanel();
        bottom.add(refresh);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }
    
    private JPanel createUpdateLocationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Update Location"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField lat = new JTextField(15);
        JTextField lon = new JTextField(15);
        JTextField speed = new JTextField(15);
        JTextField heading = new JTextField(15);
        
        c.gridx = 0; c.gridy = 0; panel.add(new JLabel("Latitude:"), c);
        c.gridx = 1; panel.add(lat, c);
        c.gridx = 0; c.gridy++; panel.add(new JLabel("Longitude:"), c);
        c.gridx = 1; panel.add(lon, c);
        c.gridx = 0; c.gridy++; panel.add(new JLabel("Speed (km/h):"), c);
        c.gridx = 1; panel.add(speed, c);
        c.gridx = 0; c.gridy++; panel.add(new JLabel("Heading (0-359):"), c);
        c.gridx = 1; panel.add(heading, c);
        
        JButton update = new JButton("Update");
        update.addActionListener(e -> {
            try {
                ResultSet rs = businessLayer.getDriverShuttle(userId);
                if (rs != null && rs.next()) {
                    int shuttleId = rs.getInt("shuttle_id");
                    String result = businessLayer.updateLocation(shuttleId,
                        Double.parseDouble(lat.getText()), Double.parseDouble(lon.getText()),
                        Double.parseDouble(speed.getText()), Integer.parseInt(heading.getText()));
                    if (result.startsWith("SUCCESS")) {
                        showSuccess(result);
                    } else {
                        showError(result);
                    }
                }
            } catch (Exception ex) {
                showError("Error: " + ex.getMessage());
            }
        });
        
        c.gridx = 0; c.gridy++; c.gridwidth = 2;
        panel.add(update, c);
        return panel;
    }
    
    private JPanel createMyAccountPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        
        JButton refresh = new JButton("Refresh Balance");
        refresh.addActionListener(e -> {
            double balance = businessLayer.viewAccountBalance(userId);
            area.setText(String.format("Account Balance: %.2f credits\n\n", balance));
            
            try {
                ResultSet rs = businessLayer.viewStudentRegistrations(userId);
                area.append("My Registered Routes:\n");
                area.append("=".repeat(50) + "\n");
                while (rs != null && rs.next()) {
                    area.append(String.format("%s (%s) - %s\n",
                        rs.getString("route_name"), rs.getString("route_code"),
                        rs.getString("status")));
                }
            } catch (Exception ex) {
                area.append("\nError loading routes: " + ex.getMessage());
            }
        });
        
        JPanel bottom = new JPanel();
        bottom.add(refresh);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }
    
    private JPanel createTransferCreditsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField destId = new JTextField(15);
        JTextField amount = new JTextField(15);
        
        c.gridx = 0; c.gridy = 0; panel.add(new JLabel("Destination User ID:"), c);
        c.gridx = 1; panel.add(destId, c);
        c.gridx = 0; c.gridy++; panel.add(new JLabel("Amount:"), c);
        c.gridx = 1; panel.add(amount, c);
        
        JButton transfer = new JButton("Transfer Credits");
        transfer.addActionListener(e -> {
            try {
                String result = businessLayer.transferCredits(userId,
                    Integer.parseInt(destId.getText()), Double.parseDouble(amount.getText()));
                if (result.startsWith("SUCCESS")) {
                    showSuccess(result);
                    destId.setText("");
                    amount.setText("");
                } else {
                    showError(result);
                }
            } catch (Exception ex) {
                showError("Invalid input: " + ex.getMessage());
            }
        });
        
        c.gridx = 0; c.gridy++; c.gridwidth = 2;
        panel.add(transfer, c);
        
        // Show students list
        c.gridy++;
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Name", "Balance"}, 0);
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(400, 300));
        panel.add(scroll, c);
        
        JButton loadStudents = new JButton("Load Students");
        loadStudents.addActionListener(e -> {
            model.setRowCount(0);
            try {
                ResultSet rs = businessLayer.getAllStudentsWithAccounts();
                while (rs != null && rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("user_id"), rs.getString("full_name"),
                        String.format("%.2f", rs.getDouble("balance"))
                    });
                }
            } catch (Exception ex) {
                showError("Error: " + ex.getMessage());
            }
        });
        
        c.gridy++;
        panel.add(loadStudents, c);
        return panel;
    }
    
    private JPanel createAvailableRoutesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Name", "Code", "Distance", "Duration", "Credits"}, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JPanel bottom = new JPanel();
        
        JButton refresh = new JButton("Refresh Routes");
        refresh.addActionListener(e -> {
            model.setRowCount(0);
            try {
                ArrayList<Route> routes = businessLayer.viewAvailableRoutes();
                for (Route route : routes) {
                    model.addRow(new Object[]{
                        route.getRouteId(), route.getRouteName(),
                        route.getRouteCode(), route.getDistanceKm(),
                        route.getEstimatedDurationMinutes(), route.getCreditsRequired()
                    });
                }
            } catch (Exception ex) {
                showError("Error: " + ex.getMessage());
            }
        });
        
        JButton register = new JButton("Register for Selected Route");
        register.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int routeId = (int) model.getValueAt(row, 0);
                String result = businessLayer.registerStudentForRoute(userId, routeId);
                if (result.startsWith("SUCCESS")) {
                    showSuccess(result);
                } else {
                    showError(result);
                }
            } else {
                showError("Please select a route");
            }
        });
        
        bottom.add(refresh);
        bottom.add(register);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }
    
    private JPanel createMyBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Route", "Code", "Status", "Registered Date"}, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JButton refresh = new JButton("Refresh My Bookings");
        refresh.addActionListener(e -> {
            model.setRowCount(0);
            try {
                ResultSet rs = businessLayer.viewStudentRegistrations(userId);
                while (rs != null && rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("route_name"),
                        rs.getString("route_code"),
                        rs.getString("status"),
                        rs.getTimestamp("registration_date")
                    });
                }
            } catch (Exception ex) {
                showError("Error: " + ex.getMessage());
            }
        });
        
        JPanel bottom = new JPanel();
        bottom.add(refresh);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }
    
    private JPanel createMySchedulePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Route", "Shuttle", "Day", "Departure", "Arrival"}, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JButton refresh = new JButton("Refresh Schedule");
        refresh.addActionListener(e -> {
            model.setRowCount(0);
            try {
                ResultSet rs = businessLayer.viewStudentRegistrations(userId);
                while (rs != null && rs.next()) {
                    int routeId = rs.getInt("route_id");
                    ResultSet schedules = businessLayer.getRouteSchedules(routeId);
                    while (schedules != null && schedules.next()) {
                        model.addRow(new Object[]{
                            rs.getString("route_name"),
                            schedules.getString("shuttle_number"),
                            schedules.getString("day_of_week"),
                            schedules.getTime("departure_time"),
                            schedules.getTime("arrival_time")
                        });
                    }
                }
            } catch (Exception ex) {
                showError("Error: " + ex.getMessage());
            }
        });
        
        JPanel bottom = new JPanel();
        bottom.add(refresh);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }
    
    private JPanel createTrackShuttlesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Shuttle", "Latitude", "Longitude", "Speed", "Last Update"}, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JButton refresh = new JButton("Refresh Locations");
        refresh.addActionListener(e -> {
            model.setRowCount(0);
            try {
                ResultSet rs = businessLayer.viewShuttleLocations();
                while (rs != null && rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("shuttle_number"), rs.getDouble("latitude"),
                        rs.getDouble("longitude"), rs.getDouble("speed_kmh"),
                        rs.getTimestamp("timestamp")
                    });
                }
            } catch (Exception ex) {
                showError("Error: " + ex.getMessage());
            }
        });
        
        JPanel bottom = new JPanel();
        bottom.add(refresh);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }
    
    private void connectDB() {
        if (businessLayer.connect()) {
            connectBtn.setEnabled(false);
            disconnectBtn.setEnabled(true);
            loginBtn.setEnabled(true);
            statusLabel.setText("Connected successfully!");
            statusLabel.setForeground(SUCCESS);
        } else {
            statusLabel.setText("Connection failed!");
            statusLabel.setForeground(DANGER);
        }
    }
    
    private void disconnectDB() {
        if (businessLayer.disconnect()) {
            connectBtn.setEnabled(true);
            disconnectBtn.setEnabled(false);
            loginBtn.setEnabled(false);
            statusLabel.setText("Disconnected");
            statusLabel.setForeground(DANGER);
        }
    }
    
    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        currentUser = businessLayer.authenticateUser(username, password);
        
        if (currentUser != null) {
            userId = currentUser.getUserId();
            userRole = currentUser.getRole();
            
            statusLabel.setText("Login successful!");
            statusLabel.setForeground(SUCCESS);
            
            if (userRole.equals("Admin")) {
                cardLayout.show(mainPanel, "ADMIN");
            } else if (userRole.equals("Driver")) {
                cardLayout.show(mainPanel, "DRIVER");
            } else if (userRole.equals("Student")) {
                cardLayout.show(mainPanel, "STUDENT");
            }
        } else {
            statusLabel.setText("Invalid credentials!");
            statusLabel.setForeground(DANGER);
        }
    }
    
    private void logout() {
        currentUser = null;
        userId = 0;
        userRole = null;
        usernameField.setText("");
        passwordField.setText("");
        statusLabel.setText(" ");
        cardLayout.show(mainPanel, "LOGIN");
    }
    
    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void displayResultSet(ResultSet rs, DefaultTableModel model) {
        try {
            model.setRowCount(0);
            model.setColumnCount(0);
            
            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();
            
            for (int i = 1; i <= colCount; i++) {
                model.addColumn(meta.getColumnName(i));
            }
            
            while (rs.next()) {
                Object[] row = new Object[colCount];
                for (int i = 1; i <= colCount; i++) {
                    row[i-1] = rs.getObject(i);
                }
                model.addRow(row);
            }
        } catch (Exception ex) {
            showError("Error displaying data: " + ex.getMessage());
        }
    }
    
    private void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    // Helper class to store foreign key dropdown items
    private class ForeignKeyItem {
        private int id;
        private String displayText;
        
        public ForeignKeyItem(int id, String displayText) {
            this.id = id;
            this.displayText = displayText;
        }
        
        public int getId() {
            return id;
        }
        
        @Override
        public String toString() {
            return displayText;
        }
    }
    
    // Get foreign key reference information
    private String[] getForeignKeyInfo(String tableName, String columnName) {
        // Returns [referenceTable, displayColumn]
        // Map foreign key columns to their reference tables and display columns
        
        if (columnName.equalsIgnoreCase("user_id")) {
            if (tableName.equalsIgnoreCase("ACCOUNTS")) {
                return new String[]{"USERS", "full_name", "username"};
            }
        }
        
        if (columnName.equalsIgnoreCase("driver_id")) {
            return new String[]{"USERS", "full_name", "username"};
        }
        
        if (columnName.equalsIgnoreCase("student_id")) {
            return new String[]{"USERS", "full_name", "username"};
        }
        
        if (columnName.equalsIgnoreCase("route_id")) {
            return new String[]{"ROUTE", "route_name", "route_code"};
        }
        
        if (columnName.equalsIgnoreCase("shuttle_id")) {
            return new String[]{"SHUTTLE", "shuttle_number", "license_plate"};
        }
        
        return null;
    }
    
    // Populate dropdown with foreign key options
    private JComboBox<ForeignKeyItem> createForeignKeyDropdown(String refTable, String displayCol, String secondaryCol) {
        JComboBox<ForeignKeyItem> combo = new JComboBox<>();
        combo.addItem(new ForeignKeyItem(0, "-- Select --"));
        
        try {
            ResultSet rs = businessLayer.viewTable(refTable);
            if (rs != null) {
                ResultSetMetaData meta = rs.getMetaData();
                int idColIndex = -1;
                int displayColIndex = -1;
                int secondaryColIndex = -1;
                
                // Find column indices
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    String colName = meta.getColumnName(i);
                    if (colName.equalsIgnoreCase(refTable.toLowerCase() + "_id") || 
                        colName.equalsIgnoreCase("user_id")) {
                        idColIndex = i;
                    }
                    if (colName.equalsIgnoreCase(displayCol)) {
                        displayColIndex = i;
                    }
                    if (colName.equalsIgnoreCase(secondaryCol)) {
                        secondaryColIndex = i;
                    }
                }
                
                // Populate combo box
                while (rs.next()) {
                    int id = rs.getInt(idColIndex);
                    String display = rs.getString(displayColIndex);
                    if (secondaryColIndex > 0) {
                        String secondary = rs.getString(secondaryColIndex);
                        display = display + " (" + secondary + ")";
                    }
                    combo.addItem(new ForeignKeyItem(id, display));
                }
            }
        } catch (Exception ex) {
            System.err.println("Error loading dropdown: " + ex.getMessage());
        }
        
        return combo;
    }
    
    private void loadTableData(String tableName, DefaultTableModel model) {
        try {
            ResultSet rs = businessLayer.viewTable(tableName);
            displayResultSet(rs, model);
        } catch (Exception ex) {
            showError("Error loading table: " + ex.getMessage());
        }
    }
    
    private void showCreateDialog(String tableName, DefaultTableModel model) {
        try {
            ResultSet rs = businessLayer.viewTable(tableName);
            if (rs != null) {
                ResultSetMetaData meta = rs.getMetaData();
                int colCount = meta.getColumnCount();
                
                JPanel panel = new JPanel(new GridLayout(colCount, 2, 5, 5));
                JComponent[] inputComponents = new JComponent[colCount];
                
                for (int i = 1; i <= colCount; i++) {
                    String colName = meta.getColumnName(i);
                    panel.add(new JLabel(colName + ":"));
                    
                    // Check if this is a primary key (first column, auto-increment)
                    if (colName.toLowerCase().endsWith("_id") && i == 1) {
                        JTextField field = new JTextField(20);
                        field.setText("AUTO");
                        field.setEnabled(false);
                        inputComponents[i-1] = field;
                        panel.add(field);
                    }
                    // Check if this is a foreign key
                    else if (colName.toLowerCase().endsWith("_id")) {
                        String[] fkInfo = getForeignKeyInfo(tableName, colName);
                        if (fkInfo != null) {
                            JComboBox<ForeignKeyItem> combo = createForeignKeyDropdown(
                                fkInfo[0], fkInfo[1], fkInfo[2]);
                            inputComponents[i-1] = combo;
                            panel.add(combo);
                        } else {
                            // Fallback to text field if FK info not found
                            JTextField field = new JTextField(20);
                            inputComponents[i-1] = field;
                            panel.add(field);
                        }
                    }
                    // Regular field
                    else {
                        JTextField field = new JTextField(20);
                        inputComponents[i-1] = field;
                        panel.add(field);
                    }
                }
                
                int result = JOptionPane.showConfirmDialog(this, panel, 
                    "Create New Record in " + tableName, JOptionPane.OK_CANCEL_OPTION);
                
                if (result == JOptionPane.OK_OPTION) {
                    createRecord(tableName, meta, inputComponents, model);
                }
            }
        } catch (Exception ex) {
            showError("Error opening create dialog: " + ex.getMessage());
        }
    }
    
    private void createRecord(String tableName, ResultSetMetaData meta, 
                             JComponent[] inputComponents, DefaultTableModel model) {
        try {
            StringBuilder cols = new StringBuilder();
            StringBuilder vals = new StringBuilder();
            
            int colCount = meta.getColumnCount();
            for (int i = 1; i <= colCount; i++) {
                String colName = meta.getColumnName(i);
                String value = "";
                
                // Get value from the appropriate component type
                JComponent component = inputComponents[i-1];
                if (component instanceof JTextField) {
                    value = ((JTextField) component).getText();
                } else if (component instanceof JComboBox) {
                    @SuppressWarnings("unchecked")
                    JComboBox<ForeignKeyItem> combo = (JComboBox<ForeignKeyItem>) component;
                    ForeignKeyItem selected = (ForeignKeyItem) combo.getSelectedItem();
                    if (selected != null && selected.getId() > 0) {
                        value = String.valueOf(selected.getId());
                    } else {
                        showError("Please select a valid " + colName);
                        return;
                    }
                }
                
                // Skip auto-increment IDs
                if (value.equals("AUTO") && i == 1) {
                    continue;
                }
                
                if (cols.length() > 0) {
                    cols.append(", ");
                    vals.append(", ");
                }
                cols.append(colName);
                vals.append("'").append(value.replace("'", "''")).append("'");
            }
            
            String sql = "INSERT INTO " + tableName + " (" + cols + ") VALUES (" + vals + ")";
            Connection conn = businessLayer.getDataLayer().getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            
            showSuccess("Record created successfully!");
            loadTableData(tableName, model);
        } catch (Exception ex) {
            showError("Error creating record: " + ex.getMessage());
        }
    }
    
    private void showUpdateDialog(String tableName, DefaultTableModel model, 
                                 JTable table, int row) {
        try {
            ResultSet rs = businessLayer.viewTable(tableName);
            if (rs != null) {
                ResultSetMetaData meta = rs.getMetaData();
                int colCount = meta.getColumnCount();
                
                JPanel panel = new JPanel(new GridLayout(colCount, 2, 5, 5));
                JComponent[] inputComponents = new JComponent[colCount];
                
                for (int i = 0; i < colCount; i++) {
                    String colName = meta.getColumnName(i + 1);
                    Object value = model.getValueAt(row, i);
                    
                    panel.add(new JLabel(colName + ":"));
                    
                    // Check if this is a primary key (first column)
                    if (colName.toLowerCase().endsWith("_id") && i == 0) {
                        JTextField field = new JTextField(value != null ? value.toString() : "", 20);
                        field.setEnabled(false);
                        inputComponents[i] = field;
                        panel.add(field);
                    }
                    // Check if this is a foreign key
                    else if (colName.toLowerCase().endsWith("_id")) {
                        String[] fkInfo = getForeignKeyInfo(tableName, colName);
                        if (fkInfo != null) {
                            JComboBox<ForeignKeyItem> combo = createForeignKeyDropdown(
                                fkInfo[0], fkInfo[1], fkInfo[2]);
                            // Pre-select the current value
                            if (value != null) {
                                int currentId = Integer.parseInt(value.toString());
                                for (int j = 0; j < combo.getItemCount(); j++) {
                                    ForeignKeyItem item = combo.getItemAt(j);
                                    if (item.getId() == currentId) {
                                        combo.setSelectedIndex(j);
                                        break;
                                    }
                                }
                            }
                            inputComponents[i] = combo;
                            panel.add(combo);
                        } else {
                            JTextField field = new JTextField(value != null ? value.toString() : "", 20);
                            inputComponents[i] = field;
                            panel.add(field);
                        }
                    }
                    // Regular field
                    else {
                        JTextField field = new JTextField(value != null ? value.toString() : "", 20);
                        inputComponents[i] = field;
                        panel.add(field);
                    }
                }
                
                int result = JOptionPane.showConfirmDialog(this, panel, 
                    "Update Record in " + tableName, JOptionPane.OK_CANCEL_OPTION);
                
                if (result == JOptionPane.OK_OPTION) {
                    updateRecord(tableName, meta, inputComponents, model, row);
                }
            }
        } catch (Exception ex) {
            showError("Error opening update dialog: " + ex.getMessage());
        }
    }
    
    private void updateRecord(String tableName, ResultSetMetaData meta, 
                             JComponent[] inputComponents, DefaultTableModel model, int row) {
        try {
            StringBuilder sets = new StringBuilder();
            String idCol = meta.getColumnName(1);
            String idVal = "";
            
            // Get ID value from first component
            JComponent firstComponent = inputComponents[0];
            if (firstComponent instanceof JTextField) {
                idVal = ((JTextField) firstComponent).getText();
            }
            
            int colCount = meta.getColumnCount();
            for (int i = 2; i <= colCount; i++) {
                String colName = meta.getColumnName(i);
                String value = "";
                
                // Get value from the appropriate component type
                JComponent component = inputComponents[i-1];
                if (component instanceof JTextField) {
                    value = ((JTextField) component).getText();
                } else if (component instanceof JComboBox) {
                    @SuppressWarnings("unchecked")
                    JComboBox<ForeignKeyItem> combo = (JComboBox<ForeignKeyItem>) component;
                    ForeignKeyItem selected = (ForeignKeyItem) combo.getSelectedItem();
                    if (selected != null && selected.getId() > 0) {
                        value = String.valueOf(selected.getId());
                    } else {
                        showError("Please select a valid " + colName);
                        return;
                    }
                }
                
                if (sets.length() > 0) {
                    sets.append(", ");
                }
                sets.append(colName).append(" = '");
                sets.append(value.replace("'", "''")).append("'");
            }
            
            String sql = "UPDATE " + tableName + " SET " + sets + " WHERE " + idCol + " = " + idVal;
            Connection conn = businessLayer.getDataLayer().getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            
            showSuccess("Record updated successfully!");
            loadTableData(tableName, model);
        } catch (Exception ex) {
            showError("Error updating record: " + ex.getMessage());
        }
    }
    
    private void deleteRecord(String tableName, DefaultTableModel model, JTable table, int row) {
        try {
            ResultSet rs = businessLayer.viewTable(tableName);
            if (rs != null) {
                ResultSetMetaData meta = rs.getMetaData();
                String idCol = meta.getColumnName(1);
                Object idVal = model.getValueAt(row, 0);
                
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete this record?\nID: " + idVal, 
                    "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    String sql = "DELETE FROM " + tableName + " WHERE " + idCol + " = " + idVal;
                    Connection conn = businessLayer.getDataLayer().getConnection();
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate(sql);
                    
                    showSuccess("Record deleted successfully!");
                    loadTableData(tableName, model);
                }
            }
        } catch (Exception ex) {
            showError("Error deleting record: " + ex.getMessage());
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Group1PL());
    }
}

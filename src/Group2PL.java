/*
Group 2
Members: Yosef Shibele, Syed Zain, Ismail Mohammed Habibi
Date: 11/17/2025
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import model.*;

public class Group2PL extends JFrame {
    private Group2BL businessLayer;
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

    // Colors - Modern palette
    private final Color PRIMARY = new Color(10, 150, 200);
    private final Color PRIMARY_DARK = new Color(31, 97, 141);
    private final Color SUCCESS = new Color(46, 204, 113);
    private final Color DANGER = new Color(231, 76, 60);
    private final Color WARNING = new Color(241, 196, 15);
    private final Color INFO = new Color(52, 152, 219);
    private final Color DARK = new Color(44, 62, 80);
    private final Color ACCENT = new Color(155, 89, 182);


    public Group2PL() {
        businessLayer = new Group2BL();
        initGUI();
    }

    private void initGUI() {
        setTitle("🚌 Group 2: Shuttle Management System");
        setSize(1400, 900);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set application icon
        setIconImage(createIconImage());

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setPaint(
                        new GradientPaint(0, 0, new Color(248, 249, 250), 0, getHeight(), new Color(233, 236, 239)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        mainPanel.add(createLoginScreen(), "LOGIN");
        mainPanel.add(createAdminPanel(), "ADMIN");
        mainPanel.add(createDriverPanel(), "DRIVER");
        mainPanel.add(createStudentPanel(), "STUDENT");

        //  i want to show our name in the center of the initial screen
         JPanel introPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(52, 152, 219),
                        getWidth(), getHeight(), new Color(41, 128, 185));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
         };
            JLabel introLabel = new JLabel("<html><div style='text-align: center;'>" +
                    "<h1 style='padding: 10px; font-size: 48px; color: white;'>Group 2</h1>" +
                    "<h2 style='padding: 10px; font-size: 24px; color: white;'>Shuttle Management System</h2>" +
                    "<p style='padding: 10px; font-size: 18px; color: white;'>Members: Yosef Shibele, Syed Zain, Ismail Mohammed Habibi</p>"
                    +
                    "<p style='padding: 10px; font-size: 18px; color: white;'>Date: 11/17/2025</p>" +
                    "</div></html>");
          
        JButton continueButton = new JButton("Continue to Login");
        continueButton.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));
        continueButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        // bottom center the button
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.SOUTH;
        introPanel.add(continueButton, gbc);

        introPanel.add(introLabel);
        mainPanel.add(introPanel, "INTRO");
        cardLayout.show(mainPanel, "INTRO");


        add(mainPanel);
        setVisible(true);
    }

    private Image createIconImage() {
        // Create a simple bus icon
        int width = 32, height = 32;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw bus shape
        g2d.setColor(PRIMARY);
        g2d.fillRoundRect(4, 8, 24, 16, 8, 8);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(8, 10, 16, 6);
        g2d.setColor(Color.BLACK);
        g2d.fillOval(8, 20, 6, 6);
        g2d.fillOval(18, 20, 6, 6);

        g2d.dispose();
        return image;
    }

    private JPanel createLoginScreen() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Background gradient
                GradientPaint gradient = new GradientPaint(0, 0, new Color(52, 152, 219),
                        getWidth(), getHeight(), new Color(41, 128, 185));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Decorative elements
                g2d.setColor(new Color(255, 255, 255, 20));
                for (int i = 0; i < 5; i++) {
                    int size = 80 + i * 40;
                    g2d.fillOval(getWidth() - 100 - i * 30, 50 + i * 20, size, size);
                }
            }
        };

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                // Header accent
                g2d.setColor(PRIMARY);
                g2d.fillRoundRect(0, 0, getWidth(), 8, 25, 25);
            }
        };
       
        card.setBackground(new Color(255, 255, 255, 230));
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        card.setPreferredSize(new Dimension(450, 600));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(10, 10, 30, 10);

        // App Icon and Title
        JLabel iconLabel = new JLabel("🚌");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        card.add(iconLabel, c);

        c.gridy++;
        JLabel title = new JLabel("Shuttle Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(DARK);
        card.add(title, c);

        c.gridy++;
        JLabel subtitle = new JLabel("Group 2 - Secure Login");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(108, 117, 125));
        card.add(subtitle, c);

        c.gridy++;
        c.gridwidth = 1;
        c.insets = new Insets(15, 5, 5, 5);

        // Connection buttons
        JPanel connectionPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        connectionPanel.setOpaque(false);

        connectBtn = new JButton("Connect Database");
        styleModernButton(connectBtn, SUCCESS);
        connectBtn.addActionListener(e -> connectDB());
        connectionPanel.add(connectBtn);

        disconnectBtn = new JButton("Disconnect");
        styleModernButton(disconnectBtn, DANGER);
        disconnectBtn.setEnabled(false);
        disconnectBtn.addActionListener(e -> disconnectDB());
        connectionPanel.add(disconnectBtn);

        c.gridwidth = 2;
        card.add(connectionPanel, c);

        // Login Form
        c.gridy++;
        c.gridwidth = 2;
        c.insets = new Insets(10, 10, 5, 10);
        c.weightx = 1.0; // allow formPanel to expand
        c.fill = GridBagConstraints.HORIZONTAL;

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints fc = new GridBagConstraints();
        fc.gridx = 0;
        fc.gridy = 0;
        fc.anchor = GridBagConstraints.WEST;
        fc.insets = new Insets(5, 5, 5, 5);
        
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        userLabel.setForeground(DARK);
        formPanel.add(userLabel, fc);

        fc.gridy++;
        fc.gridwidth = 2;
        fc.fill = GridBagConstraints.HORIZONTAL; // ★ allow stretch
        fc.weightx = 1.0; // ★ give weight
        usernameField = createModernTextField();
        formPanel.add(usernameField, fc);

        fc.gridy++;
        fc.gridwidth = 1; // reset for label
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passLabel.setForeground(DARK);
        formPanel.add(passLabel, fc);

        fc.gridy++;
        fc.gridwidth = 2;
        fc.fill = GridBagConstraints.HORIZONTAL; // ★ allow stretch
        fc.weightx = 1.0; // ★ give weight
        passwordField = createModernPasswordField();
        formPanel.add(passwordField, fc);

        card.add(formPanel, c);

        c.gridy++;
        loginBtn = new JButton("Sign In");
        styleModernButton(loginBtn, PRIMARY);
        loginBtn.setEnabled(false);
        loginBtn.addActionListener(e -> login());
        card.add(loginBtn, c);

        c.gridy++;
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setForeground(DANGER);
        card.add(statusLabel, c);

        // Test credentials
        c.gridy++;
        JPanel credsPanel = new JPanel(new BorderLayout());
        credsPanel.setBackground(new Color(248, 249, 250));
        credsPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(222, 226, 230), 1),
                new EmptyBorder(15, 15, 15, 15)));

        JLabel credsTitle = new JLabel("Test Credentials");
        credsTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        credsTitle.setForeground(DARK);
        credsPanel.add(credsTitle, BorderLayout.NORTH);

        JTextArea creds = new JTextArea(
                "👨‍💼 Admin: admin1 / admin123\n\n" +
                        "🚗 Driver: driver1 / driver123\n\n" +
                        "🎓 Student: student1 / student123");
        creds.setEditable(false);
        creds.setBackground(new Color(248, 249, 250));
        creds.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        creds.setForeground(new Color(108, 117, 125));
        credsPanel.add(creds, BorderLayout.CENTER);

        card.add(credsPanel, c);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(card, gbc);
        return panel;
    }

    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createModernHeader("👨‍💼 Admin Dashboard", "System Administrator"), BorderLayout.NORTH);

        // Admin can access all tables
        String[] adminTables = { "USERS", "ACCOUNTS", "SHUTTLE", "ROUTE", "STOP",
                "SHUTTLE_LOCATION", "STUDENT_ROUTE_REGISTRATION", "SHUTTLE_SCHEDULE" };
        panel.add(createModernTableBrowserPanel(adminTables, true), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDriverPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createModernHeader("🚗 Driver Dashboard", "Shuttle Operations"), BorderLayout.NORTH);

        // Driver can access shuttle-related tables
        String[] driverTables = { "SHUTTLE", "SHUTTLE_LOCATION", "SHUTTLE_SCHEDULE", "ROUTE", "STOP" };
        panel.add(createModernTableBrowserPanel(driverTables, false), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createModernHeader("🎓 Student Dashboard", "Campus Transportation"), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        tabs.setUI(new ModernTabbedPaneUI());

        // Load all student data immediately
        tabs.addTab("📊 My Account", createMyAccountPanel());
        tabs.addTab("💳 Transfer Credits", createTransferCreditsPanel());
        tabs.addTab("🛣️ Available Routes", createAvailableRoutesPanel());
        tabs.addTab("📋 My Bookings", createMyBookingsPanel());
        tabs.addTab("⏰ My Schedule", createMySchedulePanel());
        tabs.addTab("📍 Track Shuttles", createTrackShuttlesPanel());

        panel.add(tabs, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createModernHeader(String title, String subtitle) {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, PRIMARY, getWidth(), 0, PRIMARY_DARK);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));
        panel.setPreferredSize(new Dimension(0, 120));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        leftPanel.add(titleLabel, BorderLayout.NORTH);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        leftPanel.add(subtitleLabel, BorderLayout.CENTER);

        panel.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);

        if (currentUser != null) {
            JPanel userPanel = new JPanel(new BorderLayout());
            userPanel.setOpaque(false);

            JLabel welcome = new JLabel("Welcome back,");
            welcome.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            welcome.setForeground(new Color(255, 255, 255, 180));
            userPanel.add(welcome, BorderLayout.NORTH);

            JLabel user = new JLabel(currentUser.getFullName());
            user.setFont(new Font("Segoe UI", Font.BOLD, 16));
            user.setForeground(Color.WHITE);
            userPanel.add(user, BorderLayout.CENTER);

            rightPanel.add(userPanel);
        }

        JButton logout = new JButton("🚪 Logout");
        styleModernButton(logout, new Color(255, 255, 255, 40));
        logout.setForeground(Color.WHITE);
        logout.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 1));
        logout.addActionListener(e -> logout());
        rightPanel.add(logout);

        panel.add(rightPanel, BorderLayout.EAST);
        return panel;
    }

    private JPanel createModernTableBrowserPanel(String[] allowedTables, boolean fullAccess) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(280);
        splitPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Left side - Modern table list
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(222, 226, 230), 1),
                new EmptyBorder(15, 15, 15, 15)));

        JLabel leftTitle = new JLabel("📋 Available Tables");
        leftTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        leftTitle.setForeground(DARK);
        leftTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        leftPanel.add(leftTitle, BorderLayout.NORTH);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String table : allowedTables) {
            listModel.addElement("📊 " + table);
        }
        JList<String> tableList = new JList<>(listModel);
        tableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableList.setBackground(new Color(248, 249, 250));
        tableList.setSelectionBackground(PRIMARY);
        tableList.setSelectionForeground(Color.WHITE);
        tableList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane listScroll = new JScrollPane(tableList);
        listScroll.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230), 1));
        leftPanel.add(listScroll, BorderLayout.CENTER);

        // Right side - Modern table content
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(222, 226, 230), 1),
                new EmptyBorder(20, 20, 20, 20)));

        JLabel rightTitle = new JLabel("📈 Table Data");
        rightTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        rightTitle.setForeground(DARK);
        rightTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        rightPanel.add(rightTitle, BorderLayout.NORTH);

        // Modern table
        DefaultTableModel tableModel = new DefaultTableModel();
        JTable dataTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
                }
                return c;
            }
        };
        dataTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dataTable.setRowHeight(35);
        dataTable.setSelectionBackground(new Color(52, 152, 219, 100));
        dataTable.setSelectionForeground(DARK);
        dataTable.setGridColor(new Color(222, 226, 230));
        dataTable.setShowVerticalLines(true);
        dataTable.setShowHorizontalLines(true);

        JScrollPane tableScroll = new JScrollPane(dataTable);
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230), 1));
        tableScroll.getViewport().setBackground(Color.WHITE);
        rightPanel.add(tableScroll, BorderLayout.CENTER);

        // Modern operations panel
        JPanel opsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        opsPanel.setBackground(Color.WHITE);
        opsPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(222, 226, 230)));

        JButton refreshBtn = new JButton("🔄 Refresh");
        styleModernButton(refreshBtn, INFO);

        JButton createBtn = new JButton("➕ Create");
        styleModernButton(createBtn, SUCCESS);

        JButton updateBtn = new JButton("✏️ Update");
        styleModernButton(updateBtn, WARNING);

        JButton deleteBtn = new JButton("🗑️ Delete");
        styleModernButton(deleteBtn, DANGER);

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
                String tableName = tableList.getSelectedValue().replace("📊 ", "");
                loadTableData(tableName, tableModel);
            }
        });

        refreshBtn.addActionListener(e -> {
            if (tableList.getSelectedValue() != null) {
                String tableName = tableList.getSelectedValue().replace("📊 ", "");
                loadTableData(tableName, tableModel);
            }
        });

        createBtn.addActionListener(e -> {
            if (tableList.getSelectedValue() != null) {
                String tableName = tableList.getSelectedValue().replace("📊 ", "");
                showCreateDialog(tableName, tableModel);
            }
        });

        updateBtn.addActionListener(e -> {
            int row = dataTable.getSelectedRow();
            if (row >= 0 && tableList.getSelectedValue() != null) {
                String tableName = tableList.getSelectedValue().replace("📊 ", "");
                showUpdateDialog(tableName, tableModel, dataTable, row);
            } else {
                showError("Please select a row to update");
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = dataTable.getSelectedRow();
            if (row >= 0 && tableList.getSelectedValue() != null) {
                String tableName = tableList.getSelectedValue().replace("📊 ", "");
                deleteRecord(tableName, tableModel, dataTable, row);
            } else {
                showError("Please select a row to delete");
            }
        });

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);

        wrapper.add(splitPane, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createMyAccountPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Create a modern card layout
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(222, 226, 230), 1),
                new EmptyBorder(25, 25, 25, 25)));

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setBackground(Color.WHITE);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        // Load data immediately without refresh button
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                double balance = businessLayer.viewAccountBalance(userId);
                StringBuilder sb = new StringBuilder();
                sb.append("💰 ACCOUNT OVERVIEW\n");
                sb.append("═".repeat(50)).append("\n\n");
                sb.append(String.format("Current Balance: %.2f credits\n\n", balance));

                try {
                    ArrayList<StudentRouteRegistration> registrations = businessLayer.getStudentRegistrations(userId);
                    sb.append("📋 REGISTERED ROUTES\n");
                    sb.append("═".repeat(50)).append("\n");
                    if (registrations.isEmpty()) {
                        sb.append("No routes registered yet.\n");
                    } else {
                        for (StudentRouteRegistration reg : registrations) {
                            String routeName = businessLayer.getRouteNameById(reg.getRouteId());
                            String routeCode = businessLayer.getRouteCodeById(reg.getRouteId());
                            String status = reg.getStatus();
                            String statusIcon = status.equals("Active") ? "✅" : "⏸️";
                            sb.append(String.format("%s %s (%s) - %s\n",
                                    statusIcon, routeName, routeCode, status));
                        }
                    }
                } catch (Exception ex) {
                    sb.append("\n⚠️ Error loading routes: ").append(ex.getMessage());
                }

                area.setText(sb.toString());
                return null;
            }
        };
        worker.execute();

        contentPanel.add(new JScrollPane(area), BorderLayout.CENTER);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTransferCreditsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(400);

        // Left - Transfer form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(222, 226, 230), 1),
                new EmptyBorder(25, 25, 25, 25)));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("💳 Transfer Credits");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(DARK);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        formPanel.add(title, c);

        c.gridy++;
        c.gridwidth = 1;
        JLabel destLabel = new JLabel("Destination User ID:");
        destLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(destLabel, c);

        c.gridx = 1;
        JTextField destId = createModernTextField();
        formPanel.add(destId, c);

        c.gridx = 0;
        c.gridy++;
        JLabel amountLabel = new JLabel("Amount (credits):");
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(amountLabel, c);

        c.gridx = 1;
        JTextField amount = createModernTextField();
        formPanel.add(amount, c);

        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;
        JButton transfer = new JButton("🚀 Transfer Credits");
        styleModernButton(transfer, ACCENT);
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
        formPanel.add(transfer, c);

        // Right - Students list
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(Color.WHITE);
        listPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(222, 226, 230), 1),
                new EmptyBorder(25, 25, 25, 25)));

        JLabel listTitle = new JLabel("🎓 Available Students");
        listTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        listTitle.setForeground(DARK);
        listTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        listPanel.add(listTitle, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new String[] { "ID", "Name", "Balance" }, 0);
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(30);

        // Load student data immediately
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    ResultSet rs = businessLayer.viewTable("USERS");
                    if (rs != null) {
                        while (rs.next()) {
                            if (rs.getString("role").equals("Student")) {
                                int studentId = rs.getInt("user_id");
                                double balance = businessLayer.viewAccountBalance(studentId);
                                model.addRow(new Object[] {
                                        studentId,
                                        rs.getString("full_name"),
                                        String.format("%.2f", balance)
                                });
                            }
                        }
                    }
                } catch (Exception ex) {
                    showError("Error loading students: " + ex.getMessage());
                }
                return null;
            }
        };
        worker.execute();

        listPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        splitPane.setLeftComponent(formPanel);
        splitPane.setRightComponent(listPanel);
        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAvailableRoutesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(222, 226, 230), 1),
                new EmptyBorder(25, 25, 25, 25)));

        DefaultTableModel model = new DefaultTableModel(
                new String[] { "ID", "Name", "Code", "Distance", "Duration", "Credits" }, 0);
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(35);

        // Load routes data immediately
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    ArrayList<Route> routes = businessLayer.viewAvailableRoutes();
                    for (Route route : routes) {
                        model.addRow(new Object[] {
                                route.getRouteId(), route.getRouteName(),
                                route.getRouteCode(), route.getDistanceKm() + " km",
                                route.getEstimatedDurationMinutes() + " min",
                                route.getCreditsRequired() + " ⭐"
                        });
                    }
                } catch (Exception ex) {
                    showError("Error loading routes: " + ex.getMessage());
                }
                return null;
            }
        };
        worker.execute();

        contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(new EmptyBorder(15, 0, 0, 0));

        JButton register = new JButton("📝 Register for Selected Route");
        styleModernButton(register, SUCCESS);
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
        bottom.add(register);

        contentPanel.add(bottom, BorderLayout.SOUTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMyBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(222, 226, 230), 1),
                new EmptyBorder(25, 25, 25, 25)));

        DefaultTableModel model = new DefaultTableModel(
                new String[] { "Route", "Code", "Status", "Registered Date" }, 0);
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(35);

        // Load bookings data immediately
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    ArrayList<StudentRouteRegistration> registrations = businessLayer.getStudentRegistrations(userId);
                    for (StudentRouteRegistration reg : registrations) {
                        String statusIcon = reg.getStatus().equals("Active") ? "✅" : "⏸️";
                        model.addRow(new Object[] {
                                businessLayer.getRouteNameById(reg.getRouteId()),
                                businessLayer.getRouteCodeById(reg.getRouteId()),
                                statusIcon + " " + reg.getStatus(),
                                reg.getRegistrationDate()
                        });
                    }
                } catch (Exception ex) {
                    showError("Error loading bookings: " + ex.getMessage());
                }
                return null;
            }
        };
        worker.execute();

        contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMySchedulePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(222, 226, 230), 1),
                new EmptyBorder(25, 25, 25, 25)));

        DefaultTableModel model = new DefaultTableModel(
                new String[] { "Route", "Day", "Departure", "Arrival", "Shuttle" }, 0);
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(35);

        // Load schedule data immediately
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    ArrayList<StudentRouteRegistration> registrations = businessLayer.getStudentRegistrations(userId);
                    for (StudentRouteRegistration reg : registrations) {
                        int routeId = reg.getRouteId();
                        ArrayList<ShuttleSchedule> schedules = businessLayer.getSchedulesForRoute(routeId);
                        for (ShuttleSchedule schedule : schedules) {
                            model.addRow(new Object[] {
                                    businessLayer.getRouteNameById(routeId),
                                    schedule.getDayOfWeek(),
                                    schedule.getDepartureTime(),
                                    schedule.getArrivalTime(),
                                    businessLayer.getShuttleNumberById(schedule.getShuttleId())
                            });
                        }
                    }
                } catch (Exception ex) {
                    showError("Error loading schedule: " + ex.getMessage());
                }
                return null;
            }
        };
        worker.execute();

        contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTrackShuttlesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(222, 226, 230), 1),
                new EmptyBorder(25, 25, 25, 25)));

        DefaultTableModel model = new DefaultTableModel(
                new String[] { "Shuttle", "📍 Latitude", "📍 Longitude", "🚀 Speed", "🕐 Last Update" }, 0);
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(35);

        // Load shuttle locations immediately
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    ArrayList<ShuttleLocation> locations = businessLayer.getShuttleLocations();
                    for (ShuttleLocation loc : locations) {
                        model.addRow(new Object[] {
                                businessLayer.getShuttleNumberById(loc.getShuttleId()),
                                String.format("%.6f", loc.getLatitude()),
                                String.format("%.6f", loc.getLongitude()),
                                loc.getSpeedKmh() + " km/h",
                                loc.getTimestamp()
                        });
                    }
                } catch (Exception ex) {
                    showError("Error loading shuttle locations: " + ex.getMessage());
                }
                return null;
            }
        };
        worker.execute();

        contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    // Modern UI Helper Methods
    private JTextField createModernTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14)); 
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(12, 15, 12, 15)));
        field.setBackground(new Color(248, 249, 250));
        field.setForeground(DARK); // ensure text is visible
        field.setCaretColor(DARK);
        field.setOpaque(true);
        field.setPreferredSize(new Dimension(500, 42)); // wider / taller input
        return field;
    }

    private JPasswordField createModernPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // larger, readable font
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(12, 15, 12, 15)));
        field.setBackground(new Color(248, 249, 250));
        field.setForeground(DARK);
        field.setCaretColor(DARK);
        field.setOpaque(true);
        field.setPreferredSize(new Dimension(500, 42)); // wider / taller input
        return field;
    }

    private void styleModernButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.black);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        // Add hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(color.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(color);
            }
        });
    }

    // Custom TabbedPane UI for modern look
    class ModernTabbedPaneUI extends javax.swing.plaf.metal.MetalTabbedPaneUI {
        @Override
        protected void installDefaults() {
            super.installDefaults();
            highlight = PRIMARY;
            lightHighlight = PRIMARY.brighter();
            shadow = PRIMARY.darker();
            darkShadow = PRIMARY.darker().darker();
            focus = PRIMARY;
        }

        @Override
        protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex,
                int x, int y, int w, int h, boolean isSelected) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (isSelected) {
                g2d.setColor(PRIMARY);
            } else {
                g2d.setColor(new Color(248, 249, 250));
            }
            g2d.fillRoundRect(x + 2, y + 2, w - 4, h - 2, 15, 15);
        }

        @Override
        protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
            // Custom border painting
        }
    }

    // Existing methods (keep all your existing functionality)
    private void connectDB() {
        if (businessLayer.connect()) {
            connectBtn.setEnabled(false);
            disconnectBtn.setEnabled(true);
            loginBtn.setEnabled(true);
            statusLabel.setText("✅ Connected successfully!");
            statusLabel.setForeground(SUCCESS);
        } else {
            statusLabel.setText("❌ Connection failed!");
            statusLabel.setForeground(DANGER);
        }
    }

    private void disconnectDB() {
        if (businessLayer.disconnect()) {
            connectBtn.setEnabled(true);
            disconnectBtn.setEnabled(false);
            loginBtn.setEnabled(false);
            statusLabel.setText("🔌 Disconnected");
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

            statusLabel.setText("✅ Login successful!");
            statusLabel.setForeground(SUCCESS);

            if (userRole.equals("Admin")) {
                cardLayout.show(mainPanel, "ADMIN");
            } else if (userRole.equals("Driver")) {
                cardLayout.show(mainPanel, "DRIVER");
            } else if (userRole.equals("Student")) {
                cardLayout.show(mainPanel, "STUDENT");
            }
        } else {
            statusLabel.setText("❌ Invalid credentials!");
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
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }
        } catch (Exception ex) {
            showError("Error displaying data: " + ex.getMessage());
        }
    }

    private void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "✅ Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "❌ Error",
                JOptionPane.ERROR_MESSAGE);
    }

    // Keep all your existing helper classes and methods...
    // (Foreign key handling, table operations, etc. remain the same)

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
                return new String[] { "USERS", "full_name", "username" };
            }
        }

        if (columnName.equalsIgnoreCase("driver_id")) {
            return new String[] { "USERS", "full_name", "username" };
        }

        if (columnName.equalsIgnoreCase("student_id")) {
            return new String[] { "USERS", "full_name", "username" };
        }

        if (columnName.equalsIgnoreCase("route_id")) {
            return new String[] { "ROUTE", "route_name", "route_code" };
        }

        if (columnName.equalsIgnoreCase("shuttle_id")) {
            return new String[] { "SHUTTLE", "shuttle_number", "license_plate" };
        }

        return null;
    }

    // Populate dropdown with foreign key options
    private JComboBox<ForeignKeyItem> createForeignKeyDropdown(String refTable, String displayCol,
            String secondaryCol) {
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
                        inputComponents[i - 1] = field;
                        panel.add(field);
                    }
                    // Check if this is a foreign key
                    else if (colName.toLowerCase().endsWith("_id")) {
                        String[] fkInfo = getForeignKeyInfo(tableName, colName);
                        if (fkInfo != null) {
                            JComboBox<ForeignKeyItem> combo = createForeignKeyDropdown(
                                    fkInfo[0], fkInfo[1], fkInfo[2]);
                            inputComponents[i - 1] = combo;
                            panel.add(combo);
                        } else {
                            // Fallback to text field if FK info not found
                            JTextField field = new JTextField(20);
                            inputComponents[i - 1] = field;
                            panel.add(field);
                        }
                    }
                    // Regular field
                    else {
                        JTextField field = new JTextField(20);
                        inputComponents[i - 1] = field;
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
                JComponent component = inputComponents[i - 1];
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
                JComponent component = inputComponents[i - 1];
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
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // Modern styling
            UIManager.put("Button.arc", 20);
            UIManager.put("Component.arc", 20);
            UIManager.put("ProgressBar.arc", 20);
            UIManager.put("TextComponent.arc", 10);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            Group2PL app = new Group2PL();
            app.setVisible(true);
        });
    }
}
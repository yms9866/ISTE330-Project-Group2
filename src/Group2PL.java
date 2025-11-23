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
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
    private final Color INFO = new Color(52, 152, 219);
    private final Color DARK = new Color(44, 62, 80);
    private final Color ACCENT = new Color(155, 89, 182);

    public Group2PL() {
        businessLayer = new Group2BL();
        initGUI();
    }

    private void initGUI() {
        setTitle("Group 2: Shuttle Management System");
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
        fc.fill = GridBagConstraints.HORIZONTAL; 
        fc.weightx = 1.0; 
        usernameField = createModernTextField();
        formPanel.add(usernameField, fc);

        fc.gridy++;
        fc.gridwidth = 1; 
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passLabel.setForeground(DARK);
        formPanel.add(passLabel, fc);

        fc.gridy++;
        fc.gridwidth = 2;
        fc.fill = GridBagConstraints.HORIZONTAL; 
        fc.weightx = 1.0; 
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
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(statusLabel, c);

        panel.add(card, gbc);
        return panel;
    }

    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createModernHeader("Admin Dashboard", "System Administrator"), BorderLayout.NORTH);

        // Admin can access all tables
        String[] adminTables = { "USERS", "ACCOUNTS", "SHUTTLE", "ROUTE", "STOP",
                "SHUTTLE_LOCATION", "STUDENT_ROUTE_REGISTRATION", "SHUTTLE_SCHEDULE" };
        panel.add(createModernTableBrowserPanel(adminTables, true), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDriverPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createModernHeader("Driver Dashboard", "Shuttle Operations"), BorderLayout.NORTH);

        // Driver can access shuttle-related tables
        String[] driverTables = { "SHUTTLE", "SHUTTLE_LOCATION", "SHUTTLE_SCHEDULE", "ROUTE", "STOP" };
        panel.add(createModernTableBrowserPanel(driverTables, false), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createModernHeader("Student Dashboard", "Campus Transportation"), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        tabs.setUI(new ModernTabbedPaneUI());

        tabs.addTab("My Account", createMyAccountPanel());
        tabs.addTab("Transfer Credits", createTransferCreditsPanel());
        tabs.addTab("Available Routes", createAvailableRoutesPanel());
        tabs.addTab("My Bookings", createMyBookingsPanel());
        tabs.addTab("My Schedule", createMySchedulePanel());
        tabs.addTab("Track Shuttles", createTrackShuttlesPanel());
        tabs.addTab("Transaction History", createTransactionHistoryPanel());
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

        JButton logout = new JButton("Logout");
        logout.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        logout.setPreferredSize(new Dimension(100, 40));
        logout.setForeground(Color.RED);
        logout.setBackground(new Color(255, 255, 255, 30));
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

        JLabel leftTitle = new JLabel("Available Tables");
        leftTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        leftTitle.setForeground(DARK);
        leftTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        leftPanel.add(leftTitle, BorderLayout.NORTH);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String table : allowedTables) {
            listModel.addElement(table);
        }

        JList<String> tableList = new JList<>(listModel);
        tableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableList.setOpaque(false); 
        tableList.setFixedCellHeight(45);
        tableList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableList.setCellRenderer(new ListCellRenderer<String>() {

            Color normal = PRIMARY;
            Color selected = PRIMARY.darker();
            Color text = Color.WHITE;

            JLabel cell = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    int w = getWidth();
                    int h = getHeight();
                    int arrow = 24; // arrow head size

                    boolean isSelected = Boolean.TRUE.equals(getClientProperty("selected"));
                    Color fill = isSelected ? selected : normal;

                    Polygon p = new Polygon();
                    p.addPoint(0, 0);
                    p.addPoint(w - arrow, 0);
                    p.addPoint(w, h / 2);
                    p.addPoint(w - arrow, h);
                    p.addPoint(0, h);

                    g2.setColor(fill);
                    g2.fillPolygon(p);

                    super.paintComponent(g2);
                }
            };

            {
                cell.setOpaque(false);
                cell.setForeground(text);
                cell.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                cell.setBorder(new EmptyBorder(10, 20, 10, 20));
            }

            @Override
            public Component getListCellRendererComponent(
                    JList<? extends String> list, String value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                cell.setText(value);
                cell.putClientProperty("selected", isSelected);
                return cell;
            }
        });

        // Scrollpanel stays same
        JScrollPane listScroll = new JScrollPane(tableList);
        listScroll.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230), 1));
        listScroll.getViewport().setOpaque(false);
        listScroll.setOpaque(false);

        leftPanel.add(listScroll, BorderLayout.CENTER);

        // Right side - Modern table content
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(222, 226, 230), 1),
                new EmptyBorder(20, 20, 20, 20)));

        JLabel rightTitle = new JLabel("Table Data");
        rightTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        rightTitle.setForeground(DARK);
        rightTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        rightPanel.add(rightTitle, BorderLayout.NORTH);


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

        JButton refreshBtn = new JButton("Refresh");
        styleModernButton(refreshBtn, INFO);

        
        JButton createBtn = new JButton("Create User");
        createBtn.setEnabled(false);
        createBtn.setVisible(false);
        styleModernButton(createBtn, SUCCESS);
        opsPanel.add(refreshBtn);
        opsPanel.add(createBtn);

        rightPanel.add(opsPanel, BorderLayout.SOUTH);
        
        tableList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableList.getSelectedValue() != null) {
                String tableName = tableList.getSelectedValue();
                boolean isfullAcces = businessLayer.getUserById(currentUser.getUserId()).getRole()
                        .equalsIgnoreCase("Admin");
                loadTableData(tableName, tableModel,isfullAcces, currentUser.getUserId());
                // Only enable create when USERS table is selected and current user is Admin
                boolean isAdmin = userRole != null && userRole.equalsIgnoreCase("Admin");
                createBtn.setEnabled(isAdmin && tableName.equalsIgnoreCase("USERS"));
                createBtn.setVisible(isAdmin && tableName.equalsIgnoreCase("USERS"));
            }
        });

        refreshBtn.addActionListener(e -> {
            if (tableList.getSelectedValue() != null) {
                String tableName = tableList.getSelectedValue();
                boolean isfullAcces = businessLayer.getUserById(currentUser.getUserId()).getRole()
                        .equalsIgnoreCase("Admin");
                loadTableData(tableName, tableModel,isfullAcces, currentUser.getUserId());
            }
        });

        createBtn.addActionListener(e -> {

            boolean isAdmin = userRole != null && userRole.equalsIgnoreCase("Admin");
            if (!isAdmin) {
                showError("Create action is only allowed for the USERS table (Admin only).");
                return;
            }
            showCreateUserDialog(tableModel);
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
        // load button
        JButton loadButton = new JButton("Load");
        styleModernButton(loadButton, INFO);
        loadButton.addActionListener(e -> {
            double balance = businessLayer.viewAccountBalance(currentUser.getUserId());
            StringBuilder sb = new StringBuilder();
            sb.append("ACCOUNT OVERVIEW\n");
            sb.append(String.format("Current Balance: %.2f credits\n\n", balance));

            try {
                ArrayList<StudentRouteRegistration> registrations = businessLayer.getStudentRegistrations(currentUser.getUserId());
                sb.append("REGISTERED ROUTES\n");
                if (registrations.isEmpty()) {
                    sb.append("No routes registered yet.\n");
                } else {
                    for (StudentRouteRegistration reg : registrations) {
                        String routeName = businessLayer.getRouteNameById(reg.getRouteId());
                        String routeCode = businessLayer.getRouteCodeById(reg.getRouteId());
                        String status = reg.getStatus();
                        String statusIcon = status.equals("Active") ? "Active" : "Paused";
                        sb.append(String.format("%s %s (%s) - %s\n",
                                statusIcon, routeName, routeCode, status));
                    }
                }
            } catch (Exception ex) {
                sb.append("\nError loading routes: ").append(ex.getMessage());
            }   

            area.setText(sb.toString());
        });
        
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(new EmptyBorder(15, 0, 0, 0));
        bottom.add(loadButton);

        contentPanel.add(bottom, BorderLayout.SOUTH);
        contentPanel.add(new JScrollPane(area), BorderLayout.CENTER);
        panel.add(contentPanel, BorderLayout.CENTER);


        return panel;
    }

    private JPanel createTransferCreditsPanel() {
        JPanel panel = new JPanel(new GridBagLayout()); 
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));
        panel.setBackground(Color.WHITE);

        // The card itself
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setPreferredSize(new Dimension(400, 320)); // width, height
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(222, 226, 230), 1),
                new EmptyBorder(25, 25, 25, 25)));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Transfer Credits");
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
        c.gridy = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;

        JTextField destId = createModernTextField();
        destId.setPreferredSize(new Dimension(200, 35));
        formPanel.add(destId, c);

        c.gridx = 0;
        c.gridy++;
        JLabel amountLabel = new JLabel("Amount (credits):");
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(amountLabel, c);

        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;

        JTextField amount = createModernTextField();
        amount.setPreferredSize(new Dimension(200, 35));
        formPanel.add(amount, c);

        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;
        JButton transfer = new JButton("Transfer Credits");
        styleModernButton(transfer, ACCENT);
        transfer.addActionListener(e -> {
            try {
                String result = businessLayer.transferCredits(
                        currentUser.getUserId(),
                        Integer.parseInt(destId.getText()),
                        Double.parseDouble(amount.getText()));

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

        GridBagConstraints center = new GridBagConstraints();
        center.anchor = GridBagConstraints.CENTER;
        panel.add(formPanel, center);

        return panel;
    }

    private JPanel createAvailableRoutesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Card container
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
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JButton loadButton = new JButton("Load Routes");
        styleModernButton(loadButton, INFO);
        loadButton.addActionListener(e -> {
            try {
                ArrayList<Route> routes = businessLayer.viewAvailableRoutes();
                for (Route route : routes) {
                    model.addRow(new Object[] {
                            route.getRouteId(),
                            route.getRouteName(),
                            route.getRouteCode(),
                            route.getDistanceKm() + " km",
                            route.getEstimatedDurationMinutes() + " min",
                            route.getCreditsRequired() + " Credits"
                    });
                }
            } catch (Exception ex) {
                showError("Error loading routes: " + ex.getMessage());
            }
        });

        contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);

   
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(new EmptyBorder(15, 0, 0, 0));
        bottom.add(loadButton);

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

        JButton loadButton = new JButton("Load Bookings");
        styleModernButton(loadButton, INFO);
        loadButton.addActionListener(e -> { 
                try {
                    ArrayList<StudentRouteRegistration> registrations = businessLayer.getStudentRegistrations(userId);
                    for (StudentRouteRegistration reg : registrations) {
                        String statusIcon = reg.getStatus().equals("Active") ? "Active" : "Paused";
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
            
            });
        contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(contentPanel, BorderLayout.CENTER);
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(new EmptyBorder(15, 0, 0, 0));
        bottom.add(loadButton);
        contentPanel.add(bottom, BorderLayout.SOUTH);


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
        // load button
        JButton loadButton = new JButton("Load Schedule");
        styleModernButton(loadButton, INFO);
        loadButton.addActionListener(e -> {
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
        });

        contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(contentPanel, BorderLayout.CENTER);
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(new EmptyBorder(15, 0, 0, 0));
        bottom.add(loadButton);
        contentPanel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTransactionHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        DefaultTableModel model = new DefaultTableModel(
                new String[] { "Date", "Receiver Name", "Sender Name", "Amount"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JButton loadButton = new JButton("Load Transactions");
        styleModernButton(loadButton, INFO);
        loadButton.addActionListener(e -> {
            try {
                model.setRowCount(0); 
                ArrayList<Transaction> transactions = businessLayer.getTransactionHistory(currentUser.getUserId());
                System.out.println("Fetched " + transactions);
                for (Transaction t : transactions) {
                    model.addRow(new Object[] {
                            t.getTransactionDate(),
                            businessLayer.getUserNameById(t.getRecieverid()),
                            businessLayer.getUserNameById(t.getSenderid()),
                            t.getAmount(),
                    });
                }
            } catch (Exception ex) {
                showError("Error loading transactions: " + ex.getMessage());
            }
        });

        JButton downloadButton = new JButton("Download Report");
        styleModernButton(downloadButton, SUCCESS);
        downloadButton.addActionListener(e -> downloadTransactionReport(model));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(loadButton);
        buttonPanel.add(downloadButton);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void downloadTransactionReport(DefaultTableModel model) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Transaction Report");
        fileChooser.setSelectedFile(new File("transaction_report.csv"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".csv")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
            }
            
            try (PrintWriter writer = new PrintWriter(fileToSave)) {
                // Write header
                for (int i = 0; i < model.getColumnCount(); i++) {
                    writer.write(model.getColumnName(i));
                    if (i < model.getColumnCount() - 1) writer.write(",");
                }
                writer.write("\n");
                
                // Write data
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        Object value = model.getValueAt(i, j);
                        writer.write(value != null ? value.toString() : "");
                        if (j < model.getColumnCount() - 1) writer.write(",");
                    }
                    writer.write("\n");
                }
                
                showSuccess("Transaction report downloaded successfully!");
            } catch (IOException ex) {
                showError("Error saving file: " + ex.getMessage());
            }
        }
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
                new String[] { "Shuttle", "Latitude", "Longitude", "Speed", "Last Update" }, 0);
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(35);

        // load button
       JButton loadButton = new JButton("Load Locations");
       styleModernButton(loadButton, INFO);
        loadButton.addActionListener(e -> {

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
        });

        contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(contentPanel, BorderLayout.CENTER);
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(new EmptyBorder(15, 0, 0, 0));
        bottom.add(loadButton);
        contentPanel.add(bottom, BorderLayout.SOUTH);
        

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
        JOptionPane.showMessageDialog(this, msg, "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

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

        

        return null;
    }

    private JComboBox<ForeignKeyItem> createForeignKeyDropdown(String refTable, String displayCol,
            String secondaryCol) {
        JComboBox<ForeignKeyItem> combo = new JComboBox<>();
        combo.addItem(new ForeignKeyItem(0, "-- Select --"));

        try {
            ResultSet rs = businessLayer.viewTable(refTable, true, currentUser.getUserId());
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

    private void loadTableData(String tableName, DefaultTableModel model, boolean fullAccess, int userId) {
        try {
            ResultSet rs = businessLayer.viewTable(tableName , fullAccess, userId);
            displayResultSet(rs, model);
        } catch (Exception ex) {
            showError("Error loading table: " + ex.getMessage());
        }
    }

    private void showCreateUserDialog(DefaultTableModel model) {
        try {
            ResultSet rs = businessLayer.viewTable("USERS", true, userId);
            if (rs != null) {
                ResultSetMetaData meta = rs.getMetaData();
                int colCount = meta.getColumnCount();

                JPanel panel = new JPanel(new GridLayout(colCount, 2, 5, 5));
                JComponent[] inputComponents = new JComponent[colCount];

                for (int i = 1; i <= colCount; i++) {
                    String colName = meta.getColumnName(i);
                    if (colName.equalsIgnoreCase("created_at")) {
                        continue; 
                    }
                    String displayLabel = (colName.equalsIgnoreCase("password_hash"))
                                    ? "password"
                                    : colName;
                    panel.add(new JLabel(displayLabel + ":"));

                    if (colName.toLowerCase().endsWith("_id") && i == 1) {
                        JTextField field = new JTextField(20);
                        field.setText("AUTO");
                        field.setEnabled(false);
                        inputComponents[i - 1] = field;
                        panel.add(field);
                    }
                    else if (colName.toLowerCase().endsWith("_id")) {
                        String[] fkInfo = getForeignKeyInfo("USERS", colName);
                        if (fkInfo != null) {
                            JComboBox<ForeignKeyItem> combo = createForeignKeyDropdown(
                                    fkInfo[0], fkInfo[1], fkInfo[2]);
                            inputComponents[i - 1] = combo;
                            panel.add(combo);
                        } else {
                            JTextField field = new JTextField(20);
                            inputComponents[i - 1] = field;
                            panel.add(field);
                        }
                    }
                    else if(colName.equals("role")) {
                        String[] roles = { "Admin", "Driver", "Student" };
                        JComboBox<String> combo = new JComboBox<>(roles);
                        inputComponents[i - 1] = combo;
                        panel.add(combo);
                    }
                    else if (colName.equalsIgnoreCase("password_hash")) {
                        JTextField passField = new JTextField(20);
                        inputComponents[i - 1] = passField;
                        panel.add(passField);
                    }
                    else {
                        JTextField field = new JTextField(20);
                        inputComponents[i - 1] = field;
                        panel.add(field);
                    }
                }

                int result = JOptionPane.showConfirmDialog(this, panel,
                        "Create New Record in USERS", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    createUserRecord(meta, inputComponents, model);
                }
            }
        } catch (Exception ex) {
            showError("Error opening create dialog: " + ex.getMessage());
        }
    }

    private void createUserRecord(ResultSetMetaData meta,
            JComponent[] inputComponents, DefaultTableModel model) {
        try {
            ArrayList<String> valuesArray = new ArrayList<>();

            int colCount = meta.getColumnCount();
            for (int i = 1; i <= colCount; i++) {
                String value = "";

                JComponent comp = inputComponents[i - 1];
                if (comp instanceof JTextField) {
                    value = ((JTextField) comp).getText().trim();
                } else if (comp instanceof JComboBox) {
                    Object selectedItem = ((JComboBox<?>) comp).getSelectedItem();
                    if (selectedItem instanceof ForeignKeyItem) {
                        value = String.valueOf(((ForeignKeyItem) selectedItem).getId());
                    } else if (selectedItem != null) {
                        value = selectedItem.toString();
                    }
                }
                valuesArray.add(value);
            }
            
            boolean success = businessLayer.addUser(valuesArray.get(1), valuesArray.get(2), valuesArray.get(3),
                    valuesArray.get(4), valuesArray.get(5), valuesArray.get(6));
            
            if (success) {
                showSuccess("Record created successfully!");
                loadTableData("USERS", model, true, currentUser.getUserId());
            } else {
                showError("Failed to create record.");
            }
        } catch (Exception ex) {
            showError("Error creating record: " + ex.getMessage());
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
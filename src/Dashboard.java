import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Dashboard extends JFrame {

    // =====================================================
    // NAVIGATION - ORIGINAL BUTTONS PRESERVED
    // =====================================================

    public JButton dashboardButton = new JButton("Dashboard");
    public JButton productsButton = new JButton("Products");
    public JButton salesButton = new JButton("Sales");
    public JButton reportsButton = new JButton("Reports");
    public JButton logoutButton = new JButton("Logout");

    private JButton activeButton = dashboardButton;

    private JPanel contentPanel;
    private JPanel dashboardContent;
    private JPanel inventoryAlertsList;
    private JPanel activityList;

    // FIX: Keep the Recent Activity scroll pane accessible
    // so its scroll position can be preserved on refresh.
    private JScrollPane activityScroll;

    private String currentTitle = "Dashboard";

    private final Database db;
    private JLabel admin;
    private Computes compute;

    private JLabel totalProductsValueLabel;
    private JLabel totalOrdersValueLabel;
    private JLabel totalSalesValueLabel;

    private javax.swing.Timer dashboardRefreshTimer;

    public static Admin user;
    private static String user_name;
    private static String user_password;

    // =====================================================
    // COLORS
    // =====================================================

    private static final Color BACKGROUND = new Color(0x0F172A);
    private static final Color PANEL = new Color(0x111C3D);
    private static final Color HEADER = new Color(0x0B1026);
    private static final Color CYAN = new Color(0x22D3EE);
    private static final Color WHITE = new Color(0xE2E8F0);
    private static final Color GRAY = new Color(0x94A3B8);
    private static final Color BORDER = new Color(0x263653);
    private static final Color GREEN = new Color(0x22C55E);
    private static final Color RED = new Color(0xEF4444);
    private static final Color YELLOW = new Color(0xFBBF24);
    private static final Color PURPLE = new Color(0xA78BFA);

    // =====================================================
    // START
    // =====================================================

    public static void start() {

        Login login = new Login();
        Database data = new Database();
        Computes compute = new Computes(data);

        try {
            for (String line :
                    Files.readAllLines(Paths.get("src/Account.txt"))) {

                String[] parts = line.split("=", 2);

                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();

                    if (key.equals("username")) {
                        user_name = value;
                    }

                    if (key.equals("Password")) {
                        user_password = value;
                    }
                }
            }

        } catch (IOException e) {
            System.out.println(
                    "Error reading Account.txt: " + e.getMessage()
            );
        }

        user = new Admin(user_name, user_password);

        if (!data.userExists(user.admin_username)) {
            data.createAccount(
                    user.admin_username,
                    user.password
            );
        }

        if (data.IsLogIn()) {
            login.dispose();
            new Dashboard(data.getusername(), data, compute);
            return;
        }

        login.setVisible(true);

        login.loginButton.addActionListener(e -> {

            if (login.loginButtonActionPerformed()) {

                String username =
                        login.usernameField.getText().trim();

                String password =
                        new String(login.passwordField.getPassword());

                if (!data.checkLogin(username, password)) {
                    JOptionPane.showMessageDialog(
                            login,
                            "Unable to verify the login session."
                    );
                    return;
                }

                login.dispose();
                new Dashboard(username, data, compute);

            } else {
                login.usernameField.setBorder(
                        BorderFactory.createLineBorder(RED, 1)
                );

                login.passwordField.setBorder(
                        BorderFactory.createLineBorder(RED, 1)
                );
            }
        });
    }

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public Dashboard(String username, Database db, Computes compute) {

        this.db = db;
        this.compute = compute;

        this.compute.compute();

        Logout logout = new Logout(this, db);
        logout.log_out();

        setTitle("Computer Shop Management System");
        setSize(1000, 600);
        setMinimumSize(new Dimension(850, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // =================================================
        // SIDEBAR - ORIGINAL LAYOUT AND BUTTONS PRESERVED
        // =================================================

        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(0x080B1A));
        sidebar.setPreferredSize(new Dimension(150, 600));
        sidebar.setLayout(new GridLayout(10, 1, 0, 5));

        JPanel sidebarTitle = new JPanel();
        sidebarTitle.setOpaque(false);
        sidebarTitle.setLayout(
                new BoxLayout(sidebarTitle, BoxLayout.Y_AXIS)
        );
        sidebarTitle.setBorder(
                new EmptyBorder(10, 10, 10, 10)
        );

        JLabel mainTitle = new JLabel("COMPUTER SHOP");
        JLabel subTitle = new JLabel("MANAGEMENT SYSTEM");

        mainTitle.setForeground(Color.WHITE);
        mainTitle.setFont(new Font("Arial", Font.BOLD, 8));

        subTitle.setForeground(CYAN);
        subTitle.setFont(new Font("Arial", Font.BOLD, 10));

        sidebarTitle.add(mainTitle);
        sidebarTitle.add(subTitle);
        sidebar.add(sidebarTitle);

        dashboardButton.setIcon(
                loadIcon("images/Nav_pics/Dashboard.png")
        );
        productsButton.setIcon(
                loadIcon("images/Nav_pics/Product.png")
        );
        salesButton.setIcon(
                loadIcon("images/Nav_pics/Sales.png")
        );
        reportsButton.setIcon(
                loadIcon("images/Nav_pics/Reports.png")
        );
        logoutButton.setIcon(
                loadIcon("images/Nav_pics/Logout.png")
        );

        styleButton(dashboardButton);
        styleButton(productsButton);
        styleButton(salesButton);
        styleButton(reportsButton);
        styleButton(logoutButton);

        sidebar.add(dashboardButton);
        sidebar.add(productsButton);
        sidebar.add(salesButton);
        sidebar.add(reportsButton);

        for (int i = 0; i < 4; i++) {
            JPanel gap = new JPanel();
            gap.setOpaque(false);
            sidebar.add(gap);
        }

        sidebar.add(logoutButton);

        // =================================================
        // MAIN PANEL WITH GRADIENT
        // =================================================

        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_RENDERING,
                        RenderingHints.VALUE_RENDER_QUALITY
                );

                g2.setPaint(new GradientPaint(
                        0, 0,
                        new Color(0x00C6FF),
                        getWidth(), getHeight(),
                        new Color(0x12002F)
                ));

                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };

        // =================================================
        // HEADER - ORIGINAL BUTTON STYLE PRESERVED
        // =================================================

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER);
        header.setPreferredSize(new Dimension(0, 65));

        JLabel upperTitle = new JLabel("  " + currentTitle);
        upperTitle.setForeground(WHITE);
        upperTitle.setFont(new Font("Arial", Font.BOLD, 25));

        admin = new JLabel(db.getusername());

        ImageIcon userIcon = new ImageIcon("images/user.png");

        if (userIcon.getIconWidth() > 0) {
            admin.setIcon(new ImageIcon(
                    userIcon.getImage().getScaledInstance(
                            20, 20, Image.SCALE_SMOOTH
                    )
            ));
        }

        admin.setForeground(new Color(0xCBD5E1));
        admin.setFont(new Font("Arial", Font.PLAIN, 18));

        JPanel rightPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 30, 8)
        );
        rightPanel.setOpaque(false);

        JButton settingsButton = new JButton();

        ImageIcon settingsIcon =
                new ImageIcon("images/settings.png");

        if (settingsIcon.getIconWidth() > 0) {
            settingsButton.setIcon(new ImageIcon(
                    settingsIcon.getImage().getScaledInstance(
                            20, 20, Image.SCALE_SMOOTH
                    )
            ));
        }

        settingsButton.setPreferredSize(new Dimension(45, 45));
        settingsButton.setOpaque(false);
        settingsButton.setContentAreaFilled(false);
        settingsButton.setBorderPainted(false);
        settingsButton.setFocusPainted(false);
        settingsButton.setToolTipText("Settings");

        settingsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                settingsButton.setBorder(
                        BorderFactory.createMatteBorder(
                                0, 0, 3, 0, CYAN
                        )
                );
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                settingsButton.setBorder(
                        BorderFactory.createEmptyBorder()
                );
            }
        });

        rightPanel.add(admin);
        rightPanel.add(settingsButton);

        header.add(upperTitle, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        mainPanel.add(header, BorderLayout.NORTH);

        // =================================================
        // DASHBOARD CONTENT
        // =================================================

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND);

        dashboardContent = buildDashboard();

        contentPanel.add(
                dashboardContent,
                BorderLayout.CENTER
        );

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // =================================================
        // NAVIGATION ACTIONS - ORIGINAL BEHAVIOR PRESERVED
        // =================================================

        dashboardButton.addActionListener(e -> {

            setActiveButton(dashboardButton);
            currentTitle = "Dashboard";
            upperTitle.setText("  " + currentTitle);

            refreshDashboard();

            contentPanel.removeAll();
            contentPanel.add(
                    dashboardContent,
                    BorderLayout.CENTER
            );

            contentPanel.revalidate();
            contentPanel.repaint();
        });

        productsButton.addActionListener(e -> {

            setActiveButton(productsButton);
            currentTitle = "Products";
            upperTitle.setText("  " + currentTitle);

            contentPanel.removeAll();
            contentPanel.add(new Products(db), BorderLayout.CENTER);

            contentPanel.revalidate();
            contentPanel.repaint();
        });

        salesButton.addActionListener(e -> {

            setActiveButton(salesButton);
            currentTitle = "Sales";
            upperTitle.setText("  " + currentTitle);

            contentPanel.removeAll();
            contentPanel.add(new Sales(db), BorderLayout.CENTER);

            contentPanel.revalidate();
            contentPanel.repaint();
        });

        reportsButton.addActionListener(e -> {

            setActiveButton(reportsButton);
            currentTitle = "Reports";
            upperTitle.setText("  " + currentTitle);

            contentPanel.removeAll();
            contentPanel.add(new Reports(db), BorderLayout.CENTER);

            contentPanel.revalidate();
            contentPanel.repaint();
        });

        settingsButton.addActionListener(e -> {
            settings settingsWindow =
                    new settings(this, db, user);
            settingsWindow.setVisible(true);
        });

        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);

        // =================================================
        // RESPONSIVE RESIZING - LAYOUT ONLY
        // =================================================

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (dashboardContent != null) {
                    dashboardContent.revalidate();
                    dashboardContent.repaint();
                }

                contentPanel.revalidate();
                contentPanel.repaint();
            }
        });

        // =================================================
        // AUTOMATIC REFRESH EVERY 5 SECONDS
        // =================================================

        dashboardRefreshTimer = new javax.swing.Timer(
                5000,
                e -> {
                    refreshStatistics();
                    refreshInventoryAlerts();
                    refreshRecentActivity();
                }
        );

        dashboardRefreshTimer.setRepeats(true);
        dashboardRefreshTimer.start();

        setVisible(true);
    }

    // =====================================================
    // BUILD DASHBOARD - RESPONSIVE
    // =====================================================

    private JPanel buildDashboard() {

        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(BACKGROUND);
        page.setBorder(new EmptyBorder(18, 20, 18, 20));

        JPanel body = new JPanel();
        body.setBackground(BACKGROUND);
        body.setLayout(
                new BoxLayout(body, BoxLayout.Y_AXIS)
        );

        // STATISTIC CARDS

        JPanel cards = new JPanel(
                new GridLayout(1, 3, 12, 0)
        );
        cards.setOpaque(false);
        cards.setAlignmentX(Component.LEFT_ALIGNMENT);
        cards.setPreferredSize(new Dimension(0, 125));
        cards.setMinimumSize(new Dimension(0, 100));
        cards.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 125)
        );

        cards.add(createStatCard(
                "TOTAL PRODUCTS",
                String.valueOf(db.getTotalProduct()),
                CYAN
        ));

        cards.add(createStatCard(
                "TOTAL ORDERS",
                String.valueOf(compute.completedOrders),
                PURPLE
        ));

        cards.add(createStatCard(
                "TOTAL SALES",
                String.format("₱%,.2f", compute.totalSales),
                GREEN
        ));

        body.add(cards);
        body.add(Box.createVerticalStrut(18));

        // INVENTORY ALERTS

        JPanel inventoryPanel = createInventoryPanel();
        inventoryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        inventoryPanel.setPreferredSize(new Dimension(0, 225));
        inventoryPanel.setMinimumSize(new Dimension(0, 180));
        inventoryPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 225)
        );

        body.add(inventoryPanel);
        body.add(Box.createVerticalStrut(18));

        // RECENT ACTIVITY

        JPanel activityPanel = createActivityPanel();
        activityPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        activityPanel.setPreferredSize(new Dimension(0, 300));
        activityPanel.setMinimumSize(new Dimension(0, 200));
        activityPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 300)
        );

        body.add(activityPanel);

        // MAIN SCROLL PANE

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.setBackground(BACKGROUND);
        scroll.getViewport().setBackground(BACKGROUND);

        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        JScrollBar verticalBar = scroll.getVerticalScrollBar();
        verticalBar.setUnitIncrement(16);
        verticalBar.setPreferredSize(new Dimension(6, 0));

        verticalBar.setUI(
                new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = CYAN;
                trackColor = BACKGROUND;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });

        page.add(scroll, BorderLayout.CENTER);
        return page;
    }

    // =====================================================
    // STATISTIC CARD
    // =====================================================

    private JPanel createStatCard(
            String title,
            String value,
            Color accent) {

        JPanel card = new JPanel(new BorderLayout(0, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(PANEL);
                g2.fillRoundRect(
                        0, 0, getWidth(), getHeight(), 20, 20
                );

                g2.setColor(BORDER);
                g2.drawRoundRect(
                        0, 0, getWidth() - 1,
                        getHeight() - 1, 20, 20
                );

                g2.setColor(accent);
                g2.fillRoundRect(
                        0, 0, 5, getHeight(), 5, 5
                );

                g2.dispose();
            }
        };

        card.setOpaque(false);
        card.setBorder(new EmptyBorder(13, 15, 13, 12));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(GRAY);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 10));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(WHITE);
        valueLabel.setFont(new Font(
                "Arial",
                Font.BOLD,
                value.length() > 8 ? 17 : 24
        ));

        if (title.equals("TOTAL PRODUCTS")) {
            totalProductsValueLabel = valueLabel;
        } else if (title.equals("TOTAL ORDERS")) {
            totalOrdersValueLabel = valueLabel;
        } else if (title.equals("TOTAL SALES")) {
            totalSalesValueLabel = valueLabel;
        }

        // Adjust text size according to the card width.
        card.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = card.getWidth();

                int fontSize = width < 170 ? 13
                        : width < 220 ? 17
                        : width < 280 ? 20
                        : 24;

                valueLabel.setFont(
                        new Font("Arial", Font.BOLD, fontSize)
                );
            }
        });

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    // =====================================================
    // AUTOMATIC STATISTICS REFRESH
    // =====================================================

    private void refreshStatistics() {
        try {
            compute.compute();

            if (totalProductsValueLabel != null) {
                totalProductsValueLabel.setText(
                        String.valueOf(db.getTotalProduct())
                );
            }

            if (totalOrdersValueLabel != null) {
                totalOrdersValueLabel.setText(
                        String.valueOf(compute.completedOrders)
                );
            }

            if (totalSalesValueLabel != null) {
                totalSalesValueLabel.setText(
                        String.format("₱%,.2f", compute.totalSales)
                );
            }

            if (dashboardContent != null) {
                dashboardContent.revalidate();
                dashboardContent.repaint();
            }

        } catch (Exception ex) {
            System.err.println("Statistics refresh failed:");
            ex.printStackTrace();
        }
    }

    // =====================================================
    // INVENTORY ALERTS PANEL
    // =====================================================

    private JPanel createInventoryPanel() {

        JPanel panel = createPanel();
        panel.setLayout(new BorderLayout(0, 12));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Inventory Alerts");
        title.setForeground(WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel subtitle = new JLabel(
                "Products that require your attention"
        );
        subtitle.setForeground(GRAY);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 11));

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(
                new BoxLayout(heading, BoxLayout.Y_AXIS)
        );

        heading.add(title);
        heading.add(Box.createVerticalStrut(4));
        heading.add(subtitle);

        inventoryAlertsList = new JPanel();
        inventoryAlertsList.setOpaque(false);
        inventoryAlertsList.setLayout(
                new BoxLayout(inventoryAlertsList, BoxLayout.Y_AXIS)
        );

        JScrollPane scroll = new JScrollPane(inventoryAlertsList);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        JScrollBar bar = scroll.getVerticalScrollBar();
        bar.setUnitIncrement(16);
        bar.setPreferredSize(new Dimension(6, 0));

        bar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = CYAN;
                trackColor = BACKGROUND;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });

        panel.add(heading, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        refreshInventoryAlerts();
        return panel;
    }

    // =====================================================
    // REFRESH INVENTORY ALERTS
    // =====================================================

    private void refreshInventoryAlerts() {

        if (inventoryAlertsList == null) {
            return;
        }

        inventoryAlertsList.removeAll();

        try {
            ArrayList<InventoryAlert> alerts = db.getStockAlert();

            if (alerts == null || alerts.isEmpty()) {
                JLabel emptyLabel = new JLabel(
                        "No inventory alerts found."
                );
                emptyLabel.setForeground(GRAY);
                emptyLabel.setFont(
                        new Font("Arial", Font.PLAIN, 12)
                );
                inventoryAlertsList.add(emptyLabel);

            } else {
                boolean hasAlert = false;

                for (InventoryAlert alert : alerts) {
                    if (alert == null) {
                        continue;
                    }

                    inventoryAlertsList.add(
                            createInventoryAlert(
                                    alert.productName,
                                    alert.category,
                                    alert.quantity,
                                    alert.outOfStock
                            )
                    );

                    inventoryAlertsList.add(
                            Box.createVerticalStrut(10)
                    );

                    hasAlert = true;
                }

                if (!hasAlert) {
                    JLabel emptyLabel = new JLabel(
                            "No inventory alerts found."
                    );
                    emptyLabel.setForeground(GRAY);
                    inventoryAlertsList.add(emptyLabel);
                }
            }

        } catch (Exception ex) {
            JLabel errorLabel = new JLabel(
                    "Unable to load inventory alerts."
            );
            errorLabel.setForeground(RED);
            inventoryAlertsList.add(errorLabel);

            System.err.println(
                    "Inventory alert refresh failed: "
                    + ex.getMessage()
            );
        }

        inventoryAlertsList.revalidate();
        inventoryAlertsList.repaint();
    }

    // =====================================================
    // INDIVIDUAL INVENTORY ALERT
    // =====================================================

    private JPanel createInventoryAlert(
            String productName,
            String category,
            int quantity,
            boolean outOfStock) {

        Color accent = outOfStock ? RED : YELLOW;

        String status = outOfStock
                ? "Out of Stock"
                : "Low Stock";

        JPanel item = new JPanel(new BorderLayout(8, 8));
        item.setBackground(new Color(0x172440));
        item.setBorder(new EmptyBorder(10, 10, 10, 10));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));
        item.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel details = new JPanel();
        details.setOpaque(false);
        details.setLayout(
                new BoxLayout(details, BoxLayout.Y_AXIS)
        );

        JLabel name = new JLabel(
                productName == null ? "" : productName
        );
        name.setForeground(WHITE);
        name.setFont(new Font("Arial", Font.BOLD, 12));

        JLabel categoryLabel = new JLabel(
                "Category: " + (category == null ? "" : category)
        );
        categoryLabel.setForeground(GRAY);
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 10));

        JLabel quantityLabel = new JLabel(
                "Remaining: " + quantity + " units"
        );
        quantityLabel.setForeground(accent);
        quantityLabel.setFont(new Font("Arial", Font.PLAIN, 11));

        details.add(name);
        details.add(Box.createVerticalStrut(3));
        details.add(categoryLabel);
        details.add(Box.createVerticalStrut(3));
        details.add(quantityLabel);

        JLabel statusLabel = new JLabel(status);
        statusLabel.setForeground(accent);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 10));

        item.add(details, BorderLayout.CENTER);
        item.add(statusLabel, BorderLayout.EAST);

        return item;
    }

    // =====================================================
    // RECENT ACTIVITY PANEL - RESPONSIVE
    // =====================================================

    private JPanel createActivityPanel() {

        JPanel panel = createPanel();
        panel.setLayout(new BorderLayout(0, 12));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Recent Activity");
        title.setForeground(WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel subtitle = new JLabel("Latest shop events");
        subtitle.setForeground(GRAY);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 11));

        JPanel headingText = new JPanel();
        headingText.setOpaque(false);
        headingText.setLayout(
                new BoxLayout(headingText, BoxLayout.Y_AXIS)
        );

        headingText.add(title);
        headingText.add(Box.createVerticalStrut(4));
        headingText.add(subtitle);

        JPanel heading = new JPanel(new BorderLayout());
        heading.setOpaque(false);
        heading.add(headingText, BorderLayout.CENTER);

        activityList = new JPanel();
        activityList.setOpaque(false);
        activityList.setLayout(
                new BoxLayout(activityList, BoxLayout.Y_AXIS)
        );

        // FIX: Assign to the class field instead of a local variable.
        activityScroll = new JScrollPane(activityList);

        activityScroll.setBorder(null);
        activityScroll.setOpaque(false);
        activityScroll.getViewport().setOpaque(false);

        activityScroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        activityScroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        activityScroll.getVerticalScrollBar().setUnitIncrement(12);
        activityScroll.getVerticalScrollBar().setPreferredSize(
                new Dimension(6, 0)
        );

        activityScroll.getVerticalScrollBar().setUI(
                new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = CYAN;
                trackColor = BACKGROUND;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });

        panel.add(heading, BorderLayout.NORTH);
        panel.add(activityScroll, BorderLayout.CENTER);

        refreshRecentActivity();
        return panel;
    }

    // =====================================================
    // REFRESH RECENT ACTIVITY - SCROLL POSITION PRESERVED
    // =====================================================

    private void refreshRecentActivity() {

        if (activityList == null) {
            return;
        }

        // Save the scrollbar position before replacing the rows.
        final JScrollBar scrollBar = activityScroll == null
                ? null
                : activityScroll.getVerticalScrollBar();

        final int previousScrollValue = scrollBar == null
                ? 0
                : scrollBar.getValue();

        // If the user was already at the bottom, keep them there.
        final boolean wasAtBottom = scrollBar != null
                && scrollBar.getValue() + scrollBar.getVisibleAmount()
                >= scrollBar.getMaximum() - 2;

        activityList.removeAll();

        try {
            ArrayList<ActivityRow> activities = db.getActivityRows();

            if (activities == null || activities.isEmpty()) {
                JLabel emptyLabel = new JLabel(
                        "No recent activity found."
                );
                emptyLabel.setForeground(GRAY);
                emptyLabel.setFont(
                        new Font("Arial", Font.PLAIN, 12)
                );
                activityList.add(emptyLabel);

            } else {
                boolean hasActivity = false;

                for (int i = 0; i < activities.size(); i++) {

                    ActivityRow activity = activities.get(i);

                    if (activity == null) {
                        continue;
                    }

                    String type = activity.type == null
                            ? ""
                            : activity.type.trim();

                    Color accent;

                    switch (type.toUpperCase()) {
                        case "SALE":
                            accent = GREEN;
                            break;
                        case "STOCK":
                            accent = CYAN;
                            break;
                        case "NEW":
                            accent = PURPLE;
                            break;
                        case "ALERT":
                            accent = RED;
                            break;
                        default:
                            accent = GRAY;
                            break;
                    }

                    JPanel row = createActivityRow(
                            accent,
                            type,
                            activity.title,
                            activity.description,
                            activity.time,
                            activity.value
                    );

                    activityList.add(row);

                    if (i < activities.size() - 1) {
                        activityList.add(createSeparator());
                    }

                    hasActivity = true;
                }

                if (!hasActivity) {
                    JLabel emptyLabel = new JLabel(
                            "No recent activity found."
                    );
                    emptyLabel.setForeground(GRAY);
                    activityList.add(emptyLabel);
                }
            }

        } catch (Exception ex) {
            JLabel errorLabel = new JLabel(
                    "Unable to load recent activity."
            );
            errorLabel.setForeground(RED);
            activityList.add(errorLabel);

            System.err.println("Recent activity refresh failed:");
            ex.printStackTrace();
        }

        activityList.revalidate();
        activityList.repaint();

        // Restore the position after Swing recalculates the layout.
        SwingUtilities.invokeLater(() -> {

            if (activityScroll == null || scrollBar == null) {
                return;
            }

            activityScroll.revalidate();
            activityScroll.repaint();

            SwingUtilities.invokeLater(() -> {

                int maximumScroll = Math.max(
                        0,
                        scrollBar.getMaximum()
                                - scrollBar.getVisibleAmount()
                );

                int targetPosition;

                if (wasAtBottom) {
                    // Keep the bottom visible if the user was there.
                    targetPosition = maximumScroll;
                } else {
                    // Otherwise, keep the user's previous position.
                    targetPosition = Math.min(
                            previousScrollValue,
                            maximumScroll
                    );
                }

                scrollBar.setValue(
                        Math.max(0, targetPosition)
                );
            });
        });
    }

    // =====================================================
    // RESPONSIVE ACTIVITY ROW
    // =====================================================

    private JPanel createActivityRow(
            Color accent,
            String type,
            String title,
            String description,
            String dateTime,
            String value) {

        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBorder(new EmptyBorder(10, 3, 10, 3));
        row.setMinimumSize(new Dimension(0, 60));

        // Activity type badge - ORIGINAL STYLE
        JLabel typeLabel = new JLabel(
                type == null ? "" : type,
                SwingConstants.CENTER
        );

        typeLabel.setForeground(accent);
        typeLabel.setFont(new Font("Arial", Font.BOLD, 9));
        typeLabel.setOpaque(true);
        typeLabel.setBackground(new Color(0x172440));
        typeLabel.setPreferredSize(new Dimension(65, 28));
        typeLabel.setMinimumSize(new Dimension(55, 28));

        // Details column
        JPanel details = new JPanel();
        details.setOpaque(false);
        details.setLayout(
                new BoxLayout(details, BoxLayout.Y_AXIS)
        );

        JLabel titleLabel = new JLabel(
                title == null ? "" : title
        );
        titleLabel.setForeground(WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Wrap the description as the window becomes narrower.
        JTextArea descriptionLabel = new JTextArea(
                description == null ? "" : description
        );

        descriptionLabel.setEditable(false);
        descriptionLabel.setFocusable(false);
        descriptionLabel.setLineWrap(true);
        descriptionLabel.setWrapStyleWord(true);
        descriptionLabel.setOpaque(false);
        descriptionLabel.setForeground(GRAY);
        descriptionLabel.setFont(
                new Font("Arial", Font.PLAIN, 10)
        );
        descriptionLabel.setBorder(null);
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel timeLabel = new JLabel(
                dateTime == null ? "" : dateTime
        );
        timeLabel.setForeground(GRAY);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 9));
        timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        details.add(titleLabel);
        details.add(Box.createVerticalStrut(4));
        details.add(descriptionLabel);
        details.add(Box.createVerticalStrut(4));
        details.add(timeLabel);

        // Flexible value column
        String displayValue =
                value == null || value.trim().isEmpty()
                        ? "N/A"
                        : value.trim();

        JLabel valueLabel = new JLabel(
                "<html><div style='text-align:right;'>"
                        + escapeHtml(displayValue)
                        + "</div></html>",
                SwingConstants.RIGHT
        );

        valueLabel.setForeground(accent);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 11));
        valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        valueLabel.setPreferredSize(new Dimension(90, 30));
        valueLabel.setMinimumSize(new Dimension(55, 25));

        row.add(typeLabel, BorderLayout.WEST);
        row.add(details, BorderLayout.CENTER);
        row.add(valueLabel, BorderLayout.EAST);

        // Recalculate layout after resizing.
        row.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                details.revalidate();
                details.repaint();
            }
        });

        return row;
    }

    // =====================================================
    // HTML ESCAPING FOR ACTIVITY LABELS
    // =====================================================

    private String escapeHtml(String text) {
        if (text == null) {
            return "";
        }

        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }

    // =====================================================
    // RESPONSIVE SEPARATOR
    // =====================================================

    private JComponent createSeparator() {

        JPanel line = new JPanel();
        line.setOpaque(true);
        line.setBackground(BORDER);
        line.setAlignmentX(Component.LEFT_ALIGNMENT);

        line.setPreferredSize(new Dimension(1, 1));
        line.setMinimumSize(new Dimension(1, 1));
        line.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 1)
        );

        return line;
    }

    // =====================================================
    // COMMON ROUNDED PANEL
    // =====================================================

    private JPanel createPanel() {

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(PANEL);
                g2.fillRoundRect(
                        0, 0, getWidth(), getHeight(), 18, 18
                );

                g2.setColor(BORDER);
                g2.drawRoundRect(
                        0, 0, getWidth() - 1,
                        getHeight() - 1, 18, 18
                );

                g2.dispose();
            }
        };

        panel.setOpaque(false);
        return panel;
    }

    // =====================================================
    // LOAD AND RESIZE ICON - ORIGINAL METHOD
    // =====================================================

    private ImageIcon loadIcon(String path) {

        ImageIcon icon = new ImageIcon(path);

        if (icon.getIconWidth() <= 0) {
            return new ImageIcon();
        }

        Image image = icon.getImage().getScaledInstance(
                20, 20, Image.SCALE_SMOOTH
        );

        return new ImageIcon(image);
    }

    // =====================================================
    // NAVIGATION BUTTON STYLING - ORIGINAL CODE PRESERVED
    // =====================================================

    private void styleButton(JButton button) {

        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(50, 50));

        if (button == logoutButton) {

            button.setBackground(Color.RED);
            button.setForeground(Color.WHITE);

        } else if (button == activeButton) {

            button.setBackground(new Color(0x386641));
            button.setForeground(CYAN);
            button.setFont(new Font("Arial", Font.BOLD, 14));

            roundedButton(button, 20);

        } else {

            button.setBackground(new Color(0x080B1A));
            button.setForeground(new Color(0xCBD5E1));
            button.setFont(new Font("Arial", Font.BOLD, 14));
        }

        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(8);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {

                if (button == logoutButton) {
                    button.setBackground(new Color(0xDC2626));
                } else if (button == activeButton) {
                    button.setBackground(new Color(0x386641));
                } else {
                    button.setBackground(new Color(0x1E1B4B));
                }

                button.setForeground(CYAN);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {

                if (button == logoutButton) {
                    button.setBackground(Color.RED);
                    button.setForeground(Color.WHITE);

                } else if (button == activeButton) {
                    button.setBackground(new Color(0x386641));
                    button.setForeground(CYAN);

                } else {
                    button.setBackground(new Color(0x080B1A));
                    button.setForeground(new Color(0xCBD5E1));
                }
            }
        });
    }

    // =====================================================
    // SET ACTIVE NAVIGATION BUTTON - ORIGINAL CODE PRESERVED
    // =====================================================

    private void setActiveButton(JButton button) {

        activeButton = button;

        styleButton(dashboardButton);
        styleButton(productsButton);
        styleButton(salesButton);
        styleButton(reportsButton);

        dashboardButton.repaint();
        productsButton.repaint();
        salesButton.repaint();
        reportsButton.repaint();
    }

    // =====================================================
    // ROUNDED BUTTON - ORIGINAL CODE PRESERVED
    // =====================================================

    public void roundedButton(JButton button, int radius) {

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);

        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(button.getBackground());
                g2.fillRoundRect(
                        0, 0,
                        button.getWidth(), button.getHeight(),
                        radius, radius
                );

                super.paint(g2, c);
                g2.dispose();
            }
        });
    }

    // =====================================================
    // REFRESH ALL DASHBOARD CONTENT
    // =====================================================

    public void refreshDashboard() {

        if (admin != null) {
            admin.setText(db.getusername());
        }

        refreshStatistics();
        refreshInventoryAlerts();
        refreshRecentActivity();
    }

    // =====================================================
    // STOP TIMER WHEN WINDOW CLOSES
    // =====================================================

    @Override
    public void dispose() {

        if (dashboardRefreshTimer != null) {
            dashboardRefreshTimer.stop();
        }

        super.dispose();
    }

    // =====================================================
    // MAIN
    // =====================================================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Dashboard::start);
    }
}
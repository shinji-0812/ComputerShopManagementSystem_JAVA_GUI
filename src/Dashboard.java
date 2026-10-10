import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class Dashboard extends JFrame {

    public JButton dashboardButton = new JButton("Dashboard");
    public JButton productsButton = new JButton("Products");
    public JButton salesButton = new JButton("Sales");

    public JButton reportsButton = new JButton("Reports");
    public JButton logoutButton = new JButton("Logout");

    private JButton activeButton = dashboardButton;
    private JPanel contentPanel;
    private JPanel dashboardContent;

    private String currentTitle = "Dashboard";
    private final Database db;

    public static void start() {

        Login login = new Login();
        Database data = new Database();

        if (!data.userExists("Dave")) {
            data.createAccount("Dave", "1234");
        }

        if (data.IsLogIn()) {

            login.dispose();

            new Dashboard(data.getusername(), data);

            return;
        }

        login.setVisible(true);

        login.loginButton.addActionListener(e -> {

            if (login.loginButtonActionPerformed()) {

                String username = login.usernameField.getText().trim();
                String password = new String(login.passwordField.getPassword());

                // Authenticate using the SAME Database object passed to Dashboard.
                // This lets Database retain the logged-in user's ID for product operations.
                if (!data.checkLogin(username, password)) {
                    JOptionPane.showMessageDialog(
                        login,
                        "Login succeeded in the login window, but the database session could not be restored."
                    );
                    return;
                }

                login.dispose();
                new Dashboard(username, data);

            } else {

                login.usernameField.setBorder(
                    BorderFactory.createLineBorder(
                        new Color(0xEF4444),
                        1
                    )
                );

                login.passwordField.setBorder(
                    BorderFactory.createLineBorder(
                        new Color(0xEF4444),
                        1
                    )
                );
            }
        });
    }


    public Dashboard(String username, Database db) {

        this.db = db;

        Logout logout = new Logout(this);
        logout.log_out();

        setTitle("Computer Shop Management System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        JPanel sidebar = new JPanel();

        sidebar.setBackground(
            new Color(0x080B1A)
        );

        sidebar.setPreferredSize(
            new Dimension(150, 600)
        );

        sidebar.setLayout(
            new GridLayout(10, 1, 0, 5)
        );


        JPanel title = new JPanel(
            new GridBagLayout()
        );

        title.setOpaque(false);

        JLabel main_title = new JLabel(
            "COMPUTER SHOP"
        );

        JLabel sub_title = new JLabel(
            "MANAGEMENT SYSTEM"
        );


        main_title.setForeground(
            Color.WHITE
        );

        main_title.setFont(
            new Font("Arial", Font.BOLD, 8)
        );


        sub_title.setForeground(
            new Color(0x22D3EE)
        );

        sub_title.setFont(
            new Font("Arial", Font.BOLD, 10)
        );


        title.setLayout(
            new BoxLayout(title, BoxLayout.Y_AXIS)
        );

        title.setBorder(
            BorderFactory.createEmptyBorder(
                10,
                10,
                10,
                10
            )
        );


        title.add(main_title);
        title.add(sub_title);

        sidebar.add(title);


        styleButton(dashboardButton);

        dashboardButton.setIcon(
            new ImageIcon(
                resize(
                    new ImageIcon(
                        "images/Nav_pics/Dashboard.png"
                    )
                )
            )
        );


        styleButton(productsButton);

        productsButton.setIcon(
            new ImageIcon(
                resize(
                    new ImageIcon(
                        "images/Nav_pics/Product.png"
                    )
                )
            )
        );


        styleButton(salesButton);

        salesButton.setIcon(
            new ImageIcon(
                resize(
                    new ImageIcon(
                        "images/Nav_pics/Sales.png"
                    )
                )
            )
        );





        styleButton(reportsButton);

        reportsButton.setIcon(
            new ImageIcon(
                resize(
                    new ImageIcon(
                        "images/Nav_pics/Reports.png"
                    )
                )
            )
        );


        styleButton(logoutButton);

        logoutButton.setIcon(
            new ImageIcon(
                resize(
                    new ImageIcon(
                        "images/Nav_pics/Logout.png"
                    )
                )
            )
        );

        logoutButton.setBackground(Color.RED);


        sidebar.add(dashboardButton);
        sidebar.add(productsButton);
        sidebar.add(salesButton);
        sidebar.add(reportsButton);

        JPanel gap1 = new JPanel();
        JPanel gap2 = new JPanel();
        JPanel gap3 = new JPanel();
        JPanel gap4 = new JPanel(); 

        gap3.setOpaque(false);
        gap1.setOpaque(false);
        gap2.setOpaque(false);
        gap4.setOpaque(false);

        sidebar.add(gap1);
        sidebar.add(gap2);
        sidebar.add(gap3);
        sidebar.add(gap4);

        sidebar.add(logoutButton);


        JPanel mainPanel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D g2 =
                    (Graphics2D) g;

                GradientPaint gradient =
                    new GradientPaint(
                        0,
                        0,
                        new Color(0x00C6FF),
                        getWidth(),
                        getHeight(),
                        new Color(0x12002F)
                    );

                g2.setPaint(gradient);

                g2.fillRect(
                    0,
                    0,
                    getWidth(),
                    getHeight()
                );
            }
        };


        mainPanel.setLayout(
            new BorderLayout()
        );


        JPanel header =
            new JPanel(
                new BorderLayout()
            );

        header.setBackground(
            new Color(0x0B1026)
        );

        header.setPreferredSize(
            new Dimension(0, 65)
        );

        // ==========HEADER TITLE CHANGE========================================
        JLabel upperTitle =
            new JLabel(
                "  " + currentTitle
            );

        // =====================================================================

        upperTitle.setForeground(
            new Color(0xE2E8F0)
        );

        upperTitle.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                28
            )
        );


        JLabel admin =
            new JLabel(username);

        ImageIcon icon =
            new ImageIcon(
                "images/user.png"
            );

        Image image =
            icon.getImage();

        Image resized =
            image.getScaledInstance(
                20,
                20,
                Image.SCALE_SMOOTH
            );

        admin.setIcon(
            new ImageIcon(resized)
        );

        admin.setForeground(
            new Color(0xCBD5E1)
        );

        admin.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                16
            )
        );


        header.add(
            upperTitle,
            BorderLayout.WEST
        );


        JPanel rightPanel =
            new JPanel(
                new FlowLayout(
                    FlowLayout.RIGHT,
                    10,
                    10
                )
            );

        rightPanel.setOpaque(false);


        JButton settings =
            new JButton();

        ImageIcon setting_img =
            new ImageIcon(
                "images/settings.png"
            );

        Image settings_resize =
            setting_img
                .getImage()
                .getScaledInstance(
                    20,
                    20,
                    Image.SCALE_SMOOTH
                );

        settings.setIcon(
            new ImageIcon(settings_resize)
        );

        settings.setPreferredSize(
            new Dimension(100, 50)
        );

        settings.setOpaque(false);
        settings.setContentAreaFilled(false);
        settings.setBorderPainted(false);
        settings.setFocusPainted(false);


        settings.addMouseListener(
            new MouseAdapter() {

                @Override
                public void mouseEntered(
                    MouseEvent e
                ) {

                    settings.setBorder(
                        BorderFactory.createMatteBorder(
                            0,
                            0,
                            3,
                            0,
                            new Color(0x22D3EE)
                        )
                    );
                }


                @Override
                public void mouseExited(
                    MouseEvent e
                ) {

                    settings.setBorder(
                        BorderFactory.createMatteBorder(
                            0,
                            0,
                            0,
                            0,
                            new Color(0x0B1026)
                        )
                    );
                }
            }
        );


        rightPanel.add(admin);
        rightPanel.add(settings);

        header.add(
            rightPanel,
            BorderLayout.EAST
        );


        mainPanel.add(
            header,
            BorderLayout.NORTH
        );


        JPanel cards =
            new JPanel();

        cards.setLayout(
            new GridLayout(
                1,
                3,
                15,
                15
            )
        );


        cards.setBorder(
            BorderFactory.createEmptyBorder(
                25,
                25,
                20,
                25
            )
        );

        cards.setOpaque(false);


        JPanel productCard =
            createCard(
                "Total Products",
                "86"
            );

        JPanel orderCard =
            createCard(
                "Total Orders",
                "24"
            );

        JPanel salesCard =
            createCard(
                "Total Sales",
                "₱25,450"
            );


        cards.add(productCard);
        cards.add(orderCard);
        cards.add(salesCard);


        contentPanel =
            new JPanel(
                new BorderLayout()
            );

        contentPanel.setBackground(
            new Color(0x0F172A)
        );


        dashboardContent =
            new JPanel(
                new BorderLayout()
            );

        dashboardContent.setBackground(
            new Color(0x0F172A)
        );


        JPanel content =
            dashboardContent;

        content.add(
            cards,
            BorderLayout.NORTH
        );


        // =====================================================
        //                    RECENT SALES
        // =====================================================

        JPanel recentSales =
            new JPanel();

        recentSales.setBackground(
            new Color(0x111C3D)
        );

        recentSales.setLayout(
            new BorderLayout()
        );

        recentSales.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(0x22007c),
                    1
                ),
                BorderFactory.createEmptyBorder(
                    15,
                    20,
                    15,
                    20
                )
            )
        );


        JLabel recentSalesTitle =
            new JLabel(
                "Recent Sales"
            );

        recentSalesTitle.setForeground(
            new Color(0xE2E8F0)
        );

        recentSalesTitle.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                20
            )
        );

        recentSalesTitle.setBorder(
            BorderFactory.createEmptyBorder(
                0,
                0,
                15,
                0
            )
        );

        recentSales.add(
            recentSalesTitle,
            BorderLayout.NORTH
        );


        JPanel salesList =
            new JPanel();

        salesList.setOpaque(false);

        salesList.setLayout(
            new BoxLayout(
                salesList,
                BoxLayout.Y_AXIS
            )
        );

        // ==================SALES TEMPORARY===================================

        salesList.add(
            createSale(
                "Gaming Keyboard",
                "John",
                "₱1,500",
                "Today, 2:35 PM"
            )
        );

        salesList.add(
            Box.createVerticalStrut(8)
        );


        salesList.add(
            createSale(
                "Wireless Mouse",
                "Mark",
                "₱850",
                "Today, 1:20 PM"
            )
        );

        salesList.add(
            Box.createVerticalStrut(8)
        );


        salesList.add(
            createSale(
                "Gaming Headset",
                "Alex",
                "₱2,200",
                "Yesterday, 5:45 PM"
            )
        );

        salesList.add(
            Box.createVerticalStrut(8)
        );


        salesList.add(
            createSale(
                "24-inch Monitor",
                "David",
                "₱8,500",
                "Yesterday, 3:15 PM"
            )
        );

        salesList.add(
            Box.createVerticalStrut(8)
        );


        salesList.add(
            createSale(
                "24-inch Monitor",
                "David",
                "₱8,500",
                "Yesterday, 3:15 PM"
            )
        );

        salesList.add(
            Box.createVerticalStrut(8)
        );


        salesList.add(
            createSale(
                "24-inch Monitor",
                "David",
                "₱8,500",
                "Yesterday, 3:15 PM"
            )
        );

        salesList.add(
            Box.createVerticalStrut(8)
        );


        salesList.add(
            createSale(
                "24-inch Monitor",
                "David",
                "₱8,500",
                "Yesterday, 3:15 PM"
            )
        );

        // =====================================================


            JScrollPane scrollPane = new JScrollPane(salesList);

            scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
            );

            scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
            );

            scrollPane.setBorder(null);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);

            JScrollBar verticalBar =
                scrollPane.getVerticalScrollBar();

            verticalBar.setPreferredSize(
                new Dimension(6, 0)
            );

            verticalBar.setOpaque(false);

            scrollPane.getVerticalScrollBar().setUI(
                new javax.swing.plaf.basic.BasicScrollBarUI() {

                    @Override
                    protected void configureScrollBarColors() {
                        this.thumbColor = new Color(0x22D3EE);
                        this.trackColor = new Color(0x0F172A);
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
                        button.setPreferredSize(new Dimension(3, 3));
                        return button;
                    }
                }
            );
            


        recentSales.add(
            scrollPane,
            BorderLayout.CENTER
        );


        // ==========MAIN CONTENT PANEL========================================

        content.add(
            recentSales,
            BorderLayout.CENTER
        );

        contentPanel.add(
            content,
            BorderLayout.CENTER
        );

        mainPanel.add(
            contentPanel,
            BorderLayout.CENTER
        );


        productsButton.addActionListener(e -> {

            setActiveButton(
                productsButton
            );

            currentTitle = "Products";
            contentPanel.removeAll();
            upperTitle.setText(
                "  " + currentTitle
            );

            contentPanel.add(
                new Products(db),
                BorderLayout.CENTER
            );


            contentPanel.revalidate();
            contentPanel.repaint();
        });


        dashboardButton.addActionListener(e -> {

            setActiveButton(
                dashboardButton
            );
            currentTitle = "Dashboard";

            upperTitle.setText(
                "  " + currentTitle
            );

            contentPanel.removeAll();

            contentPanel.add(
                dashboardContent,
                BorderLayout.CENTER
            );

            contentPanel.revalidate();
            contentPanel.repaint();
        });

        salesButton.addActionListener(e -> {

            setActiveButton(
                salesButton
            );

            currentTitle = "Sales";
            contentPanel.removeAll();
            upperTitle.setText(
                "  " + currentTitle
            );

            contentPanel.add(
                new Sales(db),
                BorderLayout.CENTER
            );
        });

        reportsButton.addActionListener(e -> {
            setActiveButton(
                reportsButton
            );

            currentTitle = "Reports";
            contentPanel.removeAll();
            upperTitle.setText(
                "  " + currentTitle
            );

            contentPanel.add(
                new Reports(db),
                BorderLayout.CENTER
            );
        });

        // =====================================================================


        add(
            sidebar,
            BorderLayout.WEST
        );


        add(
            mainPanel,
            BorderLayout.CENTER
        );


        setVisible(true);
    }


    private Image resize(ImageIcon icon) {

        Image image =
            icon.getImage();

        Image resized =
            image.getScaledInstance(
                20,
                20,
                Image.SCALE_SMOOTH
            );

        return resized;
    }


    private void styleButton(JButton button) {

        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.setPreferredSize(
            new Dimension(50, 50)
        );


        if (
            button == logoutButton
        ) {

            button.setBackground(
                Color.RED
            );

            button.setForeground(
                Color.WHITE
            );

        } else if (
            button == activeButton
        ) {

            button.setBackground(
                new Color(0x386641)
            );

            button.setForeground(
                new Color(0x22D3EE)
            );

            button.setFont(
                new Font(
                    "Arial",
                    Font.BOLD,
                    14
                )
            );

            roundedButton(
                button,
                20
            );

        } else {

            button.setBackground(
                null
            );

            button.setForeground(
                new Color(0xCBD5E1)
            );

            button.setFont(
                new Font(
                    "Arial",
                    Font.BOLD,
                    14
                )
            );
        }


        button.addMouseListener(
            new MouseAdapter() {

                @Override
                public void mouseEntered(
                    MouseEvent e
                ) {

                    if (
                        button == logoutButton
                    ) {

                        button.setBackground(
                            new Color(0xDC2626)
                        );

                    } else if (
                        button == activeButton
                    ) {

                        button.setBackground(
                            new Color(0x386641)
                        );

                    } else {

                        button.setBackground(
                            new Color(0x1E1B4B)
                        );
                    }


                    button.setForeground(
                        new Color(0x22D3EE)
                    );
                }


                @Override
                public void mouseExited(
                    MouseEvent e
                ) {

                    if (
                        button == logoutButton
                    ) {

                        button.setBackground(
                            Color.RED
                        );

                    } else if (
                        button == activeButton
                    ) {

                        button.setBackground(
                            new Color(0x386641)
                        );

                    } else {

                        button.setBackground(
                            null
                        );

                        button.setForeground(
                            new Color(0xCBD5E1)
                        );
                    }
                }
            }
        );
    }


    private void setActiveButton(
        JButton button
    ) {

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


    private JPanel createCard(
        String name,
        String value
    ) {

        JPanel card =
            new JPanel();


        card.setBackground(
            new Color(0x111C3D)
        );


        card.setLayout(
            new GridLayout(
                2,
                1
            )
        );


        card.setBorder(
            BorderFactory.createLineBorder(
                new Color(0x22007c),
                1
            )
        );


        JLabel nameLabel =
            new JLabel(
                name,
                SwingConstants.CENTER
            );


        nameLabel.setForeground(
            new Color(0x94A3B8)
        );


        nameLabel.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                15
            )
        );


        JLabel valueLabel =
            new JLabel(
                value,
                SwingConstants.CENTER
            );


        valueLabel.setForeground(
            new Color(0x22D3EE)
        );


        valueLabel.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                27
            )
        );


        card.add(nameLabel);
        card.add(valueLabel);


        return card;
    }


    private JPanel createSale(
        String product,
        String customer,
        String amount,
        String time
    ) {

        JPanel sale =
            new JPanel();


        sale.setBackground(
            new Color(0x0F172A)
        );


        sale.setLayout(
            new BorderLayout()
        );


        sale.setBorder(
            BorderFactory.createEmptyBorder(
                10,
                15,
                10,
                15
            )
        );


        JLabel productLabel =
            new JLabel(
                product
            );


        productLabel.setForeground(
            new Color(0xE2E8F0)
        );


        productLabel.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                15
            )
        );


        JLabel infoLabel =
            new JLabel(
                customer + "  •  " + time
            );


        infoLabel.setForeground(
            new Color(0x94A3B8)
        );


        infoLabel.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                12
            )
        );


        JPanel info =
            new JPanel();


        info.setOpaque(false);


        info.setLayout(
            new BoxLayout(
                info,
                BoxLayout.Y_AXIS
            )
        );


        info.add(
            productLabel
        );


        info.add(
            Box.createVerticalStrut(4)
        );


        info.add(
            infoLabel
        );


        JLabel amountLabel =
            new JLabel(
                amount
            );


        amountLabel.setForeground(
            new Color(0x22D3EE)
        );


        amountLabel.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                16
            )
        );


        sale.add(
            info,
            BorderLayout.WEST
        );


        sale.add(
            amountLabel,
            BorderLayout.EAST
        );


        return sale;
    }


    public void roundedButton(
        JButton button,
        int radius
    ) {

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);


        button.setUI(
            new javax.swing.plaf.basic.BasicButtonUI() {

                @Override
                public void paint(
                    Graphics g,
                    JComponent c
                ) {

                    Graphics2D g2 =
                        (Graphics2D) g.create();


                    g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                    );


                    // Button background

                    g2.setColor(
                        button.getBackground()
                    );


                    g2.fillRoundRect(
                        0,
                        0,
                        button.getWidth(),
                        button.getHeight(),
                        radius,
                        radius
                    );


                    // Button text

                    super.paint(
                        g2,
                        c
                    );


                    g2.dispose();
                }
            }
        );
    }


    public static void main(String[] args) {

        start();
    }
}
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class Dashboard extends JFrame {

    public JButton dashboardButton = new JButton("Dashboard");
    public JButton productsButton = new JButton("Products");
    public JButton inventoryButton = new JButton("Inventory");
    public JButton salesButton = new JButton("Sales");
    public JButton customersButton = new JButton("Customers");
    public JButton ordersButton = new JButton("Orders");
    public JButton reportsButton = new JButton("Reports");
    public JButton logoutButton = new JButton("Logout");

    public static void start() {

        Login login = new Login();
        Database data = new Database();

        if (!data.userExists("Dave")) {
            data.createAccount("Dave", "1234");
        }

        if (data.IsLogIn()) {

            login.dispose();

            new Dashboard(data.getusername());

            return;
        }

        login.setVisible(true);

        login.loginButton.addActionListener(e -> {

            if (login.loginButtonActionPerformed()) {

                login.dispose();

                new Dashboard(
                    login.usernameField.getText()
                );

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





    public Dashboard(String username) {

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

        styleButton(inventoryButton);

        inventoryButton.setIcon(
            new ImageIcon(
                resize(
                    new ImageIcon(
                        "images/Nav_pics/Inventory.png"
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

        styleButton(customersButton);

        customersButton.setIcon(
            new ImageIcon(
                resize(
                    new ImageIcon(
                        "images/Nav_pics/Customer.png"
                    )
                )
            )
        );

        styleButton(ordersButton);

        ordersButton.setIcon(
            new ImageIcon(
                resize(
                    new ImageIcon(
                        "images/Nav_pics/Orders.png"
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
        sidebar.add(inventoryButton);
        sidebar.add(salesButton);
        sidebar.add(customersButton);
        sidebar.add(ordersButton);
        sidebar.add(reportsButton);

        JPanel gap = new JPanel();

        gap.setOpaque(false);

        sidebar.add(gap);
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

        JLabel dashboardTitle =
            new JLabel(
                "  Dashboard"
            );

        dashboardTitle.setForeground(
            new Color(0xE2E8F0)
        );

        dashboardTitle.setFont(
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
            dashboardTitle,
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

        JPanel content =
            new JPanel();

        content.setBackground(
            new Color(0x0F172A)
        );

        content.setLayout(
            new BorderLayout()
        );

        content.add(
            cards,
            BorderLayout.NORTH
        );

        mainPanel.add(
            content,
            BorderLayout.CENTER
        );

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

        if (button.getText().equals("Logout")) {

            button.setBackground(
                Color.RED
            );

        } else {

            button.setBackground(null);
        }
        String currentButtonText = "Dashboard";

        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.setPreferredSize(
            new Dimension(50, 50)
        );

        if(button.getText().equals(currentButtonText)) {

            button.setBackground(
                new Color(0x386641)
            );

            roundedButton(button, 20);

            button.setForeground(
                new Color(0x22D3EE)
            );
        }
        else{
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
                        button.getText()
                            .equals("Logout")
                    ) {

                        button.setBackground(
                            new Color(0xDC2626)
                        );

                    }

                    else if(button.getText().equals(currentButtonText)) {

                            button.setBackground(
                                new Color(0x386641)
                            );
                    }
                    
                    else {

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
                        button.getText()
                            .equals("Logout")
                    ) {

                        button.setBackground(
                            Color.RED
                        );

                    }
                    else if(button.getText().equals(currentButtonText)) {

                        button.setBackground(
                            new Color(0x386641)
                        );

                    }

                    
                    else {

                        button.setBackground(null);
                    }

                    button.setForeground(
                        new Color(0xCBD5E1)
                    );
                }
            }
        );
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

                // Button background
                g2.setColor(button.getBackground());

                g2.fillRoundRect(
                    0,
                    0,
                    button.getWidth(),
                    button.getHeight(),
                    radius,
                    radius
                );

                // Button text
                super.paint(g2, c);

                g2.dispose();
            }
        });
    }


    public static void main(String[] args) {

        start();
    }
}
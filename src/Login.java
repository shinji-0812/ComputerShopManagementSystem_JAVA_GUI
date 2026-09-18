import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class Login extends JFrame {

    public JTextField usernameField;
    public JPasswordField passwordField;
    public JButton loginButton = new JButton("Login");

    private final Database data = new Database();

    public Login() {

        setTitle("Login");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel main_panel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;

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

        main_panel.setLayout(
            new GridLayout(1, 2, 10, 10)
        );



        JPanel panel = new JPanel();

        panel.setBackground(
            new Color(0x0B1026)
        );

        panel.setLayout(
            new GridLayout(3, 1, 20, 60)
        );

        panel.setBorder(
            BorderFactory.createEmptyBorder(
                30,
                30,
                30,
                30
            )
        );



        JLabel usernameLabel =
            new JLabel("Username:");

        usernameLabel.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                12
            )
        );

        usernameLabel.setForeground(
            new Color(0xE2E8F0)
        );

        usernameField =
            new JTextField();

        usernameField.setBackground(
            new Color(0x111A3A)
        );

        usernameField.setForeground(
            new Color(0xF8FAFC)
        );

        usernameField.setCaretColor(
            new Color(0x22D3EE)
        );

        usernameField.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                12
            )
        );

        usernameField.setBorder(
            BorderFactory.createLineBorder(
                new Color(0x6C63FF),
                1
            )
        );

        JPanel usernamePanel =
            new JPanel(
                new BorderLayout(0, 5)
            );

        usernamePanel.setOpaque(false);

        usernamePanel.add(
            usernameLabel,
            BorderLayout.NORTH
        );

        usernamePanel.add(
            usernameField,
            BorderLayout.CENTER
        );



        JLabel passwordLabel =
            new JLabel("Password:");

        passwordLabel.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                12
            )
        );

        passwordLabel.setForeground(
            new Color(0xE2E8F0)
        );

        passwordField =
            new JPasswordField();

        passwordField.setBackground(
            new Color(0x111A3A)
        );

        passwordField.setForeground(
            new Color(0xF8FAFC)
        );

        passwordField.setCaretColor(
            new Color(0x22D3EE)
        );

        passwordField.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                12
            )
        );

        passwordField.setBorder(
            BorderFactory.createLineBorder(
                new Color(0x6C63FF),
                1
            )
        );

        JPanel passwordPanel =
            new JPanel(
                new BorderLayout(0, 5)
            );

        passwordPanel.setOpaque(false);

        passwordPanel.add(
            passwordLabel,
            BorderLayout.NORTH
        );

        passwordPanel.add(
            passwordField,
            BorderLayout.CENTER
        );

        // LOGIN BUTTON

        loginButton.setPreferredSize(
            new Dimension(230, 40)
        );

        loginButton.setBackground(
            new Color(0x7C3AED)
        );

        loginButton.setForeground(
            Color.WHITE
        );

        loginButton.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                18
            )
        );

        loginButton.setFocusPainted(false);

        loginButton.setBorder(
            BorderFactory.createLineBorder(
                new Color(0xA78BFA),
                1
            )
        );

        loginButton.addMouseListener(
            new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {

                    loginButton.setBackground(
                        new Color(0x9333EA)
                    );

                    loginButton.setForeground(
                        new Color(0x22D3EE)
                    );

                    loginButton.setBorder(
                        BorderFactory.createLineBorder(
                            new Color(0x22D3EE),
                            1
                        )
                    );
                }

                @Override
                public void mouseExited(MouseEvent e) {

                    loginButton.setBackground(
                        new Color(0x7C3AED)
                    );

                    loginButton.setForeground(
                        Color.WHITE
                    );

                    loginButton.setBorder(
                        BorderFactory.createLineBorder(
                            new Color(0xA78BFA),
                            1
                        )
                    );
                }
            }
        );

        JPanel buttonPanel =
            new JPanel(
                new FlowLayout(
                    FlowLayout.CENTER
                )
            );

        buttonPanel.setOpaque(false);

        buttonPanel.add(loginButton);

        panel.add(usernamePanel);
        panel.add(passwordPanel);
        panel.add(buttonPanel);


        JPanel rightPanel =
            new JPanel();

        rightPanel.setOpaque(false);

        rightPanel.setLayout(
            new BoxLayout(
                rightPanel,
                BoxLayout.Y_AXIS
            )
        );

        ImageIcon original =
            new ImageIcon(
                "images/user2.png"
            );

        Image image =
            original.getImage();

        Image resized =
            image.getScaledInstance(
                200,
                200,
                Image.SCALE_SMOOTH
            );

        JLabel imageLabel =
            new JLabel(
                new ImageIcon(resized)
            );

        imageLabel.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        JLabel title =
            new JLabel(
                "Welcome Back!"
            );

        title.setForeground(
            new Color(0x22D3EE)
        );

        title.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                24
            )
        );

        title.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        JLabel subtitle =
            new JLabel(
                "COMPUTER SHOP MANAGEMENT"
            );

        JLabel subtitle2 =
            new JLabel(
                "SYSTEM"
            );

        subtitle.setForeground(
            new Color(0xE2E8F0)
        );

        subtitle.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                12
            )
        );

        subtitle.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        subtitle2.setForeground(
            new Color(0xE2E8F0)
        );

        subtitle2.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                12
            )
        );

        subtitle2.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        rightPanel.add(imageLabel);

        rightPanel.add(
            Box.createVerticalStrut(10)
        );

        rightPanel.add(title);

        rightPanel.add(
            Box.createVerticalStrut(5)
        );

        rightPanel.add(subtitle);

        rightPanel.add(subtitle2);

        rightPanel.add(
            Box.createVerticalGlue()
        );

        main_panel.add(panel);
        main_panel.add(rightPanel);

        add(main_panel);
    }



    public boolean loginButtonActionPerformed() {

        String username =
            usernameField.getText();

        String password =
            new String(
                passwordField.getPassword()
            );

        if (
            username.equals("") ||
            password.equals("")
        ) {

            JOptionPane.showMessageDialog(
                this,
                "Please fill up all fields."
            );

            return false;
        }

        if (data.checkLogin(username,password)) {

            if (
                data.setLogin(username)
            ) {

                return true;
            }

            JOptionPane.showMessageDialog(
                this,
                "Could not save login status."
            );

            return false;
        }

        JOptionPane.showMessageDialog(
            this,
            "Incorrect username or password"
        );

        passwordField.setText("");

        return false;
    }
}
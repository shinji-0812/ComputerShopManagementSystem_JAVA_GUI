
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Login extends JFrame {

    public JTextField usernameField;
    public JPasswordField passwordField;
    public JButton loginButton = new JButton("Login");

    private final Database data = new Database();

    // COLORS
    private final Color BACKGROUND = new Color(0x0B1026);
    private final Color FIELD = new Color(0x111A3A);
    private final Color CYAN = new Color(0x22D3EE);
    private final Color WHITE = new Color(0xF1F5F9);
    private final Color GRAY = new Color(0x94A3B8);
    private final Color BORDER = new Color(0x263650);

    public Login() {

        setTitle("Computer Shop Management System - Login");
        setSize(760, 460);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // MAIN BACKGROUND
        JPanel mainPanel = new JPanel(new GridLayout(1, 2)) {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                    RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY
                );

                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(0x00C6FF),
                    getWidth(), getHeight(), new Color(0x12002F)
                );

                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };

        // ================= LEFT: LOGIN FORM =================

        JPanel formPanel = new JPanel();
        formPanel.setBackground(BACKGROUND);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(35, 30, 30, 30));

        JLabel formTitle = new JLabel("Sign In");
        formTitle.setForeground(WHITE);
        formTitle.setFont(new Font("Arial", Font.BOLD, 28));
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel formSubtitle = new JLabel("Enter your account details below.");
        formSubtitle.setForeground(GRAY);
        formSubtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        formSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        formPanel.add(formTitle);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(formSubtitle);
        formPanel.add(Box.createVerticalStrut(30));

        // USERNAME
        JLabel usernameLabel = createLabel("USERNAME");

        usernameField = new JTextField();
        styleTextField(usernameField);

        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(20));

        // PASSWORD
        JLabel passwordLabel = createLabel("PASSWORD");

        passwordField = new JPasswordField();
        styleTextField(passwordField);
        passwordField.setEchoChar('\u2022');

        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(10));

        // SHOW PASSWORD CHECKBOX
        JCheckBox showPassword = new JCheckBox("Show password");
        showPassword.setOpaque(false);
        showPassword.setForeground(GRAY);
        showPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        showPassword.setFocusPainted(false);
        showPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('\u2022');
            }
        });

        formPanel.add(showPassword);
        formPanel.add(Box.createVerticalStrut(22));

        // LOGIN BUTTON
        loginButton.setText("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 15));
        loginButton.setForeground(new Color(0x08202B));
        loginButton.setBackground(CYAN);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 43));
        loginButton.setPreferredSize(new Dimension(280, 43));

        loginButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(0x67E8F9));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(CYAN);
            }
        });

        formPanel.add(loginButton);

        // Allow Enter to trigger login.
        getRootPane().setDefaultButton(loginButton);

        // ================= RIGHT: WELCOME PANEL =================

        JPanel welcomePanel = new JPanel();
        welcomePanel.setOpaque(false);
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBorder(new EmptyBorder(35, 20, 35, 20));

        welcomePanel.add(Box.createVerticalGlue());

        // LOGO
        ImageIcon original = new ImageIcon("images/Logo.png");

        JLabel imageLabel;

        if (original.getIconWidth() > 0) {
            Image resized = original.getImage().getScaledInstance(
                225, 225, Image.SCALE_SMOOTH
            );

            imageLabel = new JLabel(new ImageIcon(resized));
        } else {
            imageLabel = new JLabel("COMPUTER SHOP");
            imageLabel.setForeground(CYAN);
            imageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        }

        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcomeTitle = new JLabel("Welcome Back!");
        welcomeTitle.setForeground(WHITE);
        welcomeTitle.setFont(new Font("Arial", Font.BOLD, 25));
        welcomeTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle1 = new JLabel("COMPUTER SHOP");
        subtitle1.setForeground(CYAN);
        subtitle1.setFont(new Font("Arial", Font.BOLD, 13));
        subtitle1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle2 = new JLabel("MANAGEMENT SYSTEM");
        subtitle2.setForeground(WHITE);
        subtitle2.setFont(new Font("Arial", Font.BOLD, 12));
        subtitle2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tagline = new JLabel("Manage your business with ease.");
        tagline.setForeground(GRAY);
        tagline.setFont(new Font("Arial", Font.PLAIN, 11));
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        welcomePanel.add(imageLabel);
        welcomePanel.add(Box.createVerticalStrut(18));
        welcomePanel.add(welcomeTitle);
        welcomePanel.add(Box.createVerticalStrut(12));
        welcomePanel.add(subtitle1);
        welcomePanel.add(Box.createVerticalStrut(4));
        welcomePanel.add(subtitle2);
        welcomePanel.add(Box.createVerticalStrut(12));
        welcomePanel.add(tagline);

        welcomePanel.add(Box.createVerticalGlue());

        // ASSEMBLE
        mainPanel.add(formPanel);
        mainPanel.add(welcomePanel);

        setContentPane(mainPanel);
    }

    // ================= LABEL STYLE =================

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(GRAY);
        label.setFont(new Font("Arial", Font.BOLD, 11));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    // ================= FIELD STYLE =================

    private void styleTextField(JTextField field) {

        field.setBackground(FIELD);
        field.setForeground(WHITE);
        field.setCaretColor(CYAN);
        field.setFont(new Font("Arial", Font.PLAIN, 14));

        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1),
            new EmptyBorder(10, 12, 10, 12)
        ));

        field.setPreferredSize(new Dimension(280, 42));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    // ================= LOGIN FUNCTION =================

    public boolean loginButtonActionPerformed() {

        String username = usernameField.getText().trim();

        String password = new String(
            passwordField.getPassword()
        );

        if (username.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Please fill up all fields.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );

            return false;
        }

        if (data.checkLogin(username, password)) {

            if (data.setLogin(username)) {
                return true;
            }

            JOptionPane.showMessageDialog(
                this,
                "Could not save login status.",
                "Login Error",
                JOptionPane.ERROR_MESSAGE
            );

            return false;
        }

        JOptionPane.showMessageDialog(
            this,
            "Incorrect username or password.",
            "Login Failed",
            JOptionPane.ERROR_MESSAGE
        );

        passwordField.setText("");
        passwordField.requestFocusInWindow();

        return false;
    }
}

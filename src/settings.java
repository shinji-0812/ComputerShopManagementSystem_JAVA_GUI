
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class settings extends JDialog {

    private final Database db;
    private final Admin admin;

    private JTextField usernameField;
    private JPasswordField currentPassword;
    private JPasswordField newPassword;
    private JPasswordField confirmPassword;

    private final Color BACKGROUND = new Color(0x0F172A);
    private final Color PANEL = new Color(0x111C3D);
    private final Color FIELD = new Color(0x1E293B);
    private final Color CYAN = new Color(0x22D3EE);
    private final Color WHITE = new Color(0xE2E8F0);
    private final Color GRAY = new Color(0x94A3B8);
    private final Color BORDER = new Color(0x334155);
    private final Color GREEN = new Color(0x22C55E);
    private final Color RED = new Color(0xEF4444);

    public settings(JFrame parent, Database db, Admin admin) {
        super(parent, "Account Settings", true);

        this.db = db;
        this.admin = admin;

        setSize(520, 650);
        setMinimumSize(new Dimension(480, 550));
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(24, 25, 20, 25));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // ================= HEADER =================

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel iconLabel = new JLabel("⚙  ACCOUNT SETTINGS");
        iconLabel.setForeground(CYAN);
        iconLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JLabel title = new JLabel("Manage your account");
        title.setForeground(WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 25));

        JLabel subtitle = new JLabel(
                "Update your username and password.");
        subtitle.setForeground(GRAY);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));

        header.add(iconLabel);
        header.add(Box.createVerticalStrut(10));
        header.add(title);
        header.add(Box.createVerticalStrut(5));
        header.add(subtitle);

        mainPanel.add(header);
        mainPanel.add(Box.createVerticalStrut(25));

        // ================= USERNAME CARD =================

        JPanel usernameCard = createCard();

        JLabel usernameHeading = createHeading("Profile Information");
        JLabel usernameDescription = createDescription(
                "Change the username associated with your account.");

        usernameField = new JTextField(
                db.getusername() == null ? "" : db.getusername());

        styleField(usernameField);

        JButton usernameButton = createButton(
                "Save Username", CYAN, BACKGROUND);

        usernameButton.addActionListener(e -> updateUsername());

        usernameCard.add(usernameHeading);
        usernameCard.add(Box.createVerticalStrut(5));
        usernameCard.add(usernameDescription);
        usernameCard.add(Box.createVerticalStrut(20));
        usernameCard.add(createFieldLabel("USERNAME"));
        usernameCard.add(Box.createVerticalStrut(7));
        usernameCard.add(usernameField);
        usernameCard.add(Box.createVerticalStrut(15));
        usernameCard.add(usernameButton);

        mainPanel.add(usernameCard);
        mainPanel.add(Box.createVerticalStrut(18));

        // ================= PASSWORD CARD =================

        JPanel passwordCard = createCard();

        JLabel passwordHeading = createHeading("Security");
        JLabel passwordDescription = createDescription(
                "Use your current password to set a new one.");

        currentPassword = new JPasswordField();
        newPassword = new JPasswordField();
        confirmPassword = new JPasswordField();

        styleField(currentPassword);
        styleField(newPassword);
        styleField(confirmPassword);

        passwordCard.add(passwordHeading);
        passwordCard.add(Box.createVerticalStrut(5));
        passwordCard.add(passwordDescription);
        passwordCard.add(Box.createVerticalStrut(18));

        addPasswordInput(
                passwordCard, "CURRENT PASSWORD", currentPassword);

        addPasswordInput(
                passwordCard, "NEW PASSWORD", newPassword);

        addPasswordInput(
                passwordCard, "CONFIRM NEW PASSWORD", confirmPassword);

        JCheckBox showPasswords = new JCheckBox("Show passwords");
        showPasswords.setOpaque(false);
        showPasswords.setForeground(GRAY);
        showPasswords.setFont(new Font("Arial", Font.PLAIN, 12));
        showPasswords.setFocusPainted(false);
        showPasswords.setAlignmentX(Component.LEFT_ALIGNMENT);

        char currentEcho = currentPassword.getEchoChar();
        char newEcho = newPassword.getEchoChar();
        char confirmEcho = confirmPassword.getEchoChar();

        showPasswords.addActionListener(e -> {
            if (showPasswords.isSelected()) {
                currentPassword.setEchoChar((char) 0);
                newPassword.setEchoChar((char) 0);
                confirmPassword.setEchoChar((char) 0);
            } else {
                currentPassword.setEchoChar(currentEcho);
                newPassword.setEchoChar(newEcho);
                confirmPassword.setEchoChar(confirmEcho);
            }
        });

        passwordCard.add(showPasswords);
        passwordCard.add(Box.createVerticalStrut(15));

        JButton passwordButton = createButton(
                "Update Password", CYAN, BACKGROUND);

        passwordButton.addActionListener(e -> validatePassword());

        passwordCard.add(passwordButton);

        mainPanel.add(passwordCard);
        mainPanel.add(Box.createVerticalStrut(20));

        // ================= BOTTOM BUTTON =================

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bottomPanel.setOpaque(false);
        bottomPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottomPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 42));

        JButton closeButton = createButton(
                "Close", new Color(0x334155), WHITE);

        closeButton.setPreferredSize(new Dimension(105, 38));
        closeButton.setMaximumSize(new Dimension(105, 38));
        closeButton.addActionListener(e -> dispose());

        bottomPanel.add(closeButton);
        mainPanel.add(bottomPanel);

        // ================= SCROLL PANE =================

        JScrollPane scrollPane = new JScrollPane(mainPanel);

        scrollPane.setBorder(null);
        scrollPane.setBackground(BACKGROUND);
        scrollPane.getViewport().setBackground(BACKGROUND);
        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        scrollBar.setUnitIncrement(16);
        scrollBar.setPreferredSize(new Dimension(7, 0));
        scrollBar.setBackground(BACKGROUND);

        scrollBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = new Color(0x475569);
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

        setContentPane(scrollPane);
    }

    // ================= CARD =================

    private JPanel createCard() {
        JPanel card = new JPanel();

        card.setBackground(PANEL);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(18, 18, 18, 18)));

        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1000));

        return card;
    }

    // ================= LABELS =================

    private JLabel createHeading(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 17));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel createDescription(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(GRAY);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(GRAY);
        label.setFont(new Font("Arial", Font.BOLD, 11));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    // ================= INPUTS =================

    private void styleField(JTextField field) {
        field.setBackground(FIELD);
        field.setForeground(WHITE);
        field.setCaretColor(CYAN);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(8, 11, 8, 11)));

        field.setPreferredSize(new Dimension(400, 40));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(CYAN, 1, true),
                        new EmptyBorder(8, 11, 8, 11)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER, 1, true),
                        new EmptyBorder(8, 11, 8, 11)));
            }
        });
    }

    private void addPasswordInput(
            JPanel panel, String labelText, JPasswordField field) {

        panel.add(createFieldLabel(labelText));
        panel.add(Box.createVerticalStrut(7));
        panel.add(field);
        panel.add(Box.createVerticalStrut(13));
    }

    // ================= BUTTONS =================

    private JButton createButton(
            String text, Color background, Color foreground) {

        JButton button = new JButton(text);

        button.setBackground(background);
        button.setForeground(foreground);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        button.setPreferredSize(new Dimension(160, 39));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 39));

        Color hoverColor = background.equals(CYAN)
                ? new Color(0x67E8F9)
                : new Color(0x475569);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(background);
            }
        });

        return button;
    }

    // ================= UPDATE USERNAME =================

    private void updateUsername() {
        String username = usernameField.getText().trim();

        if (username.isEmpty()) {
            showMessage("Username cannot be empty.", false);
            return;
        }

        if (username.length() < 3) {
            showMessage(
                    "Username must be at least 3 characters.", false);
            return;
        }

        if (username.equals(db.getusername())) {
            showMessage("This is already your username.", false);
            return;
        }

        boolean updated = db.usernameChange(username);

        if (updated) {
            if (getOwner() instanceof Dashboard) {
                Dashboard dashboard = (Dashboard) getOwner();
                dashboard.refreshDashboard();
            }

            if (admin != null) {
                admin.changename(username);
            }

            showMessage("Username updated successfully!", true);
            usernameField.setText(username);
        } else {
            showMessage(
                    "Could not update username. It may already be in use.",
                    false);
        }
    }

    // ================= UPDATE PASSWORD =================

    private void validatePassword() {
        String current = new String(currentPassword.getPassword());
        String newPass = new String(newPassword.getPassword());
        String confirm = new String(confirmPassword.getPassword());

        if (current.isEmpty() || newPass.isEmpty() || confirm.isEmpty()) {
            showMessage("Please fill in all password fields.", false);
            return;
        }

        if (newPass.length() < 4) {
            showMessage(
                    "New password must be at least 6 characters.", false);
            return;
        }

        if (!newPass.equals(confirm)) {
            showMessage("New passwords do not match.", false);
            return;
        }

        if (current.equals(newPass)) {
            showMessage(
                    "Your new password must be different from your current password.",
                    false);
            return;
        }
        if(!db.checkLogin(db.getusername(), current)){
            showMessage("Invalid current password", false);
            return;
        }

        boolean updated = db.passwordChange(confirm);

        if(updated){
            Dashboard dashboard = (Dashboard) getOwner();
            dashboard.refreshDashboard();
            showMessage("Password updated successfully!", true);
            currentPassword.setText("");
            newPassword.setText("");
            confirmPassword.setText("");
            if (admin != null) {
                admin.changepassword(confirm);
            }


        } else{
            showMessage("There might be something Wrong", false);
        }
    }

    // ================= MESSAGE =================

    private void showMessage(String message, boolean success) {
        JOptionPane.showMessageDialog(
                this,
                message,
                success ? "Success" : "Notice",
                success
                        ? JOptionPane.INFORMATION_MESSAGE
                        : JOptionPane.WARNING_MESSAGE);
    }
}

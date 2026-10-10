import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class Products extends JPanel {

    private final Color BACKGROUND = new Color(0x0F172A);
    private final Color PANEL = new Color(0x111C3D);
    private final Color HEADER = new Color(0x0B1026);
    private final Color CYAN = new Color(0x22D3EE);
    private final Color WHITE = new Color(0xE2E8F0);
    private final Color GRAY = new Color(0x94A3B8);
    private final Color BORDER = new Color(0x334155);
    private final Color GREEN = new Color(0x22C55E);
    private final Color ORANGE = new Color(0xF59E0B);
    private final Color RED = new Color(0xEF4444);

    private final Database db;
    private final ArrayList<Product> products = new ArrayList<>();
    private final JPanel cardsPanel = new JPanel(new GridLayout(0, 3, 16, 16));
    private JTextField searchField;
    private JComboBox<String> categoryBox;

    public Products(Database db) {
        this.db = db;
        setBackground(BACKGROUND);
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(25, 25, 25, 25));


        // Sample products
        products.addAll(db.getProducts());
        //==========================================================


        JPanel top = new JPanel(new BorderLayout(15, 0));
        top.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Product Inventory");
        title.setForeground(WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel subtitle = new JLabel("Manage your computer shop products");
        subtitle.setForeground(GRAY);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);
        top.add(titlePanel, BorderLayout.WEST);

        JButton addButton = new JButton("+ Add Product");
        stylePrimaryButton(addButton);
        addButton.addActionListener(e -> showProductDialog(-1));
        top.add(addButton, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 15));
        content.setOpaque(false);

        JPanel tools = new JPanel(new BorderLayout(15, 0));
        tools.setOpaque(false);

        searchField = new JTextField();
        searchField.setText("Search products...");
        searchField.setForeground(GRAY);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBackground(PANEL);
        searchField.setCaretColor(CYAN);
        searchField.setPreferredSize(new Dimension(200, 42));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        searchField.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search products...")) {
                    searchField.setText("");
                    searchField.setForeground(WHITE);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (searchField.getText().trim().isEmpty()) {
                    searchField.setText("Search products...");
                    searchField.setForeground(GRAY);
                }
            }
        });
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { refreshCards(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { refreshCards(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { refreshCards(); }
        });
        tools.add(searchField, BorderLayout.CENTER);

        categoryBox = new JComboBox<>(new String[] {
                "All Categories", "Keyboard", "Mouse", "Audio", "Monitor", "Graphics Card", "CPU", "Motherboard", "RAM", "Storage", "Power Supply", "Case"
        });
        categoryBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        categoryBox.setBackground(PANEL);
        categoryBox.setForeground(WHITE);
        categoryBox.setPreferredSize(new Dimension(180, 42));
        categoryBox.setBorder(BorderFactory.createLineBorder(BORDER));
        categoryBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setFont(new Font("Segoe UI", Font.PLAIN, 13));
                setBorder(new EmptyBorder(7, 10, 7, 10));
                setBackground(isSelected ? new Color(0x1E1B4B) : PANEL);
                setForeground(isSelected ? CYAN : WHITE);
                return this;
            }
        });
        categoryBox.addActionListener(e -> refreshCards());
        tools.add(categoryBox, BorderLayout.EAST);
        content.add(tools, BorderLayout.NORTH);

        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(new EmptyBorder(2, 2, 2, 2));

        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        



        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(7, 0));
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = CYAN;
                trackColor = BACKGROUND;
            }
            @Override protected JButton createDecreaseButton(int orientation) { return zeroButton(); }
            @Override protected JButton createIncreaseButton(int orientation) { return zeroButton(); }
            private JButton zeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }
        });
        content.add(scrollPane, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        refreshCards();
    }

    private void refreshCards() {
        if (cardsPanel == null || searchField == null || categoryBox == null) return;

        cardsPanel.removeAll();
        String search = searchField.getText().trim().toLowerCase();
        if (search.equals("search products...")) search = "";
        Object selected = categoryBox.getSelectedItem();
        String category = selected == null ? "All Categories" : selected.toString();

        int count = 0;
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            boolean matchesSearch = product.name.toLowerCase().contains(search)
                    || product.category.toLowerCase().contains(search);
            boolean matchesCategory = category.equals("All Categories")
                    || product.category.equals(category);

            if (matchesSearch && matchesCategory) {
                cardsPanel.add(createProductCard(product, i));
                count++;
            }
        }

        if (count == 0) {
            JPanel empty = new JPanel(new GridBagLayout());
            empty.setOpaque(false);
            JLabel message = new JLabel("No products found");
            message.setForeground(GRAY);
            message.setFont(new Font("Arial", Font.PLAIN, 15));
            empty.add(message);
            cardsPanel.setLayout(new BorderLayout());
            cardsPanel.add(empty, BorderLayout.CENTER);
        } else {
            cardsPanel.setLayout(new GridLayout(0, 3, 16, 16));
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private JPanel createProductCard(Product product, int index) {
        JPanel card = new JPanel(new BorderLayout(0, 13));
        card.setBackground(PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(18, 18, 18, 18)));
        card.setPreferredSize(new Dimension(220, 225));

        JPanel top = new JPanel(new BorderLayout(8, 8));
        top.setOpaque(false);

        JLabel productIcon = new JLabel(iconFor(product.category), SwingConstants.CENTER);
        productIcon.setOpaque(true);
        productIcon.setBackground(HEADER);
        productIcon.setForeground(CYAN);
        productIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 25));
        productIcon.setPreferredSize(new Dimension(48, 48));
        productIcon.setBorder(BorderFactory.createLineBorder(BORDER));

        JPanel names = new JPanel();
        names.setOpaque(false);
        names.setLayout(new BoxLayout(names, BoxLayout.Y_AXIS));
        JLabel name = new JLabel(product.name);
        name.setForeground(WHITE);
        name.setFont(new Font("Arial", Font.BOLD, 15));
        name.setAlignmentX(Component.LEFT_ALIGNMENT);
        name.setToolTipText(product.name);

        JLabel category = new JLabel(product.category);
        category.setForeground(GRAY);
        category.setFont(new Font("Arial", Font.PLAIN, 12));
        category.setAlignmentX(Component.LEFT_ALIGNMENT);

        names.add(Box.createVerticalStrut(3));
        names.add(name);
        names.add(Box.createVerticalStrut(5));
        names.add(category);

        top.add(productIcon, BorderLayout.WEST);
        top.add(names, BorderLayout.CENTER);
        card.add(top, BorderLayout.NORTH);

        JPanel details = new JPanel(new GridLayout(3, 1, 0, 8));
        details.setOpaque(false);

        JLabel price = new JLabel("Price: " + String.format("₱%,.0f", product.price));
        price.setForeground(CYAN);
        price.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel stock = new JLabel("Stock: " + product.stock + " units");
        stock.setForeground(WHITE);
        stock.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel status = new JLabel("●  " + getStatus(product.stock));
        status.setFont(new Font("Arial", Font.BOLD, 12));
        status.setForeground(product.stock == 0 ? RED : product.stock <= 10 ? ORANGE : GREEN);

        details.add(price);
        details.add(stock);
        details.add(status);
        card.add(details, BorderLayout.CENTER);

        JPanel actions = new JPanel(new GridLayout(1, 2, 10, 0));
        actions.setOpaque(false);

        JButton edit = new JButton("");
        edit.setIcon(
            new ImageIcon(
                resize(
                    new ImageIcon(
                        "images/Nav_pics/edit.png"
                    )
                )
            )
        );

        styleActionButton(edit, CYAN, new Color(0x0F172A));
        edit.addActionListener(e -> showProductDialog(products.indexOf(product)));

        JButton delete = new JButton("");
        delete.setIcon(
            new ImageIcon(
                resize(
                    new ImageIcon(
                        "images/Nav_pics/delete.png"
                    )
                )
            )
        );


        styleActionButton(delete, new Color(0x3B1722), RED);
        delete.addActionListener(e -> deleteProduct(products.indexOf(product)));

        actions.add(edit);
        actions.add(delete);
        card.add(actions, BorderLayout.SOUTH);

        return card;
    }

    private String iconFor(String category) {
        switch (category) {
            case "Keyboard": return "⌨";
            case "Mouse": return "🖱";  
            case "Audio": return "🎧";
            case "Monitor": return "🖥";
            case "Graphics Card": return "▰";
            case "CPU": return "🔲";
            case "Motherboard": return "📟";
            case "RAM": return "💾";
            case "Storage": return "💿";
            case "Power Supply": return "🔌";
            case "Case": return "🖥";
            default: return "□";
        }
    }

    private String getStatus(int stock) {
        if (stock == 0) return "Out of Stock";
        if (stock <= 10) return "Low Stock";
        return "In Stock";
    }

    private void deleteProduct(int index) {
        if (index < 0 || index >= products.size()) return;
        Product product = products.get(index);
        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete\n" + product.name + "?",
                "Delete Product", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {

            db.deleteProduct(product.name);
            products.remove(index);
            refreshCards();
        }
    }

    private void showProductDialog(int index) {
        boolean editing = index >= 0 && index < products.size();
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                editing ? "Edit Product" : "Add New Product", true);
        dialog.setSize(430, 390);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(PANEL);
        panel.setBorder(new EmptyBorder(22, 28, 22, 28));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 5, 7, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JTextField nameField = new JTextField();
        JComboBox<String> categoryField = new JComboBox<>(new String[] {
                "Keyboard", "Mouse", "Audio", "Monitor", "Graphics Card","CPU", "Motherboard", "RAM", "Storage", "Power Supply", "Case"
        });
        JTextField priceField = new JTextField();
        JTextField stockField = new JTextField();

        styleInput(nameField);
        styleInput(priceField);
        styleInput(stockField);
        categoryField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryField.setBackground(PANEL);
        categoryField.setForeground(WHITE);
        categoryField.setPreferredSize(new Dimension(180, 38));

        if (editing) {
            Product product = products.get(index);
            nameField.setText(product.name);
            categoryField.setSelectedItem(product.category);
            priceField.setText(String.valueOf(product.price));
            stockField.setText(String.valueOf(product.stock));
        }

        addFormRow(panel, gbc, 0, "Product Name", nameField);
        addFormRow(panel, gbc, 1, "Category", categoryField);
        addFormRow(panel, gbc, 2, "Price (₱)", priceField);
        addFormRow(panel, gbc, 3, "Stock Quantity", stockField);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        JButton cancel = new JButton("Cancel");
        styleActionButton(cancel, new Color(0x334155), WHITE);
        JButton save = new JButton(editing ? "Save Changes" : "Add Product");
        stylePrimaryButton(save);
        cancel.addActionListener(e -> dialog.dispose());


        save.addActionListener(e -> {

            String name = nameField.getText().trim();

            String priceText = priceField.getText()
                    .trim()
                    .replace(",", "")
                    .replace("₱", "");

            String stockText = stockField.getText().trim();

            String selectedCategory =
                    (String) categoryField.getSelectedItem();

            
            if (name.isEmpty() ||
                priceText.isEmpty() ||
                stockText.isEmpty() ||
                selectedCategory == null) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Please fill in all fields."
                );
                return;
            }

            try {
                double price = Double.parseDouble(priceText);
                int stock = Integer.parseInt(stockText);

                
                if (price < 0 || stock < 0) {
                    JOptionPane.showMessageDialog(
                            dialog,
                            "Price and stock cannot be negative."
                    );
                    return;
                }

                
                if (!editing) {
                    if(products.stream().anyMatch(p -> p.name.equalsIgnoreCase(name) && p.category.equalsIgnoreCase(selectedCategory))) {
                        
                        JOptionPane.showMessageDialog(
                                dialog,
                                "This product already exists."
                        );
                        


                        return;
                    }



                    db.addProduct(
                            name,
                            selectedCategory,
                            price,
                            stock
                    );

                }

                              
                if (editing) {

                    // Get the original product before changing its name.
                    Product oldProduct = products.get(index);
                    String oldName = oldProduct.name;

                    // Prevent duplicate product names in the same category.
                    boolean duplicate = products.stream().anyMatch(p ->
                        !p.name.equalsIgnoreCase(oldName)
                        && p.name.equalsIgnoreCase(name)
                        && p.category.equalsIgnoreCase(selectedCategory)
                    );

                    if (duplicate) {
                        JOptionPane.showMessageDialog(
                            dialog,
                            "This product already exists."
                        );
                        return;
                    }

                    boolean success = db.updateProduct(
                        oldName,
                        name,
                        selectedCategory,
                        price,
                        stock
                    );

                    if (!success) {
                        JOptionPane.showMessageDialog(
                            dialog,
                            "Failed to update product. Please try again."
                        );
                        return;
                    }

                    products.set(index,
                        new Product(name, selectedCategory, price, stock));

                } else {

                    if (products.stream().anyMatch(p ->
                        p.name.equalsIgnoreCase(name)
                        && p.category.equalsIgnoreCase(selectedCategory)
                    )) {
                        JOptionPane.showMessageDialog(
                            dialog,
                            "This product already exists."
                        );
                        return;
                    }

                    db.addProduct(name, selectedCategory, price, stock);

                    products.add(
                        new Product(name, selectedCategory, price, stock)
                    );
                }

                refreshCards();
                dialog.dispose();


            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Price must be a number and stock must be a whole number."
                );
            }
        });

        buttons.add(cancel);
        buttons.add(save);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(buttons, gbc);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
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

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row,
            String labelText, JComponent field) {
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        JLabel label = new JLabel(labelText);
        label.setForeground(GRAY);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        field.setPreferredSize(new Dimension(210, 38));
        panel.add(field, gbc);
    }

    private void styleInput(JTextField field) {
        field.setBackground(HEADER);
        field.setForeground(WHITE);
        field.setCaretColor(CYAN);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(7, 9, 7, 9)));
    }

    private void stylePrimaryButton(JButton button) {
        button.setBackground(CYAN);
        button.setForeground(new Color(0x0F172A));
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(145, 40));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void styleActionButton(JButton button, Color background, Color foreground) {
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(75, 32));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}

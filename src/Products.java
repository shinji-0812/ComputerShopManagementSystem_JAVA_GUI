import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Products extends JPanel {

    // =========================================================
    // COLORS - SAME AS YOUR DASHBOARD
    // =========================================================

    private final Color BACKGROUND = new Color(0x0F172A);
    private final Color PANEL = new Color(0x111C3D);
    private final Color HEADER = new Color(0x0B1026);

    private final Color CYAN = new Color(0x22D3EE);
    private final Color WHITE = new Color(0xE2E8F0);
    private final Color GRAY = new Color(0x94A3B8);
    private final Color BORDER = new Color(0x22007C);

    // =========================================================
    // COMPONENTS
    // =========================================================

    private JTable productTable;

    private DefaultTableModel tableModel;

    private JTextField searchField;

    private JComboBox<String> categoryBox;

    // =========================================================
    // TEMPORARY PRODUCT DATA
    // =========================================================

    private ArrayList<Product> products =
        new ArrayList<>();


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public Products() {

        setBackground(
            BACKGROUND
        );

        setLayout(
            new BorderLayout()
        );

        setBorder(
            new EmptyBorder(
                25,
                25,
                25,
                25
            )
        );


        // =====================================================
        // SAMPLE PRODUCTS
        // =====================================================

        products.add(
            new Product(
                "Gaming Keyboard",
                "Keyboard",
                "₱1,500",
                24
            )
        );

        products.add(
            new Product(
                "Wireless Mouse",
                "Mouse",
                "₱850",
                18
            )
        );

        products.add(
            new Product(
                "Gaming Headset",
                "Audio",
                "₱2,200",
                5
            )
        );

        products.add(
            new Product(
                "24-inch Monitor",
                "Monitor",
                "₱8,500",
                12
            )
        );

        products.add(
            new Product(
                "RTX 4060",
                "Graphics Card",
                "₱18,500",
                0
            )
        );

        products.add(
            new Product(
                "Mechanical Keyboard",
                "Keyboard",
                "₱2,800",
                7
            )
        );


        // =====================================================
        // TOP SECTION
        // =====================================================

        JPanel top =
            new JPanel(
                new BorderLayout()
            );

        top.setOpaque(false);


        // =====================================================
        // TITLE
        // =====================================================

        JPanel titlePanel =
            new JPanel();

        titlePanel.setOpaque(false);

        titlePanel.setLayout(
            new BoxLayout(
                titlePanel,
                BoxLayout.Y_AXIS
            )
        );


        JLabel title =
            new JLabel(
                "Product Inventory"
            );

        title.setForeground(
            WHITE
        );

        title.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                22
            )
        );


        JLabel subtitle =
            new JLabel(
                "Manage your computer shop products"
            );

        subtitle.setForeground(
            GRAY
        );

        subtitle.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                13
            )
        );


        titlePanel.add(
            title
        );

        titlePanel.add(
            Box.createVerticalStrut(5)
        );

        titlePanel.add(
            subtitle
        );


        top.add(
            titlePanel,
            BorderLayout.WEST
        );


        // =====================================================
        // ADD PRODUCT BUTTON
        // =====================================================

        JButton addButton =
            new JButton(
                "+ Add Product"
            );

        stylePrimaryButton(
            addButton
        );


        addButton.addActionListener(
            e -> showProductDialog(-1)
        );


        top.add(
            addButton,
            BorderLayout.EAST
        );


        add(
            top,
            BorderLayout.NORTH
        );


        // =====================================================
        // SEARCH + FILTER
        // =====================================================

        JPanel tools =
            new JPanel(
                new BorderLayout(
                    15,
                    0
                )
            );

        tools.setOpaque(false);

        tools.setBorder(
            new EmptyBorder(
                20,
                0,
                15,
                0
            )
        );


        // =====================================================
        // SEARCH
        // =====================================================

        searchField =
            new JTextField(
                "Search products..."
            );

        searchField.setForeground(
            GRAY
        );

        searchField.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                14
            )
        );

        searchField.setBackground(
            PANEL
        );

        searchField.setCaretColor(
            CYAN
        );

        searchField.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    BORDER
                ),
                BorderFactory.createEmptyBorder(
                    8,
                    12,
                    8,
                    12
                )
            )
        );


        searchField.addFocusListener(
            new FocusAdapter() {

                @Override
                public void focusGained(
                    FocusEvent e
                ) {

                    if (
                        searchField
                            .getText()
                            .equals(
                                "Search products..."
                            )
                    ) {

                        searchField.setText("");

                        searchField.setForeground(
                            WHITE
                        );
                    }
                }


                @Override
                public void focusLost(
                    FocusEvent e
                ) {

                    if (
                        searchField
                            .getText()
                            .isEmpty()
                    ) {

                        searchField.setText(
                            "Search products..."
                        );

                        searchField.setForeground(
                            GRAY
                        );
                    }
                }
            }
        );


        searchField.getDocument()
            .addDocumentListener(
                new javax.swing.event.DocumentListener() {

                    public void insertUpdate(
                        javax.swing.event.DocumentEvent e
                    ) {

                        filterProducts();
                    }


                    public void removeUpdate(
                        javax.swing.event.DocumentEvent e
                    ) {

                        filterProducts();
                    }


                    public void changedUpdate(
                        javax.swing.event.DocumentEvent e
                    ) {

                        filterProducts();
                    }
                }
            );


        tools.add(
            searchField,
            BorderLayout.CENTER
        );


        // =====================================================
        // CATEGORY
        // =====================================================

        categoryBox =
            new JComboBox<>(
                new String[]{
                    "All Categories",
                    "Keyboard",
                    "Mouse",
                    "Audio",
                    "Monitor",
                    "Graphics Card"
                }
            );


        categoryBox.setPreferredSize(
            new Dimension(
                170,
                42
            )
        );

        categoryBox.setBackground(
            PANEL
        );

        categoryBox.setForeground(
            WHITE
        );

        categoryBox.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                13
            )
        );


        categoryBox.addActionListener(
            e -> filterProducts()
        );


        tools.add(
            categoryBox,
            BorderLayout.EAST
        );


        // =====================================================
        // TABLE AREA
        // =====================================================

        JPanel tableArea =
            new JPanel(
                new BorderLayout()
            );

        tableArea.setOpaque(false);

        tableArea.add(
            tools,
            BorderLayout.NORTH
        );


        // =====================================================
        // TABLE COLUMNS
        // =====================================================

        String[] columns = {
            "PRODUCT",
            "CATEGORY",
            "PRICE",
            "STOCK",
            "STATUS",
            "ACTIONS"
        };


        tableModel =
            new DefaultTableModel(
                columns,
                0
            ) {

                @Override
                public boolean isCellEditable(
                    int row,
                    int column
                ) {

                    // Only Actions column is editable
                    return column == 5;
                }
            };


        productTable =
            new JTable(
                tableModel
            );


        productTable.setRowHeight(
            60
        );

        productTable.setBackground(
            PANEL
        );

        productTable.setForeground(
            WHITE
        );

        productTable.setGridColor(
            BORDER
        );

        productTable.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                13
            )
        );


        // =====================================================
        // TABLE HEADER
        // =====================================================

        productTable.getTableHeader()
            .setBackground(
                HEADER
            );

        productTable.getTableHeader()
            .setForeground(
                WHITE
            );

        productTable.getTableHeader()
            .setFont(
                new Font(
                    "Arial",
                    Font.BOLD,
                    12
                )
            );

        productTable.getTableHeader()
            .setPreferredSize(
                new Dimension(
                    0,
                    40
                )
            );


        // =====================================================
        // COLUMN WIDTHS
        // =====================================================

        productTable
            .getColumnModel()
            .getColumn(0)
            .setPreferredWidth(220);

        productTable
            .getColumnModel()
            .getColumn(1)
            .setPreferredWidth(120);

        productTable
            .getColumnModel()
            .getColumn(2)
            .setPreferredWidth(100);

        productTable
            .getColumnModel()
            .getColumn(3)
            .setPreferredWidth(80);

        productTable
            .getColumnModel()
            .getColumn(4)
            .setPreferredWidth(110);

        productTable
            .getColumnModel()
            .getColumn(5)
            .setPreferredWidth(160);


        // =====================================================
        // CELL RENDERERS
        // =====================================================

        productTable
            .getColumnModel()
            .getColumn(2)
            .setCellRenderer(
                new CenterRenderer()
            );

        productTable
            .getColumnModel()
            .getColumn(3)
            .setCellRenderer(
                new CenterRenderer()
            );

        productTable
            .getColumnModel()
            .getColumn(4)
            .setCellRenderer(
                new StatusRenderer()
            );


        // =====================================================
        // ACTION BUTTONS
        // =====================================================

        productTable
            .getColumnModel()
            .getColumn(5)
            .setCellRenderer(
                new ActionRenderer()
            );

        productTable
            .getColumnModel()
            .getColumn(5)
            .setCellEditor(
                new ActionEditor()
            );


        // =====================================================
        // SCROLL PANE
        // =====================================================

        JScrollPane scrollPane =
            new JScrollPane(
                productTable
            );

        scrollPane.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scrollPane.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollPane.setBorder(null);

        scrollPane.setOpaque(false);

        scrollPane.getViewport()
            .setOpaque(false);


        JScrollBar verticalBar =
            scrollPane.getVerticalScrollBar();

        verticalBar.setPreferredSize(
            new Dimension(
                6,
                0
            )
        );

        verticalBar.setOpaque(false);


        verticalBar.setUI(
            new javax.swing.plaf.basic.BasicScrollBarUI() {

                @Override
                protected void configureScrollBarColors() {

                    this.thumbColor =
                        new Color(0x22D3EE);

                    this.trackColor =
                        new Color(0x0F172A);
                }


                @Override
                protected JButton createDecreaseButton(
                    int orientation
                ) {

                    return createZeroButton();
                }


                @Override
                protected JButton createIncreaseButton(
                    int orientation
                ) {

                    return createZeroButton();
                }


                private JButton createZeroButton() {

                    JButton button =
                        new JButton();

                    button.setPreferredSize(
                        new Dimension(
                            0,
                            0
                        )
                    );

                    return button;
                }
            }
        );


        tableArea.add(
            scrollPane,
            BorderLayout.CENTER
        );


        add(
            tableArea,
            BorderLayout.CENTER
        );


        // =====================================================
        // REFRESH TABLE
        // =====================================================

        refreshTable();
    }


    // =========================================================
    // REFRESH TABLE
    // =========================================================

    private void refreshTable() {

        tableModel.setRowCount(
            0
        );


        for (
            Product product : products
        ) {

            tableModel.addRow(
                new Object[]{
                    product.name,
                    product.category,
                    product.price,
                    product.stock,
                    getStatus(
                        product.stock
                    ),
                    ""
                }
            );
        }
    }


    // =========================================================
    // FILTER PRODUCTS
    // =========================================================

    private void filterProducts() {

        String search =
            searchField
                .getText()
                .toLowerCase();


        if (
            search.equals(
                "search products..."
            )
        ) {

            search = "";
        }


        String category =
            categoryBox
                .getSelectedItem()
                .toString();


        tableModel.setRowCount(
            0
        );


        for (
            int i = 0;
            i < products.size();
            i++
        ) {

            Product product =
                products.get(i);


            boolean matchesSearch =
                product.name
                    .toLowerCase()
                    .contains(search);


            boolean matchesCategory =
                category.equals(
                    "All Categories"
                )
                ||
                product.category.equals(
                    category
                );


            if (
                matchesSearch
                &&
                matchesCategory
            ) {

                tableModel.addRow(
                    new Object[]{
                        product.name,
                        product.category,
                        product.price,
                        product.stock,
                        getStatus(
                            product.stock
                        ),
                        ""
                    }
                );
            }
        }
    }


    // =========================================================
    // GET STATUS
    // =========================================================

    private String getStatus(
        int stock
    ) {

        if (
            stock == 0
        ) {

            return "Out of Stock";

        } else if (
            stock <= 10
        ) {

            return "Low Stock";

        } else {

            return "In Stock";
        }
    }


    // =========================================================
    // DELETE PRODUCT
    // =========================================================

    private void deleteProduct(
        int index
    ) {

        Product product =
            products.get(index);


        int result =
            JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete\n"
                + product.name
                + "?",
                "Delete Product",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );


        if (
            result ==
            JOptionPane.YES_OPTION
        ) {

            products.remove(
                index
            );

            refreshTable();
        }
    }


    // =========================================================
    // PRODUCT DIALOG
    // =========================================================

    private void showProductDialog(
        int index
    ) {

        boolean editing =
            index >= 0;


        JDialog dialog =
            new JDialog(
                (Frame) SwingUtilities
                    .getWindowAncestor(this),
                editing
                    ? "Edit Product"
                    : "Add New Product",
                true
            );


        dialog.setSize(
            430,
            420
        );

        dialog.setLocationRelativeTo(
            this
        );


        JPanel panel =
            new JPanel();


        panel.setBackground(
            PANEL
        );


        panel.setBorder(
            new EmptyBorder(
                25,
                30,
                25,
                30
            )
        );


        panel.setLayout(
            new GridLayout(
                5,
                2,
                12,
                12
            )
        );


        JTextField nameField =
            new JTextField();


        JTextField categoryField =
            new JTextField();


        JTextField priceField =
            new JTextField();


        JTextField stockField =
            new JTextField();


        // =====================================================
        // LOAD EXISTING PRODUCT
        // =====================================================

        if (
            editing
        ) {

            Product product =
                products.get(index);


            nameField.setText(
                product.name
            );


            categoryField.setText(
                product.category
            );


            priceField.setText(
                product.price
                    .replace(
                        "₱",
                        ""
                    )
                    .replace(
                        ",",
                        ""
                    )
            );


            stockField.setText(
                String.valueOf(
                    product.stock
                )
            );
        }


        // =====================================================
        // FORM
        // =====================================================

        panel.add(
            createLabel(
                "Product Name"
            )
        );

        panel.add(
            nameField
        );


        panel.add(
            createLabel(
                "Category"
            )
        );

        panel.add(
            categoryField
        );


        panel.add(
            createLabel(
                "Price"
            )
        );

        panel.add(
            priceField
        );


        panel.add(
            createLabel(
                "Stock"
            )
        );

        panel.add(
            stockField
        );


        // =====================================================
        // BUTTONS
        // =====================================================

        JPanel buttonPanel =
            new JPanel(
                new FlowLayout(
                    FlowLayout.RIGHT
                )
            );


        buttonPanel.setOpaque(
            false
        );


        JButton cancel =
            new JButton(
                "Cancel"
            );


        JButton save =
            new JButton(
                editing
                    ? "Save Changes"
                    : "Add Product"
            );


        cancel.setFocusPainted(
            false
        );


        stylePrimaryButton(
            save
        );


        cancel.addActionListener(
            e -> dialog.dispose()
        );


        save.addActionListener(
            e -> {

                try {

                    String name =
                        nameField
                            .getText()
                            .trim();


                    String category =
                        categoryField
                            .getText()
                            .trim();


                    String price =
                        priceField
                            .getText()
                            .trim();


                    int stock =
                        Integer.parseInt(
                            stockField
                                .getText()
                                .trim()
                        );


                    if (
                        name.isEmpty()
                        ||
                        category.isEmpty()
                        ||
                        price.isEmpty()
                    ) {

                        JOptionPane.showMessageDialog(
                            dialog,
                            "Please fill in all fields."
                        );

                        return;
                    }


                    double priceNumber =
                        Double.parseDouble(
                            price
                        );


                    String formattedPrice =
                        "₱"
                        +
                        String.format(
                            "%,.0f",
                            priceNumber
                        );


                    Product product =
                        new Product(
                            name,
                            category,
                            formattedPrice,
                            stock
                        );


                    if (
                        editing
                    ) {

                        products.set(
                            index,
                            product
                        );

                    } else {

                        products.add(
                            product
                        );
                    }


                    refreshTable();

                    dialog.dispose();


                } catch (
                    NumberFormatException ex
                ) {

                    JOptionPane.showMessageDialog(
                        dialog,
                        "Price and stock must be valid numbers."
                    );
                }
            }
        );


        buttonPanel.add(
            cancel
        );


        buttonPanel.add(
            save
        );


        panel.add(
            new JLabel()
        );


        panel.add(
            buttonPanel
        );


        dialog.add(
            panel
        );


        dialog.setVisible(
            true
        );
    }


    // =========================================================
    // LABEL
    // =========================================================

    private JLabel createLabel(
        String text
    ) {

        JLabel label =
            new JLabel(
                text
            );


        label.setForeground(
            GRAY
        );


        label.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                13
            )
        );


        return label;
    }


    // =========================================================
    // PRIMARY BUTTON STYLE
    // =========================================================

    private void stylePrimaryButton(
        JButton button
    ) {

        button.setBackground(
            CYAN
        );


        button.setForeground(
            new Color(
                0x0F172A
            )
        );


        button.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                13
            )
        );


        button.setFocusPainted(
            false
        );


        button.setBorderPainted(
            false
        );


        button.setPreferredSize(
            new Dimension(
                145,
                40
            )
        );
    }


    // =========================================================
    // ACTION BUTTON STYLE
    // =========================================================

    private void styleActionButton(
        JButton button,
        Color color
    ) {

        button.setBackground(
            color
        );


        if (
            color.equals(
                CYAN
            )
        ) {

            button.setForeground(
                new Color(
                    0x0F172A
                )
            );

        } else {

            button.setForeground(
                WHITE
            );
        }


        button.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                12
            )
        );


        button.setFocusPainted(
            false
        );


        button.setBorderPainted(
            false
        );


        button.setPreferredSize(
            new Dimension(
                65,
                30
            )
        );
    }


    // =========================================================
    // PRODUCT CLASS
    // =========================================================

    private static class Product {

        String name;

        String category;

        String price;

        int stock;


        Product(
            String name,
            String category,
            String price,
            int stock
        ) {

            this.name =
                name;


            this.category =
                category;


            this.price =
                price;


            this.stock =
                stock;
        }
    }


    // =========================================================
    // CENTER RENDERER
    // =========================================================

    private class CenterRenderer
        extends DefaultTableCellRenderer {

        public CenterRenderer() {

            setHorizontalAlignment(
                SwingConstants.CENTER
            );
        }


        @Override
        public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column
        ) {

            Component c =
                super.getTableCellRendererComponent(
                    table,
                    value,
                    isSelected,
                    hasFocus,
                    row,
                    column
                );


            c.setBackground(
                PANEL
            );


            c.setForeground(
                WHITE
            );


            return c;
        }
    }


    // =========================================================
    // STATUS RENDERER
    // =========================================================

    private class StatusRenderer
        extends DefaultTableCellRenderer {

        public StatusRenderer() {

            setHorizontalAlignment(
                SwingConstants.CENTER
            );
        }


        @Override
        public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column
        ) {

            Component c =
                super.getTableCellRendererComponent(
                    table,
                    value,
                    isSelected,
                    hasFocus,
                    row,
                    column
                );


            c.setBackground(
                PANEL
            );


            if (
                value.equals(
                    "In Stock"
                )
            ) {

                c.setForeground(
                    new Color(
                        0x22C55E
                    )
                );

            } else if (
                value.equals(
                    "Low Stock"
                )
            ) {

                c.setForeground(
                    new Color(
                        0xF59E0B
                    )
                );

            } else {

                c.setForeground(
                    new Color(
                        0xEF4444
                    )
                );
            }


            return c;
        }
    }


    // =========================================================
    // ACTION RENDERER
    // =========================================================

    private class ActionRenderer
        extends JPanel
        implements javax.swing.table.TableCellRenderer {

        private JButton editButton;

        private JButton deleteButton;


        public ActionRenderer() {

            setOpaque(
                true
            );


            setBackground(
                PANEL
            );


            setLayout(
                new FlowLayout(
                    FlowLayout.CENTER,
                    8,
                    10
                )
            );


            editButton =
                new JButton(
                    "Edit"
                );


            deleteButton =
                new JButton(
                    "Delete"
                );


            styleActionButton(
                editButton,
                CYAN
            );


            styleActionButton(
                deleteButton,
                new Color(
                    0xEF4444
                )
            );


            add(
                editButton
            );


            add(
                deleteButton
            );
        }


        @Override
        public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column
        ) {

            setBackground(
                PANEL
            );


            return this;
        }
    }


    // =========================================================
    // ACTION EDITOR
    // =========================================================

    private class ActionEditor
        extends AbstractCellEditor
        implements javax.swing.table.TableCellEditor {

        private JPanel panel;

        private JButton editButton;

        private JButton deleteButton;

        private int row;


        public ActionEditor() {

            panel =
                new JPanel(
                    new FlowLayout(
                        FlowLayout.CENTER,
                        8,
                        10
                    )
                );


            panel.setBackground(
                PANEL
            );


            editButton =
                new JButton(
                    "Edit"
                );


            deleteButton =
                new JButton(
                    "Delete"
                );


            styleActionButton(
                editButton,
                CYAN
            );


            styleActionButton(
                deleteButton,
                new Color(
                    0xEF4444
                )
            );


            // =================================================
            // EDIT
            // =================================================

            editButton.addActionListener(
                e -> {

                    fireEditingStopped();


                    int modelRow =
                        productTable
                            .convertRowIndexToModel(
                                row
                            );


                    showProductDialog(
                        modelRow
                    );
                }
            );


            // =================================================
            // DELETE
            // =================================================

            deleteButton.addActionListener(
                e -> {

                    fireEditingStopped();


                    int modelRow =
                        productTable
                            .convertRowIndexToModel(
                                row
                            );


                    deleteProduct(
                        modelRow
                    );
                }
            );


            panel.add(
                editButton
            );


            panel.add(
                deleteButton
            );
        }


        @Override
        public Component getTableCellEditorComponent(
            JTable table,
            Object value,
            boolean isSelected,
            int row,
            int column
        ) {

            this.row =
                row;


            return panel;
        }


        @Override
        public Object getCellEditorValue() {

            return "";
        }
    }
}

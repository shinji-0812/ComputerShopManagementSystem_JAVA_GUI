
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Sales extends JPanel {

    // COLORS
    private final Color BACKGROUND = new Color(0x0F172A);
    private final Color PANEL = new Color(0x111C3D);
    private final Color FIELD = new Color(0x0B1226);
    private final Color CYAN = new Color(0x22D3EE);
    private final Color WHITE = new Color(0xE2E8F0);
    private final Color GRAY = new Color(0x94A3B8);
    private final Color BORDER = new Color(0x263454);
    private final Color GREEN = new Color(0x22C55E);
    private final Color RED = new Color(0xEF4444);

    private final ArrayList<Product> products = new ArrayList<>();
    private final ArrayList<Transaction> transactions = new ArrayList<>();

    private int nextTransactionNumber = 3;

    private JComboBox<String> customerField;
    private JComboBox<String> productField;
    private JComboBox<String> paymentField;
    private JSpinner quantityField;

    private JLabel unitPriceLabel;
    private JLabel totalLabel;

    private JTextField searchField;
    private JComboBox<String> statusFilter;

    private JTable transactionTable;
    private DefaultTableModel tableModel;

    public Sales() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(20, 22, 20, 22));

        loadSampleData();
        buildUI();
        refreshTransactionTable();
        updateTotal();
    }

    // =====================================================
    // SAMPLE DATA
    // =====================================================

    private void loadSampleData() {

        products.add(new Product("Gaming Keyboard", 1500, 18));
        products.add(new Product("Wireless Mouse", 850, 25));
        products.add(new Product("Gaming Headset", 2200, 12));
        products.add(new Product("24-inch Monitor", 8500, 8));
        products.add(new Product("Graphics Card", 18500, 5));

        transactions.add(new Transaction(
            "TRX-001", "John", "Gaming Keyboard",
            1, 1500, "Cash", "Completed", new Date()
        ));

        transactions.add(new Transaction(
            "TRX-002", "Mark", "Wireless Mouse",
            1, 850, "GCash", "Completed", new Date()
        ));
    }

    // =====================================================
    // MAIN UI
    // =====================================================

    private void buildUI() {

        JPanel heading = new JPanel(new BorderLayout());
        heading.setOpaque(false);

        JPanel headingText = new JPanel();
        headingText.setOpaque(false);
        headingText.setLayout(
            new BoxLayout(headingText, BoxLayout.Y_AXIS)
        );

        JLabel title = new JLabel("MANAGE THE SALES");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(WHITE);

        JLabel subtitle = new JLabel(
            "Create transactions and manage customer purchases."
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(GRAY);

        headingText.add(title);
        headingText.add(Box.createVerticalStrut(5));
        headingText.add(subtitle);

        heading.add(headingText, BorderLayout.WEST);

        JPanel formCard = createCard();
        formCard.setLayout(new BorderLayout(0, 14));
        formCard.add(
            createSectionTitle("Create New Transaction"),
            BorderLayout.NORTH
        );
        formCard.add(buildSaleForm(), BorderLayout.CENTER);

        JPanel historyCard = createCard();
        historyCard.setLayout(new BorderLayout(0, 14));
        historyCard.add(buildHistoryHeader(), BorderLayout.NORTH);
        historyCard.add(buildHistoryTable(), BorderLayout.CENTER);

        JPanel stacked = new JPanel();
        stacked.setOpaque(false);
        stacked.setLayout(
            new BoxLayout(stacked, BoxLayout.Y_AXIS)
        );

        formCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        historyCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        stacked.add(formCard);
        stacked.add(Box.createVerticalStrut(18));
        stacked.add(historyCard);

        JScrollPane pageScroll = new JScrollPane(stacked);

        pageScroll.setBorder(BorderFactory.createEmptyBorder());
        pageScroll.setOpaque(false);
        pageScroll.getViewport().setOpaque(false);
        pageScroll.getVerticalScrollBar().setUnitIncrement(16);
        pageScroll.getVerticalScrollBar().setPreferredSize(new Dimension(7, 0));
        pageScroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
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
        pageScroll.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );

        pageScroll.getViewport().addComponentListener(
            new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    int width = pageScroll.getViewport().getWidth();

                    if (width > 0) {
                        Dimension formSize = formCard.getPreferredSize();
                        Dimension historySize =
                            historyCard.getPreferredSize();

                        formCard.setPreferredSize(
                            new Dimension(width, formSize.height)
                        );

                        historyCard.setPreferredSize(
                            new Dimension(
                                width,
                                Math.max(280, historySize.height)
                            )
                        );

                        stacked.setPreferredSize(
                            new Dimension(
                                width,
                                formCard.getPreferredSize().height
                                + historyCard.getPreferredSize().height
                                + 18
                            )
                        );

                        stacked.revalidate();
                    }
                }
            }
        );

        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(18, 0, 0, 0));
        content.add(pageScroll, BorderLayout.CENTER);

        add(heading, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
    }

    // =====================================================
    // SALE FORM
    // =====================================================

    private JPanel buildSaleForm() {

        JPanel form = new JPanel(new BorderLayout(0, 14));
        form.setOpaque(false);

        JPanel fields = new JPanel(
            new GridLayout(0, 3, 12, 12)
        );
        fields.setOpaque(false);

        customerField = new JComboBox<>(new String[] {
            "Walk-in Customer", "John", "Mark", "Alex", "David"
        });
        styleComboBox(customerField);

        productField = new JComboBox<>();
        for (Product product : products) {
            productField.addItem(product.name);
        }

        styleComboBox(productField);
        productField.addActionListener(e -> updateTotal());

        quantityField = new JSpinner(
            new SpinnerNumberModel(1, 1, 999, 1)
        );

        quantityField.setFont(
            new Font("Segoe UI", Font.PLAIN, 14)
        );
        quantityField.setBackground(FIELD);
        quantityField.setForeground(WHITE);
        quantityField.setBorder(new LineBorder(BORDER));

        JComponent editor = quantityField.getEditor();

        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField spinnerText =
                ((JSpinner.DefaultEditor) editor).getTextField();

            spinnerText.setBackground(FIELD);
            spinnerText.setForeground(WHITE);
            spinnerText.setCaretColor(CYAN);
            spinnerText.setBorder(
                new EmptyBorder(7, 8, 7, 8)
            );
        }

        quantityField.addChangeListener(e -> updateTotal());

        paymentField = new JComboBox<>(new String[] {
            "Cash", "GCash", "Credit/Debit Card", "Bank Transfer"
        });
        styleComboBox(paymentField);

        unitPriceLabel = new JLabel("₱0.00");
        unitPriceLabel.setFont(
            new Font("Segoe UI", Font.BOLD, 14)
        );
        unitPriceLabel.setForeground(WHITE);
        unitPriceLabel.setOpaque(true);
        unitPriceLabel.setBackground(FIELD);
        unitPriceLabel.setBorder(
            new EmptyBorder(10, 10, 10, 10)
        );

        fields.add(fieldPanel("Customer", customerField));
        fields.add(fieldPanel("Product", productField));
        fields.add(fieldPanel("Quantity", quantityField));
        fields.add(fieldPanel("Payment Method", paymentField));
        fields.add(fieldPanel("Unit Price", unitPriceLabel));

        JPanel totalPanel = new JPanel();
        totalPanel.setOpaque(false);
        totalPanel.setLayout(
            new BoxLayout(totalPanel, BoxLayout.Y_AXIS)
        );

        JLabel totalCaption = new JLabel("Total Amount");
        totalCaption.setFont(
            new Font("Segoe UI", Font.PLAIN, 12)
        );
        totalCaption.setForeground(GRAY);

        totalLabel = new JLabel("₱0.00");
        totalLabel.setFont(
            new Font("Segoe UI", Font.BOLD, 23)
        );
        totalLabel.setForeground(CYAN);

        totalPanel.add(totalCaption);
        totalPanel.add(Box.createVerticalStrut(6));
        totalPanel.add(totalLabel);
        fields.add(totalPanel);

        JPanel actions = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 10, 0)
        );
        actions.setOpaque(false);

        JButton clearButton = createButton(
            "Clear", new Color(0x1E293B), WHITE
        );
        clearButton.setBorder(new LineBorder(BORDER));
        clearButton.addActionListener(e -> clearForm());

        JButton completeButton = createButton(
            "Complete Sale", GREEN, Color.WHITE
        );
        completeButton.addActionListener(e -> completeSale());

        actions.add(clearButton);
        actions.add(completeButton);

        form.add(fields, BorderLayout.CENTER);
        form.add(actions, BorderLayout.SOUTH);

        return form;
    }

    // =====================================================
    // TRANSACTION HISTORY HEADER
    // =====================================================

    private JPanel buildHistoryHeader() {

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(
            new BoxLayout(header, BoxLayout.Y_AXIS)
        );

        JLabel title = createSectionTitle("Transaction History");

        JPanel tools = new JPanel(new GridBagLayout());
        tools.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 0, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1;

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(150, 36));
        styleTextField(searchField);
        searchField.setToolTipText(
            "Search transaction, customer, or product"
        );

        searchField.getDocument().addDocumentListener(
            new javax.swing.event.DocumentListener() {
                public void insertUpdate(
                    javax.swing.event.DocumentEvent e
                ) {
                    refreshTransactionTable();
                }

                public void removeUpdate(
                    javax.swing.event.DocumentEvent e
                ) {
                    refreshTransactionTable();
                }

                public void changedUpdate(
                    javax.swing.event.DocumentEvent e
                ) {
                    refreshTransactionTable();
                }
            }
        );

        // CHANGED: Voided -> Canceled
        statusFilter = new JComboBox<>(new String[] {
            "All Status", "Completed", "Canceled"
        });
        styleComboBox(statusFilter);
        statusFilter.addActionListener(
            e -> refreshTransactionTable()
        );

        JButton receiptButton = createButton(
            "View Receipt", new Color(0x1E293B), WHITE
        );
        receiptButton.setBorder(new LineBorder(BORDER));
        receiptButton.addActionListener(
            e -> showSelectedReceipt()
        );

        // NEW: CANCEL TRANSACTION BUTTON
        JButton cancelButton = createButton(
            "Cancel Transaction", RED, Color.WHITE
        );
        cancelButton.addActionListener(
            e -> cancelSelectedTransaction()
        );

        gbc.gridx = 0;
        gbc.weightx = 1;
        tools.add(searchField, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0;
        tools.add(statusFilter, gbc);

        gbc.gridx = 2;
        tools.add(receiptButton, gbc);

        gbc.gridx = 3;
        gbc.insets = new Insets(8, 0, 0, 0);
        tools.add(cancelButton, gbc);

        header.add(title);
        header.add(tools);

        return header;
    }

    // =====================================================
    // TRANSACTION TABLE
    // =====================================================

    private JScrollPane buildHistoryTable() {

        String[] columns = {
            "Transaction ID", "Customer", "Product",
            "Qty", "Total", "Payment", "Date",
            "Status", "Action"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        transactionTable = new JTable(tableModel);
        transactionTable.setFont(
            new Font("Segoe UI", Font.PLAIN, 12)
        );
        transactionTable.setRowHeight(38);
        transactionTable.setForeground(WHITE);
        transactionTable.setBackground(PANEL);
        transactionTable.setGridColor(BORDER);
        transactionTable.setSelectionBackground(
            new Color(0x164E63)
        );
        transactionTable.setSelectionForeground(WHITE);
        transactionTable.setShowVerticalLines(false);
        transactionTable.setAutoResizeMode(
            JTable.AUTO_RESIZE_OFF
        );
        transactionTable.setFillsViewportHeight(true);

        transactionTable.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 12)
        );
        transactionTable.getTableHeader().setBackground(
            new Color(0x0B1026)
        );
        transactionTable.getTableHeader().setForeground(WHITE);
        transactionTable.getTableHeader().setReorderingAllowed(false);
        transactionTable.getTableHeader().setPreferredSize(
            new Dimension(0, 38)
        );

        int[] widths = {
            125, 110, 160, 55, 100, 125, 115, 95, 125
        };

        for (int i = 0; i < widths.length; i++) {
            transactionTable.getColumnModel()
                .getColumn(i).setPreferredWidth(widths[i]);
        }

        DefaultTableCellRenderer center =
            new DefaultTableCellRenderer();

        center.setHorizontalAlignment(SwingConstants.CENTER);
        center.setForeground(WHITE);
        center.setBackground(PANEL);

        for (int i = 0; i < widths.length; i++) {
            transactionTable.getColumnModel()
                .getColumn(i).setCellRenderer(center);
        }

        // NEW: STATUS COLOR RENDERER
        transactionTable.getColumnModel().getColumn(7)
            .setCellRenderer(new DefaultTableCellRenderer() {

                @Override
                public Component getTableCellRendererComponent(
                    JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column
                ) {
                    super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus,
                        row, column
                    );

                    setHorizontalAlignment(SwingConstants.CENTER);

                    setBackground(
                        isSelected
                            ? table.getSelectionBackground()
                            : PANEL
                    );

                    setFont(new Font(
                        "Segoe UI", Font.BOLD, 12
                    ));

                    if ("Completed".equals(value)) {
                        setForeground(GREEN);
                    } else if ("Canceled".equals(value)) {
                        setForeground(RED);
                    } else {
                        setForeground(WHITE);
                    }

                    return this;
                }
            });

        transactionTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = transactionTable.rowAtPoint(e.getPoint());
                int column = transactionTable.columnAtPoint(e.getPoint());

                if (row >= 0 &&
                    (column == 8 || e.getClickCount() == 2)) {
                    transactionTable.setRowSelectionInterval(row, row);
                    showSelectedReceipt();
                }
            }
        });

        JScrollPane scroll = new JScrollPane(transactionTable);
        scroll.setBorder(new LineBorder(BORDER));
        scroll.getViewport().setBackground(PANEL);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        scroll.getHorizontalScrollBar().setUnitIncrement(14);
        scroll.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        return scroll;
    }

    // =====================================================
    // COMPLETE SALE
    // =====================================================

    private void completeSale() {

        Product selectedProduct = getSelectedProduct();

        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(
                this, "Please select a product.",
                "Missing Product", JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int quantity = (Integer) quantityField.getValue();

        if (quantity > selectedProduct.stock) {
            JOptionPane.showMessageDialog(
                this,
                "Not enough stock. Available quantity: "
                    + selectedProduct.stock,
                "Insufficient Stock",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        double total = selectedProduct.price * quantity;

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Complete this sale for " + money(total) + "?",
            "Confirm Sale",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String transactionId = String.format(
            "TRX-%03d", nextTransactionNumber++
        );

        Transaction transaction = new Transaction(
            transactionId,
            String.valueOf(customerField.getSelectedItem()),
            selectedProduct.name,
            quantity,
            total,
            String.valueOf(paymentField.getSelectedItem()),
            "Completed",
            new Date()
        );

        transactions.add(0, transaction);

        // Reduce stock when a sale is completed.
        selectedProduct.stock -= quantity;

        refreshTransactionTable();
        showReceipt(transaction);
        clearForm();
    }

    // =====================================================
    // NEW: CANCEL SELECTED TRANSACTION
    // =====================================================

    private void cancelSelectedTransaction() {

        int selectedRow = transactionTable.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                this,
                "Please select a transaction to cancel.",
                "No Transaction Selected",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int modelRow =
            transactionTable.convertRowIndexToModel(selectedRow);

        String transactionId = String.valueOf(
            tableModel.getValueAt(modelRow, 0)
        );

        Transaction selectedTransaction = null;

        for (Transaction transaction : transactions) {
            if (transaction.id.equals(transactionId)) {
                selectedTransaction = transaction;
                break;
            }
        }

        if (selectedTransaction == null) {
            JOptionPane.showMessageDialog(
                this,
                "Transaction not found.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Do not allow a transaction to be canceled twice.
        if ("Canceled".equals(selectedTransaction.status)) {
            JOptionPane.showMessageDialog(
                this,
                "This transaction has already been canceled.",
                "Already Canceled",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to cancel this transaction?\n\n"
                + "Transaction: " + selectedTransaction.id + "\n"
                + "Customer: " + selectedTransaction.customer + "\n"
                + "Product: " + selectedTransaction.product + "\n"
                + "Total: " + money(selectedTransaction.total)
                + "\n\nThis action will restore the product stock.",
            "Confirm Cancellation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Restore the stock of the sold product.
        Product soldProduct = null;

        for (Product product : products) {
            if (product.name.equals(selectedTransaction.product)) {
                soldProduct = product;
                break;
            }
        }

        if (soldProduct == null) {
            JOptionPane.showMessageDialog(
                this,
                "The product could not be found. "
                    + "Cancellation was not completed.",
                "Product Not Found",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // IMPORTANT: Change status and restore stock only once.
        selectedTransaction.status = "Canceled";
        soldProduct.stock += selectedTransaction.quantity;

        refreshTransactionTable();

        JOptionPane.showMessageDialog(
            this,
            "Transaction " + selectedTransaction.id
                + " has been canceled.\n"
                + "Product stock has been restored.",
            "Transaction Canceled",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =====================================================
    // UPDATE TOTAL
    // =====================================================

    private void updateTotal() {

        Product product = getSelectedProduct();

        if (product == null || quantityField == null) {
            if (unitPriceLabel != null) {
                unitPriceLabel.setText("₱0.00");
            }
            if (totalLabel != null) {
                totalLabel.setText("₱0.00");
            }
            return;
        }

        int quantity = (Integer) quantityField.getValue();

        unitPriceLabel.setText(money(product.price));
        totalLabel.setText(money(product.price * quantity));
    }

    private Product getSelectedProduct() {

        if (productField == null) {
            return null;
        }

        String selectedName =
            (String) productField.getSelectedItem();

        if (selectedName == null) {
            return null;
        }

        for (Product product : products) {
            if (product.name.equals(selectedName)) {
                return product;
            }
        }

        return null;
    }

    // =====================================================
    // REFRESH TRANSACTION TABLE
    // =====================================================

    private void refreshTransactionTable() {

        if (tableModel == null) {
            return;
        }

        tableModel.setRowCount(0);

        String search = searchField == null
            ? ""
            : searchField.getText().trim().toLowerCase();

        String status = statusFilter == null
            ? "All Status"
            : String.valueOf(statusFilter.getSelectedItem());

        SimpleDateFormat dateFormat =
            new SimpleDateFormat("MMM dd, yyyy");

        for (Transaction transaction : transactions) {

            boolean matchesSearch =
                transaction.id.toLowerCase().contains(search)
                || transaction.customer.toLowerCase().contains(search)
                || transaction.product.toLowerCase().contains(search);

            boolean matchesStatus =
                status.equals("All Status")
                || transaction.status.equals(status);

            if (matchesSearch && matchesStatus) {
                tableModel.addRow(new Object[] {
                    transaction.id,
                    transaction.customer,
                    transaction.product,
                    transaction.quantity,
                    money(transaction.total),
                    transaction.payment,
                    dateFormat.format(transaction.date),
                    transaction.status,
                    "View Receipt  →"
                });
            }
        }
    }

    // =====================================================
    // RECEIPT
    // =====================================================

    private void showSelectedReceipt() {

        int selectedRow = transactionTable.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                this, "Select a transaction first.",
                "No Transaction Selected",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        int modelRow =
            transactionTable.convertRowIndexToModel(selectedRow);

        String id = String.valueOf(
            transactionTable.getValueAt(modelRow, 0)
        );

        for (Transaction transaction : transactions) {
            if (transaction.id.equals(id)) {
                showReceipt(transaction);
                return;
            }
        }
    }

    private void showReceipt(Transaction transaction) {

        SimpleDateFormat dateFormat =
            new SimpleDateFormat("MMM dd, yyyy hh:mm a");

        JTextArea receipt = new JTextArea();

        receipt.setEditable(false);
        receipt.setFont(new Font("Monospaced", Font.PLAIN, 13));
        receipt.setBackground(FIELD);
        receipt.setForeground(WHITE);
        receipt.setBorder(new EmptyBorder(16, 18, 16, 18));

        receipt.setText(
            "       COMPUTER SHOP\n" +
            "       SALES RECEIPT\n" +
            "----------------------------------\n" +
            "Transaction: " + transaction.id + "\n" +
            "Date:        " + dateFormat.format(transaction.date) + "\n" +
            "Customer:    " + transaction.customer + "\n" +
            "----------------------------------\n" +
            "Product:     " + transaction.product + "\n" +
            "Quantity:    " + transaction.quantity + "\n" +
            "Total:       " + money(transaction.total) + "\n" +
            "Payment:     " + transaction.payment + "\n" +
            "Status:      " + transaction.status + "\n" +
            "----------------------------------\n" +
            "       Thank you for shopping!\n"
        );

        JScrollPane scroll = new JScrollPane(receipt);
        scroll.setPreferredSize(new Dimension(390, 330));
        scroll.setBorder(new LineBorder(BORDER));

        JOptionPane.showMessageDialog(
            this, scroll,
            "Receipt - " + transaction.id,
            JOptionPane.PLAIN_MESSAGE
        );
    }

    // =====================================================
    // CLEAR FORM
    // =====================================================

    private void clearForm() {

        if (customerField != null) {
            customerField.setSelectedIndex(0);
        }

        if (productField != null) {
            productField.setSelectedIndex(0);
        }

        if (quantityField != null) {
            quantityField.setValue(1);
        }

        if (paymentField != null) {
            paymentField.setSelectedIndex(0);
        }

        updateTotal();
    }

    // =====================================================
    // UI HELPERS
    // =====================================================

    private JPanel fieldPanel(String labelText, JComponent field) {

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(
            new BoxLayout(panel, BoxLayout.Y_AXIS)
        );

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(GRAY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setPreferredSize(new Dimension(100, 38));
        field.setMinimumSize(new Dimension(60, 38));
        field.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 38)
        );

        panel.add(label);
        panel.add(Box.createVerticalStrut(7));
        panel.add(field);

        return panel;
    }

    private JPanel createCard() {

        JPanel card = new JPanel();
        card.setBackground(PANEL);

        card.setBorder(
            BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(18, 18, 18, 18)
            )
        );

        return card;
    }

    private JLabel createSectionTitle(String text) {

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 17));
        label.setForeground(WHITE);

        return label;
    }

    private JButton createButton(
        String text, Color background, Color foreground
    ) {

        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(foreground);
        button.setBackground(background);
        button.setFocusPainted(false);
        button.setCursor(
            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );
        button.setBorder(new EmptyBorder(10, 16, 10, 16));
        button.setOpaque(true);

        return button;
    }

    private void styleComboBox(JComboBox<String> combo) {

        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setForeground(WHITE);
        combo.setBackground(FIELD);
        combo.setBorder(new LineBorder(BORDER));
        combo.setFocusable(false);
    }

    private void styleTextField(JTextField field) {

        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setForeground(WHITE);
        field.setBackground(FIELD);
        field.setCaretColor(CYAN);

        field.setBorder(
            BorderFactory.createCompoundBorder(
                new LineBorder(BORDER),
                new EmptyBorder(7, 9, 7, 9)
            )
        );
    }

    private String money(double amount) {
        return String.format("₱%,.2f", amount);
    }

    // =====================================================
    // DATA CLASSES
    // =====================================================

    private static class Product {
        String name;
        double price;
        int stock;

        Product(String name, double price, int stock) {
            this.name = name;
            this.price = price;
            this.stock = stock;
        }
    }

    private static class Transaction {
        String id;
        String customer;
        String product;
        int quantity;
        double total;
        String payment;
        String status;
        Date date;

        Transaction(
            String id, String customer, String product,
            int quantity, double total, String payment,
            String status, Date date
        ) {
            this.id = id;
            this.customer = customer;
            this.product = product;
            this.quantity = quantity;
            this.total = total;
            this.payment = payment;
            this.status = status;
            this.date = date;
        }
    }
}
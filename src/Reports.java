
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Reports extends JPanel {

    // COLORS
    private final Color BACKGROUND = new Color(0x0F172A);
    private final Color PANEL = new Color(0x111C3D);
    private final Color CYAN = new Color(0x22D3EE);
    private final Color WHITE = new Color(0xE2E8F0);
    private final Color GRAY = new Color(0x94A3B8);
    private final Color BORDER = new Color(0x22007C);
    private final Color GREEN = new Color(0x4ADE80);
    private final Color RED = new Color(0xF87171);
    private final Color PURPLE = new Color(0x818CF8);
    private final Color YELLOW = new Color(0xFBBF24);
    private final Color PINK = new Color(0xF472B6);

    private final Database db;
    private final Computes compute;

    private JLabel revenueValue;
    private JLabel ordersValue;
    private JLabel productsValue;
    private JLabel averageValue;

    private JPanel main;
    private JPanel summary;
    private JPanel bottom;
    private JPanel categoryPanel;

    private JScrollPane scroll;

    private int lastWidth = -1;

    public Reports(Database db) {

        this.db = db;
        this.compute = new Computes(db);

        compute.compute();
        compute.topSellingProducts();

        setLayout(new BorderLayout());
        setBackground(BACKGROUND);

        // MAIN CONTENT
        main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(BACKGROUND);
        main.setBorder(new EmptyBorder(16, 16, 16, 16));
        main.setAlignmentX(Component.LEFT_ALIGNMENT);



        summary = new JPanel(new GridLayout(1, 4, 12, 12));
        summary.setOpaque(false);
        summary.setAlignmentX(Component.LEFT_ALIGNMENT);

        revenueValue = new JLabel(
            String.format("₱%,.2f", compute.totalSales)
        );

        ordersValue = new JLabel(
            String.valueOf(compute.completedOrders)
        );

        productsValue = new JLabel(
            String.valueOf(compute.productsSold)
        );

        averageValue = new JLabel(
            String.format("₱%,.2f", compute.averageSales)
        );

        summary.add(createMetricCard(
            "Total Revenue",
            revenueValue,
            "Revenue from sales",
            CYAN
        ));

        summary.add(createMetricCard(
            "Completed Orders",
            ordersValue,
            "Successful transactions",
            WHITE
        ));

        summary.add(createMetricCard(
            "Products Sold",
            productsValue,
            "Total units sold",
            WHITE
        ));

        summary.add(createMetricCard(
            "Average Order",
            averageValue,
            "Revenue per order",
            YELLOW
        ));

        main.add(summary);
        main.add(Box.createVerticalStrut(18));



        categoryPanel = createPanel("Sales by Category");

        categoryPanel.add(
            new CategoryChart(),
            BorderLayout.CENTER
        );

        categoryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        categoryPanel.setPreferredSize(
            new Dimension(600, 250)
        );

        main.add(categoryPanel);
        main.add(Box.createVerticalStrut(18));

        // =====================================
        // BOTTOM PANELS
        // =====================================

        bottom = new JPanel(new GridLayout(1, 2, 15, 15));
        bottom.setOpaque(false);
        bottom.setAlignmentX(Component.LEFT_ALIGNMENT);

        // TOP-SELLING PRODUCTS
        JPanel topProductsPanel =
            createPanel("Top-Selling Products");

        JPanel productList = new JPanel();
        productList.setOpaque(false);
        productList.setLayout(
            new BoxLayout(productList, BoxLayout.Y_AXIS)
        );

        boolean hasTopProducts = false;

        // CHANGED: DISPLAY ONLY THE TOP 3 PRODUCTS
        for (int i = 0; i < Math.min(3, compute.topfour.length); i++) {

            String productName = compute.topfourNames[i];
            int quantity = compute.topfour[i];

            if (productName != null && quantity > 0) {

                hasTopProducts = true;

                productList.add(
                    createProductBar(
                        productName,
                        quantity,
                        quantity + " units"
                    )
                );

                productList.add(
                    Box.createVerticalStrut(13)
                );
            }
        }

        if (!hasTopProducts) {

            JLabel noSales = new JLabel(
                "No completed product sales yet."
            );

            noSales.setForeground(GRAY);
            noSales.setFont(
                new Font("Arial", Font.PLAIN, 12)
            );

            productList.add(noSales);
        }

        topProductsPanel.add(
            productList,
            BorderLayout.CENTER
        );

        // ORDER STATISTICS
        JPanel orderPanel = createPanel("Order Statistics");

        JPanel orderList = new JPanel();
        orderList.setOpaque(false);
        orderList.setLayout(
            new BoxLayout(orderList, BoxLayout.Y_AXIS)
        );

        orderList.add(
            createStatusRow(
                "Completed Orders",
                String.valueOf(compute.completedOrders),
                GREEN
            )
        );

        orderList.add(Box.createVerticalStrut(18));

        orderList.add(
            createStatusRow(
                "Canceled Orders",
                String.valueOf(compute.canceledOrders),
                RED
            )
        );

        orderPanel.add(orderList, BorderLayout.CENTER);

        bottom.add(topProductsPanel);
        bottom.add(orderPanel);

        main.add(bottom);



        scroll = new JScrollPane(main);

        scroll.setBorder(null);
        scroll.setBackground(BACKGROUND);
        scroll.getViewport().setBackground(BACKGROUND);

        scroll.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getVerticalScrollBar().setPreferredSize(
            new Dimension(7, 0)
        );

        scroll.getVerticalScrollBar().setUI(
            new javax.swing.plaf.basic.BasicScrollBarUI() {

                @Override
                protected void configureScrollBarColors() {
                    thumbColor = CYAN;
                    trackColor = BACKGROUND;
                }

                @Override
                protected JButton createDecreaseButton(
                    int orientation
                ) {
                    return zeroButton();
                }

                @Override
                protected JButton createIncreaseButton(
                    int orientation
                ) {
                    return zeroButton();
                }

                private JButton zeroButton() {
                    JButton button = new JButton();
                    button.setPreferredSize(
                        new Dimension(0, 0)
                    );
                    return button;
                }
            }
        );

        // RESPOND TO WINDOW RESIZING
        scroll.getViewport().addComponentListener(
            new ComponentAdapter() {

                @Override
                public void componentResized(ComponentEvent e) {
                    updateResponsiveLayout();
                }
            }
        );

        add(scroll, BorderLayout.CENTER);

        // INITIAL LAYOUT
        SwingUtilities.invokeLater(
            this::updateResponsiveLayout
        );
    }

    // =====================================
    // RESPONSIVE LAYOUT
    // =====================================

    private void updateResponsiveLayout() {

        if (scroll == null || main == null) {
            return;
        }

        int viewportWidth =
            scroll.getViewport().getWidth();

        if (viewportWidth <= 0) {
            return;
        }

        if (viewportWidth == lastWidth) {
            return;
        }

        lastWidth = viewportWidth;

        int contentWidth = Math.max(120, viewportWidth - 32);

        // SUMMARY CARDS:
        // Wide window: 4 columns
        // Medium window: 2 columns
        // Narrow window: 1 column

        int summaryColumns;

        if (contentWidth >= 1000) {
            summaryColumns = 4;
        } else if (contentWidth >= 560) {
            summaryColumns = 2;
        } else {
            summaryColumns = 1;
        }

        summary.setLayout(
            new GridLayout(0, summaryColumns, 12, 12)
        );

        int summaryRows =
            (int) Math.ceil(4.0 / summaryColumns);

        int summaryHeight =
            summaryRows * 105 + (summaryRows - 1) * 12;

        summary.setPreferredSize(
            new Dimension(contentWidth, summaryHeight)
        );

        summary.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, summaryHeight)
        );

        boolean stackPanels = contentWidth < 700;

        int bottomColumns = stackPanels ? 1 : 2;
        int bottomRows = stackPanels ? 2 : 1;

        bottom.setLayout(
            new GridLayout(
                bottomRows,
                bottomColumns,
                15,
                15
            )
        );

        int bottomHeight =
            bottomRows * 210 + (bottomRows - 1) * 15;

        bottom.setPreferredSize(
            new Dimension(contentWidth, bottomHeight)
        );

        bottom.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, bottomHeight)
        );

        // Keep the content as wide as the visible viewport.
        main.setPreferredSize(
            new Dimension(
                contentWidth,
                main.getPreferredSize().height
            )
        );

        main.revalidate();
        main.repaint();
    }

    // =====================================
    // SUMMARY CARD
    // =====================================

    private JPanel createMetricCard(
        String name,
        JLabel value,
        String description,
        Color accent
    ) {

        JPanel card = new JPanel();

        card.setLayout(
            new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        card.setBackground(PANEL);

        card.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(12, 12, 10, 10)
            )
        );

        card.setMinimumSize(new Dimension(0, 95));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setForeground(WHITE);
        nameLabel.setFont(
            new Font("Arial", Font.BOLD, 11)
        );
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        value.setForeground(accent);
        value.setFont(
            new Font("Arial", Font.BOLD, 20)
        );
        value.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel desc = new JLabel(description);
        desc.setForeground(GRAY);
        desc.setFont(
            new Font("Arial", Font.PLAIN, 10)
        );
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(nameLabel);
        card.add(Box.createVerticalStrut(9));
        card.add(value);
        card.add(Box.createVerticalStrut(5));
        card.add(desc);

        return card;
    }

    // =====================================
    // GENERAL PANEL
    // =====================================

    private JPanel createPanel(String title) {

        JPanel panel = new JPanel(
            new BorderLayout(0, 12)
        );

        panel.setBackground(PANEL);

        panel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(14, 14, 14, 14)
            )
        );

        JLabel heading = new JLabel(title);
        heading.setForeground(WHITE);
        heading.setFont(
            new Font("Arial", Font.BOLD, 15)
        );

        panel.add(heading, BorderLayout.NORTH);

        return panel;
    }

    // =====================================
    // TOP-SELLING PRODUCT BAR
    // =====================================

    private JPanel createProductBar(
        String name,
        int quantityValue,
        String quantity
    ) {

        JPanel row = new JPanel(
            new BorderLayout(8, 5)
        );

        row.setOpaque(false);

        JLabel productName = new JLabel(name);
        productName.setForeground(WHITE);
        productName.setFont(
            new Font("Arial", Font.PLAIN, 11)
        );

        JLabel units = new JLabel(quantity);
        units.setForeground(GRAY);
        units.setFont(
            new Font("Arial", Font.PLAIN, 10)
        );

        JPanel labels = new JPanel(new BorderLayout(5, 0));
        labels.setOpaque(false);

        labels.add(productName, BorderLayout.CENTER);
        labels.add(units, BorderLayout.EAST);

        int maxQuantity = 1;

        for (int value : compute.topfour) {
            maxQuantity = Math.max(maxQuantity, value);
        }

        int barValue = (int) Math.round(
            quantityValue * 100.0 / maxQuantity
        );

        JProgressBar bar = new JProgressBar(0, 100);

        bar.setValue(barValue);
        bar.setStringPainted(false);
        bar.setForeground(CYAN);
        bar.setBackground(BACKGROUND);
        bar.setBorderPainted(false);
        bar.setPreferredSize(new Dimension(100, 7));

        row.add(labels, BorderLayout.NORTH);
        row.add(bar, BorderLayout.CENTER);

        return row;
    }

    // =====================================
    // ORDER STATISTICS ROW
    // =====================================

    private JPanel createStatusRow(
        String name,
        String count,
        Color color
    ) {

        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setForeground(WHITE);
        nameLabel.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        JLabel countLabel = new JLabel(count);
        countLabel.setForeground(color);
        countLabel.setFont(
            new Font("Arial", Font.BOLD, 16)
        );

        row.add(nameLabel, BorderLayout.CENTER);
        row.add(countLabel, BorderLayout.EAST);

        return row;
    }

    // =====================================
    // RESPONSIVE DONUT CHART
    // =====================================

    private class CategoryChart extends JPanel {

        private final int[] amounts = compute.finalProductSales;
        private final String[] names = compute.productNames;

        private final Color[] colors = {
            CYAN,
            PURPLE,
            GREEN,
            YELLOW,
            PINK,
            RED,
            new Color(0xF59E0B),
            new Color(0x10B981),
            new Color(0x3B82F6),
            new Color(0x8B5CF6),
            new Color(0xEC4899)
        };

        CategoryChart() {
            setOpaque(false);
            setPreferredSize(new Dimension(400, 220));
            setMinimumSize(new Dimension(100, 180));
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            int width = getWidth();
            int height = getHeight();

            if (width <= 0 || height <= 0) {
                g2.dispose();
                return;
            }

            long total = 0;

            for (int amount : amounts) {
                if (amount > 0) {
                    total += amount;
                }
            }

            // Responsive donut size
            int diameter = Math.min(
                160,
                Math.min(height - 20, Math.max(70, width / 3))
            );

            diameter = Math.max(50, diameter);

            int x = 8;
            int y = Math.max(0, (height - diameter) / 2);

            // Draw donut slices
            int start = 90;
            int usedAngle = 0;

            if (total > 0) {

                for (int i = 0; i < amounts.length; i++) {

                    if (amounts[i] <= 0) {
                        continue;
                    }

                    int angle;

                    if (i == amounts.length - 1) {
                        angle = 360 - usedAngle;
                    } else {
                        angle = (int) Math.round(
                            amounts[i] * 360.0 / total
                        );
                    }

                    angle = Math.min(angle, 360 - usedAngle);

                    if (angle > 0) {

                        g2.setColor(colors[i % colors.length]);

                        g2.fillArc(
                            x,
                            y,
                            diameter,
                            diameter,
                            start,
                            -angle
                        );

                        start -= angle;
                        usedAngle += angle;
                    }
                }
            } else {

                g2.setColor(BORDER);
                g2.fillOval(x, y, diameter, diameter);
            }

            // Donut hole
            int hole = diameter / 2;

            g2.setColor(PANEL);

            g2.fillOval(
                x + (diameter - hole) / 2,
                y + (diameter - hole) / 2,
                hole,
                hole
            );

            // LEGEND
            int legendX = x + diameter + 12;
            int legendY = Math.max(15, y + 5);

            g2.setFont(
                new Font("Arial", Font.PLAIN, 10)
            );

            FontMetrics metrics = g2.getFontMetrics();

            int availableWidth = Math.max(
                0,
                width - legendX - 35
            );

            for (int i = 0; i < names.length; i++) {

                if (i >= amounts.length) {
                    break;
                }

                int yy = legendY + i * 17;

                // Do not draw beyond the chart's visible area.
                if (yy > height - 5) {
                    break;
                }

                g2.setColor(colors[i % colors.length]);

                g2.fillRoundRect(
                    legendX,
                    yy - 7,
                    9,
                    9,
                    3,
                    3
                );

                g2.setColor(WHITE);

                String name = names[i] == null
                    ? "Unknown"
                    : names[i];

                // Shorten long names to fit the chart.
                String displayName = name;

                while (
                    !displayName.isEmpty()
                    && metrics.stringWidth(displayName) > availableWidth
                ) {
                    displayName = displayName.substring(
                        0,
                        displayName.length() - 1
                    );
                }

                if (!displayName.equals(name)
                    && !displayName.isEmpty()) {
                    displayName = displayName.substring(
                        0,
                        Math.max(0, displayName.length() - 1)
                    ) + "...";
                }

                g2.drawString(
                    displayName,
                    legendX + 15,
                    yy + 1
                );

                g2.setColor(GRAY);

                String percentage = total > 0
                    ? Math.round(amounts[i] * 100.0 / total) + "%"
                    : "0%";

                g2.drawString(
                    percentage,
                    Math.max(legendX + 15, width - 32),
                    yy + 1
                );
            }

            g2.dispose();
        }
    }
}

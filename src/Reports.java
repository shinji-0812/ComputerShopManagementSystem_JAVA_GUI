
import java.awt.*;
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

    public Reports(Database db) {

        this.db = db;
        this.compute = new Computes(db);

        // IMPORTANT: Calculate the data BEFORE displaying it.
        compute.compute();
        compute.topSellingProducts();

        setLayout(new BorderLayout());
        setBackground(BACKGROUND);

        // MAIN CONTENT
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(BACKGROUND);
        main.setBorder(new EmptyBorder(20, 20, 20, 20));

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 55)
        );

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(
            new BoxLayout(heading, BoxLayout.Y_AXIS)
        );

        JLabel title = new JLabel("Analytics Overview");
        title.setForeground(WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel subtitle = new JLabel(
            "Monitor your computer shop performance"
        );
        subtitle.setForeground(GRAY);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 12));

        heading.add(title);
        heading.add(Box.createVerticalStrut(5));
        heading.add(subtitle);
        header.add(heading, BorderLayout.WEST);

        main.add(header);
        main.add(Box.createVerticalStrut(20));

        // SUMMARY CARDS
        JPanel summary = new JPanel(
            new GridLayout(1, 4, 12, 0)
        );
        summary.setOpaque(false);
        summary.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 105)
        );

        revenueValue = new JLabel(
            "₱" + String.format("%.2f", compute.totalSales)
        );

        ordersValue = new JLabel(
            String.valueOf(compute.completedOrders)
        );

        productsValue = new JLabel(
            String.valueOf(compute.productsSold)
        );

        averageValue = new JLabel(
            "₱" + String.format("%.2f", compute.averageSales)
        );

        summary.add(createMetricCard(
            "Total Revenue", revenueValue,
            "Revenue from sales", CYAN
        ));

        summary.add(createMetricCard(
            "Completed Orders", ordersValue,
            "Successful transactions", GREEN
        ));

        summary.add(createMetricCard(
            "Products Sold", productsValue,
            "Total units sold", PURPLE
        ));

        summary.add(createMetricCard(
            "Average Order", averageValue,
            "Revenue per order", YELLOW
        ));

        main.add(summary);
        main.add(Box.createVerticalStrut(18));

        // SALES BY CATEGORY
        JPanel categoryPanel = createPanel("Sales by Category");

        categoryPanel.add(
            new CategoryChart(),
            BorderLayout.CENTER
        );

        categoryPanel.setPreferredSize(
            new Dimension(700, 250)
        );

        categoryPanel.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 250)
        );

        main.add(categoryPanel);
        main.add(Box.createVerticalStrut(18));

        // BOTTOM SECTION
        JPanel bottom = new JPanel(
            new GridLayout(1, 2, 15, 0)
        );

        bottom.setOpaque(false);
        bottom.setPreferredSize(
            new Dimension(700, 210)
        );

        bottom.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 210)
        );

        // TOP-SELLING PRODUCTS
        JPanel topProductsPanel = createPanel(
            "Top-Selling Products"
        );

        JPanel productList = new JPanel();
        productList.setOpaque(false);
        productList.setLayout(
            new BoxLayout(productList, BoxLayout.Y_AXIS)
        );

        boolean hasTopProducts = false;

        for (int i = 0; i < compute.topfour.length; i++) {

            String productName = compute.topfourNames[i];
            int quantity = compute.topfour[i];

            // Display only valid entries with sales.
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

        // SCROLL PANE
        JScrollPane scroll = new JScrollPane(main);

        scroll.getVerticalScrollBar().setPreferredSize(
            new Dimension(7, 0)
        );

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

        add(scroll, BorderLayout.CENTER);
    }

    // METRIC CARD
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

        JLabel nameLabel = new JLabel(name);
        nameLabel.setForeground(GRAY);
        nameLabel.setFont(
            new Font("Arial", Font.BOLD, 12)
        );

        value.setForeground(accent);
        value.setFont(
            new Font("Arial", Font.BOLD, 21)
        );

        JLabel desc = new JLabel(description);
        desc.setForeground(GRAY);
        desc.setFont(
            new Font("Arial", Font.PLAIN, 10)
        );

        card.add(nameLabel);
        card.add(Box.createVerticalStrut(9));
        card.add(value);
        card.add(Box.createVerticalStrut(5));
        card.add(desc);

        return card;
    }

    // STANDARD PANEL
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

    // TOP-SELLING PRODUCT BAR
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

        JPanel labels = new JPanel(new BorderLayout());
        labels.setOpaque(false);
        labels.add(productName, BorderLayout.WEST);
        labels.add(units, BorderLayout.EAST);

        // Scale bars relative to the largest displayed quantity.
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

    // ORDER STATUS
    private JPanel createStatusRow(
        String name,
        String count,
        Color color
    ) {

        JPanel row = new JPanel(new BorderLayout());
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

        row.add(nameLabel, BorderLayout.WEST);
        row.add(countLabel, BorderLayout.EAST);

        return row;
    }

    // CATEGORY DONUT CHART
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
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            int diameter = Math.min(
                160,
                Math.max(100, getHeight() - 10)
            );

            int x = 8;
            int y = (getHeight() - diameter) / 2;

            int start = 90;

            for (int i = 0; i < amounts.length; i++) {

                int angle = amounts[i] * 360 / 100;

                if (angle > 0) {
                    g2.setColor(colors[i]);

                    g2.fillArc(
                        x, y, diameter, diameter,
                        start, -angle
                    );
                }

                start -= angle;
            }

            // DONUT HOLE
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

            g2.setFont(new Font("Arial", Font.PLAIN, 10));

            for (int i = 0; i < names.length; i++) {

                int yy = legendY + i * 17;

                // Do not draw beyond the visible chart area.
                if (yy > getHeight() - 5) {
                    break;
                }

                g2.setColor(colors[i]);

                g2.fillRoundRect(
                    legendX, yy - 7, 9, 9, 3, 3
                );

                g2.setColor(WHITE);
                g2.drawString(names[i], legendX + 15, yy + 1);

                g2.setColor(GRAY);
                g2.drawString(
                    amounts[i] + "%",
                    getWidth() - 35,
                    yy + 1
                );
            }

            g2.dispose();
        }
    }
}

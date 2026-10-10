import java.sql.*;
import java.util.ArrayList;

public class Database {

    private Connection connection;
    private String username;
    private int userId;



    public Database() {
        try {
            connection = DriverManager.getConnection(
                "jdbc:sqlite:Database/ComputerShopManagement.db"
            );

            try (Statement statement = connection.createStatement()) {
                //statement.execute("PRAGMA busy_timeout = 5000");
                statement.execute("PRAGMA foreign_keys = ON");

                statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT NOT NULL UNIQUE, " +
                    "password TEXT NOT NULL, " +
                    "IsLogIn INTEGER NOT NULL DEFAULT 0)"
                );

                statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS products (" +
                    "product_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "category TEXT NOT NULL, " +
                    "price REAL NOT NULL, " +
                    "quantity INTEGER NOT NULL, " +
                    "user_id INTEGER NOT NULL, " +
                    "FOREIGN KEY(user_id) REFERENCES users(id))"
                );

                statement.executeUpdate(
                    """
                    CREATE TABLE IF NOT EXISTS salesHistory (
                        sale_id TEXT PRIMARY KEY,
                        customer_name TEXT NOT NULL,
                        product_name TEXT NOT NULL,
                        quantity_sold INTEGER NOT NULL,
                        total_price REAL NOT NULL,
                        payment_method TEXT NOT NULL,
                        sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        status TEXT NOT NULL,
                        user_id INTEGER NOT NULL,
                        FOREIGN KEY(user_id) REFERENCES users(id)
                    )
                    """
                );


            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Transaction> getSalesHistory() {
        ArrayList<Transaction> salesHistory = new ArrayList<>();

        String sql = "SELECT * FROM salesHistory WHERE user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, getUserId());
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String saleId = rs.getString("sale_id");
                    String customerName = rs.getString("customer_name");
                    String productName = rs.getString("product_name");
                    int quantitySold = rs.getInt("quantity_sold");
                    double totalPrice = rs.getDouble("total_price");
                    String paymentMethod = rs.getString("payment_method");
                    Timestamp saleDate = rs.getTimestamp("sale_date");
                    String status = rs.getString("status");


                salesHistory.add(new Transaction(
                    saleId,
                    customerName,
                    productName,
                    quantitySold,
                    totalPrice,
                    paymentMethod,
                    status,
                    saleDate
                ));

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salesHistory;
    }

    public double getTotalSales(){
        double totalSales = 0;

        String sql = "SELECT SUM(total_price) AS total_sales FROM salesHistory WHERE user_id = ? AND status = 'Completed'";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, getUserId());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    totalSales = rs.getDouble("total_sales");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (totalSales == 0) {
            return 0;
        }

        return totalSales;
    }


        public double getProductSales(String category) {

            double productSales = 0;

            String sql = """
                SELECT SUM(s.total_price) AS total_sales
                FROM salesHistory s
                JOIN products p
                    ON s.product_name = p.name
                    AND s.user_id = p.user_id
                WHERE s.user_id = ?
                AND p.category = ?
                AND s.status = 'Completed'
                """;

            try (PreparedStatement statement =
                    connection.prepareStatement(sql)) {

                statement.setInt(1, getUserId());
                statement.setString(2, category);

                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        productSales = rs.getDouble("total_sales");
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return productSales;
        }

    public int productSold(){
        int productsSold = 0;

        String sql = "SELECT SUM(quantity_sold) AS products_sold FROM salesHistory WHERE user_id = ? AND status = 'Completed'";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, getUserId());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    productsSold = rs.getInt("products_sold");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (productsSold == 0) {
            return 0;
        }

        return productsSold;
    }

    public int getTopSellingProducts(String status, String productName) {
        int ordersCount = 0;

        String sql = "SELECT SUM(quantity_sold) AS orders_count FROM salesHistory WHERE user_id = ? AND product_name = ? AND status = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, getUserId());
            statement.setString(2, productName);
            statement.setString(3, status);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    ordersCount = rs.getInt("orders_count");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (ordersCount == 0) {
            return 0;
        }

        return ordersCount;
    }
    
    public java.util.ArrayList<String> getCompletedProductNames() {

        java.util.ArrayList<String> names = new java.util.ArrayList<>();

        String sql = """
            SELECT DISTINCT product_name
            FROM salesHistory
            WHERE user_id = ?
            AND status = 'Completed'
            AND product_name IS NOT NULL
            AND TRIM(product_name) <> ''
            ORDER BY product_name
            """;

        try (PreparedStatement statement =
                connection.prepareStatement(sql)) {

            statement.setInt(1, getUserId());

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    names.add(rs.getString("product_name"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return names;
    }



    public int getOrdersCount(String status) {
        int completedOrders = 0;

        String sql = "SELECT COUNT(*) AS completed_orders FROM salesHistory WHERE user_id = ? AND status = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, getUserId());
            statement.setString(2, status);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    completedOrders = rs.getInt("completed_orders");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (completedOrders == 0) {
            return 0;
        }

        return completedOrders;
    }

    public boolean hasHistory() {

        String sql = "SELECT 1 FROM salesHistory LIMIT 1";

        try (PreparedStatement statement =
                connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery()) {

            return result.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cancelSale(String saleId) {
        String sql = "UPDATE salesHistory SET status = 'Canceled' WHERE sale_id = ? AND user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, saleId);
            statement.setInt(2, getUserId());

            int rows = statement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean stockIncrease(String name, int stock_available) {

        String sql = "UPDATE products SET quantity = ? WHERE name = ? AND user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, stock_available);
            statement.setString(2, name);
            statement.setInt(3, getUserId());

            // Execute the SQL update
            int rows = statement.executeUpdate();

            // Return true if a product was updated
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addSaleHistory(String saleId, String customerName, String productName,
                                  int quantitySold, double totalPrice,
                                  String paymentMethod, String status) {
        int userId = getUserId();

        String sql = """
            INSERT INTO salesHistory
            (sale_id, customer_name, product_name, quantity_sold,
             total_price, payment_method, status, user_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement statement =
                connection.prepareStatement(sql)) {

            statement.setString(1, saleId);
            statement.setString(2, customerName);
            statement.setString(3, productName);
            statement.setInt(4, quantitySold);
            statement.setDouble(5, totalPrice);
            statement.setString(6, paymentMethod);
            statement.setString(7, status);
            statement.setInt(8, userId);

            int rows = statement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addProduct(String name, String category,
                            double price, int quantity) {
        int userId = getUserId();

        String sql = """
            INSERT INTO products
            (name, category, price, quantity, user_id)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (PreparedStatement statement =
                connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, category);
            statement.setDouble(3, price);
            statement.setInt(4, quantity);
            statement.setInt(5, userId);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                return true;
            }

            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProduct(String name) {
        String sql = "DELETE FROM products WHERE name = ? AND user_id = ?";

        try (PreparedStatement statement =
                connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setInt(2, getUserId());
            int rows = statement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean updateProduct(String oldName, String name, String category,
                                double price, int quantity) {

        String sql = """
            UPDATE products
            SET name = ?, category = ?, price = ?, quantity = ?
            WHERE name = ? AND user_id = ?
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, category);
            statement.setDouble(3, price);
            statement.setInt(4, quantity);
            statement.setString(5, oldName);
            statement.setInt(6, getUserId());

            int rows = statement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public ArrayList<Product> getProductsByUserId() {
        ArrayList<Product> productList = new ArrayList<>();

        String sql = "SELECT name, category, price, quantity FROM products WHERE user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, getUserId());
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    String category = rs.getString("category");
                    double price = rs.getDouble("price");
                    int stock = rs.getInt("quantity");

                    productList.add(new Product(name, category, price, stock));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productList;
    }

    public int getUserId() {
        String sql = "SELECT id FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    

    public ArrayList<Product> getProducts() {
        ArrayList<Product> productList = new ArrayList<>();

        String sql = "SELECT name, category, price, quantity FROM products";

        try (PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                String category = rs.getString("category");
                double price = rs.getDouble("price");
                int stock = rs.getInt("quantity");

                productList.add(new Product(name, category, price, stock));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productList;
    }


    public boolean stockDecrease(String name, int stock_available) {

        String sql = "UPDATE products SET quantity = ? WHERE name = ? AND user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, stock_available);
            statement.setString(2, name);
            statement.setInt(3, getUserId());

            // Execute the SQL update
            int rows = statement.executeUpdate();

            // Return true if a product was updated
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




    public boolean createAccount(String username, String password) {

        String sql =
            "INSERT INTO users (username, password) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {

            return false;
        }
    }

    public boolean userExists(String username) {

        String sql =
            "SELECT id FROM users WHERE username = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }

        } catch (SQLException e) {

            return false;
        }
    }

    public boolean checkLogin(String username, String password) {

        String sql =
            "SELECT id FROM users WHERE username = ? AND password = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    this.userId = result.getInt("id");
                    this.username = username;
                    return true;
                }

                return false;
            }

        } catch (SQLException e) {

            return false;
        }
    }

    public String getusername() {
        return username;
    }

    public boolean setLogin(String username) {

        String sql =
            "UPDATE users SET IsLogIn = 1 WHERE username = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            int rows = statement.executeUpdate();



            return rows > 0;

        } catch (SQLException e) {

            return false;
        }
    }

    public boolean IsLogIn() {

        String sql =
            "SELECT username FROM users WHERE IsLogIn = 1";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {

                    this.username =
                        result.getString("username");

                    return true;
                }

                return false;
            }

        } catch (SQLException e) {

            return false;
        }
    }

    public boolean logout() {
        String sql = "UPDATE users SET IsLogIn = 0 WHERE IsLogIn = 1";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int rows = statement.executeUpdate();
            username = null;
            return rows > 0;
        } catch (SQLException e) {

            return false;
        }
    }
}
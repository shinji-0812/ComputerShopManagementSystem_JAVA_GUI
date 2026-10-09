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


            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addProduct(String name, String category,
                            double price, int quantity) {
        int userId = getUserId();
        if (connection == null) {
            System.out.println("ERROR: Database connection is null.");
            return false;
        }

        if (userId <= 0) {
            System.out.println("ERROR: Invalid userId: " + userId);
            System.out.println("Make sure the user is logged in.");
            return false;
        }

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
                System.out.println("Product added successfully!");
                return true;
            }

            return false;

        } catch (SQLException e) {
            System.out.println("Failed to add product!");
            e.printStackTrace();
            return false;
        }
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
import java.sql.*;

public class Database {

    private Connection connection;
    private String username;

    public Database() {
        try {
            connection = DriverManager.getConnection(
                "jdbc:sqlite:Database/ComputerShopManagement.db"
            );

            try (Statement statement = connection.createStatement()) {
                statement.execute("PRAGMA busy_timeout = 5000");

                statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT NOT NULL UNIQUE, " +
                    "password TEXT NOT NULL, " +
                    "IsLogIn INTEGER NOT NULL DEFAULT 0)"
                );
            }



        } catch (SQLException e) {

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

        String sql =
            "UPDATE users SET IsLogIn = 0 WHERE IsLogIn = 1";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            int rows = statement.executeUpdate();

            username = null;



            return rows > 0;

        } catch (SQLException e) {

            return false;
        }
    }
}

package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {

    private static final String URL =
            env("DB_URL", "jdbc:mysql://localhost:3306/ONLINE_SHOPPING_CART");

    private static final String USERNAME =
            env("DB_USER", "root");

    private static final String PASSWORD =
            env("DB_PASSWORD", "test");

    // Reads from environment variable first, then system property, then falls
    // back to the given default (keeps local/dev setup working unchanged).
    private static String env(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            value = System.getProperty(key);
        }
        return (value == null || value.isBlank()) ? defaultValue : value;
    }

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver Not Found", e);
        }
    }

    public static Connection getConnection() {

        try {
            return DriverManager.getConnection(
                    URL,
                    USERNAME,
                    PASSWORD
            );
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed To Connect Database",
                    e
            );
        }
    }
}
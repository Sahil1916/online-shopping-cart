
package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/ONLINE_SHOPPING_CART";

    private static final String USERNAME = "root";

    private static final String PASSWORD = "test";

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
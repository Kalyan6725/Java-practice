package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/practice";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "12345";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL,USERNAME,PASSWORD);
    }
    public static void closeConnection(Connection conn) throws SQLException {
        if (conn != null) {
                conn.close();
        }
    }
}

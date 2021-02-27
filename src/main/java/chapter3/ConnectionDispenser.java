package chapter3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ConnectionDispenser
 * <p/>
 * Using ThreadLocal to ensure thread confinement
 *
 * @author Brian Goetz and Tim Peierls
 */
public class ConnectionDispenser {
    static String DB_URL = "jdbc:mysql://localhost/mydatabase";

    private ThreadLocal<Connection> connectionHolder = new ThreadLocal<>() {
        @Override
        protected Connection initialValue() {
            try {
                return DriverManager.getConnection(DB_URL);
            } catch (SQLException ex) {
                throw new RuntimeException("Unable to acquire Connection, error=" + ex.getMessage());
            }
        }
    };

    public Connection getConnection() {
        return connectionHolder.get();
    }
}

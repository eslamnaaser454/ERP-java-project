package app.Classes;

import java.sql.*;
import java.util.*;

public class DataBaseConnection {

    private static final String SQLITE_DRIVER = "org.sqlite.JDBC";
    private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";

    private final String dbUrl;
    private final String driver;
    private final String user;
    private final String password;

    // Constructor for SQLite
    public DataBaseConnection(String dbPath) {
        this.dbUrl = "jdbc:sqlite:" + dbPath;
        this.driver = SQLITE_DRIVER;
        this.user = "";
        this.password = "";
    }

    // Constructor for MySQL
    public DataBaseConnection(String host, String dbName, String user, String password) {
        this.dbUrl = host + dbName;
        this.driver = MYSQL_DRIVER;
        this.user = user;
        this.password = password;
    }

    // Generic execute (INSERT/UPDATE/DELETE)
    public boolean execute(String query, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            setParameters(stmt, params);
            stmt.execute();
            return true;

        } catch (SQLException | ClassNotFoundException e) {
            logError("Execute failed", query, e);
            return false;
        }
    }

    // SELECT with parameters
    public List<Map<String, String>> select(String query, Object... params) {
        List<Map<String, String>> results = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            setParameters(stmt, params);
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();

            while (rs.next()) {
                Map<String, String> row = new HashMap<>();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    row.put(meta.getColumnName(i), rs.getString(i));
                }
                results.add(row);
            }

        } catch (SQLException | ClassNotFoundException e) {
            logError("Select failed", query, e);
        }
        return results;
    }

    // INSERT with generated keys
    public int insert(String query, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            setParameters(stmt, params);
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }

        } catch (SQLException | ClassNotFoundException e) {
            logError("Insert failed", query, e);
            return -1;
        }
    }

    // --- Helper Methods ---
    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        return user.isEmpty() ?
                DriverManager.getConnection(dbUrl) :
                DriverManager.getConnection(dbUrl, user, password);
    }

    private void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
    }

    private void logError(String action, String query, Exception e) {
        System.err.println(action + " | Query: " + query);
        System.err.println("Error: " + e.getMessage());
        e.printStackTrace();
    }
}
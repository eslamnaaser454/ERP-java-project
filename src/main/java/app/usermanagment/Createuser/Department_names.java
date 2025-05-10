package app.usermanagment.Createuser;
import app.Classes.DataBaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Department_names {
    private final String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";

    public List<String> getDepartmentNames() {
        List<String> departmentNames = new ArrayList<>();

        String query = "SELECT `name` FROM department";  // SQL query to get the 'name' column from the department table

        try (
                Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Iterate over the results
            while (rs.next()) {
                departmentNames.add(rs.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return departmentNames;
    }
}


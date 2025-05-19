package app.usermanagment.service;

import app.Classes.DataBaseConnection;
import app.usermanagment.Createuser.UserBuilder.User;
import app.usermanagment.Createuser.SecureAES;

import java.util.List;
import java.util.Map;

public class UserService {
    private final String dbPath;

    public UserService(String dbPath) {
        this.dbPath = dbPath;
    }

    public boolean addUser(User user) {
        String encryptedPassword = user.getPassword();
        if (user.isSuperUser()) {
            try {
                encryptedPassword = SecureAES.encrypt(user.getPassword());
            } catch (Exception e) {
                System.out.println("Error encrypting password: " + e.getMessage());
                return false;
            }
        }

        DataBaseConnection dataBaseConnections = new DataBaseConnection(dbPath);
        List<Map<String, String>> existingSSN = dataBaseConnections.select("SELECT * FROM users WHERE SSN ='" + user.getSsn() + "' ");
        if (!existingSSN.isEmpty()) {
            System.err.println("SSN already exists.");
            return false;
        }

        String query = "INSERT INTO users(username, password, phone, email, SSN, is_super_user, type, Department, is_active) " +
                "VALUES ('" + user.getUsername() + "', '" + encryptedPassword + "', '" + user.getPhone() + "', '" +
                user.getEmail() + "', '" + user.getSsn() + "', '" + user.isSuperUser() + "', '" +
                user.getType() + "', '" + user.getDepartment() + "', '" + user.isActive() + "')";

        DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);
        return dataBaseConnection.execute(query);
    }
}
package app.Login.Factory;

public class AdminUser implements AppUser {
    @Override
    public String processPassword(String password) {
        return password; // No encryption
    }
}


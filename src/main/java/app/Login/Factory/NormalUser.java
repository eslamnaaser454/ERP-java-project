package app.Login.Factory;

public class NormalUser implements AppUser {
    @Override
    public String processPassword(String password) {
        // Apply custom logic for normal users, or return as-is
        return password;
    }
}

package app.Login.Factory;

public class UserFactory {
    public static AppUser createUser(String type) {
        if (type.equalsIgnoreCase("HR")) {
            return new HRUser();
        } else {
            return new AdminUser(); // default
        }
    }
}


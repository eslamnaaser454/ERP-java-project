package app.Login.Factory;

public class UserFactory {
    public static AppUser createUser(String userType) {
        switch (userType.toLowerCase()) {
            case "hr":
                return new HRUser();
            case "admin":
                return new AdminUser();
            case "user":
                return new NormalUser();  // ✅ Add this case
            default:
                throw new IllegalArgumentException("Unknown user type: " + userType);
        }
    }
}

package app.usermanagment.Createuser.UserBuilder;

public class User {
    private String username;
    private String password;
    private String phone;
    private String email;
    private String ssn;
    private boolean isSuperUser;
    private String type;
    private String department;
    private boolean isActive;

    // Private constructor to enforce building through the builder
    private User() {}

    // Getters for all attributes (no setters to ensure immutability after build)
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getSsn() { return ssn; }
    public boolean isSuperUser() { return isSuperUser; }
    public String getType() { return type; }
    public String getDepartment() { return department; }
    public boolean isActive() { return isActive; }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + (password != null ? "*****" : null) + '\'' + // Mask password for display
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", ssn='" + ssn + '\'' +
                ", isSuperUser=" + isSuperUser +
                ", type='" + type + '\'' +
                ", department='" + department + '\'' +
                ", isActive=" + isActive +
                '}';
    }

    // Static inner class for the builder
    public static class UserBuilder {
        private String username;
        private String password;
        private String phone;
        private String email;
        private String ssn;
        private boolean isSuperUser;
        private String type;
        private String department;
        private boolean isActive = false; // Default value

        public UserBuilder(String username, String password, String phone, String email, String ssn) {
            this.username = username;
            this.password = password;
            this.phone = phone;
            this.email = email;
            this.ssn = ssn;
        }

        public UserBuilder isSuperUser(boolean isSuperUser) {
            this.isSuperUser = isSuperUser;
            return this;
        }

        public UserBuilder type(String type) {
            this.type = type;
            return this;
        }

        public UserBuilder department(String department) {
            this.department = department;
            return this;
        }

        public UserBuilder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public User build() {
            User user = new User();
            user.username = this.username;
            user.password = this.password;
            user.phone = this.phone;
            user.email = this.email;
            user.ssn = this.ssn;
            user.isSuperUser = this.isSuperUser;
            user.type = this.type;
            user.department = this.department;
            user.isActive = this.isActive;
            return user;
        }
    }
}
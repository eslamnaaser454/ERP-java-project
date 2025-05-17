package app.Login.Factory;

import app.usermanagment.Createuser.SecureAES;

public class HRUser implements AppUser {
    @Override
    public String processPassword(String password) {
        try {
            return SecureAES.encrypt(password);
        } catch (Exception e) {
            return password;
        }
    }
}


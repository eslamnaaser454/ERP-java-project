package app.usermanagment.Createuser;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;

public class SecureAES {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 128;

    private static final String SECRET_PASSWORD = "MyStrongPassword!";
    private static final String SALT = "StaticSaltValue1234";

    // Fixed IV (12 bytes for AES-GCM)
    private static final byte[] FIXED_IV = "1234567890ab".getBytes();  // must be 12 bytes

    // Generate fixed AES key
    public static SecretKey getFixedKey() throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(SECRET_PASSWORD.toCharArray(), SALT.getBytes(), ITERATIONS, KEY_LENGTH);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }

    // Deterministic encryption (fixed key + fixed IV)
    public static String encrypt(String plainText) throws Exception {
        SecretKey key = getFixedKey();

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, FIXED_IV);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

        byte[] encrypted = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // Decryption
    public static String decrypt(String encryptedText) throws Exception {
        SecretKey key = getFixedKey();

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, FIXED_IV);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        byte[] decoded = Base64.getDecoder().decode(encryptedText);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted);
    }
}

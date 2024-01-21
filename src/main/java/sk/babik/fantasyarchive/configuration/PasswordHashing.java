package sk.babik.fantasyarchive.configuration;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHashing {

//    public String generateHashPassword(String pPassword) {
//        String password = pPassword;
//        String salt = generateSalt();
//        String hashedPassword = hashPassword(password, salt);
//
//        // To verify a password, you would compare the stored hashed password with the newly hashed password.
//        boolean passwordMatch = checkPassword(password, salt, hashedPassword);
//        System.out.println("Password Match: " + passwordMatch);
//        return hashedPassword;
//    }

    public String generateSalt() {
        byte[] saltBytes = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    public String hashPassword(String password, String salt) {
        String saltedPassword = salt + password;
        try {
            System.out.println("SaltHashing: " + salt + ", passwordHashing: " + password);
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = messageDigest.digest(saltedPassword.getBytes());
            System.out.println("EncodedPassword: " + Base64.getEncoder().encodeToString(hashBytes));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password.", e);
        }
    }

    public boolean checkPassword(String inputPassword, String salt, String hashedPassword) {
        //String inputSaltedPassword = salt + inputPassword;
        String hashedInputPassword = hashPassword(inputPassword, salt);
        return hashedPassword.equals(hashedInputPassword);
    }
}

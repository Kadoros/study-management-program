package sec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {

    // Method to encrypt a string using SHA-256
    public static String encrSHA(String input) {
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");

            // Apply SHA-256 encryption to the input
            byte[] hashBytes = sha256.digest(input.getBytes());

            // Convert byte array to a string of hexadecimal values
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // In addition to the SHA algorithm, it would be nice to apply salt or other special security
    public static void main(String[] args) {
        String input = "admin";
        String encrypted = encrSHA(input);
        System.out.println("Input: " + input);
        System.out.println("SHA-256 Encrypted: " + encrypted);
    }
}

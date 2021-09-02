package pl.ftims.ias.your_climbing_gym.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class HashGenerator {

    private HashGenerator() {
    }

    public static String generateHash(String value) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(value, salt);
    }

    public static boolean checkPassword(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }

}

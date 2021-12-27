package pl.ftims.ias.perfectbeta.utils.security;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;

import java.nio.charset.StandardCharsets;


public class SymmetricCrypt {


    private static final String key = "7B66CB2371B02644841583934AC34B048C33E8188218812078461349140C5997";
    private static final String salt = "365F69DD9BBF0E6F";


    public static String encrypt(String plainText) {
        BytesEncryptor BE = Encryptors.standard(key, salt);
        return new String(Hex.encode(BE.encrypt(plainText.getBytes(StandardCharsets.UTF_8))));
    }

    public static String decrypt(String crypt) {
        BytesEncryptor BE = Encryptors.standard(key, salt);
        try {
            byte[] bytes = Hex.decode(crypt);
            return new String(BE.decrypt(bytes), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }
}


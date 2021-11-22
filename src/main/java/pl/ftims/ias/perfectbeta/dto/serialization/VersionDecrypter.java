package pl.ftims.ias.perfectbeta.dto.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import pl.ftims.ias.perfectbeta.utils.SymmetricCrypt;

import javax.json.bind.JsonbException;
import java.io.IOException;

public class VersionDecrypter extends JsonDeserializer<Long> {

    static final String INVALID_VERSION_MESSAGE = "Invalid version provided";

    @Override
    public Long deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        try {
            String decrypted = SymmetricCrypt.decrypt(p.getText().replaceAll("\"", ""));
            if (null == decrypted || decrypted.isBlank())
                return 0L;
            return Long.valueOf(decrypted);
        } catch (IllegalArgumentException ex) {
            throw new JsonbException(INVALID_VERSION_MESSAGE, ex);
        }
    }
}


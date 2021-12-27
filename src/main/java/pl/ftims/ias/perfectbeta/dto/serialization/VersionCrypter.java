package pl.ftims.ias.perfectbeta.dto.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import pl.ftims.ias.perfectbeta.utils.security.SymmetricCrypt;

import java.io.IOException;

public class VersionCrypter extends JsonSerializer<Long> {

    @Override
    public void serialize(Long value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(SymmetricCrypt.encrypt((String.valueOf(value))));
    }
}
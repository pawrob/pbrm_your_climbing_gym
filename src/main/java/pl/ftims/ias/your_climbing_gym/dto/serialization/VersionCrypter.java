package pl.ftims.ias.your_climbing_gym.dto.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import pl.ftims.ias.your_climbing_gym.utils.SymmetricCrypt;

import java.io.IOException;

public class VersionCrypter extends JsonSerializer<Long> {

    @Override
    public void serialize(Long value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(SymmetricCrypt.encrypt((String.valueOf(value))));
    }
}
package pl.ftims.ias.your_climbing_gym.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import pl.ftims.ias.your_climbing_gym.dto.serialization.VersionCrypter;
import pl.ftims.ias.your_climbing_gym.dto.serialization.VersionDecrypter;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public abstract class AbstractDTO {

    private long id;

    @JsonSerialize(using = VersionCrypter.class)
    @JsonDeserialize(using = VersionDecrypter.class)
    private Long version;
}

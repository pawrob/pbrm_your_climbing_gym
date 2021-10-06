package pl.ftims.ias.your_climbing_gym.dto;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public abstract class AbstractDTO {

    private long id;

    //    @JsonSerialize(using = VersionCrypter.class)
//    @JsonDeserialize(using = VersionDecrypter.class)
    private Long version;
}

package pl.ftims.ias.your_climbing_gym.dto.access_levels;

import lombok.*;
import pl.ftims.ias.your_climbing_gym.dto.AbstractDTO;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccessLevelDTO extends AbstractDTO {

    private String accessLevel;
    private Boolean isActive;

    public AccessLevelDTO(long id, Long version, String accessLevel, Boolean isActive) {
        super(id, version);
        this.accessLevel = accessLevel;
        this.isActive = isActive;

    }
}

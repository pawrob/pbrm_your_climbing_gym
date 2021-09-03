package pl.ftims.ias.your_climbing_gym.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.ftims.ias.your_climbing_gym.dto.validation.AccessLevel;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccessLevelDTO extends AbstractDTO {

    @AccessLevel
    private String accessLevel;
    private Boolean isActive;

    public AccessLevelDTO(long id, Long version, String accessLevel, Boolean isActive) {
        super(id, version);
        this.accessLevel = accessLevel;
        this.isActive = isActive;

    }
}

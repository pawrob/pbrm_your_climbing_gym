package pl.ftims.ias.your_climbing_gym.dto.access_levels;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdministratorDTO extends AccessLevelDTO{
    public AdministratorDTO(long id, Long version, String accessLevel, Boolean isActive) {
        super(id, version, accessLevel, isActive);
    }
}

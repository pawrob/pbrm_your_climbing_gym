package pl.ftims.ias.your_climbing_gym.dto.user_dtos;

import lombok.*;
import pl.ftims.ias.your_climbing_gym.dto.access_levels.AccessLevelDTO;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWithAccessLevelDTO extends UserDTO {


    @ToString.Exclude
    private List<AccessLevelDTO> accessLevels;

    public UserWithAccessLevelDTO(long id, Long version, String login, String email, Boolean isActive, Boolean isVerified, List<AccessLevelDTO> accessLevels) {
        super(id, version, login, email, isActive, isVerified);
        this.accessLevels = accessLevels;
    }
}

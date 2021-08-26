package pl.ftims.ias.your_climbing_gym.dto.user_dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.ftims.ias.your_climbing_gym.dto.PersonalDataDTO;
import pl.ftims.ias.your_climbing_gym.dto.access_levels.AccessLevelDTO;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UserWithPersonalDataAccessLevelDTO extends UserWithAccessLevelDTO {
    private PersonalDataDTO personalData;


    public UserWithPersonalDataAccessLevelDTO(long id, Long version, String login, String email, Boolean isActive, Boolean isVerified, List<AccessLevelDTO> accessLevels, PersonalDataDTO personalData) {
        super(id, version, login, email, isActive, isVerified, accessLevels);
        this.personalData = personalData;
    }
}

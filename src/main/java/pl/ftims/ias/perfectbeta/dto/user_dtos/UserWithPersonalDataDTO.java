package pl.ftims.ias.perfectbeta.dto.user_dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.ftims.ias.perfectbeta.dto.PersonalDataDTO;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UserWithPersonalDataDTO extends UserDTO {

    private PersonalDataDTO personalData;

    public UserWithPersonalDataDTO(long id, Long version, String login, String email, Boolean isActive, Boolean isVerified, PersonalDataDTO personalData) {
        super(id, version, login, email, isActive, isVerified);
        this.personalData = personalData;
    }
}

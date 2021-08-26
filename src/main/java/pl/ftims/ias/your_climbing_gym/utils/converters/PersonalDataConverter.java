package pl.ftims.ias.your_climbing_gym.utils.converters;

import lombok.NoArgsConstructor;
import pl.ftims.ias.your_climbing_gym.dto.PersonalDataDTO;
import pl.ftims.ias.your_climbing_gym.entities.PersonalDataEntity;

@NoArgsConstructor
public class PersonalDataConverter {

    public static PersonalDataEntity personalDataEntityFromDTO(PersonalDataDTO personalDataDTO, long userId) {
        return new PersonalDataEntity(userId, personalDataDTO.getName(),
                personalDataDTO.getSurname(), personalDataDTO.getPhoneNumber(),
                personalDataDTO.getLanguage(), personalDataDTO.getGender(),
                personalDataDTO.getVersion());
    }

    public static PersonalDataDTO personalDataDTOfromEntity(PersonalDataEntity personalDataEntity){
        return new PersonalDataDTO(personalDataEntity.getUserId(),personalDataEntity.getVersion(),personalDataEntity.getName(),
                personalDataEntity.getSurname(),personalDataEntity.getPhoneNumber(),personalDataEntity.getLanguage(),personalDataEntity.getGender());

    }
}

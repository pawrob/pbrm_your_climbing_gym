package pl.ftims.ias.perfectbeta.utils.converters;

import lombok.NoArgsConstructor;
import pl.ftims.ias.perfectbeta.dto.PersonalDataDTO;
import pl.ftims.ias.perfectbeta.entities.PersonalDataEntity;

@NoArgsConstructor
public class PersonalDataConverter {

    public static PersonalDataEntity personalDataEntityFromDTO(PersonalDataDTO personalDataDTO) {
        return new PersonalDataEntity(personalDataDTO.getId(), personalDataDTO.getVersion(), personalDataDTO.getName(),
                personalDataDTO.getSurname(), personalDataDTO.getPhoneNumber(),
                personalDataDTO.getLanguage(), personalDataDTO.getGender());
    }

    public static PersonalDataDTO personalDataDTOfromEntity(PersonalDataEntity personalDataEntity) {
        return new PersonalDataDTO(personalDataEntity.getId(), personalDataEntity.getVersion(), personalDataEntity.getName(),
                personalDataEntity.getSurname(), personalDataEntity.getPhoneNumber(), personalDataEntity.getLanguage(), personalDataEntity.getGender());

    }
}

package pl.ftims.ias.your_climbing_gym.utils.converters;

import lombok.NoArgsConstructor;
import pl.ftims.ias.your_climbing_gym.dto.access_levels.AccessLevelDTO;
import pl.ftims.ias.your_climbing_gym.entities.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@NoArgsConstructor
public class AccessLevelConverter {

    public static AccessLevelDTO AccessLevelDTOFromEntity(AccessLevelEntity accessLevelEntity) {
        return new AccessLevelDTO(accessLevelEntity.getId(), accessLevelEntity.getVersion(),
                accessLevelEntity.getAccessLevel(), accessLevelEntity.getActive());
    }

    public static List<AccessLevelDTO> AccessLevelDTOListFromEntities(Collection<AccessLevelEntity> accessLevelEntities) {
        return null == accessLevelEntities ? null : accessLevelEntities.stream()
                .filter(Objects::nonNull)
                .map(AccessLevelConverter::AccessLevelDTOFromEntity)
                .collect(Collectors.toList());
    }


    public static AccessLevelEntity AccessLevelEntityFromDTO(AccessLevelDTO accessLevelDTO, UserEntity userEntity) {
        AccessLevelEntity result;
        switch (accessLevelDTO.getAccessLevel().toUpperCase()) {
            case "CLIMBER":
                result = new ClimberEntity(accessLevelDTO.getIsActive(), userEntity);
                break;
            case "MANAGER":
                result = new ManagerEntity(accessLevelDTO.getIsActive(), userEntity);
                break;
            case "ADMINISTRATOR":
                result = new AdministratorEntity(accessLevelDTO.getIsActive(), userEntity);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + accessLevelDTO.getAccessLevel().toUpperCase());
        }

        return result;
    }


    public static List<AccessLevelEntity> accessLevelEntityListFromDTOs(Collection<AccessLevelDTO> accessLevelDTOS, UserEntity userEntity) {
        return null == accessLevelDTOS ? null : accessLevelDTOS.stream()
                .filter(Objects::nonNull)
                .map(accessLevelDTO -> AccessLevelEntityFromDTO(accessLevelDTO, userEntity))
                .collect(Collectors.toList());
    }
}

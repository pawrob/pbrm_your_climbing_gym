package pl.ftims.ias.your_climbing_gym.utils.converters;

import lombok.NoArgsConstructor;
import pl.ftims.ias.your_climbing_gym.dto.user_dtos.*;
import pl.ftims.ias.your_climbing_gym.entities.UserEntity;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@NoArgsConstructor
public class UserConverter {

    public static UserDTO userEntityToDTO(UserEntity userEntity) {
        return new UserDTO(userEntity.getId(), userEntity.getVersion(),
                userEntity.getLogin(), userEntity.getEmail(),
                userEntity.getActive(), userEntity.getVerified());
    }

    public static UserEntity createNewUserEntityFromDTO(RegistrationDTO userDTO) {
        return new UserEntity(userDTO.getLogin(), userDTO.getEmail(), userDTO.getPassword());
    }

//    public static UserEntity userWithPersonalDataDTOtoEntity(UserWithPersonalDataDTO userDTO, String password) {
//        UserEntity userEntity = createNewUserEntityFromDTO(userDTO, password);
//        userEntity.setActive(userDTO.getIsActive());
//        userEntity.setVerified(userDTO.getIsVerified());
//        if (null != userDTO.getPersonalData()) {
//            userEntity.setPersonalData(
//                    PersonalDataConverter.personalDataEntityFromDTO(
//                            userDTO.getPersonalData(), userDTO.getId()
//                    ));
//        }
//        return userEntity;
//    }

    public static List<UserDTO> createUserListDTOFromEntity(Collection<UserEntity> userEntities) {
        return null == userEntities ? null : userEntities.stream()
                .filter(Objects::nonNull)
                .map(UserConverter::userEntityToDTO)
                .collect(Collectors.toList());
    }

    public static List<UserWithPersonalDataAccessLevelDTO> createUserWithPersonalDataAccessLevelDTOListFromEntity(Collection<UserEntity> userEntities) {
        return null == userEntities ? null : userEntities.stream()
                .filter(Objects::nonNull)
                .map(UserConverter::userWithPersonalDataAccessLevelDTOFromEntity)
                .collect(Collectors.toList());
    }


    public static UserWithAccessLevelDTO userWithAccessLevelDTOFromEntity(UserEntity userEntity) {
        return new UserWithAccessLevelDTO(userEntity.getId(), userEntity.getVersion(), userEntity.getLogin(),
                userEntity.getEmail(), userEntity.getActive(), userEntity.getVerified(),
                AccessLevelConverter.AccessLevelDTOListFromEntities(userEntity.getAccessLevels()));
    }


    public static List<UserWithAccessLevelDTO> userWithAccessLevelDTOListFromEntities(Collection<UserEntity> userEntities) {
        return null == userEntities ? null : userEntities.stream()
                .filter(Objects::nonNull)
                .map(UserConverter::userWithAccessLevelDTOFromEntity)
                .collect(Collectors.toList());
    }

    public static UserWithPersonalDataDTO userWithPersonalDataDTOFromEntity(UserEntity userEntity) {
        return new UserWithPersonalDataDTO(userEntity.getId(), userEntity.getVersion(), userEntity.getLogin(),
                userEntity.getEmail(), userEntity.getActive(), userEntity.getVerified(),
                PersonalDataConverter.personalDataDTOfromEntity(userEntity.getPersonalData()));
    }

    public static UserWithPersonalDataAccessLevelDTO userWithPersonalDataAccessLevelDTOFromEntity(UserEntity userEntity) {
        return new UserWithPersonalDataAccessLevelDTO(userEntity.getId(), userEntity.getVersion(), userEntity.getLogin(),
                userEntity.getEmail(), userEntity.getActive(), userEntity.getVerified(),
                AccessLevelConverter.AccessLevelDTOListFromEntities(userEntity.getAccessLevels()),
                PersonalDataConverter.personalDataDTOfromEntity(userEntity.getPersonalData()));

    }
}

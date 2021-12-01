package pl.ftims.ias.perfectbeta.utils.converters;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.userdetails.User;
import pl.ftims.ias.perfectbeta.dto.routes_dtos.RouteDTO;
import pl.ftims.ias.perfectbeta.dto.user_dtos.*;
import pl.ftims.ias.perfectbeta.entities.RouteEntity;
import pl.ftims.ias.perfectbeta.entities.UserEntity;

import java.util.ArrayList;
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
    public static Page<UserWithPersonalDataAccessLevelDTO> userEntityPageToDTOPage(Page<UserEntity> entity) {

        List<UserEntity> entities = entity.getContent();

        List<UserWithPersonalDataAccessLevelDTO> dtos = new ArrayList<>();
        for (UserEntity e : entities){
            dtos.add(userWithPersonalDataAccessLevelDTOFromEntity(e));
        }
        Page<UserWithPersonalDataAccessLevelDTO> page = new PageImpl<UserWithPersonalDataAccessLevelDTO>(dtos,entity.getPageable(),dtos.size());
        return page;
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

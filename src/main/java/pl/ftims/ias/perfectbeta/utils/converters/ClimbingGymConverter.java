package pl.ftims.ias.perfectbeta.utils.converters;

import pl.ftims.ias.perfectbeta.dto.routes_dtos.*;
import pl.ftims.ias.perfectbeta.entities.ClimbingGymEntity;
import pl.ftims.ias.perfectbeta.entities.GymDetailsEntity;
import pl.ftims.ias.perfectbeta.entities.GymMaintainerEntity;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ClimbingGymConverter {

    public static ClimbingGymDTO climbingGymEntityToDTO(ClimbingGymEntity entity) {
        return new ClimbingGymDTO(entity.getId(), entity.getVersion(), entity.getOwner().getId(), entity.getGymName(), entity.getStatus());
    }

    public static ClimbingGymWithDetailsDTO climbingGymWithDetailsEntityToDTO(ClimbingGymEntity entity) {
        return new ClimbingGymWithDetailsDTO(entity.getId(), entity.getVersion(), entity.getOwner().getId(), entity.getGymName(), entity.getStatus(), GymDetailsEntityToDTO(entity.getGymDetails()));
    }

    public static GymDetailsDTO GymDetailsEntityToDTO(GymDetailsEntity entity) {
        return new GymDetailsDTO(entity.getId(), entity.getVersion(), entity.getCountry(), entity.getCity(), entity.getStreet(), entity.getNumber(), entity.getDescription());
    }

    public static List<ClimbingGymDTO> createGymListDTOFromEntity(List<ClimbingGymEntity> gymEntities) {
        return null == gymEntities ? null : gymEntities.stream()
                .filter(Objects::nonNull)
                .map(ClimbingGymConverter::climbingGymEntityToDTO)
                .collect(Collectors.toList());
    }

    public static ClimbingGymWithMaintainersDTO climbingGymEntityWithMaintainersEntityToDTO(ClimbingGymEntity entity) {
        return new ClimbingGymWithMaintainersDTO(entity.getId(), entity.getVersion(), entity.getOwner().getId(), entity.getGymName(), entity.getStatus(), gymMaintainerDTOListFromEntities(entity.getMaintainers()));
    }

    public static GymMaintainerDTO gymMaintainerDTOFromEntity(GymMaintainerEntity entity) {
        return new GymMaintainerDTO(entity.getId(), entity.getVersion(), entity.getUser().getId(), entity.getMaintainedGym().getId(), entity.getActive());
    }

    public static List<GymMaintainerDTO> gymMaintainerDTOListFromEntities(Collection<GymMaintainerEntity> entities) {
        return null == entities ? null : entities.stream()
                .filter(Objects::nonNull)
                .map(ClimbingGymConverter::gymMaintainerDTOFromEntity)
                .collect(Collectors.toList());
    }
}

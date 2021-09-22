package pl.ftims.ias.your_climbing_gym.utils.converters;

import pl.ftims.ias.your_climbing_gym.dto.routes_dtos.ClimbingGymDTO;
import pl.ftims.ias.your_climbing_gym.dto.routes_dtos.ClimbingGymWithDetailsDTO;
import pl.ftims.ias.your_climbing_gym.dto.routes_dtos.GymDetailsDTO;
import pl.ftims.ias.your_climbing_gym.entities.ClimbingGymEntity;
import pl.ftims.ias.your_climbing_gym.entities.GymDetailsEntity;

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

}

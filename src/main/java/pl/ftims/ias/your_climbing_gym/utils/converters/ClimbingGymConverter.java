package pl.ftims.ias.your_climbing_gym.utils.converters;

import pl.ftims.ias.your_climbing_gym.dto.ClimbingGymDTO;
import pl.ftims.ias.your_climbing_gym.entities.ClimbingGymEntity;

public class ClimbingGymConverter {

    public static ClimbingGymDTO climbingGymEntityToDTO(ClimbingGymEntity entity) {
        return new ClimbingGymDTO(entity.getId(), entity.getVersion(), entity.getOwner().getId(), entity.getGymName());
    }
}

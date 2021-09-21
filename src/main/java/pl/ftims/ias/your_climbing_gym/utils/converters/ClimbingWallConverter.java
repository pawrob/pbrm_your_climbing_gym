package pl.ftims.ias.your_climbing_gym.utils.converters;

import pl.ftims.ias.your_climbing_gym.dto.ClimbingWallDTO;
import pl.ftims.ias.your_climbing_gym.entities.ClimbingWallEntity;

public class ClimbingWallConverter {

    public static ClimbingWallDTO climbingWallEntityToDTO(ClimbingWallEntity entity){
        return new ClimbingWallDTO(entity.getId(), entity.getVersion(), entity.getBoulderName(), entity.getDifficulty(), entity.getClimbingGym().getId());
    }

}

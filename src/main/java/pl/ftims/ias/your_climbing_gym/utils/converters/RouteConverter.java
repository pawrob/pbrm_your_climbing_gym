package pl.ftims.ias.your_climbing_gym.utils.converters;

import pl.ftims.ias.your_climbing_gym.dto.routes_dtos.RouteDTO;
import pl.ftims.ias.your_climbing_gym.entities.RouteEntity;

public class RouteConverter {

    public static RouteDTO climbingWallEntityToDTO(RouteEntity entity) {
        return new RouteDTO(entity.getId(), entity.getVersion(), entity.getRouteName(), entity.getDifficulty(),entity.getPhotoWithBoxesLink(), entity.getPhotoWithNumbersLink(), entity.getHoldsDetails(), entity.getClimbingGym().getId());
    }

}

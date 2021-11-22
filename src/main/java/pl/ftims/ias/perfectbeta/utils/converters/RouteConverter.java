package pl.ftims.ias.perfectbeta.utils.converters;

import pl.ftims.ias.perfectbeta.dto.routes_dtos.RouteDTO;
import pl.ftims.ias.perfectbeta.entities.RouteEntity;

public class RouteConverter {

    public static RouteDTO climbingWallEntityToDTO(RouteEntity entity) {
        return new RouteDTO(entity.getId(), entity.getVersion(), entity.getRouteName(), entity.getDifficulty(), entity.getPhotoWithBoxesLink(), entity.getPhotoWithNumbersLink(), entity.getHoldsDetails(), entity.getClimbingGym().getId());
    }

}

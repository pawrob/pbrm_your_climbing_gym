package pl.ftims.ias.perfectbeta.utils.converters;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import pl.ftims.ias.perfectbeta.dto.routes_dtos.RouteDTO;
import pl.ftims.ias.perfectbeta.entities.RouteEntity;

import java.util.ArrayList;
import java.util.List;

public class RouteConverter {

    public static RouteDTO climbingWallEntityToDTO(RouteEntity entity) {
        return new RouteDTO(entity.getId(), entity.getVersion(), entity.getRouteName(), entity.getDifficulty(),
                entity.getPhotoWithBoxesLink(), entity.getPhotoWithNumbersLink(), entity.getHoldsDetails(),
                entity.getClimbingGym().getId());
    }

    public static Page<RouteDTO> climbingWallEntityPageToDTOPage(Page<RouteEntity> entity) {

        List<RouteEntity> entities = entity.getContent();

        List<RouteDTO> dtos = new ArrayList<>();
        for (RouteEntity e : entities){
            dtos.add(climbingWallEntityToDTO(e));
        }
        Page<RouteDTO> page = new PageImpl<RouteDTO>(dtos,entity.getPageable(),dtos.size());
        return page;
    }
}

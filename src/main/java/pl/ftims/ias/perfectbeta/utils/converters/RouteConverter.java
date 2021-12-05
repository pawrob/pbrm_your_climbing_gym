package pl.ftims.ias.perfectbeta.utils.converters;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import pl.ftims.ias.perfectbeta.dto.routes_dtos.PhotoDTO;
import pl.ftims.ias.perfectbeta.dto.routes_dtos.RouteDTO;
import pl.ftims.ias.perfectbeta.entities.PhotoEntity;
import pl.ftims.ias.perfectbeta.entities.RouteEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RouteConverter {

    public static RouteDTO climbingWallEntityToDTO(RouteEntity entity) {
        return new RouteDTO(entity.getId(), entity.getVersion(), entity.getRouteName(), entity.getDifficulty(), entity.getDescription(), entity.getHoldsDetails(),
                entity.getClimbingGym().getId(), photoListDTOFromEntity(entity.getPhotos()));
    }


    public static PhotoDTO photoEntityToDTO(PhotoEntity entity) {
        return new PhotoDTO(entity.getId(), entity.getVersion(), entity.getPhotoUrl(), entity.getRoute().getId());
    }


    public static List<PhotoDTO> photoListDTOFromEntity(List<PhotoEntity> photoEntities) {

        return null == photoEntities ? null : photoEntities.stream()
                .filter(Objects::nonNull)
                .map(RouteConverter::photoEntityToDTO)
                .collect(Collectors.toList());


    }


    public static Page<RouteDTO> climbingWallEntityPageToDTOPage(Page<RouteEntity> entity) {

        List<RouteEntity> entities = entity.getContent();

        List<RouteDTO> dtos = new ArrayList<>();
        for (RouteEntity e : entities) {
            dtos.add(climbingWallEntityToDTO(e));
        }
        Page<RouteDTO> page = new PageImpl<RouteDTO>(dtos, entity.getPageable(), dtos.size());
        return page;
    }
}

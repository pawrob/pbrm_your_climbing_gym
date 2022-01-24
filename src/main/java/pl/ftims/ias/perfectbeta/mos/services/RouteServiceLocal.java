package pl.ftims.ias.perfectbeta.mos.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import pl.ftims.ias.perfectbeta.dto.routes_dtos.RatingDTO;
import pl.ftims.ias.perfectbeta.dto.routes_dtos.RouteDTO;
import pl.ftims.ias.perfectbeta.entities.RatingEntity;
import pl.ftims.ias.perfectbeta.entities.RouteEntity;
import pl.ftims.ias.perfectbeta.exceptions.AbstractAppException;

import java.util.List;

public interface RouteServiceLocal {

    Page<RouteEntity> findAllByGymId(Long gymId, Pageable page) throws AbstractAppException;

    RouteEntity addRoute(RouteDTO wallDTO) throws AbstractAppException;

    ResponseEntity removeRoute(Long gymId, Long routeId) throws AbstractAppException;

    RouteEntity editRouteDetails(Long gymId, Long routeId, RouteDTO routeDTO) throws AbstractAppException;

    RouteEntity addRouteToFavourites(Long id) throws AbstractAppException;

    Page<RouteEntity> getFavouriteRoutes(Pageable page) throws AbstractAppException;

    ResponseEntity deleteRouteFromFavourites(Long id) throws AbstractAppException;

    RouteEntity updateRating(Long route_id, RatingDTO ratingDTO) throws AbstractAppException;

    ResponseEntity deleteOwnRating(Long rating_id) throws AbstractAppException;

    ResponseEntity deleteRatingByOwnerOrMaintainer(Long rating_id) throws AbstractAppException;

    List<RatingEntity> getRatings(Long route_id) throws AbstractAppException;
}

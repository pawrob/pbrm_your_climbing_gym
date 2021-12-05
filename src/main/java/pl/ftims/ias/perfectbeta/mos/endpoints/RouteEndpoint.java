package pl.ftims.ias.perfectbeta.mos.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.ftims.ias.perfectbeta.dto.routes_dtos.RatingDTO;
import pl.ftims.ias.perfectbeta.dto.routes_dtos.RouteDTO;
import pl.ftims.ias.perfectbeta.exceptions.AbstractAppException;
import pl.ftims.ias.perfectbeta.mos.services.RouteService;
import pl.ftims.ias.perfectbeta.mos.services.RouteServiceLocal;
import pl.ftims.ias.perfectbeta.utils.converters.RouteConverter;

import javax.validation.Valid;

@RestController
@RequestMapping("route")
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.NEVER)
public class RouteEndpoint {

    RetryTemplate retry;
    RouteServiceLocal routeService;

    @Autowired
    public RouteEndpoint(RetryTemplate retry, RouteService routeService) {
        this.routeService = routeService;
        this.retry = retry;
    }


    @GetMapping("{gym_id}")
    public Page<RouteDTO> findAllRoutesByGymId(@PathVariable Long gym_id, Pageable page) throws AbstractAppException {
        return retry.execute(arg0 -> RouteConverter.climbingWallEntityPageToDTOPage(routeService.findAllByGymId(gym_id, page)));
    }


    @Secured("ROLE_MANAGER")
    @PostMapping("add")
    public RouteDTO addRoute(@RequestBody @Valid RouteDTO routeDTO) throws AbstractAppException {
        return retry.execute(arg0 -> RouteConverter.climbingWallEntityToDTO(routeService.addRoute(routeDTO)));
    }

    @Secured("ROLE_MANAGER")
    @DeleteMapping("{gym_id}/remove/{route_id}")
    public ResponseEntity addRoute(@PathVariable Long gym_id, @PathVariable Long route_id) throws AbstractAppException {
        return retry.execute(arg0 -> routeService.removeRoute(gym_id, route_id));
    }

    @Secured("ROLE_MANAGER")
    @PutMapping("{gym_id}/edit/{route_id}")
    public RouteDTO editRouteDetails(@PathVariable Long gym_id, @PathVariable Long route_id, @RequestBody @Valid RouteDTO routeDTO) throws AbstractAppException {
        return retry.execute(arg0 -> RouteConverter.climbingWallEntityToDTO(routeService.editRouteDetails(gym_id, route_id, routeDTO)));
    }

    @Secured("ROLE_CLIMBER")
    @GetMapping("favorites")
    public Page<RouteDTO> getFavouriteRoutes(Pageable page) throws AbstractAppException {
        return retry.execute(arg0 -> RouteConverter.climbingWallEntityPageToDTOPage(routeService.getFavouriteRoutes(page)));
    }

    @Secured("ROLE_CLIMBER")
    @PostMapping("/{route_id}/add-favorite")
    public RouteDTO addRouteToFavourites(@PathVariable Long route_id) throws AbstractAppException {
        return retry.execute(arg0 -> RouteConverter.climbingWallEntityToDTO(routeService.addRouteToFavourites(route_id)));
    }

    @Secured("ROLE_CLIMBER")
    @DeleteMapping("/{route_id}/remove-favorite")
    public ResponseEntity removeRouteFromFavourites(@PathVariable Long route_id) throws AbstractAppException {
        return retry.execute(arg0 -> routeService.deleteRouteFromFavourites(route_id));
    }

    @Secured("ROLE_CLIMBER")
    @PostMapping("/{route_id}/rate")
    public RouteDTO updateRating(@PathVariable Long route_id, @RequestBody @Valid RatingDTO ratingDTO) throws AbstractAppException {
        return retry.execute(arg0 -> RouteConverter.climbingWallEntityToDTO(routeService.updateRating(route_id, ratingDTO)));
    }

    @Secured("ROLE_CLIMBER")
    @DeleteMapping("/rate/{rate_id}/delete")
    public ResponseEntity deleteOwnRating(@PathVariable Long rate_id) throws AbstractAppException {
        return retry.execute(arg0 -> routeService.deleteOwnRating(rate_id));

    }

    @Secured("ROLE_MANAGER")
    @DeleteMapping("/rate/{rate_id}/force-delete")
    public ResponseEntity deleteRatingByOwnerOrMaintainer(@PathVariable Long rate_id) throws AbstractAppException {
        return retry.execute(arg0 -> routeService.deleteRatingByOwnerOrMaintainer(rate_id));
    }

}

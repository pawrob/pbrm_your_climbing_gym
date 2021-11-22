package pl.ftims.ias.perfectbeta.mos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ftims.ias.perfectbeta.dto.routes_dtos.RouteDTO;
import pl.ftims.ias.perfectbeta.entities.ClimbingGymEntity;
import pl.ftims.ias.perfectbeta.entities.GymMaintainerEntity;
import pl.ftims.ias.perfectbeta.entities.RouteEntity;
import pl.ftims.ias.perfectbeta.entities.enums.GymStatusEnum;
import pl.ftims.ias.perfectbeta.exceptions.AbstractAppException;
import pl.ftims.ias.perfectbeta.exceptions.GymNotFoundException;
import pl.ftims.ias.perfectbeta.exceptions.NotAllowedAppException;
import pl.ftims.ias.perfectbeta.exceptions.RouteNotFoundException;
import pl.ftims.ias.perfectbeta.mos.repositories.ClimbingGymRepository;
import pl.ftims.ias.perfectbeta.mos.repositories.RouteRepository;
import pl.ftims.ias.perfectbeta.mos.repositories.UserMosRepository;

@Service
@Transactional(transactionManager = "mosTransactionManager", isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
public class RouteService {

    RouteRepository routeRepository;
    UserMosRepository userMosRepository;
    ClimbingGymRepository climbingGymRepository;


    @Autowired
    public RouteService(RouteRepository routeRepository, ClimbingGymRepository climbingGymRepository, UserMosRepository userMosRepository) {
        this.routeRepository = routeRepository;
        this.climbingGymRepository = climbingGymRepository;
        this.userMosRepository = userMosRepository;
    }

    public RouteEntity addRoute(RouteDTO wallDTO) throws AbstractAppException {
        ClimbingGymEntity gym = climbingGymRepository.findById(wallDTO.getClimbingGymId())
                .orElseThrow(() -> GymNotFoundException.createGymWithProvidedIdNotFoundException(wallDTO.getClimbingGymId()));

        if (Boolean.FALSE.equals(checkIfPermitted(gym))) {
            throw NotAllowedAppException.createYouAreNotMaintainerOrOwnerException();
        }
        if (!gym.getStatus().equals(GymStatusEnum.VERIFIED)) {
            throw NotAllowedAppException.createGymNotVerifiedException();
        }

        RouteEntity route = new RouteEntity(wallDTO.getRouteName(), wallDTO.getPhotoWithBoxesLink(), wallDTO.getPhotoWithNumbersLink(), wallDTO.getHoldsDetails(), wallDTO.getDifficulty(), gym);
        routeRepository.save(route);
        return route;
    }

    public ResponseEntity removeRoute(Long gym_id, Long route_id) throws AbstractAppException {
        ClimbingGymEntity gym = climbingGymRepository.findById(gym_id)
                .orElseThrow(() -> GymNotFoundException.createGymWithProvidedIdNotFoundException(gym_id));

        if (Boolean.FALSE.equals(checkIfPermitted(gym))) {
            throw NotAllowedAppException.createYouAreNotMaintainerOrOwnerException();
        }
        if (!gym.getStatus().equals(GymStatusEnum.VERIFIED)) {
            throw NotAllowedAppException.createGymNotVerifiedException();
        }
        RouteEntity route = routeRepository.findById(route_id)
                .orElseThrow(() -> RouteNotFoundException.createRouteWithProvidedIdNotFoundException(route_id));

        routeRepository.delete(route);
        return ResponseEntity.ok().build();
    }


    private Boolean checkIfPermitted(ClimbingGymEntity gym) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (gym.getOwner().getLogin().equals(username)) {
            return Boolean.TRUE;
        } else {
            for (GymMaintainerEntity m : gym.getMaintainers()) {
                if (m.getUser().getLogin().equals(username)) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    public RouteEntity editRouteDetails(Long gym_id, Long route_id, RouteDTO routeDTO) throws AbstractAppException {
        ClimbingGymEntity gym = climbingGymRepository.findById(gym_id)
                .orElseThrow(() -> GymNotFoundException.createGymWithProvidedIdNotFoundException(gym_id));

        if (Boolean.FALSE.equals(checkIfPermitted(gym))) {
            throw NotAllowedAppException.createYouAreNotMaintainerOrOwnerException();
        }
        if (!gym.getStatus().equals(GymStatusEnum.VERIFIED)) {
            throw NotAllowedAppException.createGymNotVerifiedException();
        }

        RouteEntity route = routeRepository.findById(route_id)
                .orElseThrow(() -> RouteNotFoundException.createRouteWithProvidedIdNotFoundException(route_id));
        route.setRouteName(routeDTO.getRouteName());
        route.setDifficulty(routeDTO.getDifficulty());
        return route;
    }


}

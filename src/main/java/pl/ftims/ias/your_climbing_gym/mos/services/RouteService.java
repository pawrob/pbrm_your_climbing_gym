package pl.ftims.ias.your_climbing_gym.mos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ftims.ias.your_climbing_gym.dto.routes_dtos.RouteDTO;
import pl.ftims.ias.your_climbing_gym.entities.ClimbingGymEntity;
import pl.ftims.ias.your_climbing_gym.entities.RouteEntity;
import pl.ftims.ias.your_climbing_gym.exceptions.AbstractAppException;
import pl.ftims.ias.your_climbing_gym.exceptions.GymNotFoundException;
import pl.ftims.ias.your_climbing_gym.exceptions.NotAllowedAppException;
import pl.ftims.ias.your_climbing_gym.mos.repositories.ClimbingGymRepository;
import pl.ftims.ias.your_climbing_gym.mos.repositories.RouteRepository;

@Service
@Transactional(transactionManager = "mosTransactionManager", isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
public class RouteService {

    RouteRepository routeRepository;
    ClimbingGymRepository climbingGymRepository;

    @Autowired
    public RouteService(RouteRepository routeRepository, ClimbingGymRepository climbingGymRepository) {
        this.routeRepository = routeRepository;
        this.climbingGymRepository = climbingGymRepository;
    }

    public RouteEntity addRoute(RouteDTO wallDTO) throws AbstractAppException {
        ClimbingGymEntity gym = climbingGymRepository.findById(wallDTO.getClimbingGymId())
                .orElseThrow(() -> GymNotFoundException.createGymWithProvidedIdNotFoundException(wallDTO.getClimbingGymId()));

        if (!gym.getOwner().getLogin().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw NotAllowedAppException.createNotAllowedException();
        }

        RouteEntity route = new RouteEntity(wallDTO.getRouteName(), wallDTO.getDifficulty(), gym);
        routeRepository.save(route);
        return route;
    }
}

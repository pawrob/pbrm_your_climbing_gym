package pl.ftims.ias.your_climbing_gym.mos.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ftims.ias.your_climbing_gym.dto.routes_dtos.RouteDTO;
import pl.ftims.ias.your_climbing_gym.exceptions.AbstractAppException;
import pl.ftims.ias.your_climbing_gym.mos.services.RouteService;
import pl.ftims.ias.your_climbing_gym.utils.converters.RouteConverter;

import javax.validation.Valid;

@RestController
@RequestMapping("route")
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.NEVER)
public class RouteEndpoint {

    RetryTemplate retry;
    RouteService routeService;

    @Autowired
    public RouteEndpoint(RetryTemplate retry, RouteService routeService) {
        this.routeService = routeService;
        this.retry = retry;
    }

    @Secured("ROLE_MANAGER")
    @PostMapping("add")
    public RouteDTO addRoute(@RequestBody @Valid RouteDTO wallDTO) throws AbstractAppException {
        return retry.execute(arg0 -> RouteConverter.climbingWallEntityToDTO(routeService.addRoute(wallDTO)));
    }


}

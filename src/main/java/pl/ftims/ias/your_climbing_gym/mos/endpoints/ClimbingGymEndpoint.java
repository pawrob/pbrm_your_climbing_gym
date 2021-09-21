package pl.ftims.ias.your_climbing_gym.mos.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ftims.ias.your_climbing_gym.dto.ClimbingGymDTO;
import pl.ftims.ias.your_climbing_gym.mos.services.ClimbingGymService;
import pl.ftims.ias.your_climbing_gym.utils.converters.ClimbingGymConverter;


@RestController
@RequestMapping("climbing_gym")
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.NEVER)
public class ClimbingGymEndpoint {


    RetryTemplate retry;
    ClimbingGymService climbingGymService;

    @Autowired
    public ClimbingGymEndpoint(RetryTemplate retry, ClimbingGymService climbingGymService) {
        this.climbingGymService = climbingGymService;
        this.retry = retry;
    }


    @Secured("ROLE_MANAGER")
    @PostMapping("register/{gymName}")
    public ClimbingGymDTO registerClient(@PathVariable String gymName) {
        return retry.execute(arg0 -> ClimbingGymConverter.climbingGymEntityToDTO(climbingGymService.registerNewClimbingGym(gymName)));
    }
}

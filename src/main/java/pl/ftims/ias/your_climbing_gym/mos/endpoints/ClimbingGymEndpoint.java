package pl.ftims.ias.your_climbing_gym.mos.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.ftims.ias.your_climbing_gym.dto.routes_dtos.ClimbingGymDTO;
import pl.ftims.ias.your_climbing_gym.dto.routes_dtos.ClimbingGymWithDetailsDTO;
import pl.ftims.ias.your_climbing_gym.dto.routes_dtos.GymDetailsDTO;
import pl.ftims.ias.your_climbing_gym.exceptions.AbstractAppException;
import pl.ftims.ias.your_climbing_gym.mos.services.ClimbingGymService;
import pl.ftims.ias.your_climbing_gym.utils.converters.ClimbingGymConverter;

import javax.validation.Valid;


@RestController
@RequestMapping("gym")
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
    public ClimbingGymWithDetailsDTO registerClient(@PathVariable String gymName) {
        return retry.execute(arg0 -> ClimbingGymConverter.climbingGymWithDetailsEntityToDTO(climbingGymService.registerNewClimbingGym(gymName)));
    }

    @Secured("ROLE_ADMINISTRATOR")
    @PutMapping("verify/{id}")
    public ClimbingGymDTO verifyGym(@PathVariable Long id) throws AbstractAppException {
        return retry.execute(arg0 -> ClimbingGymConverter.climbingGymEntityToDTO(climbingGymService.verifyRoute(id)));
    }

    @Secured("ROLE_ADMINISTRATOR")
    @PutMapping("close/{id}")
    public ClimbingGymDTO closeGym(@PathVariable Long id) throws AbstractAppException {
        return retry.execute(arg0 -> ClimbingGymConverter.climbingGymEntityToDTO(climbingGymService.closeGym(id)));
    }

    @Secured("ROLE_MANAGER")
    @PutMapping("edit_gym_details/{id}")
    public ClimbingGymWithDetailsDTO editGymDetails(@PathVariable Long id, @RequestBody @Valid GymDetailsDTO detailsDTO) throws AbstractAppException {
        return retry.execute(arg0 -> ClimbingGymConverter.climbingGymWithDetailsEntityToDTO(climbingGymService.editGymDetails(id, detailsDTO)));
    }
}

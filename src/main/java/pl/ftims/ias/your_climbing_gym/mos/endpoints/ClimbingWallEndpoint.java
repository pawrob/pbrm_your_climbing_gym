package pl.ftims.ias.your_climbing_gym.mos.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.ftims.ias.your_climbing_gym.dto.ClimbingWallDTO;
import pl.ftims.ias.your_climbing_gym.exceptions.AbstractAppException;
import pl.ftims.ias.your_climbing_gym.exceptions.NotAllowedAppException;
import pl.ftims.ias.your_climbing_gym.mos.services.ClimbingWallService;
import pl.ftims.ias.your_climbing_gym.utils.converters.ClimbingWallConverter;

import javax.validation.Valid;

@RestController
@RequestMapping("climbing_wall")
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.NEVER)
public class ClimbingWallEndpoint {

    RetryTemplate retry;
    ClimbingWallService climbingWallService;

    @Autowired
    public ClimbingWallEndpoint(RetryTemplate retry, ClimbingWallService climbingWallService) {
        this.climbingWallService = climbingWallService;
        this.retry = retry;
    }

    @Secured("ROLE_MANAGER")
    @PostMapping("add_wall")
    public ClimbingWallDTO registerClient(@RequestBody @Valid ClimbingWallDTO wallDTO) throws AbstractAppException {
        return retry.execute(arg0 -> ClimbingWallConverter.climbingWallEntityToDTO(climbingWallService.addNewWall(wallDTO)));
    }
}

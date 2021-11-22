package pl.ftims.ias.perfectbeta.mos.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.ftims.ias.perfectbeta.dto.routes_dtos.ClimbingGymDTO;
import pl.ftims.ias.perfectbeta.dto.routes_dtos.ClimbingGymWithDetailsDTO;
import pl.ftims.ias.perfectbeta.dto.routes_dtos.ClimbingGymWithMaintainersDTO;
import pl.ftims.ias.perfectbeta.dto.routes_dtos.GymDetailsDTO;
import pl.ftims.ias.perfectbeta.exceptions.AbstractAppException;
import pl.ftims.ias.perfectbeta.mos.services.ClimbingGymService;
import pl.ftims.ias.perfectbeta.utils.converters.ClimbingGymConverter;

import javax.validation.Valid;
import java.util.List;


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

    @GetMapping("verified/all")
    public List<ClimbingGymDTO> listAllVerified() throws AbstractAppException {
        return retry.execute(arg0 -> ClimbingGymConverter.createGymListDTOFromEntity(climbingGymService.listVerifiedGyms()));
    }

    @GetMapping("verified/{id}")
    public ClimbingGymWithDetailsDTO findVerifiedById(@PathVariable Long id) throws AbstractAppException {
        return retry.execute(arg0 -> ClimbingGymConverter.climbingGymWithDetailsEntityToDTO(climbingGymService.findVerifiedById(id)));
    }

    @Secured("ROLE_MANAGER")
    @GetMapping("owned_gyms")
    public List<ClimbingGymDTO> listOwnedGyms() throws AbstractAppException {
        return retry.execute(arg0 -> ClimbingGymConverter.createGymListDTOFromEntity(climbingGymService.listOwnedGyms()));
    }

    @Secured("ROLE_MANAGER")
    @GetMapping("maintained_gyms")
    public List<ClimbingGymDTO> listMaintainedGyms() throws AbstractAppException {
        return retry.execute(arg0 -> ClimbingGymConverter.createGymListDTOFromEntity(climbingGymService.listMaintainedGyms()));
    }

    @Secured("ROLE_ADMINISTRATOR")
    @GetMapping("all")
    public List<ClimbingGymDTO> listAllGyms() throws AbstractAppException {
        return retry.execute(arg0 -> ClimbingGymConverter.createGymListDTOFromEntity(climbingGymService.listAllGyms()));
    }

    @Secured("ROLE_ADMINISTRATOR")
    @GetMapping("{id}")
    public ClimbingGymWithDetailsDTO findGymById(@PathVariable Long id) throws AbstractAppException {
        return retry.execute(arg0 -> ClimbingGymConverter.climbingGymWithDetailsEntityToDTO(climbingGymService.findById(id)));
    }

    @Secured("ROLE_MANAGER")
    @PostMapping("register/{gymName}")
    public ClimbingGymWithDetailsDTO registerClient(@PathVariable String gymName) {
        return retry.execute(arg0 -> ClimbingGymConverter.climbingGymWithDetailsEntityToDTO(climbingGymService.registerNewClimbingGym(gymName)));
    }

    @Secured("ROLE_ADMINISTRATOR")
    @PutMapping("verify/{id}")
    public ClimbingGymDTO verifyGym(@PathVariable Long id) throws AbstractAppException {
        return retry.execute(arg0 -> ClimbingGymConverter.climbingGymEntityToDTO(climbingGymService.verifyGym(id)));
    }

    @Secured("ROLE_ADMINISTRATOR")
    @PutMapping("close/{id}")
    public ClimbingGymDTO closeGym(@PathVariable Long id) throws AbstractAppException {
        return retry.execute(arg0 -> ClimbingGymConverter.climbingGymEntityToDTO(climbingGymService.closeGym(id)));
    }

    @Secured("ROLE_MANAGER")
    @PutMapping("{gymId}/add_maintainer/{userId}")
    public ClimbingGymWithMaintainersDTO addMaintainer(@PathVariable Long gymId, @PathVariable Long userId) throws AbstractAppException {
        return retry.execute(arg0 -> ClimbingGymConverter.climbingGymEntityWithMaintainersEntityToDTO(climbingGymService.addMaintainer(gymId, userId)));
    }

    @Secured("ROLE_MANAGER")
    @PutMapping("edit_gym_details/{id}")
    public ClimbingGymWithDetailsDTO editGymDetails(@PathVariable Long id, @RequestBody @Valid GymDetailsDTO detailsDTO) throws AbstractAppException {
        return retry.execute(arg0 -> ClimbingGymConverter.climbingGymWithDetailsEntityToDTO(climbingGymService.editGymDetails(id, detailsDTO)));
    }
}

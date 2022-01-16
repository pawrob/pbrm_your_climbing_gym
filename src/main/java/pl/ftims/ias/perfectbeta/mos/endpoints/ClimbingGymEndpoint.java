package pl.ftims.ias.perfectbeta.mos.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import pl.ftims.ias.perfectbeta.mos.services.ClimbingGymServiceLocal;
import pl.ftims.ias.perfectbeta.utils.converters.ClimbingGymConverter;

import javax.validation.Valid;


@RestController
@RequestMapping("gym")
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.NEVER)
public class ClimbingGymEndpoint {


    RetryTemplate retry;
    ClimbingGymServiceLocal climbingGymService;

    @Autowired
    public ClimbingGymEndpoint(RetryTemplate retry, ClimbingGymService climbingGymService) {
        this.climbingGymService = climbingGymService;
        this.retry = retry;
    }

    @GetMapping("verified/all")
    public Page<ClimbingGymDTO> listAllVerified(Pageable page) {
        return retry.execute(arg0 -> ClimbingGymConverter.climbingGymEntityPageToDTOPage(climbingGymService.listVerifiedGyms(page)));
    }

    @GetMapping("verified/{id}")
    public ClimbingGymWithDetailsDTO findVerifiedById(@PathVariable Long id) throws AbstractAppException {
        return retry.execute(arg0 -> ClimbingGymConverter.climbingGymWithDetailsEntityToDTO(climbingGymService.findVerifiedById(id)));
    }

    @Secured("ROLE_MANAGER")
    @GetMapping("owned_gyms")
    public Page<ClimbingGymDTO> listOwnedGyms(Pageable page) throws AbstractAppException {
        return retry.execute(arg0 -> ClimbingGymConverter.climbingGymEntityPageToDTOPage(climbingGymService.listOwnedGyms(page)));
    }

    @Secured("ROLE_MANAGER")
    @GetMapping("maintained_gyms")
    public Page<ClimbingGymDTO> listMaintainedGyms(Pageable page) throws AbstractAppException {
        return retry.execute(arg0 -> ClimbingGymConverter.climbingGymEntityPageToDTOPage(climbingGymService.listMaintainedGyms(page)));
    }

    @Secured("ROLE_ADMINISTRATOR")
    @GetMapping("all")
    public Page<ClimbingGymDTO> listAllGyms(Pageable page) {
        return retry.execute(arg0 -> ClimbingGymConverter.climbingGymEntityPageToDTOPage(climbingGymService.listAllGyms(page)));
    }

    @Secured("ROLE_ADMINISTRATOR")
    @GetMapping("{id}")
    public ClimbingGymWithDetailsDTO findGymById(@PathVariable Long id) throws AbstractAppException {
        return retry.execute(arg0 -> ClimbingGymConverter.climbingGymWithDetailsEntityToDTO(climbingGymService.findById(id)));
    }

    @Secured("ROLE_MANAGER")
    @PostMapping("register/{gymName}")
    public ClimbingGymWithDetailsDTO registerGym(@PathVariable String gymName) {
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
    @PutMapping("{gymId}/add_maintainer/{username}")
    public ClimbingGymWithMaintainersDTO addMaintainer(@PathVariable Long gymId, @PathVariable String username) throws AbstractAppException {
        return retry.execute(arg0 -> ClimbingGymConverter.climbingGymEntityWithMaintainersEntityToDTO(climbingGymService.addMaintainer(gymId, username)));
    }

    @Secured("ROLE_MANAGER")
    @PutMapping("edit_gym_details/{id}")
    public ClimbingGymWithDetailsDTO editGymDetails(@PathVariable Long id, @RequestBody @Valid GymDetailsDTO detailsDTO) throws AbstractAppException {
        return retry.execute(arg0 -> ClimbingGymConverter.climbingGymWithDetailsEntityToDTO(climbingGymService.editGymDetails(id, detailsDTO)));
    }
}

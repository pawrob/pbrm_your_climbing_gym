package pl.ftims.ias.perfectbeta.mos.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.ftims.ias.perfectbeta.dto.routes_dtos.GymDetailsDTO;
import pl.ftims.ias.perfectbeta.entities.ClimbingGymEntity;
import pl.ftims.ias.perfectbeta.exceptions.AbstractAppException;

public interface ClimbingGymServiceLocal {

    Page<ClimbingGymEntity> listOwnedGyms(Pageable page) throws AbstractAppException;

    Page<ClimbingGymEntity> listMaintainedGyms(Pageable page) throws AbstractAppException;

    Page<ClimbingGymEntity> listAllGyms(Pageable page);

    ClimbingGymEntity findById(Long id) throws AbstractAppException;

    ClimbingGymEntity findVerifiedById(Long id) throws AbstractAppException;

    Page<ClimbingGymEntity> listVerifiedGyms(Pageable page);

    ClimbingGymEntity registerNewClimbingGym(String gymName);

    ClimbingGymEntity verifyGym(Long id) throws AbstractAppException;

    ClimbingGymEntity closeGym(Long id) throws AbstractAppException;

    ClimbingGymEntity editGymDetails(Long id, GymDetailsDTO detailsDTO) throws AbstractAppException;

    ClimbingGymEntity addMaintainer(Long gymId, String username) throws AbstractAppException;

}

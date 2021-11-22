package pl.ftims.ias.perfectbeta.mos.services;

import pl.ftims.ias.perfectbeta.dto.routes_dtos.GymDetailsDTO;
import pl.ftims.ias.perfectbeta.entities.ClimbingGymEntity;
import pl.ftims.ias.perfectbeta.exceptions.AbstractAppException;

import java.util.List;

public interface ClimbingGymServiceLocal {

    List<ClimbingGymEntity> listOwnedGyms() throws AbstractAppException;

    List<ClimbingGymEntity> listMaintainedGyms() throws AbstractAppException;

    List<ClimbingGymEntity> listAllGyms();

    ClimbingGymEntity findById(Long id) throws AbstractAppException;

    ClimbingGymEntity findVerifiedById(Long id) throws AbstractAppException;

    List<ClimbingGymEntity> listVerifiedGyms();

    ClimbingGymEntity registerNewClimbingGym(String gymName);

    ClimbingGymEntity verifyGym(Long id) throws AbstractAppException;

    ClimbingGymEntity closeGym(Long id) throws AbstractAppException;

    ClimbingGymEntity editGymDetails(Long id, GymDetailsDTO detailsDTO) throws AbstractAppException;

    ClimbingGymEntity addMaintainer(Long gymId, Long userId) throws AbstractAppException;

}

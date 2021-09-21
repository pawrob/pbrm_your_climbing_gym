package pl.ftims.ias.your_climbing_gym.mos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ftims.ias.your_climbing_gym.dto.ClimbingWallDTO;
import pl.ftims.ias.your_climbing_gym.entities.ClimbingGymEntity;
import pl.ftims.ias.your_climbing_gym.entities.ClimbingWallEntity;
import pl.ftims.ias.your_climbing_gym.exceptions.AbstractAppException;
import pl.ftims.ias.your_climbing_gym.exceptions.GymNotFoundException;
import pl.ftims.ias.your_climbing_gym.exceptions.NotAllowedAppException;
import pl.ftims.ias.your_climbing_gym.mos.repositories.ClimbingGymRepository;
import pl.ftims.ias.your_climbing_gym.mos.repositories.ClimbingWallRepository;

@Service
@Transactional(transactionManager = "mosTransactionManager", isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
public class ClimbingWallService {

    ClimbingWallRepository climbingWallRepository;
    ClimbingGymRepository climbingGymRepository;

    @Autowired
    public ClimbingWallService(ClimbingWallRepository climbingWallRepository, ClimbingGymRepository climbingGymRepository) {
        this.climbingWallRepository = climbingWallRepository;
        this.climbingGymRepository = climbingGymRepository;
    }

    public ClimbingWallEntity addNewWall(ClimbingWallDTO wallDTO) throws AbstractAppException {
        ClimbingGymEntity gym = climbingGymRepository.findById(wallDTO.getClimbingGymId())
                .orElseThrow(() -> GymNotFoundException.createGymWithProvidedIdNotFoundException(wallDTO.getClimbingGymId()));
        
        if (!gym.getOwner().getLogin().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw NotAllowedAppException.createNotAllowedException();
        }

        ClimbingWallEntity wall = new ClimbingWallEntity(wallDTO.getBoulderName(), wallDTO.getDifficulty(), gym);
        climbingWallRepository.save(wall);
        return wall;
    }
}

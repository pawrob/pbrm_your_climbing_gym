package pl.ftims.ias.your_climbing_gym.mos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ftims.ias.your_climbing_gym.entities.ClimbingGymEntity;
import pl.ftims.ias.your_climbing_gym.entities.UserEntity;
import pl.ftims.ias.your_climbing_gym.mos.repositories.ClimbingGymRepository;
import pl.ftims.ias.your_climbing_gym.mos.repositories.UserMosRepository;

import java.util.Optional;

@Service
@Transactional(transactionManager = "mosTransactionManager", isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
public class ClimbingGymService {

    ClimbingGymRepository climbingGymRepository;
    UserMosRepository userMosRepository;

    @Autowired
    public ClimbingGymService(ClimbingGymRepository climbingGymRepository, UserMosRepository userMosRepository) {
        this.climbingGymRepository = climbingGymRepository;
        this.userMosRepository = userMosRepository;
    }

    public ClimbingGymEntity registerNewClimbingGym(String gymName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> owner = userMosRepository.findByLogin(auth.getName());
        ClimbingGymEntity gym = new ClimbingGymEntity(gymName, owner.get());

        climbingGymRepository.save(gym);
        return gym;
    }
}

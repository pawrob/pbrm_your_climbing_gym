package pl.ftims.ias.your_climbing_gym.mos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ftims.ias.your_climbing_gym.dto.routes_dtos.GymDetailsDTO;
import pl.ftims.ias.your_climbing_gym.entities.ClimbingGymEntity;
import pl.ftims.ias.your_climbing_gym.entities.GymDetailsEntity;
import pl.ftims.ias.your_climbing_gym.entities.UserEntity;
import pl.ftims.ias.your_climbing_gym.entities.enums.GymStatusEnum;
import pl.ftims.ias.your_climbing_gym.exceptions.AbstractAppException;
import pl.ftims.ias.your_climbing_gym.exceptions.GymNotFoundException;
import pl.ftims.ias.your_climbing_gym.exceptions.NotAllowedAppException;
import pl.ftims.ias.your_climbing_gym.exceptions.UserNotFoundAppException;
import pl.ftims.ias.your_climbing_gym.mos.repositories.ClimbingGymRepository;
import pl.ftims.ias.your_climbing_gym.mos.repositories.UserMosRepository;

import java.util.Collection;
import java.util.List;
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


    public List<ClimbingGymEntity> listOwnedGyms() throws AbstractAppException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity owner = userMosRepository.findByLogin(auth.getName()).orElseThrow(() -> UserNotFoundAppException.createUserWithProvidedLoginNotFoundException(auth.getName()));
        return  climbingGymRepository.findByOwner(owner).orElseThrow(() -> GymNotFoundException.createGymWithProvidedOwnerNotFoundException(owner.getLogin()));
    }
    public List<ClimbingGymEntity> listAllGyms() {
        return climbingGymRepository.findAll();
    }


    public List<ClimbingGymEntity> listVerifiedGyms() {
        return climbingGymRepository.findAllVerified();
    }

    public ClimbingGymEntity registerNewClimbingGym(String gymName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> owner = userMosRepository.findByLogin(auth.getName());
        ClimbingGymEntity gym = new ClimbingGymEntity(gymName, owner.get());
        gym.setGymDetails(new GymDetailsEntity(gym));

        return climbingGymRepository.save(gym);
    }

    public ClimbingGymEntity verifyRoute(Long id) throws AbstractAppException {
        ClimbingGymEntity gym = climbingGymRepository.findById(id)
                .orElseThrow(() -> GymNotFoundException.createGymWithProvidedIdNotFoundException(id));

        gym.setStatus(GymStatusEnum.VERIFIED);
        return climbingGymRepository.save(gym);
    }

    public ClimbingGymEntity closeGym(Long id) throws AbstractAppException {
        ClimbingGymEntity gym = climbingGymRepository.findById(id)
                .orElseThrow(() -> GymNotFoundException.createGymWithProvidedIdNotFoundException(id));

        gym.setStatus(GymStatusEnum.CLOSED);
        return climbingGymRepository.save(gym);
    }

    public ClimbingGymEntity editGymDetails(Long id, GymDetailsDTO detailsDTO) throws AbstractAppException {

        ClimbingGymEntity gym = climbingGymRepository.findById(id)
                .orElseThrow(() -> GymNotFoundException.createGymWithProvidedIdNotFoundException(id));
        if (!gym.getOwner().getLogin().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw NotAllowedAppException.createNotAllowedException();
        }
        GymDetailsEntity details = gym.getGymDetails();
        details.setCountry(detailsDTO.getCountry());
        details.setCity(detailsDTO.getCity());
        details.setStreet(detailsDTO.getStreet());
        details.setNumber(detailsDTO.getNumber());
        details.setDescription(detailsDTO.getDescription());
        gym.setGymDetails(details);

        return climbingGymRepository.save(gym);
    }


}

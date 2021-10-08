package pl.ftims.ias.your_climbing_gym.mos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ftims.ias.your_climbing_gym.dto.routes_dtos.GymDetailsDTO;
import pl.ftims.ias.your_climbing_gym.entities.*;
import pl.ftims.ias.your_climbing_gym.entities.enums.GymStatusEnum;
import pl.ftims.ias.your_climbing_gym.exceptions.AbstractAppException;
import pl.ftims.ias.your_climbing_gym.exceptions.GymNotFoundException;
import pl.ftims.ias.your_climbing_gym.exceptions.NotAllowedAppException;
import pl.ftims.ias.your_climbing_gym.exceptions.UserNotFoundAppException;
import pl.ftims.ias.your_climbing_gym.mos.repositories.ClimbingGymRepository;
import pl.ftims.ias.your_climbing_gym.mos.repositories.GymMaintainerRepository;
import pl.ftims.ias.your_climbing_gym.mos.repositories.UserMosRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(transactionManager = "mosTransactionManager", isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
public class ClimbingGymService {

    ClimbingGymRepository climbingGymRepository;
    UserMosRepository userMosRepository;
    GymMaintainerRepository gymMaintainerRepository;

    @Autowired
    public ClimbingGymService(ClimbingGymRepository climbingGymRepository, UserMosRepository userMosRepository, GymMaintainerRepository gymMaintainerRepository) {
        this.climbingGymRepository = climbingGymRepository;
        this.userMosRepository = userMosRepository;
        this.gymMaintainerRepository = gymMaintainerRepository;
    }


    public List<ClimbingGymEntity> listOwnedGyms() throws AbstractAppException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity owner = userMosRepository.findByLogin(auth.getName()).orElseThrow(() -> UserNotFoundAppException.createUserWithProvidedLoginNotFoundException(auth.getName()));
        return climbingGymRepository.findByOwner(owner).orElseThrow(() -> GymNotFoundException.createGymWithProvidedOwnerNotFoundException(owner.getLogin()));
    }

    public List<ClimbingGymEntity> listMaintainedGyms() throws AbstractAppException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity owner = userMosRepository.findByLogin(auth.getName()).orElseThrow(() -> UserNotFoundAppException.createUserWithProvidedLoginNotFoundException(auth.getName()));
        List<GymMaintainerEntity> gymMaintainerEntities = gymMaintainerRepository.findByUser(owner);
        List<ClimbingGymEntity> climbingGymEntities = new ArrayList<>();
        for (GymMaintainerEntity g : gymMaintainerEntities) {
            climbingGymEntities.add(g.getMaintainedGym());
        }
        return climbingGymEntities;
    }

    public List<ClimbingGymEntity> listAllGyms() {
        return climbingGymRepository.findAll();
    }

    public ClimbingGymEntity findById(Long id) throws AbstractAppException {
        return climbingGymRepository.findById(id).orElseThrow(() -> GymNotFoundException.createGymWithProvidedIdNotFoundException(id));
    }

    public ClimbingGymEntity findVerifiedById(Long id) throws AbstractAppException {
        return climbingGymRepository.findVerifiedById(id).orElseThrow(() -> GymNotFoundException.createGymWithProvidedIdNotFoundException(id));
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

    public ClimbingGymEntity verifyGym(Long id) throws AbstractAppException {
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


    public ClimbingGymEntity addMaintainer(Long gymId, Long userId) throws AbstractAppException {
        ClimbingGymEntity gym = climbingGymRepository.findById(gymId)
                .orElseThrow(() -> GymNotFoundException.createGymWithProvidedIdNotFoundException(gymId));

        if (!gym.getOwner().getLogin().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw NotAllowedAppException.createNotAllowedException();
        }
        UserEntity maintainer = userMosRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundAppException.createUserWithProvidedIdNotFoundException(userId));
        if (!checkIfManager(maintainer)) {
            throw NotAllowedAppException.createNotAllowedException();
        }
        gym.getMaintainers().add(new GymMaintainerEntity(gym, maintainer, true));
        return climbingGymRepository.save(gym);
    }

    private boolean checkIfManager(UserEntity user) {
        Collection<AccessLevelEntity> accessLevels = user.getAccessLevels();
        for (AccessLevelEntity accessLevel : accessLevels) {
            if (accessLevel.getAccessLevel().equals("MANAGER") && accessLevel.getActive().equals(Boolean.TRUE)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }


}

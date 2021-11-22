package pl.ftims.ias.perfectbeta.mok.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ftims.ias.perfectbeta.entities.AccessLevelEntity;
import pl.ftims.ias.perfectbeta.entities.PersonalDataEntity;
import pl.ftims.ias.perfectbeta.entities.UserEntity;
import pl.ftims.ias.perfectbeta.exceptions.AbstractAppException;
import pl.ftims.ias.perfectbeta.exceptions.UserNotFoundAppException;
import pl.ftims.ias.perfectbeta.mok.repositories.UserMokRepository;
import pl.ftims.ias.perfectbeta.utils.HashGenerator;

@Service
@Transactional(transactionManager = "mokTransactionManager", isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
public class ManagerService implements ManagerServiceLocal {

    private final UserMokRepository userMokRepository;


    @Autowired
    public ManagerService(UserMokRepository userMokRepository) {
        this.userMokRepository = userMokRepository;
    }

    public UserEntity createManagerAccountWithAccessLevel(UserEntity userEntity) {

        userEntity.setPassword(HashGenerator.generateHash(userEntity.getPassword()));
        userEntity.setVerified(true);
        userEntity.getAccessLevels().add(new AccessLevelEntity(true, userEntity, "MANAGER"));
        userEntity.setPersonalData(new PersonalDataEntity(userEntity));
        userMokRepository.save(userEntity);


        return userEntity;
    }

    public UserEntity deactivateManager(Long id) throws AbstractAppException {
        UserEntity userEntity = userMokRepository.findById(id)
                .orElseThrow(() -> UserNotFoundAppException.createUserWithProvidedIdNotFoundException(id));

        userEntity.getAccessLevels()
                .stream()
                .filter(level -> level.getAccessLevel().equalsIgnoreCase("MANAGER"))
                .forEach(level -> level.setActive(false));
        return userMokRepository.save(userEntity);
    }

    public UserEntity activateManager(Long id) throws AbstractAppException {
        UserEntity userEntity = userMokRepository.findById(id)
                .orElseThrow(() -> UserNotFoundAppException.createUserWithProvidedIdNotFoundException(id));

        userEntity.getAccessLevels()
                .stream()
                .filter(level -> level.getAccessLevel().equalsIgnoreCase("MANAGER"))
                .forEach(level -> level.setActive(true));
        return userMokRepository.save(userEntity);
    }
}

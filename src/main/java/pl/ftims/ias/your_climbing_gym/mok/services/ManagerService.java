package pl.ftims.ias.your_climbing_gym.mok.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ftims.ias.your_climbing_gym.entities.AccessLevelEntity;
import pl.ftims.ias.your_climbing_gym.entities.PersonalDataEntity;
import pl.ftims.ias.your_climbing_gym.entities.UserEntity;
import pl.ftims.ias.your_climbing_gym.mok.repositories.UserMokRepository;
import pl.ftims.ias.your_climbing_gym.utils.HashGenerator;

@Service
@Transactional(transactionManager = "mokTransactionManager", isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
public class ManagerService {

    private final UserMokRepository userMokRepository;


    @Autowired
    public ManagerService(UserMokRepository userMokRepository) {
        this.userMokRepository = userMokRepository;
    }

    public UserEntity createManagerAccountWithAccessLevel(UserEntity userEntity){

        userEntity.setPassword(HashGenerator.generateHash(userEntity.getPassword()));
        userEntity.setVerified(true);
        userEntity.getAccessLevels().add(new AccessLevelEntity(true, userEntity, "MANAGER"));
        userEntity.setPersonalData(new PersonalDataEntity(userEntity));
        userMokRepository.save(userEntity);


        return userEntity;
    }
}

package pl.ftims.ias.your_climbing_gym.mok.services;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ftims.ias.your_climbing_gym.entities.AccessLevelEntity;
import pl.ftims.ias.your_climbing_gym.entities.PersonalDataEntity;
import pl.ftims.ias.your_climbing_gym.entities.UserEntity;
import pl.ftims.ias.your_climbing_gym.exceptions.AbstractAppException;
import pl.ftims.ias.your_climbing_gym.exceptions.UserNotFoundAppException;
import pl.ftims.ias.your_climbing_gym.mok.repositories.UserRepository;
import pl.ftims.ias.your_climbing_gym.utils.HashGenerator;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
public class UserService {


    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> getAllUsers() {
        return IterableUtils.toList(userRepository.findAll());
    }

    public UserEntity getUserById(Long id) throws AbstractAppException {
        return userRepository.findById(id).orElseThrow(() -> UserNotFoundAppException.createUserWithProvidedIdNotFoundException(id));
    }

    public UserEntity createUserAccountWithAccessLevel(UserEntity userEntity) throws AbstractAppException {

        userEntity.setPassword(HashGenerator.generateHash(userEntity.getPassword()));
        String token = HashGenerator.generateSecureRandomToken();

        userEntity.setPasswordResetToken(token);
        userEntity.setPasswordResetTokenTimestamp(OffsetDateTime.now());
        userEntity.setEmailResetToken(token);
        userEntity.setEmailResetTokenTimestamp(OffsetDateTime.now());
        AccessLevelEntity accessLevelEntity = new AccessLevelEntity(true,userEntity,"CLIMBER");

        userEntity.getAccessLevels().add(accessLevelEntity);


        userRepository.saveAndFlush(userEntity);

//        PersonalDataEntity personalDataEntity = new PersonalDataEntity();
//        personalDataEntity.setUser(userEntity);
//        personalDataEntity.setUserId(userEntity.getId());
//        userEntity.setPersonalData(personalDataEntity);

        return userEntity;
        }

    }


package pl.ftims.ias.your_climbing_gym.mok.services;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.RandomStringUtils;
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

    public UserEntity createUserAccountWithAccessLevel(UserEntity userEntity) {
        userEntity.setPassword(HashGenerator.generateHash(userEntity.getPassword()));
        userEntity.setVerifyToken(RandomStringUtils.randomAlphabetic(64));
        userEntity.setVerifyTokenTimestamp(OffsetDateTime.now());

        userEntity.getAccessLevels().add(new AccessLevelEntity(true, userEntity, "CLIMBER"));
        userEntity.setPersonalData(new PersonalDataEntity(userEntity));

        return userRepository.save(userEntity);
    }

}


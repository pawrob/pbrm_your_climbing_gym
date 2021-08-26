package pl.ftims.ias.your_climbing_gym.mok.services;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.ftims.ias.your_climbing_gym.entities.UserEntity;
import pl.ftims.ias.your_climbing_gym.mok.repositories.UserRepository;


import java.util.List;

@Service
public class UserService {


    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> getAllUsers() {
        return IterableUtils.toList(userRepository.findAll());
    }
}

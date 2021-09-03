package pl.ftims.ias.your_climbing_gym.mok.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.ftims.ias.your_climbing_gym.dto.user_dtos.RegistrationDTO;
import pl.ftims.ias.your_climbing_gym.dto.user_dtos.UserWithAccessLevelDTO;
import pl.ftims.ias.your_climbing_gym.dto.user_dtos.UserWithPersonalDataAccessLevelDTO;
import pl.ftims.ias.your_climbing_gym.exceptions.AbstractAppException;
import pl.ftims.ias.your_climbing_gym.mok.services.UserService;
import pl.ftims.ias.your_climbing_gym.utils.converters.UserConverter;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("users")
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.NEVER)
public class UserEndpoint {

    UserService userService;
    RetryTemplate retry;

    @Autowired
    public UserEndpoint(UserService userService, RetryTemplate retry) {
        this.userService = userService;
        this.retry = retry;
    }

    @Secured("ROLE_ADMINISTRATOR")
    @GetMapping
    public List<UserWithPersonalDataAccessLevelDTO> getAllUsers() {
        return retry.execute(arg0 -> UserConverter.createUserWithPersonalDataAccessLevelDTOListFromEntity(userService.getAllUsers()));
    }

    @Secured("ROLE_ADMINISTRATOR")
    @GetMapping("/{id}")
    public UserWithPersonalDataAccessLevelDTO getUserById(@PathVariable Long id) throws AbstractAppException {
        return retry.execute(arg0 -> UserConverter.userWithPersonalDataAccessLevelDTOFromEntity(userService.getUserById(id)));
    }


    @PostMapping("register")
    public UserWithPersonalDataAccessLevelDTO addClient(@RequestBody @Valid RegistrationDTO user) {
        return retry.execute(arg0 -> UserConverter.userWithPersonalDataAccessLevelDTOFromEntity(
                userService.createUserAccountWithAccessLevel(UserConverter.createNewUserEntityFromDTO(user))));

    }

    @PutMapping("/verify")
    public UserWithAccessLevelDTO verifyUser(@RequestParam("username") String username, @RequestParam("token") String token) throws AbstractAppException {
        return retry.execute(arg0 -> UserConverter.userWithAccessLevelDTOFromEntity(userService.verifyUser(username, token)));
    }
}


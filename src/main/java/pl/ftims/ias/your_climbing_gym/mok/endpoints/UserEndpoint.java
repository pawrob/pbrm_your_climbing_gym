package pl.ftims.ias.your_climbing_gym.mok.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.ftims.ias.your_climbing_gym.dto.PasswordDTO;
import pl.ftims.ias.your_climbing_gym.dto.PersonalDataDTO;
import pl.ftims.ias.your_climbing_gym.dto.user_dtos.RegistrationDTO;
import pl.ftims.ias.your_climbing_gym.dto.user_dtos.UserWithAccessLevelDTO;
import pl.ftims.ias.your_climbing_gym.dto.user_dtos.UserWithPersonalDataAccessLevelDTO;
import pl.ftims.ias.your_climbing_gym.dto.user_dtos.UserWithPersonalDataDTO;
import pl.ftims.ias.your_climbing_gym.exceptions.AbstractAppException;
import pl.ftims.ias.your_climbing_gym.mok.services.UserService;
import pl.ftims.ias.your_climbing_gym.utils.converters.PersonalDataConverter;
import pl.ftims.ias.your_climbing_gym.utils.converters.UserConverter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

    @Secured({"ROLE_ADMINISTRATOR", "ROLE_MANAGER"})
    @GetMapping
    public List<UserWithPersonalDataAccessLevelDTO> getAllUsers() {
        return retry.execute(arg0 -> UserConverter.createUserWithPersonalDataAccessLevelDTOListFromEntity(userService.getAllUsers()));
    }

    @Secured({"ROLE_ADMINISTRATOR", "ROLE_MANAGER"})
    @GetMapping("/{id}")
    public UserWithPersonalDataAccessLevelDTO getUserById(@PathVariable Long id) throws AbstractAppException {
        return retry.execute(arg0 -> UserConverter.userWithPersonalDataAccessLevelDTOFromEntity(userService.getUserById(id)));
    }


    @PostMapping("register")
    public UserWithPersonalDataAccessLevelDTO addClient(@RequestBody @Valid RegistrationDTO user) throws AbstractAppException {
        return retry.execute(arg0 -> UserConverter.userWithPersonalDataAccessLevelDTOFromEntity(
                userService.createUserAccountWithAccessLevel(UserConverter.createNewUserEntityFromDTO(user))));

    }

    @PutMapping("/verify")
    public UserWithAccessLevelDTO verifyUser(@RequestParam("username") String username, @RequestParam("token") String token) throws AbstractAppException {
        return retry.execute(arg0 -> UserConverter.userWithAccessLevelDTOFromEntity(userService.verifyUser(username, token)));
    }

    @Secured({"ROLE_ADMINISTRATOR", "ROLE_MANAGER", "ROLE_CLIMBER"})
    @PutMapping("update/{id}")
    public UserWithPersonalDataDTO updateUserPersonalData(@RequestBody @NotNull @Valid PersonalDataDTO newData, @PathVariable("id") long id) throws AbstractAppException {
        return retry.execute(arg0 -> UserConverter.userWithPersonalDataDTOFromEntity(
                userService.editUserData(PersonalDataConverter.personalDataEntityFromDTO(newData), id)));
    }

    @Secured({"ROLE_ADMINISTRATOR", "ROLE_MANAGER", "ROLE_CLIMBER"})
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Object> deleteUser(@RequestBody @NotNull @Valid PasswordDTO password, @PathVariable("id") long id) throws AbstractAppException {
        retry.execute(arg0 -> userService.deleteUser(password, id));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}


package pl.ftims.ias.your_climbing_gym.mok.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ftims.ias.your_climbing_gym.dto.user_dtos.UserWithPersonalDataAccessLevelDTO;
import pl.ftims.ias.your_climbing_gym.mok.services.UserService;
import pl.ftims.ias.your_climbing_gym.utils.converters.UserConverter;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserEndpoint {

    UserService userService;

    @Autowired
    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    @Secured("ROLE_ADMINISTRATOR")
    @GetMapping
    public List<UserWithPersonalDataAccessLevelDTO> getAllUsers(){
        return UserConverter.createUserWithPersonalDataAccessLevelDTOListFromEntity(userService.getAllUsers());
    }
}

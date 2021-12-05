package pl.ftims.ias.perfectbeta.mok.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.ftims.ias.perfectbeta.dto.*;
import pl.ftims.ias.perfectbeta.dto.user_dtos.*;
import pl.ftims.ias.perfectbeta.exceptions.AbstractAppException;
import pl.ftims.ias.perfectbeta.mok.services.UserService;
import pl.ftims.ias.perfectbeta.mok.services.UserServiceLocal;
import pl.ftims.ias.perfectbeta.utils.converters.PersonalDataConverter;
import pl.ftims.ias.perfectbeta.utils.converters.UserConverter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("users")
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.NEVER)
public class UserEndpoint {

    UserServiceLocal userService;
    RetryTemplate retry;

    @Autowired
    public UserEndpoint(UserService userService, RetryTemplate retry) {
        this.userService = userService;
        this.retry = retry;
    }

    @Secured({"ROLE_ADMINISTRATOR", "ROLE_MANAGER"})
    @GetMapping
    public Page<UserWithPersonalDataAccessLevelDTO> getAllUsers(Pageable page) {
        return retry.execute(arg0 -> UserConverter.userEntityPageToDTOPage(userService.getAllUsers(page)));
    }

    @Secured({"ROLE_ADMINISTRATOR", "ROLE_MANAGER"})
    @GetMapping("/{id}")
    public UserWithPersonalDataAccessLevelDTO getUserById(@PathVariable Long id) throws AbstractAppException {
        return retry.execute(arg0 -> UserConverter.userWithPersonalDataAccessLevelDTOFromEntity(userService.getUserById(id)));
    }

    @PostMapping("register")
    public UserWithPersonalDataAccessLevelDTO registerClient(@RequestBody @Valid RegistrationDTO user) {
        return retry.execute(arg0 -> UserConverter.userWithPersonalDataAccessLevelDTOFromEntity(
                userService.createUserAccountWithAccessLevel(UserConverter.createNewUserEntityFromDTO(user))));
    }

    @PutMapping("/verify")
    public UserWithAccessLevelDTO verifyUser(@RequestParam("username") String username, @RequestParam("token") String token) throws AbstractAppException {
        return retry.execute(arg0 -> UserConverter.userWithAccessLevelDTOFromEntity(userService.verifyUser(username, token)));
    }

    @Secured({"ROLE_ADMINISTRATOR", "ROLE_MANAGER", "ROLE_CLIMBER"})
    @PutMapping("update/{id}")
    public UserWithPersonalDataDTO updateOwnUserPersonalData(@RequestBody @NotNull @Valid PersonalDataDTO newData, @PathVariable("id") long id) throws AbstractAppException {
        return retry.execute(arg0 -> UserConverter.userWithPersonalDataDTOFromEntity(
                userService.editUserData(PersonalDataConverter.personalDataEntityFromDTO(newData), id)));
    }

    @Secured({"ROLE_ADMINISTRATOR", "ROLE_MANAGER", "ROLE_CLIMBER"})
    @PutMapping("change_password")
    public UserDTO changePassword(@RequestBody @NotNull @Valid ChangePasswordDTO changePasswordDTO) throws AbstractAppException {
        return retry.execute(arg0 -> UserConverter.userEntityToDTO(
                userService.changePassword(changePasswordDTO)));
    }

    @Secured({"ROLE_ADMINISTRATOR", "ROLE_MANAGER", "ROLE_CLIMBER"})
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Object> deleteOwnUserAccount(@RequestBody @NotNull @Valid PasswordDTO password, @PathVariable("id") long id) throws AbstractAppException {
        retry.execute(arg0 -> userService.deleteUser(password, id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured("ROLE_ADMINISTRATOR")
    @PutMapping("deactivate/{id}")
    public UserDTO deactivateUser(@PathVariable Long id) throws AbstractAppException {
        return retry.execute(arg0 -> UserConverter.userWithAccessLevelDTOFromEntity(userService.deactivateUser(id)));

    }

    @Secured("ROLE_ADMINISTRATOR")
    @PutMapping("activate/{id}")
    public UserDTO activateUser(@PathVariable Long id) throws AbstractAppException {
        return retry.execute(arg0 -> UserConverter.userWithAccessLevelDTOFromEntity(userService.activateUser(id)));
    }

    @Secured({"ROLE_ADMINISTRATOR", "ROLE_MANAGER", "ROLE_CLIMBER"})
    @GetMapping("request_change_email")
    public UserDTO requestChangeEmail(@RequestBody @NotNull @Valid EmailDTO emailDTO) throws AbstractAppException {
        return retry.execute(arg0 -> UserConverter.userEntityToDTO(userService.requestChangeEmail(emailDTO)));
    }

    @Secured({"ROLE_ADMINISTRATOR", "ROLE_MANAGER", "ROLE_CLIMBER"})
    @GetMapping("change_email")
    public UserDTO changeEmail(@NotNull @RequestParam("token") String token, @NotNull @RequestParam("email") String email) throws AbstractAppException {
        return retry.execute(arg0 -> UserConverter.userEntityToDTO(userService.changeEmail(token, email)));
    }

    @GetMapping("request_reset_password")
    public UserDTO requestResetPassword(@RequestBody @NotNull @Valid EmailDTO emailDTO) throws AbstractAppException {
        return retry.execute(arg0 -> UserConverter.userEntityToDTO(userService.requestResetPassword(emailDTO)));
    }

    @GetMapping("reset_password")
    public UserDTO resetPassword(@NotNull @RequestParam("id") Long id, @NotNull @RequestParam("token") String token, @RequestBody @NotNull @Valid ResetPasswordDTO resetPasswordDTO) throws AbstractAppException {
        return retry.execute(arg0 -> UserConverter.userEntityToDTO(userService.resetPassword(id, token, resetPasswordDTO)));
    }

}


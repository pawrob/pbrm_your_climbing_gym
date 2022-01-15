package pl.ftims.ias.perfectbeta.mok.services;

import pl.ftims.ias.perfectbeta.dto.ChangePasswordDTO;
import pl.ftims.ias.perfectbeta.dto.EmailDTO;
import pl.ftims.ias.perfectbeta.dto.PasswordDTO;
import pl.ftims.ias.perfectbeta.dto.ResetPasswordDTO;
import pl.ftims.ias.perfectbeta.entities.PersonalDataEntity;
import pl.ftims.ias.perfectbeta.entities.UserEntity;
import pl.ftims.ias.perfectbeta.exceptions.AbstractAppException;
import pl.ftims.ias.perfectbeta.exceptions.UserNotFoundAppException;

public interface UserServiceLocal {


    UserEntity createUserAccountWithAccessLevel(UserEntity userEntity);

    UserEntity verifyUser(String username, String token) throws AbstractAppException;

    UserEntity editUserData(PersonalDataEntity personalDataEntityFromDTO, long id) throws AbstractAppException;

    Boolean deleteUser(PasswordDTO password, long id) throws AbstractAppException;

    UserEntity deactivateUser(Long id) throws AbstractAppException;

    UserEntity activateUser(Long id) throws AbstractAppException;

    UserEntity changePassword(ChangePasswordDTO changePasswordDTO) throws AbstractAppException;

    UserEntity changeEmail(String token, String email) throws AbstractAppException;

    UserEntity requestChangeEmail(EmailDTO emailDTO) throws AbstractAppException;

    UserEntity requestResetPassword(EmailDTO emailDTO) throws AbstractAppException;

    UserEntity resetPassword(Long id, String token, ResetPasswordDTO resetPasswordDTO) throws AbstractAppException;

    UserEntity getSelfUser() throws AbstractAppException;

    UserEntity verifyUserByToken(String userToken) throws AbstractAppException;
}

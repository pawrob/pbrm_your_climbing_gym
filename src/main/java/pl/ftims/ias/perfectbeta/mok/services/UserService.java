package pl.ftims.ias.perfectbeta.mok.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.ftims.ias.perfectbeta.dto.ChangePasswordDTO;
import pl.ftims.ias.perfectbeta.dto.EmailDTO;
import pl.ftims.ias.perfectbeta.dto.PasswordDTO;
import pl.ftims.ias.perfectbeta.dto.ResetPasswordDTO;
import pl.ftims.ias.perfectbeta.entities.AccessLevelEntity;
import pl.ftims.ias.perfectbeta.entities.PersonalDataEntity;
import pl.ftims.ias.perfectbeta.entities.UserEntity;
import pl.ftims.ias.perfectbeta.exceptions.*;
import pl.ftims.ias.perfectbeta.mok.repositories.PersonalDataMokRepository;
import pl.ftims.ias.perfectbeta.mok.repositories.UserMokRepository;
import pl.ftims.ias.perfectbeta.utils.security.HashGenerator;
import pl.ftims.ias.perfectbeta.utils.mailing.EmailSender;
import pl.ftims.ias.perfectbeta.utils.security.SymmetricCrypt;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

@Service
@Transactional(transactionManager = "mokTransactionManager", isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
public class UserService implements UserServiceLocal {


    private final UserMokRepository userMokRepository;
    private final PersonalDataMokRepository personalDataMokRepository;
    private final EmailSender emailSender;
    private final TemplateEngine templateEngine;


    @Autowired
    public UserService(UserMokRepository userMokRepository, PersonalDataMokRepository personalDataMokRepository, EmailSender emailSender, TemplateEngine templateEngine) {
        this.userMokRepository = userMokRepository;
        this.personalDataMokRepository = personalDataMokRepository;
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
    }


    @Override
    public UserEntity createUserAccountWithAccessLevel(UserEntity userEntity) {
        userEntity.setPassword(HashGenerator.generateHash(userEntity.getPassword()));
        userEntity.setVerifyToken(RandomStringUtils.randomAlphabetic(64));
        userEntity.setVerifyTokenTimestamp(OffsetDateTime.now());

        userEntity.getAccessLevels().add(new AccessLevelEntity(true, userEntity, "CLIMBER"));
        userEntity.setPersonalData(new PersonalDataEntity(userEntity));
        userMokRepository.save(userEntity);

        //mailing
        String encryptedUsername = SymmetricCrypt.encrypt(userEntity.getLogin());
        Context context = new Context();
        context.setVariable("header", "Dziękujemy za założenie profilu w serwisie PerfectBeta");
        context.setVariable("title", "Potwierdzenie adresu email aktywuje konto oraz pozwoli na zalogowanie się i korzystanie z serwisu!");
        context.setVariable("description", "Wklej poniższy kod w aplikacji aby aktywować konto 👇");
        context.setVariable("token", encryptedUsername);
        String body = templateEngine.process("template", context);
        emailSender.sendEmail(userEntity.getEmail(), "PerfectBeta - Dziękujemy za założenie profilu", body);


        return userEntity;
    }
    @Override
    public UserEntity getSelfUser() throws AbstractAppException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userMokRepository.findByLogin(auth.getName()).orElseThrow(() -> UserNotFoundAppException.createUserWithProvidedLoginNotFoundException(auth.getName()));
    }

    @Override
    public UserEntity verifyUserByToken(String userToken) throws AbstractAppException {

        if (null == userToken) throw InvalidTokenException.createTokenExpiredException();

        String username = SymmetricCrypt.decrypt(userToken);
        UserEntity userEntity = userMokRepository.findByLogin(username)
                .orElseThrow(() -> UserNotFoundAppException.createUserWithProvidedLoginNotFoundException(username));

        if (userEntity.getVerifyTokenTimestamp() == null)
            throw InvalidTokenException.createTokenExpiredException();
        if (userEntity.getVerifyTokenTimestamp().until(OffsetDateTime.now(), ChronoUnit.HOURS) > 24)
            throw InvalidTokenException.createTokenExpiredException();

        userEntity.setVerified(true);

        userEntity.setVerifyTokenTimestamp(null);
        userEntity.setVerifyToken(null);

        Context context = new Context();
        context.setVariable("header", "Dziękujemy za potwierdzenie konta");
        context.setVariable("title", "Konto zostało potwierdzone");
        context.setVariable("description", "Cieszymy się że jesteś z nami, Dziękujemy za potwierdzenie konta");
        String body = templateEngine.process("template", context);
        emailSender.sendEmail(userEntity.getEmail(), "PerfectBeta - Dziękujemy za potwierdzenie adresu email", body);

        return userMokRepository.save(userEntity);
    }

    public UserEntity verifyUser(String username, String token) throws AbstractAppException {
        UserEntity userEntity = userMokRepository.findByLogin(username)
                .orElseThrow(() -> UserNotFoundAppException.createUserWithProvidedLoginNotFoundException(username));


        if (null == token)
            throw InvalidTokenException.createInvalidTokenException(username);
        if (userEntity.getVerifyTokenTimestamp() == null)
            throw InvalidTokenException.createTokenExpiredException();
        if (!userEntity.getVerifyToken().equals(token))
            throw InvalidTokenException.createInvalidTokenException(username);
        if (userEntity.getVerifyTokenTimestamp().until(OffsetDateTime.now(), ChronoUnit.HOURS) > 24)
            throw InvalidTokenException.createTokenExpiredException();


        userEntity.setVerified(true);
        userEntity.setVerifyTokenTimestamp(null);
        userEntity.setVerifyToken(null);

        //mailing
        Context context = new Context();
        context.setVariable("header", "Dziękujemy za potwierdzenie konta");
        context.setVariable("title", "Konto zostało potwierdzone ");
        context.setVariable("description", "Cieszymy się że jesteś z nami, Dziękujemy za potwierdzenie konta");
        String body = templateEngine.process("template", context);
        emailSender.sendEmail(userEntity.getEmail(), "PerfectBeta - Dziękujemy za potwierdzenie adresu email", body);


        return userMokRepository.save(userEntity);
    }

    public UserEntity editUserData(PersonalDataEntity personalDataEntityFromDTO, long id) throws AbstractAppException {
        UserEntity userEntity = userMokRepository.findById(id)
                .orElseThrow(() -> UserNotFoundAppException.createUserWithProvidedIdNotFoundException(id));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userEntity.getLogin().equals(auth.getName()))
            throw NotAllowedAppException.createNotAllowedException();

        PersonalDataEntity oldData = userEntity.getPersonalData();
        oldData.setName(personalDataEntityFromDTO.getName());
        oldData.setSurname(personalDataEntityFromDTO.getSurname());
        oldData.setPhoneNumber(personalDataEntityFromDTO.getPhoneNumber());
        oldData.setLanguage(personalDataEntityFromDTO.getLanguage());
        oldData.setGender(personalDataEntityFromDTO.getGender());
        oldData.setVersion(personalDataEntityFromDTO.getVersion());

        userEntity.setPersonalData(oldData);

        personalDataMokRepository.save(oldData);
        return userEntity;

    }


    public Boolean deleteUser(PasswordDTO password, long id) throws AbstractAppException {
        UserEntity userEntity = userMokRepository.findById(id)
                .orElseThrow(() -> UserNotFoundAppException.createUserWithProvidedIdNotFoundException(id));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!userEntity.getLogin().equals(auth.getName()))
            throw NotAllowedAppException.createNotAllowedException();

        if (!HashGenerator.checkPassword(password.getPassword(), userEntity.getPassword())) {
            throw InvalidCredentialsException.createInvalidPasswordException();
        }

        userEntity.setActive(false);
        userEntity.setLogin("#" + userEntity.getLogin());
        userEntity.setEmail("#" + userEntity.getEmail());

        PersonalDataEntity personalDataEntity = userEntity.getPersonalData();

        personalDataEntity.setName(null);
        personalDataEntity.setSurname(null);
        personalDataEntity.setPhoneNumber(null);
        personalDataEntity.setLanguage("PL");
        personalDataMokRepository.save(personalDataEntity);
        userMokRepository.save(userEntity);

        return true;
    }

    public UserEntity deactivateUser(Long id) throws AbstractAppException {
        UserEntity userEntity = userMokRepository.findById(id)
                .orElseThrow(() -> UserNotFoundAppException.createUserWithProvidedIdNotFoundException(id));

        userEntity.setActive(false);
        return userMokRepository.save(userEntity);
    }

    public UserEntity activateUser(Long id) throws AbstractAppException {
        UserEntity userEntity = userMokRepository.findById(id)
                .orElseThrow(() -> UserNotFoundAppException.createUserWithProvidedIdNotFoundException(id));

        userEntity.setActive(true);
        return userMokRepository.save(userEntity);
    }

    public UserEntity changePassword(ChangePasswordDTO changePasswordDTO) throws AbstractAppException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UserEntity userEntity = userMokRepository.findByLogin(auth.getName())
                .orElseThrow(NotAllowedAppException::createNotAllowedException);

        if (!HashGenerator.checkPassword(changePasswordDTO.getOldPassword(), userEntity.getPassword())) {
            throw InvalidCredentialsException.createInvalidPasswordException();
        }
        if (HashGenerator.checkPassword(changePasswordDTO.getNewPassword(), userEntity.getPassword())) {
            throw InvalidCredentialsException.createPasswordSameAsOldException();
        }

        userEntity.setPassword(HashGenerator.generateHash(changePasswordDTO.getNewPassword()));
        return userMokRepository.save(userEntity);
    }

    public UserEntity changeEmail(String token, String email) throws AbstractAppException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UserEntity userEntity = userMokRepository.findByLogin(auth.getName())
                .orElseThrow(NotAllowedAppException::createNotAllowedException);

        if (userEntity.getEmailResetToken() == null || userEntity.getEmailResetTokenTimestamp() == null)
            throw InvalidTokenException.createTokenExpiredException();
        if (!userEntity.getEmailResetToken().equals(token))
            throw InvalidTokenException.createInvalidTokenException(userEntity.getLogin());
        if (userEntity.getEmailResetTokenTimestamp().until(OffsetDateTime.now(), ChronoUnit.MINUTES) > 20)
            throw InvalidTokenException.createTokenExpiredException();
        if (!userMokRepository.findByEmail(email).isEmpty())
            throw UniqueConstraintAppException.createEmailTakenException();

        userEntity.setEmailResetToken(null);
        userEntity.setEmailResetTokenTimestamp(null);

        userEntity.setEmail(email);
        return userMokRepository.save(userEntity);
    }

    public UserEntity changeEmailByToken(String token, String email) throws AbstractAppException {
        if (null == token) throw InvalidTokenException.createTokenExpiredException();

        String username = SymmetricCrypt.decrypt(token);
        UserEntity userEntity = userMokRepository.findByLogin(username)
                .orElseThrow(() -> UserNotFoundAppException.createUserWithProvidedLoginNotFoundException(username));

        if (userEntity.getEmailResetToken() == null || userEntity.getEmailResetTokenTimestamp() == null)
            throw InvalidTokenException.createTokenExpiredException();
        if (userEntity.getEmailResetTokenTimestamp().until(OffsetDateTime.now(), ChronoUnit.MINUTES) > 20)
            throw InvalidTokenException.createTokenExpiredException();
        if (!userMokRepository.findByEmail(email).isEmpty())
            throw UniqueConstraintAppException.createEmailTakenException();

        userEntity.setEmailResetToken(null);
        userEntity.setEmailResetTokenTimestamp(null);

        userEntity.setEmail(email);
        return userMokRepository.save(userEntity);
    }

    public UserEntity requestChangeEmail(EmailDTO emailDTO) throws AbstractAppException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UserEntity userEntity = userMokRepository.findByLogin(auth.getName())
                .orElseThrow(NotAllowedAppException::createNotAllowedException);

        if (!userMokRepository.findByEmail(emailDTO.getEmail()).isEmpty()) {
            throw UniqueConstraintAppException.createEmailTakenException();
        }
        userEntity.setEmailResetToken(RandomStringUtils.randomAlphabetic(64));
        userEntity.setEmailResetTokenTimestamp(OffsetDateTime.now());
        String encryptedUsername = SymmetricCrypt.encrypt(userEntity.getLogin());
        //mailing
        Context context = new Context();
        context.setVariable("header", "potwierdzenie zmiany adresu email w serwisie PerfectBeta");
        context.setVariable("title", "Czas lepiej się poznac");
        // todo set link to production host
        context.setVariable("description", encryptedUsername);
        String body = templateEngine.process("template", context);
        emailSender.sendEmail(userEntity.getEmail(), "PerfectBeta - potwierdzenie zmiany adresu email", body);

        return userMokRepository.save(userEntity);
    }

    public UserEntity requestResetPassword(EmailDTO emailDTO) throws AbstractAppException {

        UserEntity userEntity = userMokRepository.findByEmail(emailDTO.getEmail())
                .orElseThrow(() -> UserNotFoundAppException.createUserWithProvidedEmailNotFoundException(emailDTO.getEmail()));

        userEntity.setPasswordResetToken(RandomStringUtils.randomAlphabetic(64));
        userEntity.setPasswordResetTokenTimestamp(OffsetDateTime.now());

        String encryptedUsername = SymmetricCrypt.encrypt(userEntity.getLogin());
        //mailing
        Context context = new Context();
        context.setVariable("header", "potwierdzenie zmiany hasła  w serwisie PerfectBeta");
        context.setVariable("title", "Czas lepiej się poznac");
        // todo set link to production host
        context.setVariable("description", encryptedUsername);
        String body = templateEngine.process("template", context);
        emailSender.sendEmail(userEntity.getEmail(), "PerfectBeta - potwierdzenie zmiany hasła", body);

        return userMokRepository.save(userEntity);
    }

    public UserEntity resetPasswordByToken(String token, ResetPasswordDTO resetPasswordDTO) throws AbstractAppException {

        String username = SymmetricCrypt.decrypt(token);
        UserEntity userEntity = userMokRepository.findByLogin(username)
                .orElseThrow(() -> UserNotFoundAppException.createUserWithProvidedLoginNotFoundException(username));

        if (userEntity.getPasswordResetToken() == null || userEntity.getPasswordResetTokenTimestamp() == null)
            throw InvalidTokenException.createTokenExpiredException();
        if (userEntity.getPasswordResetTokenTimestamp().until(OffsetDateTime.now(), ChronoUnit.MINUTES) > 20)
            throw InvalidTokenException.createTokenExpiredException();


        userEntity.setPasswordResetToken(null);
        userEntity.setPasswordResetTokenTimestamp(null);

        if (HashGenerator.checkPassword(resetPasswordDTO.getNewPassword(), userEntity.getPassword())) {
            throw InvalidCredentialsException.createPasswordSameAsOldException();
        }
        if (!resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getNewPasswordConfirmation())) {
            throw InvalidCredentialsException.createPasswordNotMatchException();
        }

        userEntity.setPassword(HashGenerator.generateHash(resetPasswordDTO.getNewPassword()));
        return userMokRepository.save(userEntity);
    }

    public UserEntity resetPassword(Long id, String token, ResetPasswordDTO resetPasswordDTO) throws AbstractAppException {

        UserEntity userEntity = userMokRepository.findById(id)
                .orElseThrow(() -> UserNotFoundAppException.createUserWithProvidedIdNotFoundException(id));

        if (userEntity.getPasswordResetToken() == null || userEntity.getPasswordResetTokenTimestamp() == null)
            throw InvalidTokenException.createTokenExpiredException();
        if (!userEntity.getPasswordResetToken().equals(token))
            throw InvalidTokenException.createInvalidTokenException(userEntity.getLogin());
        if (userEntity.getPasswordResetTokenTimestamp().until(OffsetDateTime.now(), ChronoUnit.MINUTES) > 20)
            throw InvalidTokenException.createTokenExpiredException();


        userEntity.setPasswordResetToken(null);
        userEntity.setPasswordResetTokenTimestamp(null);

        if (HashGenerator.checkPassword(resetPasswordDTO.getNewPassword(), userEntity.getPassword())) {
            throw InvalidCredentialsException.createPasswordSameAsOldException();
        }
        if (!resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getNewPasswordConfirmation())) {
            throw InvalidCredentialsException.createPasswordNotMatchException();
        }

        userEntity.setPassword(HashGenerator.generateHash(resetPasswordDTO.getNewPassword()));
        return userMokRepository.save(userEntity);
    }
}


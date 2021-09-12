package pl.ftims.ias.your_climbing_gym.mok.services;

import org.apache.commons.collections4.IterableUtils;
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
import pl.ftims.ias.your_climbing_gym.dto.PasswordDTO;
import pl.ftims.ias.your_climbing_gym.entities.AccessLevelEntity;
import pl.ftims.ias.your_climbing_gym.entities.PersonalDataEntity;
import pl.ftims.ias.your_climbing_gym.entities.UserEntity;
import pl.ftims.ias.your_climbing_gym.exceptions.*;
import pl.ftims.ias.your_climbing_gym.mok.repositories.PersonalDataMokRepository;
import pl.ftims.ias.your_climbing_gym.mok.repositories.UserMokRepository;
import pl.ftims.ias.your_climbing_gym.utils.HashGenerator;
import pl.ftims.ias.your_climbing_gym.utils.mailing.EmailSender;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
public class UserService {


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

    public List<UserEntity> getAllUsers() {
        return IterableUtils.toList(userMokRepository.findAll());
    }


    public UserEntity getUserById(Long id) throws AbstractAppException {
        return userMokRepository.findById(id).orElseThrow(() -> UserNotFoundAppException.createUserWithProvidedIdNotFoundException(id));
    }

    public UserEntity createUserAccountWithAccessLevel(UserEntity userEntity) {
        //todo check if login/email is taken to optimize
        userEntity.setPassword(HashGenerator.generateHash(userEntity.getPassword()));
        userEntity.setVerifyToken(RandomStringUtils.randomAlphabetic(64));
        userEntity.setVerifyTokenTimestamp(OffsetDateTime.now());

        userEntity.getAccessLevels().add(new AccessLevelEntity(true, userEntity, "CLIMBER"));
        userEntity.setPersonalData(new PersonalDataEntity(userEntity));


        //mailing
        Context context = new Context();
        context.setVariable("header", "Dziękujemy za założenie profilu w serwisie PerfectBeta");
        context.setVariable("title", "Czas lepiej się poznac");
        context.setVariable("description", "Cieszymy się że jesteś z nami, kliknij w przycisk poniżej aby aktywować swoje konto");
        String body = templateEngine.process("template", context);
        emailSender.sendEmail(userEntity.getEmail(), "PerfectBeta - Dziękujemy za założenie profilu", body);


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

        //todo check who is editing
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
}


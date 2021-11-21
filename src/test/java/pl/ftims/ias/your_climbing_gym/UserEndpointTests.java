package pl.ftims.ias.your_climbing_gym;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ftims.ias.your_climbing_gym.dto.*;
import pl.ftims.ias.your_climbing_gym.dto.user_dtos.*;
import pl.ftims.ias.your_climbing_gym.entities.UserEntity;
import pl.ftims.ias.your_climbing_gym.exceptions.AbstractAppException;
import pl.ftims.ias.your_climbing_gym.exceptions.UserNotFoundAppException;
import pl.ftims.ias.your_climbing_gym.mok.repositories.UserMokRepository;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserEndpointTests {


    UserMokRepository userMokRepository;

    @Autowired
    public UserEndpointTests(UserMokRepository userMokRepository) {
        this.userMokRepository = userMokRepository;
    }

    public String getToken(String login, String password) {
        CredentialsDTO credentialsDTO = new CredentialsDTO(login, password);
        TokenDTO token = given()
                .contentType("application/json")
                .body(credentialsDTO)
                .when()
                .post("http://localhost:8080/api/auth/authenticate")
                .then()
                .statusCode(200).extract().body().as(TokenDTO.class);
        return token.getToken();
    }


    @Test
    @Transactional(transactionManager = "mokTransactionManager", isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
    public void testGetAllUsers() {

        List<UserWithPersonalDataAccessLevelDTO> users = given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .when()
                .get("http://localhost:8080/api/users")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().jsonPath().getList(".", UserWithPersonalDataAccessLevelDTO.class);
        assertEquals(userMokRepository.findAll().size(), users.size());
    }

    @Test
    public void testGetUserByIdButIdNotFound() {

        given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .when()
                .get("http://localhost:8080/api/users/-10")
                .then()
                .statusCode(404);
    }

    @Test
    public void testGetUserById() {

        UserWithPersonalDataAccessLevelDTO user = given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .when()
                .get("http://localhost:8080/api/users/-1")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().as(UserWithPersonalDataAccessLevelDTO.class);
        assertEquals(user.getLogin(), "jkowalski");
        assertEquals(user.getEmail(), "kowal@example.com");
        assertEquals(user.getIsActive(), true);
        assertEquals(user.getIsVerified(), true);
    }


    @Test
    @Transactional(transactionManager = "mokTransactionManager", isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
    public void testRegisterClient() throws AbstractAppException {
        RegistrationDTO registrationDTO = new RegistrationDTO("mrpawrob", "mrpawrob@gmail.com", "Test123!");
        RegistrationDTO loginTaken = new RegistrationDTO("mrpawrob", "mrpawrob@gmail.com", "Test123!");
        RegistrationDTO emailTaken = new RegistrationDTO("nottaken", "mrpawrob@gmail.com", "Test123!");
        RegistrationDTO tooShortPassword = new RegistrationDTO("nottaken", "nottaken@gmail.com", "Test123");

        UserWithPersonalDataAccessLevelDTO user = given()
                .contentType("application/json")
                .body(registrationDTO)
                .when()
                .post("http://localhost:8080/api/users/register")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().as(UserWithPersonalDataAccessLevelDTO.class);
        assertEquals(user.getLogin(), "mrpawrob");
        assertEquals(user.getEmail(), "mrpawrob@gmail.com");
        assertEquals(user.getIsActive(), true);
        assertEquals(user.getIsVerified(), false);


        UserEntity userEntity = userMokRepository.findByLogin("mrpawrob")
                .orElseThrow(() -> UserNotFoundAppException.createUserWithProvidedLoginNotFoundException("mrpawrob"));

        UserWithAccessLevelDTO userWithAccessLevelDTO = given()
                .contentType("application/json")
                .when()
                .put("http://localhost:8080/api/users/verify?username=mrpawrob&token=" + userEntity.getVerifyToken())
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().as(UserWithAccessLevelDTO.class);
        assertEquals(userWithAccessLevelDTO.getLogin(), "mrpawrob");
        assertEquals(userWithAccessLevelDTO.getEmail(), "mrpawrob@gmail.com");
        assertEquals(userWithAccessLevelDTO.getIsActive(), true);
        assertEquals(userWithAccessLevelDTO.getIsVerified(), true);

        given()
                .contentType("application/json")
                .when()
                .put("http://localhost:8080/api/users/verify?username=notexist&token=" + userEntity.getVerifyToken())
                .then()
                .statusCode(404);

        given()
                .contentType("application/json")
                .when()
                .put("http://localhost:8080/api/users/verify?username=mrpawrob&token=" + userEntity.getVerifyToken())
                .then()
                .statusCode(409);

        given()
                .contentType("application/json")
                .body(loginTaken)
                .when()
                .post("http://localhost:8080/api/users/register")
                .then()
                .statusCode(400);


        given()
                .contentType("application/json")
                .body(emailTaken)
                .when()
                .post("http://localhost:8080/api/users/register")
                .then()
                .statusCode(400);

        given()
                .contentType("application/json")
                .body(tooShortPassword)
                .when()
                .post("http://localhost:8080/api/users/register")
                .then()
                .statusCode(400);
    }

    @Test
    public void testUpdateOwnUserPersonalData() {

        PersonalDataDTO personalDataDTO = new PersonalDataDTO("Paweł", "Bucki", "111222333", "PL", true);
        UserWithPersonalDataDTO user = given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .body(personalDataDTO)
                .when()
                .put("http://localhost:8080/api/users/update/-3")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().as(UserWithPersonalDataDTO.class);
        assertEquals(user.getPersonalData().getName(), "Paweł");
        assertEquals(user.getPersonalData().getSurname(), "Bucki");
        assertEquals(user.getPersonalData().getPhoneNumber(), "111222333");
        assertEquals(user.getPersonalData().getGender(), true);
        assertEquals(user.getPersonalData().getLanguage(), "PL");


    }

    @Test
    public void testUpdateOtherUserPersonalData() {

        PersonalDataDTO personalDataDTO = new PersonalDataDTO("Paweł", "Bucki", "111222333", "PL", true);
        given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .body(personalDataDTO)
                .when()
                .put("http://localhost:8080/api/users/update/-4")
                .then()
                .statusCode(403);


    }

    @Test
    public void testChangePassword() {

        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO("Test123!", "Pbucki123!");
        ChangePasswordDTO changePasswordDTORollback = new ChangePasswordDTO("Pbucki123!", "Test123!");
        given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .body(changePasswordDTO)
                .when()
                .put("http://localhost:8080/api/users/change_password")
                .then()
                .statusCode(200);

        given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Test123!"))
                .contentType("application/json")
                .body(changePasswordDTORollback)
                .when()
                .put("http://localhost:8080/api/users/change_password")
                .then()
                .statusCode(200);

    }

    @Test
    public void testChangePasswordBadPassword() {

        ChangePasswordDTO badPassword = new ChangePasswordDTO("Test123!", "Pbucki1234!");
        ChangePasswordDTO passwordSameAsBefore = new ChangePasswordDTO("Pbucki123!", "Pbucki123!");
        given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .body(badPassword)
                .when()
                .put("http://localhost:8080/api/users/change_password")
                .then()
                .statusCode(401);

        given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .body(passwordSameAsBefore)
                .when()
                .put("http://localhost:8080/api/users/change_password")
                .then()
                .statusCode(401);

    }

    @Test
    public void testDeleteOwnAccount() {
        given().headers(
                        "Authorization",
                        "Bearer " + getToken("anowak", "Nowak123!"))
                .contentType("application/json")
                .body(new PasswordDTO("Nowak123!"))
                .when()
                .delete("http://localhost:8080/api/users/delete/-2")
                .then()
                .statusCode(200);

        UserWithPersonalDataAccessLevelDTO user = given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .when()
                .get("http://localhost:8080/api/users/-2")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().as(UserWithPersonalDataAccessLevelDTO.class);
        assertEquals(user.getLogin(), "#anowak");
        assertEquals(user.getEmail(), "#nowak@example.com");
        assertEquals(user.getIsActive(), false);
    }

    @Test
    public void testDeleteOwnAccountFail() {
        given().headers(
                        "Authorization",
                        "Bearer " + getToken("jkowalski", "Kowal123!"))
                .contentType("application/json")
                .body(new PasswordDTO("Kowal1234!"))
                .when()
                .delete("http://localhost:8080/api/users/delete/-1")
                .then()
                .statusCode(401);

        given().headers(
                        "Authorization",
                        "Bearer " + getToken("jkowalski", "Kowal123!"))
                .contentType("application/json")
                .body(new PasswordDTO("Nowak123!"))
                .when()
                .delete("http://localhost:8080/api/users/delete/-3")
                .then()
                .statusCode(403);
    }

    @Test
    public void testDeactivateOrActivateAccount() {
        UserWithAccessLevelDTO user = given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .when()
                .put("http://localhost:8080/api/users/deactivate/-4")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().as(UserWithAccessLevelDTO.class);

        assertEquals(user.getIsActive(), false);

        UserWithAccessLevelDTO userActivated = given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .when()
                .put("http://localhost:8080/api/users/activate/-4")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().as(UserWithAccessLevelDTO.class);

        assertEquals(userActivated.getIsActive(), true);
    }

    @Test
    public void testDeactivateOrActivateAccountFail() {
        given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .when()
                .put("http://localhost:8080/api/users/deactivate/100")
                .then()
                .statusCode(404);

        given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .when()
                .put("http://localhost:8080/api/users/activate/100")
                .then()
                .statusCode(404);
    }

    @Test
    @Transactional(transactionManager = "mokTransactionManager", isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
    public void testChangeEmail() throws UserNotFoundAppException {
        UserDTO user = given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .body(new EmailDTO("pbuckichange@example.com"))
                .when()
                .get("http://localhost:8080/api/users/request_change_email")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().as(UserDTO.class);

        UserEntity userEntity = userMokRepository.findByLogin("pbucki")
                .orElseThrow(() -> UserNotFoundAppException.createUserWithProvidedLoginNotFoundException("pbucki"));

        UserDTO userAfterChange = given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .when()
                .get("http://localhost:8080/api/users/change_email?token=" + userEntity.getEmailResetToken() + "&email=pbuckichange@example.com")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().as(UserDTO.class);

        assertEquals(userAfterChange.getEmail(), "pbuckichange@example.com");
    }

    @Test
    @Transactional(transactionManager = "mokTransactionManager", isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
    public void testResetPassword() throws UserNotFoundAppException {
        UserDTO user = given()
                .contentType("application/json")
                .body(new EmailDTO("jdoe@example.com"))
                .when()
                .get("http://localhost:8080/api/users/request_reset_password")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().as(UserDTO.class);

        CredentialsDTO credentialsDTO = new CredentialsDTO("johndoe", "Jdoe123!");
        given()
                .contentType("application/json")
                .body(credentialsDTO)
                .when()
                .post("http://localhost:8080/api/auth/authenticate")
                .then()
                .statusCode(200);


        UserEntity userEntity = userMokRepository.findByEmail("jdoe@example.com")
                .orElseThrow(() -> UserNotFoundAppException.createUserWithProvidedEmailNotFoundException("jdoe@example.com"));

        UserDTO userAfterChange = given()
                .contentType("application/json")
                .body(new ResetPasswordDTO("Test1234!", "Test1234!"))
                .when()
                .get("http://localhost:8080/api/users/reset_password?id=-5&token=" + userEntity.getPasswordResetToken())
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().as(UserDTO.class);

        CredentialsDTO credentialsDTO2 = new CredentialsDTO("johndoe", "Test1234!");
        given()
                .contentType("application/json")
                .body(credentialsDTO2)
                .when()
                .post("http://localhost:8080/api/auth/authenticate")
                .then()
                .statusCode(200);
    }
}

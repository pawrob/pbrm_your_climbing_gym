package pl.ftims.ias.perfectbeta;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ftims.ias.perfectbeta.dto.CredentialsDTO;
import pl.ftims.ias.perfectbeta.dto.TokenDTO;
import pl.ftims.ias.perfectbeta.dto.user_dtos.RegistrationDTO;
import pl.ftims.ias.perfectbeta.dto.user_dtos.UserWithAccessLevelDTO;
import pl.ftims.ias.perfectbeta.dto.user_dtos.UserWithPersonalDataAccessLevelDTO;
import pl.ftims.ias.perfectbeta.exceptions.AbstractAppException;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ManagerEndpointTest {

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
    public void testRegisterClient() throws AbstractAppException {
        RegistrationDTO registrationDTO = new RegistrationDTO("new_manager", "new_manager@example.com", "Test123!");

        UserWithPersonalDataAccessLevelDTO user = given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .body(registrationDTO)
                .when()
                .post("http://localhost:8080/api/managers/register")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().as(UserWithPersonalDataAccessLevelDTO.class);
        assertEquals(user.getLogin(), "new_manager");
        assertEquals(user.getEmail(), "new_manager@example.com");
        assertEquals(user.getIsActive(), true);
        assertEquals(user.getIsVerified(), true);
        assertEquals(user.getAccessLevels().get(0).getAccessLevel(), "MANAGER");

        System.out.println(getToken("new_manager", "Test123!"));

    }

    @Test
    public void testDeactivateOrActivateManager() {
        UserWithAccessLevelDTO user = given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .when()
                .put("http://localhost:8080/api/managers/deactivate/-1")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().as(UserWithAccessLevelDTO.class);

        assertEquals(user.getAccessLevels().get(0).getIsActive(), false);

        UserWithAccessLevelDTO userActivated = given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .when()
                .put("http://localhost:8080/api/managers/activate/-1")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().as(UserWithAccessLevelDTO.class);

        assertEquals(userActivated.getAccessLevels().get(0).getIsActive(), true);
    }

    @Test
    public void testDeactivateOrActivateManagerFail() {
        given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .when()
                .put("http://localhost:8080/api/managers/deactivate/-100")
                .then()
                .statusCode(404);

        given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .when()
                .put("http://localhost:8080/api/managers/activate/-100")
                .then()
                .statusCode(404);


    }
}

package pl.ftims.ias.your_climbing_gym;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.ftims.ias.your_climbing_gym.dto.CredentialsDTO;
import pl.ftims.ias.your_climbing_gym.dto.TokenDTO;
import pl.ftims.ias.your_climbing_gym.dto.user_dtos.RegistrationDTO;
import pl.ftims.ias.your_climbing_gym.entities.UserEntity;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class BootAppTests {


    public String getToken() {
        CredentialsDTO credentialsDTO = new CredentialsDTO("pbucki", "Pbucki123!");
        TokenDTO token = given()
                .contentType("application/json")
                .body(credentialsDTO)
                .when()
                .post("https://localhost:8080/api/auth/authenticate")
                .then()
                .statusCode(200).extract().body().as(TokenDTO.class);
        return token.getToken();
    }


    @Test
    public void testGetAllUsers() {

        List<UserEntity> users = given().headers(
                "Authorization",
                "Bearer " + getToken(),
                "Content-Type",
                ContentType.JSON,
                "Accept",
                ContentType.JSON)
                .when()
                .get("https://localhost:8080/api/users")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().jsonPath().getList(".", UserEntity.class);
        assertEquals(5, users.size());
    }

    @Test
    public void testGetUserById() {

        UserEntity user = given().headers(
                "Authorization",
                "Bearer " + getToken(),
                "Content-Type",
                ContentType.JSON,
                "Accept",
                ContentType.JSON)
                .when()
                .get("https://localhost:8080/api/users/-1")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().as(UserEntity.class);
        assertEquals(user.getLogin(), "jkowalski");
        assertEquals(user.getEmail(), "kowal@example.com");
        assertEquals(user.getActive(), true);
        assertEquals(user.getVerified(), true);
    }

    @Test
    public void testGetUserByIdButIdNotFound() {

        given().headers(
                "Authorization",
                "Bearer " + getToken(),
                "Content-Type",
                ContentType.JSON,
                "Accept",
                ContentType.JSON)
                .when()
                .get("https://localhost:8080/api/users/-10")
                .then()
                .statusCode(404);
    }

    @Test
    public void testRegisterClient() {
        RegistrationDTO registrationDTO = new RegistrationDTO("mrpawrob", "mrpawrob@gmail.com", "Test123!");

        UserEntity user = given().headers(
                "Authorization",
                "Bearer " + getToken(),
                "Content-Type",
                ContentType.JSON,
                "Accept",
                ContentType.JSON)
                .body(registrationDTO)
                .when()
                .post("https://localhost:8080/api/users/register")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().as(UserEntity.class);
        assertEquals(user.getLogin(), "mrpawrob");
        assertEquals(user.getEmail(), "mrpawrob@gmail.com");
        assertEquals(user.getActive(), true);
        assertEquals(user.getVerified(), false);
    }


}

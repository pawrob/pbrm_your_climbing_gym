package pl.ftims.ias.perfectbeta;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ftims.ias.perfectbeta.dto.CredentialsDTO;
import pl.ftims.ias.perfectbeta.dto.TokenDTO;
import pl.ftims.ias.perfectbeta.dto.routes_dtos.ClimbingGymDTO;
import pl.ftims.ias.perfectbeta.dto.routes_dtos.ClimbingGymWithDetailsDTO;
import pl.ftims.ias.perfectbeta.dto.routes_dtos.ClimbingGymWithMaintainersDTO;
import pl.ftims.ias.perfectbeta.dto.routes_dtos.GymDetailsDTO;
import pl.ftims.ias.perfectbeta.mok.repositories.UserMokRepository;
import pl.ftims.ias.perfectbeta.mos.repositories.ClimbingGymRepository;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ClimbingGymEndpointTest {

    ClimbingGymRepository gym;
    UserMokRepository userMokRepository;

    @Autowired
    public ClimbingGymEndpointTest(ClimbingGymRepository gym, UserMokRepository userMokRepository) {
        this.gym = gym;
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
    @Transactional(transactionManager = "mosTransactionManager", isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
    public void testGetVerifiedGyms() {

        List<ClimbingGymDTO> gyms = given()
                .contentType("application/json")
                .when()
                .get("http://localhost:8080/api/gym/verified/all")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract().path("content");
        assertEquals(gym.findAllVerified(Pageable.unpaged()).getTotalElements(), gyms.size());
    }

    @Test
    public void testGetVerifiedGymById() {

        ClimbingGymWithDetailsDTO gym = given()
                .contentType("application/json")
                .when()
                .get("http://localhost:8080/api/gym/verified/-3")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().as(ClimbingGymWithDetailsDTO.class);

        assertEquals(gym.getGymName(), "testGym_verified");
    }

    @Test
    public void testGetVerifiedGymByIdFail() {

        given()
                .contentType("application/json")
                .when()
                .get("http://localhost:8080/api/gym/verified/-100")
                .then()
                .statusCode(404);
    }

    @Test
    public void testGetOwnedGyms() {

        List<ClimbingGymDTO> gyms = given().headers(
                        "Authorization",
                        "Bearer " + getToken("jkowalski", "Kowal123!"))
                .contentType("application/json")
                .when()
                .get("http://localhost:8080/api/gym/owned_gyms")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract().path("content");
        assertEquals(4, gyms.size());
    }

    @Test
    @Transactional(transactionManager = "mosTransactionManager", isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
    public void testGetAllGyms() {

        List<ClimbingGymDTO> gyms = given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .when()
                .get("http://localhost:8080/api/gym/all")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract().path("content");
        assertEquals(gym.findAll(Pageable.unpaged()).getTotalElements(), gyms.size());
    }

    @Test
    public void testGetGymById() {

        ClimbingGymWithDetailsDTO gym = given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .when()
                .get("http://localhost:8080/api/gym/-2")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().as(ClimbingGymWithDetailsDTO.class);

        assertEquals(gym.getGymName(), "testGym_toVerify");
    }

    @Test
    public void testGetGymByIdFail() {

        given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .when()
                .get("http://localhost:8080/api/gym/-100")
                .then()
                .statusCode(404);
    }

    @Test
    public void testRegisterGymAndVerifyGymThenCloseGym() {

        ClimbingGymWithDetailsDTO gym = given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .when()
                .post("http://localhost:8080/api/gym/register/gym_registrationTest")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().as(ClimbingGymWithDetailsDTO.class);

        assertEquals(gym.getGymName(), "gym_registrationTest");
        assertEquals(gym.getStatus().toString(), "UNVERIFIED");

        ClimbingGymDTO gymAfterVerify = given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .when()
                .put("http://localhost:8080/api/gym/verify/1")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().as(ClimbingGymDTO.class);

        assertEquals(gymAfterVerify.getGymName(), "gym_registrationTest");
        assertEquals(gymAfterVerify.getStatus().toString(), "VERIFIED");

        ClimbingGymDTO gymAfterClose = given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .when()
                .put("http://localhost:8080/api/gym/close/1")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().as(ClimbingGymDTO.class);

        assertEquals(gymAfterClose.getGymName(), "gym_registrationTest");
        assertEquals(gymAfterClose.getStatus().toString(), "CLOSED");

        given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .when()
                .post("http://localhost:8080/api/gym/register/gym_registrationTest")
                .then()
                .statusCode(400);

        given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .when()
                .put("http://localhost:8080/api/gym/verify/-100")
                .then()
                .statusCode(404);
        given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .when()
                .put("http://localhost:8080/api/gym/close/-100")
                .then()
                .statusCode(404);

    }

    @Test
    public void testAddMaintainer() {

        ClimbingGymWithMaintainersDTO gymWithMaintainersDTO = given().headers(
                        "Authorization",
                        "Bearer " + getToken("jkowalski", "Kowal123!"))
                .contentType("application/json")
                .when()
                .put("http://localhost:8080/api/gym/-2/add_maintainer/-3")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().as(ClimbingGymWithMaintainersDTO.class);

        assertEquals(gymWithMaintainersDTO.getMaintainerDTO().get(0).getMaintainerId(), Long.valueOf(-3));
    }

    @Test
    public void testAddMaintainerFail() {

        given().headers(
                        "Authorization",
                        "Bearer " + getToken("jkowalski", "Kowal123!"))
                .contentType("application/json")
                .when()
                .put("http://localhost:8080/api/gym/-100/add_maintainer/-3")
                .then()
                .statusCode(404);

        given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .when()
                .put("http://localhost:8080/api/gym/-2/add_maintainer/-3")
                .then()
                .statusCode(403);

        given().headers(
                        "Authorization",
                        "Bearer " + getToken("jkowalski", "Kowal123!"))
                .contentType("application/json")
                .when()
                .put("http://localhost:8080/api/gym/-2/add_maintainer/-4")
                .then()
                .statusCode(403);
    }

    @Test
    public void testEditGymDetails() {
        GymDetailsDTO details = new GymDetailsDTO("ENGLAND", "LONDON", "Main Street", "7312", "Best London Gym");
        ClimbingGymWithDetailsDTO gymWithDetailsDTO = given().headers(
                        "Authorization",
                        "Bearer " + getToken("jkowalski", "Kowal123!"))
                .contentType("application/json")
                .body(details)
                .when()
                .put("http://localhost:8080/api/gym/edit_gym_details/-2")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().as(ClimbingGymWithDetailsDTO.class);

        assertEquals(gymWithDetailsDTO.getGymDetailsDTO().getDescription(), "Best London Gym");
        assertEquals(gymWithDetailsDTO.getGymDetailsDTO().getCity(), "LONDON");
        assertEquals(gymWithDetailsDTO.getGymDetailsDTO().getCountry(), "ENGLAND");
        assertEquals(gymWithDetailsDTO.getGymDetailsDTO().getStreet(), "Main Street");
        assertEquals(gymWithDetailsDTO.getGymDetailsDTO().getNumber(), "7312");
    }

    @Test
    public void testEditGymDetailsFail() {
        GymDetailsDTO details = new GymDetailsDTO("ENGLAND", "LONDON", "Main Street", "7312", "Best London Gym");
        given().headers(
                        "Authorization",
                        "Bearer " + getToken("jkowalski", "Kowal123!"))
                .contentType("application/json")
                .body(details)
                .when()
                .put("http://localhost:8080/api/gym/edit_gym_details/-100")
                .then()
                .statusCode(404);


        given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .body(details)
                .when()
                .put("http://localhost:8080/api/gym/edit_gym_details/-2")
                .then()
                .statusCode(403);
    }

    @Test
    public void testGetMaintainedGyms() {
        List<ClimbingGymDTO> maintainerDTOList = given().headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .when()
                .get("http://localhost:8080/api/gym/maintained_gyms")
                .then()
                .statusCode(200)
                .extract().path("content");
        assertEquals(2, maintainerDTOList.size());


        List<ClimbingGymDTO> maintainerDTOListEmpty = given().headers(
                        "Authorization",
                        "Bearer " + getToken("jkowalski", "Kowal123!"))
                .contentType("application/json")
                .when()
                .get("http://localhost:8080/api/gym/maintained_gyms")
                .then()
                .statusCode(200)
                .extract().path("content");
        assertEquals(0, maintainerDTOListEmpty.size());

    }
}

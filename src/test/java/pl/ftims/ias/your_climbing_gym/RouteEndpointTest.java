package pl.ftims.ias.your_climbing_gym;


import com.amazonaws.services.ec2.model.Route;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.ftims.ias.your_climbing_gym.dto.CredentialsDTO;
import pl.ftims.ias.your_climbing_gym.dto.TokenDTO;
import pl.ftims.ias.your_climbing_gym.dto.routes_dtos.ClimbingGymWithDetailsDTO;
import pl.ftims.ias.your_climbing_gym.dto.routes_dtos.RouteDTO;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class RouteEndpointTest {

    public String getToken(String login, String password) {
        CredentialsDTO credentialsDTO = new CredentialsDTO(login, password);
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
    public void addRouteTest(){


        RouteDTO routeByMaintainer = given().headers(
                "Authorization",
                "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .body(new RouteDTO("test route maintainer","5a",-3L))
                .when()
                .post("https://localhost:8080/api/route/add")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().as(RouteDTO.class);
        RouteDTO routeByOwner = given().headers(
                "Authorization",
                "Bearer " + getToken("jkowalski", "Kowal123!"))
                .contentType("application/json")
                .body(new RouteDTO("test route owner","5a",-3L))
                .when()
                .post("https://localhost:8080/api/route/add")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().as(RouteDTO.class);
    }
    @Test
    public void addRouteTestFail(){


        given().headers(
                "Authorization",
                "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .body(new RouteDTO("test route maintainer","5a",-2L))
                .when()
                .post("https://localhost:8080/api/route/add")
                .then()
                .statusCode(403);

        given().headers(
                "Authorization",
                "Bearer " + getToken("jkowalski", "Kowal123!"))
                .contentType("application/json")
                .body(new RouteDTO("test route owner","5a",-2L))
                .when()
                .post("https://localhost:8080/api/route/add")
                .then()
                .statusCode(403);
    }

    @Test
    public void removeRouteTest(){


        given()
                .headers(
                "Authorization",
                "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .when()
                .delete("https://localhost:8080/api/route/-3/remove/-2")
                .then()
                .statusCode(200);
        given()
                .headers(
                "Authorization",
                "Bearer " + getToken("jkowalski", "Kowal123!"))
                .contentType("application/json")
                .when()
                .delete("https://localhost:8080/api/route/-3/remove/-1")
                .then()
                .statusCode(200);
    }
    @Test
    public void removeRouteTestFail(){


        given()
                .headers(
                        "Authorization",
                        "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .when()
                .delete("https://localhost:8080/api/route/-2/remove/-2")
                .then()
                .statusCode(403);
        given()
                .headers(
                        "Authorization",
                        "Bearer " + getToken("jkowalski", "Kowal123!"))
                .contentType("application/json")
                .when()
                .delete("https://localhost:8080/api/route/-3/remove/-100")
                .then()
                .statusCode(404);
        given()
                .headers(
                        "Authorization",
                        "Bearer " + getToken("jkowalski", "Kowal123!"))
                .contentType("application/json")
                .when()
                .delete("https://localhost:8080/api/route/-100/remove/-2")
                .then()
                .statusCode(404);
    }
    @Test
    public void editRouteTest(){
        RouteDTO routeByMaintainer = given().headers(
                "Authorization",
                "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .body(new RouteDTO("test edit maintainer","5c",-3L))
                .when()
                .put("https://localhost:8080/api/route/-3/edit/-3")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().as(RouteDTO.class);
        RouteDTO routeByOwner = given().headers(
                "Authorization",
                "Bearer " + getToken("jkowalski", "Kowal123!"))
                .contentType("application/json")
                .body(new RouteDTO("test edit owner","5b",-3L))
                .when()
                .put("https://localhost:8080/api/route/-3/edit/-3")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body().as(RouteDTO.class);
    }

    @Test
    public void editRouteTestFail(){
        given().headers(
                "Authorization",
                "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .body(new RouteDTO("test edit maintainer","5c",-3L))
                .when()
                .put("https://localhost:8080/api/route/-100/edit/-2")
                .then()
                .statusCode(404);
        given().headers(
                "Authorization",
                "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .body(new RouteDTO("test edit maintainer","5c",-3L))
                .when()
                .put("https://localhost:8080/api/route/-3/edit/-100")
                .then()
                .statusCode(404);
        given().headers(
                "Authorization",
                "Bearer " + getToken("pbucki", "Pbucki123!"))
                .contentType("application/json")
                .body(new RouteDTO("test edit maintainer","5c",-3L))
                .when()
                .put("https://localhost:8080/api/route/-2/edit/-2")
                .then()
                .statusCode(403);

    }

}

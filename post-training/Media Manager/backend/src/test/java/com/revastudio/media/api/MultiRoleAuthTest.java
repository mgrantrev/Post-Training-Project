package com.revastudio.media.api;

import com.revastudio.media.entity.User;
import com.revastudio.media.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MultiRoleAuthTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        userRepository.deleteAll();
    }

    @Test
    public void testLoginWithMultipleRoles() {
        User user = new User();
        user.setUsername("multiuser");
        user.setPassword(passwordEncoder.encode("password"));
        user.getRoles().add("CUSTOMER");
        user.getRoles().add("EMPLOYEE");
        user.setCustomerId(1);
        user.setEmployeeId(1);
        userRepository.save(user);

        // Verification via the login endpoint
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("username", "multiuser", "password", "password"))
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue());

        // We could also verify the JWT content here if we wanted to be more explicit,
        // but the fact it returns 200 and a token is a good start.
        // A better test would be to use that token to access both customer and employee
        // endpoints.
    }

    @Test
    public void testAccessBothDashboards() {
        User user = new User();
        user.setUsername("multiuser");
        user.setPassword(passwordEncoder.encode("password"));
        user.getRoles().add("CUSTOMER");
        user.getRoles().add("EMPLOYEE");
        user.setCustomerId(1);
        user.setEmployeeId(2);
        userRepository.save(user);

        String token = given()
                .contentType(ContentType.JSON)
                .body(Map.of("username", "multiuser", "password", "password"))
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .extract().path("token");

        // Access Customer endpoint
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/customers/1/tracks")
                .then()
                .statusCode(200);

        // Access Employee endpoint
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/employees/2/tickets")
                .then()
                .statusCode(200);
    }
}

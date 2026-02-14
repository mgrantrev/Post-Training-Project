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
public class CustomerApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String token;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;

        if (userRepository.findByUsername("customer1").isEmpty()) {
            User user = new User();
            user.setUsername("customer1");
            user.setPassword(passwordEncoder.encode("password"));
            user.getRoles().add("CUSTOMER");
            user.setCustomerId(1);
            userRepository.save(user);
        }

        token = given()
                .contentType(ContentType.JSON)
                .body(Map.of("username", "customer1", "password", "password"))
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    @Test
    public void testGetOwnedTracks() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/customers/1/tracks")
                .then()
                .statusCode(200)
                .body("$", hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    public void testGetSupportTickets() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/customers/1/tickets")
                .then()
                .statusCode(200)
                .body("$", hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    public void testCreateSupportTicket() {
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(Map.of("subject", "API Test Ticket", "body", "Testing ticket creation via RestAssured"))
                .when()
                .post("/api/customers/1/tickets")
                .then()
                .statusCode(200)
                .body("subject", equalTo("API Test Ticket"))
                .body("status", equalTo("OPEN"));
    }
}

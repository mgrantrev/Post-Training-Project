package com.revastudio.media.api;

import com.revastudio.media.repository.CustomerRepository;
import com.revastudio.media.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RegisterApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        userRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void testRegisterSuccess() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "username", "newuser",
                        "password", "password",
                        "firstName", "John",
                        "lastName", "Doe",
                        "email", "john.doe@example.com"))
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(200)
                .body("username", equalTo("newuser"))
                .body("customerId", notNullValue());

        assertTrue(userRepository.findByUsername("newuser").isPresent());
        assertTrue(userRepository.findByUsername("newuser").get().getRoles().contains("CUSTOMER"));
    }

    @Test
    public void testRegisterDuplicateUsername() {
        // First registration
        testRegisterSuccess();

        // Second registration with same username
        given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "username", "newuser",
                        "password", "newpassword",
                        "firstName", "Jane",
                        "lastName", "Smith",
                        "email", "jane.smith@example.com"))
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(400)
                .body(containsString("Username already exists"));
    }
}

package com.revastudio.media.api;

import com.revastudio.media.entity.SupportTicket;
import com.revastudio.media.entity.User;
import com.revastudio.media.repository.SupportTicketRepository;
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
public class EmployeeApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SupportTicketRepository ticketRepository;

    private String token;
    private Integer employeeId;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;

        User employee = userRepository.findByUsername("employee1").orElseGet(() -> {
            User u = new User();
            u.setUsername("employee1");
            u.setPassword(passwordEncoder.encode("password"));
            u.getRoles().add("EMPLOYEE");
            u.setEmployeeId(1);
            return userRepository.save(u);
        });
        employeeId = employee.getEmployeeId();

        if (ticketRepository.count() == 0) {
            SupportTicket ticket = new SupportTicket();
            ticket.setSubject("Seed Ticket");
            ticket.setBody("Seed body");
            ticket.setCustomerId(1);
            ticket.setEmployeeId(employeeId);
            ticketRepository.save(ticket);
        }

        token = given()
                .contentType(ContentType.JSON)
                .body(Map.of("username", "employee1", "password", "password"))
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    @Test
    public void testGetEmployeeSupportTickets() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/employees/" + employeeId + "/tickets")
                .then()
                .statusCode(200)
                .body("$", hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    public void testRespondToTicket() {
        Long ticketId = ticketRepository.findAll().get(0).getId();

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(Map.of("message", "I am looking into this.", "employeeId", employeeId))
                .when()
                .post("/api/employees/tickets/" + ticketId + "/responses")
                .then()
                .statusCode(200)
                .body("message", equalTo("I am looking into this."));
    }

    @Test
    public void testCloseTicket() {
        Long ticketId = ticketRepository.findAll().get(0).getId();

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .put("/api/employees/tickets/" + ticketId + "/close")
                .then()
                .statusCode(200);
    }
}

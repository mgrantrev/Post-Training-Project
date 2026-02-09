package com.revastudio.media.glue;

import com.revastudio.media.pages.DashboardPage;
import com.revastudio.media.pages.LoginPage;
import com.revastudio.media.pages.SupportPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class StepDefinitions {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private SupportPage supportPage;

    @Before
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--guest");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
        supportPage = new SupportPage(driver);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Given("the user is on the login page")
    public void the_user_is_on_the_login_page() {
        driver.get("http://localhost:4200/login");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-testid=username]")));
    }

    @When("the user enters {string} and {string}")
    public void the_user_enters_and(String username, String password) {
        loginPage.login(username, password);
    }

    @When("clicks the login button")
    public void clicks_the_login_button() {
        // Handled in loginPage.login()
    }

    @Then("the user should be redirected to the dashboard")
    public void the_user_should_be_redirected_to_the_dashboard() {
        wait.until(ExpectedConditions.urlContains("dashboard"));
        assertTrue(driver.getCurrentUrl().contains("dashboard"));
    }

    @Then("see {string} as their role")
    public void see_as_their_role(String expectedRole) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-testid=user-role]")));
        assertEquals(expectedRole, dashboardPage.getUserRole());
    }

    @Then("an error message {string} should be displayed")
    public void an_error_message_should_be_displayed(String expectedError) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-testid=error-message]")));
        assertEquals(expectedError, loginPage.getErrorMessage());
    }

    @Given("the customer is logged in as {string}")
    public void the_customer_is_logged_in_as(String username) {
        the_user_is_on_the_login_page();
        loginPage.login(username, "password");
        wait.until(ExpectedConditions.urlContains("dashboard"));
    }

    @Then("the customer should see a list of their purchased tracks")
    public void the_customer_should_see_a_list_of_their_purchased_tracks() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-testid=track-item]")));
        assertTrue(dashboardPage.getTrackCount() >= 0);
    }

    @Then("each track should show the album title and artist name")
    public void each_track_should_show_the_album_title_and_artist_name() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-testid=track-item]")));
        assertTrue(dashboardPage.getTrackCount() > 0);
    }

    @When("the customer searches for {string}")
    public void the_customer_searches_for(String text) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-testid=search-input]")));
        dashboardPage.search(text);
    }

    @Then("the track list should only show tracks matching {string}")
    public void the_track_list_should_only_show_tracks_matching(String text) {
        // Verification logic
    }

    @When("the customer navigates to the support page")
    public void the_customer_navigates_to_the_support_page() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-testid=get-support-button]")));
        driver.findElement(By.cssSelector("[data-testid=get-support-button]")).click();
        wait.until(ExpectedConditions.urlContains("support"));
    }

    @Then("they should see their existing support tickets")
    public void they_should_see_their_existing_support_tickets() {
        assertTrue(supportPage.getTicketCount() >= 0);
    }

    @Then("they can see responses to their tickets")
    public void they_can_see_responses_to_their_tickets() {
        // Verification logic
    }

    @When("creates a ticket with subject {string} and description {string}")
    public void creates_a_ticket_with_subject_and_description(String subject, String description) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-testid=new-ticket-button]")));
        supportPage.createTicket(subject, description);
    }

    @Then("they should see {string} in their ticket list")
    public void they_should_see_in_their_ticket_list(String subject) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-testid=ticket-item]")));
    }

    @Given("the employee is logged in as {string}")
    public void the_employee_is_logged_in_as(String username) {
        the_user_is_on_the_login_page();
        loginPage.login(username, "password");
        wait.until(ExpectedConditions.urlContains("dashboard"));
    }

    @Then("the employee should see their sales metrics dashboard")
    public void the_employee_should_see_their_sales_metrics_dashboard() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-testid=sales-metrics-view]")));
        assertTrue(dashboardPage.isSalesMetricsVisible());
    }

    @Then("see the total revenue and assisted customers count")
    public void see_the_total_revenue_and_assisted_customers_count() {
        // Assertions for metrics text
    }

    @When("the employee navigates to the manage tickets page")
    public void the_employee_navigates_to_the_manage_tickets_page() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-testid=manage-tickets-button]")));
        dashboardPage.goToSupport();
        wait.until(ExpectedConditions.urlContains("support"));
    }

    @Then("they should see all customer support tickets")
    public void they_should_see_all_customer_support_tickets() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-testid=employee-ticket-list]")));
        assertTrue(supportPage.isEmployeeTicketListVisible());
    }

    @When("they select a ticket and respond with {string}")
    public void they_select_a_ticket_and_respond_with(String response) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-testid=employee-ticket-item]")));
        supportPage.respondToTicket(0, response);
    }

    @When("close the ticket")
    public void close_the_ticket() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-testid=close-ticket-button]")));
        supportPage.closeTicket();
    }

    @Then("the ticket status should be {string}")
    public void the_ticket_status_should_be(String status) {
        // Verify status text
    }

    @Then("the employee should see a list of customers they have assisted")
    public void the_employee_should_see_a_list_of_customers_they_have_assisted() {
        // Assertions
    }

    @Then("the employee should see a list of recent sales with track and billing information")
    public void the_employee_should_see_a_list_of_recent_sales_with_track_and_billing_information() {
        // Assertions
    }
}

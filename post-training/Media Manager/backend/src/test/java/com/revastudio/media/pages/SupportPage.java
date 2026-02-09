package com.revastudio.media.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class SupportPage {
    private WebDriver driver;

    @FindBy(css = "[data-testid='new-ticket-button']")
    private WebElement newTicketButton;

    @FindBy(css = "[data-testid='ticket-subject']")
    private WebElement ticketSubject;

    @FindBy(css = "[data-testid='ticket-description']")
    private WebElement ticketDescription;

    @FindBy(css = "[data-testid='submit-ticket-button']")
    private WebElement submitTicketButton;

    @FindBy(css = "[data-testid='ticket-item']")
    private List<WebElement> ticketItems;

    @FindBy(css = "[data-testid='employee-ticket-list']")
    private WebElement employeeTicketList;

    @FindBy(css = "[data-testid='employee-ticket-item']")
    private List<WebElement> employeeTicketItems;

    @FindBy(css = "[data-testid='ticket-response-input']")
    private WebElement responseInput;

    @FindBy(css = "[data-testid='submit-response-button']")
    private WebElement submitResponseButton;

    @FindBy(css = "[data-testid='close-ticket-button']")
    private WebElement closeTicketButton;

    public SupportPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void createTicket(String subject, String description) {
        newTicketButton.click();
        ticketSubject.sendKeys(subject);
        ticketDescription.sendKeys(description);
        submitTicketButton.click();
    }

    public int getTicketCount() {
        return ticketItems.size();
    }

    public boolean isEmployeeTicketListVisible() {
        return employeeTicketList.isDisplayed();
    }

    public void respondToTicket(int index, String response) {
        employeeTicketItems.get(index).click();
        responseInput.sendKeys(response);
        submitResponseButton.click();
    }

    public void closeTicket() {
        closeTicketButton.click();
    }
}

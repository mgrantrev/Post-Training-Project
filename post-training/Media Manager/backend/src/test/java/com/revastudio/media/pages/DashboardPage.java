package com.revastudio.media.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class DashboardPage {
    private WebDriver driver;

    @FindBy(css = "[data-testid=user-role]")
    private WebElement userRole;

    @FindBy(css = "[data-testid=logout-button]")
    private WebElement logoutButton;

    @FindBy(css = "[data-testid=track-item]")
    private List<WebElement> trackItems;

    @FindBy(css = "[data-testid=search-input]")
    private WebElement searchInput;

    @FindBy(css = "[data-testid=sales-metrics-view]")
    private WebElement salesMetricsView;

    @FindBy(css = "[data-testid=manage-tickets-button]")
    private WebElement manageTicketsButton;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getUserRole() {
        return userRole.getText();
    }

    public void logout() {
        logoutButton.click();
    }

    public int getTrackCount() {
        return trackItems.size();
    }

    public void search(String text) {
        searchInput.sendKeys(text);
    }

    public boolean isSalesMetricsVisible() {
        return salesMetricsView.isDisplayed();
    }

    public void goToSupport() {
        manageTicketsButton.click();
    }
}

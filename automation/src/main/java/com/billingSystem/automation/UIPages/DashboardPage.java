package com.billingSystem.automation.UIPages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class DashboardPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By cardContainer = By.cssSelector("div.grid > div");
    private By cardTitle = By.cssSelector(".card-header .text-lg");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /** Click tile by title */
    public void clickTile(String title) {
        wait.until(d -> driver.findElements(cardContainer).size() > 0);

        List<WebElement> cards = driver.findElements(cardContainer);
        for (WebElement card : cards) {
            WebElement t = card.findElement(cardTitle);
            if (t.getText().equalsIgnoreCase(title)) {
                card.click();
                return;
            }
        }
        throw new RuntimeException("Tile with title '" + title + "' not found");
    }
}

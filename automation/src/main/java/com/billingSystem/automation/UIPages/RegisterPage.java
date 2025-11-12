package com.billingSystem.automation.UIPages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegisterPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public RegisterPage(WebDriver driver) {
        if (driver == null) throw new IllegalArgumentException("Driver cannot be null");
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // SPA locators
    private By usernameField = By.cssSelector("input[name='username']");
    private By passwordField = By.cssSelector("input[name='password']");
    private By roleDropdown = By.cssSelector("select[name='role']");
    private By registerBtn = By.cssSelector("button[type='submit']");
    private By errorMsg = By.cssSelector("p.text-red-400");


    public void registerUser(String username, String password, String role) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).clear();
        driver.findElement(usernameField).sendKeys(username);

        driver.findElement(passwordField).clear();
        driver.findElement(passwordField).sendKeys(password);

        WebElement roleSelect = driver.findElement(roleDropdown);
        roleSelect.sendKeys(role);

        driver.findElement(registerBtn).click();
    }

    public String getErrorMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMsg)).getText();
        } catch (TimeoutException e) {
            return null;
        }
    }
}


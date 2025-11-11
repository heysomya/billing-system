package com.billingSystem.automation.UIPagesTest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class LogoutTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String BASE_URL = "http://localhost:5173";

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // LOGIN STEP
        driver.get(BASE_URL + "/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys("automationtest123");
        driver.findElement(By.name("password")).sendKeys("pass123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Wait for Dashboard
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1.text-3xl")));
    }

    @Test(description = "Verify Logout redirects to login page")
    public void testLogoutRedirectsToLogin() {
        // Wait for logout button and click
        WebElement logoutBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//span[text()='Logout']]")
        ));
        logoutBtn.click();

        // Wait for any element containing 'Login'
        WebElement loginHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'Login')]")
        ));
        Assert.assertTrue(loginHeading.getText().contains("Login"), "Login page heading not found");

        // Verify URL contains /login
        wait.until(ExpectedConditions.urlContains("/login"));
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/login"), "URL did not redirect to /login");
    }


    @AfterClass
    public void teardown() {
        if (driver != null) driver.quit();
    }
}


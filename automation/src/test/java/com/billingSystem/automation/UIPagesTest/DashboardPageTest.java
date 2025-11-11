package com.billingSystem.automation.UIPagesTest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class DashboardPageTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String BASE_URL = "http://localhost:5173";

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // -------- LOGIN STEP --------
        driver.get(BASE_URL + "/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys("automationtest123");
        driver.findElement(By.name("password")).sendKeys("pass123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    @Test(description = "Verify Dashboard is displayed after login")
    public void testDashboardOpened() {
        // Wait for Dashboard heading or container
        By dashboardHeading = By.cssSelector("h1.text-3xl"); // Assuming <h1> has text "Dashboard"
        wait.until(ExpectedConditions.visibilityOfElementLocated(dashboardHeading));

        String headingText = driver.findElement(dashboardHeading).getText();
        Assert.assertTrue(headingText.contains("Dashboard"), "Dashboard page did not open after login");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}

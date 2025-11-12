package com.billingSystem.automation.UIPagesTest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import org.testng.Assert;

import java.time.Duration;

public class UsersPageTest {

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

    @Test(description = "Verify /users page opens and headings are visible")
    public void testUsersPageOpens() {
        driver.get(BASE_URL + "/users");

        // Wait for Cashiers heading
        WebElement cashiersHeading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()='Cashiers']"))
        );
        Assert.assertTrue(cashiersHeading.isDisplayed(), "Cashiers heading not visible");

        // Wait for Customers heading
        WebElement customersHeading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()='Customers']"))
        );
        Assert.assertTrue(customersHeading.isDisplayed(), "Customers heading not visible");
    }

    @AfterClass
    public void teardown() {
        if (driver != null) driver.quit();
    }
}

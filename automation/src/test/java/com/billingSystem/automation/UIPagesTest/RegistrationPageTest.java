package com.billingSystem.automation.UIPagesTest;

import com.billingSystem.automation.UIPages.RegisterPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class RegistrationPageTest {

    private WebDriver driver;
    private RegisterPage registrationPage;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://localhost:5173/register"); // React SPA URL
        registrationPage = new RegisterPage(driver);
    }

    @Test(priority = 1, description = "Valid registration test for Admin user")
    public void testValidRegistration() {
        registrationPage.registerUser("automationtest1990", "pass123", "ADMIN");

        // Wait for navigation to /login after registration
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(webDriver -> webDriver.getCurrentUrl().contains("/login"));

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/login"),
                "Expected navigation to login page after successful registration");
    }


    @Test(priority = 2, description = "Invalid registration test")
    public void testInvalidRegistration() {
        registrationPage.registerUser("somya", "123", "CASHIER");

        // wait for error message
        String error = registrationPage.getErrorMessage();
        Assert.assertNotNull(error, "Expected registration failure message");
        Assert.assertEquals(error, "Registration failed", "Error message mismatch");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}

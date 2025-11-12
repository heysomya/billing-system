package com.billingSystem.automation.UIPagesTest;

import com.billingSystem.automation.UIPages.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class LoginPageTest {

    private WebDriver driver;
    private LoginPage loginPage;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://localhost:5173/login");
        loginPage = new LoginPage(driver);
    }
    @Test(priority = 1, description = "Invalid login")
    public void testInvalidLogin() {
        loginPage.loginUser("somya", "wrongpass");
        String error = loginPage.getErrorMessage();
        Assert.assertNotNull(error, "Expected login failure message");
        Assert.assertEquals(error, "Invalid Credentials", "Error message mismatch");
    }

    @Test(priority = 2, description = "Valid login")
    public void testValidLogin() {
        loginPage.loginUser("automationtest123", "pass123");
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Expected navigation to /dashboard after login");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}


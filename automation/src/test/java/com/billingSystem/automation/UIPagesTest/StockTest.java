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
import java.util.List;

public class StockTest {

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

    @Test(description = "Verify Stocks page opens")
    public void testStocksPageOpens() {
        // Navigate to Stocks page
        driver.get("http://localhost:5173/stock");

        // Wait for the page header (h1) or page-specific element
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement header = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1.text-3xl"))
        );

        // Assert the header contains 'Stock Management'
        Assert.assertTrue(header.getText().contains("Stock Management"),
                "Stocks page did not open");
    }

    @Test(description = "Enter text in search field and verify Search button is clickable on Stocks page")
    public void testStocksSearchButtonWithText() {
        // Navigate to Stocks page
        driver.get(BASE_URL + "/stock");

        // Find the search input field and enter text
        WebElement searchInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[placeholder^='Enter']"))
        );
        searchInput.clear();
        searchInput.sendKeys("Logitech Mouse");

        // Find the Search button
        WebElement searchButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Search']"))
        );

        // Assert the button is displayed and enabled
        Assert.assertTrue(searchButton.isDisplayed() && searchButton.isEnabled(),
                "Search button is not clickable on Stocks page after entering text");
    }


    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}


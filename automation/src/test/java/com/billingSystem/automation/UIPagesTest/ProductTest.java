package com.billingSystem.automation.UIPagesTest;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

public class ProductTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // -------- LOGIN --------
        driver.get("http://localhost:5173/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys("automationtest123");
        driver.findElement(By.name("password")).sendKeys("pass123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Wait for login to complete
        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return localStorage.getItem('role')") != null
        );

        // Go to Products page
        driver.get("http://localhost:5173/products");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1.text-3xl"))); // "Products" heading
    }

    @Test(description = "Click Add Product button")
    public void testAddProductButtonClick() {
        // Locate the button
        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[span[text()='Add Product']]")
        ));

        // Assert button is displayed and enabled
        Assert.assertTrue(addBtn.isDisplayed(), "Add Product button is not displayed");
        Assert.assertTrue(addBtn.isEnabled(), "Add Product button is not enabled");

        // Click button
        addBtn.click();

        // If it does not throw exception, click is successful
        System.out.println("Add Product button clicked successfully");
    }

    @AfterClass
    public void teardown() {
        if (driver != null) driver.quit();
    }

    @Test(description = "Verify Logitech Mouse product card is clickable")
    public void testLogitechMouseCardClickable() {
        // Navigate to products page
        driver.get("http://localhost:5173" + "/products");

        // Find Logitech Mouse card
        By productCard = By.xpath("//h3[text()='Logitech Mouse']");

        // Wait until the card is clickable
        WebElement card = wait.until(ExpectedConditions.elementToBeClickable(productCard));

        // Assert that the card is enabled and displayed (i.e., clickable)
        Assert.assertTrue(card.isDisplayed() && card.isEnabled(), "Logitech Mouse product card is not clickable");
    }


}

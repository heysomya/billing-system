package com.billingSystem.automation.UIPagesTest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class SalesPageTest {

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
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys("Cashierautomation");
        driver.findElement(By.name("password")).sendKeys("pass123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Wait for Dashboard
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1.text-3xl")));
    }

    @Test(description = "Create a sale: add product to cart and checkout")
    public void testCreateSale() throws InterruptedException {
        // Navigate to Sales page
        driver.get(BASE_URL + "/sales");

        // Wait for page heading
        WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("h1.text-3xl")
        ));
        Assert.assertTrue(heading.getText().contains("Create Sale"));

        // Select customer from input
        WebElement customerInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[placeholder='Search customer by name']")
        ));
        customerInput.clear();
        customerInput.sendKeys("Likhitha P");

        // Wait for suggestion dropdown and click first suggestion
        WebElement firstCustomer = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("ul.absolute li.p-2")
        ));
        firstCustomer.click();

        // Add first product to cart
        WebElement firstProduct = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".flex-1 .cursor-pointer")
        ));
        firstProduct.click();

        // Wait for cart to update (wait for Checkout button to be enabled)
        WebElement checkoutBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[text()='Checkout' and not(@disabled)]")
        ));
        Assert.assertTrue(checkoutBtn.isDisplayed(), "Checkout button is not visible/enabled");

        // Click Checkout
        checkoutBtn.click();

        // Wait for Confirm Sale dialog
        WebElement confirmDialog = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div[role='dialog']") // Dialog container
        ));
        Assert.assertTrue(confirmDialog.isDisplayed(), "Confirm dialog not displayed");

        // Click Confirm button
        WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[text()='Confirm']")
        ));
        confirmBtn.click();

        // Wait for success dialog
        WebElement successDialog = new WebDriverWait(driver, Duration.ofSeconds(40))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("div[data-testid='success-dialog'], .text-green-600") // adjust selector if needed
                ));

        Assert.assertTrue(successDialog.getText().contains("Sale Created Successfully!"));

        // Close success dialog
        WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[text()='Close']")
        ));
        closeBtn.click();
    }


    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}


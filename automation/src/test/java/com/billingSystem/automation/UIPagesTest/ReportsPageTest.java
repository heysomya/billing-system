package com.billingSystem.automation.UIPagesTest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.time.Duration;

public class ReportsPageTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String BASE_URL = "http://localhost:5173";

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(40));

        // LOGIN STEP
        driver.get(BASE_URL + "/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys("automationtest123");
        driver.findElement(By.name("password")).sendKeys("pass123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Wait for Dashboard
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1.text-3xl")));
    }


    @Test(description = "Verify /reports page opens and Generate Report works")
    public void testReportsPageAndGenerate() {
        // Navigate to reports page
        driver.get(BASE_URL + "/reports");

        // Verify page opened by checking heading
        WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("h1.text-3xl")
        ));
        assert heading.getText().contains("Reports");

        // Enter start date
        WebElement startDateInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//label[contains(text(),'Start Date')]/following-sibling::input")
                )
        );
        startDateInput.clear();
        startDateInput.sendKeys("11-01-2025");

        // Enter end date
        WebElement endDateInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//label[contains(text(),'End Date')]/following-sibling::input")
                )
        );
        endDateInput.clear();
        endDateInput.sendKeys("11-05-2025");

        // Click Generate Report
        WebElement generateButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Generate Report']"))
        );
        generateButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("h2.text-lg.font-semibold") // Chart heading
        ));
    }

    @Test(description = "Verify /reports page opens and Generate Report fails for invalid dates")
    public void testReportsPageAndGenerateForInvalidDate() {
        driver.get(BASE_URL + "/reports");

        // Verify Reports page heading
        WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("h1.text-3xl")
        ));
        Assertions.assertTrue(heading.getText().contains("Reports"), "Reports page did not open");

        // Enter invalid date range
        WebElement startDateInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//label[contains(text(),'Start Date')]/following-sibling::input")
                )
        );
        startDateInput.clear();
        startDateInput.sendKeys("2025-11-01");

        WebElement endDateInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//label[contains(text(),'End Date')]/following-sibling::input")
                )
        );
        endDateInput.clear();
        endDateInput.sendKeys("2025-11-05");

        // Click Generate Report
        WebElement generateButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Generate Report']"))
        );
        generateButton.click();

        // Wait for error message
        WebElement errorMsg = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//p[contains(text(), 'Failed to fetch report data')]")
                )
        );

        Assertions.assertTrue(errorMsg.isDisplayed(), "Expected error message not displayed");
    }

    @Test(description = "Verify Reports page opens and Generate Report works")
    public void testReportsPageGenerateReport() {
        // Navigate to Reports page
        driver.get("http://localhost:5173/reports");

        // Wait for page heading
        By headingLocator = By.cssSelector("h1.text-3xl");
        WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(headingLocator));
        Assertions.assertTrue(heading.getText().contains("Reports"), "Reports page did not open");

        // Enter start date
        WebElement startDateInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//label[contains(text(),'Start Date')]/following-sibling::input")
                )
        );
        startDateInput.clear();
        startDateInput.sendKeys("11-01-2025");

        // Enter end date
        WebElement endDateInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//label[contains(text(),'End Date')]/following-sibling::input")
                )
        );

        endDateInput.clear();
        endDateInput.sendKeys("11-05-2025");

        // Click Generate Report
        WebElement generateButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Generate Report']"))
        );
        generateButton.click();

        // Wait for report chart to appear (just verify any chart heading)
        By chartHeading = By.cssSelector("h2.text-lg.font-semibold");
        WebElement chart = wait.until(ExpectedConditions.visibilityOfElementLocated(chartHeading));
        Assertions.assertTrue(chart.isDisplayed(), "Report chart did not appear after clicking Generate Report");
    }

    @AfterClass
    public void teardown() {
        if (driver != null) driver.quit();
    }
}

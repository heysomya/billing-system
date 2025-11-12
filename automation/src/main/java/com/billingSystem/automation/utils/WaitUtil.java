package com.billingSystem.automation.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class WaitUtil {
    public static void waitForPageLoad(WebDriver driver, int timeoutSeconds) {
        long end = System.currentTimeMillis() + timeoutSeconds * 1000L;
        JavascriptExecutor js = (JavascriptExecutor) driver;
        while (System.currentTimeMillis() < end) {
            try {
                String readyState = js.executeScript("return document.readyState").toString();
                if ("complete".equals(readyState)) return;
            } catch (Exception ignored) {}
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {}
        }
    }
}


package com.example;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LibraryManagementSystemTest {

    @Test
    public void openLibraryManagementSystemAndLogin() throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        driver.get("https://applicationforlibrarymanagementsystem.onrender.com/");
        WebElement startTestingButton = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.cssSelector(".start-testing-button")));
        startTestingButton.click();
        driver.quit();
    }
}

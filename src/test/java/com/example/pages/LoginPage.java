package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage {

    private WebDriver driver;

    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    private By loginButton = By.id("login-button");
    private By errorMessage = By.cssSelector("div[role='alert']");
    private By usernameErrorMessage = By.id("username-error");
    private By passwordErrorMessage = By.id("password-error");

    private String expectedUrl = "https://applicationforlibrarymanagementsystem.onrender.com/login";

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isOnLoginPage() {
        return driver.getCurrentUrl().equals(expectedUrl);
    }

    public void enterUsername(String username) {
        WebElement usernameElement = driver.findElement(usernameField);
        usernameElement.clear();
        usernameElement.sendKeys(username);
    }

    public void enterPassword(String password) {
        WebElement passwordElement = driver.findElement(passwordField);
        passwordElement.clear();
        passwordElement.sendKeys(password);
    }

    public void clickLoginButton() {
        WebElement loginBtn = driver.findElement(loginButton);
        loginBtn.click();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    public void loginWithCredentials() {
        enterUsername("admin1");
        enterPassword("securePassword");
        clickLoginButton();
    }

    public boolean isErrorMessageVisible() {
        try {
            WebElement error = driver.findElement(errorMessage);
            return error.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessageText() {
        try {
            WebElement errorElement = driver.findElement(errorMessage);
            return errorElement.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isUsernameErrorVisible() {
        try {
            WebElement error = driver.findElement(usernameErrorMessage);
            return error.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameErrorText() {
        try {
            WebElement errorElement = driver.findElement(usernameErrorMessage);
            return errorElement.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isPasswordErrorVisible() {
        try {
            WebElement error = driver.findElement(passwordErrorMessage);
            return error.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getPasswordErrorText() {
        try {
            WebElement errorElement = driver.findElement(passwordErrorMessage);
            return errorElement.getText();
        } catch (Exception e) {
            return "";
        }
    }
}

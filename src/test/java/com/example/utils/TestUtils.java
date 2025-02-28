package com.example.utils;

import com.example.pages.*;

import com.example.testobjects.MockBook;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class TestUtils {

    public static void login(WebDriver driver, String appUrl) {
        driver.get(appUrl);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginWithCredentials();
    }

    public static void goToUrl(WebDriver driver, String appUrl) {
        driver.get(appUrl);
    }

    public static void assertWelcomeMessagePresent(WebDriver driver, String welcomeMessage) {
        BooksPage booksPage = new BooksPage(driver);
        Assertions.assertEquals(welcomeMessage, booksPage.getWelcomeMessage(), "Welcome message is incorrect!");
    }

    public static void assertOnLoginPage(WebDriver driver) {
        LoginPage loginPage = new LoginPage(driver);
        Assertions.assertTrue(loginPage.isOnLoginPage(), "The user is not on the login page!");
    }

    public static void assertOnBooksPage(WebDriver driver) {
        BooksPage booksPage = new BooksPage(driver);
        Assertions.assertTrue(booksPage.isOnBooksPage(), "The user is not on the Books page!");
    }

    public static void assertOnAddBookPage(WebDriver driver) {
        AddBookPage addBookPage = new AddBookPage(driver);
        Assertions.assertTrue(addBookPage.isOnAddBookPage(), "The user is not on the add book page!");
    }

    public static void assertOnEditBookPage(WebDriver driver) {
        EditBookPage editBookPage = new EditBookPage(driver);
        Assertions.assertTrue(editBookPage.isOnEditBookPage(), "The user is not on the edit book page!");
    }

    public static MockBook createFakeBook(String title, String author, String genre, String isbn, String date, String price) {
        return new MockBook(title, author, genre, isbn, date, price);
    }

    public static void assertAddBookValidationMessages(AddBookPage addBookPage, String field, String expectedMessage) {
        Assertions.assertTrue(addBookPage.isValidationMessageDisplayed(), "Top validation message box is not displayed!");
        List<String> validationErrors = addBookPage.getValidationMessages();
        Assertions.assertTrue(validationErrors.contains(expectedMessage), "Expected validation message at top is wrong or missing!");

        Assertions.assertTrue(addBookPage.isValidationMessageDisplayedFor(field), "Specific validation message under " + field + " field is not displayed!");
        Assertions.assertEquals(expectedMessage, addBookPage.getValidationMessageFor(field), "Validation message text under " + field + " field is incorrect!");
    }

    public static void assertLoginValidationMessages(
            LoginPage loginPage,
            boolean isErrorMessageVisible,
            String expectedErrorMessage,
            boolean isUsernameErrorVisible,
            String expectedUsernameErrorMessage,
            boolean isPasswordErrorVisible,
            String expectedPasswordErrorMessage) {

        // Assert general error message visibility and text
        Assertions.assertEquals(isErrorMessageVisible, loginPage.isErrorMessageVisible(), "Error message visibility mismatch.");
        if (isErrorMessageVisible) {
            Assertions.assertTrue(loginPage.getErrorMessageText().contains(expectedErrorMessage),
                    "Error message does not contain expected text.");

        }

        // Assert username-specific validation message
        Assertions.assertEquals(isUsernameErrorVisible, loginPage.isUsernameErrorVisible(), "Username error visibility mismatch.");
        if (isUsernameErrorVisible) {
            Assertions.assertEquals(expectedUsernameErrorMessage, loginPage.getUsernameErrorText(),
                    "Incorrect username validation message text.");
        }

        // Assert password-specific validation message
        Assertions.assertEquals(isPasswordErrorVisible, loginPage.isPasswordErrorVisible(), "Password error visibility mismatch.");
        if (isPasswordErrorVisible) {
            Assertions.assertEquals(expectedPasswordErrorMessage, loginPage.getPasswordErrorText(),
                    "Incorrect password validation message text.");
        }
    }
}

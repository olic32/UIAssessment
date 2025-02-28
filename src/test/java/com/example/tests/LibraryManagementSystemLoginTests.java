package com.example.tests;

import com.example.pages.*;
import com.example.utils.TestUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.Properties;

public class LibraryManagementSystemLoginTests {

    private static Properties config;
    private static WebDriver driver;
    private static String appUrl;
    private static String bookUrl;

    private static String usernameValidationMessage;
    private static String passwordValidationMessage;
    private static String invalidCredentialsMessage;
    private static String failedSubmissionMessage;

    private static String welcomeMessage;

    private LoginPage loginPage;
    private BooksPage booksPage;
    private AddBookPage addBookPage;

    @BeforeAll
    public static void setUpClass() throws IOException {
        config = new Properties();
        config.load(LibraryManagementSystemLoginTests.class.getClassLoader().getResourceAsStream("config.properties"));
        appUrl = config.getProperty("app.url");
        driver = new ChromeDriver();
        bookUrl = config.getProperty("app.url.books");
        usernameValidationMessage = config.getProperty("loginpage.usernameValidationMessage");
        passwordValidationMessage = config.getProperty("loginpage.passwordValidationMessage");
        invalidCredentialsMessage = config.getProperty("loginpage.invalidCredentialMessage");
        failedSubmissionMessage = config.getProperty("loginpage.failedSubmissionMessage");
        welcomeMessage = config.getProperty("bookspage.welcomeMessage");
    }

    @AfterAll
    public static void tearDownClass() {
        if (driver != null) {
            driver.quit();
        }
    }

    @BeforeEach
    public void setUp() {
        TestUtils.goToUrl(driver, appUrl);
        loginPage = new LoginPage(driver);
        booksPage = new BooksPage(driver);
        addBookPage = new AddBookPage(driver);
    }

    @Test
    public void openSystemAndLogin() {
        TestUtils.login(driver, appUrl);

        TestUtils.assertOnBooksPage(driver);

        TestUtils.assertWelcomeMessagePresent(driver,welcomeMessage);
    }

    @Test
    public void openSystemAndLoginWithInvalidCredentials() {
        loginPage.login("incorrectUsername", "incorrectPassword");
        TestUtils.assertOnLoginPage(driver);

        TestUtils.assertLoginValidationMessages(
                loginPage,
                true, invalidCredentialsMessage,
                false, "",
                false, ""
        );
    }

    @Test
    public void loginWithNoCredentials() {
        loginPage.login("", "");
        TestUtils.assertOnLoginPage(driver);

        TestUtils.assertLoginValidationMessages(
                loginPage,
                true, failedSubmissionMessage,
                true, usernameValidationMessage,
                true, passwordValidationMessage
        );
    }

    @Test
    public void loginWithNoPassword() {
        loginPage.enterUsername("testuser");
        loginPage.clickLoginButton();
        TestUtils.assertOnLoginPage(driver);

        TestUtils.assertLoginValidationMessages(
                loginPage,
                true, passwordValidationMessage,
                false, "",
                true, passwordValidationMessage
        );
    }

    @Test
    public void loginWithNoUsername() {
        loginPage.enterPassword("password123");
        loginPage.clickLoginButton();
        TestUtils.assertOnLoginPage(driver);

        TestUtils.assertLoginValidationMessages(
                loginPage,
                true, usernameValidationMessage,
                true, usernameValidationMessage,
                false, ""
        );
    }

    @Test
    public void verifyUserCanLoginAndLogoutFromBooksPage() {
        TestUtils.login(driver, appUrl);
        TestUtils.assertOnBooksPage(driver);
        booksPage.clickLogout();
        TestUtils.assertOnLoginPage(driver);
    }

    @Test
    public void verifyUserCanLoginAndLogoutFromAddBooksPage() {
        TestUtils.login(driver, appUrl);
        TestUtils.assertOnBooksPage(driver);
        booksPage.clickAddBookButton();
        TestUtils.assertOnAddBookPage(driver);
        addBookPage.clickLogout();
        TestUtils.assertOnLoginPage(driver);
    }

    @Test
    public void verifyUserCannotGoToBooksPageWithoutLoggingIn() {
        TestUtils.goToUrl(driver, bookUrl);
        Assertions.assertFalse(booksPage.isOnBooksPage(), "The user is on the Books page without logging in!");
    }
}

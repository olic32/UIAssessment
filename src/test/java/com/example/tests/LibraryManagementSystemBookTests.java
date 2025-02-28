package com.example.tests;

import com.example.pages.*;
import com.example.testobjects.MockBook;
import com.example.utils.TestUtils;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;



public class LibraryManagementSystemBookTests {
    private static Properties config;
    private static WebDriver driver;
    private static String appUrl;
    private static String bookUrl;


    private BooksPage booksPage;
    private AddBookPage addBookPage;
    private EditBookPage editBookPage;

    @BeforeAll
    public static void setUpClass() throws IOException {
        config = new Properties();
        config.load(LibraryManagementSystemBookTests.class.getClassLoader().getResourceAsStream("config.properties"));
        driver = new ChromeDriver();
        appUrl = config.getProperty("app.url");
        bookUrl = config.getProperty("app.url.books");
    }

    @AfterAll
    public static void tearDownClass() {
        if (driver != null) {
            driver.quit();
        }
    }

    @BeforeEach
    public void setUp() {
        TestUtils.login(driver, appUrl);
        booksPage = new BooksPage(driver);
        addBookPage = new AddBookPage(driver);
        editBookPage = new EditBookPage(driver);
    }

    @Test
    public void verifyDefaultBooksArePresentInTable() {

        //Ideally, these three 'default' books could be encapsulated in a list of mock data. This could be linked to the live app and where these
        //books are generated - decreasing maintenance if more default books are added
        MockBook book1 = TestUtils.createFakeBook("The Very Busy Spider", "Eric Carle", "Picture Book", "9780694005000", "01/09/1984", "£6.99");
        MockBook book2 = TestUtils.createFakeBook("The Cat in the Hat", "Dr. Seuss", "Children's Literature", "9780394800011", "12/03/1957", "£7.99");
        MockBook book3 = TestUtils.createFakeBook("Charlotte's Web", "E.B. White", "Children's Fiction", "9780064400558", "15/10/1952", "£8.99");

        Assertions.assertEquals("Book found with all correct details.", booksPage.verifyBookDetails(book1), "The Very Busy Spider is missing or incorrect!");
        Assertions.assertEquals("Book found with all correct details.", booksPage.verifyBookDetails(book2), "The Cat in the Hat is missing or incorrect!");
        Assertions.assertEquals("Book found with all correct details.", booksPage.verifyBookDetails(book3), "Charlotte's Web is missing or incorrect!");

    }

    @Test
    public void verifyUserCanDeleteBooks() {

        MockBook bookToDelete = TestUtils.createFakeBook("The Very Busy Spider", "Eric Carle", "Picture Book", "9780694005000", "01/09/1984", "£6.99");

        Assertions.assertEquals("Book found with all correct details.", booksPage.verifyBookDetails(bookToDelete), "The Very Busy Spider is missing or incorrect!");

        booksPage.clickDeleteButton(bookToDelete.getTitle());

        Assertions.assertTrue(booksPage.isBookAbsent(bookToDelete.getTitle()), "Book was not deleted successfully!");

    }

    @Test
    public void verifyUserCanAddBook() {

        MockBook newBook = TestUtils.createFakeBook("NewBook", "SomeDude", "Fiction", "9783161484100", "12/12/12", "12");

        booksPage.clickAddBookButton();
        TestUtils.assertOnAddBookPage(driver);

        addBookPage.addNewBook(newBook);
        TestUtils.assertOnBooksPage(driver);

        String bookVerificationResult = booksPage.verifyBookDetails(newBook);
        Assertions.assertEquals("Book found with all correct details.", bookVerificationResult, bookVerificationResult);
    }

    @Test
    public void verifyUserCanAddMultipleBooksAndSwitchPageAndViewBook() {

        MockBook newBook1 = TestUtils.createFakeBook("NewBook1", "SomeDude", "Fiction", "9783161484100", "12/12/12", "12");
        MockBook newBook2 = TestUtils.createFakeBook("NewBook2", "SomeDude", "Fiction", "9783161484100", "12/12/12", "12");
        MockBook newBook3 = TestUtils.createFakeBook("NewBook3", "SomeDude", "Fiction", "9783161484100", "12/12/12", "12");

        booksPage.clickAddBookButton();
        addBookPage.addNewBook(newBook1);
        booksPage.clickAddBookButton();
        addBookPage.addNewBook(newBook2);
        booksPage.clickAddBookButton();
        addBookPage.addNewBook(newBook3);

        booksPage.clickNextButton();

        String bookVerificationResult = booksPage.verifyBookDetails(newBook3);
        Assertions.assertEquals("Book found with all correct details.", bookVerificationResult, bookVerificationResult);

    }

    @Test
    public void verifyUserCannotAddBookWithNoTitle() {
        String titleValidationMessage = config.getProperty("addbookpage.titleValidation");

        MockBook fakeBook = TestUtils.createFakeBook("", "SomeDude", "Fiction", "9783161484100", "12/12/12", "12");

        booksPage.clickAddBookButton();
        TestUtils.assertOnAddBookPage(driver);

        addBookPage.addNewBook(fakeBook);
        Assertions.assertTrue(addBookPage.isOnAddBookPage(), "The user is not still on the Add Book page - poorly configured book was submitted successfully");

        TestUtils.assertAddBookValidationMessages(addBookPage, "title", titleValidationMessage);
    }

    @Test
    public void verifyUserCannotAddBookWithNoAuthor() {
        String authorValidationMessage = config.getProperty("addbookpage.authorValidation");

        MockBook fakeBook = TestUtils.createFakeBook("NewBook", "", "Fiction", "9783161484100", "12/12/12", "12");

        booksPage.clickAddBookButton();
        TestUtils.assertOnAddBookPage(driver);

        addBookPage.addNewBook(fakeBook);
        Assertions.assertTrue(addBookPage.isOnAddBookPage(), "The user is not still on the Add Book page - poorly configured book was submitted successfully");

        TestUtils.assertAddBookValidationMessages(addBookPage, "author", authorValidationMessage);
    }

    @Test
    public void verifyUserCannotAddBookWithNoGenre() {
        String genreValidationMessage = config.getProperty("addbookpage.genreValidation");

        MockBook fakeBook = TestUtils.createFakeBook("NewBook", "SomeFella", "", "9783161484100", "12/12/12", "12");

        booksPage.clickAddBookButton();
        TestUtils.assertOnAddBookPage(driver);

        addBookPage.addNewBookWithoutGenre(fakeBook);
        Assertions.assertTrue(addBookPage.isOnAddBookPage(), "The user is not still on the Add Book page - poorly configured book was submitted successfully");

        TestUtils.assertAddBookValidationMessages(addBookPage, "genre", genreValidationMessage);

    }

    @Test
    public void verifyUserCannotAddBookWithNoISBN() {
        String isbnValidationMessage = config.getProperty("addbookpage.isbnValidation");

        MockBook fakeBook = TestUtils.createFakeBook("NewBook", "SomeFella", "Fiction", "", "12/12/12", "12");

        booksPage.clickAddBookButton();
        TestUtils.assertOnAddBookPage(driver);

        addBookPage.addNewBook(fakeBook);
        Assertions.assertTrue(addBookPage.isOnAddBookPage(), "The user is not still on the Add Book page - poorly configured book was submitted successfully");

        TestUtils.assertAddBookValidationMessages(addBookPage, "isbn", isbnValidationMessage);
    }

    @Test
    public void verifyUserCannotAddBookWithNoDate() {
        String dateValidationMessage = config.getProperty("addbookpage.dateValidation");

        MockBook fakeBook = TestUtils.createFakeBook("NewBook", "SomeFella", "Fiction", "9783161484100", "", "12");

        booksPage.clickAddBookButton();
        TestUtils.assertOnAddBookPage(driver);

        addBookPage.addNewBook(fakeBook);
        Assertions.assertTrue(addBookPage.isOnAddBookPage(), "The user is not still on the Add Book page - poorly configured book was submitted successfully");

        TestUtils.assertAddBookValidationMessages(addBookPage, "date", dateValidationMessage);
    }

    @Test
    public void verifyUserCannotAddBookWithNoPrice() {
        String priceValidationMessage = config.getProperty("addbookpage.priceValidation");

        MockBook fakeBook = TestUtils.createFakeBook("NewBook", "SomeFella", "Fiction", "9783161484100", "12/12/12", "");

        booksPage.clickAddBookButton();
        TestUtils.assertOnAddBookPage(driver);

        addBookPage.addNewBook(fakeBook);
        Assertions.assertTrue(addBookPage.isOnAddBookPage(), "The user is not still on the Add Book page - poorly configured book was submitted successfully");

        TestUtils.assertAddBookValidationMessages(addBookPage, "price", priceValidationMessage);
    }


    @Test
    public void verifyUserCannotAddBookWithPoorlyFormattedDate() {
        MockBook fakeBook = TestUtils.createFakeBook("NewBook", "SomeFella", "Fiction", "9783161484100", "NotADate", "12");

        booksPage.clickAddBookButton();
        TestUtils.assertOnAddBookPage(driver);

        addBookPage.addNewBook(fakeBook);
        Assertions.assertTrue(addBookPage.isOnAddBookPage(), "The user is not still on the Add Book page - poorly configured book was submitted successfully");

        //Normally I would assert here for the correct validation message - however the app has no validation for this fields formatting.
    }

    @Test
    public void verifyUserCannotAddBookWithPoorlyFormattedPrice() {
        String priceValidationMessage = config.getProperty("addbookpage.priceValidation");

        MockBook fakeBook = TestUtils.createFakeBook("NewBook", "SomeFella", "Fiction", "123", "12/12/12", "NotAPrice");

        booksPage.clickAddBookButton();
        TestUtils.assertOnAddBookPage(driver);

        addBookPage.addNewBook(fakeBook);
        Assertions.assertTrue(addBookPage.isOnAddBookPage(), "The user is not still on the Add Book page - poorly configured book was submitted successfully");

        TestUtils.assertAddBookValidationMessages(addBookPage, "price", priceValidationMessage);

        //Unlike the other two formatting tests, this field does not allow you to enter anything other than price. Therefore the validation is the same for missing and poorly formatted entries in this field.
    }

    @Test
    public void verifyUserCannotAddBookWithPoorlyFormattedISBN() {
        MockBook fakeBook = TestUtils.createFakeBook("NewBook", "SomeFella", "Fiction", "NotAnISBN", "12/12/12", "12");

        booksPage.clickAddBookButton();
        TestUtils.assertOnAddBookPage(driver);

        addBookPage.addNewBook(fakeBook);
        Assertions.assertTrue(addBookPage.isOnAddBookPage(), "The user is not still on the Add Book page - poorly configured book was submitted successfully");

        //Normally I would assert here for the correct validation message - however the app has no validation for this fields formatting.
    }


    @Test
    public void verifyGenreDropdownHasExpectedValues() {

        List<String> expectedGenres = Arrays.asList("Fiction", "Non-Fiction", "Mystery", "Fantasy", "Science Fiction", "Biography", "Picture Book");

        booksPage.clickAddBookButton();
        TestUtils.assertOnAddBookPage(driver);

        Assertions.assertTrue(addBookPage.areExpectedGenresPresent(expectedGenres), "The genre dropdown does not contain the expected values!");


    }

    @Test
    public void verifyUserCanEditExistingBook() {

        MockBook changingBook = TestUtils.createFakeBook("NewBook", "SomeFella", "Fiction", "9783161484100", "12/12/12", "12");
        MockBook finalBook = TestUtils.createFakeBook("NewerBook", "SomeOtherFella", "Fiction", "9783161484101", "11/11/11", "11");

        booksPage.clickAddBookButton();
        TestUtils.assertOnAddBookPage(driver);

        addBookPage.addNewBook(changingBook);

        String bookVerificationResult = booksPage.verifyBookDetails(changingBook);
        Assertions.assertEquals("Book found with all correct details.", bookVerificationResult, bookVerificationResult);

        booksPage.clickEditButton(changingBook.getTitle());
        TestUtils.assertOnEditBookPage(driver);

        editBookPage.editBookDetails(finalBook);

        TestUtils.assertOnBooksPage(driver);

        String updatedBookVerification = booksPage.verifyBookDetails(finalBook);
        Assertions.assertEquals("Book found with all correct details.", updatedBookVerification, updatedBookVerification);

    }

    @Test
    public void verifyUserCannotSubmitEditWithoutAllFields() {

        MockBook defaultBookToEdit = TestUtils.createFakeBook("The Very Busy Spider", "Eric Carle", "Picture Book", "9780694005000", "01/09/1984", "£6.99");
        MockBook emptyFieldBook = TestUtils.createFakeBook("", "", "", "", "", "");

        String bookVerificationResult = booksPage.verifyBookDetails(defaultBookToEdit);
        Assertions.assertEquals("Book found with all correct details.", bookVerificationResult, bookVerificationResult);

        booksPage.clickEditButton(defaultBookToEdit.getTitle());
        TestUtils.assertOnEditBookPage(driver);

        editBookPage.editBookDetails(emptyFieldBook);

        TestUtils.assertOnEditBookPage(driver);

        TestUtils.goToUrl(driver,bookUrl);

        String bookVerificationResultAfterEdit = booksPage.verifyBookDetails(defaultBookToEdit);
        Assertions.assertEquals("Book found with all correct details.", bookVerificationResultAfterEdit, bookVerificationResultAfterEdit);

    }

}
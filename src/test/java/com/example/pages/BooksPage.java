package com.example.pages;

import com.example.testobjects.MockBook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

public class BooksPage {

    WebDriver driver;

    private By welcomeMessage = By.cssSelector("div.flex.justify-between h2.text-lg.font-bold");
    private By logoutButton = By.cssSelector("button.logout-button");
    private By bookTableRows = By.cssSelector("tbody tr");

    private String editButtonXPath = "//td[contains(text(),\"%s\")]/following-sibling::td/button[contains(@class, 'edit-button')]";
    private String deleteButtonXPath = "//td[contains(text(),\"%s\")]/following-sibling::td/button[contains(@class, 'delete-button')]";

    public By addBookButton = By.cssSelector(".add-book-button");
    public By previousPageButton = By.cssSelector(".prev-button");
    public By nextPageButton = By.className("next-button");

    private String expectedUrl = "https://applicationforlibrarymanagementsystem.onrender.com/books";

    public BooksPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isOnBooksPage() {
        return driver.getCurrentUrl().equals(expectedUrl);
    }

    public String getWelcomeMessage() {
        WebElement welcomeElement = driver.findElement(welcomeMessage);
        return welcomeElement.getText();
    }

    public void clickLogout() {
        WebElement logoutBtn = driver.findElement(logoutButton);
        logoutBtn.click();
    }

    public void clickAddBookButton() {
        driver.findElement(addBookButton).click();
    }

    public void clickPreviousButton() {driver.findElement(previousPageButton).click(); }

    public void clickNextButton() {driver.findElement(nextPageButton).click(); }

    public List<WebElement> getBookList() {
        return driver.findElements(bookTableRows);
    }

    // Check if a book exists with the book detals as given
    //Return a string that is asserted against - if it matches, test will pass. If not, the message will list the discrepancies.
    //Probably a more concise way to do this
    public String verifyBookDetails(MockBook expectedBook) {
        List<WebElement> rows = getBookList();

        for (WebElement row : rows) {
            List<WebElement> columns = row.findElements(By.tagName("td"));

            String actualTitle = columns.get(0).getText();

            if (actualTitle.equals(expectedBook.getTitle())) {
                // Get displayed values
                String actualAuthor = columns.get(1).getText();
                String actualGenre = columns.get(2).getText();
                String actualIsbn = columns.get(3).getText();
                String actualPublicationDate = columns.get(4).getText();
                String actualPrice = columns.get(5).getText();

                // Format expected values before comparison
                String expectedPublicationDate = formatPublicationDate(expectedBook.getPublicationDate());
                String expectedPrice = formatPrice(expectedBook.getPrice());

                // Compare with actual values
                MockBook actualBook = new MockBook(actualTitle, actualAuthor, actualGenre, actualIsbn, actualPublicationDate, actualPrice);
                MockBook expectedFormattedBook = new MockBook(
                        expectedBook.getTitle(),
                        expectedBook.getAuthor(),
                        expectedBook.getGenre(),
                        expectedBook.getIsbn(),
                        expectedPublicationDate,
                        expectedPrice
                );

                if (expectedFormattedBook.equals(actualBook)) {
                    return "Book found with all correct details.";
                } else {
                    return "Book found, but with mismatches:\n" + expectedFormattedBook.compareDifferences(actualBook);
                }
            }
        }
        return "Book not found in the table.";
    }

    public boolean isBookAbsent(String title) {
        List<WebElement> rows = getBookList();
        for (WebElement row : rows) {
            List<WebElement> columns = row.findElements(By.tagName("td"));
            if (columns.get(0).getText().equals(title)) {
                return false;
            }
        }
        return true;
    }

    public void clickEditButton(String bookTitle) {
        By editButtonLocator = By.xpath(String.format(editButtonXPath, bookTitle));
        WebElement editButton = driver.findElement(editButtonLocator);
        editButton.click();
    }

    public void clickDeleteButton(String bookTitle) {
        By deleteButtonLocator = By.xpath(String.format(deleteButtonXPath, bookTitle));
        WebElement deleteButton = driver.findElement(deleteButtonLocator);
        deleteButton.click();
    }

    //These formatting methods are not very flexible. As mentioned in README, I would discuss requirements before developing further.
    //Both make assumptions for the sake of building simpler tests. I would ask for more requirements about acceptable formats.

    //We are guessing to some extent the nature of the formatting happening in the back end. It would be more effective for these
    // tests to share the class used to do this formatting in the live app
    private String formatPublicationDate(String inputDate) {

        if (inputDate.matches("\\d{2}/\\d{2}/\\d{2}")) {
            return inputDate.substring(0, 6) + "20" + inputDate.substring(6);
        }
        return inputDate;
    }

    private String formatPrice(String inputPrice) {

        if (inputPrice.matches("\\d+")) {
            return "£" + inputPrice + ".00";
        }
        return inputPrice;
    }
}

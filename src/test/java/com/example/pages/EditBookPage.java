package com.example.pages;

import com.example.testobjects.MockBook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class EditBookPage {

    private WebDriver driver;

    private By titleInput = By.id("edit-title");
    private By authorInput = By.id("edit-author");
    private By genreInput = By.id("edit-genre");
    private By isbnInput = By.id("edit-isbn");
    private By publicationDateInput = By.id("edit-publicationDate");
    private By priceInput = By.id("edit-price");
    private By saveChangesButton = By.id("save-changes");
    private By pageHeading = By.xpath("//h2[contains(text(),'Edit book details')]");

    public EditBookPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isOnEditBookPage() {
        return driver.findElement(pageHeading).isDisplayed();
    }

    public void enterTitle(String title) {
        WebElement titleField = driver.findElement(titleInput);
        titleField.clear();
        titleField.sendKeys(title);
    }

    public void enterAuthor(String author) {
        WebElement authorField = driver.findElement(authorInput);
        authorField.clear();
        authorField.sendKeys(author);
    }

    public void enterGenre(String genre) {
        WebElement genreField = driver.findElement(genreInput);
        genreField.clear();
        genreField.sendKeys(genre);
    }

    public void enterIsbn(String isbn) {
        WebElement isbnField = driver.findElement(isbnInput);
        isbnField.clear();
        isbnField.sendKeys(isbn);
    }

    public void enterPublicationDate(String date) {
        WebElement dateField = driver.findElement(publicationDateInput);
        dateField.clear();
        dateField.sendKeys(date);
    }

    public void enterPrice(String price) {
        WebElement priceField = driver.findElement(priceInput);
        priceField.clear();
        priceField.sendKeys(price);
    }

    public void editBookDetails(MockBook book) {
        enterTitle(book.getTitle());
        enterAuthor(book.getAuthor());
        enterGenre(book.getGenre());
        enterIsbn(book.getIsbn());
        enterPublicationDate(book.getPublicationDate());
        enterPrice(book.getPrice());
        clickSaveChanges();
    }

    public void clickSaveChanges() {
        driver.findElement(saveChangesButton).click();
    }

}

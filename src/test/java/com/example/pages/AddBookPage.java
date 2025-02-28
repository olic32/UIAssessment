package com.example.pages;

import com.example.testobjects.MockBook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AddBookPage {
    private WebDriver driver;

    private By logoutButton = By.cssSelector("button.logout-button");
    private By addBookButton = By.cssSelector("button.add-book-button");
    private By validationMessageBox = By.cssSelector("div[role='alert']");
    private By validationMessagesList = By.cssSelector("div[role='alert'] ul li");

    private String expectedUrl = "https://applicationforlibrarymanagementsystem.onrender.com/add-book";

    private final Map<String, By> fieldLocators = Map.of(
            "title", By.id("title"),
            "author", By.id("author"),
            "genre", By.id("genre"),
            "isbn", By.id("isbn"),
            "date", By.id("publicationDate"),
            "price", By.id("price")
    );

    private final Map<String, By> validationLocators = Map.of(
            "title", By.id("title-error"),
            "author", By.id("author-error"),
            "genre", By.id("genre-error"),
            "isbn", By.id("isbn-error"),
            "date", By.id("publicationDate-error"),
            "price", By.id("price-error")
    );

    public AddBookPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isOnAddBookPage() {
        return driver.getCurrentUrl().equals(expectedUrl);
    }

    public void clickLogout() {
        driver.findElement(logoutButton).click();
    }

    public void addNewBook(MockBook book) {
        enterField("title", book.getTitle());
        enterField("author", book.getAuthor());
        selectGenre(book.getGenre());
        enterField("isbn", book.getIsbn());
        enterField("date", book.getPublicationDate());
        enterField("price", book.getPrice());
        clickAddBookButton();
    }

    public void addNewBookWithoutGenre(MockBook book) {
        enterField("title", book.getTitle());
        enterField("author", book.getAuthor());
        enterField("isbn", book.getIsbn());
        enterField("date", book.getPublicationDate());
        enterField("price", book.getPrice());
        clickAddBookButton();
    }

    public void enterField(String field, String value) {
        By locator = fieldLocators.get(field);
        if (locator != null) {
            WebElement element = driver.findElement(locator);
            element.clear();
            element.sendKeys(value);
        } else {
            throw new IllegalArgumentException("Invalid field name: " + field);
        }
    }

    public void selectGenre(String genre) {
        WebElement genreElement = driver.findElement(fieldLocators.get("genre"));
        Select genreSelect = new Select(genreElement);
        genreSelect.selectByVisibleText(genre);
    }

    public void clickAddBookButton() {
        driver.findElement(addBookButton).click();
    }

    public boolean isValidationMessageDisplayed() {
        try {
            WebElement element = driver.findElement(validationMessageBox);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> getValidationMessages() {
        return driver.findElements(validationMessagesList)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public boolean isValidationMessageDisplayedFor(String field) {
        By locator = validationLocators.get(field);
        if (locator != null) {
            try {
                WebElement element = driver.findElement(locator);
                return element.isDisplayed();
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public String getValidationMessageFor(String field) {
        By locator = validationLocators.get(field);
        return locator != null ? driver.findElement(locator).getText() : null;
    }

    public List<String> getAvailableGenres() {
        WebElement genreElement = driver.findElement(fieldLocators.get("genre"));
        Select genreSelect = new Select(genreElement);
        return genreSelect.getOptions()
                .stream()
                .skip(1)
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public boolean areExpectedGenresPresent(List<String> expectedGenres) {
        List<String> actualGenres = getAvailableGenres();
        return actualGenres.containsAll(expectedGenres) && expectedGenres.containsAll(actualGenres);
    }
}


---

## Install dependencies

mvn install -DskipTests


## To run tests

mvn test

## Review

For this project, I decided on a few framework approaches:

1. **Create a page object model**: This meant adding a `pages` package, which includes `BooksPage`, `LoginPage`, `AddBookPage`, and `EditBook` classes. These contain elements captured as Selenium locators and methods for web actions and performing assertions.
2. **Utilize a properties file for strings**: This includes messages that are likely to change, password-username combinations, or headings. Theoretically, you could link this properties file to the file being used as part of the live app, reducing duplication.
3. **'Books' are used in a variety of tests** and take a common structure. I made a class called `MockBook` which can be used as a placeholder in tests, reducing duplication and increasing consistency. I wrote an override for `equals` to compare directly in the table once a book is added and also a method to compare differences on specific fields between two books.
4. **Regularly used actions** have been encapsulated within a `TestUtils` class. This includes assertions, navigation, and setting up mock books.

### Other notes:

1. There is some backend formatting done on prices and dates after input as a new book. In order to test this, I added methods to reformat test data to compare it matches with the expected, formatted output displayed in the UI. This doesn't currently account for American/UK date transitions and other limitations. I would check requirements for clarity if writing these tests for a real service.
2. Book verification returns a string – if it is correct, it returns a message that it is correct. If not, it returns a list of differences for debugging.
3. Despite the fact that login currently has no effect, I still have logging in as a step for every test. This in a real service would be useful for when logging in is correctly implemented.
4. There is no issue with test 'leakage', despite using the same WebDriver across multiple tests. The data resets on every login.
5. The tests end prematurely when assessing whether poorly formatted dates/prices/ISBNs can be entered and submitted. This is because currently they can. If there were validation messages here, I would assert for those also.

### Things I would do next:

1. Extend all page classes from a single class – there is some redundancy in having similar methods to check you are on the correct page across each class.
2. Amend the assertions in `TestUtils` to allow for custom error messages for each test.
3. Add screenshotting when tests fail.
4. Login methods are a bit scattered – simplify this.
5. Add more tests around editing a book – ensure there is validation. Current tests only check it cannot be submitted, but I ran out of time to add more tests here.
6. Some selectors are likely to be a bit flaky – using text to find them that might change. In an ideal situation, we could ask a developer to add an ID or class where appropriate.
7. Add multiple browser support.

---

## **TEST CASES AND REQUIREMENTS BREAKDOWN:**

### Business Requirements:

- _Only authorized users are permitted to access the books catalog and make changes._
- _Both the login and books form pages include validation measures._
- _Upon successful login, users are taken to a books list page, where they can create, read, update, and delete books._
- _The books list page also features a welcome message and a logout button._
- _When the logout button is clicked, users are returned to the login page._

### _Login Scenarios:_

1. User can login with credentials and is taken to books list page with correct heading.
2. User cannot login with incorrect credentials.
3. User cannot login without any credentials.
4. User cannot login without a password.
5. User cannot login without a username.
6. User can log out from the main Books page.
7. User can log out from the Add Book page.
8. User cannot reach the books page without logging in first.

### _Create, Update and Delete Book Scenarios:_

1. User sees the default books as present in the table upon login.
2. User can delete a book from the table.
3. User can add a book to the table.
4. User can add multiple books and use Next button to view them.
5. User cannot add a book without title (and sees correct validation).
6. User cannot add a book without genre (and sees correct validation).
7. User cannot add a book without author (and sees correct validation).
8. User cannot add a book without ISBN (and sees correct validation).
9. User cannot add a book without publication date (and sees correct validation).
10. User cannot add a book without price (and sees correct validation).
11. User cannot add a book with incorrectly formatted date (and sees correct validation).
12. User cannot add a book with incorrectly formatted price (and sees correct validation).
13. User cannot add a book with incorrectly formatted ISBN (and sees correct validation).
14. User can see all the values available for genre.
15. User can edit an existing book.
16. User cannot edit a book to have missing data and save it.

---

### **TEST CASE NOTES**

#### _Login Scenarios:_

Test case 7 is not specified in requirements. It is only specified that a user can log out from the main page. Included contingent on discussion about requirements.

#### _Add Book Scenarios:_

Test case 1 is not in requirements. However, it is the case that there are three default books present in the app. This would be a situation where I would discuss the requirements.

Test cases 4-8 could feasibly be combined – however, for the purpose of this exercise, I left them separate for clarity. As the driver is not restarted, this doesn’t slow the test down too much currently.

Test cases 9, 10, and 11 make some assumptions about correct formatting. There are a number of ways the date can be incorrectly formatted. I would ask for stricter rules on what formatting is expected as input, as some formatting is done on this field currently in the back end. Similarly for price. ISBN also has fairly strict formatting rules that are not made explicit in requirements. These could be comfortably added, however, as back-end formatting exists for other fields, potentially we would not require strict input rules here either.

Test case 12 has been added, again as a placeholder, contingent on discussion about requirements. The dropdown does not contain the genres present in the default books – this test added in case we want to verify this going forward. It is possible to edit books to have any genre. Again this would need a requirements discussion, rather than a test.

Test case 14 is added for illustration – in reality, navigating manually back to the books page on this app refreshes the table, so we cannot see if our failed edits were successful – though we can check that we are not redirected automatically upon failed validation on an edit. I did not have time to add assertions on validation content on this page.

---

### _Other Notes_

#### 6 tests currently fail:

1. The genre dropdown doesn't contain the genres shown on the main page. As noted above, this may not be a valid test case.
2. Books can be submitted with non-date formats.
3. Books can be submitted with poorly formatted ISBNs.
4. The logout button doesn't work on the main Books page.
5. The logout button doesn't work on the Add Books page.
6. Users can view, add, and update books (and any other action) without logging in simply by navigating to the endpoint.

---

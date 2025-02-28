package com.example.testobjects;

public class MockBook {
    private String title;
    private String author;
    private String genre;
    private String isbn;
    private String publicationDate;
    private String price;

    public MockBook(String title, String author, String genre, String isbn, String publicationDate, String price) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.price = price;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public String getIsbn() { return isbn; }
    public String getPublicationDate() { return publicationDate; }
    public String getPrice() { return price; }

    public String compareDifferences(MockBook other) {
        StringBuilder mismatches = new StringBuilder();

        if (!this.title.equals(other.title))
            mismatches.append("Title mismatch! Expected: ").append(this.title).append(", Found: ").append(other.title).append("\n");
        if (!this.author.equals(other.author))
            mismatches.append("Author mismatch! Expected: ").append(this.author).append(", Found: ").append(other.author).append("\n");
        if (!this.genre.equals(other.genre))
            mismatches.append("Genre mismatch! Expected: ").append(this.genre).append(", Found: ").append(other.genre).append("\n");
        if (!this.isbn.equals(other.isbn))
            mismatches.append("ISBN mismatch! Expected: ").append(this.isbn).append(", Found: ").append(other.isbn).append("\n");
        if (!this.publicationDate.equals(other.publicationDate))
            mismatches.append("Publication Date mismatch! Expected: ").append(this.publicationDate).append(", Found: ").append(other.publicationDate).append("\n");
        if (!this.price.equals(other.price))
            mismatches.append("Price mismatch! Expected: ").append(this.price).append(", Found: ").append(other.price).append("\n");

        return mismatches.toString().isEmpty() ? "Books match!!" : mismatches.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        MockBook book = (MockBook) obj;

        return title.equals(book.title) &&
                author.equals(book.author) &&
                genre.equals(book.genre) &&
                isbn.equals(book.isbn) &&
                publicationDate.equals(book.publicationDate) &&
                price.equals(book.price);
    }
}

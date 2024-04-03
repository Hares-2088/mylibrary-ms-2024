package com.mylibrary.books.dataacess.book;


import com.mylibrary.books.dataacess.author.AuthorIdentifier;
import jakarta.persistence.*;
import lombok.*;


import java.util.Date;

@Entity
@Table(name = "books")
@NoArgsConstructor
@Setter
@Getter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // private id

    @Embedded
    private BookIdentifier bookIdentifier;

    @Embedded
    private AuthorIdentifier authorIdentifier;

    private String title;

    private Date publicationYear;

    private String genre;

    private String description;

    private Integer availableCopies;

    public Book(String title, Date publicationYear, String genre, String description, Integer availableCopies) {
        this.bookIdentifier = new BookIdentifier();
        this.title = title;
        this.publicationYear = publicationYear;
        this.genre = genre;
        this.description = description;
        this.availableCopies = availableCopies;
    }
}
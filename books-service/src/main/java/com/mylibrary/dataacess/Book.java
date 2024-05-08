package com.mylibrary.dataacess;


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

    private  String authorName;

    private String title;

    private Date publicationYear;

    private String genre;

    private String description;

    private Integer availableCopies;

    public Book(String authorName, String title, Date publicationYear, String genre, String description, Integer availableCopies) {
        this.authorName = authorName;
        this.bookIdentifier = new BookIdentifier();
        this.title = title;
        this.publicationYear = publicationYear;
        this.genre = genre;
        this.description = description;
        this.availableCopies = availableCopies;
    }
}
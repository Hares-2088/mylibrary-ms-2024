package com.mylibrary.books.presentation.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestModel {

    private String authorId;

    private String	title;

    private Date publicationYear;

    private String genre;

    private String description;

    private Integer availableCopies;
}

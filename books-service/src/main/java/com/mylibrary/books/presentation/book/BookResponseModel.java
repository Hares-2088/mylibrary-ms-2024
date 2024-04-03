package com.mylibrary.books.presentation.book;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookResponseModel {


    private String bookId;

    private String authorId;

    private String	title;

    private Date publicationYear;

    private String genre;

    private String description;

    private Integer availableCopies;

}

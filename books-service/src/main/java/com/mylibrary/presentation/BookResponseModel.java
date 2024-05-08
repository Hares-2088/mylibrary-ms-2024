package com.mylibrary.presentation;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BookResponseModel {


    private String bookId;

    private String authorName;

    private String	title;

    private Date publicationYear;

    private String genre;

    private String description;

    private Integer availableCopies;

}

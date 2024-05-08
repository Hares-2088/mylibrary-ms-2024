package com.mylibrary.presentation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequestModel {

    private String authorName;

    private String	title;

    private Date publicationYear;

    private String genre;

    private String description;

    private Integer availableCopies;
}

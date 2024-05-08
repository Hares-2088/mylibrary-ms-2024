package com.cardealership.apigateway.presentationlayer.books;

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

    private String authorId;

    private String	title;

    private Date publicationYear;

    private String genre;

    private String description;

    private Integer availableCopies;
}

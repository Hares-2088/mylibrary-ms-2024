package com.mylibrary.books.presentation.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorRequestModel {

    private String firstName;
    private String lastName;
    private String biography;
    private String country;
    private String city;
    private String province;
}

package com.mylibrary.books.presentation.author;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthorResponseModel{

    private String authorId;
    private String firstName;
    private String lastName;
    private String biography;
    private String country;
    private String city;
    private String province;

}

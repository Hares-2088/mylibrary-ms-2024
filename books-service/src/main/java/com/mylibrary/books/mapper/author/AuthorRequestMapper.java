package com.mylibrary.books.mapper.author;


import com.mylibrary.books.dataacess.author.Author;
import com.mylibrary.books.dataacess.author.AuthorIdentifier;
import com.mylibrary.books.dataacess.author.Nationality;
import com.mylibrary.books.presentation.author.AuthorRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthorRequestMapper {

    @Mapping(target = "id", ignore = true)
    Author requestModelToEntity(AuthorRequestModel authorRequestModel, AuthorIdentifier authorIdentifier, Nationality nationality);
}

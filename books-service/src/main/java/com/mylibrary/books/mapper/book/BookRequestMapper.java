package com.mylibrary.books.mapper.book;


import com.mylibrary.books.dataacess.author.AuthorIdentifier;
import com.mylibrary.books.dataacess.book.Book;
import com.mylibrary.books.dataacess.book.BookIdentifier;
import com.mylibrary.books.presentation.book.BookRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookRequestMapper {

    @Mapping(target = "id", ignore = true)
    Book requestModelToEntity(BookRequestModel bookRequestModel, AuthorIdentifier authorIdentifier, BookIdentifier bookIdentifier);
}

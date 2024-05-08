package com.mylibrary.mapper;


import com.mylibrary.dataacess.Book;
import com.mylibrary.dataacess.BookIdentifier;
import com.mylibrary.presentation.BookRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookRequestMapper {

    @Mapping(target = "id", ignore = true)
    Book requestModelToEntity(BookRequestModel bookRequestModel, BookIdentifier bookIdentifier);
}

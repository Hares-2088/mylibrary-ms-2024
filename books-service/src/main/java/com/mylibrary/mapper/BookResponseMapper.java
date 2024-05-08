package com.mylibrary.mapper;


import com.mylibrary.dataacess.Book;
import com.mylibrary.presentation.BookResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;


@Mapper(componentModel = "spring")
public interface BookResponseMapper {

    @Mapping(expression = "java(book.getBookIdentifier().getBookId())", target = "bookId")
    BookResponseModel entityToResponseModel(Book book);

    List<BookResponseModel> entityListToResponseModelList(List<Book> books);

//    @AfterMapping
//    default void addLinks(@MappingTarget BookResponseModel model, Book book) {
//        Link selfLink = linkTo(methodOn(BookController.class).
//                getBook(model.getBookId())).withSelfRel();
//        model.add(selfLink);
//    }

}

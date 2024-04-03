package com.mylibrary.books.mapper.book;


import com.mylibrary.books.dataacess.book.Book;
import com.mylibrary.books.presentation.book.BookResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;


@Mapper(componentModel = "spring")
public interface BookResponseMapper {

    @Mapping(expression = "java(book.getBookIdentifier().getBookId())", target = "bookId")
    @Mapping(expression = "java(book.getAuthorIdentifier().getAuthorId())", target = "authorId")
    BookResponseModel entityToResponseModel(Book book);

    List<BookResponseModel> entityListToResponseModelList(List<Book> books);

//    @AfterMapping
//    default void addLinks(@MappingTarget BookResponseModel model, Book book) {
//        Link selfLink = linkTo(methodOn(BookController.class).
//                getBook(model.getBookId())).withSelfRel();
//        model.add(selfLink);
//    }

}

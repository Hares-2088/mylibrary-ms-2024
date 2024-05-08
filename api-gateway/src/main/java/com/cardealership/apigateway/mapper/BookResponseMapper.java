package com.cardealership.apigateway.mapper;

import com.cardealership.apigateway.presentationlayer.books.BookController;
import com.cardealership.apigateway.presentationlayer.books.BookResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface BookResponseMapper {
    BookResponseModel responseModelToResponseModel(BookResponseModel responseModel);

    List<BookResponseModel> responseModelListToResponseModelList(List<BookResponseModel> responseModel);

    @AfterMapping
    default void afterMapping(@MappingTarget BookResponseModel bookResponseModel) {

        //customer Link
        Link selfLink = linkTo(methodOn(BookController.class)
                .getBook(bookResponseModel.getBookId()))
                .withSelfRel();

        bookResponseModel.add(selfLink);

        //all customers link
        Link allLink = linkTo(methodOn(BookController.class)
                .getBooks())
                .withRel("all books");

        bookResponseModel.add(allLink);
    }

}

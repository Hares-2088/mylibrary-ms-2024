package com.mylibrary.books.mapper.author;


import com.mylibrary.books.dataacess.author.Author;
import com.mylibrary.books.presentation.author.AuthorResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;


@Mapper(componentModel = "spring")
public interface AuthorResponseMapper {

    @Mapping(expression = "java(author.getAuthorIdentifier().getAuthorId())", target = "authorId")
    @Mapping(expression = "java(author.getNationality().getCountry())", target = "country")
    @Mapping(expression = "java(author.getNationality().getCity())", target = "city")
    @Mapping(expression = "java(author.getNationality().getProvince())", target = "province")
    AuthorResponseModel entityToResponseModel(Author author);

    List<AuthorResponseModel> entitiesToResponseModels(List<Author> authors);

//    @AfterMapping
//    default void addLinks(@MappingTarget AuthorResponseModel model, Author author) {
//        Link selfLink = linkTo(methodOn(AuthorController.class).
//                getAuthorByAuthorId(model.getAuthorId())).withSelfRel();
//        model.add(selfLink);
//    }

}

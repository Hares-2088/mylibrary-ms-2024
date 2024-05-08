package com.cardealership.apigateway.presentationlayer.books;


import com.cardealership.apigateway.presentationlayer.members.MemberResponseModel;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BookResponseModel extends RepresentationModel<BookResponseModel> {


    private String bookId;

    private String authorId;

    private String	title;

    private Date publicationYear;

    private String genre;

    private String description;

    private Integer availableCopies;

}

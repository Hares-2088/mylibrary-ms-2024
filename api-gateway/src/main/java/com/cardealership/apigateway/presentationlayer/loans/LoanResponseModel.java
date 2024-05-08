package com.cardealership.apigateway.presentationlayer.loans;

import com.cardealership.apigateway.presentationlayer.books.BookResponseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoanResponseModel extends RepresentationModel<LoanResponseModel> {

    private String loanId;

    private String memberId;

    private String memberFirstName;

    private String memberLastName;

    private String bookId;

    private String bookTitle;

    private String availableCopies;

    private String reservationId;

    private String reservationDate;

    private Boolean returned;

    private LocalDate loanDate;

    private LocalDate dueDate;
}

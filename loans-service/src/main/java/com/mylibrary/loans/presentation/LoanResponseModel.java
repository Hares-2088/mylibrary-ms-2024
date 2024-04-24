package com.mylibrary.loans.presentation;

import com.mylibrary.loans.domainclientlayer.book.BookModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoanResponseModel{

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

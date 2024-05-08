package com.mylibrary.loans.mapper;


import com.mylibrary.loans.dataacess.Loan;
import com.mylibrary.loans.presentation.LoanResponseModel;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoanResponseMapper {

    @Mapping(expression = "java(loan.getLoanIdentifier().getLoanId())", target = "loanId")
    @Mapping(expression = "java(loan.getMemberModel().getMemberId())", target = "memberId")
    @Mapping(expression = "java(loan.getMemberModel().getReservationId())", target = "reservationId")
    @Mapping(expression = "java(loan.getMemberModel().getMemberFirstName())", target = "memberFirstName")
    @Mapping(expression = "java(loan.getMemberModel().getMemberLastName())", target = "memberLastName")
    @Mapping(expression = "java(loan.getReservationModel().getReservationDate().toString())", target = "reservationDate")
    @Mapping(expression = "java(loan.getLoanPeriod().getLoanDate())", target = "loanDate")
    @Mapping(expression = "java(loan.getLoanPeriod().getDueDate())", target = "dueDate")
    @Mapping(expression = "java(loan.getBookModel().getBookId())", target = "bookId")
    @Mapping(expression = "java(loan.getBookModel().getTitle())", target = "bookTitle")
    @Mapping(expression = "java(loan.getBookModel().getAvailableCopies())", target = "availableCopies")
    LoanResponseModel entityToResponseModel(Loan loan);

}

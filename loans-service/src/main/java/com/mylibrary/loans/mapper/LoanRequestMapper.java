package com.mylibrary.loans.mapper;

import com.mylibrary.loans.dataacess.Loan;
import com.mylibrary.loans.dataacess.LoanIdentifier;
import com.mylibrary.loans.dataacess.LoanPeriod;
import com.mylibrary.loans.domainclientlayer.book.BookModel;
import com.mylibrary.loans.domainclientlayer.member.MemberModel;
import com.mylibrary.loans.domainclientlayer.reservation.ReservationModel;
import com.mylibrary.loans.presentation.LoanRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoanRequestMapper {

    @Mapping(target = "id", ignore = true)
    Loan requestModelToEntity(LoanRequestModel loanRequestModel, LoanIdentifier loanIdentifier,
                              MemberModel memberModel, BookModel bookModel,
                              ReservationModel reservationModel, LoanPeriod loanPeriod);
}

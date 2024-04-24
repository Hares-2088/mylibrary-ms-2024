package com.mylibrary.loans.dataacess;

import com.mylibrary.loans.domainclientlayer.book.BookModel;
import com.mylibrary.loans.domainclientlayer.member.MemberModel;
import com.mylibrary.loans.domainclientlayer.reservation.ReservationModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "loans")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Loan {

    @Id
    private String id;

    @Indexed(unique = true)
    private LoanIdentifier loanIdentifier;
    private MemberModel memberModel;
    private BookModel bookModel;
    private ReservationModel reservationModel;

    private Boolean returned;
    private LoanPeriod loanPeriod;

    public Loan(MemberModel memberModel, BookModel bookModel, ReservationModel reservationModel, Boolean returned, LoanPeriod loanPeriod) {
        this.loanIdentifier = new LoanIdentifier();
        this.memberModel = memberModel;
        this.bookModel = bookModel;
        this.reservationModel = reservationModel;
        this.returned = returned;
        this.loanPeriod = loanPeriod;
    }

}

package com.mylibrary.loans.utils;
import com.mylibrary.loans.dataacess.Loan;
import com.mylibrary.loans.dataacess.LoanIdentifier;
import com.mylibrary.loans.dataacess.LoanPeriod;
import com.mylibrary.loans.dataacess.LoanRepository;
import com.mylibrary.loans.domainclientlayer.book.BookModel;
import com.mylibrary.loans.domainclientlayer.member.MemberModel;
import com.mylibrary.loans.domainclientlayer.reservation.ReservationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DatabaseLoaderService implements CommandLineRunner {

    @Autowired
    LoanRepository loanRepository;

    @Override
    public void run(String... args) throws Exception {

        var loanIdentifier = new LoanIdentifier();
        var loanIdentifier2 = new LoanIdentifier();

        //create a book model
        BookModel bookModel = BookModel.builder()
                .bookId("b1")
                .title("Sample Book 1")
                .availableCopies(5)
                .build();

        //create a member model
        MemberModel memberModel = MemberModel.builder()
                .memberId("m1")
                .memberFirstName("Adem")
                .memberLastName("Doe")
                .reservationId("r1")
                .build();

        //create a member model
        MemberModel memberModel2 = MemberModel.builder()
                .memberId("m2")
                .memberFirstName("Jane")
                .memberLastName("Smith")
                .reservationId("r2")
                .build();

        //create a reservation model
        ReservationModel reservationModel = ReservationModel.builder()
                .reservationId("r1")
                .bookId("b1")
                .memberId("m1")
                .reservationDate(LocalDate.now())
                .build();

        //create a reservation model
        ReservationModel reservationModel2 = ReservationModel.builder()
                .reservationId("r2")
                .bookId("b2")
                .memberId("m2")
                .reservationDate(LocalDate.now())
                .build();

        //create a loan model
        Loan loan = Loan.builder()
                .loanIdentifier(loanIdentifier2)
                .bookModel(bookModel)
                .memberModel(memberModel)
                .reservationModel(reservationModel)
                .returned(false)
                .loanPeriod(new LoanPeriod())
                .build();

        //create a loan model
        Loan loan2 = Loan.builder()
                .loanIdentifier(loanIdentifier)
                .bookModel(bookModel)
                .memberModel(memberModel2)
                .reservationModel(reservationModel2)
                .returned(false)
                .loanPeriod(new LoanPeriod())
                .build();


        loanRepository.save(loan);
        loanRepository.save(loan2);
    }
}

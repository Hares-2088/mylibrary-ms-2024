package com.mylibrary.loans.dataacess;

import com.mylibrary.loans.domainclientlayer.book.BookModel;
import com.mylibrary.loans.domainclientlayer.member.MemberModel;
import com.mylibrary.loans.domainclientlayer.reservation.ReservationModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
class LoanRepositoryIntegrationTest {

    @Autowired
    private LoanRepository loanRepository;

    @BeforeEach
    public void setupDb(){
        loanRepository.deleteAll();
    }

    @Test
    public void whenMemberIdExists_ReturnLoans() {
        //arrange
        Loan loan = LoanBuilder();
        Loan loan2 = LoanBuilder();

        //act
        loanRepository.save(loan);
        loanRepository.save(loan2);

        List<Loan> loans =  loanRepository.findAllByMemberModel_MemberId("m1");

        //assert
        assertNotNull(loans);
        assertEquals(2, loans.size());
    }

    @Test
public void whenMemberIdDoesNotExists_ReturnEmptyList() {
        //arrange
        Loan loan = LoanBuilder();
        Loan loan2 = LoanBuilder();
        //act
        loanRepository.save(loan);
        loanRepository.save(loan2);

        List<Loan> loans =  loanRepository.findAllByMemberModel_MemberId("m2");

        //assert
        assertNotNull(loans);
        assertEquals(0, loans.size());
    }

    @Test
    public void whenLoanIdAndMemberIdExists_ReturnLoan() {
        //arrange
        Loan loan = LoanBuilder();

        //act
        loanRepository.save(loan);

        Loan loanFound =  loanRepository.findLoanByLoanIdentifier_LoanIdAndMemberModel_MemberId(loan.getLoanIdentifier().getLoanId(), loan.getMemberModel().getMemberId());

        //assert
        assertNotNull(loanFound);
        assertEquals(loan.getLoanIdentifier().getLoanId(), loanFound.getLoanIdentifier().getLoanId());
    }

    @Test
    public void whenLoanIdAndMemberIdDoesNotExists_ReturnNull() {
        //arrange
        Loan loan = LoanBuilder();

        //act
        loanRepository.save(loan);

        Loan loanFound =  loanRepository.findLoanByLoanIdentifier_LoanIdAndMemberModel_MemberId("l2", "m2");

        //assert
        assertNull(loanFound);
    }

    //Create the loan builder class
    public Loan LoanBuilder() {
        var loanIdentifier = new LoanIdentifier();

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
                .reservationId("res01")
                .build();

        //create a reservation model
        ReservationModel reservationModel = ReservationModel.builder()
                .reservationId("res01")
                .bookId("b1")
                .memberId("m1")
                .reservationDate(LocalDate.now())
                .build();

        //create a loan model
        Loan loan = Loan.builder()
                .loanIdentifier(loanIdentifier)
                .bookModel(bookModel)
                .memberModel(memberModel)
                .reservationModel(reservationModel)
                .returned(false)
                .loanPeriod(new LoanPeriod())
                .build();
        return loan;
    }


}
package com.mylibrary.loans.business;


import com.mylibrary.loans.presentation.LoanRequestModel;
import com.mylibrary.loans.dataacess.Loan;
import com.mylibrary.loans.dataacess.LoanIdentifier;
import com.mylibrary.loans.dataacess.LoanPeriod;
import com.mylibrary.loans.dataacess.LoanRepository;
import com.mylibrary.loans.domainclientlayer.book.BookModel;
import com.mylibrary.loans.domainclientlayer.book.BookServiceClient;
import com.mylibrary.loans.domainclientlayer.member.MemberModel;
import com.mylibrary.loans.domainclientlayer.member.MemberServiceClient;
import com.mylibrary.loans.domainclientlayer.reservation.ReservationModel;
import com.mylibrary.loans.domainclientlayer.reservation.ReservationServiceClient;
import com.mylibrary.loans.mapper.LoanResponseMapper;
import com.mylibrary.loans.presentation.LoanResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;


import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@SpringBootTest
class LoanServiceUnitTest {

    @Autowired
    LoanService loanService;

    @MockBean
    LoanRepository loanRepository;

    @MockBean
    MemberServiceClient memberServiceClient;

    @MockBean
    BookServiceClient bookServiceClient;

    @MockBean
    ReservationServiceClient reservationServiceClient;

    @SpyBean
    LoanResponseMapper loanResponseMapper;

    //Test the post method
    @Test
    public void whenPostLoan_thenReturnsLoan() {
        // Arrange
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
                .reservationId("r1")
                .build();

        //create a reservation model
        ReservationModel reservationModel = ReservationModel.builder()
                .reservationId("r1")
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

        var loanRequestModel = LoanRequestModel.builder()
                .bookId("b1")
                .returned(false)
                .build();

        when(bookServiceClient.getBookByBookId("b1")).thenReturn(bookModel);
        when(memberServiceClient.getMemberByMemberId("m1")).thenReturn(memberModel);
        when(reservationServiceClient.getReservationByReservationId("r1", "m1")).thenReturn(reservationModel);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        // Act
        loanService.createLoan(loanRequestModel, memberModel.getMemberId());

        // Assert
        verify(loanRepository, times(3)).save(any(Loan.class));

        assertEquals(loan.getBookModel().getBookId(), bookModel.getBookId());
        assertEquals(loan.getMemberModel().getMemberId(), memberModel.getMemberId());
        assertEquals(loan.getReservationModel().getReservationId(), reservationModel.getReservationId());
        assertEquals(loan.getReturned(), false);
    }

    //Test the get method
    @Test
    public void whenGetLoan_thenReturnsLoan() {
        // Arrange
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
                .reservationId("r1")
                .build();

        //create a reservation model
        ReservationModel reservationModel = ReservationModel.builder()
                .reservationId("r1")
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

        when(loanRepository.findLoanByLoanIdentifier_LoanIdAndMemberModel_MemberId(loan.getLoanIdentifier().getLoanId(), memberModel.getMemberId())).thenReturn(loan);
        when(memberServiceClient.getMemberByMemberId("m1")).thenReturn(memberModel);
        when(bookServiceClient.getBookByBookId("b1")).thenReturn(bookModel);


        // Act
        LoanResponseModel result = loanService.getLoan(loan.getLoanIdentifier().getLoanId(), memberModel.getMemberId());

        // Assert
        assertEquals(loan.getBookModel().getBookId(), result.getBookId());
        assertEquals(loan.getMemberModel().getMemberId(), result.getMemberId());
        assertEquals(loan.getReservationModel().getReservationId(), result.getReservationId());
        assertEquals(loan.getReturned(), result.getReturned());
    }

    //test the get all method
    @Test
    public void whenGetLoans_thenReturnsLoans() {
        // Arrange
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
                .reservationId("r1")
                .build();

        //create a reservation model
        ReservationModel reservationModel = ReservationModel.builder()
                .reservationId("r1")
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

        when(memberServiceClient.getMemberByMemberId("m1")).thenReturn(memberModel);
        when(loanRepository.findAllByMemberModel_MemberId(memberModel.getMemberId())).thenReturn(List.of(loan));

        // Act
        List<LoanResponseModel> result = loanService.getLoans(memberModel.getMemberId());

        // Assert
        assertEquals(loan.getBookModel().getBookId(), result.get(0).getBookId());
        assertEquals(loan.getMemberModel().getMemberId(), result.get(0).getMemberId());
        assertEquals(loan.getReservationModel().getReservationId(), result.get(0).getReservationId());
        assertEquals(loan.getReturned(), result.get(0).getReturned());
    }



    //test the delete method
    @Test
    public void whenDeleteLoan_thenReturnsVoid() {
        // Arrange
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
                .reservationId("r1")
                .build();

        //create a reservation model
        ReservationModel reservationModel = ReservationModel.builder()
                .reservationId("r1")
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

        when(loanRepository.findLoanByLoanIdentifier_LoanIdAndMemberModel_MemberId(loan.getLoanIdentifier().getLoanId(), memberModel.getMemberId())).thenReturn(loan);
        when(memberServiceClient.getMemberByMemberId(memberModel.getMemberId())).thenReturn(memberModel);
        when(reservationServiceClient.getReservationByReservationId(reservationModel.getReservationId(), memberModel.getMemberId())).thenReturn(reservationModel);

        // Act
        loanService.deleteLoan(loan.getLoanIdentifier().getLoanId(), memberModel.getMemberId());

        // Assert
        verify(loanRepository, times(1)).delete(loan);
    }

    //test the delete all
    @Test
    public void whenDeleteLoans_thenReturnsVoid() {
        // Arrange
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
                .reservationId("r1")
                .build();

        //create a reservation model
        ReservationModel reservationModel = ReservationModel.builder()
                .reservationId("r1")
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

        when(memberServiceClient.getMemberByMemberId(memberModel.getMemberId())).thenReturn(memberModel);
        when(loanRepository.findAllByMemberModel_MemberId(memberModel.getMemberId())).thenReturn(List.of(loan));

        // Act
        loanService.deleteLoans(memberModel.getMemberId());

        // Assert
        verify(loanRepository, times(1)).delete(loan);
    }
}
package com.mylibrary.loans.business;

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
import com.mylibrary.loans.mapper.LoanRequestMapper;
import com.mylibrary.loans.mapper.LoanResponseMapper;
import com.mylibrary.loans.presentation.LoanRequestModel;
import com.mylibrary.loans.presentation.LoanResponseModel;
import com.mylibrary.reservations.utils.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService{

    private final LoanRepository loanRepository;
    private final LoanResponseMapper loanResponseMapper;
    private final LoanRequestMapper loanRequestMapper;

    // Add the following service client attributes
    private final MemberServiceClient memberServiceClient;
    private final BookServiceClient bookServiceClient;
    private final ReservationServiceClient reservationServiceClient;

    public LoanServiceImpl(LoanRepository loanRepository, LoanResponseMapper loanResponseMapper, LoanRequestMapper loanRequestMapper, MemberServiceClient memberServiceClient, BookServiceClient bookServiceClient, ReservationServiceClient reservationServiceClient) {
        this.loanRepository = loanRepository;
        this.loanResponseMapper = loanResponseMapper;
        this.loanRequestMapper = loanRequestMapper;

        this.memberServiceClient = memberServiceClient;
        this.bookServiceClient = bookServiceClient;
        this.reservationServiceClient = reservationServiceClient;
    }

    @Override
    public List<LoanResponseModel> getLoans(String memberId) {

        // find the member using the member id
        MemberModel member = memberServiceClient.getMemberByMemberId(memberId);

        //check if the member exists
        if (member == null){
            throw new NotFoundException("This member doesn't exist" + memberId);
        }

        // return all the loans of the member
        return loanRepository.findAllByMemberModel_MemberId(memberId).stream()
                .map(loanResponseMapper::entityToResponseModel)
                .toList();
    }

    @Override
    public LoanResponseModel getLoan(String loanId, String memberId) {

        //find the loan using the loan id and the member id
        Loan loan = loanRepository.findLoanByLoanIdentifier_LoanIdAndMemberModel_MemberId(loanId, memberId);

        //check if the loan exists
        MemberModel member = memberServiceClient.getMemberByMemberId(memberId);
        if ( member == null) {
            throw new NotFoundException("Unknown memberId: " + memberId);
        }

        //check if the book exists
        BookModel book = bookServiceClient.getBookByBookId(loan.getBookModel().getBookId());
        if ( book == null) {
            throw new NotFoundException("Unknown bookId: " + loan.getBookModel().getBookId());
        }

        // return the loan response model
        return loanResponseMapper.entityToResponseModel(loan);
    }

    @Override
    public LoanResponseModel createLoan(LoanRequestModel loanRequestModel, String memberId) {

        //find the member using the member id and check if the member exists
        MemberModel member = memberServiceClient.getMemberByMemberId(memberId);
        if ( member == null) {
            throw new NotFoundException("Unknown memberId: " + memberId);
        }

        //find the book using the book id and check if the book exists
        BookModel book = bookServiceClient.getBookByBookId(loanRequestModel.getBookId());
        if ( book == null) {
            throw new NotFoundException("Unknown bookId: " + loanRequestModel.getBookId());
        }

        //find the reservation using the reservation id and check if the reservation exists
        ReservationModel reservation = reservationServiceClient.getReservationByReservationId(member.getReservationId());
        if ( reservation == null) {
            throw new NotFoundException("The member needs to have a reservation before asking for a loan");
        }

        //check if the book id in the reservation matches the book id in the loan request
        //if not, throw an exception
        if ( !reservation.getBookId().equals(loanRequestModel.getBookId())) {
            throw new NotFoundException("The bookId in the reservation does not match the bookId in the loan request");
        }

        //create the loan and return the loan response model
        Loan loan = loanRequestMapper.requestModelToEntity(loanRequestModel, new LoanIdentifier(), member,
                                                            book, reservation, new LoanPeriod());
        return loanResponseMapper.entityToResponseModel(loanRepository.save(loan));
    }

    @Override
    public LoanResponseModel updateLoan(LoanRequestModel loanRequestModel, String loanId, String memberId) {

        //find the loan using the loan id and the member id and check if the loan exists
        Loan loan = loanRepository.findLoanByLoanIdentifier_LoanIdAndMemberModel_MemberId(loanId, memberId);
        if ( loan == null) {
            throw new NotFoundException("Unknown loanId: " + loanId);
        }

        //find the member using the member id and check if the member exists
        MemberModel member = memberServiceClient.getMemberByMemberId(memberId);
        if ( member == null) {
            throw new NotFoundException("Unknown memberId: " + memberId);
        }

        //find the book using the book id and check if the book exists
        BookModel book = bookServiceClient.getBookByBookId(loan.getBookModel().getBookId());
        if ( book == null) {
            throw new NotFoundException("Unknown bookId: " + loanRequestModel.getBookId());
        }

        //find the reservation using the reservation id and check if the reservation exists
        ReservationModel reservation = reservationServiceClient.getReservationByReservationId(loan.getReservationModel().getReservationId());

        //create the updated loan, set the id, and return the loan response model
        Loan updatedLoan = loanRequestMapper.requestModelToEntity(loanRequestModel, loan.getLoanIdentifier(), member, book,
                reservation, new LoanPeriod());
        updatedLoan.setId(loan.getId());
        return loanResponseMapper.entityToResponseModel(loanRepository.save(updatedLoan));
    }

    @Override
    public void deleteLoan(String loanId, String memberId) {

        //find the loan using the loan id and the member id and check if the loan exists
        Loan loan = loanRepository.findLoanByLoanIdentifier_LoanIdAndMemberModel_MemberId(loanId, memberId);

        //find the member using the member id and check if the member exists
        ReservationModel reservation = reservationServiceClient.getReservationByReservationId(loan.getReservationModel().getReservationId());
        if ( reservation == null) {
            throw new NotFoundException("Unknown loanId: " + loanId);
        }

        //check if the loan and the member exist
        MemberModel member = memberServiceClient.getMemberByMemberId(memberId);
        if ( member != null) {
            throw new NotFoundException("Unknown memberId: " + memberId);
        }

        //delete the reservation and the loan
        reservationServiceClient.deleteReservation(reservation.getReservationId());
        loanRepository.delete(loan);
    }
}

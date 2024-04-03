package com.mylibrary.reservations.business;


import com.mylibrary.reservations.dataacess.Reservation;
import com.mylibrary.reservations.dataacess.ReservationIdentifier;
import com.mylibrary.reservations.dataacess.ReservationRepository;
import com.mylibrary.reservations.mapper.ReservationRequestMapper;
import com.mylibrary.reservations.mapper.ReservationResponseMapper;
import com.mylibrary.reservations.presentation.ReservationRequestModel;
import com.mylibrary.reservations.presentation.ReservationResponseModel;
import com.mylibrary.reservations.utils.exceptions.InvalidDateException;
import com.mylibrary.reservations.utils.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationRequestMapper reservationRequestMapper;
    private final ReservationResponseMapper reservationResponseMapper;
//    private final BookRepository bookRepository;
//    private final BookResponseMapper bookResponseMapper;
//    private final MemberRepository memberRepository;
//    private final MemberResponseMapper memberResponseMapper;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository, ReservationRequestMapper reservationRequestMapper, ReservationResponseMapper reservationResponseMapper) {
        this.reservationRepository = reservationRepository;
        this.reservationRequestMapper = reservationRequestMapper;
        this.reservationResponseMapper = reservationResponseMapper;
//        this.bookRepository = bookRepository;
//        this.bookResponseMapper = bookResponseMapper;
//        this.memberRepository = memberRepository;
//        this.memberResponseMapper = memberResponseMapper;
    }

    @Override
    public List<ReservationResponseModel> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();

        List<ReservationResponseModel> reservationResponseModels = new ArrayList<>();

        for (Reservation reservation : reservations) {
//            BookResponseModel bookResponseModel = bookResponseMapper.entityToResponseModel(bookRepository.findByBookIdentifier_BookId(reservation.getBookIdentifier().getBookId()));
//            Book book = bookRepository.findByBookIdentifier_BookId(reservation.getBookIdentifier().getBookId());
//
//            MemberResponseModel memberResponseModel = memberResponseMapper.entityToResponseModel(memberRepository.findByMemberIdentifier_MemberId(reservation.getMemberIdentifier().getMemberId()));
//            Member member = memberRepository.findByMemberIdentifier_MemberId(reservation.getMemberIdentifier().getMemberId());

//            if (book != null && member != null) {
//                reservationResponseModels.add(reservationResponseMapper.entityToResponseModel(reservation, bookResponseModel, memberResponseModel, bookRepository.findByBookIdentifier_BookId(reservation.getBookIdentifier().getBookId()), memberRepository.findByMemberIdentifier_MemberId(reservation.getMemberIdentifier().getMemberId())));
//            }
              reservationResponseModels.add(reservationResponseMapper.entityToResponseModel(reservation));

        }
        return reservationResponseModels;
    }

    @Override
    public ReservationResponseModel getReservationByReservationId(String reservationId) {
        Reservation reservation = reservationRepository.findByReservationIdentifier_ReservationId(reservationId);
        if (reservation == null) {
            throw new NotFoundException("Unknown reservationId: " + reservationId);
        }

//        BookResponseModel bookResponseModel = bookResponseMapper.entityToResponseModel(bookRepository.findByBookIdentifier_BookId(reservation.getBookIdentifier().getBookId()));
//        MemberResponseModel memberResponseModel = memberResponseMapper.entityToResponseModel(memberRepository.findByMemberIdentifier_MemberId(reservation.getMemberIdentifier().getMemberId()));

        return reservationResponseMapper.entityToResponseModel(reservation);
    }

//    @Override
//    public List<ReservationResponseModel> getReservations(String memberId) {
//        List<Reservation> reservations = reservationRepository.findAllByMemberIdentifier_MemberId(memberId);
//
//        List<ReservationResponseModel> reservationResponseModels = new ArrayList<>();
//
//        for (Reservation reservation : reservations) {
////            BookResponseModel bookResponseModel = bookResponseMapper.entityToResponseModel(bookRepository.findByBookIdentifier_BookId(reservation.getBookIdentifier().getBookId()));
////            MemberResponseModel memberResponseModel = memberResponseMapper.entityToResponseModel(memberRepository.findByMemberIdentifier_MemberId(memberId));
//            reservationResponseModels.add(reservationResponseMapper.entityToResponseModel(reservation));
//        }
//        return reservationResponseModels;
//    }
//
//    @Override
//    public ReservationResponseModel getReservation(String reservationId) {
//        Reservation reservation = reservationRepository.findByReservationIdentifier_ReservationId(reservationId);
//        if (reservation == null) {
//            throw new NotFoundException("Unknown reservationId: " + reservationId);
//        }
//
////        BookResponseModel bookResponseModel = bookResponseMapper.entityToResponseModel(bookRepository.findByBookIdentifier_BookId(reservation.getBookIdentifier().getBookId()));
////        MemberResponseModel memberResponseModel = memberResponseMapper.entityToResponseModel(memberRepository.findByMemberIdentifier_MemberId(memberId));
//
//        return reservationResponseMapper.entityToResponseModel(reservation);
//    }

    @Override
    public ReservationResponseModel createReservation(ReservationRequestModel reservationRequestModel) {

        //check if the date is in the future using the invalid date exception
        if (reservationRequestModel.getReservationDate().before(new Date())) {
            throw new InvalidDateException("This date is in the past: " + reservationRequestModel.getReservationDate());
        }

//        Book book = bookRepository.findByBookIdentifier_BookId(reservationRequestModel.getBookId());
//        if (book == null) {
//            throw new NotFoundException("Unknown bookId: " + reservationRequestModel.getBookId());
//        }
//        // Update the book available copies
//        book.setAvailableCopies(book.getAvailableCopies() - 1);
//
//        if (book.getAvailableCopies() <= 0) {
//            throw new NotEnoughCopiesException("No available copies for bookId: " + reservationRequestModel.getBookId());
//        }
//
//        Member member = memberRepository.findByMemberIdentifier_MemberId(memberId);
//        if (member == null) {
//            throw new NotFoundException("Unknown memberId: " + memberId);
//        }

        Reservation reservation = reservationRequestMapper.requestModelToEntity(reservationRequestModel, new ReservationIdentifier());

        return reservationResponseMapper.entityToResponseModel(reservationRepository.save(reservation));
    }

    @Override
    public ReservationResponseModel updateReservation(ReservationRequestModel reservationRequestModel, String reservationId) {
        Reservation reservation = reservationRepository.findByReservationIdentifier_ReservationId(reservationId);
        if (reservation == null) {
            throw new NotFoundException("Unknown reservationId: " + reservationId);
        }

        // Update the book available copies
//        Book previousBook = bookRepository.findByBookIdentifier_BookId(reservation.getBookIdentifier().getBookId());
//
//        Book newBook = bookRepository.findByBookIdentifier_BookId(reservationRequestModel.getBookId());
//        if (newBook == null) {
//            throw new NotFoundException("Unknown bookId: " + reservationRequestModel.getBookId());
//        }
//        if(!Objects.equals(reservation.getBookIdentifier().getBookId(), reservationRequestModel.getBookId())) {
//            newBook.setAvailableCopies(newBook.getAvailableCopies() - 1);
//            previousBook.setAvailableCopies(previousBook.getAvailableCopies() + 1);
//        }
//
//        Member member = memberRepository.findByMemberIdentifier_MemberId(memberId);

        Reservation updatedReservation = reservationRequestMapper.requestModelToEntity(reservationRequestModel, new ReservationIdentifier());
        updatedReservation.setId(reservation.getId());

        return reservationResponseMapper.entityToResponseModel(reservationRepository.save(updatedReservation));
    }

    @Override
    public void deleteReservation(String reservationId) {

        Reservation reservation = reservationRepository.findByReservationIdentifier_ReservationId(reservationId);
        if (reservation == null) {
            throw new NotFoundException("Unknown reservationId: " + reservationId);
        }
        // Update the book available copies
//        Book book = bookRepository.findByBookIdentifier_BookId(reservation.getBookIdentifier().getBookId());
//        book.setAvailableCopies(book.getAvailableCopies() + 1);
        reservationRepository.delete(reservation);

    }
}

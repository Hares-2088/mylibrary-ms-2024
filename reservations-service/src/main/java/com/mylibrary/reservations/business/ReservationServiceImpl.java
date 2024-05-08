package com.mylibrary.reservations.business;


import com.mylibrary.reservations.dataacess.Reservation;
import com.mylibrary.reservations.dataacess.ReservationIdentifier;
import com.mylibrary.reservations.dataacess.ReservationRepository;
import com.mylibrary.reservations.domainclientlayer.BookModel;
import com.mylibrary.reservations.domainclientlayer.BookServiceClient;
import com.mylibrary.reservations.domainclientlayer.MemberModel;
import com.mylibrary.reservations.domainclientlayer.MemberServiceClient;
import com.mylibrary.reservations.mapper.ReservationRequestMapper;
import com.mylibrary.reservations.mapper.ReservationResponseMapper;
import com.mylibrary.reservations.presentation.ReservationRequestModel;
import com.mylibrary.reservations.presentation.ReservationResponseModel;
import com.mylibrary.reservations.utils.exceptions.InvalidDateException;
import com.mylibrary.reservations.utils.exceptions.NotEnoughCopiesException;
import com.mylibrary.reservations.utils.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationRequestMapper reservationRequestMapper;
    private final ReservationResponseMapper reservationResponseMapper;
    private final BookServiceClient bookServiceClient;
    private final MemberServiceClient memberServiceClient;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository, ReservationRequestMapper reservationRequestMapper, ReservationResponseMapper reservationResponseMapper, BookServiceClient bookServiceClient, MemberServiceClient memberServiceClient) {
        this.reservationRepository = reservationRepository;
        this.reservationRequestMapper = reservationRequestMapper;
        this.reservationResponseMapper = reservationResponseMapper;

        this.bookServiceClient = bookServiceClient;
        this.memberServiceClient = memberServiceClient;
    }

    @Override
    public List<ReservationResponseModel> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();

        List<ReservationResponseModel> reservationResponseModels = new ArrayList<>();

        for (Reservation reservation : reservations) {
              reservationResponseModels.add(reservationResponseMapper.entityToResponseModel(reservation));
        }
        return reservationResponseModels;
    }

    @Override
    public ReservationResponseModel getReservationByReservationId(String reservationId, String memberId) {
        Reservation reservation = reservationRepository.findByReservationIdentifier_ReservationIdAndMemberId(reservationId, memberId);
        if (reservation == null) {
            throw new NotFoundException("Unknown reservationId: " + reservationId);
        }
        return reservationResponseMapper.entityToResponseModel(reservation);
    }


    @Override
    public ReservationResponseModel createReservation(ReservationRequestModel reservationRequestModel, String memberId) {

        //check if the date is in the future using the invalid date exception
        if (reservationRequestModel.getReservationDate().isBefore(LocalDate.now())){
            throw new InvalidDateException("This date is in the past: " + reservationRequestModel.getReservationDate());
        }

        BookModel book = bookServiceClient.getBookByBookId(reservationRequestModel.getBookId());
        if (book == null) {
            throw new NotFoundException("Unknown bookId: " + reservationRequestModel.getBookId());
        }

        MemberModel member = memberServiceClient.getMemberByMemberId(memberId);
        if (member == null) {
            throw new NotFoundException("Unknown memberId: " + memberId);
        }

        if (book.getAvailableCopies() <= 0) {
            throw new NotEnoughCopiesException("No available copies for bookId: " + reservationRequestModel.getBookId());
        }

        // Update the book available copies
//        bookServiceClient.reduceBookAvailability(reservationRequestModel.getBookId());

        //Create a new reservation
        Reservation reservation = reservationRequestMapper.requestModelToEntity(reservationRequestModel, new ReservationIdentifier());

        //patch the reservation id to the member
        memberServiceClient.patchMemberWithReservationId(memberId, reservation.getReservationIdentifier().getReservationId());

        //save the reservation
        return reservationResponseMapper.entityToResponseModel(reservationRepository.save(reservation));
    }

    @Override
    public ReservationResponseModel updateReservation(ReservationRequestModel reservationRequestModel, String reservationId, String memberId) {
        //find the reservation
        Reservation reservation = reservationRepository.findByReservationIdentifier_ReservationIdAndMemberId(reservationId, memberId);
        if (reservation == null) {
            throw new NotFoundException("Unknown reservationId: " + reservationId);
        }

        //check if the date is in the future using the invalid date exception
        if (reservationRequestModel.getReservationDate().isBefore(LocalDate.now())){
            throw new InvalidDateException("This date is in the past: " + reservationRequestModel.getReservationDate());
        }

        // Update the book available copies
//        BookModel newBook = bookServiceClient.getBookByBookId(reservationRequestModel.getBookId());
//        if (newBook == null) {
//            throw new NotFoundException("Unknown bookId: " + reservationRequestModel.getBookId());
//        }
//        if(!Objects.equals(reservation.getBookId(), reservationRequestModel.getBookId())) {
//            //Check if the new book has available copies
//            if (newBook.getAvailableCopies() <= 0) {
//                throw new NotEnoughCopiesException("No available copies for bookId: " + reservationRequestModel.getBookId());
//            }
//
//            bookServiceClient.increaseBookAvailability(reservation.getBookId());
//            bookServiceClient.reduceBookAvailability(reservationRequestModel.getBookId());
//        }

        Reservation updatedReservation = reservationRequestMapper.requestModelToEntity(reservationRequestModel, new ReservationIdentifier());
        updatedReservation.setId(reservation.getId());

        return reservationResponseMapper.entityToResponseModel(reservationRepository.save(updatedReservation));
    }

    @Override
    public void deleteReservation(String reservationId, String memberId) {

        Reservation reservation = reservationRepository.findByReservationIdentifier_ReservationIdAndMemberId(reservationId, memberId);
        if (reservation == null) {
            throw new NotFoundException("Unknown reservationId: " + reservationId);
        }
        // Update the book available copies
//        bookServiceClient.increaseBookAvailability(reservation.getBookId());
        reservationRepository.delete(reservation);

    }
}

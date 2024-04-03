package com.mylibrary.reservations.business;



import com.mylibrary.reservations.presentation.ReservationRequestModel;
import com.mylibrary.reservations.presentation.ReservationResponseModel;

import java.util.List;

public interface ReservationService {

    List<ReservationResponseModel> getAllReservations();
    ReservationResponseModel getReservationByReservationId(String reservationId);
//    List<ReservationResponseModel> getReservations(String memberId);
//    ReservationResponseModel getReservation(String reservationId, String memberId);
    ReservationResponseModel createReservation(ReservationRequestModel reservationRequestModel);
    ReservationResponseModel updateReservation(ReservationRequestModel reservationRequestModel, String reservationId);
    void deleteReservation(String reservationId);
}

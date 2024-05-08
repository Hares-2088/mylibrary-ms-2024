package com.mylibrary.reservations.business;



import com.mylibrary.reservations.presentation.ReservationRequestModel;
import com.mylibrary.reservations.presentation.ReservationResponseModel;

import java.util.List;

public interface ReservationService {

    List<ReservationResponseModel> getAllReservations();
    ReservationResponseModel getReservationByReservationId(String reservationId, String memberId);
    ReservationResponseModel createReservation(ReservationRequestModel reservationRequestModel, String memberId);
    ReservationResponseModel updateReservation(ReservationRequestModel reservationRequestModel, String reservationId, String memberId);
    void deleteReservation(String reservationId, String memberId);
}

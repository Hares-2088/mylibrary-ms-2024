package com.cardealership.apigateway.businesslayer.reservations;

import com.cardealership.apigateway.presentationlayer.reservations.ReservationRequestModel;
import com.cardealership.apigateway.presentationlayer.reservations.ReservationResponseModel;

import java.util.List;

public interface ReservationService {

    List<ReservationResponseModel> getAllReservations();
    ReservationResponseModel getReservationByReservationId(String reservationId, String memberId);
    ReservationResponseModel createReservation(ReservationRequestModel reservationRequestModel, String memberId);
    void updateReservation(ReservationRequestModel reservationRequestModel, String reservationId, String memberId);
    void deleteReservation(String reservationId, String memberId);
}

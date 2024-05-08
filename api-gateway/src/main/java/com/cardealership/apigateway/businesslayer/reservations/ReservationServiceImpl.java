package com.cardealership.apigateway.businesslayer.reservations;

import com.cardealership.apigateway.domainclientlayer.reservation.ReservationServiceClient;
import com.cardealership.apigateway.mapper.ReservationResponseMapper;
import com.cardealership.apigateway.presentationlayer.reservations.ReservationRequestModel;
import com.cardealership.apigateway.presentationlayer.reservations.ReservationResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationResponseMapper reservationResponseMapper;

    private final ReservationServiceClient reservationServiceClient;

    public ReservationServiceImpl(ReservationResponseMapper reservationResponseMapper, ReservationServiceClient reservationServiceClient) {
        this.reservationResponseMapper = reservationResponseMapper;
        this.reservationServiceClient = reservationServiceClient;
    }

    @Override
    public List<ReservationResponseModel> getAllReservations() {
        return reservationResponseMapper.responseModelListToResponseModelList(reservationServiceClient.getAllReservations());
    }

    @Override
    public ReservationResponseModel getReservationByReservationId(String reservationId, String memberId) {
        return reservationResponseMapper.responseModelToResponseModel(reservationServiceClient.getReservationByReservationId(reservationId, memberId));
    }

    @Override
    public ReservationResponseModel createReservation(ReservationRequestModel reservationRequestModel, String memberId) {
        return reservationResponseMapper.responseModelToResponseModel(reservationServiceClient.createReservation(reservationRequestModel, memberId));
    }

    @Override
    public void updateReservation(ReservationRequestModel reservationRequestModel, String reservationId, String memberId) {
        reservationServiceClient.updateReservation(reservationRequestModel, memberId, reservationId);
    }

    @Override
    public void deleteReservation(String reservationId, String memberId) {
        reservationServiceClient.deleteReservation(reservationId, memberId);
    }
}

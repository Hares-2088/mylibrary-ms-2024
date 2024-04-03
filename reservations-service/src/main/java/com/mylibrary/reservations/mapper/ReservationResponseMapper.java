package com.mylibrary.reservations.mapper;


import com.mylibrary.reservations.dataacess.Reservation;
import com.mylibrary.reservations.presentation.ReservationResponseModel;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.awt.print.Book;


@Mapper(componentModel = "spring")
public interface ReservationResponseMapper {

    @Mapping(expression = "java(reservation.getReservationIdentifier().getReservationId())", target = "reservationId")
    ReservationResponseModel entityToResponseModel(Reservation reservation);
}

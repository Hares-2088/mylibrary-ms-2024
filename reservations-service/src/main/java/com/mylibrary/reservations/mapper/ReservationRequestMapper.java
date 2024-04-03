package com.mylibrary.reservations.mapper;


import com.mylibrary.reservations.dataacess.Reservation;
import com.mylibrary.reservations.dataacess.ReservationIdentifier;
import com.mylibrary.reservations.presentation.ReservationRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationRequestMapper {

    @Mapping(target = "id", ignore = true)
    Reservation requestModelToEntity(ReservationRequestModel reservationRequestModel, ReservationIdentifier reservationIdentifier);
}

package com.mylibrary.reservations.presentation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequestModel {

//    private String bookId;

    private Date reservationDate;
}
